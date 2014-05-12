package whyq.service;

import java.util.concurrent.PriorityBlockingQueue;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public abstract class PriorityQueueService extends IntentService {

	public final class QueueItem implements Comparable<QueueItem> {
		public Intent intent;
		public int priority;
		public int startId;

		public QueueItem() {
			// TODO Auto-generated constructor stub
		}

		public int compareTo(QueueItem another) {
			if (this.priority < another.priority) {
				return -1;
			} else if (this.priority > another.priority) {
				return 1;
			} else {
				return (this.startId < another.startId) ? -1 : 1;
			}
		}
	}

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			try {

				final QueueItem item = mQueue.take();
				Log.d("N===Call itemt:", item.startId
						+ item.intent.getExtras().getString("storyId")
						+ "===" + mQueue.toString() + "===");
				// onHandleIntent(item.intent);
				if (mQueue.isEmpty()) {
					PriorityQueueService.this.stopSelf();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static final String EXTRA_PRIORITY = "priority";
	public String mName;
	public static PriorityBlockingQueue<QueueItem> mQueue;
	public boolean mRedelivery;

	public volatile ServiceHandler mServiceHandler;

	public volatile Looper mServiceLooper;

	public PriorityQueueService(String name) {
		super("queueissue");
		mName = name;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		HandlerThread thread = new HandlerThread("PriorityIntentService["
				+ mName + "]");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		mQueue = new PriorityBlockingQueue<QueueItem>();
	}

	@Override
	public void onDestroy() {
		mServiceLooper.quit();
	}

	protected abstract void onHandleIntent(Intent intent);

	/**
	 * Determines if a new item should be added to the queue or not. Subclasses
	 * may override this method to avoid adding an intent to the queue. The
	 * default implementation always returns true.
	 * 
	 * @param item
	 *            The item to add to the queue.
	 * @param queue
	 *            The queue.
	 * @return True if the item should be added to the queue, false otherwise.
	 */
	protected boolean shouldAddToQueue(QueueItem item,
			PriorityBlockingQueue<QueueItem> queue) {
		return true;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		final QueueItem item = new QueueItem();
		item.intent = intent;
		item.startId = startId;
		final int priority = intent.getIntExtra(EXTRA_PRIORITY, 0);
		item.priority = priority;
		if (this.shouldAddToQueue(item, this.mQueue)) {
			try {
					if(mQueue.contains(item)){
						mQueue.remove(item);
						mQueue.add(item);
						Log.d("L===Insert:", startId + "===="
								+ intent.getExtras().getString("storyId"));
						// mServiceHandler.sendEmptyMessage(0);
						if (mQueue.size() > 1) {
							perform();
						}else{
							completed();
						}
					}else{
						mQueue.add(item);
//						mQueue.offer(item);
						Log.d("L===Insert:", startId + "===="
								+ intent.getExtras().getString("storyId"));
						// mServiceHandler.sendEmptyMessage(0);
						if (mQueue.size() > 1) {
							perform();
						}else{
							completed();
						}
					}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	protected abstract void perform();
	protected abstract void completed();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		onStart(intent, startId);
		return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
	}

	public void setIntentRedelivery(boolean enabled) {
		mRedelivery = enabled;
	}
}