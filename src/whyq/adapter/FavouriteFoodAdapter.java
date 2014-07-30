package whyq.adapter;

import java.util.ArrayList;

import com.dealadelivery.whyq.R;

import whyq.WhyqApplication;
import whyq.model.Product;
import whyq.model.ProductGroup;
import whyq.service.img.good.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavouriteFoodAdapter extends BaseExpandableListAdapter {
	ArrayList<ProductGroup> productGroup;
	LayoutInflater mInflater;
	ImageLoader imageLoader;

	public FavouriteFoodAdapter(Context context,
			ArrayList<ProductGroup> productList) {
		this.productGroup = productList;

		imageLoader = WhyqApplication.Instance().getImageLoader();
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getGroupCount() {
		return productGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return productGroup.get(groupPosition).productList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return productGroup.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return productGroup.get(groupPosition).productList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// GroupHolder groupHolder = null;
		//
		// if (convertView != null) {
		// groupHolder = (GroupHolder) convertView.getTag();
		//
		// } else {
		TextView tvGroupName = new TextView(parent.getContext());
		tvGroupName.setPadding(10, 0, 0, 0);
		tvGroupName.setTextColor(parent.getContext().getResources()
				.getColor(R.color.red));
		tvGroupName.setTextSize(21);
		tvGroupName.setText(productGroup.get(groupPosition).groupName);

		// groupHolder = new GroupHolder();
		// groupHolder.tvGroupName = tvGroupName;

		// tvGroupName.setTag(groupHolder);
		// }
		//
		// groupHolder.tvGroupName
		// .setText(productGroup.get(groupPosition).groupName);

		return tvGroupName;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Product product = productGroup.get(groupPosition).productList
				.get(childPosition);
		ChildHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.favourite_food_child_view,
					null);
			mHolder = new ChildHolder();
			mHolder.imgThumb = (ImageView) convertView
					.findViewById(R.id.img_thumb_favourite_food_child_view);
			mHolder.tvProductName = (TextView) convertView
					.findViewById(R.id.tv_product_name_child_view);
			mHolder.tvLikeCount = (TextView) convertView
					.findViewById(R.id.tv_favourite_food_count_child_view);
			mHolder.imgLike = (ImageView) convertView
					.findViewById(R.id.img_favourite_food_child_view);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ChildHolder) convertView.getTag();
		}

		imageLoader.DisplayImage(product.imgThumb, mHolder.imgThumb);
		mHolder.tvProductName.setText(product.name);
		mHolder.tvLikeCount.setText(product.likeCount);
		if (product.isLike)
			mHolder.imgLike.setBackgroundResource(R.drawable.icon_fav_enable);
		else
			mHolder.imgLike.setBackgroundResource(R.drawable.icon_fav_disable);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	class ChildHolder {
		TextView tvProductName;
		TextView tvLikeCount;
		ImageView imgThumb;
		ImageView imgLike;
	}

}
