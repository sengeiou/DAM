package whyq.adapter;


import java.util.ArrayList;

import whyq.WhyqApplication;
import whyq.model.Store;
import whyq.utils.UrlImageViewHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dealadelivery.whyq.R;


public class WhyqItemAdapterNew extends BaseAdapter {
	private Context context;
	private ArrayList<Store> listProducts;

	static class ViewHolder {
		public ImageView imgThumb ;
		private TextView tvItemName, tvNumberFavourite,tvItemAddress, tvVisited, tvDiscoutNumber;
		private Button btnDistance;
	}

	public WhyqItemAdapterNew(Context context, ArrayList<Store> productList) {
		// super(context);
		this.context = context;
		listProducts = productList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			Store item = listProducts.get(position);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.whyq_item_new, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.imgThumb = (ImageView) rowView.findViewById(R.id.imgThumbnal2);
			viewHolder.tvItemName = (TextView) rowView.findViewById(R.id.tvItemName);
			viewHolder.tvItemAddress = (TextView)rowView.findViewById(R.id.tvItemAddress);
			viewHolder.tvNumberFavourite = (TextView)rowView.findViewById(R.id.tvNumberFavourite);
			viewHolder.tvVisited = (TextView)rowView.findViewById(R.id.tvVisited);
			viewHolder.tvDiscoutNumber = (TextView)rowView.findViewById(R.id.tvNumberDiscount);
			viewHolder.btnDistance = (Button)rowView.findViewById(R.id.btnDistance);

			viewHolder.tvItemName.setText(item.getNameStore());
			viewHolder.tvItemAddress.setText(item.getAddress());
			viewHolder.tvNumberFavourite.setText(""+item.getCountFavaouriteMember());
			viewHolder.btnDistance.setText("");
//			UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getLogo());
			WhyqApplication.Instance().getImageLoader().DisplayImage(item.getLogo(),viewHolder.imgThumb);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		
//		holder.text.setTypeface(SectionFragment.listFont.fontHomeScreen());
		return rowView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listProducts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public ArrayList<Store> getListNews() {
		return listProducts;
	}

	public void setListProducts(ArrayList<Store> listProducts) {
		this.listProducts = listProducts;
	}

	public Store getFirstProducts() {
		return listProducts.get(0);
	}

}