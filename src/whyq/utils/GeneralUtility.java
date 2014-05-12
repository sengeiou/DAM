package whyq.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * User: Maziz Date: 10/1/13
 */
public class GeneralUtility {
	private static final String JAPANESE_DATE_FORMAT = "yyyy年MM月dd日 HH時mm分";

	private static final String STRING_SEPARATOR = "<//>";
	private static final String TAG = GeneralUtility.class.getName();

	public static boolean checkNetworkStatus(Context context) {
		if (context == null) {
			return true;
		}

		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) {
			return true;
		} else {
			return false;
		}
	}

	public static void hideKeyboard(FragmentActivity activity) {
		if (activity == null || activity.getCurrentFocus() == null) {
			return;
		}
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static String generateNonce() {
		String result = null;
		try {
			String uuid = generateUniversalUniqueID();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String value = uuid.substring(uuid.length() / 2);
			digest.update(value.getBytes());
			byte[] hash = digest.digest();
			result = hexString(hash);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		return result;
	}

	public static String generateState() {
		String result = null;
		try {
			String uuid = generateUniversalUniqueID();
			MessageDigest digest = MessageDigest.getInstance("MD5");
			String value = uuid.substring(0, uuid.length() / 2);
			digest.update(value.getBytes());
			byte[] hash = digest.digest();
			result = hexString(hash);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		return result;
	}

	/**
	 * This method will be called when unique Id is required
	 * 
	 * @return String of unique ID.
	 */
	public static String generateUniversalUniqueID() {
		String testUUID = UUID.randomUUID().toString();
		String UUID = testUUID.replaceAll("-", "");
		return UUID;
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	public static String getAppVersionName(Context context) {
		String appVersionName = "";
		try {
			appVersionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_META_DATA).versionName;
		} catch (NameNotFoundException e) {

		}
		return appVersionName;
	}

	public static String getStringFromInputStream(InputStream mInputStream) {
		String stringObtained = "";
		try {
			int length = mInputStream.available();
			byte[] data = new byte[length];
			mInputStream.read(data);
			stringObtained = new String(data);
			mInputStream.close();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}

		return stringObtained;
	}

	public static String hexString(byte[] bin) {
		String s = "";
		int size = bin.length;
		for (int i = 0; i < size; i++) {
			int n = bin[i];
			if (n < 0) {
				n += 256;
			}
			String hex = Integer.toHexString(n);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			s += hex;
		}
		return s;
	}

	public static void showErrorDialog(Context context, String title,
			String message, String btnText) {
		final AlertDialog dialog = new Builder(context).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, btnText,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	public static void showSimpleDialog(Activity act, String title,
			String message, String buttonTitle) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setTitle(title);
		builder.setMessage(message);
		AlertDialog dialog = builder.create();
		builder.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, buttonTitle,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();

	}

	public static boolean isInternetAvailable(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

}
