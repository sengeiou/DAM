package com.share.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;
import whyq.WhyqApplication;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.WhyqUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.whyq.R;


public class TwitterActivity extends Activity {
	// Define constain
	public static final String TWITTER_KEY_LOGIN = "twitter_key_login";
	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_TOKEN_URL = "https://twitter.com/oauth/access_token";
	public static final String AUTH_URL = "https://twitter.com/oauth/authorize";
	public static final String CALLBACK_URL = whyq.utils.Constants.OAUTH_CALLBACK_URL;
	// Property
	public static WebView twitterWebView;
	public ProgressBar progressBar;
	
	private int TWITTER_AUTH;
	private Twitter mTwitter;
	private RequestToken mRequestToken;
	private SharedPreferencesManager shareManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_intent);
//		getWindow().setLayout(550, 600);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			 setFinishOnTouchOutside(true);
		}
		twitterWebView = (WebView)findViewById(R.id.twitter_web);
		progressBar = (ProgressBar)findViewById(R.id.twProgressBar);
		twitterWebView.clearCache(true);
		shareManager = new SharedPreferencesManager(
				WhyqApplication.Instance().getApplicationContext());
//		progressBar.setVisibility(View.VISIBLE);
		
	}
	protected boolean flag = false;
	
	@Override
	protected void onResume(){
		mTwitter = shareManager.loadTwitter();
		if(mTwitter == null){
			mTwitter = new TwitterFactory().getInstance();
			new OAuthRequestTokenTask(TwitterActivity.this).execute();

		}
		super.onResume();
//		if(flag)
//			end();
//		flag = true;
		
	}
	public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void> {

		final String TAG = getClass().getName();
		private Context	context;
		private String url;
		public OAuthRequestTokenTask(Context context) {
			this.context = context;
	
		}
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				// TODO Auto-generated method stub

				mRequestToken = null;
				mTwitter.setOAuthConsumer(whyq.utils.Constants.CONSUMER_KEY,whyq.utils.Constants.CONSUMER_SECRET);
				String callbackURL = CALLBACK_URL;
				try
				{
					mRequestToken = mTwitter.getOAuthRequestToken(callbackURL);
					url =  mRequestToken.getAuthenticationURL();
				}
				catch (TwitterException e)
				{
					e.printStackTrace();
				}
				
				twitterWebView.setWebViewClient(new TWWebviewClient());
				twitterWebView.getSettings().setJavaScriptEnabled(true);
				twitterWebView.getSettings().setSavePassword(false);
				twitterWebView.loadUrl(url);
			} catch (Exception e) {
				progressBar.setVisibility(View.GONE);
				e.printStackTrace();
				
			}
			return null;
		}

	}
	public class AsyGetData extends AsyncTask<Void, Void, String> {

		final String TAG = getClass().getName();
		private Context	context;
		private String veryfy;
		public AsyGetData(Context context, String veryfy) {
			this.context = context;
			this.veryfy = veryfy;
		}
		@Override
		protected String doInBackground(Void... params) {
			
			try {
				// TODO Auto-generated method stub

				at = mTwitter.getOAuthAccessToken(veryfy);

				shareManager.saveTwitterToken(at);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				Editor editor = prefs.edit();
				
				String token = at.getToken();
				String secret = at.getTokenSecret();
				
				AccessToken accessToken = new AccessToken(token, secret);
				WhyqUtils whyqUtils = new WhyqUtils();
				whyqUtils.saveTwitterAccess("twitter", accessToken, context);
				//TODO: validate user before forwarding to new page.
				// Check on server
				Log.d("RetrieveAccessTokenTask","Token "+token);
				Intent data = new Intent();
				data.putExtra("token", token);
				data.putExtra("token_secret", secret);
				setResult(RESULT_OK, data);
				finish();
				return at.getScreenName();
			} catch (Exception e) {
				Log.e("a", "Error during OAUth retrieve request token", e);
				progressBar.setVisibility(View.GONE);
				return "Failer";
			}
		}
		@Override
		protected void onPostExecute(String sResponse) {
			if (!sResponse.equals("Failer")) {
				Intent result = new Intent();
				result.putExtra("LOGIN_RESULT", ""+sResponse);
				setResult(RESULT_OK, result);
				finish();
			} else {
				
			}
		}

	}
	private Twitter twitter = null;
	private String ScreenName = null;
	AccessToken at;
	String _verifier;
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		flag = false;
//		Uri uri = intent.getData();
//		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
//			_verifier = uri.getQueryParameter("oauth_verifier");
//			 String _accessToken = uri.getQueryParameter("oauth_token");
//			try {
//				
//				at = mTwitter.getOAuthAccessToken(_verifier);
//				String accessKey = at.getToken();
//				String accessSecret = at.getTokenSecret();
//				Log.d("AccessToken",_accessToken+"_accessToken"+accessKey+"==="+accessSecret);
//				SharedPreferencesManager shareManager = new SharedPreferencesManager(
//						AMReaderApplication.Instance().getApplicationContext());
//				shareManager.saveTwitterToken(at);
//				twitter = shareManager.loadTwitter();
//				AMReaderApplication app = (AMReaderApplication) getApplication();
//				app.putData(TWITTER_KEY_LOGIN, true);
//				Intent result = new Intent();
//				result.putExtra("TWITTER_USER", "Logged in");
//				setResult(RESULT_OK,result);
//				finish();
//			} catch (Exception e) {
//				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//			}
//			
//			//startActivity(new Intent(this, AutoTweetActivity.class));
//		}
//		
//	}
	private void end(){
		finish();
	}
	public class TWWebviewClient extends WebViewClient{
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			// TODO Auto-generated method stub
//			super.onPageStarted(view, url, favicon);
//			Log.d("onPageStarted",url);
//			if(progressBar.getVisibility() != View.VISIBLE)
//				progressBar.setVisibility(View.VISIBLE);
//		}
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
//			if(progressBar.getVisibility() != View.VISIBLE)
			progressBar.setVisibility(View.GONE);
			if( url.contains(CALLBACK_URL))
			{
				Uri uri = Uri.parse( url );
				String oauthVerifier = uri.getQueryParameter( "oauth_verifier" );
				try {
					new AsyGetData(WhyqApplication.Instance().getApplicationContext(), oauthVerifier).execute();

				} catch (Exception e) {
					// TODO: handle exception
				}
//				Intent result = new Intent();
//				result.putExtra("TWITTER_USER", at.getScreenName());
//				setResult(RESULT_OK,result);
//				finish();
				return true;
			}
			return false;
		}
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			// TODO Auto-generated method stub
//			super.onPageFinished(view, url);
//			Log.d("onPageFinished",url);
//			if(progressBar.getVisibility() != View.GONE){
//				progressBar.setVisibility(View.GONE);
//				twitterWebView.setWebViewClient(null);
//			}
//		}
		
	}
	
	public void onCloseClicked(View v){
		finish();
	}
}
