package whyq.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import whyq.model.Menu;
import whyq.utils.UrlImageViewHelper;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.R;

public class WhyqMenuAdapter extends ArrayAdapter<Menu> {

	private final List<Menu> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	public static List<ViewHolderMitemInfo> listSectionView = new ArrayList<ViewHolderMitemInfo>();

	public WhyqMenuAdapter(Context context, List<Menu> menuList) {
		super(context, R.layout.whyq_menu_item_, menuList);
		this.context = context;
		this.list = menuList;
	}
	public List<Menu> getData(){
		return list;
	}
	public static class ViewHolderMitemInfo {
		public TextView tvType, tvPrice, tvCount;
		public ImageView imgThumb;
		public String storeId;
		public Button btnAdd, btnRemove;
		public String menuId;
		public ViewHolderMitemInfo() {

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get(""+position);
		Menu item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((ListDetailActivity) context)
					.getLayoutInflater();
			view = inflator.inflate(R.layout.whyq_menu_item_, null);
			final ViewHolderMitemInfo viewHolder = new ViewHolderMitemInfo();
			viewHolder.tvType = (TextView) view.findViewById(R.id.tvType);
			viewHolder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
			viewHolder.imgThumb = (ImageView) view.findViewById(R.id.imgThumbnail);
			viewHolder.tvCount = (TextView)view.findViewById(R.id.totalForItem);
//			viewHolder.storeId = item.getS;
			viewHolder.tvType.setText(Html.fromHtml(item.getNameProduct()));
			viewHolder.tvPrice.setText("$"+item.getValue());
			viewHolder.storeId = item.getStoreId();
//			viewHolder.tvCount.setText(item.getSort());
			viewHolder.btnAdd = (Button)view.findViewById(R.id.btnAdd);
			viewHolder.btnRemove = (Button) view.findViewById(R.id.btnRemove);
			viewHolder.menuId = item.getId();
			
			viewHolder.btnAdd.setTag(item);
			viewHolder.btnRemove.setTag(item);
			
//			UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getImageThumb());
			WhyqApplication.Instance().getImageLoader().DisplayImage(item.getImageThumb(),viewHolder.imgThumb);
			view.setTag(viewHolder);
			
			viewList.put(String.valueOf(item.getStoreId()), view);
		} else {
			view = convertView;

		}

		return view;
	}

}