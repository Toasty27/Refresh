package com.coffeestrike.refresh.api;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * Stores and retrieves bitmaps for images.
 * @author ben
 *
 */
@Deprecated
public class ImageCache {
	
	private HashMap<String, Bitmap> mCache;
	
	public ImageCache(){
		mCache = new HashMap<String, Bitmap>();
	}
	
	public Bitmap getBitmap(Wallpaper wallpaper){
		String key = wallpaper.getWallpaperId();
		return mCache.get(key);
		
	}
	
	
	public void cacheBitmap(Wallpaper wallpaper, Bitmap bitmap){
		String key = wallpaper.getWallpaperId();
		mCache.put(key, bitmap);
	}
	
	public boolean contains(Wallpaper wallpaper){
		return mCache.containsKey(wallpaper.getWallpaperId());
	}

}
