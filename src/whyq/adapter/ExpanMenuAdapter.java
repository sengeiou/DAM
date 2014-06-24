package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.interfaces.IServiceListener;
import whyq.model.Bill;
import whyq.model.ExtraItem;
import whyq.model.GroupMenu;
import whyq.model.Menu;
import whyq.model.OptionItem;
import whyq.model.ResponseData;
import whyq.model.SizeItem;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.WebImageView;
import com.dam.R;

public class ExpanMenuAdapter extends BaseExpandableListAdapter implements OnClickListener {

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}

	private Context mContext;
	private ExpandableListView mExpandableListView;
	private List<GroupMenu> mGroupCollection;

	public List<GroupMenu> getmGroupCollection() {
		return mGroupCollection;
	}

	public void setmGroupCollection(List<GroupMenu> mGroupCollection) {
		this.mGroupCollection = mGroupCollection;
	}

	private int[] groupStatus;
	private int mFocusColor;
	private int mNormalColor;

	public static Map<String, String> noteList = new HashMap<String, String>();
	
	public ExpanMenuAdapter(Context pContext, ExpandableListView pExpandableListView,
			List<GroupMenu> pGroupCollection) {
		mContext = pContext;
		mGroupCollection = pGroupCollection;
		mExpandableListView = pExpandableListView;
		groupStatus = new int[mGroupCollection.size()];

		mFocusColor = mContext.getResources().getColor(R.color.red);
		mNormalColor = mContext.getResources().getColor(R.color.grey);
		setListEvent();
	}

	private class DoubleOnCollapseListener implements ExpandableListView.OnGroupCollapseListener {
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

		mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int arg0) {
				// TODO Auto-generated method stub
				groupStatus[arg0] = 1;
			}
		});

		mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int arg0) {
				// TODO Auto-generated method stub
				groupStatus[arg0] = 0;

			}
		});
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mGroupCollection.get(arg0).getMenuList().get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		Log.d("getChildrenCount", "size: " + mGroupCollection.get(arg0).getMenuList().size());
		return mGroupCollection.get(arg0).getMenuList().size();
	}

	Map<String, View> viewList = new HashMap<String, View>();
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View view, ViewGroup arg4) {
		// TODO Auto-generated method stub
	
		final Menu item = mGroupCollection.get(arg0).getMenuList().get(arg1);
		Log.d("getChildView", "getChildView id "+item.getId());

//		View v = viewList.get(item.getId());
//		if(v!=null)
//		{
//			return v;
//		}else
		{
			LayoutInflater inflator = ((ListDetailActivity) mContext).getLayoutInflater();
			final ViewHolderMitemInfo viewHolder;
//			if(view!=null){
//				viewHolder = (ViewHolderMitemInfo) view.getTag();
//			}
			if (true) {//view == null
				viewHolder = new ViewHolderMitemInfo();
				view = inflator.inflate(R.layout.whyq_menu_item_phase2, null);
				viewHolder.tvType = (TextView) view.findViewById(R.id.tvType);
				viewHolder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
				viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgThumbnail);
				viewHolder.tvCount = (TextView) view.findViewById(R.id.totalForItem);
				viewHolder.tvType.setTypeface(Util.sTypefaceRegular);
				viewHolder.tvPrice.setTypeface(Util.sTypefaceRegular);
				viewHolder.btnAdd = (Button) view.findViewById(R.id.btnAdd);
				viewHolder.btnDoneSelect = (Button) view.findViewById(R.id.btn_done_select_extra);
				viewHolder.btnRemove = (Button) view.findViewById(R.id.btnRemove);
				viewHolder.lnPreview = (LinearLayout) view.findViewById(R.id.ln_preview_extra_selected);
				viewHolder.rlExtraView = (RelativeLayout) view.findViewById(R.id.rl_extra_view);
				viewHolder.tvFavouriteCount = (TextView) view.findViewById(R.id.tv_favourite_food_count);
				viewHolder.imgFavourite = (ImageView) view.findViewById(R.id.imgFavouriteFood);

				viewHolder.tvItem1NameSize = (TextView) view.findViewById(R.id.tv_item1_name_size);
				viewHolder.tvItem1PriceSize = (TextView) view.findViewById(R.id.tv_item1_price_size);
				viewHolder.tvItem2NameSize = (TextView) view.findViewById(R.id.tv_item2_name_size);
				viewHolder.tvItem2PriceSize = (TextView) view.findViewById(R.id.tv_item2_price_size);
				viewHolder.tvItem3NameSize = (TextView) view.findViewById(R.id.tv_item3_name_size);
				viewHolder.tvItem3PriceSize = (TextView) view.findViewById(R.id.tv_item3_price_size);
				
				viewHolder.tvItem1NameOption = (TextView) view.findViewById(R.id.tv_item1_name_option);
				viewHolder.tvItem1PriceOption = (TextView) view.findViewById(R.id.tv_item1_price_option);
				viewHolder.tvItem2NameOption = (TextView) view.findViewById(R.id.tv_item2_name_option);
				viewHolder.tvItem2PriceOption = (TextView) view.findViewById(R.id.tv_item2_price_option);
				viewHolder.tvItem3NameOption = (TextView) view.findViewById(R.id.tv_item3_name_option);
				viewHolder.tvItem3PriceOption = (TextView) view.findViewById(R.id.tv_item3_price_option);



				viewHolder.tvItem1NameExtra = (TextView) view.findViewById(R.id.tv_item1_name_extra);
				viewHolder.tvItem1PriceExtra = (TextView) view.findViewById(R.id.tv_item1_price_extra);
				viewHolder.tvItem2NameExtra = (TextView) view.findViewById(R.id.tv_item2_name_extra);
				viewHolder.tvItem2PriceExtra = (TextView) view.findViewById(R.id.tv_item2_price_extra);
				viewHolder.tvItem3NameExtra = (TextView) view.findViewById(R.id.tv_item3_name_extra);
				viewHolder.tvItem3PriceExtra = (TextView) view.findViewById(R.id.tv_item3_price_extra);


				viewHolder.lnItem1Size = (LinearLayout) view.findViewById(R.id.ln_item1_size);
				viewHolder.lnItem2Size = (LinearLayout) view.findViewById(R.id.ln_item2_size);
				viewHolder.lnItem3Size = (LinearLayout) view.findViewById(R.id.ln_item3_size);
				
				viewHolder.lnItem1Option = (LinearLayout) view.findViewById(R.id.ln_item1_option);
				viewHolder.lnItem2Option = (LinearLayout) view.findViewById(R.id.ln_item2_option);
				viewHolder.lnItem3Option = (LinearLayout) view.findViewById(R.id.ln_item3_option);


				viewHolder.lnItem1Extra = (LinearLayout) view.findViewById(R.id.ln_item1_extra);
				viewHolder.lnItem2Extra = (LinearLayout) view.findViewById(R.id.ln_item2_extra);
				viewHolder.lnItem3Extra = (LinearLayout) view.findViewById(R.id.ln_item3_extra);


				
				viewHolder.btnAdd.setOnClickListener(this);
				viewHolder.btnRemove.setOnClickListener(this);
				viewHolder.btnDoneSelect.setOnClickListener(this);
				
				
			} else {
				viewHolder = (ViewHolderMitemInfo) view.getTag();
			}

			
			viewHolder.tvFavouriteCount.setText(""+item.getCountFavorite());
			viewHolder.imgFavourite.setImageResource(item.isFavorite()? R.drawable.icon_fav_enable: R.drawable.icon_fav_disable);
//			boolean isItemInBillList = ListDetailActivity.billList.containsKey(item.getId());
			boolean isItemInBillList = item.getUnitForBill() > 0;

//			setViewVisibility(viewHolder.lnPreview, isItemInBillList);
			setViewVisibility(viewHolder.rlExtraView, isItemInBillList);	
			
			
			setViewVisibility(viewHolder.tvItem1NameOption, isItemInBillList);
			setViewVisibility(viewHolder.tvItem1PriceOption, isItemInBillList);
			setViewVisibility(viewHolder.tvItem2NameOption, isItemInBillList);
			setViewVisibility(viewHolder.tvItem2PriceOption, isItemInBillList);
			setViewVisibility(viewHolder.tvItem3NameOption, isItemInBillList);
			setViewVisibility(viewHolder.tvItem3PriceOption, isItemInBillList);

			setViewVisibility(viewHolder.tvItem1NameSize, isItemInBillList);
			setViewVisibility(viewHolder.tvItem1PriceSize, isItemInBillList);
			setViewVisibility(viewHolder.tvItem2NameSize, isItemInBillList);
			setViewVisibility(viewHolder.tvItem2PriceSize, isItemInBillList);
			setViewVisibility(viewHolder.tvItem3NameSize, isItemInBillList);
			setViewVisibility(viewHolder.tvItem3PriceSize, isItemInBillList);

			setViewVisibility(viewHolder.tvItem1NameExtra, isItemInBillList);
			setViewVisibility(viewHolder.tvItem1PriceExtra, isItemInBillList);
			setViewVisibility(viewHolder.tvItem2NameExtra, isItemInBillList);
			setViewVisibility(viewHolder.tvItem2PriceExtra, isItemInBillList);
			setViewVisibility(viewHolder.tvItem3NameExtra, isItemInBillList);
			setViewVisibility(viewHolder.tvItem3PriceExtra, isItemInBillList);

			
			/*
			 * Set text note change
			 */
//			setViewVisibility(viewHolder.etNote, isItemInBillList);
			viewHolder.etNote = (EditText) view.findViewById(R.id.et_note);
			viewHolder.etNote.setText(item.getNote());
			setViewVisibility(view.findViewById(R.id.rl_note), isItemInBillList);
			viewHolder.etNote.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					item.setNote(s.toString());
					noteList.put(item.getId(), s.toString());
				}
			});
			


			/***
			 * Bind option data to item
			 */
			boolean isHaveMenu = false;
			
			if (item.getOptionItemList() != null && item.getOptionItemList().size() > 0) {
				List<OptionItem> list = item.getOptionItemList();
				
				for (int i = 0; i < list.size(); i++) {
					OptionItem itemDetail = list.get(i);
					if (i == 0) {
						setViewVisibility(viewHolder.tvItem1NameOption, true);
						setViewVisibility(viewHolder.tvItem1PriceOption, true);
						viewHolder.tvItem1NameOption.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem1PriceOption.setText("$" + itemDetail.getValue());
						viewHolder.tvItem1PriceOption.setTag(itemDetail);
						
						viewHolder.lnItem1Option.setTag(itemDetail);
						viewHolder.lnItem1Option.setOnClickListener(this);
						
						viewHolder.tvItem1NameOption.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem1PriceOption.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
					} else if (i == 1) {

						setViewVisibility(viewHolder.tvItem2NameOption, true);
						setViewVisibility(viewHolder.tvItem2NameOption, true);
						viewHolder.tvItem2NameOption.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem2PriceOption.setText("$" + itemDetail.getValue());
						viewHolder.tvItem2PriceOption.setTag(itemDetail);
						viewHolder.tvItem2NameOption.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem2PriceOption.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);

						viewHolder.lnItem2Option.setTag(itemDetail);
						viewHolder.lnItem2Option.setOnClickListener(this);
						
					} else if (i == 2) {
						setViewVisibility(viewHolder.tvItem3NameOption, true);
						setViewVisibility(viewHolder.tvItem3PriceOption, true);
						viewHolder.tvItem3NameOption.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem3PriceOption.setText("$" + itemDetail.getValue());
						viewHolder.tvItem3PriceOption.setTag(itemDetail);
						viewHolder.tvItem3NameOption.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem3PriceOption.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						
						viewHolder.lnItem3Option.setOnClickListener(this);
						viewHolder.lnItem3Option.setTag(itemDetail);
					}

				}
				setViewVisibility(view.findViewById(R.id.divery_1), true);
				isHaveMenu = true;
				setViewVisibility(view.findViewById(R.id.ln_option), true);
			} else {
				setViewVisibility(view.findViewById(R.id.divery_1), false);
				setViewVisibility(view.findViewById(R.id.ln_option), false);
			}

			if (item.getSizeItemList() != null && item.getSizeItemList().size() > 0) {
				List<SizeItem> list = item.getSizeItemList();

				for (int i = 0; i < list.size(); i++) {
					SizeItem itemDetail = list.get(i);
					if (i == 0) {
						setViewVisibility(viewHolder.tvItem1NameSize, true);
						setViewVisibility(viewHolder.tvItem1PriceSize, true);
						viewHolder.tvItem1NameSize.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem1PriceSize.setText("$" + itemDetail.getValue());
						viewHolder.tvItem1PriceSize.setTag(itemDetail);
						viewHolder.tvItem1NameSize.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem1PriceSize.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						
						viewHolder.lnItem1Size.setTag(itemDetail);
						viewHolder.lnItem1Size.setOnClickListener(this);
						
					} else if (i == 1) {
						setViewVisibility(viewHolder.tvItem2NameSize, true);
						setViewVisibility(viewHolder.tvItem1PriceSize, true);
						viewHolder.tvItem2NameSize.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem2PriceSize.setText("$" + itemDetail.getValue());
						viewHolder.tvItem2PriceSize.setTag(itemDetail);
						viewHolder.tvItem2NameSize.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem2PriceSize.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						
						viewHolder.lnItem2Size.setTag(itemDetail);
						viewHolder.lnItem2Size.setOnClickListener(this);
						
					} else if (i == 2) {
						setViewVisibility(viewHolder.tvItem3NameSize, true);
						setViewVisibility(viewHolder.tvItem3PriceSize, true);
						viewHolder.tvItem3NameSize.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem3PriceSize.setText("$" + itemDetail.getValue());
						viewHolder.tvItem3PriceSize.setTag(itemDetail);
						viewHolder.tvItem3NameSize.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem3PriceSize.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);

						viewHolder.lnItem3Size.setTag(itemDetail);
						viewHolder.lnItem3Size.setOnClickListener(this);
						
					}
				}
				isHaveMenu = true;
				setViewVisibility(view.findViewById(R.id.ln_size), true);
			} else {
				setViewVisibility(view.findViewById(R.id.ln_size), false);
			}

			/**
			 * Bind extralist item.
			 */
			if (item.getExtraItemList() != null && item.getExtraItemList().size() > 0) {
				// setViewVisibility(lnExtra, true);
				List<ExtraItem> list = item.getExtraItemList();

				for (int i = 0; i < list.size(); i++) {
					ExtraItem itemDetail = list.get(i);
					if (i == 0) {
						setViewVisibility(viewHolder.tvItem1NameExtra, true);
						setViewVisibility(viewHolder.tvItem1PriceExtra, true);
						viewHolder.tvItem1NameExtra.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem1PriceExtra.setText("$" + itemDetail.getValue());
						viewHolder.tvItem1PriceExtra.setTag(itemDetail);
						viewHolder.tvItem1NameExtra.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem1PriceExtra.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);

						viewHolder.lnItem1Extra.setTag(itemDetail);
						viewHolder.lnItem1Extra.setOnClickListener(this);
						
					} else if (i == 1) {
						setViewVisibility(viewHolder.tvItem2NameExtra, true);
						setViewVisibility(viewHolder.tvItem2PriceExtra, true);
						viewHolder.tvItem2NameExtra.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem2PriceExtra.setText("$" + itemDetail.getValue());
						viewHolder.tvItem2PriceExtra.setTag(itemDetail);
						viewHolder.tvItem2NameExtra.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem2PriceExtra.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						
						viewHolder.lnItem2Extra.setTag(itemDetail);
						viewHolder.lnItem2Extra.setOnClickListener(this);

					} else if (i == 2) {
						setViewVisibility(viewHolder.tvItem3NameExtra, true);
						setViewVisibility(viewHolder.tvItem3PriceExtra, true);
						viewHolder.tvItem3NameExtra.setText(itemDetail.getName());
						if(floatValue(itemDetail.getValue()) !=0)
							viewHolder.tvItem3PriceExtra.setText("$" + itemDetail.getValue());
						viewHolder.tvItem3PriceExtra.setTag(itemDetail);
						viewHolder.tvItem3NameExtra.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);
						viewHolder.tvItem3PriceExtra.setTextColor( itemDetail.isSelected()?mFocusColor:mNormalColor);

						viewHolder.lnItem3Extra.setTag(itemDetail);
						viewHolder.lnItem3Extra.setOnClickListener(this);
						
					}
				}
				isHaveMenu = true;
				setViewVisibility(view.findViewById(R.id.ln_extra), true);
				setViewVisibility(view.findViewById(R.id.divery_2), true);
			} else {
				setViewVisibility(view.findViewById(R.id.ln_extra), false);
				setViewVisibility(view.findViewById(R.id.divery_2), false);
			}
			
			setViewVisibility(view.findViewById(R.id.ln_done_select_extra), isHaveMenu);
			setViewVisibility(view.findViewById(R.id.rl_extra), isHaveMenu);
			
			WhyqApplication.Instance().getImageLoader().DisplayImage(item.getImageThumb(), viewHolder.imgThumb);
			
