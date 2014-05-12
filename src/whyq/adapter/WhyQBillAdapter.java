package whyq.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import whyq.activity.WhyQBillScreen;
import whyq.model.Bill;
import whyq.model.ExtraItem;
import whyq.model.OptionItem;
import whyq.model.SizeItem;
import whyq.utils.Util;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whyq.R;

public class WhyQBillAdapter extends ArrayAdapter<Bill> {

	private final List<Bill> list;
	private Context context;
	private HashMap<String, View> viewList = new HashMap<String, View>();
	public static List<ViewBillHolder> listSectionView = new ArrayList<ViewBillHolder>();

	public WhyQBillAdapter(Context context, List<Bill> billList) {
		super(context, R.layout.whyq_bill_item, billList);
		this.context = context;
		this.list = billList;
	}

	public List<Bill> getData() {
		return list;
	}

	public static class ViewBillHolder {
		public TextView tvName, tvPrice, unit, tvAmount;
		// public ImageView imgThumb;
		public String storeId;
		public TextView tvOption;
		public TextView tvSize;
		public TextView tvExtra;

		public ViewBillHolder() {

		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		convertView = viewList.get("" + position);
		Bill item = list.get(position);
		if (convertView == null) {
			LayoutInflater inflator = ((WhyQBillScreen) context).getLayoutInflater();
			view = inflator.inflate(R.layout.whyq_bill_item_phase2, null);
			final ViewBillHolder viewHolder = new ViewBillHolder();
			viewHolder.tvPrice = (TextView) view.findViewById(R.id.tvPrice);
			// viewHolder.imgThumb = (ImageView)
			// view.findViewById(R.id.imgThumb);
			viewHolder.tvName = (TextView) view.findViewById(R.id.tvName);
			viewHolder.unit = (TextView) view.findViewById(R.id.tvUnit);
			viewHolder.tvAmount = (TextView) view.findViewById(R.id.tvAmount);
			viewHolder.tvOption = (TextView) view.findViewById(R.id.tvOption);
			viewHolder.tvSize = (TextView) view.findViewById(R.id.tvSize);
			viewHolder.tvExtra = (TextView) view.findViewById(R.id.tvExtra);

			if (!item.getPrice().equals("")) {
				viewHolder.tvPrice.setText("$" + Util.round(Float.parseFloat(item.getPrice()), 2));
			}

			viewHolder.unit.setText(item.getUnit());
			if (!item.getPrice().equals("")) {
				float value = Float.parseFloat(item.getUnit()) * Float.parseFloat(item.getPrice());
				viewHolder.tvAmount.setText("$" + Util.round(value, 2));
			}
			// viewHolder.tvCount.setText(item.getSort());
			// UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb,
			// item.getThumb());
			viewHolder.tvName.setText(Html.fromHtml(item.getProductName()));
			if (item.getOptionList() != null) {
				viewHolder.tvOption.setText(convertString(item.getOptionList(), 1));
			}

			if (item.getSizeList() != null) {
				viewHolder.tvSize.setText(convertString(item.getSizeList(), 2));
			}

			if (item.getExtraList() != null) {
				viewHolder.tvExtra.setText(convertString(item.getExtraList(), 3));
			}

			view.setTag(viewHolder);

			viewList.put(String.valueOf(item.getId()), view);
		} else {
			view = convertView;

		}
		if (position % 2 == 0) {
			view.setBackgroundColor(Color.parseColor("#f5f5f5"));
		}
		return view;
	}

	private CharSequence convertString(Object list, int type) {
		// TODO Auto-generated method stub
		String result = "";
		if (type == 1) {
			List<OptionItem> list1 = (List<OptionItem>) list;
			for (OptionItem item : list1) {
				
				result += item.getName();

			}
		} else if (type == 2) {
			List<SizeItem> list1 = (List<SizeItem>) list;
			for (SizeItem item : list1) {

				result += item.getName();

			}
		} else {
			List<ExtraItem> list1 = (List<ExtraItem>) list;
			for (ExtraItem item : list1) {

				result += item.getName();

			}
		}

		return result;
	}

}