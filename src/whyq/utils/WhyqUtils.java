/**
 * 
 */
package whyq.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.whyq.R;

import twitter4j.http.AccessToken;
import whyq.WhyqApplication;
import whyq.activity.ListActivity;
import whyq.activity.NewWhyqActivity;
import whyq.model.User;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @author Linh Nguyen
 * 
 */
public class WhyqUtils {
	// Write log file
	public final static String LINE_SEPARATOR = System
			.getProperty("line.separator");//$NON-NLS-1$
	public final static String TAG = "AndroidLogCollector";//$NON-NLS-1$
	final int MAX_LOG_MESSAGE_LENGTH = 100000;
	private String mAdditonalInfo;
	public static final String EXTRA_FILTER_SPECS = "com.permpings.activity.intent.extra.FILTER_SPECS";//$NON-NLS-1$
	public static final String EXTRA_FORMAT = "com.permpings.activity.intent.extra.FORMAT";//$NON-NLS-1$
	public static final String EXTRA_BUFFER = "com.permpings.activity.intent.extra.BUFFER";//$NON-NLS-1$
	public static final String ACTION_SEND_LOG = "com.permpings.activity.intent.action.SEND_LOG";//$NON-NLS-1$
	private String[] mFilterSpecs;
	private String mFormat;
	private String mBuffer;
	public static boolean isFollowRereshData = true;
	public static User isAuthenticated(Context context) {
		WhyqApplication state = (WhyqApplication) context
				.getApplicationContext();
		if (state != null) {
			User user = state.getUser();
			return user;
		}

		return null;
	}

	public static void clearViewHistory() {
		/*
		 * ExplorerActivityGroup.group.clearHistory();
		 * FollowerActivityGroup.group.clearHistory();
		 * ImageActivityGroup.group.clearHistory();
		 * ProfileActivityGroup.group.clearHistory();
		 * MyDiaryActivityGroup.group.clearHistory();
		 */
	}

	/**
	 * calculate image size
	 */
	public static void calculateImageSize(Context context) {
		WhyqApplication state = (WhyqApplication) context;
		if (state != null) {
			DisplayMetrics metrics = state.getDisplayMetrics();
			int screenWidth = metrics.widthPixels;
			int screenHeight = metrics.heightPixels;

		}
		// return null;
	}

