package whyq.activity;

import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.adapter.PlacesAutoCompleteAdapter;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dam.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class WhyQHomeDeliveryActivity extends FragmentActivity implements
		IServiceListener, OnClickListener {
	private EditText etOtherAddress;
	private EditText etPhoneNumber;
	private Button etHours;
	private Button etMinutes;
	private CheckBox cbASAP;
	private String storeId;
	private ProgressBar progressBar;
	private Context context;
	private String listItem;
	private String note;
	private String longitude;
	private String latgitude;
	private Service servivice;
	private GoogleMap mMap;
	private Object store;
	private AutoCompleteTextView atAddress;
	private int currentMinutes;
	private int currentHours;
	private String address;
	protected String mPhoneNumber;
	private int calMinutes;
	private int calHour;
	private int scheduleDeliery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_home_delivery);
		context = this;
		TextView headerTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		headerTitle.setText("Home Deliver");
		storeId = getIntent().getStringExtra("store_id");
		etOtherAddress = (EditText) findViewById(R.id.etOtherAddress);
		etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
		etHours = (Button) findViewById(R.id.etHours);
		etMinutes = (Button) findViewById(R.id.etMinutes);
		cbASAP = (CheckBox) findViewById(R.id.cbASAP);
		atAddress = (AutoCompleteTextView)findViewById(R.id.atAddress);
		atAddress.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.place_item_2));
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		
		currentHours = 0;
		currentMinutes = 0;
		
		// Util.checkLocationSetting(context);
		Bundle bundle = Util.getLocation(context);
		if (bundle != null) {
			longitude = bundle.getString("lon");
			latgitude = bundle.getString("lat");
			address = bundle.getString("address");
		}
		servivice = new Service(this);
		setUpMapIfNeeded();
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
		if(address!=null)
			atAddress.setText(address);
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

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Enable MyLocation Button in the Map
		mMap.setMyLocationEnabled(true);

		// Add lots of markers to the map.
		addMarkersToMap();

	}

	private void addMarkersToMap() {
		try {

			LatLng STORE_POSITION = new LatLng(Double.parseDouble(latgitude),
					Double.parseDouble(longitude));
			// TODO Auto-generated method stub
			store = mMap.addMarker(new MarkerOptions()
					.position(STORE_POSITION)
					.title("")
					.snippet("")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			LatLngBounds bounds = new LatLngBounds.Builder().include(
					STORE_POSITION).build();
			// mMap.moveCamera(CameraUpdateFactory
			// .newLatLngBounds(bounds, 50));
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(STORE_POSITION,
					15));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	// @Override
	// protected boolean isRouteDisplayed() {
	// // TODO Auto-generated method stub
	// return false;
	// }
	public void onBack(View v) {
		finish();
	}

	public void onDoneClicked(View v) {

		checkInputData();
	}

	
	public boolean checkInputData() {
		if (cbASAP.isChecked()) {
			
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

			String otherAddress = atAddress.getText().toString();//etOtherAddress.getText().toString();
			String phoneNumber = etPhoneNumber.getText().toString();
			String hours = ""+currentHours;
			String minutes = ""+currentMinutes;
			if (otherAddress.length() == 0) {
				etOtherAddress.setFocusable(true);
				etOtherAddress.startAnimation(shake);
				etOtherAddress.requestFocus();
				
				return false;
			} else if (phoneNumber.length() == 0) {
				etPhoneNumber.setFocusable(true);
				etPhoneNumber.startAnimation(shake);
				etPhoneNumber.requestFocus();
				return false;
			} else {
				if(exeCheckTimeInput()){
					showDialog();
					showRememberInfoDialog();
				}

			}
			
			return true;
			
		} else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

			String otherAddress = atAddress.getText().toString();//etOtherAddress.getText().toString();
			String phoneNumber = etPhoneNumber.getText().toString();
			String hours = ""+currentHours;
			String minutes = ""+currentMinutes;
			if (otherAddress.length() == 0) {
//				etOtherAddress.setFocusable(true);
//				etOtherAddress.startAnimation(shake);
//				etOtherAddress.requestFocus();
				atAddress.setFocusable(true);
				atAddress.startAnimation(shake);
				atAddress.requestFocus();
				return false;
			} else if (phoneNumber.length() == 0) {
				etPhoneNumber.setFocusable(true);
				etPhoneNumber.startAnimation(shake);
				etPhoneNumber.requestFocus();
				return false;
			} else if (hours.length() == 0) {
				etHours.setFocusable(true);
				etHours.startAnimation(shake);
				etHours.requestFocus();
				return false;
			} else if (minutes.length() == 0) {
				etMinutes.setFocusable(true);
				etMinutes.startAnimation(shake);
				etMinutes.requestFocus();
				return false;
			} else {
				if(exeCheckTimeInput()){
					showDialog();
					showRememberInfoDialog();
				}

				return true;
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void showRememberInfoDialog(){
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				context);
		builder.setTitle(context.getString(R.string.app_name_title));
		builder.setMessage("Do you want save your phone number?");
		final android.app.AlertDialog alertError = builder.create();
		alertError.setButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				mPhoneNumber = etPhoneNumber.getText().toString();
				alertError.dismiss();
				new asyncExeOrderSend().execute();
			}
		});
		alertError.setButton2("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mPhoneNumber = null;
				alertError.dismiss();
				new asyncExeOrderSend().execute();
			}
		});
		alertError.show();
	}
	
	class asyncExeOrderSend extends
			AsyncTask<HashMap<String, String>, Void, HashMap<String, String>> {
		public asyncExeOrderSend() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}
		@Override
		protected void onPostExecute(HashMap<String, String> location) {
			// TODO Auto-generated method stub
			Bundle bundle = ListDetailActivity.bundle;
			storeId = bundle.getString("store_id");
			listItem = bundle.getString("list_items");
			note = bundle.getString("note");
			if (location != null) {
				longitude = location.get("lon");
				latgitude = location.get("lon");
			}
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("store_id", storeId);
			params.put("deliver_type", "2");
			params.put("list_items", listItem);
			params.put("deliver_to", "" + atAddress.getText().toString());//etOtherAddress.getText().toString());
			params.put("time_zone", Util.getTimeZone(""));
			if (cbASAP.isChecked()) {
				params.put("time_deliver", "ASAP");
			} else {
				params.put("time_deliver", getTimeInpu());
			}
			params.put("deliver_latitude", longitude);
			params.put("deliver_longitude", latgitude);
			params.put("phone_deliver", etPhoneNumber.getText().toString());
			params.put("note", note);
			params.put("token", WhyqApplication.Instance().getRSAToken());
			params.put("remember_info", mPhoneNumber==null?"0":"1");
			
			servivice.orderSend(params);
		}

		private String getTimeInpu() {
			// TODO Auto-generated method stub
			String hours = ""+currentHours;
			String minutes = ""+currentMinutes;
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			return hours + ":" + minutes;
		}

		@Override
		protected HashMap<String, String> doInBackground(
				HashMap<String, String>... params) {
			// TODO Auto-generated method stub

//			return Util.getLatLonFromAddress(etOtherAddress.getText().toString());
			return Util.getLatLonFromAddress(atAddress.getText().toString());
		}
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		if (result.getAction() == ServiceAction.ActionOrderSend
				&& result.isSuccess()) {
			ResponseData data = (ResponseData) result.getData();
			if (data != null) {
				if (data.getStatus().equals("200")) {
					Util.showDialog(context, data.getMessage());
//					WhyqOrderMenuActivity.sOrderMenuActivity.dismiss();
//					WhyQBillScreen.sBillActivity.finish();
				} else if (data.getStatus().equals("401")) {
					Util.loginAgain(context, data.getMessage());
				} else {
					Util.showDialog(context, data.getMessage());
				}
			}
			WhyqApplication.Instance().pushNotification(WhyqApplication.Instance().getApplicationContext(), scheduleDeliery, "Ready to get your product?", "your delivery action will be 15 minutes next");
			finish();
		}else if(result.getAction() == ServiceAction.ActionOrderSend){
			finish();
		}
	}

	private boolean exeCheckTimeInput() {
		// TODO Auto-generated method stub
		Time today = new Time(Time.getCurrentTimezone());
		today.setToNow();
		int hourNow = today.hour;
		int minutesNow = today.hour;
		if((currentHours > hourNow) || (currentHours == calHour ) && (calMinutes < currentMinutes)){
			calHour = currentHours - hourNow;
			calMinutes = currentMinutes - minutesNow;
			scheduleDeliery = calHour*60*60*1000 + calMinutes*60 * 1000;
		}else{
			Toast.makeText(context, "Time set now correct", Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
		
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
