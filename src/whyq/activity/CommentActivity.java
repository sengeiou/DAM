package whyq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.WhyqApplication;
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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.dealadelivery.whyq.R;

public class CommentActivity extends ImageWorkerActivity {

	private CommentAdapter mAdapter;
	private RadioGroup mFilterLayout;
	private String mStoreId;
	protected int mPage = 0;
	protected int mTotalPage;
	private LoadMoreListView mListview;
	private boolean isLoadMore = false;
	private boolean mIsShowFilter;
	private String mUserId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_comment);

		setTitle(R.string.comment_title);

		mAdapter = new CommentAdapter(this, mImageWorker);

	    mListview = (LoadMoreListView) findViewById(R.id.listview);
		mListview.setAdapter(mAdapter);

		Intent i = getIntent();
		mIsShowFilter = i.getBooleanExtra("is_show_filter", false);
		if(mIsShowFilter){
			mStoreId = i.getStringExtra("store_id");	
		}else{
			mUserId = i.getStringExtra("user_id");
		}
		
		
		if (mStoreId == null) {
			mStoreId = "";
		}

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
		
		View filter = getLayoutInflater().inflate(R.layout.filter, null);
		setExtraView(filter);
		
		if(!mIsShowFilter){
			filter.setVisibility(View.INVISIBLE);
			findViewById(R.id.btnCommentHere).setVisibility(View.GONE);
		}
		mListview.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Log.d("loadmore listener", mPage+ "and total is "+ mTotalPage);
				if( mPage < mTotalPage){
					mPage++;
					getComments(true);
					
				}else{
					mListview.onLoadMoreComplete();
				}
				
			}
		});
//		getComments(false);
//		listview.setOnScrollListener(new AbsListView.OnScrollListener() {
//			
//		
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//				int currentItem = firstVisibleItem + visibleItemCount;
//				Log.d("onScroll","onScroll current "+currentItem+" and total "+totalItemCount);
//				if((currentItem >=  totalItemCount-1) && !isLoadMore){
//					isLoadMore = true;
//					page++;
//					getComments(false);
//				}
//			}
//		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d("onResume","storeId "+mStoreId);
		super.onResume();
//		page = 1;
		mPage = 1;
		getComments(false);
	}
	
	private void getComments(boolean onlyFriend) {
		setLoading(true);
//		if(!isLoadMore)
//			page = 1;
		getService().getComments(WhyqApplication.getRSAToken(), mIsShowFilter?mStoreId:mUserId, mPage , 20,
				onlyFriend, mIsShowFilter);
	}

	@Override
	protected void onExtraButtonPressed(View v) {
		super.onExtraButtonPressed(v);
		if(mIsShowFilter){
			if (mFilterLayout.getVisibility() == View.VISIBLE) {
				mFilterLayout.setVisibility(View.GONE);
				findViewById(R.id.triAngle).setVisibility(View.GONE);
			} else {
				mFilterLayout.setVisibility(View.VISIBLE);
				findViewById(R.id.triAngle).setVisibility(View.VISIBLE);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		mListview.onLoadMoreComplete();
		if (result != null && result.getCode() == ResultCode.Success
				&& result.getAction() == ServiceAction.ActionGetComment) {
			DataParser paser = new DataParser();
			ResponseData data = (ResponseData) paser.parseComments(String
					.valueOf(result.getData()));
			mTotalPage = data.getTotalPage();
			if (data == null) {
				return;
			}
			
			if (data.getStatus().equals("401")) {
				Util.loginAgain(this, data.getStatus());
			} if (data.getStatus().equals("204")) {
				isLoadMore = false;
//				page = 1;
			} else {
				if(mAdapter != null){//isLoadMore
					List<Comment> newData = mAdapter.getItems();
					
					if(data !=null && data.getData() !=null){
						if(mPage == 1){
							newData.clear();
						}
						newData.addAll((List<Comment>) data.getData());
						mAdapter.setItems(newData);
						mAdapter.notifyDataSetChanged();
					}
				}else{
					mAdapter.setItems((List<Comment>) data.getData());
					mAdapter.notifyDataSetChanged();
				}
				
			}
		}
	}

	static class CommentAdapter extends BaseAdapter {

		private static final int AVATAR_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private static final int THUMB_HEIGHT = WhyqApplication.sBaseViewHeight * 5;
		private Context mContext;
		private List<Comment> mItems;
		private ImageViewHelper mImageWorker;
		private ImageLoader mImageLoader;

		public CommentAdapter(Context context, ImageViewHelper imageWorker) {
			this.mContext = context;
			this.mItems = new ArrayList<Comment>();
			this.mImageWorker = imageWorker;
			this.mImageLoader = WhyqApplication.Instance().getImageLoader();
		}

		public void setItems(List<Comment> items) {
			if (items == null || items.size() == 0) {
				mItems.clear();
			} else {
				mItems = items;
			}
			notifyDataSetChanged();
		}

		public List<Comment> getItems(){
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

		HashMap<String, View > viewList = new HashMap<String, View>();
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Comment item = mItems.get(position);
			convertView = viewList.get(item.getId());
			if(convertView==null){
				if (convertView == null) {
					convertView = LayoutInflater.from(mContext).inflate(
							R.layout.comment_list_item, parent, false);
					Util.applyTypeface(convertView,
							WhyqApplication.sTypefaceRegular);
				}

				ViewHolder holder = getViewHolder(convertView);
				

				holder.name.setText(item.getUser().getFirstName()+" "+item.getUser().getLastName());
				holder.comment.setText(item.getContent());
				holder.like.setText("" + item.getCount_like());
				holder.like.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
//				mImageWorker.downloadImage(item.getUser().getUrlAvatar(),
//						holder.avatar);
				mImageLoader.DisplayImage(item.getUser().getUrlAvatar(), holder.avatar);
				Photo photo = item.getPhotos();
				if (photo != null && photo.getThumb() != null) {
//					mImageWorker.downloadImage(item.getPhotos().getThumb(),
//							holder.thumb);
					holder.thumb.setVisibility(View.VISIBLE);
					mImageLoader.DisplayImage(item.getPhotos().getThumb(), holder.thumb);
				}else{
					holder.thumb.setVisibility(View.GONE);
				}
				if (item.getUser().getLike() > 0) {
					holder.favorite.setImageResource(R.drawable.icon_fav_enable);
				} else {
					holder.favorite.setImageResource(R.drawable.icon_fav_disable);
				}

				return convertView;
			}else {
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
	public void onCommentHereClicked(View v){
		Intent i = new Intent(CommentActivity.this, WhyqShareActivity.class);
		i.putExtra("store_id", mStoreId);
		i.putExtra("is_comment", true);
		startActivity(i);
	}
	
	public void onBackClicked(View v){
		finish();
	}
}
