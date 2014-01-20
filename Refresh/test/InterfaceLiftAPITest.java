
import static org.junit.Assert.assertNotNull;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.coffeestrike.refresh.api.ApiProvider;
import com.coffeestrike.refresh.api.Wallpaper;

@RunWith(RobolectricTestRunner.class)
public class InterfaceLiftAPITest {

	private ApiProvider api;
	
	
	@Before
	public void setup(){
		api = new ApiProvider();
		
		
	}
	@Test
	public void test_getWallpapers(){
		assertNotNull(api.getWallpapers());
	}
	
	@Test
	public void test_getWallpapersLimits(){
		assertNotNull(api.getWallpapers(10, 0));
	}
	
	
	@Test
	public void test_getWallpaperDownload(){
		Wallpaper w = new Wallpaper(new JSONObject()){
			@Override
			public String getWallpaperId(){
				return "3474";
			}
		};
		
		assertNotNull(api.getWallpaperDownload(w, "1920x1080"));
		
	}
}