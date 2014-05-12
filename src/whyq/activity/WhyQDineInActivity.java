package whyq.activity;

import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.whyq.R;

public class WhyQDineInActivity extends FragmentActivity implements OnClickListener, IServiceListener{
	private String storeId;
	private Display display;
	private Activity context;
	private Button btnCancel;
	private Button btnOk;
	private EditText etTableNumber;
	private EditText etNoNumber;
	private CheckBox cbxNonumber;
	private String listItem;
	private String note;
	private ProgressBar progressBar;
	private Service service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
////		setContentView(R.layout.whyq_store_order_menu);
//		display = getActivity().getWindowManager().getDefaultDisplay();
//	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		context = getActivity();
//		View v = LayoutInflater.from(context).inflate(R.layout.whyq_dine_in, null);
		setContentView(R.layout.whyq_dine_in);
		// v.setBackgroundResource(android.R.color.transparent);
//		getDialog().getWindow().setBackgroundDrawableResource(
//				android.R.color.transparent);
//		getDialog().getWindow().setLayout(50,50);//display.getWidth()/3, display.getHeight() / 4
//		getDialog().setCanceledOnTouchOutside(true);
//		service = new Service(WhyqApplication.Instance());
//		LayoutParams params = (ViewGroup.LayoutParams)v.getLayoutParams();
//		params.height = 100;
//		params.width = 100;
//		
//		v.setLayoutParams(params);
		btnCancel = (Button)findViewById(R.id.tbnCancel);
		btnOk = (Button)findViewById(R.id.btnOk);
		etTableNumber = (EditText)findViewById(R.id.etTableNumber);
		etNoNumber = (EditText)findViewById(R.id.etNoNumber);
		cbxNonumber = (CheckBox)findViewById(R.id.cbNoNumber);
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
				
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		context = this;
		service = new Service(this);
//		return v;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.tbnCancel:
			exeCancel();
			break;
		case R.id.btnOk:
			exeOk();
			break;

		default:
			break;
		}
	}
	private void exeCancel() {
		// TODO Auto-generated method stub
		finish();
	}
	public boolean checkInputData(){
		boolean status = true;
		if(etTableNumber.getText().toString().equals("") && !cbxNonumber.isChecked())
		{
			return false;
		}
		return status;
	}
	private void exeOk() {
		// TODO Auto-generated method stub
		if(checkInputData()){
			Bundle bundle = ListDetailActivity.bundle;
			storeId = bundle.getString("store_id");
			listItem =  bundle.getString("list_items");
			note =  bundle.getString("note");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("store_id", storeId);
			params.put("deliver_type", "4");
			params.put("list_items", listItem);
			params.put("deliver_to", "0");
			params.put("time_zone", Util.getTimeZone(""));
	

			params.put("note", note);
			params.put("token", WhyqApplication.Instance().getRSAToken());
			
			showDialog();
			service.orderSend(params);
		}else{
//			Util.showDialog(context, "Please input data");
			etTableNumber.setError("Please input Hours");
			cbxNonumber.setError("Please input minutes");
		}
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
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
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
}
