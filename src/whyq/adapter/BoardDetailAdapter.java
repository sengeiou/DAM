/**
 * 
 */
package whyq.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import whyq.activity.NewWhyqActivity;
import whyq.activity.ProfileActivityGroup;
import whyq.model.Store;
import whyq.model.User;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.HttpPermUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dealadelivery.whyq.R;

/**
 * @author Linh Nguyen
 *
 */
public class BoardDetailAdapter extends BaseAdapter implements ListAdapter {

	private Activity activity;
	private List<Store> stores;
	private String boardName;
	private int screenWidth;
	private int screenHeight;
	private User user;
	private Context context;
	public BoardDetailAdapter(Activity activity, List<Store> stores, String boardName, 
			int screenWidth, int screenHeight, User user) {
		this.activity = activity;
		this.context = activity.getParent();
		this.stores = stores;
		this.boardName = boardName;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.user = user;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = activity.getLayoutInflater();
		final View view = inflater.inflate(R.layout.profile_perm_layout, null);
		
		final Store store = stores.get(position);
		final Button like = (Button) view.findViewById(R.id.btLike);
		final Button rePerm = (Button) view.findViewById(R.id.btReperm);
		final ImageView btnLocation = (ImageView)view.findViewById(R.id.btLocation);
		// Validate Like or Unlike
		if (store != null) {
//			if (whyq.getPermUserLikeCount() != null && "0".equals(whyq.getPermUserLikeCount())) {
//				like.setText(Constants.LIKE);
//			} else {
//				like.setText(Constants.UNLIKE);
//			}
		}
		like.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (user != null && store != null) {
					HttpPermUtils httpPermUtils = new HttpPermUtils();
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("pid", String.valueOf(store.getStoreId())));
					nameValuePairs.add(new BasicNameValuePair("uid", String.valueOf(user.getId()))); 
					httpPermUtils.sendRequest(API.likeURL, nameValuePairs, false);
//					if (v instanceof Button) {
//						String label = ((Button) v).getText().toString();
////						int likeCount = Integer.parseInt(whyq.getPermLikeCount());
//						if (label != null && label.equals(Constants.LIKE)) { // Like
//							// Update the count
//							likeCount++;
//	    					whyq.setPermLikeCount(String.valueOf(likeCount));
//	    					// Change the text to "Unlike"
//	    					like.setText(Constants.UNLIKE);
//						} else { // Unlike
//							likeCount = likeCount - 1;
//							if (likeCount < 0) 
//								likeCount = 0;
//							whyq.setPermLikeCount(String.valueOf(likeCount));
//							like.setText(Constants.LIKE);
//						}
//					}
//					String permStatus = "Like: " + whyq.getCountFavaouriteMember()+ " - Repin: " + whyq.getPermRepinCount() + " - Comment: " + whyq.getPermCommentCount();
//		            TextView txtStatus = (TextView) view.findViewById(R.id.permStatus);
//		            txtStatus.setText(permStatus);
				} else {
					Toast.makeText(v.getContext(), Constants.NOT_LOGIN, Toast.LENGTH_LONG).show();
				}
			}
		});
		
		rePerm.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				// Get the id of perm
				if (store != null) {
					
					Intent myIntent = new Intent(view.getContext(), NewWhyqActivity.class);
					myIntent.putExtra("permID", (String) store.getStoreId() );
					View repermView = ProfileActivityGroup.group.getLocalActivityManager() .startActivity("RepermActivity", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ProfileActivityGroup.group.replaceView( repermView );
					
					
					
					/*
					String permId = perm.getId();
					String permDesc = perm.getDescription();
					PermBoard currentBoard = perm.getBoard();
					
					// Go to the Reperm screen
					Intent i = new Intent(view.getContext(), RepermActivity.class);
					i.putExtra(Constants.PERM_ID, permId);
					i.putExtra(Constants.PERM_DESCRIPTION, permDesc);
					i.putExtra(Constants.CURRENT_BOARD, currentBoard);
					View rePermView = ProfileActivityGroup.group.getLocalActivityManager().startActivity("RepermActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ProfileActivityGroup.group.replaceView(rePermView);
					*/
				}
			}
		});
		btnLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent googleMap = new Intent(context,
//						GoogleMapActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putFloat("lat", Float.parseFloat(store.getLatitude()));
//				bundle.putFloat("lon", Float.parseFloat(store.getLongitude()));
//				bundle.putString("thumbnail", store.getLogo());
//				googleMap.putExtra("locationData", bundle);
//				//Log.d("AA+++++============","========="+perm.getImage().getUrl());
//				View view = ProfileActivityGroup.group.getLocalActivityManager().startActivity( "PrGoogleMapActivity"+store.getStoreId(), googleMap).getDecorView();
//				ProfileActivityGroup.group.replaceView(view);	
			}
		});
