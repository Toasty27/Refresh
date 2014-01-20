package com.coffeestrike.refresh;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coffeestrike.refresh.api.ImageProvider;
import com.coffeestrike.refresh.api.Wallpaper;

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
	protected Wallpaper mWallpaper;
	private ImageView mUpdateView;
	private ProgressBar mProgress;
	
	private TextView mDescriptionText;

	
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
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.detail_menu, menu);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.wallpaper_detail, container, false);
		
		mUpdateView = (ImageView) v.findViewById(R.id.image_preview_big);
		mProgress = (ProgressBar) v.findViewById(R.id.progressBar1);
		mDescriptionText = (TextView) v.findViewById(R.id.description_text);
		mDescriptionText.setText(mWallpaper.getTitle());
		
		new GetImagePreviewTask().execute();
		
		
		return v;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
			case R.id.action_download:
				//TODO download the real size image
				break;
			default:
				break;
	
		}
		return true;
	};
	

}