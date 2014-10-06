package whyq.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.activity.FavouriteActivity;
import whyq.activity.ListActivity;
import whyq.activity.ListDetailActivity;
import whyq.activity.ListVipStoreActivity;
import whyq.controller.WhyqListController;
import whyq.model.Comment;
import whyq.model.Promotion;
import whyq.model.Store;
import whyq.model.User;
import whyq.model.UserCheckBill;
import whyq.service.img.good.ImageLoader;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.HttpPermUtils;
import whyq.utils.Util;
import whyq.utils.WhyqUtils;
import whyq.utils.facebook.FacebookConnector;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dealadelivery.whyq.R;

public class WhyqAdapter extends ArrayAdapter<Store> implements OnClickListener {

	private ArrayList<Store> items;
	public static final String TAG = "PermAdapter";
//	public Button join;
//	public Button login;
//	public Button like;
//	public Button reperm;
//	public Button comment;
	public static class ViewHolder {
		public ImageView imgThumb , imgFriendThumb;
		public TextView tvItemName, tvNumberFavourite,tvItemAddress, tvVisited, tvDiscoutNumber;
		public Button btnDistance;
		public String id;
		public ImageView imgFavouriteThumb;
		public ProgressBar prgFavourite;
		public RelativeLayout rlDiscount;
	}
	private Activity activity;
	private Boolean header;
	private User user;
	public static String currentPermId;
	private FacebookConnector facebookConnector;

	private SharedPreferences prefs;

	private int screenWidth;
	private int screenHeight;
	private Context context;
//	private HashMap<String, View> viewList = new HashMap<String, View>();
	private HashMap<String, Store> newPermList = new HashMap<String, Store>();
	private HashMap<String, TextView> permStateList = new HashMap<String, TextView>();
	public int count = 11;
	
	private FragmentManager fragmentManager;
	private int nextItems = -1;
	
//	new
	
	final String likeString = this.getContext().getResources().getString(R.string.bt_like);
	final String unlikeString = this.getContext().getResources().getString(R.string.bt_unlike);
	final String repermString = this.getContext().getResources().getString(R.string.bt_reperm);
	final String commentString = this.getContext().getResources().getString(R.string.bt_comment);
	final String textCurrentLike = this.getContext().getResources().getString(R.string.delete);
	private Promotion promotion;
	private ImageLoader imageLoader;
//	private ArrayList<Whyq> listProducts;
/*	
	PermAdapter(ArrayList<Perm> perms) {
		super(PermAdapter.getContext(), new SpecialAdapter(perms), R.layout.pending);
	}
	*/
//	public WhyqAdapter(Context context, int textViewResourceId,
//			ArrayList<Store> items, Activity activity, int screenWidth,
//			int screenHeight, Boolean header) {
//		super(context, textViewResourceId, items);
//		this.context = context;
//		this.activity = activity;
//		this.header = header;
//		this.items = items;
//		this.screenWidth = screenWidth;
//		this.screenHeight = screenHeight;
//		facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID,
//				this.activity, context, new String[] { Constants.EMAIL,
//						Constants.PUBLISH_STREAM });
//		prefs = PreferenceManager.getDefaultSharedPreferences(context);
//		
//	}

