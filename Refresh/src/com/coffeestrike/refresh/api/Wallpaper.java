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
	
	private static final String ID = "id";
	private static final String PREVIEW_URL = "preview_url";
	private static final String AVAILABLE_RESOLUTIONS = "resolutions_available_array";
	private static final String TITLE = "title";
	private static final String BIG_PREVIEW_URL = "preview_url@2x";
	private static final String USER_NAME = "user_name";
	private static final String DESCRIPTION = "description";
	private static final String URL_IFL = "url_ifl";
	
	private JSONObject mData;
	private ArrayList<Resolution> mResList;
	private long mDownloadId;
	
	public Wallpaper(JSONObject model){
		mData = model;
	}
	
	public Wallpaper(Parcel in){
		try {
			mData = new JSONObject(in.readString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mResList = in.readArrayList(null);
		mDownloadId = in.readLong();
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
				JSONArray resArray = mData.getJSONArray(AVAILABLE_RESOLUTIONS);
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
			return mData.getString(BIG_PREVIEW_URL);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getPreviewUrl(){
		try {
			return mData.getString(PREVIEW_URL);
		} catch (JSONException e) {
			return null;
		}
	}

	
	public String getTitle(){
		try {
			return mData.getString(TITLE);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getWallpaperId(){
		try {
			return mData.getString(ID);
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
		out.writeString(mData.toString());
		out.writeValue(mResList);
		out.writeLong(mDownloadId);
	}

	public String getArtistName() {
		try {
			return mData.getString(USER_NAME);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getDescription(){
		try {
			return mData.getString(DESCRIPTION);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public String getIflUrl(){
		try {
			return mData.getString(URL_IFL);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public long getDownloadId(){
		return mDownloadId;
	}
	
	public void setDownloadId(long downloadId){
		mDownloadId = downloadId;
	}
	
	@Override 
	public int hashCode(){
		return getWallpaperId().hashCode();
	}
	
}
