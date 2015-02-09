package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.interfaces.IServiceListener;
import whyq.interfaces.OnnOptionItemSelected;
import whyq.model.Bill;
import whyq.model.ExtraItem;
import whyq.model.GroupMenu;
import whyq.model.Menu;
import whyq.model.OptionItem;
import whyq.model.ResponseData;
import whyq.model.SizeItem;
import whyq.service.Service;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

import com.custom.WebImageView;
import com.dealadelivery.whyq.R;
import com.meetme.android.horizontallistview.HorizontalListView;


public class ExpandMenuAdapterV2 extends BaseExpandableListAdapter implements OnClickListener, OnnOptionItemSelected , OnItemClickListener{

	
	public enum listview{
		SIZES, OPTIONS, EXTRAS
	}
	
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
	
	public ExpandMenuAdapterV2(Context pContext, ExpandableListView pExpandableListView,
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
//		Log.d("getChildrenCount", "size: " + mGroupCollection.get(arg0).getMenuList().size());
		return mGroupCollection.get(arg0).getMenuList().size();
	}

	Map<String, View> viewList = new HashMap<String, View>();
	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View view, ViewGroup arg4) {
		// TODO Auto-generated method stub
	
		final Menu item = mGroupCollection.get(arg0).getMenuList().get(arg1);
		Log.d("getChildView", "getChildView id "+ view);
		{
			LayoutInflater inflator = ((ListDetailActivity) mContext).getLayoutInflater();
			final ViewHolderMitemInfo viewHolder;
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
				
				viewHolder.etNote = (EditText) view.findViewById(R.id.et_note);
				
				viewHolder.hlv_sizes = (HorizontalListView)view.findViewById(R.id.hlv_sizes);
				viewHolder.hlv_options = (HorizontalListView)view.findViewById(R.id.hlv_options);
				viewHolder.hlv_extras = (HorizontalListView)view.findViewById(R.id.hlv_extras);

				viewHolder.hlv_sizes.setTag(listview.SIZES);
				viewHolder.hlv_options.setTag(listview.OPTIONS);
				viewHolder.hlv_extras.setTag(listview.EXTRAS);
				
				
				viewHolder.hlv_sizes.setOnItemClickListener(this);
				viewHolder.hlv_options.setOnItemClickListener(this);
				viewHolder.hlv_extras.setOnItemClickListener(this);
				
				viewHolder.btnAdd.setOnClickListener(this);
				viewHolder.btnRemove.setOnClickListener(this);
				viewHolder.btnDoneSelect.setOnClickListener(this);
				
				
			} else {
				viewHolder = (ViewHolderMitemInfo) view.getTag();
			}

			
			viewHolder.tvFavouriteCount.setText(""+item.getCountFavorite());
			viewHolder.imgFavourite.setImageResource(item.isFavorite()? R.drawable.icon_fav_enable: R.drawable.icon_fav_disable);
			boolean isItemInBillList = item.getUnitForBill() > 0;

			setViewVisibility(viewHolder.rlExtraView, isItemInBillList);	
			
			
			setViewVisibility(viewHolder.hlv_sizes, isItemInBillList);
			setViewVisibility(viewHolder.hlv_options, isItemInBillList);
			setViewVisibility(viewHolder.hlv_extras, isItemInBillList);

			
			/*
			 * Set text note change
			 */
//			setViewVisibility(viewHolder.etNote, isItemInBillList);
//			viewHolder.etNote = (EditText) view.findViewById(R.id.et_note);
			viewHolder.etNote.setText(item.getNote());
			setViewVisibility(view.findViewById(R.id.rl_note), isItemInBillList);
			viewHolder.etNote.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					Log.d("onTextChanged","onTextChanged "+s);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					Log.d("beforeTextChanged","beforeTextChanged "+s);
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					Log.d("afterTextChanged","afterTextChanged "+s);
					item.setNote(s.toString());
					changeItem(item);
					noteList.put(item.getId(), s.toString());
				}
			});
			


			/***
			 * Bind option data to item
			 */
			boolean isHaveMenu = false;
			
			if (item.getOptionItemList() != null && item.getOptionItemList().size() > 0) {
				List<OptionItem> list = item.getOptionItemList();
				
				OptionItemAdapter optionAdapter = (OptionItemAdapter)viewHolder.hlv_options.getAdapter();
				if (optionAdapter == null) {
					optionAdapter = new OptionItemAdapter(mContext, R.layout.option_item, list);	
					optionAdapter.setDelegate(this);
					viewHolder.hlv_options.setAdapter(optionAdapter);
				}else{
					optionAdapter.updateData(list);
					optionAdapter.notifyDataSetChanged();
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

				SizeItemAdapter sizeAdapter = (SizeItemAdapter)viewHolder.hlv_sizes.getAdapter();
				if (sizeAdapter == null) {
					sizeAdapter = new SizeItemAdapter(mContext, R.layout.option_item, list);	
					sizeAdapter.setDelegate(this);
					viewHolder.hlv_sizes.setAdapter(sizeAdapter);
				}else{
					sizeAdapter.updateData(list);
					sizeAdapter.notifyDataSetChanged();
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

				ExtraItemAdapter extrasAdapter = (ExtraItemAdapter) viewHolder.hlv_extras.getAdapter();
				if (extrasAdapter == null) {
					extrasAdapter =  new ExtraItemAdapter(mContext, R.layout.option_item, list);
					extrasAdapter.setDelegate(this);	
					viewHolder.hlv_extras.setAdapter(extrasAdapter);
				}else{
					extrasAdapter.updateData(list);
					extrasAdapter.notifyDataSetChanged();
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
			
			viewHolder.tvType.setText(Html.fromHtml(item.getNameProduct()));
		
			viewHolder.tvPrice.setText("$" + Html.fromHtml(""+Util.round(Float.parseFloat(item.getValue()), 2)));
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
			viewHolder.lnPreview.removeAllViewsInLayout();
			
			final List<Bill> billList = ListDetailActivity.billList.get(item.getId());
			if(item.getUnitForBill() > 0 && billList != null){//billList!=null && billList.size()
				
				float sizeValue = 0;
				float optinValue = 0;
				float extraValue = 0;
				for(final Bill bill: billList){
					if((bill.getSizeList() !=null && bill.getSizeList().size() > 0) ||(bill.getOptionList() !=null && bill.getOptionList().size() > 0
							||(bill.getExtraList() !=null && bill.getExtraList().size() > 0))){
						final View preview = LayoutInflater.from(mContext).inflate(R.layout.item_extra_preview, viewGroup,false);
						ImageButton btnDeleteMenu = (ImageButton)preview.findViewById(R.id.imgbtn_delete_item);
						viewHolder.lnPreview.addView(preview);
						
						if(bill.getSizeList() !=null && bill.getSizeList().size() > 0){
							viewHolder.lnPreview.setVisibility(View.VISIBLE);
							float value = getTotolSizePrice(bill.getSizeList());
							((TextView) preview.findViewById(R.id.tv_size)).setText("Size: $"+value);
							sizeValue += value;
							
						}else{
							preview.findViewById(R.id.tv_size).setVisibility(View.GONE);	
						}

						if(bill.getOptionList()!=null && bill.getOptionList().size() > 0){
							viewHolder.lnPreview.setVisibility(View.VISIBLE);
							float value = getTotalOptionPrice(bill.getOptionList());
							((TextView) preview.findViewById(R.id.tv_option)).setText("Option: $"+value);
							sizeValue += value;
						}else{
							preview.findViewById(R.id.tv_option).setVisibility(View.GONE);
						}
						
						if(bill.getExtraList()!=null && bill.getExtraList().size() > 0){
							viewHolder.lnPreview.setVisibility(View.VISIBLE);
							float value = getTotalExtraPrice(bill.getExtraList());
							((TextView) preview.findViewById(R.id.tv_extra)).setText("Extra: $"+value);
							sizeValue += value;

						}else{
							preview.findViewById(R.id.tv_extra).setVisibility(View.GONE);
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
//		case R.id.ln_item1_option:
//			onOptionClicked(1, v);
//			break;
//		case R.id.ln_item2_option:
//			onOptionClicked(2, v);
//			break;
//		case R.id.ln_item3_option:
//			onOptionClicked(3, v);
//			break;
//		case R.id.ln_item1_size:
//			onSizeClicked(1, v);
//			break;
//		case R.id.ln_item2_size:
//			onSizeClicked(2, v);
//			break;
//		case R.id.ln_item3_size:
//			onSizeClicked(3, v);
//			break;
//		case R.id.ln_item1_extra:
//			onExtraClicked(1, v);
//			break;
//		case R.id.ln_item2_extra:
//			onExtraClicked(2, v);
//			break;
//		case R.id.ln_item3_extra:
//			onExtraClicked(3, v);
//			break;
		case R.id.btnAdd:
			onAddClicked(v);
			break;
		case R.id.btnRemove:
			onRemoveClicked(v);
			break;
		case R.id.btn_done_select_extra:
			onDoneSelectedClicked(v);
		case R.id.imgbtn_delete_item:
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
		notifyDataSetChanged();
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
//		if(price <= 0){
		if(((ListDetailActivity) mContext).getTotalSize(menu.getSizeItemList()) <= 0){
			price += Float.parseFloat(menu.getValue());
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

	public void onExtraClicked(int i, ExtraItem item) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onExtraClicked" + i);
//		ExtraItem item = (ExtraItem)v0.getTag();
		item.setSelected(!item.isSelected());
		updateExtraItem(item);

		notifyDataSetChanged();
	}

	public void onSizeClicked(int i, SizeItem item) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onSizeClicked" + i);
//		SizeItem item = (SizeItem)v0.getTag();
		if(!item.isSelected())
		{
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
			
//			updateSizeItem(item);
			notifyDataSetChanged();
		}
	}

	public void onOptionClicked(int i, OptionItem item) {
		// TODO Auto-generated method stub
		Log.d("onClick", "onOptionClicked" + i);
//		OptionItem item = (OptionItem) v0.getTag();
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
		
	    private HorizontalListView hlv_sizes;
	    private HorizontalListView hlv_options;
	    private HorizontalListView hlv_extras;
		
		

		public ViewHolderMitemInfo() {

		}
	}

	@Override
	public void onSelected(int type, Object data) {
		// TODO Auto-generated method stub
		if(type == 0){
			SizeItem item = (SizeItem)data;
			onSizeClicked(0, item);
		}else if(type == 1){
			OptionItem item = (OptionItem)data;
			onOptionClicked(1, item);
		}else if(type == 2){
			ExtraItem item = (ExtraItem)data;
			onExtraClicked(0, item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		OptionItemBasicAdapter.OptionItemHolder holder = (OptionItemBasicAdapter.OptionItemHolder) view.getTag();
		OptionItem item = holder.data;
		
		// Keep the current first position
		int firstPosition = parent.getLastVisiblePosition();
		
		ExpandMenuAdapterV2.listview tag = (ExpandMenuAdapterV2.listview)parent.getTag();
		if (tag == listview.SIZES) {
			onSelected(0, SizeItemAdapter.convertOptionItemToSizeItem(item));
			
		}else if(tag == listview.OPTIONS){
			onSelected(1, item);
			
		}else if(tag == listview.EXTRAS){
			onSelected(2, ExtraItemAdapter.convertOptionItemToExtraItem(item));
			
		}
		Log.d("onItemClick", "setSelection "+firstPosition);
		// Reset first position
		parent.setSelection(firstPosition);
		
	}
}
