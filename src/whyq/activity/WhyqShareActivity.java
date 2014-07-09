package whyq.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import whyq.WhyqApplication;
import whyq.interfaces.FragmentDialogListener;
import whyq.interfaces.IFacebookLister;
import whyq.interfaces.IServiceListener;
import whyq.model.BillPushNotification;
import whyq.model.FriendFacebook;
import whyq.model.OrderCheckData;
import whyq.model.ResponseData;
import whyq.model.ShareData;
import whyq.model.Store;
import whyq.model.TransferData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.service.img.UrlImageViewHelper;
import whyq.utils.Constants;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.SessionLoginFragment;
import whyq.utils.facebook.sdk.DialogError;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.Facebook.DialogListener;
import whyq.utils.facebook.sdk.FacebookError;
import whyq.utils.share.ShareHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.Session.StatusCallback;
import com.dam.R;

public class WhyqShareActivity extends FragmentActivity implements
		IServiceListener, FragmentDialogListener {

	private static final int GET_IMAGE = 0;
	private static final int FACEBOOK = 1;
	private static final int TAG_FRIENDS = 4;
	private static final String PENDING_REQUEST_BUNDLE_KEY = "com.whyq:PendingRequest";
	private TextView tvTitle;
	private EditText etMessage;
	private String avatarPath;
	private ImageButton btnCaptureImage;
	private SharedPreferencesManager sharePreferences;
	private String billId;
//	private String accessToken;
	private ToggleButton tglShareWhyq;
	private ToggleButton tgleShareFb;
	private ProgressBar prgBar;
	private String facebookIdTag="", facebookNameTag="";
	private Context context;
	private boolean isComment;
	private RelativeLayout rlTags;
	private RelativeLayout rlShareWhyq;
	private TextView tvIntro;
	private String storeId;
	private Store store;
	private ImageView imgTitle;
	private OrderCheckData orderCheck;
	private Session session;
	private boolean pendingRequest;
	private boolean isSend;
	private ArrayList<String> tagsList;
	private boolean isTag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_share);
		context = this;
		tagsList = new ArrayList<String>();
		isComment = getIntent().getBooleanExtra("is_comment", false);
		storeId = getIntent().getStringExtra("store_id");
		BillPushNotification pushNotificationData = null;
		if (getIntent().getExtras() != null)
			pushNotificationData = (BillPushNotification) getIntent()
					.getExtras().getSerializable("push_data");
		tvTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		imgTitle = (ImageView) findViewById(R.id.imgHeader);
		etMessage = (EditText) findViewById(R.id.etMessage);
		btnCaptureImage = (ImageButton) findViewById(R.id.btnCaptureImage);
		tglShareWhyq = (ToggleButton) findViewById(R.id.tglShareWhyq);
		tgleShareFb = (ToggleButton) findViewById(R.id.tglShareFB);
		rlShareWhyq = (RelativeLayout) findViewById(R.id.rlShareWhyq);
		rlTags = (RelativeLayout) findViewById(R.id.rlTags);
		tvIntro = (TextView) findViewById(R.id.tvShareIntro);

		if (isComment) {
			rlShareWhyq.setVisibility(View.GONE);
			rlTags.setVisibility(View.GONE);
			tvIntro.setVisibility(View.GONE);
			tvIntro.setVisibility(View.VISIBLE);
		} else {

		}
		findViewById(R.id.btnDone).setVisibility(View.INVISIBLE);
		tvTitle.setText("Share");
		prgBar = (ProgressBar) findViewById(R.id.prgBar);
		sharePreferences = new SharedPreferencesManager(WhyqApplication
				.Instance().getApplicationContext());
		if (pushNotificationData != null) {
			billId = pushNotificationData.getBillId();
			findViewById(R.id.btnDone).setVisibility(View.INVISIBLE);

		} else {

		}
		Log.d("WhyqShareActivity","storeId "+storeId);
		if (storeId != null)
			getStoreInfo();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("onResume","onResume");
		isTag = false;
		super.onResume();
	}
	private void getStoreInfo() {
		// TODO Auto-generated method stub
		setProgressBar(true);
		Service service = new Service(this);
		service.getBusinessDetail(storeId);
	}

	public void setProgressBar(boolean isShow) {
		prgBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
	}

	public void onBack(View v) {
		finish();
	}

	public void onTag(View v) {
		checkLoginFacebook(false, true);
	}

	public void showTagDialog(String fbId) {
		WhyqTagFriendsDialog fragment = new WhyqTagFriendsDialog();
		Bundle bundle = new Bundle();
		bundle.putString("accessToken", fbId);
		// fragment.setArguments(bundle);
		// getFragmentManager().beginTransaction().add(fragment,
		// "add_tag").commit();
		tagsList.clear();
		facebookIdTag = "";
		facebookNameTag = "";
		isTag = true;
		Intent i = new Intent(context, WhyqTagFriendsDialog.class);
		i.putExtras(bundle);
		startActivityForResult(i, TAG_FRIENDS);
	}

	public void onInviteClicked(View v) {

	}

	public void onSend(View v) {

		if (etMessage.getText().toString().equals("")) {
			Toast.makeText(WhyqShareActivity.this, "Please input your comment",
					Toast.LENGTH_LONG).show();
		} else {
			if (isComment) {
				shareWhyq(null);
			} else {
				shareWhyq(null);
//				checkLoginFacebook(true, false);
			}

		}
//		tagsList.clear();
//		facebookIdTag= "";
//		facebookNameTag = "";
	}

	private void checkLoginFacebook(final boolean isSend, boolean isTag) {
		try {

			this.isSend = isSend;
			session = Util.createSession();

			if (session.isOpened()) {
				
				List<String> permissions = Arrays.asList("publish_actions", "publish_stream","user_checkins","publish_actions","offline_access");
				NewPermissionsRequest newPermission = new Session.NewPermissionsRequest(this, permissions);
				session.requestNewPublishPermissions(newPermission);
				if (isSend) {
					shareWhyq(session.getAccessToken());
				} else {
					if (isComment) {
						exePostFacebook(session.getAccessToken());
					} else {
						if(isTag){
							showTagDialog(session.getAccessToken());
						}else{
							exePostFacebook(session.getAccessToken());							
						}
						

					}
				}
				
			} else {
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
							List<String> permissions = Arrays.asList("publish_actions", "publish_stream","user_checkins","publish_actions","offline_access");
							NewPermissionsRequest newPermission = new Session.NewPermissionsRequest(WhyqShareActivity.this, permissions);
							session.requestNewPublishPermissions(newPermission);
							exePostFacebook(session.getAccessToken());	
						}
					}
				};
				pendingRequest = true;
				session.openForRead(new Session.OpenRequest(WhyqShareActivity.this)
						.setCallback(callback));

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void shareWhyq(String facebookId) {
		if (!tgleShareFb.isChecked())
			facebookId = null;
		Service service = new Service(WhyqShareActivity.this);
		if (isComment) {
			service.postComment(WhyqApplication.Instance().getRSAToken(),
					storeId, etMessage.getText().toString(), avatarPath);
		} else {
			service.pushOrderCheck(WhyqApplication.Instance().getRSAToken(),
					billId, facebookIdTag, etMessage.getText().toString(),
					avatarPath);
		}
		setProgressBar(true);
	}

	protected void exePostFacebook(String accessToken) {
		// TODO Auto-generated method stub
		OrderCheckData data = orderCheck;
		ShareHandler shareHandler = new ShareHandler(WhyqShareActivity.this);
		ShareData shareData = new ShareData();
		shareData.setCaption("");
		shareData.setDescription("");
		shareData.setLink(data.getLink());
		shareData.setMessage(etMessage.getText().toString().replace(facebookNameTag, ""));
		shareData.setName(getResources().getString(R.string.app_name));
		shareData.setPicture(data.getImage());
		shareData.setThumb("");
		shareData.setTags(tagsList);
		setProgressBar(true);
//		shareHandler.postFacebook(accessToken, shareData);
		Service service = new Service(WhyqShareActivity.this);
		if(isComment){
			service.postFBComments(accessToken, shareData);	
		}else{
			service.postFBCheckBill(accessToken, shareData);
		}
		
		
		
		pendingRequest = false;
	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}

	public void onCaptureImage(View v) {
		Intent intent = new Intent(WhyqShareActivity.this, ImageActivity.class);
		startActivityForResult(intent, GET_IMAGE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			Log.d("onActivityResult Join", "onActivityResult " + requestCode);
			 if (session != null &&session
						.onActivityResult(this, requestCode, resultCode, data)
						&& pendingRequest && this.session.getState().isOpened()) {

				Log.d("onActivityResult","isSend "+isSend);
				if (isSend) {
					shareWhyq(session.getAccessToken());
				} else {
					if (isComment) {
						exePostFacebook(session.getAccessToken());
					} else {
						if(isTag ){
							showTagDialog(session.getAccessToken());
						}else{
							exePostFacebook(session.getAccessToken());
						}
					}
				}
			
			}else if (resultCode == RESULT_OK) {
				if (requestCode == GET_IMAGE) {
					avatarPath = data.getStringExtra("path");
					Log.d("onActivityResult Join", "" + avatarPath);
					if (avatarPath != null) {
						File imgFile = new File(avatarPath);
						if (imgFile.exists()) {
							// btnCaptureImage.setImageURI(Uri.fromFile(imgFile));

							UrlImageViewHelper.setUrlDrawable(btnCaptureImage,
									avatarPath);
						}
					}
				} else if (requestCode == TAG_FRIENDS) {
					TransferData tfdata = (TransferData) data
							.getSerializableExtra("data");
					if (tfdata != null){
						facebookIdTag = convertData(tfdata.getData());
						facebookNameTag = convertFBNameData(tfdata.getData());
						tagsList = convertDataToFBIDArray(tfdata.getData());
					}
//					shareWhyq(session.getAccessToken());
					etMessage.setText("With "+facebookNameTag);
				}
				ImageActivity.imagePath = "";
			}

			

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        pendingRequest = savedInstanceState.getBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(PENDING_REQUEST_BUNDLE_KEY, pendingRequest);
    }
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		setProgressBar(false);
		if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionOrderCheck) {
			if (session.getAccessToken() != null) {
				ResponseData data = (ResponseData) result.getData();
				if (data.getStatus().equals("200")) {
					orderCheck = (OrderCheckData) data.getData();
					if (tgleShareFb.isChecked())
						checkLoginFacebook(false,false);
				} else if (data.getStatus().equals("401")) {
					Util.loginAgain(getParent(), data.getMessage());
				} else if (data.getStatus().equals("204")) {
				} else {

				}

			}
		} else if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetBusinessDetail) {

			ResponseData data = (ResponseData) result.getData();
			if (data != null) {
				if (data.getStatus().equals("200")) {
					store = (Store) data.getData();
					if (store != null) {
						bindHeaderData();
					}
				} else if (data.getStatus().equals("401")) {
					Util.loginAgain(context, data.getMessage());
				} else {
					// Util.showDialog(context, data.getMessage());
				}
			}

		} else if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionPostComment) {
			ResponseData data = (ResponseData) result.getData();
			if (data != null) {
				if (data.getStatus().equals("200")) {
					Toast.makeText(context, "Message sent.", Toast.LENGTH_LONG)
							.show();
					orderCheck = (OrderCheckData) data.getData();
					if (tgleShareFb.isChecked())
						checkLoginFacebook(false, false);
					// exePostFacebook(accessToken, responseData);
				} else if (data.getStatus().equals("401")) {
					Util.loginAgain(context, data.getMessage());
				} else {
					// Util.showDialog(context, data.getMessage());
				}
			}
		} else if ((result.isSuccess()
				&& result.getAction() == ServiceAction.ActionPostFBComment)||((result.isSuccess()
						&& result.getAction() == ServiceAction.ActionPostFBCheckBill))) {
			setProgressBar(false);
			boolean status = (Boolean) result.getData();
			if(status){
				Toast.makeText(context, "Message sent.", Toast.LENGTH_LONG).show();
			}
		} else if ((!result.isSuccess()
				&& result.getAction() == ServiceAction.ActionPostFBComment)||((result.isSuccess()
						&& result.getAction() == ServiceAction.ActionPostFBCheckBill))) {
			setProgressBar(false);
			Log.d("Fail post to Facebook",""+result.getData());
			Toast.makeText(context, "Fail post to Facebook!", Toast.LENGTH_LONG).show();
		}else if (!result.isSuccess()
				&& result.getAction() == ServiceAction.ActionPostComment) {
			Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show();
		}
	}

	private void bindHeaderData() {
		// TODO Auto-generated method stub
		try {
			tvTitle.setText("" + store.getNameStore());
//			String cateId = store.getCateid();
//			if (cateId.equals("1")) {
//				imgTitle.setImageResource(R.drawable.icon_cat_cutlery);
//			} else if (cateId.equals("1")) {
//				imgTitle.setImageResource(R.drawable.icon_cat_wine);
//			} else if (cateId.equals("1")) {
//				imgTitle.setImageResource(R.drawable.icon_cat_coffee);
//			} else {
//				imgTitle.setImageResource(R.drawable.icon_cat_hotel);
//			}
			int storeType = Integer.parseInt(store.getCateid());
			if (storeType == 1) {
				imgTitle.setImageResource(R.drawable.icon_tab_cutlery_active);
			} else if (storeType == 2) {
				imgTitle.setImageResource(R.drawable.icon_tab_wine_active);
			} else if (storeType == 3) {
				imgTitle.setImageResource(R.drawable.icon_tab_coffee_active);
			} else if (storeType == 4) {
				imgTitle.setImageResource(R.drawable.icon_tab_hotel_active);
			}
//			Log.d("bindHeaderData", "cate id " + cateId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void onCompleted(Object data) {
		// TODO Auto-generated method stub
		Log.d("onCompleted", "onCompleted(Object data) {" + data);
		facebookIdTag = convertData(data);
		facebookNameTag = convertFBNameData(data);
		tagsList = convertDataToFBIDArray(data);
		Toast.makeText(context, "Completed" + data, Toast.LENGTH_LONG).show();
	}

	private String convertData(Object data) {
		// TODO Auto-generated method stub
		facebookIdTag = "";
		if (data != null) {
			List<FriendFacebook> list = (List<FriendFacebook>) data;
			String result = "";
			for (int i = 0; i < list.size(); i++) {
				FriendFacebook item = list.get(i);
				if (i == 0)
					result += "" + item.getFacebookId();
				else
					result += "," + item.getFacebookId();
			}
			Log.d("convertData", "convertData" + result);
			return result;
		} else {
			return "";
		}
	}
	
	private ArrayList<String> convertDataToFBIDArray(Object data) {
		// TODO Auto-generated method stub
		tagsList.clear();
		if (data != null) {
			List<FriendFacebook> list = (List<FriendFacebook>) data;
			for (int i = 0; i < list.size(); i++) {
				FriendFacebook item = list.get(i);
				tagsList.add(item.getFacebookId());
			}
			Log.d("convertData", "convertData" + tagsList);
			return tagsList;
		} else {
			return tagsList;
		}
	}
	private String convertFBNameData(Object data) {
		// TODO Auto-generated method stub
		facebookNameTag = "";
		if (data != null) {
			List<FriendFacebook> list = (List<FriendFacebook>) data;
			String result = "";
			for (int i = 0; i < list.size(); i++) {
				FriendFacebook item = list.get(i);
				if (i == 0)
					result += "" + item.getFirstName();
				else
					result += "," + item.getFirstName();
			}
			Log.d("convertData", "convertData" + result);
			return result;
		} else {
			return "";
		}
	}
//	// Oncompleted will be call when login facebook successful
//	@Override
//	public void onCompled(boolean b) {
//		// TODO Auto-generated method stub
//		
//		Log.d("onCompleted", "onCompled(boolean b) {" + b);
//		if (b) {
//			accessToken = getAccessToken();
//			if(isComment){
//				if(accessToken!=null)
//					exePostFacebook(accessToken);
//			}else{
//				if(accessToken!=null)
//					showTagDialog(accessToken);	
//			}
//			
//		}
//	}
}