	public WhyqAdapter(Context context,FragmentManager fragmentManager, int textViewResourceId,
			ArrayList<Store> items, Activity activity, int screenWidth,
			int screenHeight, Boolean header, User user) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.fragmentManager = fragmentManager;
		this.activity = activity;
		this.header = header;
		this.items = items;
		this.user = user;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		facebookConnector = new FacebookConnector(Constants.FACEBOOK_APP_ID,
				this.activity, context, new String[] { Constants.EMAIL,
						Constants.PUBLISH_STREAM });
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		imageLoader = WhyqApplication.Instance().getImageLoader();
	}

	private String getActivityGroupName() {
		// com.permping.activity.FollowerActivity

		String activityName = activity.getParent().getClass().getName()
				.replace("com.permping.activity.", "");
		return activityName;
	}

	@Override
	public View getView(final int position, View rowView, ViewGroup parent) {
		try {

			if(items != null && !items.isEmpty() && position < items.size()){
				
				if(position == items.size() - 1 && WhyqListController.isFooterAdded == true) {
					if(!WhyqListController.isLoading){
						WhyqListController.isLoading = true;
						loadMoreItems();
					}

					View footerView = createNullView();

					
					return footerView;
				}
				final Store store = items.get(position);
				final String viewId = store.getStoreId();
				if(viewId == null || viewId.length() == 0) {
					return createNullView();
				}
				currentPermId = viewId;
//				convertView = viewList.get(viewId);
				ViewHolder viewHolder = new ViewHolder();
				newPermList.put(viewId, store);
				Store item = items.get(position);
				if (rowView != null){
					viewHolder = (ViewHolder)rowView.getTag();
//					return convertView;
				}else{

					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.whyq_item_new, null);
					LayoutParams PARAMS = (LinearLayout.LayoutParams)rowView.getLayoutParams();
					
					viewHolder.id = item.getStoreId();
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
					viewHolder.imgFriendThumb = (ImageView)rowView.findViewById(R.id.imgFriendThumb);

					rowView.setTag(viewHolder);
//					viewList.put(store.getStoreId(), rowView);
					
				}
				
				final boolean isOpen = checkOpenTime(item);
				rowView.findViewById(R.id.v_grey_no_open).setVisibility(isOpen ? View.INVISIBLE: View.VISIBLE);
				((Button)rowView.findViewById(R.id.v_grey_no_open)).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Util.showDialog(context, "This store is open from "+store.getStartTime()+" to "+store.getEndTime());	
					}
				});
				
				viewHolder.tvItemName.setText(item.getNameStore().toUpperCase());
				viewHolder.tvItemAddress.setText(item.getAddress());
				viewHolder.tvNumberFavourite.setText(""+item.getCountFavaouriteMember());
				if(item.getPromotionList().size() > 0){
					viewHolder.tvDiscoutNumber.setVisibility(View.VISIBLE);
					promotion = item.getPromotionList().get(0);
					viewHolder.tvDiscoutNumber.setText(""+promotion.getValuePromotion()+promotion.getTypeValue()+ " for order over $"+promotion.getConditionPromotion());
				}else{
					viewHolder.rlDiscount.setVisibility(View.GONE);
					
				}
				
				if(!item.getDistance().equals(""))
					viewHolder.btnDistance.setText((int)Float.parseFloat(item.getDistance())+" km");
				
				
				UserCheckBill userCheckBill = item.getUserCheckBill();
				if(userCheckBill !=null){
					if(userCheckBill.getTotalMember()!=null && !userCheckBill.getTotalMember().equals("")){
						if(userCheckBill.getAvatar()!=null && !userCheckBill.getAvatar().equals("")){
							if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
								viewHolder.tvVisited.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" others visited");	
							}else{
								viewHolder.tvVisited.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" other visited");
							}
							
							viewHolder.imgFriendThumb.setVisibility(View.VISIBLE);
//							UrlImageViewHelper.setUrlDrawable(viewHolder.imgFriendThumb, userCheckBill.getAvatar());
							imageLoader.DisplayImage(userCheckBill.getAvatar(), viewHolder.imgFriendThumb);
						}else{
							if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
								viewHolder.tvVisited.setText(userCheckBill.getTotalMember()+" others visited");
							}else{
								viewHolder.tvVisited.setText(userCheckBill.getTotalMember()+" other visited");
							}
							
						}

					}
				}
				if(item.getIsFavourite()){
					viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_enable);
				}else{
					viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_disable);
				}
				viewHolder.imgFavouriteThumb.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(isOpen){
							if( FavouriteActivity.isFavorite)
								((FavouriteActivity)context).onFavouriteClicked(v);
							else
								((ListActivity)context).onFavouriteClicked(v);
						}
					}
				});