//			if(ListDetailActivity.billList!=null && ListDetailActivity.billList.containsKey(item.getId())){
//				viewHolder.tvCount.setText(""+ListDetailActivity.billList.get(item.getId()).size());
////				viewHolder.tvPrice.setText(""+getTotolPrice(ListDetailActivity.billList.get(item.getId())));
//			}
			viewHolder.tvType.setText(Html.fromHtml(item.getNameProduct()));
		
			viewHolder.tvPrice.setText("$" + Html.fromHtml(""+Util.round(Float.parseFloat(item.getValue()), 1)));
			viewHolder.tvCount.setText(""+item.getUnitForBill());
			viewHolder.storeId = item.getStoreId();
			viewHolder.menuId = item.getId();
			viewHolder.imgFavourite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					
					Service service = new Service(new IServiceListener() {
						
						@Override
						public void onCompleted(Service service, ServiceResponse result) {
							// TODO Auto-generated method stub
							try {
								Log.d("favorite onCompleted", "isFavorite "+item.isFavorite());
								ResponseData data = (ResponseData) result.getData();

								if (data.getStatus().equals("200")) {
									item.setFavorite(!item.isFavorite());
									viewHolder.imgFavourite.setImageResource(item.isFavorite()? R.drawable.icon_fav_enable: R.drawable.icon_fav_disable);
									int newNumberFavCount = item.isFavorite()?Integer.parseInt(viewHolder.tvFavouriteCount.getText().toString())+1: Integer.parseInt(viewHolder.tvFavouriteCount.getText().toString())-1; 
									viewHolder.tvFavouriteCount.setText(newNumberFavCount > 0? ""+newNumberFavCount: "0");
								} else if (data.getStatus().equals("401")) {
									Util.loginAgain(mContext, data.getMessage());
								} else {
									Util.showDialog(mContext, data.getMessage());
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
								
						}
					});
					service.postLikeFavouriteFoods(item.getId());	
				}
			});
			
			viewList.put(item.getId(), view);
			viewHolder.btnRemove.setTag(item);
			viewHolder.btnAdd.setTag(item);
			viewHolder.btnDoneSelect.setTag(item);
			view.setTag(viewHolder);
			

			
			if(isItemInBillList){
				/***
				 * Display totoal price for option choosen
				 */
				float price = ((ListDetailActivity) mContext).getTotalSize(item.getSizeItemList())
						+ ((ListDetailActivity) mContext).getTotalOption(item.getOptionItemList())
						+ ((ListDetailActivity) mContext).getTotalExtra(item.getExtraItemList());
				if(((ListDetailActivity) mContext).getTotalSize(item.getSizeItemList()) <= 0){
					price += Float.parseFloat(item.getValue());
				}
				viewHolder.tvPrice.setText("$" + price);
			}
			
			/***
			 * Bind menu preview to view
			 */
			ViewGroup viewGroup = (ViewGroup) viewHolder.lnPreview.getParent();
			final List<Bill> billList = ListDetailActivity.billList.get(item.getId());
			if(item.getUnitForBill() > 0 && billList != null){//billList!=null && billList.size()
				
				float sizeValue = 0;
				float optinValue = 0;
				float extraValue = 0;
				for(final Bill bill: billList){
//					final View preview = LayoutInflater.from(mContext).inflate(R.layout.item_extra_preview, viewGroup,false);
//					ImageButton btnDeleteMenu = (ImageButton)preview.findViewById(R.id.imgbtn_delete_item);
//					viewHolder.lnPreview.addView(preview);
					if((bill.getSizeList() !=null && bill.getSizeList().size() > 0) ||(bill.getOptionList() !=null && bill.getOptionList().size() > 0
							||(bill.getExtraList() !=null && bill.getExtraList().size() > 0))){
						final View preview = LayoutInflater.from(mContext).inflate(R.layout.item_extra_preview, viewGroup,false);
						ImageButton btnDeleteMenu = (ImageButton)preview.findViewById(R.id.imgbtn_delete_item);
						viewHolder.lnPreview.addView(preview);
						
						if(bill.getSizeList() !=null && bill.getSizeList().size() > 0){
							viewHolder.lnPreview.setVisibility(View.VISIBLE);
//							SizeItem sizeItem = bill.getSizeList().get(bill.getSizeList().size() - 1);
//							((TextView) preview.findViewById(R.id.tv_size)).setText("Size: $"+sizeItem.getValue());
//							sizeValue += Float.parseFloat(sizeItem.getValue());
							float value = getTotolSizePrice(bill.getSizeList());
							((TextView) preview.findViewById(R.id.tv_size)).setText("Size: $"+value);
							sizeValue += value;
							
						}

						if(bill.getOptionList()!=null && bill.getOptionList().size() > 0){
							viewHolder.lnPreview.setVisibility(View.VISIBLE);
//							OptionItem optionItem = bill.getOptionList().get(bill.getOptionList().size() - 1);
//							((TextView) preview.findViewById(R.id.tv_option)).setText("Option: $"+optionItem.getValue());
//							sizeValue += Float.parseFloat(optionItem.getValue());
							float value = getTotalOptionPrice(bill.getOptionList());
							((TextView) preview.findViewById(R.id.tv_option)).setText("Option: $"+value);
							sizeValue += value;
						}
						if(bill.getExtraList()!=null && bill.getExtraList().size() > 0){
							viewHolder.lnPreview.setVisibility(View.VISIBLE);
//							ExtraItem extraItem = bill.getExtraList().get(bill.getExtraList().size() - 1);
//							((TextView) preview.findViewById(R.id.tv_extra)).setText("Extra: $"+extraItem.getValue());
//							sizeValue += Float.parseFloat(extraItem.getValue());
							float value = getTotalExtraPrice(bill.getExtraList());
							((TextView) preview.findViewById(R.id.tv_extra)).setText("Extra: $"+value);
							sizeValue += value;

						}
						
						btnDeleteMenu.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								viewHolder.lnPreview.removeView(preview);
								deleteBill(bill);
							}

							private void deleteBill(Bill bill) {
								// TODO Auto-generated method stub
								deleteBill(item, bill, billList);
							}

							private void deleteBill(Menu item, Bill bill, List<Bill> billList) {
								// TODO Auto-generated method stub
								List<Bill> result = billList;
								for(int i=0; i < billList.size(); i++){
									Bill bill2 =  billList.get(i);
									if(bill2== bill){
										result.remove(bill2);
									}
								}
								ListDetailActivity.billList.remove(item.getId());
								ListDetailActivity.billList.put(item.getId(), result);
								notifyDataSetChanged();
								((ListDetailActivity)mContext).updateTotal();
							}
						});
					}
		
				}
			}
			
		}
		
		return view;
	}

	private float floatValue(String value) {
		// TODO Auto-generated method stub
		float result =0;
		if(value !=null && !value.equals("")){
			result = Float.parseFloat(""+Util.round(Float.parseFloat(value), 2));
		}
		return result;
	}

	private int getTotolPrice(List<Bill> list) {
		// TODO Auto-generated method stub
		int result = 0;
		for(Bill item: list){
			try {
				result+= Integer.parseInt(item.getPrice()) + getTotolSizePrice(item.getSizeList()) + getTotalOptionPrice(item.getOptionList())+ getTotalExtraPrice(item.getExtraList());	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return result;
	}

	private float getTotalOptionPrice(List<OptionItem> optionList) {
		// TODO Auto-generated method stub
		float result = 0;
		for(OptionItem item: optionList){
			try {
				result+=Float.parseFloat(item.getValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return result;
	}

	private float getTotolSizePrice(List<SizeItem> sizeList) {
		// TODO Auto-generated method stub
		float result = 0;
		for(SizeItem item: sizeList){
			try {
				result+=Float.parseFloat(item.getValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return result;
	}

	private float getTotalExtraPrice(List<ExtraItem> extraList) {
		// TODO Auto-generated method stub
		float result = 0;
		for(ExtraItem item: extraList){
			try {
				result+=Float.parseFloat(item.getValue());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return result;
	}

	private void setViewVisibility(View v, boolean b) {
		// TODO Auto-generated method stub
		v.setVisibility(b ? View.VISIBLE : View.GONE);
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
			arg2 = LayoutInflater.from(mContext).inflate(R.layout.list_group, null);
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

		groupHolder.title.setTypeface(Util.sTypefaceRegular);
		groupHolder.title.setText(Html.fromHtml(mGroupCollection.get(arg0).getName()));
		return arg2;

	}

	class GroupHolder {
		ImageView img;
		TextView title;
	}

	class ChildHolder {
		TextView storyName;
		TextView storyIntro;
		WebImageView thumbnal;
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		Log.d("onClick", "onClick");
		int id = v.getId();
		switch (id) {
		case R.id.ln_item1_option:
			onOptionClicked(1, v);
			break;
		case R.id.ln_item2_option:
			onOptionClicked(2, v);
			break;
		case R.id.ln_item3_option:
			onOptionClicked(3, v);
			break;
		case R.id.ln_item1_size:
			onSizeClicked(1, v);
			break;
		case R.id.ln_item2_size:
			onSizeClicked(2, v);
			break;
		case R.id.ln_item3_size:
			onSizeClicked(3, v);
			break;
		case R.id.ln_item1_extra:
			onExtraClicked(1, v);
			break;
		case R.id.ln_item2_extra:
			onExtraClicked(2, v);
			break;
		case R.id.ln_item3_extra:
			onExtraClicked(3, v);
			break;
		case R.id.btnAdd:
			onAddClicked(v);
			break;
		case R.id.btnRemove:
			onRemoveClicked(v);
			break;
		case R.id.btn_done_select_extra:
			onDoneSelectedClicked(v);
		case R.id.btn_delete_item:
			onRemoveMenuSelectClicked(v);
			break;
		default:
			break;
		}
	}


	private void onAddClicked(View v) {
		// TODO Auto-generated method stub
//		((ListDetailActivity)mContext).onAddClicked(v);

		
		Menu item = (Menu) v.getTag();
		Log.d("onAddClicked", "id =" + v.getId()+ " unit "+item.getUnitForBill());
		if (item != null) {
			item.setUnitForBill(item.getUnitForBill()+1);
			changeItem(item);
			if(item.getExtraItemList().size() > 0 || item.getOptionItemList().size() >0 || item.getSizeItemList().size() > 0){

			}else{
				exeAddItemToList(item, true);
			}
//			Bill bill = new Bill();
//			bill.setId(item.getId());
//			bill.setPrice(item.getValue());
//			if (ListDetailActivity.promotion != null) {
//				bill.setDiscount(ListDetailActivity.promotion.getValuePromotion() != null ? ListDetailActivity.promotion
//						.getValuePromotion() : "" + 0);
//			}
//			bill.setUnit("1");
//			bill.setProductId(item.getId());
//			bill.setProductName(item.getNameProduct());
//			if (ListDetailActivity.billList.get(item.getId()) != null) {
//
//				ListDetailActivity.billList.get(item.getId()).add(bill);
//			} else {
//				List<Bill> list = new ArrayList<Bill>();
//				list.add(bill);
//				ListDetailActivity.billList.put(item.getId(), list);
//			}
//
			notifyDataSetChanged();
		}
		
	
	}

	private void onRemoveClicked(View v) {
		// TODO Auto-generated method stub
		// ((ListDetailActivity)mContext).onRemoveClicked(v);

		Log.d("onRemoveClicked", "id =" + v.getId());
		Menu item = (Menu) v.getTag();
		if (item != null) {
			if(item.getUnitForBill() > 0){
				item.setUnitForBill(item.getUnitForBill()-1);	
			}else{
				item.setUnitForBill(0);
			}
			if(item.getExtraItemList().size() > 0 || item.getOptionItemList().size() >0 || item.getSizeItemList().size() > 0){
				
			}else{
				changeItem(item);
				if(item.getExtraItemList().size() > 0 || item.getOptionItemList().size() >0 || item.getSizeItemList().size() > 0){

				}else{
					exeAddItemToList(item, true);
				}
			}
//			if (ListDetailActivity.billList.containsKey(item.getId())) {
//				List<Bill> list = ListDetailActivity.billList.get(item.getId());
//				if(list !=null && list.size() > 0){
//					list.remove(list.size() - 1);
//				}
//				if(list.size()==0){
//					ListDetailActivity.billList.remove(item.getId());
//				}
//
//			} else {
//				ListDetailActivity.billList.remove(item.getId());	
//			}
			((ListDetailActivity)mContext).updateTotal();
			notifyDataSetChanged();
		}

	}

//	public void exeRemoveBill(Menu item){
//		List<Bill> billList = ListDetailActivity.billList.get(item.getId());
//		try {
//			List<Bill> result = billList ;
//			for(int i=0; i < billList.size(); i++){
//				Bill bill2 =  billList.get(i);
//				if(bill2== bill){
//					result.remove(bill2);
//				}
//			}
//			ListDetailActivity.billList.remove(item.getId());
//			ListDetailActivity.billList.put(item.getId(), result);
//			notifyDataSetChanged();
//			((ListDetailActivity)mContext).updateTotal();
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//	}
	
	private void onDoneSelectedClicked(View v) {
		// TODO Auto-generated method stub

		Menu menu = (Menu)v.getTag();
		exeAddItemToList(menu, false);
	}

	private void exeAddItemToList(Menu menu, boolean isUpdate) {
		// TODO Auto-generated method stub
		List<SizeItem> sizelist = menu.getSizeItemList();
		List<OptionItem> optionList = menu.getOptionItemList();
		List<ExtraItem> extraList = menu.getExtraItemList();
		
		List<SizeItem> sizelistBill = new ArrayList<SizeItem>();
		List<OptionItem> optionListBill = new ArrayList<OptionItem>();
		List<ExtraItem> extraListBill = new ArrayList<ExtraItem>();

		List<Bill> billList = ListDetailActivity.billList.get(menu.getId());
		if(billList ==null){
			billList = new ArrayList<Bill>();
		}else{
			if(isUpdate){
				billList.clear();
			}
		}
		
		Bill bill = new Bill();
		bill.setId(menu.getId());
		bill.setProductName(menu.getNameProduct());
		bill.setProductId(menu.getId());
		float price = ((ListDetailActivity) mContext).getTotalSize(menu.getSizeItemList())
				+ ((ListDetailActivity) mContext).getTotalOption(menu.getOptionItemList())
				+ ((ListDetailActivity) mContext).getTotalExtra(menu.getExtraItemList());
		if(price <= 0){
			price = Float.parseFloat(menu.getValue());
		}
		bill.setPrice(""+price);
		bill.setUnit(""+menu.getUnitForBill());
		bill.setNote(noteList.get(menu.getId()));
		
		if(menu.getUnitForBill() > 0){
			for(SizeItem item: sizelist){
				if(item.isSelected()){
					sizelistBill.add(item);
				}
			}
			for(OptionItem item: optionList){
				if(item.isSelected()){
					optionListBill.add(item);
				}
			}
			for(ExtraItem item: extraList){
				if(item.isSelected()){
					extraListBill.add(item);
				}
			}
			bill.setSizeList(sizelistBill);
			bill.setOptionList(optionListBill);
			bill.setExtraList(extraListBill);
			if(ListDetailActivity.promotion!=null){
				bill.setDiscount(ListDetailActivity.promotion.getValuePromotion()!=null?ListDetailActivity.promotion.getValuePromotion():""+0);
			}
			
			billList.add(bill);
			
			ListDetailActivity.billList.put(menu.getId(), billList);
		}
		
		notifyDataSetChanged();
		((ListDetailActivity)mContext).updateTotal();

	}

	private void onRemoveMenuSelectClicked(View v) {
		// TODO Auto-generated method stub
		
	}

	private void onExtraClicked(int i, View v0) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onExtraClicked" + i);
		ExtraItem item = (ExtraItem)v0.getTag();
		item.setSelected(!item.isSelected());
		updateExtraItem(item);

		notifyDataSetChanged();
	}

	private void onSizeClicked(int i, View v0) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onSizeClicked" + i);
		SizeItem item = (SizeItem)v0.getTag();
		if(!item.isSelected()){
			Menu menu = getMenuItemById(item.getProductId());
			if(menu!=null){
				List<SizeItem> list = menu.getSizeItemList();
				if(list!=null && list.size() > 0){
					boolean isSelected = item.isSelected();
					for(int i2=0; i2 < list.size(); i2++){
						SizeItem sItem = list.get(i2);
						Log.d("onSizeClicked", "id" + sItem.getId() + " and item id "+item.getId()+"is selected "+isSelected);
						if(item.getId().equals(sItem.getId())){
							sItem.setSelected(!isSelected);				
						}else{
							sItem.setSelected(isSelected);
						}
					}
				}
			}
			
			updateSizeItem(item);
			notifyDataSetChanged();
		}
	}

	private void onOptionClicked(int i, View v0) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onOptionClicked" + i);
		OptionItem item = (OptionItem) v0.getTag();
		item.setSelected(!item.isSelected());
		updateOptionItem(item);

		notifyDataSetChanged();
	}
	
	private void changeItem(Menu item) {
		// TODO Auto-generated method stub
		List<GroupMenu> data = getData();
		for (GroupMenu groupItem : data) {
			List<Menu> menuList = groupItem.getMenuList();
			if(menuList !=null && menuList.contains(item)){
				for(int i=0;i < menuList.size(); i++){
					Menu menu = menuList.get(i);
					if(menu.getId().equals(""+item.getId())){
						int position = menuList.indexOf(menu);
						menuList.remove(menu);
						menuList.add(position, item);
					}
				}
			}
		}
	}
	
	private void updateOptionItem(OptionItem optItem) {
		// TODO Auto-generated method stub
		if(optItem.getProductId() !=null){
			Menu item = getMenuItemById(optItem.getProductId());
			List<OptionItem> list  = item.getOptionItemList();
			if(list ==null){
				list = new ArrayList<OptionItem>();
			}
			if(true)//optItem.isSelected()
			{
				list = removeItem(list, optItem);
//				list.add(optItem);
				item.setOptionItemList(list);				
			}else{
				if(list.contains(optItem)){
					
					list.remove(optItem);
					item.setOptionItemList(list);
				}
				
			}
			changeItem(item);
		}
	}


	private void updateSizeItem(SizeItem sizeItem) {
		// TODO Auto-generated method stub
		if(sizeItem.getProductId() !=null){
			Menu item = getMenuItemById(sizeItem.getProductId());
			List<SizeItem> list  = item.getSizeItemList();
			if(list ==null){
				list = new ArrayList<SizeItem>();
			}
			if(true){//sizeItem.isSelected()
				list = removeItem(list, sizeItem);
//				list.add(sizeItem);
				item.setSizeItemList(list);				
			}else{
				if(list.contains(sizeItem)){
					list.remove(sizeItem);
					item.setSizeItemList(list);
				}
				
			}
			changeItem(item);
		}
	}
	private void updateExtraItem(ExtraItem extraItem) {
		// TODO Auto-generated method stub
		if(extraItem.getProductId() !=null){
			Menu item = getMenuItemById(extraItem.getProductId());
			List<ExtraItem> list  = item.getExtraItemList();
			if(list ==null){
				list = new ArrayList<ExtraItem>();
			}
			if(true){//extraItem.isSelected()
				list = removeItemExtra(list, extraItem);
//				list.add(extraItem);
				item.setExtraItemList(list);				
			}else{
				if(list.contains(extraItem)){
					list.remove(extraItem);
					item.setExtraItemList(list);
				}
				
			}
			changeItem(item);
		}
	}
	private List<ExtraItem> removeItemExtra(List<ExtraItem> list, ExtraItem extraItem) {
		// TODO Auto-generated method stub
		List<ExtraItem> result = list;
		if(list.size() > 0){
			for(int i=0;i< list.size();i++){
				ExtraItem item = list.get(i);
				if(item!=null&& item.getId().equals(extraItem.getId())){
					int position = list.indexOf(item);
					result.remove(item);
					result.add(position, extraItem);
				}
			}
		}
		return result;
	}
	private List<OptionItem> removeItem(List<OptionItem> list, OptionItem extraItem) {
		// TODO Auto-generated method stub
		List<OptionItem> result = list;
		if(list.size() > 0){
			for(int i=0;i< list.size();i++){
				OptionItem item = list.get(i);
				if(item.getId().equals(extraItem.getId())){
					int position = list.indexOf(item);
					result.remove(item);
					result.add(position, extraItem);
				}
			}
		}
		return result;
	}
	private List<SizeItem> removeItem(List<SizeItem> list, SizeItem extraItem) {
		// TODO Auto-generated method stub
		List<SizeItem> result = list;
		if(list.size() > 0){
			for(int i=0;i< list.size();i++){
				SizeItem item = list.get(i);
				if(item.getId().equals(extraItem.getId())){
					int position = list.indexOf(item);
					result.remove(item);
					result.add(position, extraItem);
				}
			}
		}
		return result;
	}

	private Menu getMenuItemById(String menuId) {
		// TODO Auto-generated method stub
		List<GroupMenu> data = getData();
		for (GroupMenu groupItem : data) {
			List<Menu> menuList = groupItem.getMenuList();
			for (Menu menu : menuList) {
				if (menu.getId() != null && menuId.equalsIgnoreCase(menu.getId())) {
					return menu;
				}
			}
		}
		return null;
	}

	


	public List<GroupMenu> getData() {
		return mGroupCollection;
	}

	public static class ViewHolderMitemInfo {
		public TextView tvType, tvPrice, tvCount, tvFavouriteCount;
		public ImageView imgThumb, imgFavourite;
		public String storeId;
		public Button btnAdd;
		public Button btnRemove;
		public String menuId;
		public LinearLayout lnPreview;
		public RelativeLayout rlExtraView;
		public EditText etNote;
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
}
