package whyq.activity;

import java.util.ArrayList;

import whyq.WhyqMain;
import whyq.adapter.WhyqAdapter;
import whyq.model.Store;
import whyq.model.Transporter;
import whyq.model.User;
import whyq.utils.Constants;
import whyq.utils.WhyqUtils;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class BoardDetailActivity extends Activity {

	private ListView permList;
	private TextView message;
	Button back;

	int screenWidth;
	int screenHeight;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		Transporter transporter =null;
		if(extras != null)
			transporter = (Transporter) extras
				.get(Constants.TRANSPORTER);
		ArrayList<Store> stores = null;
		String boardName = null;
		if(transporter != null){
			stores = transporter.getPerms();
			boardName = transporter.getBoardName();
		}
		// Get the screen's size.
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;

		if (stores == null || stores.size() == 0) {
			// Display man hinh No Perms Found
			setContentView(R.layout.profile_emptyperm_layout);
			message = (TextView) findViewById(R.id.message);
			message.setText("No Perms Found. Please press Back to select another board!");
		} else {
			setContentView(R.layout.profile_permlist_layout);
			permList = (ListView) findViewById(R.id.permList);
			User user = WhyqUtils.isAuthenticated(getApplicationContext());
//			BoardDetailAdapter boardDetailAdapter = new BoardDetailAdapter(
//					this, perms, boardName, screenHeight, screenWidth, user);
			WhyqAdapter boardDetailAdapter =  new WhyqAdapter(FriendActivityGroup.context,
					null,R.layout.whyq_item_1, stores, BoardDetailActivity.this, screenHeight, screenWidth, false, user);

			permList.setAdapter(boardDetailAdapter);
		}
		
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}

//		thien back = (Button) findViewById(R.id.btBack);
//
//		back.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//				ProfileActivityGroup.group.back();
//			}
//		});
	}

	/**
	 * This code is being executed when the layout has not been laid out yet.
	 */
	/*
	 * public void onGlobalLayout() { // Get the screen's size. DisplayMetrics
	 * metrics = new DisplayMetrics();
	 * getWindowManager().getDefaultDisplay().getMetrics(metrics);
	 * 
	 * screenHeight = metrics.heightPixels; screenWidth = metrics.widthPixels; }
	 */
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        WhyqMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
