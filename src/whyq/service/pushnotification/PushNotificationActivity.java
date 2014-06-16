package whyq.service.pushnotification;

import whyq.activity.ListDetailActivity;
import whyq.activity.LoginHome;
import whyq.activity.WhyQBillScreen;
import whyq.activity.WhyqShareActivity;
import whyq.model.BillPushNotification;
import whyq.service.DataParser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dam.R;

public class PushNotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushnotification_screen);
		String pushNotificationData = getIntent().getExtras().getString(
				"com.parse.Data");
		if (pushNotificationData != null) {
			DataParser parser = new DataParser();
			BillPushNotification data = parser
					.parserJsonPushnotification(pushNotificationData);
			Toast.makeText(this, data.getAlert(), Toast.LENGTH_LONG).show();
			if (data.getType().toUpperCase().equals("DELIVERY")) {
				Intent i = new Intent(this, WhyqShareActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("push_data", data);
				i.putExtras(bundle);
				startActivity(i);
				finish();
			} else if (data.getType().toUpperCase().equals("ACCEPT")) {
				Intent i = new Intent(this, WhyQBillScreen.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("push_data", data);
				i.putExtras(bundle);
				startActivity(i);
				finish();
			}else{
				Intent i = new Intent(this, ListDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("push_data", data);
				i.putExtras(bundle);
				startActivity(i);
				finish();
			}
		}
	}
}
