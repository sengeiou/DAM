package whyq.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import whyq.WhyqApplication;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


public final class UrlImageViewHelper {

	public static int copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] stuff = new byte[1024];
		int read = 0;
		int total = 0;
		while ((read = input.read(stuff)) != -1) {
			output.write(stuff, 0, read);
			total += read;
		}
//		Log.d("aaaa", "=========>" + total);
		return total;
	}

	static int IMAGE_MAX_SIZE = 1024000;// 60000;//30000;//1200000/4;//60000;//1200000;
	static int SCALE_RATIO = 2;
	static Resources mResources;
	static DisplayMetrics mMetrics;


	private static void prepareResources(Context context) {
		if (mMetrics != null)
			return;
		mMetrics = new DisplayMetrics();
		Activity act = (Activity) context;
		act.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		AssetManager mgr = context.getAssets();
		mResources = new Resources(mgr, mMetrics, context.getResources()
				.getConfiguration());
	}

	private static BitmapDrawable loadDrawableFromStream(Context context,
			InputStream stream) {
		prepareResources(context);
		/*
		 * Bitmap bitmap = null; try { InputStream temp = stream;
		 * BitmapFactory.Options options = new BitmapFactory.Options();
		 * options.inJustDecodeBounds = true; BitmapFactory.decodeStream(temp,
		 * null, options); temp.close();
		 * 
		 * int scale = 1; if (options.outHeight > 800 || options.outWidth > 800)
		 * scale = (int)Math.pow(2.0, (int) Math.round(Math.log(1280 / (double)
		 * Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
		 * 
		 * BitmapFactory.Options options2 = new BitmapFactory.Options();
		 * options2.inSampleSize = 4; options2.inJustDecodeBounds = false; temp
		 * = stream; bitmap = BitmapFactory.decodeStream(temp, null, options2);
		 * temp.close(); } catch (IOException ioe) { ioe.printStackTrace(); }
		 * 
		 * if (bitmap != null) return new BitmapDrawable(mResources, bitmap);
		 * else return null;
		 */

		final Bitmap bitmap = BitmapFactory.decodeStream(stream);
		return new BitmapDrawable(mResources, bitmap);
	}

	public static final int CACHE_DURATION_INFINITE = Integer.MAX_VALUE;
	public static final int CACHE_DURATION_ONE_DAY = 1000 * 60 * 60 * 24;
	public static final int CACHE_DURATION_TWO_DAYS = CACHE_DURATION_ONE_DAY * 2;
	public static final int CACHE_DURATION_THREE_DAYS = CACHE_DURATION_ONE_DAY * 3;
	public static final int CACHE_DURATION_FOUR_DAYS = CACHE_DURATION_ONE_DAY * 4;
	public static final int CACHE_DURATION_FIVE_DAYS = CACHE_DURATION_ONE_DAY * 5;
	public static final int CACHE_DURATION_SIX_DAYS = CACHE_DURATION_ONE_DAY * 6;
	public static final int CACHE_DURATION_ONE_WEEK = CACHE_DURATION_ONE_DAY * 7;

	public static void setUrlDrawable(final ImageView imageView,
			final String url) {
		setUrlDrawable(imageView.getContext(), imageView, url, null,
				CACHE_DURATION_THREE_DAYS, 0, 0, false);
	}

	public static void setUrlDrawable(final ImageView imageView,
			final String url, boolean scaleView) {
		setUrlDrawable(imageView.getContext(), imageView, url, null,
				CACHE_DURATION_THREE_DAYS, 0, 0, scaleView);
	}

	public static void setUrlDrawable(final ImageView imageView,
			final String url, int width, int height) {
		setUrlDrawable(imageView.getContext(), imageView, url, null,
				CACHE_DURATION_THREE_DAYS, width, height, false);
	}

	public static void loadUrlDrawable(final Context context, final String url,
			int width, int height) {
		setUrlDrawable(context, null, url, null, CACHE_DURATION_THREE_DAYS,
				width, height, false);
	}

	private static boolean mHasCleaned = false;

	public static String getFilenameForUrl(String url) {
		return "" + url.hashCode() + ".urlimage";
	}

	private static void cleanup(Context context) {
		if (mHasCleaned)
			return;
		mHasCleaned = true;
		try {
			// purge any *.urlimage files over a week old
			String[] files = context.getFilesDir().list();
			if (files == null)
				return;
			for (String file : files) {
				if (!file.endsWith(".urlimage"))
					continue;

				File f = new File(context.getFilesDir().getAbsolutePath() + '/'
						+ file);
				if (System.currentTimeMillis() > f.lastModified()
						+ CACHE_DURATION_ONE_WEEK)
					f.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean isNullOrEmpty(CharSequence s) {
		return (s == null || s.equals("") || s.equals("null") || s
				.equals("NULL"));
	}

	private static void setUrlDrawable(final Context context,
			final ImageView imageView, final String url,
			final Drawable defaultDrawable, long cacheDurationMs,
			final int width, final int height, final boolean scaleView) {
		cleanup(context);
		// disassociate this ImageView from any pending downloads
		if (imageView != null)
			mPendingViews.remove(imageView);

		if (isNullOrEmpty(url)) {
			if (imageView != null && defaultDrawable != null)
				imageView.setImageDrawable(defaultDrawable);
			return;
		}

		// final UrlImageCache cache = UrlImageCache.getInstance();
		// Drawable d = cache.get(url);
		// if (d != null) {
		// //Log.i(LOGTAG, "Cache hit on: " + url);
		// if (imageView != null)
		// imageView.setImageDrawable(d);
		// return;
		// }

		final String filename = getFilenameForUrl(url);

		File file = context.getFileStreamPath(filename);

		// null it while it is downloading
		/*if (imageView != null)
			imageView.setImageDrawable(defaultDrawable);*/

		// since listviews reuse their views, we need to
		// take note of which url this view is waiting for.
		// This may change rapidly as the list scrolls or is filtered, etc.
		// Log.i(LOGTAG, "Waiting for " + url);
		if (imageView != null)
			mPendingViews.put(imageView, url);

		ArrayList<ImageView> currentDownload = mPendingDownloads.get(url);
		if (currentDownload != null) {
			// Also, multiple vies may be waiting for this url.
			// So, let's maintain a list of these views.
			// When the url is downloaded, it sets the imagedrawable for
			// every view in the list. It needs to also validate that
			// the imageview is still waiting for this url.
			if (imageView != null)
				currentDownload.add(imageView);
			return;
		}

		final ArrayList<ImageView> downloads = new ArrayList<ImageView>();
		if (imageView != null)
			downloads.add(imageView);
		mPendingDownloads.put(url, downloads);

		AsyncTask<Void, Void, Drawable> downloader = new AsyncTask<Void, Void, Drawable>() {

			private int oiw = 0;
			private int oih = 0;

			@Override
			protected Drawable doInBackground(Void... params) {
				AndroidHttpClient client = AndroidHttpClient
						.newInstance(context.getPackageName());
				try {
					HttpGet get = new HttpGet(url);
					final HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,
							HttpPermUtils.TIME_OUT);
					HttpConnectionParams.setSoTimeout(httpParams,
							HttpPermUtils.TIME_OUT);
					HttpClientParams.setRedirecting(httpParams, true);
					get.setParams(httpParams);
					HttpResponse resp = client.execute(get);
					int status = resp.getStatusLine().getStatusCode();
					if (status != HttpURLConnection.HTTP_OK) {
						// Log.i(LOGTAG, "Couldn't download image from Server: "
						// + url + " Reason: " +
						// resp.getStatusLine().getReasonPhrase() + " / " +
						// status);
						return null;
					}
					HttpEntity entity = resp.getEntity();
//                    Log.i(LOGTAG, url + " Image Content Length: " + entity.getContentLength());
					InputStream is = entity.getContent();
					FileOutputStream fos = context.openFileOutput(filename,
							Context.MODE_PRIVATE);
					copyStream(is, fos);
					fos.close();
					is.close();
					FileInputStream fis = context.openFileInput(filename);
					int fileSize = (int) fis.getChannel().size();

					// Decode image size
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(fis, null, o);
					fis.close();

					oiw = o.outWidth;
					oih = o.outHeight;
					Runtime.getRuntime().gc();
					int scale = 1;
//                      scale = (int)Math.max(oiw/IMAGE_MAX_SIZE, oih/IMAGE_MAX_SIZE)+1;
//                      if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
					// // scale = 2;
//                    	  int scale2 = (int)Math.pow(2.0, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
					// }
//                      while (o.outWidth * o.outHeight / Math.pow(scale, 2) > IMAGE_MAX_SIZE) {
					// scale+= 1;
					// }
					do {
						scale++;
					} while (fileSize / Math.pow(2, scale) > IMAGE_MAX_SIZE);

					//Log.d("aaaa", "=========>Scale=: " + scale);
					// Decode with inSampleSize
					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = scale;
					fis = context.openFileInput(filename);
					Bitmap b = null;
					if (fis != null)
						b = BitmapFactory.decodeStream(fis, null, o2);
					fis.close();

					prepareResources(context);
                      //BitmapDrawable drawable = loadDrawableFromStream(context, fis);
					BitmapDrawable drawable = new BitmapDrawable(mResources, b);
					mDownloadException = false;
					if (width > 0 && height > 0) {
						Drawable dr = scaleDrawable(drawable, width, height);
						client.close();
						return dr;
					}
					client.close();
					return drawable;
				} catch (Exception ex) {
					/*Log.e("PERMPING_IMAGE",
							"Exception during Image download of " + url, ex);*/
					mDownloadException = true;
					client.close();
					return null;
				} finally {
					client.close();
				}
			}

			protected void onPostExecute(Drawable result) {
				if (result == null)
					result = defaultDrawable;
				mPendingDownloads.remove(url);
				// cache.put(url, result);
				for (ImageView iv : downloads) {
					// validate the url it is waiting for
					String pendingUrl = mPendingViews.get(iv);
					if (!url.equals(pendingUrl)) {
                        //Log.i(LOGTAG, "Ignoring out of date request to update view for " + url);
						continue;
					}
					mPendingViews.remove(iv);
					if (result != null) {
						final Drawable newImage = result;
						final ImageView imageView = iv;

						if (scaleView == true) {

							WhyqApplication state = (WhyqApplication) context
									.getApplicationContext();
							DisplayMetrics metrics = state.getDisplayMetrics();
							//Log.d("==ORIGINAL IMAGE SIZE:==========>"," Width==="+oiw+"===Heigth===="+oih);
							//String logShow = "==ORIGINAL IMAGE SIZE:==========> Width==="+oiw+"===Heigth===="+oih+"\n";
							// Calculate with & height //This should be in a
							// function
							int marginLeft = 6;
							int marginRight = 6;

							float imgWidth = 0;
							float imgHeight = 0;
							float width = 0;
                        	width = (metrics.widthPixels - (marginLeft + marginRight));
                        	float ratioX = (float)width / (float)oiw;
                        	float ratioY = (float)metrics.heightPixels / (float)oih;
                        	float ratio = Math.min(ratioX, ratioY);
							imgWidth = oiw * ratio;
							imgHeight = oih * ratio;
							//Log.d("==DEVICE SIZE:==========>"," Width==="+metrics.widthPixels+"===Heigth===="+metrics.heightPixels);
							//logShow+="==DEVICE SIZE:==========> Width==="+metrics.widthPixels+"===Heigth===="+metrics.heightPixels+"\n";
							//Log.d("==SCCALED IMAGE SIZE:==========>"," Width==="+imgWidth+"===Heigth===="+imgHeight);
							//logShow+="==SCCALED IMAGE SIZE:==========> Width==="+imgWidth+"===Heigth===="+imgHeight;
							//Logger.appendLog("\n==For permId==="+PermAdapter.currentPermId+"=====>>>Data="+logShow, "LogImageInfo");
							LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView
									.getLayoutParams();
							layoutParams.width = (int) imgWidth;
							layoutParams.height = (int) imgHeight;
							layoutParams.setMargins(marginLeft, 10,
									marginRight, 10);
							imageView.setLayoutParams(layoutParams);
						}

						imageView.setImageDrawable(newImage);
						imageView.setScaleType(ScaleType.FIT_XY);
					}
				}
			}
		};
		if (mDownloadException == false) {
			downloader.execute();
		} else {
			downloader.cancel(true);
		}

	}

    
    public static Drawable scaleDrawable( Drawable drawable , int width, int height ){
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
		bitmap.recycle();
		bitmap = scaledBitmap;
		System.gc();
		Drawable dr = new BitmapDrawable(bitmap);
		return dr;
	}

	public static void clearAllImageView() {
		mPendingViews.clear();
		mPendingDownloads.clear();
	}

	public static boolean mDownloadException = false;

	private static Hashtable<ImageView, String> mPendingViews = new Hashtable<ImageView, String>();
	private static Hashtable<String, ArrayList<ImageView>> mPendingDownloads = new Hashtable<String, ArrayList<ImageView>>();
}
