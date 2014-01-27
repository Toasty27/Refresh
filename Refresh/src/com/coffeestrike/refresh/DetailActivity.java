package com.coffeestrike.refresh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DetailActivity extends FragmentActivity {

	private DownloadCompleteReceiever mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		
//		mReceiver = new DownloadCompleteReceiever();
//		registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		
		Fragment frag = fm.findFragmentById(R.id.fragment_container);
		if(frag == null){
			frag = new DetailFragment();
			frag.setArguments(getIntent().getExtras());
			fm.beginTransaction().add(R.id.fragment_container, frag).commit();
		}
	}
	
	
	
	protected class DownloadCompleteReceiever extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			
		}

	}

}
