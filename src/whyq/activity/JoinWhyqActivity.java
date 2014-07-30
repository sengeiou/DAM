/**
 * 
 */
package whyq.activity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import oauth.signpost.OAuth;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;

import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.interfaces.IServiceListener;
import whyq.interfaces.JoinPerm_Delegate;
import whyq.model.ResponseData;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.RSA;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dealadelivery.whyq.R;


/**
 * @author Linh Nguyen This activity supports to create new Perming account.
 */
public class JoinWhyqActivity extends Activity implements TextWatcher,
		JoinPerm_Delegate, IServiceListener {
	private static final int GET_IMAGE = 0;
	// Button createAccount;
	EditText firstName;
	EditText lastName;
	EditText email;
	EditText password;
	EditText confirmPassword;
	private SharedPreferences prefs;
	private Button createAccount;
	private ProgressDialog dialog;
	private JoinWhyqActivity context;
	private View progressBar;
	private ImageView imgAvatar;
	private String avatarPath;
	private String firstNameValue;
	private String lastNameValue;
	private String emailValue;
	private String passValue;
	private String confirmPassValue;
	private String notify;
	private EditText userName;
	private String userNameValue;
	private Service service;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_join);
		
		context = JoinWhyqActivity.this;
		service = new Service(this);
		TextView textView = (TextView) findViewById(R.id.permpingTitle);
//		Typeface tf = Typeface.createFromAsset(getAssets(),
//				"ufonts.com_franklin-gothic-demi-cond-2.ttf");
		Util.applyTypeface(textView, Util.sTypefaceRegular);
		textView.setText("New Account");
