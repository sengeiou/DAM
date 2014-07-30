package whyq.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import whyq.adapter.FriendAdapter;
import whyq.interfaces.FragmentDialogListener;
import whyq.model.FriendFacebook;
import whyq.model.TransferData;
import whyq.utils.Constants;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.BaseRequestListener;
import whyq.utils.facebook.sdk.AsyncFacebookRunner;
import whyq.utils.facebook.sdk.Facebook;
import whyq.utils.facebook.sdk.FacebookError;
import whyq.utils.facebook.sdk.Util;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dealadelivery.whyq.R;

public class WhyqTagFriendsDialog extends FragmentActivity {

	private ListView lvFriends;
	private Context context;
	private List<FriendFacebook> friendList;
	private FragmentDialogListener listener;
	private List<FriendFacebook> friendTagList;
	private TextView tvHeader;
	private ProgressBar prBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tags_friend_screen);
		context = this;
		prBar = (ProgressBar) findViewById(R.id.prgBar);
		lvFriends = (ListView) findViewById(R.id.lvFriends);
		tvHeader = (TextView)findViewById(R.id.tvHeaderTitle);
		tvHeader.setText("Tag Friends");
		friendList = new ArrayList<FriendFacebook>();
		friendTagList = new ArrayList<FriendFacebook>();
		Bundle bundle = getIntent().getExtras();
		
		if (bundle.containsKey("accessToken")) {
			String accessToken = bundle.getString("accessToken");
			exeGetData(accessToken);
		}
	}

//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		View v = inflater
//				.inflate(R.layout.tags_friend_screen, container, false);
//		context = getActivity();
//		lvFriends = (ListView) v.findViewById(R.id.lvFriends);
//		Bundle bundle = getArguments();
//		if (bundle.containsKey("accessToken")) {
//			String accessToken = bundle.getString("accessToken");
//			exeGetData(accessToken);
//		}
//		return v;
//	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(context);
	}

	public void onDoneClicked(View v){

		// TODO Auto-generated method stub
		Intent i = new Intent();
		Bundle b = new Bundle();
		TransferData data = new TransferData();
		data.setData(friendTagList);
		b.putSerializable("data", data);
		i.putExtras(b);
		setResult(RESULT_OK, i);
		finish();
	
	}
	
	public void onBack(View v){
		finish();
	}
	
	
	private void exeGetData(String accessToken) {
		// TODO Auto-generated method stub
		setProgressBarShowing(true);
		accessToken = whyq.utils.Util.createSession().getAccessToken();
		if (accessToken != null) {
			Facebook mfacebook = new Facebook(Constants.FACEBOOK_APP_ID);
			mfacebook.setAccessToken(accessToken);
			final AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mfacebook);
			mAsyncRunner.request("me/friends", new Bundle(), new FriendListRequestListener());
		} else {

		}

	}

	private void setProgressBarShowing(boolean b) {
		// TODO Auto-generated method stub
		prBar.setVisibility(b?View.VISIBLE:View.INVISIBLE);
	}

	public class FriendListRequestListener extends BaseRequestListener {

		private Object _error;

		public void onComplete(final String response) {
			_error = null;
			
			Log.d("friends",""+response.toString());
			try {
				JSONObject json = Util.parseJson(response);
				final JSONArray friends = json.getJSONArray("data");
				
				runOnUiThread(new Runnable() {
					public void run() {
						// Do stuff here with your friends array,
						// which is an array of JSONObjects.
						setProgressBarShowing(false);
						friendList = getFrindsFromJson(friends);
						bindFriends(friendList);
					}
				});

			} catch (JSONException e) {
				_error = "JSON Error in response";
			} catch (FacebookError e) {
				_error = "Facebook Error: " + e.getMessage();
			}

			if (_error != null) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(context, "Error occurred:  " + _error,
								Toast.LENGTH_LONG).show();
					}
				});
			}
		}


		protected List<FriendFacebook> getFrindsFromJson(JSONArray friends) {
			// TODO Auto-generated method stub
			int length = friends.length();
			friendList.clear();
			for(int i=0;i<length;i++){
				FriendFacebook item = new FriendFacebook();
				JSONObject obj = friends.optJSONObject(i);
				if(obj!=null){
					item.setFirstName(obj.optString("name"));
					item.setFacebookId(obj.optString("id"));
					friendList.add(item);
				}
			}
			return friendList;
		}


		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMalformedURLException(MalformedURLException e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}
	}

//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//		listener = (FragmentDialogListener) activity;
//	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

//		listener.onCompleted(friendTagList);
	}

	private List<FriendFacebook> getFriends() {
		// TODO Auto-generated method stub
		List<FriendFacebook> list = new ArrayList<FriendFacebook>();
		for (int i = 0; i < 10; i++) {
			FriendFacebook item = new FriendFacebook();
			list.add(item);
		}
		return list;
	}

	private void bindFriends(List<FriendFacebook> friendList) {
		// TODO Auto-generated method stub
		FriendAdapter adapter = new FriendAdapter(context, friendList);
		lvFriends.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	public void onChecked(View v){
		CheckBox cbx = (CheckBox)v;
		Log.d("onChecked","onChecked"+cbx.isChecked());
		if(cbx.isChecked()){
			friendTagList.add((FriendFacebook)v.getTag());
		}else{
			friendTagList.remove((FriendFacebook)v.getTag());
		}
	}
	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		Intent i = getIntent();
//		Bundle b = new Bundle();
//		TransferData data = new TransferData();
//		data.setData(friendTagList);
//		b.putSerializable("data", data);
//		i.putExtras(b);
//		setResult(RESULT_OK, i);
//	}
}
