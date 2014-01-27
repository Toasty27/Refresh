package com.coffeestrike.refresh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

import com.coffeestrike.refresh.ImageListFragment.OnListItemSelectedListener;
import com.coffeestrike.refresh.api.Wallpaper;
import com.testflightapp.lib.TestFlight;
/*
 * Developers not using TestFlight should remove this import 
 * as well as the call to start TestFlight in onCreate()
 */

public class ImageListActivity extends FragmentActivity implements OnListItemSelectedListener {
	
	
	private FragmentManager mFragMan;
	private Fragment mListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Git cloners should feel free to create their own TestFlight API
		 * key to track bugs on their own build, or simply remover this line.
		 */
		TestFlight.takeOff(this.getApplication(), TestFlightApiKey.API_KEY);
		
		setContentView(R.layout.activity_main);
		mFragMan = getSupportFragmentManager(); 
		 
		mListFragment = mFragMan.findFragmentById(R.id.fragment_container);
		if(mListFragment == null){
			mFragMan.beginTransaction()
				.add(R.id.fragment_container, new ImageListFragment())
				.commit();
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onListItemSelected(Wallpaper w) {
		/*
		 * Start another fragment to display more information about
		 * the wallpaper.
		 */
		Bundle args = new Bundle();
		args.putParcelable(DetailFragment.EXTRA_WALLPAPER, w);
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtras(args);
		startActivity(intent);
		
		
	}

	@Override
	public void onBackPressed() {
		if(mFragMan.findFragmentById(R.id.fragment_container) != null)
		super.onBackPressed();
	}
	
	

}
