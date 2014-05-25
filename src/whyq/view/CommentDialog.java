package whyq.view;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.dam.R;

import whyq.WhyqApplication;
import whyq.activity.WhyqShareActivity;
import whyq.interfaces.IServiceListener;
import whyq.model.Comment;
import whyq.model.Photo;
import whyq.model.ResponseData;
import whyq.service.DataParser;
import whyq.service.ResultCode;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.service.img.good.ImageLoader;
import whyq.utils.ImageViewHelper;
import whyq.utils.Util;
import whyq.utils.XMLParser;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CommentDialog extends Dialog implements IServiceListener {
	private CommentAdapter mAdapter;
	private RadioGroup mFilterLayout;
	private int page = 1;
	private boolean isLoadMore = false;
	private boolean mIsShowFilter;

	// this should be storeId or userId
	private String id;

	public CommentDialog(Context context, boolean mIsShowFilter, String id) {
		super(context);
		// this makes dialog full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//remove dialog border
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		//allow to cancel by click to screen
		setCancelable(true);

		this.mIsShowFilter = mIsShowFilter;
		this.id = id;
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_comment);

		mAdapter = new CommentAdapter(getContext());

		ListView listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(mAdapter);

		mFilterLayout = (RadioGroup) findViewById(R.id.filterLayout);
		mFilterLayout.getLayoutParams().width = WhyqApplication.sScreenWidth * 2 / 3;
		mFilterLayout.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.viewAll) {
					getComments(false);
				} else {
					getComments(true);
				}
			}
		});

		// View filter = getLayoutInflater().inflate(R.layout.filter, null);
		// setExtraView(filter);

		if (!mIsShowFilter) {
			// filter.setVisibility(View.INVISIBLE);
			findViewById(R.id.btnCommentHere).setVisibility(View.GONE);
		}
		// getComments(false);
		listview.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				int currentItem = firstVisibleItem + visibleItemCount;
				Log.d("onScroll", "onScroll current " + currentItem
						+ " and total " + totalItemCount);
				if ((currentItem >= totalItemCount - 1) && !isLoadMore) {
					isLoadMore = true;
					page++;
					getComments(false);
				}
			}
		});
		
		findViewById(R.id.btnCommentHere).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), WhyqShareActivity.class);
				i.putExtra("store_id", mIsShowFilter ? id : "");
				i.putExtra("is_comment", true);
				getContext().startActivity(i);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		Log.e("CommentDialog", "onCompleted");
		setLoading(false);
		if (result != null && result.getCode() == ResultCode.Success
				&& result.getAction() == ServiceAction.ActionGetComment) {
			DataParser paser = new DataParser();
			ResponseData data = (ResponseData) paser.parseComments(String
					.valueOf(result.getData()));
			if (data == null) {
				return;
			}

			if (data.getStatus().equals("401")) {
				Util.loginAgain(getContext(), data.getStatus());
			}
			if (data.getStatus().equals("204")) {
				isLoadMore = false;
				page = 1;
			} else {
				if (isLoadMore) {
					List<Comment> newData = mAdapter.getItems();
					if (data != null && data.getData() != null) {
						newData.addAll((List<Comment>) data.getData());
						mAdapter.setItems(newData);
					}
				} else {
					mAdapter.setItems((List<Comment>) data.getData());
				}

			}
		}
	}

	private void getComments(boolean onlyFriend) {
		setLoading(true);
		if (!isLoadMore)
			page = 1;
		new Service(this).getComments(WhyqApplication.getRSAToken(), id, page, 20,
				onlyFriend, mIsShowFilter);
	}

	protected void setLoading(boolean show) {
		findViewById(R.id.pgb_loading).setVisibility(
				show ? View.VISIBLE : View.GONE);
	}

	static class CommentAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private static final int THUMB_HEIGHT = WhyqApplication.sBaseViewHeight * 5;
		private Context mContext;
		private List<Comment> mItems;
		private ImageLoader mImageLoader;

		public CommentAdapter(Context context) {
			this.mContext = context;
			this.mItems = new ArrayList<Comment>();
			this.mImageLoader = new ImageLoader(context);
		}

		public void setItems(List<Comment> items) {
			if (items == null || items.size() == 0) {
				mItems.clear();
			} else {
				mItems = items;
			}
			notifyDataSetChanged();
		}

		public List<Comment> getItems() {
			return mItems;
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

		HashMap<String, View> viewList = new HashMap<String, View>();

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Comment item = mItems.get(position);
			convertView = viewList.get(item.getId());
			if (convertView == null) {
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.comment_list_item, parent, false);
					Util.applyTypeface(convertView,
							WhyqApplication.sTypefaceRegular);
				}

				ViewHolder holder = getViewHolder(convertView);

				holder.name.setText(item.getUser().getFirstName() + " "
						+ item.getUser().getLastName());
				holder.comment.setText(item.getContent());
				holder.like.setText("" + item.getCount_like());
				holder.like.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
				// mImageWorker.downloadImage(item.getUser().getUrlAvatar(),
				// holder.avatar);
				mImageLoader.DisplayImage(item.getUser().getUrlAvatar(),
						holder.avatar);
				Photo photo = item.getPhotos();
				if (photo != null) {
					// mImageWorker.downloadImage(item.getPhotos().getThumb(),
					// holder.thumb);
					mImageLoader.DisplayImage(item.getPhotos().getThumb(),
							holder.thumb);
				}
				if (item.getUser().getLike() > 0) {
					holder.favorite
							.setImageResource(R.drawable.icon_fav_enable);
				} else {
					holder.favorite
							.setImageResource(R.drawable.icon_fav_disable);
				}

				return convertView;
			} else {
				return convertView;
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

		class ViewHolder {
			ImageView avatar;
			ImageView favorite;
			ImageView thumb;
			TextView name;
			TextView like;
			TextView comment;

			public ViewHolder(View view) {
				view.findViewById(R.id.infor).getLayoutParams().height = WhyqApplication.sBaseViewHeight;
				avatar = (ImageView) view.findViewById(R.id.avatar);
				avatar.getLayoutParams().width = AVATAR_SIZE;
				avatar.getLayoutParams().height = AVATAR_SIZE;
				thumb = (ImageView) view.findViewById(R.id.imageThumb);
				thumb.getLayoutParams().height = THUMB_HEIGHT;

				favorite = (ImageView) view.findViewById(R.id.favorite);
				name = (TextView) view.findViewById(R.id.name);
				like = (TextView) view.findViewById(R.id.like);
				comment = (TextView) view.findViewById(R.id.comment);
			}
		}

	}

}
