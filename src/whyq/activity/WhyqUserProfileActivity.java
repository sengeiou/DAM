package whyq.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whyq.WhyqApplication;
import whyq.controller.WhyqListController;
import whyq.model.ActivityItem;
import whyq.model.Photo;
import whyq.model.ResponseData;
import whyq.model.Store;
import whyq.model.TotalCount;
import whyq.model.UserProfile;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.service.img.good.ImageLoader;
import whyq.utils.ImageViewHelper;
import whyq.utils.SpannableUtils;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import whyq.view.ExtendedListView;
import whyq.view.ExtendedListView.OnPositionChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.dam.R;

public class WhyqUserProfileActivity extends ImageWorkerActivity implements
		OnPositionChangedListener {

	private static final String TIME_SERVER = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_FORMAT = "HH:mm a";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	public static final String ARG_USER_ID = "userid";

	private static final Map<String, String> ACTIVITY_MAP;
	static {
		ACTIVITY_MAP = new HashMap<String, String>();
		ACTIVITY_MAP.put("friend_invite",
				"wants to add you as friend on WHY Q, accept");
		ACTIVITY_MAP.put("favourite", "favoured");
		ACTIVITY_MAP.put("comment", "commented on");
		ACTIVITY_MAP.put("comment_like", "liked a comment of");
	}

	private static int AVATAR_SIZE = 0;
	private ActivitiesAdapter mActivitiesAdapter;
	private PhotoAdapter mPhotoAdapter;
	protected String mUserId;
	private int clockHeight;
	private HorizontalListView mPhotos;
	private TextView tvHeader;
	private boolean isFriendProfile;
	private boolean isFriended;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile);

		Intent i = getIntent();

		clockHeight = (int) (getResources().getDisplayMetrics().density * 32);
		tvHeader = (TextView) findViewById(R.id.tvHeaderTitle);
		tvHeader.setText("Profile");
		ExtendedListView lv = (ExtendedListView) findViewById(R.id.listview);
		lv.setOnPositionChangedListener(this);
		mActivitiesAdapter = new ActivitiesAdapter(this);
		lv.setAdapter(mActivitiesAdapter);

		mPhotoAdapter = new PhotoAdapter(this, mImageWorker);
		int PHOTO_SIZE = WhyqApplication.sScreenWidth / 5;
		mPhotos = (HorizontalListView) findViewById(R.id.gallery);
		mPhotos.setVisibility(View.GONE);
		mPhotos.getLayoutParams().height = PHOTO_SIZE;
		mPhotos.setAdapter(mPhotoAdapter);
		mPhotos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Photo photo = (Photo) mPhotoAdapter.getItem(arg2);
				Intent i = new Intent(WhyqUserProfileActivity.this,
						WhyqImageDisplayActivity.class);
				i.putExtra(WhyqImageDisplayActivity.ARG_IMAGE_URL,
						photo.getImage());
				startActivity(i);
			}
		});

		mUserId = i.getStringExtra(ARG_USER_ID);
		if (mUserId == null) {
			mUserId = XMLParser.getUserId(WhyqApplication.Instance()
					.getApplicationContext());
			showTab(true);
		} else {
			showTab(false);
		}

		isFriendProfile = getIntent().getBooleanExtra("is_friend", false);

		ImageView setting = new ImageView(this);

		if (isFriendProfile) {
			isFriended = getIntent().getBooleanExtra("is_friended", false);
			if (isFriended) {
				setting.setImageResource(R.drawable.icon_friended);
				setting.setClickable(false);
				setting.setFocusable(false);
			} else {
				setting.setImageResource(R.drawable.icon_friend_invite);
			}

			setting.setTag(true);
			setBackButtonFieldShowing(true);
		} else {
			setting.setImageResource(R.drawable.header_icon_setting);
			setting.setTag(false);
			setBackButtonFieldShowing(false);
		}

		setExtraView(setting, true);
		//
		// hideExtraButton();
		//
		// setLoading(true);
		// getService().getUserActivities(getEncryptedToken(), mUserId);
		// getService().getPhotos(getEncryptedToken(), mUserId);
		// getService().getProfiles(getEncryptedToken(), mUserId);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		hideExtraButton();

		setLoading(true);
		getService().getUserActivities(
				WhyqApplication.Instance().getRSAToken(), mUserId);
		getService().getPhotos(WhyqApplication.Instance().getRSAToken(),
				mUserId);
		getService().getProfiles(WhyqApplication.Instance().getRSAToken(),
				mUserId);

	}

	public void exeInviteFriend() {

		if (!isFriended) {
			setLoading(true);
			Service service = new Service(WhyqUserProfileActivity.this);
			service.inviteFriendsWhyq(WhyqApplication.Instance().getRSAToken(),
					mUserId);
		}
	}

	private void bindData(UserProfile user) {

		if (user != null) {

			String userName = user.getFirst_name() + " " + user.getLast_name();
			if (userName != null) {
				setTitle(userName);
			}
			String avatar = user.getAvatar();
			if (avatar != null) {
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(
						displaymetrics);
				WhyqApplication.initScreenSize(displaymetrics.widthPixels,
						displaymetrics.heightPixels);
				ImageView imageView = (ImageView) findViewById(R.id.avatar);
				imageView.setVisibility(View.VISIBLE);
				LayoutParams LP = imageView.getLayoutParams();
				AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
				LP.width = AVATAR_SIZE;
				LP.height = AVATAR_SIZE;
				mImageWorker.downloadImage(avatar, imageView);
			}
			initCategory(user);

		}
	}

	@Override
	public void onPositionChanged(ExtendedListView listView, int position,
			View scrollBarPanel) {

		final TextView tvTime = (TextView) scrollBarPanel
				.findViewById(R.id.textTime);
		tvTime.getLayoutParams().height = clockHeight / 2;
		Util.adjustTextSizeByTextHeight(tvTime, clockHeight / 3);
		final TextView tvDate = (TextView) scrollBarPanel
				.findViewById(R.id.textDate);
		tvDate.getLayoutParams().height = clockHeight / 2;
		Util.adjustTextSizeByTextHeight(tvDate, clockHeight / 3);

		final whyq.view.AnalogClock analogClock = (whyq.view.AnalogClock) scrollBarPanel
				.findViewById(R.id.clock);

		ActivityItem item = (ActivityItem) mActivitiesAdapter.getItem(position);
		if (item != null) {
			Date date = getDate(item.getUpdatedate());
			if (date != null) {
				analogClock.setTime(date.getHours(), date.getMinutes(), 00);
			}
			tvTime.setText(converServerTimeToTime(item.getUpdatedate()));
			tvDate.setText(converServerTimeToDate(item.getUpdatedate()));
		}
	}

	@Override
	protected void onExtraButtonPressed(View v) {
		super.onExtraButtonPressed(v);

		if (isFriendProfile) {
			exeInviteFriend();
		} else {
			startActivity(new Intent(WhyqUserProfileActivity.this,
					ProfileWhyQActivty.class));
		}

	}

	private String converServerTimeToDate(String serverTime) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(TIME_SERVER);
		try {
			Date date = sdf.parse(serverTime);
			sdf.applyPattern(DATE_FORMAT);
			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "00/00/0000";
	}

	private Date getDate(String serverTime) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(TIME_SERVER);
		try {
			return sdf.parse(serverTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String converServerTimeToTime(String serverTime) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(TIME_SERVER);
		try {
			Date date = sdf.parse(serverTime);
			sdf.applyPattern(TIME_FORMAT);
			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "00:00 AM";
	}

	int count = 0;
	private UserProfile userProfile;

	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		count++;
		if (count == 3) {
			setLoading(false);
			count = 0;
		}

		showExtraButton();
		if (result == null)
			return;
		DataParser parser = new DataParser();

		if (result.getAction() == ServiceAction.ActionGetUserActivities) {
			ResponseData data = (ResponseData) parser.parseActivities(String
					.valueOf(result.getData()));

			if (data.getStatus().equals("401")) {
				Util.loginAgain(this, data.getMessage());
				return;
			} else {
				mActivitiesAdapter
						.setItems((List<ActivityItem>) data.getData());
			}
		} else if (result.getAction() == ServiceAction.ActionGetPhotos) {
			ResponseData data = (ResponseData) parser.parsePhotos(String
					.valueOf(result.getData()));
			List<Photo> photos = (List<Photo>) data.getData();
			if (photos == null || photos.size() == 0) {
				return;
			}
			mPhotos.setVisibility(View.VISIBLE);
			mPhotoAdapter.setItems(photos);
		} else if (result.getAction() == ServiceAction.ActionGetProfiles) {
			userProfile = DataParser.parseUerProfiles(String.valueOf(result
					.getData()));
			bindData(userProfile);
		} else if (result.isSuccess()
				&& (result.getAction() == ServiceAction.ActionInviteFriendsWhyQ)) {
			setLoading(false);
			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
				Toast.makeText(WhyqUserProfileActivity.this, "Invited",
						Toast.LENGTH_LONG).show();
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else if (data.getStatus().equals("204")) {

			} else {
				Util.showDialog(WhyqUserProfileActivity.this, data.getMessage());
			}
		}
	}

	private void initCategory(final UserProfile user) {
		TotalCount total = user.getTotal_count();
		if (total == null) {
			return;
		}
		// final int totalCheckBill = total.getTotal_place_check_bill();
		// bindCategory(R.id.check_bill, R.drawable.btn_blue_place,
		// totalCheckBill + " places", "checked bills",
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(WhyqUserProfileActivity.this,
		// WhyqCheckedBillActivity.class);
		// i.putExtra(WhyqCheckedBillActivity.ARG_USER_ID, mUserId);
		// startActivity(i);
		// }
		// });
		final int totalFavourite = total.getTotal_favourite();
		bindCategory(R.id.favourite_stores, R.drawable.btn_profile_fav_store,
				"" + totalFavourite, "Favourite stores", new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(WhyqUserProfileActivity.this,
								FavouriteActivity.class);
						startActivity(i);
					}
				});
		final int totalHistory = total.getTotal_check_bill();
		bindCategory(R.id.history, R.drawable.btn_profile_history,
				isFriendProfile == true ? "" + total.getTotal_friend()
						: totalHistory + "",
				isFriendProfile == true ? "Friends" : "History",
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						if (isFriendProfile) {
							Intent i = new Intent(WhyqUserProfileActivity.this,
									WhyqFriendsActivity.class);
							i.putExtra("is_friend", true);
							i.putExtra("friend_id", userProfile.getId());
							startActivity(i);
						} else {
							Intent i = new Intent(WhyqUserProfileActivity.this,
									WhyqHistoryActivity.class);
							i.putExtra(WhyqHistoryActivity.ARG_USER_ID, mUserId);
							startActivity(i);
						}
					}
				});

		final int totalFavFood = total.getTotal_favourite_foods();
		bindCategory(R.id.favourite_foods, R.drawable.btn_profile_fav_food, ""
				+ totalFavFood, "Favourtie foods", new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(WhyqUserProfileActivity.this,
						FavouriteFoodActivity.class);
				startActivity(i);
				// new Service().getFavouriteFoods(1);
				// Toast.makeText(getApplicationContext(), "In construction",
				// Toast.LENGTH_SHORT).show();
			}
		});

		final int totalComment = total.getTotal_comment();
		bindCategory(R.id.comment, R.drawable.btn_profile_comments,
				totalComment + "", "Comments", new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								WhyqUserProfileActivity.this,
								CommentActivity.class);
						intent.putExtra("is_show_filter", false);
						intent.putExtra("user_id", mUserId);
						startActivity(intent);
					}
				});
	}

	private void bindCategory(int id, int iconRes, String textTop,
			String textBottom, OnClickListener listener) {
		View categoryView = findViewById(id);
		categoryView.setOnClickListener(listener);
		categoryView.setBackgroundResource(iconRes);
		TextView textviewTop = (TextView) categoryView
				.findViewById(R.id.textviewTop);
		textviewTop.setText(textTop);
		TextView textviewBottom = (TextView) categoryView
				.findViewById(R.id.textviewBottom);
		textviewBottom.setText(textBottom);
	}

	@Override
	protected View getTitleView() {
		LinearLayout titleView = (LinearLayout) getLayoutInflater().inflate(
				R.layout.navigation_title_avatar, null);
		View title = super.getTitleView();
		LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		titleView.addView(title, 1, LP);
		return titleView;
	}

	static class ActivitiesAdapter extends BaseAdapter {

		private Context mContext;
		private List<ActivityItem> mItems;

		public ActivitiesAdapter(Context context) {
			this.mContext = context;
			this.mItems = new ArrayList<ActivityItem>();
		}

		public void setItems(List<ActivityItem> items) {
			if (items == null || items.size() == 0) {
				mItems.clear();
			} else {
				mItems = items;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			if (position < 0 || position >= mItems.size()) {
				return null;
			}
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.activities_list_item, parent, false);
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}

			ViewHolder holder = getViewHolder(convertView);
			ActivityItem item = mItems.get(position);
			String message = Html.fromHtml(item.getMessage()).toString();
			String key = ACTIVITY_MAP.get(item.getActivity_type());
			if (key == null) {
				holder.activity.setText(message);
			} else {
				holder.activity.setText(SpannableUtils.stylistText(message,
						key, 0xff808080));

			}
			if (item.getActivity_type().toUpperCase().equals("COMMENT")) {
				holder.activity.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.icon_status_comment, 0, 0, 0);
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
			TextView activity;

			public ViewHolder(View view) {
				activity = (TextView) view.findViewById(R.id.activity);
			}
		}

	}

	static class PhotoAdapter extends BaseAdapter {

		private static final int PHOTO_SIZE = WhyqApplication.sScreenWidth / 5;
		private Context mContext;
		private List<Photo> mItems;
		private ImageViewHelper mImageWorker;
		private ImageLoader mImageLoader;

		public PhotoAdapter(Context context, ImageViewHelper imageWorker) {
			this.mContext = context;
			this.mImageWorker = imageWorker;
			this.mImageLoader = WhyqApplication.Instance().getImageLoader();
			this.mItems = new ArrayList<Photo>();
		}

		public void setItems(List<Photo> items) {
			if (items == null || items.size() == 0) {
				mItems.clear();
			} else {
				mItems = items;
			}
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.photo_item, parent, false);
			}

			final Photo item = mItems.get(position);

			ViewHolder holder = getViewHolder(convertView);
			// mImageWorker.downloadImage(item.getImage(), holder.photo);
			mImageLoader.DisplayImage(item.getImage(), holder.photo);

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
			ImageView photo;

			public ViewHolder(View view) {
				photo = (ImageView) view.findViewById(R.id.photo);
				photo.getLayoutParams().width = PHOTO_SIZE;
				photo.getLayoutParams().height = PHOTO_SIZE;
			}
		}

	}

	public void onBackClicked(View v) {
		finish();
	}
}
