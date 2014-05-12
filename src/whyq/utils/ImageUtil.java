package whyq.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class ImageUtil {

	public static void imageViewFromURL(final ImageView imageView, final String url) {

		AsyncTask<Void, Void, Drawable> downloader = new AsyncTask<Void, Void, Drawable>() {

			@Override
			protected Drawable doInBackground(Void... params) {
				try{
					URL imageURL = new URL( url );
					 HttpURLConnection conn= (HttpURLConnection)imageURL.openConnection();
			            conn.setDoInput(true);
			            conn.connect();
			            int length = conn.getContentLength();
			            int[] bitmapData =new int[length];
			            byte[] bitmapData2 =new byte[length];
			            InputStream is = conn.getInputStream();
			            BitmapFactory.Options options = new BitmapFactory.Options();
			            options.inJustDecodeBounds = true;
			            options.inSampleSize = 1;
			            Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
			            Drawable drawable = new  BitmapDrawable(mResources, bitmap);
			           // Log.d("IMAGE_DOWNLOADER", "do in background " + url);
			            return drawable;
				}
				catch (Exception e) {
					// TODO: handle exception
					//Log.e("IMAGE_DOWNLOADER", "onPostExcute ERROR " + url, e);
				}
				return null;
			}

			protected void onPostExecute(Drawable result) {
				if (result != null) {
					//Log.d("IMAGE_DOWNLOADER", "onPostExcute " + url);
					final Drawable newImage = result;
					imageView.setImageDrawable(newImage);
				}
			}
		};
		downloader.execute();
	}

	static int IMAGE_MAX_SIZE = 700;
	static int SCALE_RATIO = 2;
	static Resources mResources;
	static DisplayMetrics mMetrics;

	static void prepareResources(Context context) {
		if (mMetrics != null)
			return;
		mMetrics = new DisplayMetrics();
		Activity act = (Activity) context;
		act.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
		AssetManager mgr = context.getAssets();
		mResources = new Resources(mgr, mMetrics, context.getResources()
				.getConfiguration());
	}
}
