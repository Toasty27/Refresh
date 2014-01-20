package com.coffeestrike.refresh.api;

import android.os.Parcel;
import android.os.Parcelable;

public class Resolution implements Parcelable{
	String resolution;
	
	public static final Parcelable.Creator<Resolution> CREATOR
		= new Parcelable.Creator<Resolution>() {

		@Override
		public Resolution createFromParcel(Parcel arg0) {
			return new Resolution(arg0);
		}

		@Override
		public Resolution[] newArray(int arg0) {
			return new Resolution[arg0];
		}
		
	};
	
	public Resolution(Parcel in){
		this.resolution = in.readString();
	}
	
	/**
	 * Expects a String in the format {width}x{height}
	 * @param resolution
	 */
	public Resolution(String resolution){
		this.resolution = resolution;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public int getHeight(){
		return Integer.parseInt(resolution.split("x")[1]);
	}

	public int getWidth(){
		return Integer.parseInt(resolution.split("x")[0]);
	}
	
	public String toString(){
		return resolution;
	}
	
	public int totalPixels(){
		return getWidth() * getHeight();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(resolution);
	}
	
}