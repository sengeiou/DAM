package whyq.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import whyq.model.FriendFacebook;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.dealadelivery.whyq.R;

public class FriendAdapter extends BaseAdapter {

	private List<FriendFacebook> list;
	private Context context;

	public FriendAdapter(Context c, List<FriendFacebook> list) {
		// TODO Auto-generated const-ructor stub
		this.list = list;
		context = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Map<String, View> viewList = new HashMap<String, View>();
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		FriendFacebook item = list.get(position);
		Holder holder = null;
		convertView = viewList.get(item.getFacebookId());
		if (convertView == null){
			Log.d("getView","new");
			convertView = LayoutInflater.from(context).inflate(
					R.layout.friend_facebook_tag_item, parent, false);
			holder = new Holder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.cbxTag = (CheckBox) convertView.findViewById(R.id.cbxTag);
			holder.fbId = item.getId();
			holder.cbxTag.setChecked(false);
			holder.tvName.setText(item.getFirstName());
			
			holder.cbxTag.setTag(item);
			convertView.setTag(holder);
			viewList.put(item.getFacebookId(), convertView);
			return convertView;
			
		}else{
			Log.d("getView","old");
//			holder = (Holder) convertView.getTag();
			return convertView;
//			if(holder==null){
//				holder = new Holder();
//				holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
//				holder.cbxTag = (CheckBox) convertView.findViewById(R.id.cbxTag);
//				holder.fbId = item.getId();
//			}
		}



	}

	public class Holder {
		private TextView tvName;
		private CheckBox cbxTag;
		private String fbId;
	}
}
