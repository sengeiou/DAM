package whyq.activity;

import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.R;

public class WhyqProfileNotifyPersionalActivity extends Activity implements IServiceListener {
	private CheckBox cbxReceiveNotification;
	private CheckBox cbxAllowSeeMyFriend;
	private CheckBox cbxSoundNotification;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_notification_screen);
		TextView tvHeader = (TextView)findViewById(R.id.tvHeaderTitle);
		tvHeader.setText("Notification");
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
		cbxReceiveNotification = (CheckBox) findViewById(R.id.cbxReceiveNotification);
		cbxAllowSeeMyFriend = (CheckBox) findViewById(R.id.cbxAllowUserToSeeFriends);
		cbxSoundNotification = (CheckBox)findViewById(R.id.cbxSoundsNotification);
	}
	
	public void onBack(View v){
		finish();
	}
	
	public void onDoneClicked(View v){
		setProgressShowing(true);
		Service service = new Service(WhyqProfileNotifyPersionalActivity.this);
		service.pushNotification(cbxReceiveNotification.isChecked()?"1":"0", cbxAllowSeeMyFriend.isChecked()?"1":"0");
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		setProgressShowing(false);
		if(result.isSuccess()&& result.getAction() == ServiceAction.ActionPushNotification){
			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){}else if(data.getStatus().equals("401")){
				Util.loginAgain(WhyqProfileNotifyPersionalActivity.this, data.getMessage());
			}else if(data.getStatus().equals("204")){}else{
			}
			
		}else if(!result.isSuccess()){
			Toast.makeText(WhyqProfileNotifyPersionalActivity.this, "Fail!", Toast.LENGTH_LONG).show();
		}
	}
	private void setProgressShowing(boolean isShowing) {
		// TODO Auto-generated method stub
    	if(isShowing){
    		progressBar.setVisibility(View.VISIBLE);
    	}else{
    		progressBar.setVisibility(View.GONE);
    	}
	}
}
