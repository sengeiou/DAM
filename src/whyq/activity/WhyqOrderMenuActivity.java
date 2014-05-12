package whyq.activity;

import whyq.WhyqApplication;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.whyq.R;

public class WhyqOrderMenuActivity extends DialogFragment implements
		OnClickListener {
	private String storeId;
	private Display display;
	private Context context;
	private Button btnHomeDelivery;
	private Button btnTakeAway;
	private Button btnHotelDelivery;
	private Button btnDinein;
	private Button btnCancel;
	private TextView btnTitle;
	private TextView tvHomeDelivery;

	public static int HOME_DELIVERY = 1;
	public static int TAKE_AWAY = 2;
	public static int HOTEL_ROOM_DELIVERY = 3;
	public static int DINE_IN = 4;

	public static WhyqOrderMenuActivity sOrderMenuActivity;
	
	public WhyqOrderMenuActivity() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.whyq_store_order_menu);
		display = getActivity().getWindowManager().getDefaultDisplay();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View v = LayoutInflater.from(
				WhyqApplication.Instance().getApplicationContext()).inflate(
				R.layout.whyq_store_order_menu, null);
		// setContentView(v);
		// context = this;
		btnHomeDelivery = (Button) v.findViewById(R.id.btnHomeDelivery);
		btnTakeAway = (Button) v.findViewById(R.id.btnHomeTakeAway);
		btnHotelDelivery = (Button) v.findViewById(R.id.btnHotelRoomDelivery);
		btnDinein = (Button) v.findViewById(R.id.btnDineIn);
		btnCancel = (Button) v.findViewById(R.id.btnCancel);
		btnTitle = (TextView) v.findViewById(R.id.tvHeaderTittle);
		btnTitle.setText("in this order:");
		tvHomeDelivery = (TextView) v.findViewById(R.id.tvOpeningTime);
		tvHomeDelivery.setText("(Home delivery time "
				+ ListDetailActivity.bundle.getString("start_time") + " - "
				+ ListDetailActivity.bundle.getString("close_time") + ")");
		btnHomeDelivery.setOnClickListener(this);
		btnTakeAway.setOnClickListener(this);
		btnHotelDelivery.setOnClickListener(this);
		btnDinein.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		checkOrderMenu();

		v.setBackgroundResource(android.R.color.transparent);
		getDialog().getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		float density = WhyqApplication.Instance().getDensity();
		getDialog().getWindow().setLayout((int) (100 * density),
				(int) (density * 300));// display.getWidth()/3,
										// display.getHeight() / 4
		getDialog().setCanceledOnTouchOutside(true);
		
		sOrderMenuActivity = this;
		return v;
	}

	private void checkOrderMenu() {
		// TODO Auto-generated method stub
		if (ListDetailActivity.store.isHomeDeliver()) {
			btnHomeDelivery.setVisibility(View.VISIBLE);
		}else{
			btnHomeDelivery.setVisibility(View.GONE);
		}
		if (ListDetailActivity.store.isTakeAway()) {
			btnTakeAway.setVisibility(View.VISIBLE);
			
		}else{
			btnTakeAway.setVisibility(View.GONE);
		}
		if (ListDetailActivity.store.isHotelDeliver()) {
			btnHotelDelivery.setVisibility(View.VISIBLE);
		}else{
			btnHotelDelivery.setVisibility(View.GONE);
		}
		if (ListDetailActivity.store.isAtPlace()) {
			btnDinein.setVisibility(View.VISIBLE);
		}else{
			btnDinein.setVisibility(View.GONE);
		}
	}

	public void onResume() {
		super.onResume();
		Window window = getDialog().getWindow();
		float density = WhyqApplication.Instance().getDensity();
		window.setLayout((int) (density * 320), (int) (density * 440));
		window.setGravity(Gravity.CENTER);
		// TODO:
	}

	public void homeDeliveryClicked() {

		Intent intent = new Intent(context, WhyQHomeDeliveryActivity.class);
		intent.putExtra("store_id", storeId);
		startActivity(intent);

	}

	public void homeTakeawayClicked() {
		Intent intent = new Intent(context, WhyQTakeAwayActivity.class);
		intent.putExtra("store_id", storeId);
		startActivity(intent);
	}

	public void hotelRoomDeliveryClicked() {
		Intent intent = new Intent(context, WhyQHotelRoomDelivery.class);
		intent.putExtra("store_id", "" + storeId);
		startActivity(intent);
	}

	public void dineinClicked() {
		// WhyQDineInFragment dineIn = new WhyQDineInFragment(storeId);
		// dineIn.show(getFragmentManager(), "orderMenuFrag");
		Intent i = new Intent(context, WhyQDineInActivity.class);
		startActivity(i);
	}

	public void cancelClicked() {
		dismiss();
		// finish();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.btnHomeDelivery:
			homeDeliveryClicked();
			break;
		case R.id.btnHomeTakeAway:
			homeTakeawayClicked();
			break;
		case R.id.btnHotelRoomDelivery:
			hotelRoomDeliveryClicked();
			break;
		case R.id.btnDineIn:
			dineinClicked();
			break;
		case R.id.btnCancel:
			cancelClicked();
			break;
		default:
			break;
		}

	}
}
