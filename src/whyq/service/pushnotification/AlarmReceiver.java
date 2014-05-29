package whyq.service.pushnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

     @Override
     public void onReceive(Context context, Intent intent) {
     // When our Alaram time is triggered , this method will be excuted (onReceive)
     // We're invoking a service in this method which shows Notification to the User
      Intent myIntent = new Intent(context, NotificationService.class);
      myIntent.putExtras(intent);
      context.startService(myIntent);
    }

} 