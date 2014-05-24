package whyq.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.auth.AccessToken;
import whyq.WhyqApplication;
import whyq.adapter.AmazingAdapter;
import whyq.interfaces.FriendTwitterController;
import whyq.model.FriendTwitter;
import whyq.model.ResponseData;
import whyq.model.SearchFriendCriteria;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.SpannableUtils;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.view.AmazingListView;
import whyq.view.SearchField;

import com.dam.BuildConfig;
import com.dam.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class WhyqFriendsTwitterActivity extends ImageWorkerActivity {
	private FriendsTwitterAdapter mFriendsTwitterAdapter = null;
	private AccessToken mTwitterAccess;
	private AmazingListView mListview;
	private TextView mInviteMessage;
	private Button mInviteButton;
	private View mInviteContainer;
	private boolean isTwitter;
	private static final Map<String, String> INVITED_LIST = new HashMap<String, String>();
	private FriendTwitter twitter;
	private Service service;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_friends);
//		getIntent().getBooleanExtra("is_facebook", false);
		showHeaderSearchField(true);
		SearchField searchField = getSearchField();
		EditText tvSearch = searchField.getEditTextView();

		tvSearch.setHint(R.string.find_a_friend);
		tvSearch.setTextColor(getResources().getColor(R.color.white));
		tvSearch.setBackgroundResource(R.drawable.textfield_search_default_holo_dark);
		
		service = new Service(WhyqFriendsTwitterActivity.this);
		mListview = (AmazingListView) findViewById(R.id.listview);
		mListview.setPinnedHeaderView(findViewById(R.id.header));
		mListview.setOnItemClickListener(new OnItemClickListener() {			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				final Object item = arg0.getItemAtPosition(arg2);
				twitter = (FriendTwitter)item;
				if(twitter.getIs_join()){
					startUserProfileActivity(twitter.getId(),twitter.getScreenName(),twitter.getAvatar(), twitter.getIsFriend());
				}
				
			}

			
		});
		mFriendsTwitterAdapter = new FriendsTwitterAdapter(this,mImageWorker);
		mTwitterAccess = getTwitterAccess();