//				UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getLogo());
				imageLoader.DisplayImage(item.getLogo(), viewHolder.imgThumb);
				rowView.setTag(viewHolder);
				rowView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.d("rowView","rowView oid"+viewId);

						if(isOpen){
							if(viewId.equals(""+49)){
								//Vip restaurent
								Intent intent = new Intent(context, ListVipStoreActivity.class);
								intent.putExtra("vip", true);
								intent.putExtra("name", store.getNameStore());
								context.startActivity(intent);							
								
								
							}else{
								Intent intent = new Intent(context, ListDetailActivity.class);
								intent.putExtra("store_id", store.getStoreId());//store.getStoreId()
								intent.putExtra("id", viewId);//store.getStoreId()
								context.startActivity(intent);							
							}
						}else{
							Util.showDialog(context, "store open from"+store.getStartTime()+" to "+store.getEndTime());
						}
					}
				});
				if (store != null) {
					

				}
				viewHolder.imgFavouriteThumb.setTag(item);
				viewHolder.btnDistance.setTag(item);
				rowView.setEnabled(isOpen);
			
				return rowView;
			}
			else{
		
				return createNullView();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return createNullView();
		}
	}
	
	private boolean checkOpenTime(Store store) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		long current =  hour*60 + minutes;
		
		String start = store.getStartTime();
		String end = store.getEndTime();
		
		long startTime = getLongFromTime(start);
		long endTime = getLongFromTime(end);
		
		if((current >= startTime) && (current <= endTime)){
			return true;
		}else{
			return false;
		}
	}

	private long getLongFromTime(String start) {
		// TODO Auto-generated method stub
		
		String hour = start.substring(0, start.indexOf(":"));
		String minute = start.substring(start.indexOf(":")+1, start.length());
		
		return Integer.parseInt(hour)*60 + Integer.parseInt(minute);
	}

	public void addComments(View view, Store store) {
		LinearLayout comments = (LinearLayout) view
				.findViewById(R.id.comments);
//		if(whyq != null && comments != null){
//			if(whyq.getComments() != null){
//				if(whyq.getComments().size() == comments.getChildCount()) {
//					return;
//				}				
//			}
//		}

		comments.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		if(whyq.getComments() != null){
//			for (int i = 0; i < whyq.getComments().size(); i++) {
//				View cm = inflater.inflate(R.layout.comment_item, null);
//				final Comment pcm = whyq.getComments().get(i);
//				if (pcm != null && pcm.getAuthor() != null) {
//
//					ImageView cma = (ImageView) cm
//							.findViewById(R.id.commentAvatar);
//					cma.setOnClickListener(new View.OnClickListener() {
//						
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							WhyqMain.gotoTab(4, pcm);
//						}
//					});
//					UrlImageViewHelper.setUrlDrawable(cma, pcm.getAuthor()
//							.getAvatar().getUrl());
//
//					TextView authorName = (TextView) cm
//							.findViewById(R.id.commentAuthor);
//					authorName.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							WhyqMain.gotoTab(4, pcm);
//						}
//					});
//					if(pcm !=null){
//						if(pcm.getAuthor() != null)
//							if(pcm.getAuthor().getName() != null)
//								authorName.setText(pcm.getAuthor().getName());
//
//						TextView cmt = (TextView) cm
//							.findViewById(R.id.commentContent);
//						if(pcm.getContent() != null)
//							cmt.setText(pcm.getContent());
//					}
//					/*
//					 * boolean isWrapped = PermUtils.isTextWrapped(activity,
//					 * cmt.getText().toString(), cmt.getContext()); if
//					 * (isWrapped) { cmt.setMaxLines(5);
//					 * cmt.setSingleLine(false);
//					 * cmt.setEllipsize(TruncateAt.MARQUEE); }
//					 */
//					if (i == (whyq.getComments().size() - 1)) {
//						View sp = (View) cm.findViewById(R.id.separator);
//						sp.setVisibility(View.INVISIBLE);
//					}
//					/*
//					 * EllipsizingTextView cmt = (EllipsizingTextView) cm
//					 * .findViewById(R.id.commentContent);
//					 * cmt.setText(pcm.getContent());
//					 * 
//					 * if (i == (perm.getComments().size() - 1)) { View sp =
//					 * (View) cm.findViewById(R.id.separator);
//					 * sp.setVisibility(View.INVISIBLE); }
//					 */
//					comments.addView(cm);
//				}
//			}
//			
//		}
	}
	
	/**
	 * MSA
	 * @return
	 */
	public void loadMoreItems() {
		ListActivity follow = ((ListActivity)activity);
		follow.loadNextItems();
	}
	
	public View createFooterView() {
		final Context context = this.getContext();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View footerView = inflater.inflate(R.layout.prev_next_layout, null);
		ImageButton previousButton = (ImageButton) footerView.findViewById(R.id.previous);
		previousButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(activity instanceof ListActivity) {
					ListActivity follow = ((ListActivity)activity);
					follow.loadPreviousItems();
				}

			}
		});

	
		ImageButton nextButton = (ImageButton) footerView.findViewById(R.id.next);
		nextButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(activity instanceof ListActivity) {
						ListActivity follow = ((ListActivity)activity);
						follow.loadNextItems();
					}
					}
			});
		return footerView;
    
	}

	public View createNullView() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.whyq_item_3, null);
		view.setVisibility(view.GONE);
		return view;
	}
	
	private void loadImage(ImageView imageView, String url) {
		// TODO Auto-generated method stub
		
	}

	public class getData extends AsyncTask< ImageView  , String, Bitmap>{

		String imageUrl;
		ImageView imageView;
		public getData(String imageLink) {
			// TODO Auto-generated constructor stub
			super();
			imageUrl = imageLink;
		}

		@Override
		protected Bitmap doInBackground(ImageView... params) {
			// TODO Auto-generated method stub
			Bitmap bm = null;
			imageView = params[0];
			//Log.d("a","==========>"+imageUrl);
			bm = WhyqUtils.scaleBitmap(imageUrl);
			return bm;
		}
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO Auto-generated method stub
            super.onPostExecute(bitmap);   
//            Drawable bm =  imageView.getDrawable();
//            if( bm != null)
//            	((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
//            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setImageBitmap(bitmap);
        }

	}

	class CommentDialog extends Dialog implements android.view.View.OnClickListener{
		
		Store store  = null;
		User user = null;
		
		Button btnOK = null;
		EditText txtComment = null;
		
		private ProgressDialog dialog;
		
		public CommentDialog(Context context, Store store, User user) {
			super(context);
			setContentView( R.layout.comment_dialog );
			this.setTitle( R.string.comment_title );
			
			this.store = store;
			this.user = user;
			
			btnOK = (Button) findViewById( R.id.btnOK );
			btnOK.setOnClickListener(this);
			
			txtComment = ( EditText ) findViewById( R.id.commentEditText );
			
			
		}

		public void onClick(View v) {
			if( v == btnOK )
			{				
				final String cmText = txtComment.getText().toString();
				
				AsyncTask<Void, Void, String> comment = new AsyncTask<Void, Void, String >(){

					@Override
					protected String doInBackground(Void... arg0) {
						
						HttpPermUtils util = new HttpPermUtils();
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("cmnt", cmText ) );
						nameValuePairs.add(new BasicNameValuePair("pid", store.getStoreId() ) );
						nameValuePairs.add(new BasicNameValuePair("uid", user.getId() ) );
						String response  = util.sendRequest(API.commentUrl, nameValuePairs, false);
						
						return response;
					}
					
					
					@Override
			  		protected void onProgressUpdate(Void... unsued) {

			  		}

			  		@Override
			  		protected void onPostExecute(String sResponse) {
			  			if (dialog.isShowing()){
			  				dialog.dismiss();
			  				Toast.makeText(context,"Added comment!",Toast.LENGTH_LONG).show();
			  				int position = items.indexOf(store);			  				
			  				Comment comment = new Comment(store.getStoreId(), cmText);
			  				comment.setUser(user);
			  				//perm.addCommnent(comment);
//			  				items.get(position).addCommnent(comment);
			  				
			  				if(activity != null) {
			  					ListView list = (ListView) activity.findViewById(R.id.permList);
			  					if(list != null) {
			  						list.invalidateViews();
			  					}
			  				}			  				
			  			}
			  		}
					
				};
				
				if(cmText.length() > 0 ){ //!cmText.isEmpty()
					this.dismiss();
					dialog = ProgressDialog.show(context, "Uploading","Please wait...", true);
					comment.execute();
				}
				else{
					Toast.makeText(txtComment.getContext().getApplicationContext(),"Don't leave text blank!",Toast.LENGTH_LONG).show();
				}
				
			}
		}
		
	}

	/**
	 * This listener is to handle the click action of Join button This should
	 * show the dialog and user can choose Facebook or Twitter login or login to
	 * Permping directly.
	 */


	/**
	 * @return the count
	 */
	public int getCount() {
		if(items == null) {
		return count;
	}
		return items.size();
	}
	

	/**
	 * @return the nextItems
	 */
	public int getNextItems() {
		return nextItems;
	}

	/**
	 * @param nextItems the nextItems to set
	 */
	public void setNextItems(int nextItems) {
		this.nextItems = nextItems;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItem(int)
	 */
	@Override
	public Store getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}
	
	public View createHeaderView() {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.whyq_item_2, null);
		// Process buttons
