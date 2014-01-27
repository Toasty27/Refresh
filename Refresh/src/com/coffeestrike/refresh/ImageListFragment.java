package com.coffeestrike.refresh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coffeestrike.refresh.api.ApiProvider;
import com.coffeestrike.refresh.api.ImageProvider;
import com.coffeestrike.refresh.api.Wallpaper;

/**
 * 
 * Shows a list of image previews.
 * @author Benjamin Daschel
 *
 */
public class ImageListFragment extends ListFragment {
	
	public interface OnListItemSelectedListener{
		public void onListItemSelected(Wallpaper w);
	}

	private static final String STATE_POSITION = "firstPosition";

	List<Wallpaper> mWallpaperList;
	List<GetImagePreviewTask> mTaskList;
	ImageProvider mImageProvider;
	ApiProvider mApiProvider;
	private OnListItemSelectedListener mItemSelectedListener;

	private int mLastScrollState;
	private int limit = 10;
	private int start = -10;

	private ListView mListView;

	private Button mRetryButton;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mItemSelectedListener = (OnListItemSelectedListener) activity;
		}
		catch(ClassCastException e){
			throw new ClassCastException("Activity must implement OnListItemSelectedListener.");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mImageProvider = ImageProvider.getInstance(getActivity());
		mTaskList = new ArrayList<ImageListFragment.GetImagePreviewTask>();
		mApiProvider = new ApiProvider();
		mWallpaperList = new ArrayList<Wallpaper>();


		checkNetworkAndFetchWallpapers();

	}

	private void checkNetworkAndFetchWallpapers() {
		ConnectivityManager netMan = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		if(netMan.getActiveNetworkInfo().isConnected()){
			FetchWallpapersTask fetchTask = new FetchWallpapersTask();
			fetchTask.execute();
		}
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.image_list, container, false);
//		View v =  super.onCreateView(inflater, container, savedInstanceState);
		mListView = (ListView) v.findViewById(android.R.id.list);
		mListView.setOnScrollListener(mScrollListener);
		mRetryButton = (Button) v.findViewById(R.id.btn_retry);
		mRetryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkNetworkAndFetchWallpapers();
				
			}
		});
		
		if(savedInstanceState != null){
			int position = savedInstanceState.getInt(STATE_POSITION);
			mListView.setSelection(position);
		}
		return v;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id){
		Wallpaper wallpaper = mWallpaperList.get(pos);
		mItemSelectedListener.onListItemSelected(wallpaper);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_POSITION, mLastScrollState);
	}

	class FetchWallpapersTask extends AsyncTask<Void, Void, Void>{
		
			@Override
			protected void onPreExecute(){
				/*
				 * Increment the page number for future requests
				 */
				start += 10;
			}

			@Override
			protected Void doInBackground(Void... arg0) {
				List<Wallpaper> resultsList = mApiProvider.getWallpapers(limit, start);
				for (Wallpaper w : resultsList) {
					mWallpaperList.add(w);
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void arg){
				/*
				 * update the list adapter
				 */
				if(getListAdapter() ==  null){
					setListAdapter(mAdapter);
				}
				mAdapter.notifyDataSetChanged();

			}

	};
	
	/**
	 * Handles downloading of wallpaper preview images.
	 * Updates the appropriate image view when finished
	 * and caches the downloaded image.
	 */
	protected class GetImagePreviewTask extends AsyncTask<Void, Void, Bitmap>{
		private ImageView mUpdateView;
		private Wallpaper mWallpaper;
		
		protected GetImagePreviewTask(ImageView view, Wallpaper wallpaper){
			mUpdateView = view;
			mWallpaper = wallpaper;
		}
				@Override
				protected Bitmap doInBackground(Void... args) {
					/*
					 * The real magic happens here.
					 * The image provider gets to decide whether or not
					 * to download the image or reuse a cached copy.
					 */
					Bitmap imagePreview = mImageProvider.getPreviewImage(mWallpaper);
					return imagePreview;
				}
				
				@Override
				protected void onPostExecute(Bitmap bitmap){
					if(bitmap != null){
						mUpdateView.setImageBitmap(bitmap);
					}

					/*
					 * Remove this AsyncTask instance from the "list".
					 */
					mTaskList.remove(this);
				}
		
	};
	
	
	protected BaseAdapter mAdapter = new BaseAdapter(){

		@Override
		public int getCount() {
			return mWallpaperList.size();
		}

		@Override
		public Object getItem(int position) {
			return mWallpaperList.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Wallpaper wallpaper = (Wallpaper) getItem(position);
			if(convertView == null){
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.list_item, parent, false);
			}
			
			ImageView imagePreview = (ImageView) convertView.findViewById(R.id.image_view);
			imagePreview.setImageResource(R.drawable.placeholder);
			TextView titleText = (TextView)convertView.findViewById(R.id.title_text);
			
			/*
			 * Start a task to fetch the image. 
			 */
			GetImagePreviewTask task = new GetImagePreviewTask(imagePreview, wallpaper);
			task.execute();
			mTaskList.add(task);

			titleText.setText(wallpaper.getTitle());
			
			return convertView;
		}
		
		
	};
	
	protected OnScrollListener mScrollListener = new OnScrollListener() {
		int visibleThreshold = 5;
		@Override
		public void onScrollStateChanged(AbsListView listView, int scrollState) {
			if (scrollState == SCROLL_STATE_IDLE) {
				if (listView.getLastVisiblePosition() >= listView.getCount() - visibleThreshold) {
					checkNetworkAndFetchWallpapers();
				}
			}			
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mLastScrollState = firstVisibleItem;
			
		}
	};


}