	/**
	 * Scale the ImageView based on the screen's resolution
	 * 
	 * @param imageView
	 *            the ImageView object
	 * @param screenWidth
	 *            the screen'width which the view is displayed.
	 * @param screenHeight
	 *            the screen's height which the view is displayed.
	 */
	public static void scaleImage(ImageView view, int screenWidth,
			int screenHeight) {
		// Get the ImageView and its bitmap
		Drawable drawing = view.getDrawable();
		if (drawing != null) {
			Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();
			if (bitmap != null) {
				// Get current dimensions
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				// float boundBoxInDp = 500;
				float desiredWidth = 500;
				float desiredHeight = 500;
				if (width <= 560) { // image width <= 560 pixel
					if (screenWidth == 320 && screenHeight == 480) {
						desiredWidth = ((float) width / 560) * 304;
						desiredHeight = ((float) height / 560) * 304;
					} else if (screenWidth == 480 && screenHeight == 800) {
						desiredWidth = ((float) width / 560) * 456;
						desiredHeight = ((float) height / 560) * 456;
					} else if (screenWidth == 800 && screenHeight == 1280) {
						desiredWidth = ((float) width / 560) * 760;
						desiredHeight = ((float) height / 560) * 760;
					} else { // Other resolution

					}
				} else { // image width > 560 pixel
					if (screenWidth == 320 && screenHeight == 480) {
						desiredWidth = 304;
						desiredHeight = (float) height * 304 / (float) width;
					} else if (screenWidth == 480 && screenHeight == 800) {
						desiredWidth = 456;
						desiredHeight = (float) height * 456 / (float) width;
					} else if (screenWidth == 800 && screenHeight == 1280) {
						desiredWidth = 760;
						desiredHeight = (float) height * 760 / (float) width;
					} else { // Other resolution

					}
				}
				// Determine how much to scale: the dimension requiring less
				// scaling is
				// closer to the its side. This way the image always stays
				// inside your
				// bounding box AND either x/y axis touches it.
				float xScale = ((float) desiredWidth) / width;
				float yScale = (float) desiredHeight / height;
				float scale = (xScale <= yScale) ? xScale : yScale;

				// Create a matrix for the scaling and add the scaling data
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);

				// Create a new bitmap and convert it to a format understood by
				// the ImageView
				Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
						height, matrix, true);

				BitmapDrawable result = new BitmapDrawable(scaledBitmap);

				width = scaledBitmap.getWidth();
				height = scaledBitmap.getHeight();

				// Apply the scaled bitmap
				view.setImageDrawable(result);

				// Now change ImageView's dimensions to match the scaled image
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
						.getLayoutParams();
				params.width = width;
				params.height = height;
				view.setLayoutParams(params);

				unbindDrawables(view);
				System.gc();
				Runtime.getRuntime().gc();

			}

		}

	}

	/**
	 * 
	 * @param view
	 */
	private static void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	/**
	 * Check if the text should be wrapped (when the text's length is greater
	 * than the screen's width.
	 * 
	 * @param text
	 *            the text.
	 * @return true if text will be wrapped. Else, it returns false.
	 */
	public static boolean isTextWrapped(Activity activity, String text,
			Context context) {
		boolean isWrapped = false;
		int textWidth = 0;
		int deviceWidth = 0;

		// Calculate the width of text
		TextView textView = new TextView(context);
		textView.setVisibility(View.GONE);
		Rect bounds = new Rect();
		Paint textPaint = textView.getPaint();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		textWidth = bounds.width();

		// Calculate the width of screen
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		deviceWidth = displaymetrics.widthPixels;

		isWrapped = textWidth > deviceWidth ? true : false;

		return isWrapped;
	}

	public static Bitmap LoadImage(String URL, BitmapFactory.Options options) {
		Bitmap bitmap = null;
		InputStream in = null;
		try {
			in = OpenHttpConnection(URL);
			bitmap = BitmapFactory.decodeStream(in, null, options);
			if (in != null)
				in.close();
		} catch (IOException e1) {
		}
		return bitmap;
	}

	public static InputStream OpenHttpConnection(String strURL)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(strURL);
		URLConnection conn = url.openConnection();

		try {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpConn.getInputStream();
			}
		} catch (Exception ex) {
		}
		return inputStream;
	}

	public static Bitmap scaleBitmap(String url) {
		try {
			Bitmap bm = null;
			BitmapFactory.Options bmOptions;
			bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;
			bmOptions.inJustDecodeBounds = false;
			Bitmap bitmp = LoadImage(url, bmOptions);
			if (bitmp != null) {
				int height = bitmp.getHeight();
				int width = bitmp.getWidth();
				int deviceSizeHeight = ListActivity.screenHeight;
				int deviceSizeWidth = ListActivity.screenWidth;
				int imgWidth = 0, imgHeight = 0;
				if (deviceSizeWidth <= 320) { // 320 x 480
					// marginLeft = 8;
					// marginRight = 8;
					if (width < 560) {
						imgWidth = (width * 304 / 560);
						imgHeight = (height * 304 / 560);
					} else {
						imgWidth = 304;
						imgHeight = (height * 304) / width;
					}
				} else if (deviceSizeWidth <= 480) { // 480 x 800
					// marginLeft = 12;
					// marginRight = 12;
					if (width < 560) {
						imgWidth = (width * 456 / 560);
						imgHeight = (height * 456 / 560);
					} else {
						imgWidth = 456;
						imgHeight = (height * 456) / width;
					}
				} else if (deviceSizeWidth <= 800) { // 800 x 1280
					// marginLeft = 20;
					// marginRight = 20;
					if (width < 560) {
						imgWidth = (width * 760 / 560);
						imgHeight = (height * 760 / 560);
					} else {
						imgWidth = 760;
						imgHeight = (height * 760) / width;
					}
				}
				// bm = Bitmap.createScaledBitmap(bitmp, imgWidth*6,
				// imgHeight*6, false);
			}
			return bm;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	// Login facebook get token
	public String integateLoginFacebook(final Activity activity,
			final Handler handleFbLogin) {
		final SharedPreferences prefs;
		Facebook mFacebook;
		String token = null;
		mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
		mFacebook.authorize(activity, new String[] { "email", "status_update",
				"user_birthday" }, new DialogListener() {
			@Override
			public void onComplete(Bundle values) {
				//Log.d("", "=====>" + values.toString());
				String accessToken = values.getString("access_token");
				saveFacebookToken("oauth_token", accessToken, activity);
				Message message = handleFbLogin.obtainMessage(
						NewWhyqActivity.LOGIN_FACEBOOK, "");
				handleFbLogin.sendMessage(message);
			}

			@Override
			public void onFacebookError(FacebookError error) {
				Message message = handleFbLogin.obtainMessage(
						NewWhyqActivity.MESSAGE_LOGIN_FACEBOOK_ERROR, "");
				handleFbLogin.sendMessage(message);
			}

			@Override
			public void onError(DialogError e) {
				Message message = handleFbLogin.obtainMessage(
						NewWhyqActivity.MESSAGE_LOGIN_FACEBOOK_ERROR, "");
				handleFbLogin.sendMessage(message);
			}

			@Override
			public void onCancel() {
				// cancel press or back press
				Message message = handleFbLogin.obtainMessage(
						NewWhyqActivity.MESSAGE_LOGIN_FACEBOOK_ERROR, "");
				handleFbLogin.sendMessage(message);
			}
		});
		return getFacebookToken(activity);
	}

	public boolean saveFacebookToken(String key, String value, Context activity) {
		Editor editor = activity.getSharedPreferences("FACEBOOK",
				Context.MODE_PRIVATE).edit();
		if (value != null)
			editor.putString("fb_token", value);

		return editor.commit();
	}

	public String getFacebookToken(Context activity) {
		SharedPreferences savedSession = activity.getSharedPreferences(
				"FACEBOOK", Context.MODE_PRIVATE);
		String key = savedSession.getString("fb_token", "");
		if (key.equals("")) {
			return null;
		}
		return key;
	}

	public boolean saveTwitterAccess(String key, AccessToken value,
			Context activity) {
		Editor editor = activity.getSharedPreferences("TWITTER",
				Context.MODE_PRIVATE).edit();
		if (value.getToken() != null)
			editor.putString("twitter_key", value.getToken());
		if (value.getTokenSecret() != null)
			editor.putString("twitter_secret", value.getTokenSecret());
		return editor.commit();
	}

	public AccessToken getTwitterAccess(Context activity) {
		SharedPreferences savedSession = activity.getSharedPreferences(
				"TWITTER", Context.MODE_PRIVATE);
		String key = savedSession.getString("twitter_key", "");
		String secret = savedSession.getString("twitter_secret", "");
		if (key == "" || secret == "" || key == null || secret == null) {
			return null;
		}
		return new AccessToken(key, secret);
	}

	public boolean logOutFacebook(Activity activity) {
		String result = "";
		try {
			Facebook mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
			result = mFacebook.logout(activity.getApplicationContext());
			saveFacebookToken("oauth_token", "", activity);
			if (result.equals("true"))
				return true;
			else
				return false;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

	}

	public void logOutTwitter(Context context) {
		// TODO Auto-generated method stub
		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		saveTwitterAccess("twitter_access", new AccessToken("", ""), context);
	}

	@SuppressWarnings("unchecked")
	public void writeLogFile(Intent intent) {
		// Intent intent = getIntent();
		if (null != intent) {
			String action = intent.getAction();
//			if (ACTION_SEND_LOG.equals(action))
			{
				mFilterSpecs = intent.getStringArrayExtra(EXTRA_FILTER_SPECS);
				mFormat = intent.getStringExtra(EXTRA_FORMAT);
				mBuffer = intent.getStringExtra(EXTRA_BUFFER);
				ArrayList<String> list = new ArrayList<String>();

				if (mFormat != null) {
					list.add("-v");
					list.add(mFormat);
				}

				if (mBuffer != null) {
					list.add("-b");
					list.add(mBuffer);
				}

				if (mFilterSpecs != null) {
					for (String filterSpec : mFilterSpecs) {
						list.add(filterSpec);
					}
				}
				new CollectLogTask().execute(list);
			}
		}

	}

	class CollectLogTask extends
			AsyncTask<ArrayList<String>, Void, StringBuilder> {
		@Override
		protected void onPreExecute() {

		}

		protected StringBuilder doInBackground(ArrayList<String>... params) {
			final StringBuilder log = new StringBuilder();
			try {
				ArrayList<String> commandLine = new ArrayList<String>();
				commandLine.add("logcat");//$NON-NLS-1$
				commandLine.add("-d");//$NON-NLS-1$
				ArrayList<String> arguments = ((params != null) && (params.length > 0)) ? params[0]
						: null;
				if (null != arguments) {
					commandLine.addAll(arguments);
				}

				Process process = Runtime.getRuntime().exec(
						commandLine.toArray(new String[0]));
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));

				String line;
				while ((line = bufferedReader.readLine()) != null) {
					log.append(line);
					log.append(LINE_SEPARATOR);
				}
			} catch (IOException e) {
				//Log.e(TAG, "CollectLogTask.doInBackground failed", e);//$NON-NLS-1$
			}

			return log;
		}

		@Override
		protected void onPostExecute(StringBuilder log) {
			if (null != log) {
				// truncate if necessary
				int keepOffset = Math.max(
						log.length() - MAX_LOG_MESSAGE_LENGTH, 0);
				if (keepOffset > 0) {
					log.delete(0, keepOffset);
				}

				if (mAdditonalInfo != null) {
					log.insert(0, LINE_SEPARATOR);
					log.insert(0, mAdditonalInfo);
				}
				//Log.d("====>>>>", "=======Begin get log");
				/*String logData = log.toString();
				Logger.appendLog(logData, "AllLog");*/
			} else {

			}
		}
	}

	public static void showViewFromBottonToTop(Context context,
			final View etComment) {
		// TODO Auto-generated method stub
		
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.issue_list_show);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				etComment.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		etComment.startAnimation(anim);
	}

	public static void hideViewFromToptoBottm(Context context,
			final View etComment) {
		// TODO Auto-generated method stub
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.issue_list_hide);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				etComment.setVisibility(View.GONE);
			}
		});
		etComment.startAnimation(anim);
	}
	
}
