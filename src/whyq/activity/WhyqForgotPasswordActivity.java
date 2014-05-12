/**
 * 
 */
package whyq.activity;

import whyq.WhyqMain;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.Util;
import whyq.utils.facebook.FacebookConnector;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

/**
 * @author Linh Nguyen This activity supports to authenticate the user by
 *         user-name and password. Also, it can communicate to FB and Twitter to
 *         do authentication as well
 */
public class WhyqForgotPasswordActivity extends Activity implements
		IServiceListener {

	EditText email;
	Button facebookLogin;
	Button twitterLogin;
	Button login;
	public static boolean isLoginFb = false;
	public static boolean isTwitter = false;
	// private ProgressDialog loadingDialog;
	ProgressBar progressBar;
	private WhyqMain login_delegate;
	SharedPreferences prefs;
	private FacebookConnector facebookConnector;
	public static Context context;

	// public LoginPermActivity(Login_delegate loginDelegate){
	// login_delegate = loginDelegate;
	// }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_forgot_pass);
		context = WhyqForgotPasswordActivity.this;
		TextView textView = (TextView) findViewById(R.id.permpingTitle);
		textView.setText("Forgot Password");
		Util.applyTypeface(textView, Util.sTypefaceRegular);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		login_delegate = new WhyqMain();
		email = (EditText) findViewById(R.id.etEmail);

		// facebookLogin = (Button) findViewById(R.id.loginfb);
		// twitterLogin = (Button) findViewById(R.id.logintw);
		login = (Button) findViewById(R.id.loginPerm);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);

		// Login button
		login.setOnClickListener(new View.OnClickListener() {
			private Service service;

			public void onClick(View v) {
				if (checkInputData()) {
					showDialog();
					service= new Service(WhyqForgotPasswordActivity.this);
					service.exeResetPass(email.getText().toString());
				}
			}

		});

		//
		facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID,
				this, getApplicationContext(), new String[] { Constants.EMAIL,
						Constants.PUBLISH_STREAM });

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public void onPause() {
		super.onPause();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public boolean checkInputData() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		String userEmail = email.getText().toString();
		if (userEmail.length() == 0) {
			email.setFocusable(true);
			email.startAnimation(shake);
			email.requestFocus();
			return false;
		} else {
			return true;
		}
	}

	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// WhyqMain.back();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		if (result.isSuccess() == true && result.getAction() == ServiceAction.ActionForgotPassword) {

			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){
				Util.showDialog(context, data.getMessage());
				finish();
			}else if(data.getStatus().equals("401")){
				Util.loginAgain(context, data.getMessage());
			}else{
				Util.showDialog(context, data.getMessage());
			}
		}
	}
}