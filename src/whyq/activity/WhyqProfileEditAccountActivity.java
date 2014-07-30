package whyq.activity;

import java.io.File;
import java.util.HashMap;

import whyq.WhyqApplication;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Constants;
import whyq.utils.Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dealadelivery.whyq.R;

public class WhyqProfileEditAccountActivity extends Activity implements IServiceListener{

	private static final int GET_IMAGE = 0;
	private EditText etFirstName;
	private EditText etLastName;
	private EditText etPass;
	private EditText etPassVerify;
	private Service service;
	private Context context;
	private String oldPassword;
	private ProgressBar progressBar;
	private TextView tvTitle;
	private String avatarPath;
	private ImageView imgAvatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_profile);

		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etLastName = (EditText) findViewById(R.id.etLastName);
		etPass = (EditText) findViewById(R.id.etPass);
		etPassVerify = (EditText) findViewById(R.id.etVerifyPass);
		service = new Service(this);
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		imgAvatar = (ImageView)findViewById(R.id.imgAvatar);
		tvTitle.setText("Edit Profile");
		context = this;
	}
	public void takeImageOnclicked(View v) {
		Intent intent = new Intent(WhyqProfileEditAccountActivity.this, ImageActivity.class);
		startActivityForResult(intent,GET_IMAGE);
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("onActivityResult Join","");
	    if (resultCode == RESULT_OK) {
	        if (requestCode == GET_IMAGE ) {
	        	avatarPath = data.getStringExtra("path");
	        	Log.d("onActivityResult Join",""+avatarPath);
	        	if(avatarPath!=null){
	        		 File imgFile = new  File(avatarPath);
	        		if(imgFile.exists()){
	        			imgAvatar.setImageURI(Uri.fromFile(imgFile));
	        		}
	        	}
	        }
	    }
	    
	    ImageActivity.imagePath = "";
	}
	public boolean checkInputData() {
		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		if (etFirstName.getText().toString().length() == 0) {
			etFirstName.setFocusable(true);
			etFirstName.startAnimation(shake);
			etLastName.requestFocus();
			return false;
		} else if (etLastName.getText().toString().length() == 0) {
			etLastName.setFocusable(true);
			etLastName.startAnimation(shake);
			etLastName.requestFocus();
			return false;
		} else if (etPass.getText().toString().length() == 0) {
			etPass.setFocusable(true);
			etPass.startAnimation(shake);
			etPass.requestFocus();
			return false;
		} else if (etPassVerify.getText().toString().length() == 0) {
			etPassVerify.setFocusable(true);
			etPassVerify.startAnimation(shake);
			etPassVerify.requestFocus();
			return false;
		} else {
			if(!etPass.getText().toString().equals(etPassVerify.getText().toString())){
				etPass.setError("Password and Verify password is not match");
				return false;
			}
			return true;
		}
	}

	public void onDoneClicked(View v) {
		if(checkInputData()){
			showInputOlderPass();
		}
	}

	private void showInputOlderPass() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(WhyqProfileEditAccountActivity.this);
		builder.setTitle(Constants.APP_NAME);
		builder.setMessage("Enter your password:");
		// Set up the input
		final EditText input = new EditText(this);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		builder.setView(input);

		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        oldPassword = input.getText().toString();
		        if(oldPassword!=null){
		        	if(oldPassword.equals(WhyqApplication.Instance().getPassword())){
						showDialog();
						exeEditProfile();		        		
		        	}
		        }else{
		        	Util.showDialog(context, "Your password is incorect");
		        }
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
	}

	private void exeEditProfile() {
		// TODO Auto-generated method stub
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("first_name",etFirstName.getText().toString());
		params.put("last_name",etLastName.getText().toString());
		params.put("new_password",etPass.getText().toString());
		params.put("old_password",oldPassword);
		params.put("token", WhyqApplication.Instance().getRSAToken());
		service.editProfile(params);
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		if(result.getAction() == ServiceAction.ActionEditProfile && result.isSuccess()){
			ResponseData data = (ResponseData)result.getData();
			hideDialog();
			if(data!=null){
				if(data.getStatus().equals("200")){
					Util.loginAgain(context, "Please login to use this function");
				}else if(data.getStatus().equals("401")){
					Util.loginAgain(context, data.getMessage());
				}else{
					Util.showDialog(context, data.getMessage());
				}
			}
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
}
