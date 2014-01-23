package com.coffeestrike.refresh.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class StorageProvider{
	
	public static final int FLAG_NO_OVERWRITE = 0;
	
	public static final String DIR_DOWNLOADS = "Refresh";
	
	private Context mContext;

	public StorageProvider(Context context){
		mContext = context;
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public Bitmap getPreviewBitmap(Wallpaper wallpaper){
		String filename = wallpaper.getWallpaperId();
		
		File dir = mContext.getExternalFilesDir(null);
		File imageFile = new File(dir, filename);
		if(imageFile.exists()){
			InputStream iStream;
			try {
				iStream = new FileInputStream(imageFile);
				return BitmapFactory.decodeStream(iStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return null;
		
	}
	

	public Bitmap getBigPreviewBitmap(Wallpaper wallpaper){
		String filename = wallpaper.getWallpaperId()+ "2x";
		
		File dir = mContext.getExternalFilesDir(null);
		File imageFile = new File(dir, filename);
		if(imageFile.exists()){
			InputStream iStream;
			try {
				iStream = new FileInputStream(imageFile);
				return BitmapFactory.decodeStream(iStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return null;
		
	}
	
	public void saveBigPreviewBitmap(Wallpaper wallpaper, Bitmap image, int ... flags){
		savePreviewBitmap(wallpaper.getWallpaperId()+"2x", image, flags);
	}
	
	public void savePreviewBitmap(Wallpaper wallpaper, Bitmap image, int ... flags){
		savePreviewBitmap(wallpaper.getWallpaperId(), image, flags);
	}
	
	private void savePreviewBitmap(String filename, Bitmap image, int ... flags){

		File dir = mContext.getExternalFilesDir(null);
		File imageFile = new File(dir, filename);
//		ObjectOutputStream outStream = null;
		FileOutputStream fout = null;
		/*
		 * Don't overwrite existing files if the caller specifies against it
		 */
		if(flags != null && flags[0] == FLAG_NO_OVERWRITE && imageFile.exists()){
			return;
		}
		
		try{
			fout = new FileOutputStream(imageFile);
			image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if(fout != null){
				try {
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
