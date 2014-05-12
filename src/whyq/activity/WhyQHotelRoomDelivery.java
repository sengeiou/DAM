package whyq.activity;


import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.whyq.R;

public class WhyQHotelRoomDelivery extends FragmentActivity implements IServiceListener, OnClickListener {
	private EditText etRoomNo;
	private EditText etHotelChargeCode;
	private Button etHours;
	private Button etMinutes;
	private CheckBox cbASAP;
	private String storeId;
	private String listItem;
	private ProgressBar progressBar;
	private String note;
	private Service service;
	private Context context;
	protected int currentHours;
	protected int currentMinutes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.why_room_delivery_screen);
		TextView headerTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		headerTitle.setText("Home Deliver");
		storeId = getIntent().getStringExtra("store_id");
		etRoomNo = (EditText)findViewById(R.id.etRoomNo);
		etHotelChargeCode = (EditText)findViewById(R.id.etHotelChargeCode);
		etHours = (Button)findViewById(R.id.etHours);
		etMinutes = (Button)findViewById(R.id.etMinutes);
		cbASAP = (CheckBox)findViewById(R.id.cbASAP);
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
		context = this;
		service = new Service(this);
		
		cbASAP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(cbASAP.isChecked()){
					disableTimeField();
				}else{
					endAbleTimeField();
				}
			}
		});
		
		etHours.setOnClickListener(this);
		etMinutes.setOnClickListener(this);
	}
	protected void endAbleTimeField() {
		// TODO Auto-generated method stub
		etHours.setEnabled(true);
		etMinutes.setEnabled(true);
	}

	protected void disableTimeField() {
		// TODO Auto-generated method stub
		etHours.setEnabled(false);
		etMinutes.setEnabled(false);
	}
//	@Override
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
	public void onBack(View v){
		finish();
	}
	public void onDoneClicked(View v){

		checkInputData();
	}
    public boolean checkInputData()
    {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        
		String otherAddress = etRoomNo.getText().toString();
		String phoneNumber = etHotelChargeCode.getText().toString();
		String hours = ""+currentHours;
		String minutes = ""+currentMinutes;
        if (otherAddress.length() == 0)
        {
        	etRoomNo.setFocusable(true);
        	etRoomNo.startAnimation(shake);
        	etRoomNo.requestFocus();
        	return false;
        } else if (phoneNumber.length() == 0)
        {
        	etHotelChargeCode.setFocusable(true);
        	etHotelChargeCode.startAnimation(shake);
        	etHotelChargeCode.requestFocus();
        	return false;
        }else if (hours.length() == 0)
        {
        	etHours.setFocusable(true);
        	etHours.startAnimation(shake);
        	etHours.requestFocus();
        	return false;
        }else if (minutes.length() == 0)
        {
        	etMinutes.setFocusable(true);
        	etMinutes.startAnimation(shake);
        	etMinutes.requestFocus();
        	return false;
        } 
        else
        {
			Bundle bundle = ListDetailActivity.bundle;
			storeId = bundle.getString("store_id");
			listItem =  bundle.getString("list_items");
			note =  bundle.getString("note");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("store_id", storeId);
			params.put("deliver_type", "3");
			params.put("list_items", listItem);
			params.put("deliver_to", etRoomNo.getText().toString());
			params.put("time_zone", Util.getTimeZone(""));
			if(cbASAP.isChecked()){
				params.put("time_deliver", "ASAP");				
			}else{
				params.put("time_deliver", ""+getTimeInpu());
			}

			params.put("note", note);
			params.put("token", WhyqApplication.Instance().getRSAToken());
			
			showDialog();
			service.orderSend(params);
			return true;
        }
    }

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		if(result.getAction() == ServiceAction.ActionOrderSend && result.isSuccess()){
			ResponseData data = (ResponseData)result.getData();
			hideDialog();
			if(data!=null){
				if(data.getStatus().equals("200")){
					Util.showDialog(context, data.getMessage());
					WhyqOrderMenuActivity.sOrderMenuActivity.dismiss();
					WhyQBillScreen.sBillActivity.finish();
				}else if(data.getStatus().equals("401")){
					Util.loginAgain(context, data.getMessage());
				}else{
					Util.showDialog(context, data.getMessage());
				}
			}
		}
	}
	private String getTimeInpu() {
		// TODO Auto-generated method stub
		String hours =""+currentHours;
		String minutes = ""+currentMinutes;
		if(hours.length()<2){
			hours="0"+hours;
		}
		if(minutes.length()<2){
			minutes="0"+minutes;
		}
		return hours+":"+minutes;
	}
	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if(id == R.id.etHours||id == R.id.etMinutes){
			new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
				
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					// TODO Auto-generated method stub
					currentHours = hourOfDay;
					currentMinutes = minute;
					etHours.setText(""+hourOfDay);
					etMinutes.setText(""+minute);
				}
			}, currentHours, currentMinutes, true).show();
		}
	}
}
