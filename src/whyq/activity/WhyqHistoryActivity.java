package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.WhyqApplication;
import whyq.activity.WhyqHistoryActivity.BillAdapter.ViewHolder;
import whyq.model.BillItem;
import whyq.model.BusinessInfo;
import whyq.model.ResponseData;
import whyq.service.DataParser;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.ImageViewHelper;
import whyq.utils.Util;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.costum.android.widget.LoadMoreListView;
import com.dealadelivery.whyq.R;

public class WhyqHistoryActivity extends ImageWorkerActivity {

	public static final String ARG_USER_ID = "userid";

	private BillAdapter mAdapter;

	private LoadMoreListView mListview;
	protected int mPage = 0;
	protected int mTotalPage;
	
	private static final int[] STATUS_MAP = new int[] {R.drawable.waiting_for_payment, R.drawable.declined, R.drawable.delivered, R.drawable.dismissed, R.drawable.waiting_to_be_accepted,  R.drawable.paid};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_history);

		setTitle(R.string.history);

		mAdapter = new BillAdapter(this, mImageWorker);
		mListview = (LoadMoreListView) findViewById(R.id.listview);
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ViewHolder holder = (ViewHolder)arg1.getTag();
				Intent intent = new Intent(WhyqHistoryActivity.this, WhyQBillScreen.class);
				
				BillItem item = holder.data;
				
				Bundle bundle = new Bundle();
				bundle.putString("store_id", item.getStore_id());
				bundle.putString("bill_id", item.getId());
//				bundle.putString("list_items", item.getBusiness_info());
				bundle.putBoolean("is_ordered", true);
				bundle.putString("bill_status", item.getStatus_bill()> 0?"1":"0");
				bundle.putString("lat", "" + item.getBusiness_info().getLatitude());
				bundle.putString("lon", "" + item.getBusiness_info().getLongitude());
				bundle.putString("start_time", "" + item.getBusiness_info().getStart_time());
				bundle.putString("close_time", "" + item.getBusiness_info().getEnd_time());
				intent.putExtra("data", bundle);
				startActivity(intent);
			}
		});

		mListview.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Log.d("loadmore listener", mPage+ "and total is "+ mTotalPage);
				if( mPage < mTotalPage){
					mPage++;
					exeGetData(mPage);
					
				}else{
					mListview.onLoadMoreComplete();
				}
			}
		});
		mPage = 1;
		exeGetData(mPage);
	}
	
	private void exeGetData(int page) {
		// TODO Auto-generated method stub
		setLoading(true);
		String userId = getIntent().getStringExtra(ARG_USER_ID);
		getService().getHistories(WhyqApplication.getRSAToken(), userId, page);
	}

	public void onBackClicked(View v){
		finish();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		super.onCompleted(service, result);
		setLoading(false);
		mListview.onLoadMoreComplete();
		if (result != null && result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetHistories) {
			DataParser parser = new DataParser();
			ResponseData data = (ResponseData) parser.parseBills(String.valueOf(result.getData()));
			mTotalPage = data.getTotalPage();
			if (data.getStatus().equals("401")) {
				Util.loginAgain(this, data.getMessage()) ;
			} else if(data.getStatus().equals("200")) {
				List<BillItem> newData = mAdapter.getData();
				if(mPage == 1){
					newData.clear();
				}
				newData.addAll((List<BillItem>) data.getData());
				mAdapter.setItems(newData);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	static class BillAdapter extends BaseAdapter {

		private static final int PHOTO_SIZE = WhyqApplication.sBaseViewHeight / 5 * 4;
		private Context mContext;
		private List<BillItem> mItems;
		private ImageViewHelper mImageWorker;

		public BillAdapter(Context context, ImageViewHelper imageWorker) {
			this.mContext = context;
			this.mItems = new ArrayList<BillItem>();
			this.mImageWorker = imageWorker;
		}

		public void setItems(List<BillItem> items) {
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
						R.layout.whyq_bill_list_item, parent, false);
				Util.applyTypeface(convertView,
						WhyqApplication.sTypefaceRegular);
			}
			BillItem item = mItems.get(position);
			ViewHolder holder = getViewHolder(convertView, item);
			
//			if (item.getStatus_bill() > 0 && item.getStatus_bill() <= 6) {
//				holder.circleIcon.setImageResource(STATUS_MAP[item.getStatus_bill() - 1]);
//			} else {
//				holder.circleIcon.setImageResource(R.drawable.circle);
//			}
			int statusBill = item.getStatus_bill();
			
			if(statusBill == -1){
//				holder.circleIcon.setImageResource(R.drawable.circle);
				holder.circleIcon.setImageResource(STATUS_MAP[1]);
			}else if(statusBill == 0){
				
			}else if(statusBill == 1){
				holder.circleIcon.setImageResource(STATUS_MAP[0]);
			}else if(statusBill == 2 || statusBill == 6){
				holder.circleIcon.setImageResource(STATUS_MAP[5]);
			}else if(statusBill == 3 || statusBill == 7){
				holder.circleIcon.setImageResource(STATUS_MAP[2]);
			}else if(statusBill == 4){
				holder.circleIcon.setImageResource(STATUS_MAP[3]);
			}else if(statusBill == 5){
				
			}else{
				holder.circleIcon.setImageResource(R.drawable.circle);
			}
			
			holder.circleIcon.setVisibility(View.VISIBLE);
			BusinessInfo bi = item.getBusiness_info();
			if (bi != null) {
				holder.name.setText(bi.getName_store());
				mImageWorker.downloadImage(bi.getLogo(), holder.photo);
			}
			holder.unit.setText("Order id: " + item.getId());
			holder.price.setVisibility(View.GONE);

			return convertView;
		}

		private ViewHolder getViewHolder(View view, BillItem item) {
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder == null) {
				holder = new ViewHolder(view, item);
				view.setTag(holder);
			}
			return holder;
		}

		class ViewHolder {
			ImageView circleIcon;
			ImageView photo;
			TextView name;
			TextView unit;
			Button price;
			private BillItem data;

			public ViewHolder(View view, BillItem item) {
				circleIcon = (ImageView) view.findViewById(R.id.circle);
				photo = (ImageView) view.findViewById(R.id.photo);
				photo.getLayoutParams().width = PHOTO_SIZE;
				photo.getLayoutParams().height = PHOTO_SIZE;
				name = (TextView) view.findViewById(R.id.name);
				unit = (TextView) view.findViewById(R.id.unit);
				price = (Button) view.findViewById(R.id.price);
				data = item;
			}
		}

		public List<BillItem> getData(){
			return mItems;
		}
		public void changeSrc(List<BillItem> list){
			mItems = list;
		}
	}
}