//		if(whyq.getLon() ==0 && whyq.getLat() == 0){
//			btnLocation.setVisibility(View.GONE);
//		}else{
//			btnLocation.setVisibility(View.VISIBLE);
//		}
		if (store != null) {
			// The Board Name
			TextView txtBoardName = (TextView) view.findViewById(R.id.boardName);
			if (boardName != null) {
				txtBoardName.setText(boardName);
			} else {
//				WhyqBoard board = whyq.getBoard();
//				if (board != null) {
//					txtBoardName.setText(board.getName());
//				} else {
//					txtBoardName.setText("N/A");
//				}
			}
			
			// The image of perm
//			final ImageView permImage = (ImageView) view.findViewById(R.id.permImage);
//			UrlImageViewHelper.setUrlDrawable(permImage, whyq.getImage().getUrl());
//			//PermUtils.scaleImage(permImage, screenWidth, screenHeight);
//			
//			// Perm Description
//			TextView txtPermDescription = (TextView) view.findViewById(R.id.permDescription);
//			txtPermDescription.setText(whyq.getDescription());
//			
////			// Perm Information
////			TextView txtPermInfo = (TextView) view.findViewById(R.id.permInfo);
////			txtPermInfo.setText("via " + perm.getAuthor().getName() + " on to " + boardName);
//			
//			// Status
//			String permStatus = "Like: " + whyq.getPermLikeCount() + " - Repin: " + whyq.getPermRepinCount() + " - Comment: " + whyq.getPermCommentCount();
//            TextView txtStatus = (TextView) view.findViewById(R.id.permStatus );
//            txtStatus.setText(permStatus);
            
//            LinearLayout comments = (LinearLayout) view.findViewById(R.id.comments);
//            if(whyq.getComments() != null){
//            	for(int i = 0; i < whyq.getComments().size(); i ++){
//                    View cm = inflater.inflate(R.layout.comment_item, null );
//                    Comment comment = whyq.getComments().get(i);
//                    if(comment != null && comment.getAuthor() != null) {
//                       
//                 		   ImageView cma = (ImageView) cm.findViewById(R.id.commentAvatar);
//                 		   UrlImageViewHelper.setUrlDrawable(cma, comment.getAuthor().getAvatar().getUrl());
//                 	   
//    	                   
//    	               TextView cmt = (TextView) cm.findViewById(R.id.commentContent);
//    	               cmt.setText(comment.getContent());
//    	                   
//    	               if( i == (whyq.getComments().size() -1)){
//    	            	   View sp = (View) cm.findViewById(R.id.separator);
//    	            	   sp.setVisibility(View.INVISIBLE);
//    	               }
//    	               comments.addView(cm);
//                    }
//                }
//	
//            }
            
    		
		}
		return view;
	}
	
	
	public int getCount() {
		// TODO Auto-generated method stub
		return stores.size();
	}
	
	
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return stores.get(position);
	}
	
	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
}
