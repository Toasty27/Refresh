package com.coffeestrike.refresh.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * An wrapper for the data representing a wallpaper.
 * 
 * @author Benjamin Daschel
 *
 */
public class Wallpaper implements Parcelable{
	
	public static final String ID = "id";
	public static final String PREVIEW_URL = "preview_url";
	public static final String AVAILABLE_RESOLUTIONS = "resolutions_available_array";
	public static final String TITLE = "title";
	private static final String BIG_PREVIEW_URL = "preview_url@2x";
	
	private JSONObject mWallpaper;
	private ArrayList<Resolution> mResList;
	
	public Wallpaper(JSONObject model){
		mWallpaper = model;
	}
	
	public Wallpaper(Parcel in){
		try {
			mWallpaper = new JSONObject(in.readString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mResList = in.readArrayList(null);
	}
	
	public static final Parcelable.Creator<Wallpaper> CREATOR
	    	= new Parcelable.Creator<Wallpaper>() {
		public Wallpaper createFromParcel(Parcel in) {
		    return new Wallpaper(in);
		}
		
		public Wallpaper[] newArray(int size) {
		    return new Wallpaper[size];
		}
	};
	
	
	public ArrayList<Resolution> getAvailableResolutions(){
		/*
		 * create the list of resolutions if it hasn't been already
		 */
		if(mResList == null){
			try {
				JSONArray resArray = mWallpaper.getJSONArray(AVAILABLE_RESOLUTIONS);
				mResList = new ArrayList<Resolution>();
				for(int i = 0; i< resArray.length(); i++){
					Resolution res = new Resolution(resArray.getString(i));
					mResList.add(res);
				}
				
			} catch (JSONException e) {
				return null;
			}
		}
		
		return mResList;
	}
	
	public String getBigPreviewUrl(){
		try {
			return mWallpaper.getString(BIG_PREVIEW_URL);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getPreviewUrl(){
		try {
			return mWallpaper.getString(PREVIEW_URL);
		} catch (JSONException e) {
			return null;
		}
	}

	
	public String getTitle(){
		try {
			return mWallpaper.getString(TITLE);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getWallpaperId(){
		try {
			return mWallpaper.getString(ID);
		} catch (JSONException e) {
			return null;
		}
	}


	@Override
	public int describeContents() {
		return 0;
	}


	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mWallpaper.toString());
		out.writeValue(mResList);
	}
	
	
}
