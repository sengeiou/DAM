package whyq.adapter;

import java.util.List;

import whyq.model.OptionItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dealadelivery.whyq.R;

public class OptionItemBasicAdapter extends ArrayAdapter<OptionItem> {

	private Context mContext;
	private List<OptionItem> list;
	private LayoutInflater mInflater;
	private int mFocusColor;
	private int mNormalColor;

	public OptionItemBasicAdapter(Context context, int resource, List<OptionItem> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		list = objects;
		mContext = context;
		
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mFocusColor = mContext.getResources().getColor(R.color.red);
		mNormalColor = mContext.getResources().getColor(R.color.grey);
	}

	public void setDatas(List<OptionItem> list) {
		this.list = list;
	}

	public List<OptionItem> getDatas() {
		return this.list;

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size();
	}

	public OptionItem getItemAtIndex(int index) {
		if (this.list.size() > index) {
			return this.list.get(index);

		} else {
			return null;
		}
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		OptionItemHolder holder;
		final OptionItem item = this.list.get(position);
		if (view == null) {
			view = mInflater.inflate(R.layout.option_item, parent, false);

			holder = new OptionItemHolder();

			holder.mName = (TextView) view.findViewById(R.id.tv_option_item_name);
			holder.mValue = (TextView) view.findViewById(R.id.tv_option_item_value);
			holder.mLnItem = (LinearLayout)view.findViewById(R.id.ln_item_option);
			
//			holder.mLnItem.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					onItemClicked(item);
//				}
//			});
			
			view.setTag(holder);
		} else {
			holder = (OptionItemHolder) view.getTag();
		}

	

		holder.mName.setText("" + item.getName() != null ? item.getName() : "");
		holder.mValue.setText("" + item.getValue() != null ? item.getValue() : "");
		
		holder.mName.setTextColor(item.isSelected() ? mFocusColor: mNormalColor);
		holder.mValue.setTextColor(item.isSelected() ? mFocusColor: mNormalColor);

		holder.data = item;
		return view;

	}

	protected void onItemClicked(OptionItem item) {
		// TODO Auto-generated method stub
		
	}

	public static class OptionItemHolder {

		private TextView mName;
		private TextView mValue;
		private LinearLayout mLnItem;
		public OptionItem data;
	}
}
