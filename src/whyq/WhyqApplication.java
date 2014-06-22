/**
 * 
 */
package whyq;

import java.util.Calendar;
import java.util.TimeZone;

import whyq.model.User;
import whyq.service.img.good.ImageLoader;
import whyq.service.pushnotification.AlarmReceiver;
import whyq.utils.RSA;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Linh Nguyen This class is to store the global information (data
 *         objects, variable) during the Permping application is running
 */
public class WhyqApplication extends Application {
	/**
	 * The perm user stored in application context
	 */
	private User user;
	public static final float RATIO_BASE_HEIGHT = 0.08f;
	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFESH_TOKEN = "refresh_token";
	public static final String TOKEN_EXPIRED_TIME = "expires_in";
	public static String DEVICE_ID = "noID";
	public static final String DISK_CACHE_DIR = "images";
	public static int sScreenWidth = 0;
	public static int sScreenHeight;
	public static int sBaseViewHeight;
	public static int sBaseViewPadding;
	public static Typeface sTypefaceRegular;
	public static Typeface sTypefaceBold;
	public static Typeface sTypefaceItalic;
	private Location currentLocation;

	/**
	 * The current login type
	 */
	private String loginType;

	public static WhyqApplication _instance;

	public WhyqApplication() {
		super();
		_instance = this;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		sTypefaceRegular = Typeface.createFromAsset(getAssets(),
				"Roboto-Regular.ttf");
		sTypefaceBold = Typeface
				.createFromAsset(getAssets(), "Roboto-Bold.ttf");
		sTypefaceItalic = Typeface.createFromAsset(getAssets(),
				"Roboto-Italic.ttf");
	}

	public static void initScreenSize(int width, int height) {
		if (sScreenWidth == 0) {
			sScreenHeight = height;
			sScreenWidth = width;
			sBaseViewHeight = (int) (sScreenHeight * RATIO_BASE_HEIGHT);
		}
	}

	public static WhyqApplication Instance() {
		return _instance;
	}

	public float getDensity() {
		return getApplicationContext().getResources().getDisplayMetrics().density;
	}

	/*
	 * Current screen info
	 */
	public static String getRSAToken() {

		try {
			// DAM token
			Log.d("token",
					XMLParser.getToken(Instance().getApplicationContext()));
			return XMLParser.getToken(Instance().getApplicationContext());
			// WHYQ token
			// RSA rsa = new RSA();
			// String token =
			// rsa.RSAEncrypt(XMLParser.getToken(Instance().getApplicationContext()));
			// return token;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	// public String getToken() {
	// SharedPreferences pref = PreferenceManager
	// .getDefaultSharedPreferences(getApplicationContext());
	// long lastSaved = pref.getLong("token_save_at", 0);
	// int expireTime = pref.getInt(TOKEN_EXPIRED_TIME, 3600);
	// if ((System.currentTimeMillis() - lastSaved) / 1000 < expireTime) {
	// return pref.getString(ACCESS_TOKEN, null);
	// } else {
	// Editor editor = pref.edit();
	// editor.remove(ACCESS_TOKEN);
	// editor.remove(TOKEN_EXPIRED_TIME);
	// editor.remove("token_save_at");
	// editor.commit();
	// return null;
	// }
	// }
	public void setToken(User user) {
		XMLParser.storeUserAccount(getApplicationContext(), user);
	}

	public void clearToken() {
		XMLParser.clearToken(getApplicationContext());
	}

	private DisplayMetrics metrics;
	private ImageLoader imageLoader;

	public void setDisplayMetrics(DisplayMetrics metrics) {
		this.metrics = metrics;
	}

	public DisplayMetrics getDisplayMetrics() {
		return this.metrics;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		// return new User("121");
		return user;

	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the loginType
	 */
	public String getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType
	 *            the loginType to set
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getDeviceID() {
		// TelephonyManager tManager = (TelephonyManager)
		// this.getSystemService(Context.TELEPHONY_SERVICE);
		if (Util.generateDeviceId() != null)
			DEVICE_ID = Util.generateDeviceId();
		return DEVICE_ID;
	}

	/*
	 * 
	 * private static Context context; public void onCreate(){
	 * PermpingApplication.context = getApplicationContext(); }
	 * 
	 * public static Context getAppContext(){ return
	 * PermpingApplication.context; }
	 */

	public void savePassword(String string) {
		// TODO Auto-generated method stub

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = pref.edit();
		editor.putString("password", string);
		editor.commit();
	}

	public String getPassword() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		return pref.getString("password", null);
	}

	public Location getCurrentLocation() {
		// TODO Auto-generated method stub
		if (currentLocation != null) {

		} else {

		}
		return currentLocation;
	}

	public void setCurrentLocation(Location location) {
		// TODO Auto-generated method stub

		this.currentLocation = location;
	}

	public ImageLoader getImageLoader() {
		if (imageLoader == null)
			imageLoader = new ImageLoader(getApplicationContext());
		return imageLoader;
	}

	public static void pushNotification(Context ctx, long time, String title,
			String message) {
		Intent intentAlarm = new Intent(ctx, AlarmReceiver.class);
		intentAlarm.putExtra("title", title);
		intentAlarm.putExtra("message", ""+message);
		
//		TimeZone tz = TimeZone.getTimeZone("GMT+07:00");
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		
		Log.d("pushNotification","compare time: "+ (time - cal.getTimeInMillis())/(60*60*1000));
		if (time <= cal.getTimeInMillis() + 15 * 60 * 1000)
			time = time + 60 * 1000;
		else if (time - 15 * 60 * 1000 >= cal.getTimeInMillis())
			// notify before 15'
			time = time - 15 * 60 * 1000;

		Log.d("pushNotification","pushNotification time: "+time+" current time: "+cal.getTimeInMillis());
		// create the object
		AlarmManager alarmManager = (AlarmManager) ctx
				.getSystemService(ALARM_SERVICE);
		// set the alarm for particular time
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent
				.getBroadcast(ctx, 1, intentAlarm,
						PendingIntent.FLAG_UPDATE_CURRENT));
	}

}