//		SharedPreferencesManager shareManager = new SharedPreferencesManager(
//				WhyqApplication.Instance().getApplicationContext());
//		AccessToken twitterAccess = shareManager.loadTwitterToken();
		if(BuildConfig.DEBUG){
			Log.d("WhyqFriendsTwitterActivity", "TwitterAccess ===>"+mTwitterAccess.getToken()+mTwitterAccess.getUserId()+mTwitterAccess.getScreenName());
		}
		setTitle("Friends from Twitter");
		getFriends();
		mInviteContainer = findViewById(R.id.inviteContainer);
		mInviteMessage = (TextView) findViewById(R.id.tvMessage);
		mInviteButton = (Button) findViewById(R.id.invite);
		mInviteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (INVITED_LIST.size() == 0) {
					return;
				}
				inviteFriends();
			}
		});
		
	}
	
	private void startUserProfileActivity(String userId, String userName,
			String avatar, int isFriend) {
		// TODO Auto-generated method stub
		Intent i = new Intent(WhyqFriendsTwitterActivity.this,WhyqUserProfileActivity.class);
		i.putExtra(WhyqUserProfileActivity.ARG_USER_ID, userId);
		i.putExtra("is_friend", true);
		startActivity(i);
		
	}
	private AccessToken getTwitterAccess(){
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getTwitterAccess(this);
	}
	@Override
	public void onQuery(String queryString) {
		// TODO Auto-generated method stub
		if (queryString != null && queryString.length() > 0) {
			searchFriends(queryString);
		} else {
			getFriends();
		}
	}
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		if (result != null) {
			if(result.isSuccess()&& result.getAction() == ServiceAction.ActionInviteFriendsTwitter) {
//				DataParser parserInvite = new DataParser();
//				ResponseData dataa = (ResponseData)parserInvite.parseInviteTwitterResult(result.getData());
				Toast.makeText(WhyqFriendsTwitterActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
				mInviteContainer.setVisibility(View.GONE);
				INVITED_LIST.clear();
				getFriends();
			} else {
				if (result.getAction() == ServiceAction.ActionSearchFriendsFacebook) {
					DataParser parser = new DataParser();
					ResponseData data = (ResponseData) parser
							.parseFriendTwitter(String.valueOf(result
									.getData()));
					if (data.getStatus().equals("401")) {
						Util.loginAgain(this, data.getMessage());
					} else {
						mFriendsTwitterAdapter.setController((FriendTwitterController) data.getData());
					}
				}
			}
		}
	}
	private void searchFriends(String queryString) {
		if(mFriendsTwitterAdapter==null){
			mFriendsTwitterAdapter = new FriendsTwitterAdapter(this,mImageWorker);
		}
		mListview.setAdapter(mFriendsTwitterAdapter);
//		Service service = getService();
		setLoading(true);
		String oauth_token = mTwitterAccess.getToken();
		String oauth_token_secret = mTwitterAccess.getTokenSecret();
		if(oauth_token!=null && oauth_token_secret!=null){
			service.searchFriends(SearchFriendCriteria.twitter, WhyqApplication.getRSAToken(), queryString, null, oauth_token, oauth_token_secret);
		}
	}
	private void getFriends() {
//		Service service = getService();
		setLoading(true);
//		if(isTwitter){
			String oauth_token = mTwitterAccess.getToken();
			String oauth_token_secret = mTwitterAccess.getTokenSecret();
			if(oauth_token!=null && oauth_token_secret!=null){
				mListview.setAdapter(mFriendsTwitterAdapter);
				service.searchFriends(SearchFriendCriteria.twitter, WhyqApplication.getRSAToken(), "", null, oauth_token, oauth_token_secret);
			}
//		}
	}
	void inviteFriends() {
//		Service service = getService();
		setLoading(true);
		StringBuilder userIds = new StringBuilder();
		for (String id : INVITED_LIST.keySet()) {
			userIds.append(id + ",");
		}
		String oauth_token = mTwitterAccess.getToken();
		String oauth_token_secret = mTwitterAccess.getTokenSecret();
		service.inviteFriendsTwitter(WhyqApplication.getRSAToken(), oauth_token, oauth_token_secret, userIds.toString());
	}
	void removeInviteFriend(FriendTwitter friend) {
		INVITED_LIST.remove(friend.getId());
		if (INVITED_LIST.size() == 0) {
			mInviteContainer.setVisibility(View.GONE);
		} else {
			displayInviteMessage(INVITED_LIST.keySet().toArray(new String[] {})[INVITED_LIST
					.size() - 1]);
		}
	}
	void removeInviteFriendNotJoin(FriendTwitter friend){
		INVITED_LIST.remove(friend.getTwitterId());
		if (INVITED_LIST.size() == 0) {
			mInviteContainer.setVisibility(View.GONE);
		} else {
			displayInviteMessage(INVITED_LIST.keySet().toArray(new String[] {})[INVITED_LIST
					.size() - 1]);
		}
	}
	void addInviteFriend(FriendTwitter friend) {
		INVITED_LIST.put(friend.getId(),
				friend.getFirstName());
		mInviteContainer.setVisibility(View.VISIBLE);
		displayInviteMessage(friend.getId());
	}
	void addInviteFriendNotJoin(FriendTwitter friend){
		INVITED_LIST.put(friend.getTwitterId(),
				friend.getFirstName());
		mInviteContainer.setVisibility(View.VISIBLE);
		displayInviteMessage(friend.getTwitterId());
	}
	void displayInviteMessage(String userId) {
		String key = INVITED_LIST.get(userId);
//		Log.d("Invite list size", "INVITE_LIST size====>>>>>>"+ INVITED_LIST.size());
//		Log.d("Invite list value", "INVITE_LIST value====>>>>>>"+ INVITED_LIST.get(userId));

		if (INVITED_LIST.size() > 1) {
			String message = "Invite " + key + " and "
					+ (INVITED_LIST.size() - 1) + " other to join WHYQ?";
			Spannable messageSpannable = SpannableUtils.stylistTextBold(
					message, key, R.color.orange);
			mInviteMessage.setText(messageSpannable);
//			mInviteContainer.requestLayout();
			Log.d("Invite message", "mInvite Message" + mInviteMessage.getText().toString());
		} else {
			String message = "Invite " + key + " to join WHYQ?";
			mInviteMessage.setText(SpannableUtils.stylistTextBold(message, key,
					R.color.orange));
			Log.d("Invite message", "mInvite Message" + mInviteMessage.getText().toString());
//			mInviteMessage.setText(message);
//			mInviteContainer.requestLayout();

		}
	}
	

	static class FriendsTwitterAdapter extends AmazingAdapter {
		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private static final String SECTION_WHYQ = "joined whyq";
		private static final String SECTION_NOT_JOIND_WHYQ = "not joined whyq";
		private WhyqFriendsTwitterActivity mActivity;
		private List<FriendTwitter> listWhyq;
		private List<FriendTwitter> listNotJoinWhyq;
		private ImageViewHelper mImageWorker;
		private int countListNotJoinWhyq = 0;
		private int countListWhyq = 0;
		public FriendsTwitterAdapter(WhyqFriendsTwitterActivity context, ImageViewHelper imageWorker) {
			this.mActivity = context;
			this.mImageWorker = imageWorker;
		}
		public void setController(FriendTwitterController controller){
			if (controller != null) {
				listWhyq = controller.getListWhyq();
				listNotJoinWhyq = controller.getListNotJoinWhyq();
			} else {
				listWhyq = null;
				listNotJoinWhyq = null;
			}
			if (listWhyq != null) {
				countListWhyq = listWhyq.size();
			} else {
				countListWhyq = 0;
			}
			if (listNotJoinWhyq != null) {
				countListNotJoinWhyq = listNotJoinWhyq.size();
			} else {
				countListNotJoinWhyq = 0;
			}
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return countListWhyq + countListNotJoinWhyq;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (position < 0 || position >= getCount()) {
				return null;
			}

			if (position < countListWhyq) {
				return listWhyq.get(position);
			} else {
				return listNotJoinWhyq.get(position - countListWhyq);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		protected void onNextPageRequested(int page) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void bindSectionHeader(View view, int position,
				boolean displaySectionHeader) {
			// TODO Auto-generated method stub
			ViewHolder holder = getViewHolder(view);
			if (displaySectionHeader) {
				holder.header.setVisibility(View.VISIBLE);
				bindHeader(holder,
						getSections()[getSectionForPosition(position)]);
			} else {
				holder.header.setVisibility(View.GONE);
			}
		}
		private class ViewHolder {
			ImageView avatar;
			TextView name;
			Button invite;
			View header;
			TextView headerMessage;
			Button headerFriendAll;

			public ViewHolder(View view) {
				avatar = (ImageView) view.findViewById(R.id.avatar);
				if (avatar != null) {
					avatar.getLayoutParams().width = AVATAR_SIZE;
					avatar.getLayoutParams().height = AVATAR_SIZE;
				}
				name = (TextView) view.findViewById(R.id.name);
				invite = (Button) view.findViewById(R.id.invite);
				header = view.findViewById(R.id.header);
				headerMessage = (TextView) header.findViewById(R.id.message);
				headerFriendAll = (Button) header.findViewById(R.id.friendAll);
			}
		}
		private ViewHolder getViewHolder(View view) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder(view);
				view.setTag(holder);
			}
			return holder;
		}
		HashMap<String, View> viewList = new HashMap<String, View>();
		
		@Override
		public View getAmazingView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			final FriendTwitter item = (FriendTwitter)getItem(position);
			convertView = viewList.get(item.getTwitterId());
			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.friend_list_item, parent, false);
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
				final ViewHolder holder = getViewHolder(convertView);
				
				holder.name.setText(item.getFirstName());
				mImageWorker.downloadImage(item.getAvatar(), holder.avatar);
				if(item.getIsFriend()==0){
					holder.invite.setBackgroundResource(R.drawable.btn_accept);
					holder.invite.setText("");
				}else{
					
					displayInviteButton(holder, item);
					holder.invite.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (item.getIs_join()) {
								if (INVITED_LIST.containsKey(item.getId())) {
									mActivity.removeInviteFriend(item);
								} else {
									mActivity.addInviteFriend(item);
								}
								displayInviteButton(holder, item);
							} else {
//								Bundle params = new Bundle();
//								params.putString("to", item.getTwitterId());
								if(INVITED_LIST.containsKey(item.getTwitterId())){
									mActivity.removeInviteFriendNotJoin(item);
									Log.d("Invite Friend", "INVITED_LIST =>>>>>>>" +INVITED_LIST);
								}else{
									mActivity.addInviteFriendNotJoin(item);
									Log.d("Invite Friend", "INVITED_LIST =>>>>>>>" +INVITED_LIST);
									
								}
								displayInviteButtonNotJoin(holder, item);
								//send request invite twitter 
								//
								
								
							}
						}
					});
				}
				viewList.put(item.getTwitterId(), convertView);
			}else{
				
			}


			return convertView;
		}
		private void displayInviteButton(ViewHolder holder, FriendTwitter item) {
			if (INVITED_LIST.containsKey(item.getId())) {
				holder.invite.setBackgroundResource(R.drawable.btn_accept);
				holder.invite.setText("");
			} else {
				holder.invite.setBackgroundResource(R.drawable.btn_base);
				holder.invite.setText(R.string.invite);
			}
		}
		private void displayInviteButtonNotJoin(ViewHolder holder, FriendTwitter item) {
			if (INVITED_LIST.containsKey(item.getTwitterId())) {
				holder.invite.setBackgroundResource(R.drawable.btn_accept);
				holder.invite.setText("");
			} else {
				holder.invite.setBackgroundResource(R.drawable.btn_base);
				holder.invite.setText(R.string.invite);
			}
		}
		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			// TODO Auto-generated method stub
			bindHeader(getViewHolder(header),getSections()[getSectionForPosition(position)]);
		}

		@Override
		public int getPositionForSection(int section) {
			// TODO Auto-generated method stub
			if (section == 0) {
				return 0;
			} else {
				return countListWhyq;
			}
		}

		@Override
		public int getSectionForPosition(int position) {
			// TODO Auto-generated method stub
			if (position >= 0 && position < countListWhyq) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public String[] getSections() {
			// TODO Auto-generated method stub
			return new String[] { SECTION_WHYQ, SECTION_NOT_JOIND_WHYQ };
		}
		private void bindHeader(ViewHolder holder, String section) {
			if (section.equals(SECTION_WHYQ)) {
				final String key = countListWhyq + " twitter friends";
				final String result = "You have " + key + " had joined WHY Q.";
				Spannable message = SpannableUtils.stylistTextBold(result, key,
						mActivity.getResources().getColor(R.color.orange));
				holder.headerMessage.setText(message);
				holder.headerFriendAll.setVisibility(View.VISIBLE);
			} else {
				final String key = countListNotJoinWhyq + " twitter friends";
				final String result = "And "
						+ key
						+ " haven't joined WHY Q. Invite your friend to join this app!";
				final Spannable message = SpannableUtils.stylistTextBold(
						result, key,
						mActivity.getResources().getColor(R.color.orange));
				holder.headerMessage.setText(message);
				holder.headerFriendAll.setVisibility(View.GONE);
			}
		}
		
	}
	public void onBackClicked(View v){
		finish();
	}
	
}