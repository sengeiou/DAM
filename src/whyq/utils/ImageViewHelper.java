package whyq.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

public class ImageViewHelper {

	private static final int MAX_CACHE_SIZE = 4 * 1024 * 1024;
	private static final int CLEAR_CACHE_SIZE = 2 * 1024 * 1024;
	
	public interface OnCompleteListener {
		void onComplete();
	}
	
	private OnCompleteListener mListener;
	public void setOnCompleteListener(OnCompleteListener listener) {
		mListener = listener;
	}

	LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
			MAX_CACHE_SIZE) {
		@Override
		protected int sizeOf(String key, Bitmap bitmap) {
			if (null != bitmap)
				return bitmap.getRowBytes() * bitmap.getHeight();
			return 0;
		}
	};

	final List<BitmapDownloaderTask> lsCurrentTask = new LinkedList<BitmapDownloaderTask>();

	private void animatedLoadImage(Bitmap bmp, ImageView imgView) {
		final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
				new ColorDrawable(android.R.color.transparent),
				new BitmapDrawable(imgView.getResources(), bmp) });
		imgView.setImageDrawable(td);
		td.startTransition(500);
		if (mListener != null) {
			mListener.onComplete();
		}
	}

	private Bitmap bmp;

	public void setLoading(Bitmap loading) {
		bmp = loading;
	}

	public void downloadImage(String url, ImageView imageView) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		
		String key = hashKeyForDisk(url);
		Bitmap bmp = cache.get(hashKeyForDisk(url));
		if (null != bmp) {
			cancelPotentialDownload(key, imageView);
			imageView.setImageBitmap(bmp);
		} else {
			File file = getDiskCacheDir(imageView.getContext(), key);
			if (file.exists()) {
				bmp = decodeImageFromFile(file.getAbsolutePath());
				if (cache.size() >= CLEAR_CACHE_SIZE) {
					cache.evictAll();
					System.gc();
				}
				if (null != bmp) {
					cache.put(key, bmp);
					animatedLoadImage(bmp, imageView);
				} else {
					forceDownload(key, url, file.getAbsolutePath(), imageView);
				}
			} else {
				forceDownload(key, url, file.getAbsolutePath(), imageView);
			}
		}
	}

	private void forceDownload(String key, String url, String saveFile,
			ImageView imageView) {
		BitmapDownloaderTask task = new BitmapDownloaderTask(key, imageView);
		lsCurrentTask.add(task);
		while (lsCurrentTask.size() > 10) {
			BitmapDownloaderTask cancelTask = lsCurrentTask.get(0);
			lsCurrentTask.remove(0);
			if (null != cancelTask) {
				cancelTask.forceCancel();
			}
		}
		FakeDrawable fakeDrawable = new FakeDrawable(task, bmp);
		imageView.setImageDrawable(fakeDrawable);
		task.execute(url, saveFile);
	}

	private Bitmap decodeImageFromFile(String fileName) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(fileName, options);
		if (options.outHeight <= 100 || options.outWidth <= 100)
			options.inSampleSize = 1;
		else
			options.inSampleSize = 3;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(fileName, options);
	}

	private static void cancelPotentialDownload(String key, ImageView imageView) {
		BitmapDownloaderTask task = getBitmapDownloaderTask(imageView);
		if (null != task && key.equals(task.key)) {
			task.forceCancel();
		}
	}

	private static BitmapDownloaderTask getBitmapDownloaderTask(
			ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof FakeDrawable) {
				FakeDrawable fakeDrawable = (FakeDrawable) drawable;
				return fakeDrawable.getTask();
			}
		}
		return null;
	}

	static class FakeDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> refTargetTask;

		public FakeDrawable(BitmapDownloaderTask targetTask, Bitmap bmp) {
			super(bmp);
			this.refTargetTask = new WeakReference<BitmapDownloaderTask>(
					targetTask);
		}

		public BitmapDownloaderTask getTask() {
			return refTargetTask.get();
		}
	}

	class BitmapDownloaderTask extends AsyncTask<Object, Void, Bitmap> {
		private final WeakReference<ImageView> imageViewReference;
		public final String key;
		private boolean __isCancel = false;

		public void forceCancel() {
			__isCancel = true;
		}

		public BitmapDownloaderTask(String key, ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			this.key = key;
		}
		
		@Override
		protected Bitmap doInBackground(Object... params) {
			String request = (String) params[0];
			final String saveFile = (String) params[1];
			try {

				URL url = new URL(request);
				URLConnection connection = url.openConnection();
				connection.setConnectTimeout(3000); /*
													 * 3 seconds for connect to
													 * server
													 */
				connection.setReadTimeout(5000); /* 5 seconds for read data */

				connection.connect();
				InputStream input = connection.getInputStream();
				int count;
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				byte data[] = new byte[1024];

				while ((count = input.read(data)) != -1) {
					buffer.write(data, 0, count);

					if (__isCancel) {
						buffer.reset();
						input.close();
						return null;
					}
				}

				if (!__isCancel) {

					Bitmap bmp = BitmapFactory.decodeByteArray(
							buffer.toByteArray(), 0, buffer.size());

					// if (isNormalize) {
					// bmp = Bitmap.createScaledBitmap(bmp,
					// Constant.IMAGE_WIDTH,
					// Constant.IMAGE_HEIGHT, true);
					// }

					bmp.compress(CompressFormat.JPEG, 70, new FileOutputStream(
							saveFile));

					buffer.reset();
					// FileUtil.writeFile(saveFile, buffer.toByteArray());
					return bmp;
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (__isCancel) {
				bitmap = null;
			}

			int saveIndex = -1;
			for (int i = 0; i < lsCurrentTask.size(); ++i) {
				BitmapDownloaderTask task = lsCurrentTask.get(i);
				if (task == this) {
					saveIndex = i;
					break;
				}
			}
			if (saveIndex >= 0)
				lsCurrentTask.remove(saveIndex);

			if (null != bitmap) {
				if (cache.size() >= CLEAR_CACHE_SIZE) {
					cleanCache();
				}
				cache.put(key, bitmap);

				if (imageViewReference != null) {
					ImageView imageView = imageViewReference.get();
					if (null != imageView) {
						BitmapDownloaderTask task = getBitmapDownloaderTask(imageView);
						if (this == task) {
							animatedLoadImage(bitmap, imageView);
						}
					}
				}
			}
		}
	}
	
	

	public void cleanCache() {
		cache.evictAll();
		System.gc();
	}

	/**
	 * A hashing method that changes a string (like a URL) into a hash suitable
	 * for using as a disk filename.
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	@TargetApi(8)
	public static File getExternalCacheDir(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			return context.getExternalCacheDir();
		}

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath()
				+ cacheDir);
	}

	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique directory name to append to the cache dir
	 * @return The cache dir
	 */
	private File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		File cacheDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) || !isExternalStorageRemovable()) {
			cacheDir = getExternalCacheDir(context);
		}

		if (cacheDir == null) {
			cacheDir = context.getCacheDir();
		}

		return new File(cacheDir.getPath() + File.separator + uniqueName);
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	private boolean isExternalStorageRemovable() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

}
