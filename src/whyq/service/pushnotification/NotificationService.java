package whyq.service.pushnotification;

import whyq.activity.LoginHome;

import com.dam.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotificationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		String title = intent.getExtras().getString("title");
		String message = intent.getExtras().getString("message");
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.logo_whyq)
				.setContentTitle(title).setContentText(message)
				.setAutoCancel(true);

		Intent resultIntent = new Intent(this, LoginHome.class);
		resultIntent.putExtra("is_push_delivery", true);
		resultIntent.putExtra("message", message);
		// Because clicking the notification opens a new ("special") activity,
		// there's
		// no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		Notification notification = mBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		// Sets an ID for the notification
		int mNotificationId = 0;
		// Gets an instance of the NotificationManager service
		NotificationManager mNotifyMgr = (NotificationManager) this
				.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
		// Builds the notification and issues it.
		mNotifyMgr.notify(mNotificationId, notification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}