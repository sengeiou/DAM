package whyq.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.interfaces.IServiceListener;
import whyq.model.FriendFacebook;
import whyq.model.SearchFriendCriteria;
import whyq.model.ShareData;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.Logger;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Service implements Runnable {

	private static final String TAG = Service.class.getSimpleName();
	private HttpURLConnection _connection;
	private ServiceAction _action;
	private ArrayList<IServiceListener> _listener;
	private boolean _connecting;
	private Thread _thread;
	private String _actionURI;
	private Map<String, Object> _params;
	private boolean _includeHost;
	private boolean _isGet;
	private Service _service;
	private boolean _isBitmap;
	private HttpClient httpclient;

	public void getProductList() {
		_action = ServiceAction.ActionGetRetaurentList;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("deviceType", "kindle-fire");
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/v1/shop/purchase", params, true, false);
	}

	public void logout() {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLogout;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/logout", params, true, false);
	}

	public void postFavorite(String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionPostFavorite;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/favourite/business", params, true, false);
	}
	
	public void getDeliveryFeeList() {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetDeliveryFeeList;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/innscor/delivery", params, true, false);
	}

	
	public void removeFavorite(String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionRemoveFavorite;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/favourite/business", params, true, false);
	}

	public void getComments(String encryptedToken, String id, int page,
			int count, boolean onlyFriend, boolean isStore) {
		_action = ServiceAction.ActionGetComment;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		if(isStore){
			params.put("store_id", id);			
		}else{
			params.put("user_id", id);
		}
		params.put("page", String.valueOf(page));
		params.put("count", String.valueOf(count));
		params.put("only_friend", onlyFriend ? "1" : "");
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/comment", params, true, false);
	}

	public void getFriends(String token, String user_id) {
		// �Nedd add page and key = if search

		_action = ServiceAction.ActionGetFriends;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (user_id != null) {
			params.put("user_id", user_id);
		}
		request("/m/member/friend", params, true, false);
	}

	public void getInvitation(String token, String listInvited) {
		// �Nedd add page and key = if search

		_action = ServiceAction.ActionGetInvitations;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (listInvited != null) {
			params.put("list_invited", listInvited);
		}
		request("/m/member/friend", params, true, false);
	}

	public void getInvitationNotification(String token, String listInvited) {
		// �Nedd add page and key = if search

		_action = ServiceAction.ActionGetInvitationsNotification;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("list_invited", "");
		params.put("get_total", "");
		request("/m/member/friend", params, true, false);
	}

	public void getFriendsFacebook(String encryptedToken, String accessToken) {
		_action = ServiceAction.ActionGetFriendsFacebook;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("access_token", accessToken);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/facebook", params, true, false);
	}

	public void searchFriends(SearchFriendCriteria criteria,
			String encryptedToken, String key, String accessToken,
			String oauth_token, String oauth_token_secret) {
		_action = ServiceAction.ActionSearchFriendsFacebook;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("key", key);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("search", criteria.toString());
		switch (criteria) {
		case facebook:
			params.put("access_token", accessToken);
			break;
		case twitter:
			params.put("oauth_token", oauth_token);
			params.put("oauth_token_secret", oauth_token_secret);
			break;
		}
		request("/m/member/search/friend", params, true, false);
	}

	public void inviteFriendsFacebook(String encryptedToken, String userId,
			String accessToken) {
		_action = ServiceAction.ActionInviteFriendsFacebook;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("access_token", accessToken);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/facebook/invite", params, true, false);
	}

	public void getUserActivities(String encryptedToken, String userId, int page) {
		_action = ServiceAction.ActionGetUserActivities;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("page", page);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/recent/activity", params, true, false);
	}

	public void postComment(String encryptedToken, String storeId,
			String content, String photoFile) {
		_action = ServiceAction.ActionPostComment;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("store_id", storeId);
		params.put("content", content);

		try {
			File file = new File(photoFile);
			if (file.exists()) {
				FileBody encFile = new FileBody(file, "image/png");
				params.put("photo", encFile);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/comment/post", params, true, false);
	}

	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			for (IServiceListener listener : _listener) {
				if (listener != null)
					listener.onCompleted(_service, (ServiceResponse) msg.obj);
			}

		}
	};

	public Service(IServiceListener... listeners) {
		_action = ServiceAction.ActionNone;
		_listener = new ArrayList<IServiceListener>();
		if (listeners != null)
			for (IServiceListener listener : listeners) {
				_listener.add(listener);
			}
		_connecting = false;
		_includeHost = true;
		_service = this;
		_isBitmap = false;
	}

	public void addListener(IServiceListener listener) {
		_listener.add(listener);
	}

	public boolean isConnecting() {
		return _connecting;
	}

	public void stop() {
		// cleanUp();
		if (httpclient != null)
			httpclient.getConnectionManager().shutdown();
		_action = ServiceAction.ActionNone;
		_connecting = false;
	}

	private boolean request(String uri, Map<String, Object> params,
			boolean includeHost, boolean isGet) {
		_isGet = isGet;
		request(uri, params, includeHost);
		return true;
	}

	private boolean request(String uri, Map<String, Object> params,
			boolean includeHost) {
		if (_connecting)
			return false;
		_connecting = true;
		_actionURI = uri;
		_params = params;
		_includeHost = includeHost;
		_thread = new Thread(this);
		_thread.start();
		return true;
	}

	private void processError(ResultCode errorCode) {
		// if (_listener == null || _action == ServiceAction.ActionNone
		// || !_connecting)
		// return;
		Message msg = handler.obtainMessage(0, new ServiceResponse(_action,
				null, errorCode));
		handler.sendMessage(msg);
	}

	private void dispatchResult(String result) {
		if (_listener == null || _action == ServiceAction.ActionNone
				|| !_connecting)
			return;

		ServiceAction act = _action;
		Object resObj = null;
		ServiceResponse response = null;
		DataParser parser = new DataParser();
		switch (act) {
		// TODO
		case ActionNone:
			break;
		case ActionGetRetaurentList:
			resObj = parser.parseRetaurentList();
			break;
		case ActionLogout:
			resObj = parser.parseLogout();
			break;
		case ActionLoginFacebook:
			result = result.replace("<0></0>", "");
			resObj = parser.parserLoginData(result);
			break;
		case ActionLoginWhyq:
			result = result.replace("<0></0>", "");
			resObj = parser.parserLoginData(result);
			break;
		case ActionLoginasGuest:
			result = result.replace("<0></0>", "");
			resObj = parser.parserLoginData(result);
			break;
		case ActionSigup:
			resObj = parser.parserSignupResult(result);
			break;
		case ActionEditProfile:
			resObj = parser.parserLoginData(result);
			break;
		case ActionPushNotification:
			resObj = parser.parserLoginData(result); // same struct to login api
			break;
		case ActionRegisterDeviceToPushServer:
			resObj = parser.parserLoginData(result); // same struct to login api
			break;
		case ActionGetInvitations:
			resObj = parser.parseLUserCheckedResult(result);// Same structor
															// data
			break;
		case ActionGetInvitationsNotification:
			resObj = parser.parseInvitationNotification(result);// Same structor
																// data
			break;
		case ActionPostComment:
			resObj = parser.parseOrderCheck(result);// Same structor
													// data
			break;

		case ActionAcceptInvitation:
			resObj = parser.parseAcceptInvitation(result);// Same structor
															// data
			break;
		case ActionDeclineInvitation:
			resObj = parser.parseAcceptInvitation(result);// Same structor
															// data
			break;
		case ActionUnFriend:
			resObj = parser.parseAcceptInvitation(result);// Same structor
															// data
			break;
		case ActionOrderCheck:
			resObj = parser.parseOrderCheck(result);
			break;
		case ActionDeleteFriend:
			resObj = parser.parserDeleteFriend(result);
			break;
		case ActionOrderSend:
			resObj = parser.parserOrderSend(result);
			break;
		case ActionGetBillDetail:
			resObj = parser.parserBillDetailResultSend(result);
			break;
		case ActionGetDistance:
			resObj = parser.parserDistanceResultSend(result);
			break;
		case ActionInviteFriendsWhyQ:
			resObj = parser.parseFriendWhyq(result);
			break;
		case ActionLoginTwitter:
			resObj = parser.parserLoginData(result);
			break;
		case ActionGetBusinessDetail:
			resObj = parser.parseBusinessDetail(result);
			break;
		case ActionGetFaqs:
			resObj = parser.parseFaqsResult(result);
			break;
		case ActionGetBusinessList:
			resObj = parser.parseBusinessList(result);
			break;
		case ActionComment:
			resObj = parser.parseCommentResult(result);
			break;
		case ActionGetLocation:
			resObj = parser.parseLCationResult(result);
			break;
		case ActionGetDeliveryFeeList:
			resObj = parser.parseLGetDeliveryFeeList(result);
			break;
		case ActionPostFavorite:
			resObj = parser.parseLFavouriteResult(result);
			break;
		case ActionOrderEcoCash:
			resObj = parser.parseOrderEcoCash(result);
			break;
		case ActionRemoveFavorite:
			resObj = parser.parseLFavouriteResult(result);
			break;
		case ActionForgotPassword:
			resObj = parser.parseLResetPasswordResult(result);
			break;
		case ActionGetUserChecked:
			resObj = parser.parseLUserCheckedResult(result);
			break;
		case ActionCheckbill:
			resObj = parser.parserCheckBillResult(result);
			break;
		case ActionPostFBComment:
			resObj = parser.parserPostOpenGraphResult(result);
			break;
		case ActionPostFBCheckBill:
			resObj = parser.parserPostOpenGraphResult(result);
			break;
		case ActionPostFBCheckDiscountBill:
			resObj = parser.parserPostOpenGraphResult(result);
			break;
		case ActionPostFBAdd:
			resObj = parser.parserPostOpenGraphResult(result);
			break;
		case ActionGetFavouriteFoods:
			resObj = parser.parseFavouriteFood(result);
			break;
		case ActionPostFavouriteFoods:
			resObj = parser.parseLFavouriteResult(result);
			break;
		default:
			resObj = result;
			Logger.appendLog(result);
			break;
		}
		if (resObj == null)
			response = new ServiceResponse(act, null, ResultCode.Failed);
		else
			response = new ServiceResponse(act, resObj);
		stop();
		Message msg = handler.obtainMessage(0, response);
		handler.sendMessage(msg);
	}

	private void dispatchResult(Bitmap result) {
		if (_listener == null || _action == ServiceAction.ActionNone
				|| !_connecting)
			return;
		ServiceAction act = _action;
		ServiceResponse response = null;
		if (result == null)
			response = new ServiceResponse(act, null, ResultCode.Failed);
		else
			response = new ServiceResponse(act, result);
		stop();
		Message msg = handler.obtainMessage(0, response);
		handler.sendMessage(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Log.d(_action.toString(), "=========== run ===========\n" + _actionURI);
		httpclient = new DefaultHttpClient();
		// AndroidHttpClient httpclient = AndroidHttpClient
		// .newInstance(_actionURI);
		HttpParams httpParameters = httpclient.getParams();
		// HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
		HttpConnectionParams.setSoTimeout(httpParameters, 600000);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);
		try {
			final String urlString;
			if (_action == ServiceAction.ActionGetLocation) {
				String textSearch = (String) _params.get("text-search");
				textSearch = textSearch.replace(" ", "+");
				urlString = "http://ws.geonames.org/search?q=" + textSearch
						+ "&style=full&maxRows=10";
			} else if (_action == ServiceAction.ActionGetDistance) {
				urlString = _actionURI;
			} else if (_action == ServiceAction.ActionPostFBComment) {
				urlString = _includeHost ? API.hostFbOpenGraph + _actionURI
						: _actionURI;
			} else if (_action == ServiceAction.ActionPostFBCheckBill) {
				urlString = _includeHost ? API.hostFbOpenGraph + _actionURI
						: _actionURI;
			} else if (_action == ServiceAction.ActionPostFBCheckDiscountBill) {
				urlString = _includeHost ? API.hostFbOpenGraph + _actionURI
						: _actionURI;
			} else if (_action == ServiceAction.ActionPostFBAdd) {
				urlString = _includeHost ? API.hostFbOpenGraph + _actionURI
						: _actionURI;
			} else
				urlString = _includeHost ? API.hostURL + _actionURI
						: _actionURI;
			HttpRequestBase request = null;
			Log.d("Service", "url: " + urlString + " " + _isGet);
			if (_isGet) {
				request = new HttpGet();
				if (_params != null) {
					attachUriWithQuery(request, Uri.parse(urlString), _params);
				}
			} else {
				request = new HttpPost(urlString);
				if (_params != null) {
					MultipartEntity reqEntity = paramsToList2(_params);

					// UrlEncodedFormEntity formEntity = new
					// UrlEncodedFormEntity(
					// paramsToList(_params), HTTP.UTF_8);
					((HttpPost) request).setEntity(reqEntity);
				}
			}

			// Set default headers
			HttpResponse response = httpclient.execute(request);

			InputStream in = null;

			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				Header[] header = response.getHeaders("Content-Encoding");
				if (header != null && header.length != 0) {
					for (Header h : header) {
						if (h.getName().trim().equalsIgnoreCase("gzip"))
							in = new GZIPInputStream(response.getEntity()
									.getContent());
					}
				}

				if (in == null)
					in = new BufferedInputStream(response.getEntity()
							.getContent());

				if (_isBitmap) {
					Bitmap bm = BitmapFactory.decodeStream(in);
					dispatchResult(bm);
				} else {
					String temp = convertStreamToString(in);// text.toString();
					Log.d(_action.toString(), "==" + temp + "");
					in.close();
					dispatchResult(temp);
				}

			} else if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_NOT_FOUND)
				processError(ResultCode.Failed);
			else if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_SERVER_ERROR)
				processError(ResultCode.ServerError);
			else
				processError(ResultCode.NetworkError);

		} catch (Exception e) {
			e.printStackTrace();
			processError(ResultCode.NetworkError);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}

	private String convertStreamToString(InputStream is) {
		// TODO Auto-generated method stub

		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (IOException e) {
				return "";
			} finally {
				try {
					is.close();
				} catch (IOException e) {

				}
			}
			return writer.toString();
		} else {
			return "";
		}

	}

	public int getConnectionTimeout() {
		return _connection.getConnectTimeout();
	}

	public void loginFacebook(String accessToken) {
		_action = ServiceAction.ActionLoginFacebook;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", accessToken);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/login/fb", params, true, false);
	}

	public void loginTwitter(String oauthToken, String oauthTokenSecret) {
		// TODO Auto-generated method stub
		Log.d("loginTwitter", "loginTwitter");
		_action = ServiceAction.ActionLoginTwitter;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oauth_token", oauthToken);
		params.put("oauth_token_secret", oauthTokenSecret);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/login/tw", params, true, false);
	}

	public void inviteFriendsTwitter(String encryptedToken, String oauth_token,
			String oauth_token_secret, String twitter_id) {
		_action = ServiceAction.ActionInviteFriendsTwitter;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("oauth_token", oauth_token);
		params.put("oauth_token_secret", oauth_token_secret);
		params.put("twitter_id", twitter_id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/twitter/invite", params, true, false);
	}

	public void inviteFriendsWhyq(String encryptedToken, String userId) {
		_action = ServiceAction.ActionInviteFriendsWhyQ;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/invite", params, true, false);
	}

	private void attachUriWithQuery(HttpRequestBase request, Uri uri,
			Map<String, Object> params) {
		try {
			if (params == null) {
				// No params were given or they have already been
				// attached to the Uri.
				request.setURI(new URI(uri.toString()));
			} else {
				Uri.Builder uriBuilder = uri.buildUpon();

				// Loop through our params and append them to the Uri.
				for (BasicNameValuePair param : paramsToList(params)) {
					uriBuilder.appendQueryParameter(param.getName(),
							param.getValue());
				}

				uri = uriBuilder.build();
				request.setURI(new URI(uri.toString()));
			}
		} catch (URISyntaxException e) {
			Log.e(TAG, "URI syntax was incorrect: " + uri.toString());
		}
	}

	private static List<BasicNameValuePair> paramsToList(
			Map<String, Object> params) {
		ArrayList<BasicNameValuePair> formList = new ArrayList<BasicNameValuePair>(
				params.size());

		for (String key : params.keySet()) {
			Object value = params.get(key);

			if (value != null)
				formList.add(new BasicNameValuePair(key, value.toString()));
		}

		return formList;
	}

	public static MultipartEntity paramsToList2(Map<String, Object> params) {
		MultipartEntity reqEntity = new MultipartEntity();
		for (String key : params.keySet()) {
			try {

				Object value = params.get(key);
				if (key.toUpperCase().equals("PHOTO") || key.toUpperCase().equals("IMAGE")) {
					reqEntity.addPart(key, (ContentBody) value);
				} else {
					Charset chars = Charset.forName("UTF-8");
					reqEntity.addPart(key, new StringBody(value.toString(),
							chars));
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		return reqEntity;
	}

	public void getBusinessList(HashMap<String, String> params, String url) {
		_action = ServiceAction.ActionGetBusinessList;
		request(url, convert(params), true, false);
	}

	public void getBusinessDetail(String id) {
		_action = ServiceAction.ActionGetBusinessDetail;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/business/show", params, true, false);
	}

	public void getFaqs(String id) {
		_action = ServiceAction.ActionGetFaqs;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		request("/m/faqs", params, true, false);
	}

	public void getCheckedBills(String encryptedToken, String store_id) {
		_action = ServiceAction.ActionGetBills;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/business/member/check_bill", params, true, false);
	}

	public void getUserCheckedBills(String encryptedToken, String store_id,
			String text) {
		_action = ServiceAction.ActionGetUserChecked;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("store_id", store_id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		if (text != null) {
			params.put("key", text);
			request("/m/business/member/check_bill/search", params, true, false);
		} else {
			request("/m/business/member/check_bill", params, true, false);
		}

	}

	public void getOrder(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetOrder;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile/order", params, true, false);
	}

	public void getHistories(String encryptedToken, String userId, int page) {
		_action = ServiceAction.ActionGetHistories;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("page", page);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/order", params, true, false);
	}

	public void exeComment(String string, String storeId) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionComment;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("store_id", storeId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/business/show", params, true, false);

	}
	
	public void getFavouriteFoods(int page) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetFavouriteFoods;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("page", page);
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/product/like", params, true, false);
	}
	
	public void postLikeFavouriteFoods(String id) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionPostFavouriteFoods;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("product_id", id);
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/product/like", params, true, false);
	}

	public void orderDelivery(String storeId, String otherAddress,
			String phoneNumber, String hours, String minutes) {
		// TODO Auto-generated method stub

	}

	public void getPhotos(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetPhotos;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/profile/photo", params, true, false);
	}

	public void setLocation(String string) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetLocation;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("text-search", string);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("getlocation", params, true, false);
	}

	public void getProfiles(String encryptedToken, String userId) {
		_action = ServiceAction.ActionGetProfiles;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/profile", params, true, false);
	}

	public void exeResetPass(String string) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionForgotPassword;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", string);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/forget_password", params, true, false);
	}

	public void loginWhyq(String email, String pass) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLoginWhyq;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/login", params, true, false);
		params.put("email", email);
		params.put("password", pass);
	}

	public void loginasGuest(String deviceToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionLoginasGuest;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("device_token", deviceToken);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/login/guest", params, true, false);
	}
	
	public void register(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionSigup;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/register", params, true, false);
	}

	public void editProfile(HashMap<String, String> params2) {
		// TODO Auto-generated method stub
		// first_name, las_name,token,new_password,old_password
		HashMap<String, Object> params = convert(params2);
		_action = ServiceAction.ActionEditProfile;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile/edit", params, true, false);
	}

	private HashMap<String, Object> convert(HashMap<String, String> params2) {
		// TODO Auto-generated method stub
		HashMap<String, Object> result = new HashMap<String, Object>();
		Set<Entry<String, String>> set = params2.entrySet();
		Iterator<Entry<String, String>> interator = set.iterator();
		while (interator.hasNext()) {
			Entry<String, String> item = interator.next();
			result.put(item.getKey(), item.getValue());

		}
		return result;
	}

	public void pushNotification(String isReceiveNotify, String isShowFriend) {
		// TODO Auto-generated method stub
		// value is 0 or 1 for isReceivedNotify and isShowFriend
		HashMap<String, Object> params = new HashMap<String, Object>();
		_action = ServiceAction.ActionPushNotification;
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		params.put("is_receive_notification", isReceiveNotify);
		params.put("is_show_friend", isReceiveNotify);
		request("/m/member/setting/edit", params, true, false);
	}

	public void registerDeviceToPushNotificationServer(String appName,
			String appVersion, String deviceName, String deviceModel,
			String installationId, String objectId) {
		// TODO Auto-generated method stub
		// value is 0 or 1 for isReceivedNotify and isShowFriend
		HashMap<String, Object> params = new HashMap<String, Object>();
		_action = ServiceAction.ActionRegisterDeviceToPushServer;
		params.put("token", "" + WhyqApplication.Instance().getRSAToken());
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("appname", Constants.APP_NAME);
		params.put("env", Constants.DEVELOPMENT);//pro
		params.put("appversion", appVersion);
		params.put("devicename", deviceName);
		params.put("devicemodel", deviceModel);
		params.put("installationId", installationId);
		params.put("objectId", objectId);
		request("/m/member/device/register", params, true, false);
	}

	public void searchOnlyFriend(SearchFriendCriteria criteria,
			String encryptedToken, String key, String accessToken,
			String oauth_token, String oauth_token_secret) {
		_action = ServiceAction.ActionSearchOnlyFriend;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("key", key);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		params.put("search", criteria.toString());
		request("/m/member/search/friend", params, true, false);
	}

	public void deleteFriend(String userId, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionDeleteFriend;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/profile", params, true, false);
	}

	public void orderSend(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		// store_id, deliver_type(1: tike away, 4: dinein),
		// list_items,deliver_to(empty for typle=1 and 0 if type =4),
		// time_zone,time_deliver(if not empty, oly for type =1),note(commend),
		// token
		_action = ServiceAction.ActionOrderSend;
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		if (ListDetailActivity.commentContent != null) {
			params.put("note", ListDetailActivity.commentContent);
		}
		request("/m/member/order/send", convert(params), true, false);
	}

	public void getBillDetail(String billId, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetBillDetail;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("bill_id", billId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/order/show", params, true, false);
	}

	public void getDistance(HashMap<String, String> params) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionGetDistance;
		request(Constants.GET_DISTANCE_API, convert(params), true, true);

	}

	public void checkBill(String billId, String message, String encryptedToken) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionCheckbill;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", encryptedToken);
		params.put("bill_id", billId);
		params.put("message", message);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/order/show", params, true, false);
	}

	public void ecoCash(String billId, String usingEcocash) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionOrderEcoCash;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", WhyqApplication.Instance().getRSAToken());
		params.put("bill_id", billId);
		params.put("using_cash", usingEcocash);
		params.put("hotel_charge_code", "");
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());
		request("/m/member/order/update", params, true, false);
	}
	
	
	public void getPaypalURI(String token, String billId) {
		// TODO Auto-generat ed method stub
		_action = ServiceAction.ActionGetPaypalURI;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("bill_id", billId);
		params.put("drt", "true");
		params.put("currencyCode", "AUD");
		if (Constants.isSaxbox)
			params.put("env", Constants.isSaxbox ? "dev" : "live");
		params.put("app", Constants.APP);

		params.put("app_name", Constants.APP_NAME);
		request("/m/expressCheckout/set", params, true, false);
	}

	public void acceptInvitation(String token, String userId) {
		_action = ServiceAction.ActionAcceptInvitation;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/accept", params, true, false);
	}

	public void declineInvitation(String token, String userId) {
		_action = ServiceAction.ActionDeclineInvitation;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		params.put("user_id", userId);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/decline", params, true, false);
	}

	public void unFriend(String rsaToken, String id) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionUnFriend;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", rsaToken);
		params.put("user_id", id);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		request("/m/member/friend/delete", params, true, false);
	}

	public void pushOrderCheck(String rsaToken, String billId,
			String facebookId, String message, String image) {
		// TODO Auto-generated method stub
		_action = ServiceAction.ActionOrderCheck;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", rsaToken);
		params.put("bill_id", billId);
		if (facebookId != null)
			params.put("facebook_id ", facebookId);
		params.put("message", message);
		try {
			File file = new File(image);
			if (file.exists()) {
				FileBody encFile = new FileBody(file, "image/png");
				params.put("image", encFile);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		params.put("image", image);
		params.put("app", Constants.APP);
		params.put("app_name", Constants.APP_NAME);
		params.put("version", Constants.APP_VERSION);
		params.put("time_zone", TimeZone.getDefault());

		request("/m/member/order/check", params, true, false);
	}

	public void postFBComments(String accessToken, ShareData data) {
		Log.d("postFBComments","accessToken "+accessToken);
		_action = ServiceAction.ActionPostFBComment;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", accessToken);
		params.put("fb:explicitly_shared", true);
		params.put("format", "json");
		if(data.getPicture()!=null && !"".equals(""+data.getPicture())){
			params.put("image[0][url]", data.getPicture());
			params.put("image[0][user_generated]", true);
		}
		params.put("message", "" + data.getMessage());
		params.put("place", data.getLink());
		params.put("scrape", true);
		params.put("sdk", "android");
		params.put("sdk_version", 1);
		params.put("venue", data.getLink());

		request("/dial_a_delivery:comment", params, true, false);
	}

	public void postFBCheckBill(String accessToken, ShareData data) {
		_action = ServiceAction.ActionPostFBCheckBill;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", accessToken);
		params.put("fb:explicitly_shared", true);
		params.put("format", "json");
		if(data.getPicture()!=null && !"".equals(""+data.getPicture())){
			params.put("image[0][url]", data.getPicture());
			params.put("image[0][user_generated]", true);
		}
		params.put("message", data.getMessage()+ " " + convertArrayToFBArray(data.getTags()));
		params.put("place", data.getLink());
		params.put("scrape", true);
		params.put("sdk", "android");
		params.put("sdk_version", 1);
		params.put("tags", convertArrayToString(data.getTags()));
		params.put("venue", data.getLink());

		request("/dial_a_delivery:check_bill", params, true, false);
	}

	private String convertArrayToString(ArrayList<String> data) {
		// TODO Auto-generated method stub
		if (data != null) {
			List<String> list = (List<String>) data;
			String result = "";
			for (int i = 0; i < list.size(); i++) {
				String item = "";
				item = list.get(i);
				if (i == 0)
					result += "" + item;
				else
					result += "," + item;
			}
			Log.d("convertArrayToString", "result" + result);
			return result;
		} else {
			return null;
		}
	}
	
	private String convertArrayToFBArray(ArrayList<String> data) {
		// TODO Auto-generated method stub
		if (data != null) {
			ArrayList<String> list = (ArrayList<String>) data;
			String result = "";
			for (int i = 0; i < list.size(); i++) {
				String item = "";
				item = list.get(i);
				item="@["+item+"]";
				if (i == 0)
					result += "" + item;
				else
					result += "," + item;
			}
			Log.d("convertArrayToString", "result" + result);
			return result;
		} else {
			return "";
		}
	}

	public void postFBCheckDiscountBill(String encryptedToken, ShareData data) {
		_action = ServiceAction.ActionPostFBCheckDiscountBill;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", encryptedToken);
		params.put("fb:explicitly_shared", true);
		params.put("format", "json");
		params.put("image[0][url]", data.getPicture());
		params.put("message", "" + data.getMessage());
		params.put("place", data.getLink());
		params.put("scrape", true);
		params.put("sdk", "android");
		params.put("sdk_version", 1);
		params.put("venue", data.getLink());

		request("/whyqapp:check_discount_bill", params, true, false);
	}

	public void postFBAdd(String encryptedToken, ShareData data) {
		_action = ServiceAction.ActionPostFBAdd;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", encryptedToken);
		params.put("fb:explicitly_shared", true);
		params.put("format", "json");
		params.put("image[0][url]", data.getPicture());
		params.put("message", "" + data.getMessage());
		params.put("place", data.getLink());
		params.put("scrape", true);
		params.put("sdk", "android");
		params.put("sdk_version", 1);
		params.put("venue", data.getLink());

		request("/whyqapp:add", params, true, false);
	}
}
