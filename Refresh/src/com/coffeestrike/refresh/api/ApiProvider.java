package com.coffeestrike.refresh.api;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ApiProvider {

	private static final String BASE_URL = "https://interfacelift-interfacelift-wallpapers.p.mashape.com/v1";
	private static final String LIMIT = "?limit=";
	private static final String START = "&start=";
	
	
	/**
	 * Retrieves 10 wallpapers from the most recent category.
	 * @return a list containing Wallpapers
	 */
	@Deprecated
	public ArrayList<Wallpaper> getWallpapers(){
		ArrayList<Wallpaper> list = null;
		
		try {
			HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/wallpapers/")
					.header("X-Mashape-Authorization", InterfaceLiftApiKey.API_KEY_TESTING)
					.asJson();
			if(response.getCode() == 200){
				list = new ArrayList<Wallpaper>();
				JSONArray body = response.getBody().getArray();
				
				for(int i = 0; i < body.length(); i++){
					Wallpaper wallpaper = new Wallpaper(body.getJSONObject(i));
					list.add(wallpaper);
				}
				
			}
			
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	public ArrayList<Wallpaper> getWallpapers(int limit, int start){
		ArrayList<Wallpaper> list = null;
		
		try {
			HttpResponse<JsonNode> response = Unirest.get(BASE_URL + "/wallpapers/"+ LIMIT
					+ limit + START + start)
					.header("X-Mashape-Authorization", InterfaceLiftApiKey.API_KEY_TESTING)
					.asJson();
			if(response.getCode() == 200){
				list = new ArrayList<Wallpaper>();
				JSONArray body = response.getBody().getArray();
				
				for(int i = 0; i < body.length(); i++){
					Wallpaper wallpaper = new Wallpaper(body.getJSONObject(i));
					list.add(wallpaper);
				}
				
			}
			
		} catch (UnirestException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		return list;
	}
	
	public WallpaperDownload getWallpaperDownload(Wallpaper wallpaper, Resolution resolution){
		String downloadUrl = makeDownloadUrl(wallpaper, resolution.toString());
		try {
			HttpResponse<JsonNode> request = Unirest.get(downloadUrl)
					  .header("X-Mashape-Authorization", InterfaceLiftApiKey.API_KEY_TESTING)
					  .asJson();
			if(request.getCode() == 200){
				return new WallpaperDownload(request.getBody().getObject());
				
			}
			
			
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Bitmap getImage(String downloadUrl){
		try {
			HttpResponse<InputStream> request = Unirest.get(downloadUrl)
					.asBinary();
			
			InputStream iStream = request.getBody();
			Bitmap image = BitmapFactory.decodeStream(iStream);
			return image;
		} catch (UnirestException e) {
			return null;
		}
		
	}

	private String makeDownloadUrl(Wallpaper wallpaper, String resolution) {
		return BASE_URL + "/wallpaper_download" + "/" 
				+ wallpaper.getWallpaperId()+ "/" + resolution +"/";
	}
	
}
