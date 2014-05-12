//package whyq.utils.gcm;
//
//import java.util.HashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import jp.co.yahoo.android.YAuctionPad.R;
//import jp.co.yahoo.android.YAuctionPad.apis.SimpleXmlRequest;
//import jp.co.yahoo.android.YAuctionPad.common.ErrorManager;
//import jp.co.yahoo.android.YAuctionPad.common.GCMConstant;
//import jp.co.yahoo.android.YAuctionPad.common.YHOApplication;
//import jp.co.yahoo.android.YAuctionPad.common.YHOCommonConstant;
//import jp.co.yahoo.android.YAuctionPad.common.YHOServerConfig;
//import jp.co.yahoo.android.YAuctionPad.entities.EditNotificationSetting;
//import jp.co.yahoo.android.YAuctionPad.entities.NotificationSettings;
//import jp.co.yahoo.android.YAuctionPad.entities.PushNotificationRegistration;
//import jp.co.yahoo.android.YAuctionPad.gcm.GCMManager;
//import jp.co.yahoo.android.YAuctionPad.gcm.GCMManager.GCMRegistratrionListener;
//import jp.co.yahoo.android.YAuctionPad.utils.GeneralUtility;
//import jp.co.yahoo.android.YAuctionPad.utils.LogUtil;
//import jp.co.yahoo.android.YAuctionPad.yconnect.YConnectServiceManager;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.TextView;
//import android.widget.ToggleButton;
//
//import com.android.volley.Request.Method;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//
//public class SettingPushDialogFragment extends AbstractDialog implements OnClickListener {
//	private static final String TAG = SettingPushDialogFragment.class.getCanonicalName();
//
//	public static final String AUCTION_CLOSE = "auction_closed";
//	public static final String BID_REINSTATED = "bid_reinstated";
//	public static final String CONTACT_FORM = "contact_form";
//	public static final String IS_AUTO_SET = "isAutoSet";
//	public static final String NOTIFICATION_SETTING = "notification_setting";
//	public static final String OUTBID = "outbid";
//	public static final String TIME = "time";
//	public static final String WATCHLIST_REMIND = "watch_list_remind";
//
//	private boolean auction_closed;
//	private boolean bid_reinstated;
//	private boolean contact_form;
//	private ErrorListener errorListener = new ErrorListener() {
//
//		@Override
//		public void onErrorResponse(VolleyError arg0) {
//			setProgressDialogVisibility(false);
//			if (arg0.getMessage() != null && arg0.getMessage().contains("Error")) {
//				int start = arg0.getMessage().indexOf("<Message>");
//				int end = arg0.getMessage().indexOf("</Message>");
//				String message = arg0.getMessage().substring(start + "<Message>".length(), end);
//				GeneralUtility.showSimpleDialog(getActivity(), "ã‚¨ãƒ©ãƒ¼", message, "OK");
//			} else {
//				ErrorManager.processErrorResponse(getActivity(), arg0);
//			}
//		}
//	};
//
//	private ErrorListener errorListenerSendRegistrationIdToBackend = new ErrorListener() {
//
//		@Override
//		public void onErrorResponse(VolleyError arg0) {
//			setProgressDialogVisibility(false);
//			if (arg0.networkResponse != null && arg0.networkResponse.statusCode == 400) {
//				String data = new String(arg0.networkResponse.data);
//				if (!data.contains("ALREADY_EXIST")) {
//					int start = data.indexOf("<Message>");
//					int end = data.indexOf("</Message>");
//					if (start >= 0 && end >= 0 && end >= start) {
//						String message = data.substring(start + "<Message>".length(), end);
//						GeneralUtility.showSimpleDialog(getActivity(), "ã‚¨ãƒ©ãƒ¼", message, "OK");
//					}
//				}
//			} else {
//				showCommunicationError();
//			}
//			if (arg0.networkResponse != null && arg0.networkResponse.statusCode == 401) {
//				ErrorManager.handleRefreshTokenException(getActivity(), arg0);
//			}
//		}
//	};
//	GoogleCloudMessaging gcm;
//
//	private GCMRegistratrionListener gcmRegistrationListener = new GCMRegistratrionListener() {
//
//		@Override
//		public void onGCMRegistratrionComplete(String msg) {
//			setProgressDialogVisibility(false);
//			if (msg.contains("Error")) {
//				showCommunicationError();
//			} else {
//				regid = msg;
//				sendRegistrationIdToBackend();
//			}
//		}
//	};
//
//	private boolean isAutoSet;
//	private Listener<EditNotificationSetting> listener = new Listener<EditNotificationSetting>() {
//
//		@Override
//		public void onResponse(EditNotificationSetting arg0) {
//			if (getActivity() == null) {
//				return;
//			}
//			setProgressDialogVisibility(false);
//			if (arg0.result.get(0) != null && "OK".equals(arg0.result.get(0).value)) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//				builder.setTitle("");
//				builder.setMessage("é€šçŸ¥è¨­å®šã�Œæ­£å¸¸ã�«å®Œäº†ã�—ã�¾ã�—ã�Ÿã€‚");
//				AlertDialog dialog = builder.create();
//				builder.setCancelable(false);
//				dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						dismiss();
//						SettingDialogFragment newFragment = new SettingDialogFragment();
//						newFragment.setArguments(getArguments());
//						newFragment.show(getActivity().getSupportFragmentManager(), AbstractDialog.DIALOG_TAG);
//					}
//				});
//				dialog.show();
//			}
//
//		}
//	};
//	private Listener<PushNotificationRegistration> listenerSendRegistrationIdToBackend = new Listener<PushNotificationRegistration>() {
//
//		@Override
//		public void onResponse(PushNotificationRegistration arg0) {
//			setProgressDialogVisibility(false);
//		}
//	};
//
//	private RequestQueue mQueue;
//	AtomicInteger msgId = new AtomicInteger();
//	private boolean outbid;
//	String regid;
//	private NotificationSettings resultFromAPI;
//
//	private int time;
//	private boolean watch_list_remind;
//
//	protected boolean checkChanges(NotificationSettings arg0) {
//		resultFromAPI = arg0;
//		for (int i = 0; i < resultFromAPI.result.get(0).getPush().getSetting().size(); i++) {
//			String name = resultFromAPI.result.get(0).getPush().getSetting().get(i).getName();
//			boolean isEnable = resultFromAPI.result.get(0).getPush().getSetting().get(i).getisEnable();
//
//			if ("outbid".equals(name) && isEnable != outbid) {
//				return true;
//			} else if ("watch_list_remind".equals(name) && isEnable != watch_list_remind) {
//				return true;
//			}
//			if ("auction_closed".equals(name) && isEnable != auction_closed) {
//				return true;
//			}
//			if ("contact_form".equals(name) && isEnable != contact_form) {
//				return true;
//			}
//		}
//		if (time != Integer.valueOf(resultFromAPI.result.get(0).getWatchListReminderSetting().time)) {
//			return true;
//		}
//		if (isAutoSet != resultFromAPI.result.get(0).getWatchListReminderSetting().isAutoSet) {
//			return true;
//		}
//
//		return false;
//	}
//
//	private void checkGCMToken() {
//		// Check device for Play Services APK. If check succeeds, proceed with
//		// GCM registration.
//		LogUtil.debug(TAG, "checkGCMToken is called.");
//		if (checkPlayServices()) {
//			gcm = GoogleCloudMessaging.getInstance(YHOApplication.getApplication().getApplicationContext());
//			regid = GCMManager.getRegistrationId(YHOApplication.getApplication().getApplicationContext());
//			LogUtil.debug(TAG, "Regid: " + regid);
//			if (regid.isEmpty()) {
//				setProgressDialogVisibility(true);
//				GCMManager.registerInBackground(YHOApplication.getApplication().getApplicationContext(), gcmRegistrationListener);
//			} else {
//				sendRegistrationIdToBackend();
//			}
//		} else {
//			dismiss();
//		}
//	}
//
//	/**
//	 * Check the device to make sure it has the Google Play Services APK. If it
//	 * doesn't, display a dialog that allows users to download the APK from the
//	 * Google Play Store or enable it in the device's system settings.
//	 */
//	private boolean checkPlayServices() {
//		if (YHOCommonConstant.YHO_TESTMODE == true) {
//			return true;
//		}
//		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(YHOApplication.getApplication().getApplicationContext());
//		if (resultCode != ConnectionResult.SUCCESS) {
//			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//				GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), GCMConstant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
//			} else {
//			}
//			return false;
//		}
//		return true;
//	}
//
//	protected void goBack() {
//		dismiss();
//		SettingDialogFragment newFragment = new SettingDialogFragment();
//		newFragment.setArguments(getArguments());
//		newFragment.show(getActivity().getSupportFragmentManager(), AbstractDialog.DIALOG_TAG);
//	}
//
//	@Override
//	public void onClick(View v) {
//		DialogFragment newFragment;
//		switch (v.getId()) {
//		case R.id.setting_popup_header_back_id:
//			showDialogForConfirm(true);
//			break;
//		case R.id.setting_push_screen_as_bidder_reminder_time_id:
//			dismiss();
//			newFragment = new SettingRemindDialogFragment();
//			newFragment.setArguments(getArguments());
//			newFragment.show(getActivity().getSupportFragmentManager(), AbstractDialog.DIALOG_TAG);
//			break;
//		case R.id.setting_push_screen_as_bidder_reminder_method_id:
//			dismiss();
//			newFragment = new SettingRemindMethodDialogFragment();
//			newFragment.setArguments(getArguments());
//			newFragment.show(getActivity().getSupportFragmentManager(), AbstractDialog.DIALOG_TAG);
//			break;
//		case R.id.search_popup_header_close_id:
//			showDialogForConfirm(false);
//			break;
//		case R.id.setting_notification_submit:
//			requestForSubmit();
//			break;
//		case R.id.push_notification_help:
//			if (ErrorManager.checkNetworkStatusForWebview(getActivity())) {
//				dismiss();
//				newFragment = new SettingPushWebviewDialogFragment();
//				Bundle arguments = getArguments();
//				arguments.putString(WebViewDialogFragment.DIALOG_TITLE, getString(R.string.setting_push_description_title));
//				arguments.putString(WebViewDialogFragment.WEBVIEW_URL, YHOServerConfig.YHO_PUSH_HELP);
//				arguments.putString(WebViewDialogFragment.PARENT_CLASS, this.getClass().getCanonicalName());
//				newFragment.setArguments(arguments);
//				newFragment.show(getActivity().getSupportFragmentManager(), AbstractDialog.DIALOG_TAG);
//			}
//			break;
//		default:
//			break;
//		}
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		/* checkGCMToken(); */
//		mQueue = YHOApplication.getApplication().getRequestQueue();
//		if (getArguments() != null && savedInstanceState != null) {
//			outbid = getArguments().getBoolean(OUTBID);
//			watch_list_remind = getArguments().getBoolean(WATCHLIST_REMIND);
//			auction_closed = getArguments().getBoolean(AUCTION_CLOSE);
//			contact_form = getArguments().getBoolean(CONTACT_FORM);
//			time = getArguments().getInt(TIME);
//			isAutoSet = getArguments().getBoolean(IS_AUTO_SET);
//			bid_reinstated = getArguments().getBoolean(BID_REINSTATED);
//			resultFromAPI = (NotificationSettings) getArguments().getSerializable(NOTIFICATION_SETTING);
//		} else if (getArguments() != null) {
//			resultFromAPI = (NotificationSettings) getArguments().getSerializable(NOTIFICATION_SETTING);
//			if (getArguments().containsKey(OUTBID)) {
//				outbid = getArguments().getBoolean(OUTBID);
//				watch_list_remind = getArguments().getBoolean(WATCHLIST_REMIND);
//				auction_closed = getArguments().getBoolean(AUCTION_CLOSE);
//				contact_form = getArguments().getBoolean(CONTACT_FORM);
//				time = getArguments().getInt(TIME);
//				isAutoSet = getArguments().getBoolean(IS_AUTO_SET);
//				bid_reinstated = getArguments().getBoolean(BID_REINSTATED);
//				resultFromAPI = (NotificationSettings) getArguments().getSerializable(NOTIFICATION_SETTING);
//			} else {
//				parseFromAPI(resultFromAPI);
//				checkGCMToken();
//			}
//		}
//	}
//
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		// YJAdsSDK
//		requestAd("/user/push/setting");
//
//		return createDialog(R.layout.fragment_dialog_setting_push);
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle arg0) {
//		super.onSaveInstanceState(arg0);
//		saveAllObjectIntoArgument(arg0);
//	}
//
//	protected void parseFromAPI(NotificationSettings arg0) {
//		resultFromAPI = arg0;
//		for (int i = 0; i < resultFromAPI.result.get(0).getPush().getSetting().size(); i++) {
//			String name = resultFromAPI.result.get(0).getPush().getSetting().get(i).getName();
//			boolean isEnable = resultFromAPI.result.get(0).getPush().getSetting().get(i).getisEnable();
//
//			if ("outbid".equals(name)) {
//				outbid = isEnable;
//			} else if ("watch_list_remind".equals(name)) {
//				watch_list_remind = isEnable;
//			}
//			if ("auction_closed".equals(name)) {
//				auction_closed = isEnable;
//			}
//			if ("contact_form".equals(name)) {
//				contact_form = isEnable;
//			}
//			if ("bid_reinstated".equals(name)) {
//				bid_reinstated = isEnable;
//			}
//		}
//		time = Integer.valueOf(resultFromAPI.result.get(0).getWatchListReminderSetting().time);
//		isAutoSet = resultFromAPI.result.get(0).getWatchListReminderSetting().isAutoSet;
//		saveAllObjectIntoArgument(getArguments());
//
//	}
//
//	private void requestForSubmit() {
//		SimpleXmlRequest<EditNotificationSetting> request = new SimpleXmlRequest<EditNotificationSetting>(Method.POST, YHOServerConfig.YHO_API_EDIT_NOTIFICATION_SETTING, EditNotificationSetting.class, listener, errorListener);
//
//		HashMap<String, String> parameter = new HashMap<String, String>();
//		parameter.put("push_auction_closed", auction_closed + "");
//		parameter.put("push_contact_form", contact_form + "");
//		parameter.put("push_outbid", outbid + "");
//		parameter.put("push_bid_reinstated", bid_reinstated + "");
//		parameter.put("push_watch_list_remind", watch_list_remind + "");
//		parameter.put("watch_list_remind_time", time + "");
//		parameter.put("watch_list_remind_auto_set", isAutoSet + "");
//
//		request.setParams(parameter);
//		mQueue.add(request);
//		setProgressDialogVisibility(true);
//	}
//
//	@Override
//	protected boolean onBackPressed() {
//		showDialogForConfirm(true);
//		return true;
//	}
//
//	@Override
//	protected void retrieveData() {
//		setTitle(R.string.setting_push_screen_title);
//		((TextView) mDialog.findViewById(R.id.yahoo_id)).setText(YConnectServiceManager.getInstance().getUserId());
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_bidder_high_update_btn_id)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				outbid = isChecked;
//				saveAllObjectIntoArgument(getArguments());
//			}
//		});
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_bidder_watch_list_reminder_id)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				watch_list_remind = isChecked;
//				saveAllObjectIntoArgument(getArguments());
//			}
//		});
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_exhibitorbidder_sucessful_bid_btn_id)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				auction_closed = isChecked;
//				saveAllObjectIntoArgument(getArguments());
//			}
//		});
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_exhibitorbidder_business_communication_btn_id)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				contact_form = isChecked;
//				saveAllObjectIntoArgument(getArguments());
//			}
//		});
//		mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_time_id).setOnClickListener(this);
//		mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_method_id).setOnClickListener(this);
//		setDataForView();
//	}
//
//	private void saveAllObjectIntoArgument(Bundle arg0) {
//		arg0.putBoolean(OUTBID, outbid);
//		arg0.putBoolean(WATCHLIST_REMIND, watch_list_remind);
//		arg0.putBoolean(AUCTION_CLOSE, auction_closed);
//		arg0.putBoolean(CONTACT_FORM, contact_form);
//		arg0.putInt(TIME, time);
//		arg0.putBoolean(IS_AUTO_SET, isAutoSet);
//		arg0.putBoolean(BID_REINSTATED, bid_reinstated);
//		arg0.putSerializable(NOTIFICATION_SETTING, resultFromAPI);
//	}
//
//	/**
//	 * Send regId to YH server
//	 */
//	private void sendRegistrationIdToBackend() {
//		SimpleXmlRequest<PushNotificationRegistration> request = new SimpleXmlRequest<PushNotificationRegistration>(Method.POST, YHOServerConfig.YHO_API_REGISTRATION_PUSHNOTIFICATION, PushNotificationRegistration.class, listenerSendRegistrationIdToBackend, errorListenerSendRegistrationIdToBackend);
//
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("prod_id", "auc");
//		params.put("topic_id", "auc");
//		params.put("consumeruri", regid);
//		params.put("consumeruri_type", "android");
//		params.put("userid", YConnectServiceManager.getInstance().getUserId());
//		params.put("userid_type", "yid");
//		params.put("subflag", "1");
//		params.put("output", "xml");
//		params.put("target", "tablet");
//
//		request.setParams(params);
//		request.setContentType("application/x-www-form-urlencoded");
//
//		mQueue.add(request);
//		setProgressDialogVisibility(true);
//	}
//
//	protected void setDataForView() {
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_bidder_high_update_btn_id)).setChecked(outbid);
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_bidder_watch_list_reminder_id)).setChecked(watch_list_remind);
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_exhibitorbidder_sucessful_bid_btn_id)).setChecked(auction_closed);
//		((ToggleButton) mDialog.findViewById(R.id.setting_push_screen_as_exhibitorbidder_business_communication_btn_id)).setChecked(contact_form);
//		((TextView) mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_time__title_id)).setText("çµ‚äº†ã�®" + time + "åˆ†å‰�");
//		if (isAutoSet) {
//			((TextView) mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_method_title_id)).setText("è‡ªå‹•è¨­å®š");
//		} else {
//			((TextView) mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_method_title_id)).setText("æ‰‹å‹•è¨­å®š");
//		}
//	}
//
//	@Override
//	protected void setListener() {
//		super.setListener();
//		btnBack.setVisibility(View.VISIBLE);
//		btnBack.setOnClickListener(this);
//		mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_time_id).setOnClickListener(this);
//		mDialog.findViewById(R.id.setting_push_screen_as_bidder_reminder_method_id).setOnClickListener(this);
//		mDialog.findViewById(R.id.search_popup_header_close_id).setOnClickListener(this);
//		mDialog.findViewById(R.id.setting_notification_submit).setOnClickListener(this);
//		mDialog.findViewById(R.id.push_notification_help).setOnClickListener(this);
//	}
//
//	protected void showCommunicationError() {
//		if (isShowing()) {
//			AlertDialog dialog = new Builder(getActivity()).create();
//			dialog.setTitle("ã‚¨ãƒ©ãƒ¼");
//			dialog.setMessage("é€šä¿¡ã�«å¤±æ•—ã�—ã�¾ã�—ã�Ÿã€‚");
//			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					goBack();
//				}
//			});
//			dialog.show();
//		}
//	}
//
//	private void showDialogForConfirm(final boolean isBack) {
//		AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
//		dialog.setMessage("è¨­å®šå†…å®¹ã�¯å­˜åœ¨ã�•ã‚Œã�¾ã�›ã‚“");
//		dialog.setTitle("é€šçŸ¥è¨­å®šã�®çµ‚äº†");
//		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dismiss();
//				if (isBack) {
//					goBack();
//				}
//			}
//		});
//		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ã‚­ãƒ£ãƒ³ã‚»ãƒ«", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//
//			}
//		});
//		dialog.show();
//	}
//
//}
