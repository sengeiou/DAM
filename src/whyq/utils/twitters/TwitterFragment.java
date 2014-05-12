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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.whyq.R;


public class TwitterFragment extends DialogFragment {

	// Define constain
	public static final String TWITTER_KEY_LOGIN = "twitter_key_login";
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
	public static final String AUTH_URL = "http://twitter.com/oauth/authorize";
	public static final String CALLBACK_URL = "AMReader://twitter";
	public static WebView twitterWebView;
	private ProgressBar progessBar;
	public TwitterFragment() {
	}
	// Property
	public static CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
			Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
	public static OAuthProvider provider = new DefaultOAuthProvider(
			REQUEST_URL, ACCESS_TOKEN_URL, AUTH_URL);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_TITLE, 0);
	}
	
	protected boolean flag = false;
	private ImageButton btnClose;
	@Override
	public void onResume(){
		super.onResume();
		if(flag)
			end();
		flag = true;
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.twitter_intent, null);
		twitterWebView = (WebView)v.findViewById(R.id.twitter_web);
		progessBar = (ProgressBar)v.findViewById(R.id.twProgressBar);
		getDialog().getWindow().setLayout(300,300);
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		twitterWebView.clearCache(true);
		btnClose = (ImageButton)v.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				end();
			}
		});
		new OAuthRequestTokenTask(this.getActivity(), consumer, provider).doInBackground();
		return v;
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
				twitterWebView.loadUrl(url);
				progessBar.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				Log.e("a", "Error during OAUth retrieve request token", e);
			}
			return null;

		}
	}
	
	private Twitter twitter = null;
	private String ScreenName = null;
	protected void onNewIntent(Intent intent) {
//		super.getActivity().onNewIntent(intent);
		flag = false;
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
			String _verifier = uri.getQueryParameter("oauth_verifier");
//			 String _accessToken = uri.getQueryParameter("oauth_token");
			try {
				provider.retrieveAccessToken(consumer, _verifier);
				String accessKey = consumer.getToken();
				String accessSecret = consumer.getTokenSecret();
				AccessToken at = new AccessToken(accessKey, accessSecret);
				SharedPreferencesManager shareManager = new SharedPreferencesManager(
						this.getActivity());
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
//				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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
//		this.getActivity().finish();
		dismiss();
	}

}
