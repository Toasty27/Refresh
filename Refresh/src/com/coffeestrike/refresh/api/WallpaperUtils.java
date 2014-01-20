package com.coffeestrike.refresh.api;

import java.util.List;

public class WallpaperUtils {

	
	public static Resolution bestAvailableRes(List<Resolution> resList, int targetWidth, int targetHeight){
		int targetPixels = targetWidth * targetHeight;
		
		Resolution bestRes = resList.get(0);
		
		for(Resolution r: resList){
			if(Math.abs(r.totalPixels() - targetPixels) < Math.abs(bestRes.totalPixels() - targetPixels)){
				bestRes = r;
			}
		}
		
		return bestRes;

	}

}