//		join = (Button) view.findViewById(R.id.bt_join);
//		login = (Button) view.findViewById(R.id.bt_login);
//		login.setOnClickListener(WhyqAdapter.this);
//		join.setOnClickListener(WhyqAdapter.this);
		
		updateHeaderView(view);
		
		return view;
	}
	
	public void updateHeaderView(View view) {
		TableRow loginRow = (TableRow) view.findViewById(R.id.loginBar);
		if(loginRow == null) {
			return;
		}
		user = WhyqUtils.isAuthenticated(view.getContext());
		if (user != null) {
			loginRow.setVisibility(View.GONE);
		} else {
			loginRow.setVisibility(View.VISIBLE);
		}
	}
	
	public boolean isPermDuplicate(Store store) {
		if(store == null || store.getStoreId() == null) {
			return false;
		}
		for(int i = 0; i < items.size(); i++) {
			Store permItem = items.get(i);
			if(permItem != null && permItem.getStoreId() != null) {
				if(permItem.getStoreId().equals(store.getStoreId())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/*
	static class ViewHolder {
		ImageView avatar;
		TextView authorName;
		TextView boardName;
		ImageView permImage;
		TextView permDesc;
		TextView permInfo;
		TextView permStat;
		LinearLayout comments;
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id= v.getId();
		switch (id) {
		case R.id.bt_login:
			exeLogin();
			break;

		case R.id.btnLike:
//			exeLike(v);
			break;
		case R.id.btnRepem:
			exeReperm(v);
			break;
		case R.id.btnComment:
			exeComment(v);
			break;
		case R.id.btnLocation:
			exeGotoMap(v);
			break;
		case R.id.lvRowView:
			exeGotoDetail(v);
			break;
		default:
			break;
		}
		
	}

	private void exeGotoDetail(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, ListDetailActivity.class);
		context.startActivity(intent);
	}

	private void exeGotoMap(View view) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		String permId = (String)view.getTag();
//		if(permId != null){
//			Store store = newPermList.get(permId);
//			Intent googleMap = new Intent(context,
//					GoogleMapActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putFloat("lat", Float.parseFloat(store.getLatitude()));
//			bundle.putFloat("lon", Float.parseFloat(store.getLongitude()));
//			bundle.putString("thumbnail", store.getLogo());
//			googleMap.putExtra("locationData", bundle);
//			//Log.d("AA+++++============","========="+perm.getImage().getUrl());
//			View view2 = ListActivityGroup.group.getLocalActivityManager().startActivity( "GoogleMapActivity"+store.getStoreId(), googleMap).getDecorView();
//			ListActivityGroup.group.replaceView(view2);
//
//		}	
	}

	private void exeComment(View view) {
		// TODO Auto-generated method stub
		String permId = (String)view.getTag();
		if(permId != null){
			Store store = newPermList.get(permId);
			user = WhyqUtils.isAuthenticated(view.getContext());
			if (user != null) {
				
				WhyqApplication state = (WhyqApplication)view.getContext().getApplicationContext();
				
				CommentDialog commentDialog = new CommentDialog( view.getContext(), store , state.getUser() );
				commentDialog.show();
			} else {
				Toast.makeText(view.getContext(), Constants.NOT_LOGIN, Toast.LENGTH_LONG).show();
			}

		}
	
	}

	private void exeReperm(View view) {
		// TODO Auto-generated method stub
		String permId = (String)view.getTag();
		if(permId != null){
			Store store = newPermList.get(permId);
			user = WhyqUtils.isAuthenticated(view.getContext());
//			if (user != null) {
//				Intent myIntent = new Intent(view.getContext(),
//						NewWhyqActivity.class);
//				myIntent.putExtra("reperm", true);
//				NewWhyqActivity.boardList = user.getBoards();
//				myIntent.putExtra("boardId", whyq.getBoard().getId());
//				myIntent.putExtra("boardDesc", (String) whyq.getBoard().getDescription());
//				myIntent.putExtra("permId", (String) whyq.getId());
//				myIntent.putExtra("userId", user.getId());
//				context.startActivity(myIntent);
//			} else {
//				Toast.makeText(view.getContext(), Constants.NOT_LOGIN,
//						Toast.LENGTH_LONG).show();
//			}
			
		}
	
	}

//	private void exeLike(final View v) {
//		// TODO Auto-generated method stub
//		String permId = (String)v.getTag();
//		if(permId != null){
//			final Whyq whyq = newPermList.get(permId);
//			user = WhyqUtils.isAuthenticated(v.getContext());
//			if (user != null) {
//				// final ProgressDialog dialog =
//				// ProgressDialog.show(v.getContext(),
//				// "Loading","Please wait...", true);
//
//				final HttpPermUtils util = new HttpPermUtils();
//				//Log.d("aasdfsdss", like.getText().toString()+"======="+R.string.delete);
//				if(like.getText().toString().equals(likeString) || like.getText().toString().equals(unlikeString)){
//					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//					nameValuePairs.add(new BasicNameValuePair("pid", String
//							.valueOf(whyq.getId())));
//					nameValuePairs.add(new BasicNameValuePair("uid", String
//							.valueOf(user.getId())));
//					util.sendRequest(API.likeURL, nameValuePairs, false);
//
//					if (v instanceof Button) {
//						String label = ((Button) v).getText().toString();
//						int likeCount = Integer.parseInt(whyq
//								.getPermLikeCount());
//						if (label != null && label.equals(likeString)) { // Like
//							// Update the count
//							likeCount++;
//							whyq.setPermLikeCount(String.valueOf(likeCount));
//							// Change the text to "Unlike"
//							((Button)v).setText(R.string.bt_unlike);
//							v.invalidate();
//						} else { // Unlike
//							likeCount = likeCount - 1;
//							if (likeCount < 0)
//								likeCount = 0;
//							whyq.setPermLikeCount(String.valueOf(likeCount));
//							((Button)v).setText(R.string.bt_like);
//							v.invalidate();
//						}
//					}
//					
//					String permStatus = likeString + ": " + whyq.getPermLikeCount()
//							+ " - " + repermString + ": " + whyq.getPermRepinCount()
//							+ " - " + commentString + ": " + whyq.getPermCommentCount();
//					TextView txtStatus = permStateList.get(permId);
//					if(permStatus != null)
//						txtStatus.setText(permStatus);
//				}else if(like.getText().toString().equals(textCurrentLike)){
//					AlertDialog alertDialog = new AlertDialog.Builder(context).create();		
////					alertDialog.setTitle(AlertDialog);
//					alertDialog.setMessage(v.getContext().getString(R.string.confirm_delete));
//					alertDialog.setButton(v.getContext().getString(R.string.yes_delete), new DialogInterface.OnClickListener() {
//					   public void onClick(DialogInterface dialog, int which) {
//					      // here you can add functions
//							viewList.remove(whyq.getId());
//							int position = v.getNextFocusDownId();
//							if(position >= 0 && items.size() > position)
//								items.remove(position);
//							notifyDataSetChanged();
//							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//							nameValuePairs.add(new BasicNameValuePair("delid", String
//									.valueOf(whyq.getId())));
//							nameValuePairs.add(new BasicNameValuePair("uid", String
//									.valueOf(user.getId())));
//							util.sendRequest(API.deleteUrl, nameValuePairs, false);
//					   }
//					});
//					alertDialog.setButton2(v.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
//					   public void onClick(DialogInterface dialog, int which) {
//							      
//					   }
//					});
//					alertDialog.setIcon(R.drawable.icon);
//					alertDialog.show();
//
//					
//				}
//			} else {
//
//			}
//
//		}	
//	}



	private void exeLogin() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = prefs.edit();
		// Set default login type.
		editor.putString(Constants.LOGIN_TYPE,
				Constants.PERMPING_LOGIN);
		editor.commit();
		WhyqMain.showLogin();
	}

	public ArrayList<Store> getData() {
		// TODO Auto-generated method stub
		return items;
	}

	public void changeSrc(ArrayList<Store> list) {
		// TODO Auto-generated method stub
		items = list;
	}
}

