package whyq.utils;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import whyq.utils.facebook.sdk.Facebook;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class SharedPreferencesManager {

	private Context _context;
	private SharedPreferences _sharedPreferences;
	private Editor _editor;

	public static final String CODE_ENABLE_LYRIC = "lyric-enable";
	public static final String CODE_EDIT_METADATA = "edit-metadata";
	public static final String CODE_ENABLE_MAP = "show-on-map-enable";
	public static final String CODE_ENABLE_VIBRATION = "vibration-enable";
	public static final String CODE_ENABLE_MOBION_COM_VIBRATION = "mobion-com-vibration";

	public SharedPreferencesManager(Context context) {
		_context = context;
		_sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(_context);
	}




	// ------------------------------TWITTER-------------------------------
	public Twitter loadTwitter() {
		Twitter _twitter = new TwitterFactory().getInstance();
		_twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);

		AccessToken accessToken = loadTwitterToken();
		if (accessToken == null)
			return null;

		_twitter.setOAuthAccessToken(accessToken);
		return _twitter;
	}

	public boolean saveTwitterToken(AccessToken token) {
		Editor editor = _context.getSharedPreferences("TWITTER",
				Context.MODE_PRIVATE).edit();
		if(token.getToken() != null)
			editor.putString("twitter_key", token.getToken());
		if(token.getTokenSecret() != null)
			editor.putString("twitter_secret", token.getTokenSecret());
		return editor.commit();
	}

	public AccessToken loadTwitterToken() {
		SharedPreferences savedSession = _context.getSharedPreferences(
				"TWITTER", Context.MODE_PRIVATE);
		String key = savedSession.getString("twitter_key", "");
		String secret = savedSession.getString("twitter_secret", "");
		if (key == "" || secret == "") {
			return null;
		}
		return new AccessToken(key, secret);
	}
	public void clearTwitter() {
		Editor editor = _context.getSharedPreferences("TWITTER",
				Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}
	// ------------------------FACE BOOK---------------------------
	public boolean saveFacebook(Facebook facebook) {
		Editor editor = _context.getSharedPreferences("FACEBOOK",
				Context.MODE_PRIVATE).edit();
		editor.putString("facebook_token", facebook.getAccessToken());
		editor.putLong("expires", facebook.getAccessExpires());
		return editor.commit();
	}

	public void clearFacebook() {
		Editor editor = _context.getSharedPreferences("FACEBOOK",
				Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}

	public Facebook loadFacebook() {
		SharedPreferences savedSession = _context.getSharedPreferences(
				"FACEBOOK", Context.MODE_PRIVATE);
		Facebook facebook = new Facebook(Constants.FACEBOOK_APP_ID);
		facebook.setAccessToken(savedSession.getString("facebook_token", null));
		facebook.setAccessExpires(savedSession.getLong("expires", 0));
		if (facebook.isSessionValid())
			return facebook;
		else
			return null;
	}

	

	public SharedPreferences getPrefs() {
		return _sharedPreferences;
	}
}