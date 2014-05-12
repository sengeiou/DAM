package whyq.activity;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.http.AccessToken;
import whyq.interfaces.LoginTWDelegate;
import whyq.utils.Constants;
import whyq.utils.WhyqUtils;
import whyq.utils.twitter.OAuthRequestTokenTask;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Prepares a OAuthConsumer and OAuthProvider 
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret.
 * OAuthProvider is configured with the 3 OAuth endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
public class PrepareRequestTokenActivity extends Activity {

	final String TAG = getClass().getName();
	
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    private Activity parentActivity;
    private Context context;  
    private LoginTWDelegate loginDelegate;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = PrepareRequestTokenActivity.this;
		parentActivity = PrepareRequestTokenActivity.this;//getParent();
    	try {
    		this.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    	    this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,Constants.ACCESS_URL,Constants.AUTHORIZE_URL);
    	} catch (Exception e) {
    		//Log.e(TAG, "Error creating consumer / provider",e);
		}
    	
       //Log.i(TAG, "Starting task to retrieve request token.");
		new OAuthRequestTokenTask(this,consumer,provider).execute();
	}
	public static boolean flag = false;

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the request token).
	 * The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent); 
		flag = true;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(Constants.OAUTH_CALLBACK_URL)) {
			//Log.i(TAG, "Callback received : " + uri);
			//Log.i(TAG, "Retrieving Access Token");
			new RetrieveAccessTokenTask(this,consumer,provider,prefs).execute(uri);
			if(!LoginHome.isTwitter)
				finish();	
		}
	}
	
	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context	context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;
		private SharedPreferences prefs;
		
		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs=prefs;
		}


		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret 
		 * for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri...params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				Editor editor = prefs.edit();
				// Set the login type as Twitter
				editor.putString(Constants.LOGIN_TYPE, Constants.TWITTER_LOGIN);
//				editor.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
//				editor.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				editor.putString(OAuth.OAUTH_VERIFIER, oauth_verifier);
//				editor.commit();
				
				String token = consumer.getToken();
				String secret = consumer.getTokenSecret();
				
				consumer.setTokenWithSecret(token, secret);
				AccessToken accessToken = new AccessToken(token, secret);
				WhyqUtils whyqUtils = new WhyqUtils();
				whyqUtils.saveTwitterAccess("twitter", accessToken, context);
				//TODO: validate user before forwarding to new page.
				// Check on server
				if(LoginHome.isTwitter){
				Log.d("RetrieveAccessTokenTask","Token "+token);
					Intent data = new Intent();
					data.putExtra("token", token);
					data.putExtra("token_secret", secret);
					setResult(RESULT_OK, data);
					finish();
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
//					nameValuePairs.add(new BasicNameValuePair("type", "twitter"));
//					nameValuePairs.add(new BasicNameValuePair("oauth_token", token));
//					nameValuePairs.add(new BasicNameValuePair("oauth_token_secret", secret));
//					nameValuePairs.add(new BasicNameValuePair("oath_verifier", oauth_verifier));
//					AuthorizeController authorize = new AuthorizeController(PrepareRequestTokenActivity.this);
//					authorize.authorize(context, nameValuePairs);
					
				}
			
				//Log.i(TAG, "OAuth - Access Token Retrieved");
				
			} catch (Exception e) {
				//Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
			}

			return null;
		}

/*
		private void executeAfterAccessTokenRetrieval() {
			String msg = getIntent().getExtras().getString("tweet_msg");
			try {
				TwitterUtils.sendTweet(prefs, msg);
			} catch (Exception e) {
				Log.e(TAG, "OAuth - Error sending to Twitter", e);
			}
		}*/
	}

	
}
