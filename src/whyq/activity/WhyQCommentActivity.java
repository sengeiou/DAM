package whyq.activity;

import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dam.R;

public class WhyQCommentActivity extends Activity implements IServiceListener {

	private String storeId;
	private TextView tvMesssage;
	private ToggleButton tglShareFace;
	private Service service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_comment_screen);

		storeId = getIntent().getStringExtra("store_id ");
		tvMesssage = (TextView) findViewById(R.id.tvMessage);
		tglShareFace = (ToggleButton) findViewById(R.id.tglShareFacebook);
		service = new Service(this);
//		exeComment();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("onResume","storeId "+storeId);
		super.onResume();
		exeComment();
	}

	public void takeImageOnclicked(View v) {
		Intent intent = new Intent(WhyQCommentActivity.this, ImageActivity.class);
		startActivity(intent);
	}

	private void exeComment() {
		// TODO Auto-generated method stub
		service.exeComment(tvMesssage.getText().toString(), storeId);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub

	}
	
	public void onBackClicked(View v){
		finish();
	}
}
