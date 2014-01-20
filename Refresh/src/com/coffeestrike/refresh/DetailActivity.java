package com.coffeestrike.refresh;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class DetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager fm = getSupportFragmentManager();
		DetailFragment frag = new DetailFragment();
		frag.setArguments(getIntent().getExtras());
		fm.beginTransaction().add(R.id.fragment_container, frag).commit();
	}
	
	

}
