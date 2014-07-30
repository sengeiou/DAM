package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.interfaces.FriendFacebookController;
import whyq.model.FriendFacebook;
import whyq.model.FriendWhyq;
import whyq.model.SearchFriendCriteria;
import whyq.model.StatusWithFriend;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.SpannableUtils;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import whyq.view.SearchField;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dealadelivery.whyq.R;

public class FriendsFacebookActivity extends ImageWorkerActivity {

	private FriendsFacebookAdapter mFriendFacebookAdapter = null;
	private FriendsWhyqAdapter mFriendWhyqAdapter = null;
	private String mAccessToken;
	private ListView mListview;
	private boolean isFacebook = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_facebook_friends);

		getIntent().getBooleanExtra("is_facebook", true);
		
		showHeaderSearchField(true);
		SearchField searchField = getSearchField();
		searchField.getEditTextView().setHint(R.string.find_a_friend);

		mListview = (ListView) findViewById(R.id.listview);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final Object item = arg0.getItemAtPosition(arg2);
				if (item instanceof FriendFacebook) {
					FriendFacebook facebook = (FriendFacebook) item;
					startUserProfileActivity(facebook.getId(),
							facebook.getFirstName(), facebook.getAvatar(), facebook.getIsFriend());
				} else if (item instanceof FriendWhyq) {
					FriendWhyq whyq = (FriendWhyq) item;
					startUserProfileActivity(whyq.getId(),

							whyq.getFirst_name()+" "+whyq.getLast_name(), whyq.getAvatar(), whyq.getIsFriend());
				}
			}
		});

		mFriendWhyqAdapter = new FriendsWhyqAdapter(this, mImageWorker);
		mFriendFacebookAdapter = new FriendsFacebookAdapter(this, mImageWorker);
		mAccessToken = getAccessToken();

		if (mAccessToken == null) {
			setTitle(R.string.friends);
		} else {
			setTitle(R.string.friend_from_facebook);
		}

		// Set extra button.
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.icon_add_friend);
		setExtraView(imageView);

		setBackButtonIcon(R.drawable.icon_friend_invite);

		getFriends();

	}

	private void startUserProfileActivity(String userId, String userName,
			String avatar, int isFriended) {
		Intent i = new Intent(this, WhyqUserProfileActivity.class);
		i.putExtra(WhyqUserProfileActivity.ARG_USER_ID, userId);
		i.putExtra("is_friend", true);
		i.putExtra("is_friended", (isFriended==1?true:false));
		startActivity(i);
	}

	private String getAccessToken() {
		final WhyqUtils mPermutils = new WhyqUtils();
		return mPermutils.getFacebookToken(this);
	}

	@Override
	public void onQuery(String queryString) {
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
			if (result.getAction() == ServiceAction.ActionInviteFriendsFacebook) {

			} else {
				if (result.getAction() == ServiceAction.ActionGetFriendsFacebook
						|| result.getAction() == ServiceAction.ActionSearchFriendsFacebook) {
					DataParser paser = new DataParser();
					FriendFacebookController handler = (FriendFacebookController)paser
							.parseFriendFacebook(String.valueOf(result
									.getData()));
					mFriendFacebookAdapter.setController(handler);
				} else {
					DataParser paser = new DataParser();
					mFriendWhyqAdapter.setItems((List<FriendWhyq>)paser
							.parseFriendWhyq(String.valueOf(result.getData())));
				}
			}
		}
	}

	private void searchFriends(String queryString) {
		if (mFriendFacebookAdapter == null) {
			mFriendFacebookAdapter = new FriendsFacebookAdapter(this,
					mImageWorker);
		}
		mListview.setAdapter(mFriendFacebookAdapter);

		Service service = getService();
		setLoading(true);
		if (mAccessToken == null || mAccessToken.length() == 0) {
			service.searchFriends(SearchFriendCriteria.whyq,
					WhyqApplication.getRSAToken(), queryString, null, null, null);
		} else {
			service.searchFriends(SearchFriendCriteria.facebook,
					WhyqApplication.getRSAToken(), queryString, mAccessToken, null, null);
		}

	}

	private void getFriends() {
		Service service = getService();
		setLoading(true);
		if(isFacebook){
			if (mAccessToken == null || mAccessToken.length() == 0) {
				mListview.setAdapter(mFriendWhyqAdapter);
				service.getFriends(WhyqApplication.getRSAToken(),
						XMLParser.getValue(this, XMLParser.STORE_USER_ID));
			} else {
				mListview.setAdapter(mFriendFacebookAdapter);
				service.getFriendsFacebook(WhyqApplication.getRSAToken(), mAccessToken);
			}
		}else{
			//Code for getting twitter friends.
		}
	}

	void inviteFriend(String userId) {
		final String accessToken = getAccessToken();
		Service service = getService();
		setLoading(true);
		service.inviteFriendsFacebook(WhyqApplication.getRSAToken(), userId, accessToken);
	}

	static class FriendsFacebookAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private FriendsFacebookActivity mActivity;
		private List<FriendFacebook> listWhyq;
		private List<FriendFacebook> listNotJoinWhyq;
		private ImageViewHelper mImageWorker;
		private static int countListNotJoinWhyq = 0;
		private static int countListWhyq = 0;

		public FriendsFacebookAdapter(FriendsFacebookActivity context,
				ImageViewHelper imageWorker) {
			this.mActivity = context;
			this.mImageWorker = imageWorker;
		}

		public void setController(FriendFacebookController controller) {
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
			return countListWhyq + countListNotJoinWhyq + 2;
		}

		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= getCount()) {
				return null;
			}

			if (position == 0) {
				final String key = countListWhyq + " facebook friends";
				final String result = "You have " + key + " had joined WHY Q.";
				return SpannableUtils.stylistTextBold(result, key, mActivity
						.getResources().getColor(R.color.orange));
			} else if (position == countListWhyq + 1) {
				final String key = countListNotJoinWhyq + " facebook friends";
				final String result = "And "
						+ key
						+ " haven't joined WHY Q. Invite your friend to join this app!";
				return SpannableUtils.stylistTextBold(result, key, mActivity
						.getResources().getColor(R.color.orange));
			} else if (position <= countListWhyq) {
				return listWhyq.get(position - 1);
			} else {
				return listNotJoinWhyq.get(position - countListWhyq - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0 || position == countListWhyq + 1) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Object item = getItem(position);
			if (convertView == null) {
				if (item instanceof FriendFacebook) {
					convertView = mActivity.getLayoutInflater().inflate(
							R.layout.friend_list_item, parent, false);
				} else {
					convertView = mActivity.getLayoutInflater().inflate(
							R.layout.friend_list_item_secondary, parent, false);
				}

				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			final ViewHolder holder = getViewHolder(convertView);

			if (item instanceof FriendFacebook) {
				final FriendFacebook friendfacebook = (FriendFacebook) item;
				holder.name.setText(friendfacebook.getFirstName());
				mImageWorker.downloadImage(friendfacebook.getAvatar(),
						holder.avatar);
				if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_NOT_CONNECT) {
					if (position <= countListWhyq) {
						holder.action.setText(R.string.add);
					} else {
						holder.action.setText(R.string.invite);
						holder.actionContainer
								.setOnClickListener(new View.OnClickListener() {

									@Override
									public void onClick(View v) {
										mActivity.inviteFriend(friendfacebook
												.getId());
									}
								});
					}
				} else if (friendfacebook.getIsFriend() == StatusWithFriend.STATUS_WAITING_FOR_ACCEPT) {

				}

			} else {
				holder.name.setText((Spannable) item);
				if (position == 0 && countListWhyq > 0) {
					holder.action.setText(R.string.friend_all);
					holder.actionContainer.setVisibility(View.VISIBLE);
				} else {
					holder.actionContainer.setVisibility(View.GONE);
				}
			}

			return convertView;
		}

		private ViewHolder getViewHolder(View view) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder(view);
				view.setTag(holder);
			}
			return holder;
		}

		class ViewHolder {
			ImageView avatar;
			TextView name;
			FrameLayout actionContainer;
			TextView action;

			public ViewHolder(View view) {
				avatar = (ImageView) view.findViewById(R.id.avatar);
				if (avatar != null) {
					avatar.getLayoutParams().width = AVATAR_SIZE;
					avatar.getLayoutParams().height = AVATAR_SIZE;
				}
				name = (TextView) view.findViewById(R.id.name);
				action = (TextView) view.findViewById(R.id.action);
				actionContainer = (FrameLayout) view
						.findViewById(R.id.actionContainer);
			}
		}

	}

	static class FriendsWhyqAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private FriendsFacebookActivity mActivity;
		private List<FriendWhyq> listWhyq;
		private ImageViewHelper mImageWorker;

		public FriendsWhyqAdapter(FriendsFacebookActivity context,
				ImageViewHelper imageWorker) {
			this.mActivity = context;
			this.mImageWorker = imageWorker;
			this.listWhyq = new ArrayList<FriendWhyq>();
		}

		public void setItems(List<FriendWhyq> items) {
			if (items == null || items.size() == 0) {
				listWhyq.clear();
			} else {
				listWhyq = items;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return listWhyq.size();
		}

		@Override
		public Object getItem(int position) {
			return listWhyq.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mActivity.getLayoutInflater().inflate(
						R.layout.friend_list_item, parent, false);
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			final ViewHolder holder = getViewHolder(convertView);

			final FriendWhyq item = listWhyq.get(position);
			holder.name.setText(item.getFirst_name()+" "+item.getLast_name());
			mImageWorker.downloadImage(item.getAvatar(), holder.avatar);
			return convertView;
		}

		private ViewHolder getViewHolder(View view) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder(view);
				view.setTag(holder);
			}
			return holder;
		}

		class ViewHolder {
			ImageView avatar;
			TextView name;
			View action;

			public ViewHolder(View view) {
				view.getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				avatar = (ImageView) view.findViewById(R.id.avatar);
				if (avatar != null) {
					avatar.getLayoutParams().width = AVATAR_SIZE;
					avatar.getLayoutParams().height = AVATAR_SIZE;
				}
				name = (TextView) view.findViewById(R.id.name);
				action = view.findViewById(R.id.action);
				action.setBackgroundResource(R.drawable.icon_friend);
			}
		}

	}

}
