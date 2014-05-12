//package whyq.utils.gcm;
//
//import java.io.IOException;
//
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//public class GCMManager {
//	public static interface GCMRegistratrionListener {
//		public void onGCMRegistratrionComplete(String msg);
//	}
//
//	private static final String TAG = "GCMManager";
//
//	/**
//	 * @return Application's {@code SharedPreferences}.
//	 */
//	public static SharedPreferences getGCMPreferences(Context context) {
//		// This sample app persists the registration ID in shared preferences,
//		// but
//		// how you store the regID in your app is up to you.
//		return context.getSharedPreferences(YHOMainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
//	}
//
//	/**
//	 * Gets the current registration ID for application on GCM service.
//	 * <p>
//	 * If result is empty, the app needs to register.
//	 * 
//	 * @return registration ID, or empty string if there is no existing
//	 *         registration ID.
//	 */
//	public static String getRegistrationId(Context context) {
//		final SharedPreferences prefs = getGCMPreferences(context);
//		String registrationId = prefs.getString(GCMConstant.PROPERTY_REG_ID, "");
//		if (registrationId.isEmpty()) {
//			Log.i(TAG, "Registration not found.");
//			return "";
//		}
//		// Check if app was updated; if so, it must clear the registration ID
//		// since the existing regID is not guaranteed to work with the new
//		// app version.
//		int registeredVersion = prefs.getInt(GCMConstant.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//		int currentVersion = GeneralUtility.getAppVersion(context);
//		if (registeredVersion != currentVersion) {
//			Log.i(TAG, "App version changed.");
//			return "";
//		}
//		return registrationId;
//	}
//
//	/**
//	 * Registers the application with GCM servers asynchronously.
//	 * <p>
//	 * Stores the registration ID and app versionCode in the application's
//	 * shared preferences.
//	 */
//	public static void registerInBackground(final Context context, final GCMRegistratrionListener listener) {
//		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
//
//			@Override
//			protected String doInBackground(Void... params) {
//				String msg;
//				try {
//					GoogleCloudMessaging gcm = null;
//					if (gcm == null) {
//						gcm = GoogleCloudMessaging.getInstance(context);
//					}
//					String regid = gcm.register(GCMConstant.SENDER_ID);
//					msg = regid;
//					Log.d(TAG, "ID: "+msg);
//					// Persist the regID - no need to register again.
//					storeRegistrationId(context, regid);
//				} catch (IOException ex) {
//					msg = "Error :" + ex.getMessage();
//					// If there is an error, don't just keep trying to register.
//					// Require the user to click a button again, or perform
//					// exponential back-off.
//				}
//				return msg;
//			}
//
//			@Override
//			protected void onPostExecute(String msg) {
//				if (listener != null) {
//					listener.onGCMRegistratrionComplete(msg);
//				}
//			}
//		};
//		task.execute(null, null, null);
//	}
//
//	/**
//	 * Stores the registration ID and app versionCode in the application's
//	 * {@code SharedPreferences}.
//	 * 
//	 * @param context
//	 *            application's context.
//	 * @param regId
//	 *            registration ID
//	 */
//	private static void storeRegistrationId(Context context, String regId) {
//		final SharedPreferences prefs = getGCMPreferences(context);
//		int appVersion = GeneralUtility.getAppVersion(context);
//		Log.i(TAG, "Saving regId on app version " + appVersion);
//		SharedPreferences.Editor editor = prefs.edit();
//		editor.putString(GCMConstant.PROPERTY_REG_ID, regId);
//		editor.putInt(GCMConstant.PROPERTY_APP_VERSION, appVersion);
//		editor.commit();
//	}
//	
//	public static void removeRegistrationId(Context context){
//		final SharedPreferences prefs = getGCMPreferences(context);
//		SharedPreferences.Editor editor = prefs.edit();
//		editor.remove(GCMConstant.PROPERTY_REG_ID);
//		editor.commit();
//	}
//}
