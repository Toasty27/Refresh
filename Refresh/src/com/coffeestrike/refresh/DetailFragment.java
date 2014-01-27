package com.coffeestrike.refresh;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coffeestrike.refresh.api.ApiProvider;
import com.coffeestrike.refresh.api.ImageProvider;
import com.coffeestrike.refresh.api.Resolution;
import com.coffeestrike.refresh.api.Wallpaper;
import com.coffeestrike.refresh.api.WallpaperDownload;
import com.coffeestrike.refresh.api.WallpaperUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class DetailFragment extends Fragment {
	
	protected class GetImagePreviewTask extends AsyncTask<Void, Void, Bitmap>{
		
			@Override
			protected Bitmap doInBackground(Void... args) {
				/*
				 * The real magic happens here.
				 * The image provider gets to decide whether or not
				 * to download the image or reuse a cached copy.
				 */
				Bitmap imagePreview = ImageProvider.getInstance(getActivity())
						.getLargePreview(mWallpaper);
				return imagePreview;
			}
			
			@Override
			protected void onPostExecute(Bitmap bitmap){
				if(bitmap != null){
					bitmap = Bitmap.createScaledBitmap(bitmap,
							 bitmap.getWidth() *2,bitmap.getHeight()*2, false);
					mUpdateView.setImageBitmap(bitmap);
					mProgress.setVisibility(View.GONE);
				}
			}
	}
	

	
	public static final String EXTRA_WALLPAPER = "wallpaper";
	private static final String TAG = "DetailFragment";
	protected Wallpaper mWallpaper;
	private ImageView mUpdateView;
	private ProgressBar mProgress;
	
	private TextView mTitleText;
	private SlidingUpPanelLayout mLayout;
	private TextView mArtistText;
	private TextView mInfoText;
	private TextView mLinkText;
	private AsyncTask<Void, Void, WallpaperDownload> mImageDownload;

	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		setHasOptionsMenu(true);
		Bundle args = getArguments();
		mWallpaper = args.getParcelable(EXTRA_WALLPAPER);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.detail_menu, menu);
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.wallpaper_detail_with_slider, container, false);
		mLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
		mLayout.setShadowDrawable(getResources().getDrawable(com.sothree.slidinguppanel.library.R.drawable.above_shadow));
		mLayout.setAnchorPoint(0.3f);
		
		mUpdateView = (ImageView) v.findViewById(R.id.image_preview_big);
		mProgress = (ProgressBar) v.findViewById(R.id.progressBar1);

		mTitleText = (TextView) v.findViewById(R.id.image_title_text);
		mTitleText.setText(mWallpaper.getTitle());
		
		mLayout.setDragView(mTitleText);
		
		mArtistText = (TextView) v.findViewById(R.id.image_artist_text);
		mArtistText.setText("Artist: "+ mWallpaper.getArtistName());
		
		mInfoText = (TextView) v.findViewById(R.id.image_info_text);
		mInfoText.setText(mWallpaper.getDescription());
		
		mLinkText = (TextView) v.findViewById(R.id.image_ifl_link);
		mLinkText.setText(mWallpaper.getIflUrl());
		mLinkText.setClickable(true);
		Linkify.addLinks(mLinkText, Linkify.WEB_URLS);
		mLinkText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchBrowser(mWallpaper.getIflUrl());
            }
        });
		
		

		
		return v;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		new GetImagePreviewTask().execute();
		
	}
	
	private void launchBrowser(String url) {
		Uri link = Uri.parse(url);
		Intent webIntent = new Intent(Intent.ACTION_VIEW, link);
		PackageManager pm = getActivity().getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(webIntent, 0);
		boolean isIntentSafe = activities.size() > 0;
		if(isIntentSafe){
			startActivity(webIntent);
		}
	}
	
	
	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
			case R.id.action_download:
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				getActivity().invalidateOptionsMenu();
//			}
				if(mImageDownload == null){
					pickBestResAndDownload();
				}
				else{
					Toast.makeText(getActivity(), "Already downloading", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
	
		}
		return true;
	}

	private void pickBestResAndDownload() {
		/*
		 * Pick the resolution closest to the screen size of the
		 * device and download it.
		 * 
		 */
		WallpaperManager wm = WallpaperManager.getInstance(getActivity());
		int width = wm.getDesiredMinimumWidth();
		int height = wm.getDesiredMinimumHeight();
		Log.i(TAG, "Width desired: "+width +"\nHeight desired: "+height);
		Resolution bestRes = WallpaperUtils.bestAvailableRes(mWallpaper.getAvailableResolutions(),
				width, height);
		Log.i(TAG, "Best res:"+ bestRes.toString());
		
		mImageDownload = new ImageDownloadTask(mWallpaper, bestRes).execute();
		
	}
	
	private class ImageDownloadTask extends AsyncTask<Void, Void, WallpaperDownload>{
		
		private Wallpaper wallpaper;
		private Resolution res;

		protected ImageDownloadTask(Wallpaper wallpaper, Resolution res){
			this.wallpaper = wallpaper;
			this.res = res;
		}
		
		@Override
		protected WallpaperDownload doInBackground(Void... arg0) {
			ApiProvider api = new ApiProvider();
			WallpaperDownload download = api.getWallpaperDownload(wallpaper, res);
			Log.d(TAG, download.toString());
			return download;
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		protected void onPostExecute(WallpaperDownload download){
			Uri uri = Uri.parse(download.getDownloadUrl());
			DownloadManager.Request request = new DownloadManager.Request(uri);
			request.setTitle(this.wallpaper.getTitle())
				.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES 
						,download.getFileName());
			
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
				request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				request.allowScanningByMediaScanner();
			}
			
			DownloadManager dm = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
			
			dm.enqueue(request);
			
		}
		
		
	}
	

}
