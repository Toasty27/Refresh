package com.coffeestrike.refresh;

import android.content.Context;

public class DownloadController {
	
	private static DownloadController sInstance;
	private Context mContext;
	
	private DownloadController(Context context){
		mContext = context;
	}
	
	public DownloadController getInstance(Context c){
		if(sInstance == null){
			sInstance = new DownloadController(c);
		}
		return sInstance;
	}
	
	
	
	

}
