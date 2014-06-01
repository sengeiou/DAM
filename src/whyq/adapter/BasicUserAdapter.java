package whyq.adapter;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.model.User;
import whyq.utils.UrlImageViewHelper;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.R;

public class BasicUserAdapter extends BaseAdapter {

	private final List<User> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	public static List<ViewUserHolder> listSectionView = new ArrayList<ViewUserHolder>();

	public BasicUserAdapter(Context context, List<User> UserList) {
		this.context = context;
		this.list = UserList;
	}
	public void resetData(){
		this.list.clear();
		notifyDataSetChanged();
	}
	public List<User> getData(){
		return list;
	}
	public static class ViewUserHolder {
		public TextView tvName;
		public ImageView imgThumb, imgFriendThumb;
		public String storeId;
		public ViewUserHolder() {

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get(""+position);
		User item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((ListDetailActivity) context)
					.getLayoutInflater();
			view = inflator.inflate(R.layout.user_item, null);
			final ViewUserHolder viewHolder = new ViewUserHolder();
			viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgAvatar);
			viewHolder.imgFriendThumb = (ImageView) view.findViewById(R.id.imgFriend);
			viewHolder.tvName = (TextView)view.findViewById(R.id.tvFriendCount);
//			UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getUrlAvatar());
			
			Log.d("BasicUserAdapter", "getUrlAvatar : "+item.getUrlAvatar());
			WhyqApplication.Instance().getImageLoader().DisplayImage(item.getUrlAvatar(), viewHolder.imgThumb);
			
			viewHolder.tvName.setText(item.getFirstName()+" "+item.getLastName());
			if(item.isFriend()){
				viewHolder.imgFriendThumb.setBackgroundResource(R.drawable.icon_friended);
			}else{
				viewHolder.imgFriendThumb.setVisibility(View.INVISIBLE);
			}
			view.setTag(viewHolder);
			
			viewList.put(String.valueOf(position), view);
		} else {
			view = convertView;

		}

		return view;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public User getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}