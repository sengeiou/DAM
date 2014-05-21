package whyq.activity;

import twitter4j.http.AccessToken;
import whyq.WhyqApplication;
import whyq.interfaces.IFacebookLister;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import whyq.utils.facebook.SessionLoginFragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.share.twitter.TwitterActivity;
import com.dam.R;

public class WhyqFindMenuActivity extends FragmentActivity implements
		IServiceListener, IFacebookLister {

	private static final String PENDING_REQUEST_BUNDLE_KEY = "com.whyq:PendingRequest";
	private TextView tvTitle;
	private Session session;
	private Context context;
	private boolean pendingRequest;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_find_menu);
		tvTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		tvTitle.setText("Find Friends");
		context = this;
	}

	public void findFromTwitterClicked(View v) {
		SharedPreferencesManager shareManager = new SharedPreferencesManager(
				WhyqApplication.Instance().getApplicationContext());
		AccessToken twitterAccess = shareManager.loadTwitterToken();
		// mTwitterAccess = getTwitterAccess();

		// String token_secret = mTwitterAccess.getTokenSecret();
		Log.d("WhyqFindMenu", "Twitter Access ======>" + twitterAccess);
		if (twitterAccess != null) {

			// String token = mTwitterAccess.getToken();
			// String token_secret = mTwitterAccess.getTokenSecret();
			// exeLoginTwitter(token, token_secret);
			Intent iFriendsTwitter = new Intent(WhyqFindMenuActivity.this,
					WhyqFriendsTwitterActivity.class);
			startActivity(iFriendsTwitter);
		} else {
			Intent iTwitter = new Intent(WhyqFindMenuActivity.this,
					TwitterActivity.class);
			startActivity(iTwitter);
			// startActivityForResult(iTwitter, LOGIN_TWITTER);
		}
	}

	public void findFromFaccebookClicked(View v) {
		// if (getAccessToken() == null) {
		// Facebook mFacebook;
		// mFacebook = new Facebook(Constants.FACEBOOK_APP_ID);
		// final Activity activity = this;
		// mFacebook.authorize(activity, new String[] { "email",
		// "status_update",
		// "user_birthday" }, new DialogListener() {
		// @Override
		// public void onComplete(Bundle values) {
		// WhyqUtils permutils = new WhyqUtils();
		// String accessToken = values.getString(Facebook.TOKEN);
		// permutils.saveFacebookToken("oauth_token", accessToken,
		// getApplication());
		// exeLoginFacebook(accessToken);
		// }
		//
		// @Override
		// public void onFacebookError(FacebookError error) {
		//
		// }
		//
		// @Override
		// public void onError(DialogError e) {
		//
		// }
		//
		// @Override
		// public void onCancel() {
		// // cancel press or back press
		// }
		// });
		// } else {
		// Intent intent = new Intent(this, WhyqFriendsFacebookActivity.class);
		// startActivity(intent);
		// finish();
		// }

		try {
			session = Util.createSession();
			if (session.isOpened()) {
				Intent intent = new Intent(context, WhyqFriendsFacebookActivity.class);
				 startActivity(intent);
				 finish();
			} else {
				Util.onLogoutFacebook();
				StatusCallback callback = new StatusCallback() {
					public void call(Session session, SessionState state,
							Exception exception) {
						if (exception != null) {
							new AlertDialog.Builder(context)
									.setTitle(R.string.login_text1)
									.setMessage(exception.getMessage())
									.setPositiveButton(R.string.ok, null)
									.show();
							session = Util.createSession();
						}
					}
				};
				pendingRequest = true;
				session.openForRead(new Session.OpenRequest(
						WhyqFindMenuActivity.this).setCallback(callback));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (session
				.onActivityResult(this, requestCode, resultCode, data)
				&& pendingRequest && this.session.getState().isOpened()) {
			Intent intent = new Intent(context, WhyqFriendsFacebookActivity.class);
			 startActivity(intent);
			 finish();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		pendingRequest = savedInstanceState.getBoolean(
				PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
	}

	private void gotoFacebookFriend() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, WhyqFriendsFacebookActivity.class);
		startActivity(intent);
		finish();
	}

	private void showFacebookLogin() {
		// TODO Auto-generated method stub
		SessionLoginFragment fragment = new SessionLoginFragment();
		getSupportFragmentManager().beginTransaction()
				.add(fragment, "facebook_login").commit();
	}

	public void exeLoginFacebook(String accessToken) {
		Service service = new Service(this);
		service.loginFacebook(accessToken);
	}

	public void lnFindFromFW(View v) {
		Intent intent = new Intent(WhyqFindMenuActivity.this, LoginHome.class);
		startActivity(intent);
	}

	public void searchByNameClicked(View v) {
		Intent intent = new Intent(WhyqFindMenuActivity.this,
				WhyqFriendsActivity.class);
		intent.putExtra("is_search_by_name", true);
		startActivity(intent);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		if (result.isSuccess() == true
				&& result.getAction() == ServiceAction.ActionLoginFacebook) {
			ListActivity.isLogin = true;
			ListActivity.loginType = 1;
			Log.d("LoginHome by Facebook", "result: " + result.getData());

			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
				User user = (User) data.getData();
				XMLParser
						.storeUserAccount(WhyqApplication._instance
								.getApplicationContext(), user);
				Intent intent = new Intent(this,
						WhyqFriendsFacebookActivity.class);
				startActivity(intent);
				finish();
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else {
				// Util.showDialog(getParent(), data.getMessage());
			}

		}
	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}

	public void onBack(View v) {
		finish();
	}

	@Override
	public void onCompled(boolean b) {
		// TODO Auto-generated method stub
		if (b) {
			gotoFacebookFriend();
		}
	}
}
