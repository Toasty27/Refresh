package com.coffeestrike.refresh.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Provides images for the app.
 * Dynamically handles the caching and downloading 
 * of images to save bandwidth and improve app responsiveness.
 * @author Benjamin Daschel
 *
 */
public class ImageProvider {

	private static ImageProvider sImageProviderInstance;

	private LruCache<Wallpaper, Bitmap> mImagePreviewCache;
	private StorageProvider mStorageProvider;
	private Context mAppContext;
	
	protected ImageProvider(Context context){
		mAppContext = context.getApplicationContext();
		int cacheSize = 4 * 1024 * 1024;
		mImagePreviewCache = new LruCache<Wallpaper, Bitmap>(cacheSize);
		mStorageProvider = new StorageProvider(context);
	}
	
	public static ImageProvider getInstance(Context context){
		if(sImageProviderInstance == null){
			sImageProviderInstance = new ImageProvider(context);
		}
		return sImageProviderInstance;
	}
	
	
	public Bitmap getPreviewImage(Wallpaper wallpaper){
		Bitmap image = null;
		/*
		 * Does the cache in memory contain the image?
		 */
		if((image = mImagePreviewCache.get(wallpaper)) != null);
		/*
		 * Does the cache on local storage contain the image?
		 */
		else if((image = mStorageProvider.getPreviewBitmap(wallpaper)) != null){
			mImagePreviewCache.put(wallpaper, image);
			return image;
		}
		/*
		 * 
		 */
		else{
			ApiProvider api = new ApiProvider();
			image = api.getImage(wallpaper.getPreviewUrl());
			/*
			 * Sometimes the preview might be a 404 page, so 
			 * we need to get the larger preview instead.
			 */
			if(image == null){
				image = api.getImage(wallpaper.getBigPreviewUrl());
			}
			/*
			 * If we had to download it, we should also write 
			 * it to external storage
			 */
			mStorageProvider.savePreviewBitmap(wallpaper, image,
					StorageProvider.FLAG_NO_OVERWRITE);
			mImagePreviewCache.put(wallpaper, image);
		}

		return image;
	}

	public Bitmap getLargePreview(Wallpaper wallpaper) {
		Bitmap image = null;
		
		if( (image = mStorageProvider.getBigPreviewBitmap(wallpaper)) != null){
			return image;
		}
		else{
			image = new ApiProvider().getImage(wallpaper.getBigPreviewUrl());
			mStorageProvider.saveBigPreviewBitmap(wallpaper, image, StorageProvider.FLAG_NO_OVERWRITE);
		}
		
		
		return image;
	}
	
}
