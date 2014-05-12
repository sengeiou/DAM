package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import twitter4j.util.ImageUpload.ImgLyOAuthUploader;
import whyq.activity.InvitationActivity;
import whyq.model.User;
import whyq.service.img.UrlImageViewHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whyq.R;

public class InvitationAdapter extends ArrayAdapter<User> {

	private List<User> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	private InvitationHolder holder;
	public static List<InvitationAdapter.InvitationHolder> listSectionView = new ArrayList<InvitationAdapter.InvitationHolder>();

	public InvitationAdapter(Context context, List<User> sectionList) {
		super(context, R.layout.invitation_screen, sectionList);
		this.context = context;
		this.list = sectionList;
	}

	public static class InvitationHolder {
		public TextView tvName;
		public ImageView imgAvatar, imgTypeAccount;
		public Button btnAccept;
		public Button btnRemove;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get(""+position);
		User item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((InvitationActivity) context).getLayoutInflater();
			view = inflator.inflate(R.layout.invitation_item, null);
			holder = new InvitationHolder();

			holder.imgAvatar = (ImageView)view.findViewById(R.id.imgAvatar);
			holder.imgTypeAccount = (ImageView)view.findViewById(R.id.imgTypeAccount);
			holder.tvName = (TextView)view.findViewById(R.id.tvName);
			
			holder.tvName.setText(item.getFirstName()+" "+item.getLastName());
			if( item.getAvatar()!=null)
				UrlImageViewHelper.setUrlDrawable(holder.imgAvatar, item.getAvatar().getUrl());
			holder.btnAccept = (Button)view.findViewById(R.id.btnAccept);
			holder.btnRemove = (Button)view.findViewById(R.id.btnDelete);
			holder.btnAccept.setTag(item);
			holder.btnRemove.setTag(item);
			if(item.getFacebookId()!=null){
				holder.imgTypeAccount.setImageResource(R.drawable.icon_facebook);
			}else if(item.getTwitterId()!=null){
				holder.imgTypeAccount.setImageResource(R.drawable.icon_twitter);
			}
			view.setTag(holder);
			listSectionView.add(holder);
			viewList.put(String.valueOf(position), view);
		} else {
			view = convertView;
			holder = (InvitationHolder) view.getTag();
		}
		return view;
	}

	public void setData(ArrayList<User> arrayList) {
		// TODO Auto-generated method stub
		list = arrayList;
	}


}