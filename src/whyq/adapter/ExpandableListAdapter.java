package whyq.adapter;

import java.util.HashMap;
import java.util.List;

import whyq.WhyqApplication;
import whyq.activity.FavouriteActivity;
import whyq.activity.ListActivity;
import whyq.activity.ListDetailActivity;
import whyq.adapter.WhyqAdapter.ViewHolder;
import whyq.model.GroupList;
import whyq.model.GroupMenu;
import whyq.model.Menu;
import whyq.model.Promotion;
import whyq.model.Store;
import whyq.model.UserCheckBill;
import whyq.utils.UrlImageViewHelper;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dam.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private ExpandableListView mExpandableListView;
	private List<GroupList> mGroupCollection;
	private int[] groupStatus;
	private Context context;
	private String currentPermId;
	private View convertView;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	private HashMap<String, Store> newPermList = new HashMap<String, Store>();
	public ExpandableListAdapter(Context pContext,
			ExpandableListView pExpandableListView,
			List<GroupList> pGroupCollection) {
		context = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];

		setListEvent();
	}

	private class DoubleOnCollapseListener implements
			ExpandableListView.OnGroupCollapseListener {
		private View currentView;

		public DoubleOnCollapseListener(View view) {
			currentView = view;
		}

		public void onGroupCollapse(int groupPosition) {
			currentView.getLayoutParams().height = 300;
			currentView.requestLayout();
		}
	}

	private void setListEvent() {

		mExpandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 1;
					}
				});

		mExpandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int arg0) {
						// TODO Auto-generated method stub
						groupStatus[arg0] = 0;

					}
				});
	}

	@Override
	public String getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getMenuList().get(arg1)
				.getStoreInfo().getCateid();
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		// TODO Auto-generated method stub
		View view = arg3;
		final Store store = mGroupCollection.get(arg0).getMenuList().get(arg1);
		final String viewId = store.getStoreId();

		currentPermId = viewId;
		convertView = viewList.get(viewId);
		newPermList.put(viewId, store);
		if (convertView != null){

			return convertView;
		}else{

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.whyq_item_new, null);
//			LayoutParams PARAMS = (LinearLayout.LayoutParams)rowView.getLayoutParams();
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.id = store.getStoreId();
			viewHolder.imgThumb = (ImageView) rowView.findViewById(R.id.imgThumbnal2);
			viewHolder.tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
			viewHolder.tvItemAddress = (TextView)rowView.findViewById(R.id.tvItemAddress);
			viewHolder.tvNumberFavourite = (TextView)rowView.findViewById(R.id.tvNumberFavourite);
			viewHolder.tvVisited = (TextView)rowView.findViewById(R.id.tvVisited);
			viewHolder.tvDiscoutNumber = (TextView)rowView.findViewById(R.id.tvNumberDiscount);
			viewHolder.btnDistance = (Button)rowView.findViewById(R.id.btnDistance);
			viewHolder.imgFavouriteThumb = (ImageView)rowView.findViewById(R.id.imgFavourite);
			viewHolder.prgFavourite = (ProgressBar)rowView.findViewById(R.id.prgFavourite);
			viewHolder.rlDiscount = (RelativeLayout)rowView.findViewById(R.id.rlDiscount);
			viewHolder.tvItemName.setText(store.getNameStore().toUpperCase());
			viewHolder.tvItemAddress.setText(store.getAddress());
			viewHolder.tvNumberFavourite.setText(""+store.getCountFavaouriteMember());
			if(store.getPromotionList().size() > 0){
				viewHolder.tvDiscoutNumber.setVisibility(View.VISIBLE);
				Promotion promotion = store.getPromotionList().get(0);
				viewHolder.tvDiscoutNumber.setText(""+promotion.getValuePromotion()+promotion.getTypeValue()+ " for order over $"+promotion.getConditionPromotion());
			}else{
				viewHolder.rlDiscount.setVisibility(View.GONE);
				
			}
			
			viewHolder.btnDistance.setText((int)Float.parseFloat(store.getDistance())+" km");
			viewHolder.imgFriendThumb = (ImageView)rowView.findViewById(R.id.imgFriendThumb);
			
			UserCheckBill userCheckBill = store.getUserCheckBill();
			if(userCheckBill !=null){
				if(userCheckBill.getTotalMember()!=null && !userCheckBill.getTotalMember().equals("")){
					if(userCheckBill.getAvatar()!=null && !userCheckBill.getAvatar().equals("")){
						if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
							viewHolder.tvVisited.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" others visited");	
						}else{
							viewHolder.tvVisited.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" other visited");
						}
						
						viewHolder.imgFriendThumb.setVisibility(View.VISIBLE);
