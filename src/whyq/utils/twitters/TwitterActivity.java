package whyq.utils.twitters;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import twitter4j.Twitter;
import twitter4j.http.AccessToken;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.whyq.R;

public class TwitterActivity extends Activity {
	// Define constain
	public static final String TWITTER_KEY_LOGIN = "twitter_key_login";
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	public static final String AUTH_URL = "http://twitter.com/oauth/authorize";
	public static final String CALLBACK_URL = "perm://twitter";
	// Property
	public static CommonsHttpOAuthConsumer consumer;
	public static OAuthProvider provider = new DefaultOAuthProvider(REQUEST_URL, ACCESS_TOKEN_URL, AUTH_URL);
	public static WebView twitterWebView;
	public ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_intent);
		twitterWebView = (WebView)findViewById(R.id.twitter_web);
		progressBar = (ProgressBar)findViewById(R.id.twProgressBar);
		twitterWebView.clearCache(true);
		consumer =  new CommonsHttpOAuthConsumer(
				Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		new OAuthRequestTokenTask(this, consumer, provider).doInBackground();
	}
	
	public void onCloseClicked(View v){
		finish();
	}
	protected boolean flag = false;
	@Override
	protected void onResume(){
		super.onResume();
		if(flag)
			end();
		flag = true;
		
	}
	private class OAuthRequestTokenTask{
		private Context _context;
		private CommonsHttpOAuthConsumer _consumer;
		private OAuthProvider _provider;

		public OAuthRequestTokenTask(Context context,
				CommonsHttpOAuthConsumer consumer, OAuthProvider provider) {
			// TODO Auto-generated constructor stub
			_context = context;
			_consumer = consumer;
			_provider = provider;
		}

		protected Void doInBackground() {
			try {
				// TODO Auto-generated method stub
				final String url = _provider.retrieveRequestToken(_consumer,
						CALLBACK_URL);
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
//						.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//								| Intent.FLAG_ACTIVITY_NO_HISTORY
//								| Intent.FLAG_FROM_BACKGROUND);
//				_context.startActivity(intent);
				twitterWebView.setWebViewClient(new TWWebviewClient());
				twitterWebView.getSettings().setJavaScriptEnabled(true);
				twitterWebView.loadUrl(url);
			} catch (Exception e) {
				Log.e("a", "Error during OAUth retrieve request token", e);
				progressBar.setVisibility(View.GONE);
			}
			return null;

		}
	}
	
	private Twitter twitter = null;
	private String ScreenName = null;
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		flag = false;
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
			String _verifier = uri.getQueryParameter("oauth_verifier");
			// String _accessToken = uri.getQueryParameter("oauth_token");
			try {
				
				provider.retrieveAccessToken(consumer, _verifier);
				String accessKey = consumer.getToken();
				String accessSecret = consumer.getTokenSecret();
				AccessToken at = new AccessToken(accessKey, accessSecret);
				SharedPreferencesManager shareManager = new SharedPreferencesManager(
						this);
				shareManager.saveTwitterToken(at);
				twitter = shareManager.loadTwitter();

			} catch (OAuthMessageSignerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthNotAuthorizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthExpectationFailedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthCommunicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			//startActivity(new Intent(this, AutoTweetActivity.class));
		}
		if (twitter != null) {
			
		}
		else{
			Log.d("s", "twitter = null");
			end();
		}
		
	}
	private void end(){
		finish();
	}
	public class TWWebviewClient extends WebViewClient{
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			if(progressBar.getVisibility() != View.VISIBLE)
				progressBar.setVisibility(View.VISIBLE);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if(progressBar.getVisibility() != View.GONE){
				progressBar.setVisibility(View.GONE);
				twitterWebView.setWebViewClient(null);
			}
		}
		
	}

}