//		if (textView != null) {
//			textView.setTypeface(WhyqApplication.sTypefaceRegular);
//		}

		imgAvatar = (ImageView)findViewById(R.id.imgAvatar);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		dialog = new ProgressDialog(context);
		firstName = (EditText) findViewById(R.id.first_name);
		lastName = (EditText) findViewById(R.id.last_name);
		// name.addTextChangedListener(this);
		// nickName = (EditText) findViewById(R.id.nickName);
		email = (EditText) findViewById(R.id.email);
		// email.addTextChangedListener(this);
		password = (EditText) findViewById(R.id.acccountPassword);
		// password.addTextChangedListener(this);
		confirmPassword = (EditText) findViewById(R.id.confirm_password);
		// confirmPassword.addTextChangedListener(this);
		userName = (EditText)findViewById(R.id.user_name);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		createAccount = (Button) findViewById(R.id.loginPerm);
		createAccount.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(checkInputData()){
					showDialog();
					exeSignup();
				}else{
//					Util.showDialog(context, notify);
				}
			}
		});

	}
	protected void exeSignup() {
		// TODO Auto-generated method stub

		showDialog();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				8);
		// nameValuePairs.add(new BasicNameValuePair("type",
		// prefs.getString(Constants.LOGIN_TYPE, "")));
		if (prefs.getString(Constants.LOGIN_TYPE, "").equals(
				Constants.FACEBOOK_LOGIN)) {// Facebook
			nameValuePairs.add(new BasicNameValuePair("oauth_token",
					prefs.getString(Constants.ACCESS_TOKEN, "")));
			nameValuePairs.add(new BasicNameValuePair("first_name",
					firstNameValue));// prefs.getString(Constants.ACCESS_TOKEN,
														// ""))
			nameValuePairs.add(new BasicNameValuePair("last_name",
					lastNameValue));
			nameValuePairs.add(new BasicNameValuePair("email", emailValue));
			nameValuePairs.add(new BasicNameValuePair("password",
					passValue));
			nameValuePairs.add(new BasicNameValuePair("cpassword",
					confirmPassValue));
		} else if (prefs.getString(Constants.LOGIN_TYPE, "").equals(
				Constants.TWITTER_LOGIN)) { // Twitter
			nameValuePairs.add(new BasicNameValuePair("oauth_token",
					prefs.getString(OAuth.OAUTH_TOKEN, "")));
			nameValuePairs.add(new BasicNameValuePair(
					"oauth_token_secret", prefs.getString(
							OAuth.OAUTH_TOKEN_SECRET, "")));
			nameValuePairs.add(new BasicNameValuePair("first_name",
					firstNameValue));// prefs.getString(Constants.ACCESS_TOKEN,
														// ""))
			nameValuePairs.add(new BasicNameValuePair("last_name",
					lastNameValue));
			nameValuePairs.add(new BasicNameValuePair("email",emailValue));
			RSA rsa = new RSA();
			String pass = null;
			try {
				pass = rsa.RSAEncrypt(password.getText().toString());
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nameValuePairs
					.add(new BasicNameValuePair("password", pass));
			nameValuePairs.add(new BasicNameValuePair("cpassword",
					confirmPassword.getText().toString()));
		} else { // Whyq
			HashMap<String, Object> params = new HashMap<String, Object>();
			RSA rsa = new RSA();
			String pass = "", confirmPass = "";
			try {
				pass = rsa.RSAEncrypt(passValue);
				// confirmPass =
				// rsa.RSAEncrypt(confirmPassword.getText().toString());
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			nameValuePairs.add(new BasicNameValuePair("email",emailValue));
			nameValuePairs
					.add(new BasicNameValuePair("password", pass));
			nameValuePairs.add(new BasicNameValuePair("first_name",
					firstNameValue));// prefs.getString(Constants.ACCESS_TOKEN,
														// ""))
			nameValuePairs.add(new BasicNameValuePair("last_name",
					lastNameValue));
			
			params.put("email",emailValue);
			params.put("password", pass);
			params.put("first_name", firstNameValue);
			params.put("lastNameValue", lastNameValue);
			try {
				File file = new File(avatarPath);
				if(file.exists()){
					FileBody encFile = new FileBody(file,"image/png");	
					params.put("avatar", encFile);
				}
			    
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			service.register(params);
			XMLParser parser = new XMLParser(XMLParser.JOIN_WHYQ,
					JoinWhyqActivity.this, API.createAccountURL,
					nameValuePairs);

		}

	
	}
	protected boolean checkInputData() {
		// TODO Auto-generated method stub
		firstNameValue = firstName.getText().toString();
		lastNameValue = lastName.getText().toString();
		emailValue =email.getText().toString();
		passValue = password.getText().toString();
		confirmPassValue = confirmPassword.getText().toString();
		userNameValue = userName.getText().toString();
		notify = "";
		boolean status = true;
		if(firstNameValue.equals("")){
			notify+="\nInput First Name";
			status = false;
			firstName.setError("Please input your first name");
			
		}
		if(lastNameValue.equals("")){
			notify+="Input Last Name";
			status = false;
			lastName.setError("Please input your last name");
		}
		if(emailValue.equals("")){
			notify+="Input Email";
			status = false;
			email.setError("Please input your email");
		}else{
			if(!Util.isValidEmail(emailValue)){
				notify+="Your email format is not correct. Please try again";
				status = false;
				email.setError("Your email format is not correct. Please try again");
				
			}
		}
		if(userNameValue.equals("")){
			notify+="Input Username";
			status = false;
			userName.setError("Please input your username");
		}
		if(passValue.equals("")){
			notify+="Input password";
			status = false;
			password.setError("Please input your password");
		}
		if(confirmPassValue.equals("")){
			status = false;
			notify+="Please input your confirm password";
			confirmPassword.setError("Please input your confirm password");
		}
		if(!passValue.equals(confirmPassValue)){
			status = false;
			Toast.makeText(context, "Your confirmed password is not match", Toast.LENGTH_SHORT).show();
		}
		return status;
	}
	private Bitmap getBitmapWithCareMemory(String filename){
		try {
			int IMAGE_MAX_SIZE = 1024000;
			FileOutputStream fos = context.openFileOutput(Util.getFileName(filename),
					Context.MODE_PRIVATE);
			InputStream is = new BufferedInputStream(new FileInputStream(filename));
			copyStream(is, fos);
			fos.close();
			is.close();
			FileInputStream fis = context.openFileInput(filename);
			int fileSize = (int) fis.getChannel().size();

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(fis, null, o);
			fis.close();

			int oiw = o.outWidth;
			int oih = o.outHeight;
			Runtime.getRuntime().gc();
			int scale = 1;
//              scale = (int)Math.max(oiw/IMAGE_MAX_SIZE, oih/IMAGE_MAX_SIZE)+1;
//              if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
			// // scale = 2;
//            	  int scale2 = (int)Math.pow(2.0, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
			// }
//              while (o.outWidth * o.outHeight / Math.pow(scale, 2) > IMAGE_MAX_SIZE) {
			// scale+= 1;
			// }
			do {
				scale++;
			} while (fileSize / Math.pow(2, scale) > IMAGE_MAX_SIZE);

			Log.d("aaaa", "=========>Scale=: " + scale);
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = context.openFileInput(filename);
			Bitmap b = null;
			if (fis != null){
				b = BitmapFactory.decodeStream(fis, null, o2);

			
			}
			fis.close();
			return b;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	public static int copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] stuff = new byte[1024];
		int read = 0;
		int total = 0;
		while ((read = input.read(stuff)) != -1) {
			output.write(stuff, 0, read);
			total += read;
		}
		return total;
	}
	private Bitmap getBitmap2(String path) {
		try {

			final int IMAGE_MAX_SIZE = 1024;// 1200000; // 1.2MP
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			InputStream in = new FileInputStream(path);
			BitmapFactory.decodeStream(in, null, o);
			in.close();
			in = new FileInputStream(path);

			// Decode image size

			double scale = 1;
			int currentWidth = o.outWidth;
			if (currentWidth > IMAGE_MAX_SIZE) {
				scale = currentWidth / IMAGE_MAX_SIZE;

			}

			Bitmap b = null;
			if (scale > 1) {

				double y = (double) o.outHeight / scale;
				double x = 1024;
				b = BitmapFactory.decodeFile(path);
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
				b = scaledBitmap;
				System.gc();

			} else {
				b = BitmapFactory.decodeFile(path);
			}

			/*
			 * Log.d("", "bitmap size - width: " + b.getWidth() +
			 * ", height: " + b.getHeight() + "");
			 */
			return b;
		} catch (Exception e) {
			// Log.e("", e.getMessage(), e);
			return null;
		}
	}

	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if (firstName.getText().toString().equals("")) {
			firstName.setError("Name is required!");
		}
		if (lastName.getText().toString().equals("")) {
			firstName.setError("Name is required!");
		}
		if (email.getText().toString().equals("")) {
			email.setError("Email is required!");
		}
		if (password.getText().toString().equals("")) {
			password.setError("Password is required!");
		}
		if (confirmPassword.getText().toString().equals("")
				|| !confirmPassword.getText().toString()
						.equals(password.getText().toString())) {
			confirmPassword.setError("Confirm password is invalid!");
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(User user) {
		// TODO Auto-generated method stub
		hideDialog();
		if (user != null) {
			// Store the user object to PermpingApplication
			WhyqApplication state = (WhyqApplication) getApplicationContext();
			state.setUser(user);
			if(getApplicationContext() != null && email != null && password != null) {
				if(email.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
					XMLParser.storeUserAccount(getApplicationContext(), user);

				}
			}
			ListActivity.isLogin = true;
			ListActivity.loginType = 0;
			Intent intent = new Intent(JoinWhyqActivity.this, WhyqMain.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			 finish();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(),
					"Joined perm failed! Please try again.", Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
			toast.show();

		}

	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		hideDialog();
		Toast toast = Toast.makeText(getApplicationContext(),
				"Joined perm failed! Please try again.", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
		toast.show();
		this.finish();
	}

	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.INVISIBLE);
	}

	public void takeImageOnclicked(View v) {
		Intent intent = new Intent(JoinWhyqActivity.this, ImageActivity.class);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
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
		
		if(result.isSuccess()&& result.getAction()==ServiceAction.ActionSigup){
			ResponseData data = (ResponseData)result.getData();
			if(data.getStatus().equals("200")){
				User user = (User)data.getData();
					WhyqApplication.Instance().setToken(user);
					ListActivity.isLogin = true;
					ListActivity.loginType = 2;
					Intent intent = new Intent(JoinWhyqActivity.this, WhyqMain.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();

			}else if(data.getStatus().equals("401")){
				Util.loginAgain(context, data.getMessage());
			}else{
				Util.showDialog(context, data.getMessage());
			}

		}else{
			Util.showDialog(context,"Can not login for now\nTry again!");
		}
	}
	

}