//						UrlImageViewHelper.setUrlDrawable(viewHolder.imgFriendThumb, userCheckBill.getAvatar());
						WhyqApplication.Instance().getImageLoader().DisplayImage(userCheckBill.getAvatar(), viewHolder.imgFriendThumb);
					}else{
						if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
							viewHolder.tvVisited.setText(userCheckBill.getTotalMember()+" others visited");
						}else{
							viewHolder.tvVisited.setText(userCheckBill.getTotalMember()+" other visited");
						}
						
					}

				}
			}
//			if(item.getCountFavaouriteMember() !=null)
//				if(!item.getCountFavaouriteMember().equals(""))
//					if(Integer.parseInt(item.getCountFavaouriteMember()) >0){
////						viewHolder.imgFavouriteThumb.setBackgroundResource(R.drawable.icon_fav_enable);
//						viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_enable);
//			}
			if(store.getIsFavourite()){
				viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_enable);
			}else{
				viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_disable);
			}
			viewHolder.imgFavouriteThumb.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if( FavouriteActivity.isFavorite)
						((FavouriteActivity)context).onFavouriteClicked(v);
					else
						((ListActivity)context).onFavouriteClicked(v);
				}
			});
//			UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, store.getLogo());
			WhyqApplication.Instance().getImageLoader().DisplayImage(store.getLogo(), viewHolder.imgThumb);
			rowView.setTag(viewHolder);
			rowView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("fdsfsfsdfs","fdsfsdfsf");
					Intent intent = new Intent(context, ListDetailActivity.class);
					intent.putExtra("store_id", ListActivity.storeId);
					intent.putExtra("id", store.getStoreId());
					context.startActivity(intent);
				}
			});
			if (store != null) {
				

			}
			viewHolder.imgFavouriteThumb.setTag(store);
			viewHolder.btnDistance.setTag(store);
			rowView.setEnabled(true);
			viewList.put(store.getStoreId(), rowView);
			return rowView;
			}

	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getMenuList().size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return mGroupCollection.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
		// TODO Auto-generated method stub
		GroupHolder groupHolder;
		if (arg2 == null) {
			arg2 = LayoutInflater.from(context).inflate(R.layout.list_group,
					null);
			groupHolder = new GroupHolder();
			groupHolder.img = (ImageView) arg2.findViewById(R.id.tag_img);
			groupHolder.title = (TextView) arg2.findViewById(R.id.group_title);
			arg2.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) arg2.getTag();
		}
		if (groupStatus[arg0] == 0) {
			groupHolder.img.setImageResource(R.drawable.group_down);
		} else {
			groupHolder.img.setImageResource(R.drawable.group_up);
		}

		LayoutParams params = (RelativeLayout.LayoutParams) groupHolder.title
				.getLayoutParams();
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.LEFT_OF, R.id.tag_img);
		groupHolder.title.setLayoutParams(params);
		groupHolder.title.setText(mGroupCollection.get(arg0).getName());
		groupHolder.title.setTextColor(Color.parseColor("#ffffff"));
		arg2.setBackgroundColor(Color.parseColor("#000000"));
		// arg2.getBackground().setAlpha(40);

		return arg2;
	}

	class GroupHolder {
		ImageView img;
		TextView title;
	}

	public static class ViewHolderMitemInfo {
		public TextView tvType, tvPrice, tvCount;
		public ImageView imgThumb;
		public String storeId;
		public Button btnAdd;
		public Button btnRemove;
		public String menuId;
		public LinearLayout lnPreview;
		public RelativeLayout rlExtraView;
		public Button btnDoneSelect;
		public TextView tvItem1NameOption;
		public TextView tvItem1PriceOption;
		public TextView tvItem2NameOption;
		public TextView tvItem2PriceOption;
		public TextView tvItem3NameOption;
		public TextView tvItem3PriceOption;
		
		public TextView tvItem1NameSize;
		public TextView tvItem1PriceSize;
		public TextView tvItem2NameSize;
		public TextView tvItem2PriceSize;
		public TextView tvItem3NameSize;
		public TextView tvItem3PriceSize;

		public TextView tvItem1NameExtra;
		public TextView tvItem1PriceExtra;
		public TextView tvItem2NameExtra;
		public TextView tvItem2PriceExtra;
		public TextView tvItem3NameExtra;
		public TextView tvItem3PriceExtra;
		public LinearLayout lnItem1Option;
		public LinearLayout lnItem2Option;
		public LinearLayout lnItem3Option;
		
		public LinearLayout lnItem1Size;
		public LinearLayout lnItem2Size;
		public LinearLayout lnItem3Size;
		
		public LinearLayout lnItem1Extra;
		public LinearLayout lnItem2Extra;
		public LinearLayout lnItem3Extra;
		

		public ViewHolderMitemInfo() {

		}
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
