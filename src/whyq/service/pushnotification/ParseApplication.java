package whyq.service.pushnotification;

import whyq.WhyqMain;
import whyq.activity.HomeActivity;
import whyq.activity.ListActivity;
import whyq.activity.WhyQBillScreen;
import whyq.adapter.WhyqMenuAdapter;
import whyq.service.Service;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.PushService;

import com.parse.ParseUser;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class ParseApplication {

	
	private static final String APPLICATION_ID = "b6Aq0s7gZq2XK79XJJMxCJu2GAi8FAarj2X3QRXS";
	private static final String CLIENT_KEY = "nbVoZs1qjWcOCMk4ztIQP0pZzIXPamBk4gfuT0bH";
	public static void registerPushNotification(Context context) {

		// Add your initialization code here
		Parse.initialize(context, APPLICATION_ID, CLIENT_KEY);

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();

		// If you would like all objects to be private by default, remove this
		// line.
		defaultACL.setPublicReadAccess(true);

		ParseACL.setDefaultACL(defaultACL, true);
	}
	
	public static void exePushNotification(Context context){
        // inform the Parse Cloud that it is ready for notifications
		try {
	        PushService.setDefaultPushCallback(context, PushNotificationActivity.class);
	        ParseInstallation.getCurrentInstallation().saveInBackground();
	        ParseInstallation parseInstallation = ParseInstallation.getCurrentInstallation();
	        if(parseInstallation!=null){
		        String installationId = parseInstallation.getInstallationId();
		        String objectId = parseInstallation.getObjectId();
		        Log.d("exePushNotification","INSTALLATION_ID "+installationId);
		        String appName = "Whyq";
				String appVerions = "1";
				String deviceName = android.os.Build.DEVICE;
				String deviceModel = android.os.Build.MODEL;
				((WhyqMain)context).sendDeviceInfoToPushNotificationServer(appName, appVerions, deviceName, deviceModel, installationId, objectId);
	        	
	        }
	        
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
