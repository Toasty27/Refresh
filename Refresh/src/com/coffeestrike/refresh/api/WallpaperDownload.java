package com.coffeestrike.refresh.api;

import org.json.JSONException;
import org.json.JSONObject;

public class WallpaperDownload {
	
	public static final String DOWNLOAD_URL = "download_url";
	public static final String FILENAME = "filename";
	
	
	private JSONObject mData;
	
	WallpaperDownload(JSONObject data){
		mData = data;
		
	}
	
	public String getDownloadUrl(){
		try {
			return mData.getString(DOWNLOAD_URL);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getFileName(){
		try {
			return mData.getString(FILENAME);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String toString(){
		return getDownloadUrl();
	}

}
