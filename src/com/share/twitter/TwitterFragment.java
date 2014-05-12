package com.share.twitter;
//package com.audiencemedia.reader.share.twitter;
//
//
//import twitter4j.Twitter;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.widget.ProgressBar;
//
//import com.audiencemedia.reader.AMReaderApplication;
//import com.audiencemedia.reader.Config;
//import com.audiencemedia.reader.R;
//import com.audiencemedia.reader.settings.SharedPreferencesManager;
//
//
//public class TwitterFragment extends DialogFragment {
//
//	// Define constain
//	public static final String TWITTER_KEY_LOGIN = "twitter_key_login";
//	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
//	public static final String ACCESS_TOKEN_URL = "http://twitter.com/oauth/access_token";
//	public static final String AUTH_URL = "http://twitter.com/oauth/authorize";
//	public static final String CALLBACK_URL = "AMReader://twitter";
//	public static WebView twitterWebView;
//	private ProgressBar progessBar;
//	public TwitterFragment() {
//	}
//	// Property
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setStyle(STYLE_NO_TITLE, 0);
//	}
//	
//	protected boolean flag = false;
//	@Override
//	public void onResume(){
//		super.onResume();
//		if(flag)
//			end();
//		flag = true;
//		
//	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View v = LayoutInflater.from(getActivity()).inflate(R.layout.twitter_intent, null);
//		twitterWebView = (WebView)v.findViewById(R.id.twitter_web);
//		progessBar = (ProgressBar)v.findViewById(R.id.twProgressBar);
//		getDialog().getWindow().setLayout(540,700);
//		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//		twitterWebView.clearCache(true);
//		new OAuthRequestTokenTask(this.getActivity(), consumer, provider).doInBackground();
//		return v;
//	}
//	private class OAuthRequestTokenTask{
//		private Context _context;
//		private CommonsHttpOAuthConsumer _consumer;
//		private OAuthProvider _provider;
//
//		public OAuthRequestTokenTask(Context context,
//				CommonsHttpOAuthConsumer consumer, OAuthProvider provider) {
//			// TODO Auto-generated constructor stub
//			_context = context;
//			_consumer = consumer;
//			_provider = provider;
//		}
//
//		protected Void doInBackground() {
//			try {
//				// TODO Auto-generated method stub
//				final String url = _provider.retrieveRequestToken(_consumer,
//						CALLBACK_URL);
//				twitterWebView.loadUrl(url);
//				progessBar.setVisibility(View.VISIBLE);
//			} catch (Exception e) {
//				Log.e("a", "Error during OAUth retrieve request token", e);
//			}
//			return null;
//
//		}
//	}
//	
//	private Twitter twitter = null;
//	private String ScreenName = null;
//	protected void onNewIntent(Intent intent) {
////		super.getActivity().onNewIntent(intent);
//		flag = false;
//		Uri uri = intent.getData();
//		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
//			String _verifier = uri.getQueryParameter("oauth_verifier");
////			 String _accessToken = uri.getQueryParameter("oauth_token");
//			try {
//				provider.retrieveAccessToken(consumer, _verifier);
//				String accessKey = consumer.getToken();
//				String accessSecret = consumer.getTokenSecret();
//				AccessToken at = new AccessToken(accessKey, accessSecret);
//				SharedPreferencesManager shareManager = new SharedPreferencesManager(
//						this.getActivity());
//				shareManager.saveTwitterToken(at);
//				twitter = shareManager.loadTwitter();
//				AMReaderApplication app = (AMReaderApplication)this.getActivity().getApplication();
//				app.putData(TWITTER_KEY_LOGIN, true);
//			} catch (OAuthMessageSignerException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (OAuthNotAuthorizedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (OAuthExpectationFailedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (OAuthCommunicationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
////				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//			}
//			
//			//startActivity(new Intent(this, AutoTweetActivity.class));
//		}
//		if (twitter != null) {
//			try {
//				ScreenName = getString(R.string.TwitterScreenName);
//				if (!twitter.existsFriendship(twitter.getScreenName(),
//						ScreenName)) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
//					builder.setTitle(getString(R.string.TwitterFollowingTittle));
//					builder.setCancelable(false)
//							.setPositiveButton(
//									getString(R.string.Ok),
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//											// here you can add functions
//											try {
//												twitter.createFriendship(ScreenName);
//												end();
//											} catch (TwitterException e) {
//												// TODO Auto-generated catch
//												// block
//												e.printStackTrace();
//											}
//										}
//									})
//							.setNegativeButton(
//									getString(R.string.Cancel),
//									new DialogInterface.OnClickListener() {
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//											// here you can add functions
//											end();
//										}
//									});
//					builder.setMessage(String.format(getString(R.string.TwitterFollowingMessage), ScreenName));
//					builder.create().show();
//				}
//				else{
//					end();
//				}
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (TwitterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//		else{
//			Log.d("s", "twitter = null");
//			end();
//		}
//		
//	}
//	private void end(){
////		this.getActivity().finish();
//		dismiss();
//	}
//
//}
