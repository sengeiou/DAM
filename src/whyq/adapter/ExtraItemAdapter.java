package whyq.adapter;

import java.util.ArrayList;
import java.util.List;

import whyq.interfaces.OnnOptionItemSelected;
import whyq.model.ExtraItem;
import whyq.model.OptionItem;
import android.content.Context;
import android.util.Log;

public class ExtraItemAdapter extends OptionItemBasicAdapter {

	private Context mContext;
	private OnnOptionItemSelected mOnOptionItemSelected;

	public ExtraItemAdapter(Context context, int resource, List<ExtraItem> objects) {

		super(context, resource, convertExtraToOption(objects));
		// TODO Auto-generated constructor stub
		mContext = context;
	}


	
	public void updateData(List<ExtraItem> list) {
		// TODO Auto-generated method stub
		super.setDatas(convertExtraToOption(list));
	}
	
	public void setDelegate(OnnOptionItemSelected listener){
		mOnOptionItemSelected = listener;
	}
	
	private static List<OptionItem> convertExtraToOption(List<ExtraItem> objects) {
		// TODO Auto-generated method stub
		List<OptionItem> result = new ArrayList<OptionItem>();
		for (ExtraItem item : objects) {
			OptionItem i = new OptionItem();
			i.setName(item.getName());
			i.setValue(item.getValue());
			i.setCreatedata(item.getCreatedata());
			i.setId(item.getId());
			i.setNote(item.getNote());
			i.setProductId(item.getProductId());
			i.setSelected(item.isSelected());
			i.setSkue(item.getSkue());
			i.setSort(item.getSort());
			i.setStatus(item.getStatus());
			i.setType(item.getType());
			result.add(i);
		}

		return result;

	}
	
//	@Override
//	protected void onItemClicked(OptionItem item) {
//		// TODO Auto-generated method stub
//		super.onItemClicked(item);
//		
//		Log.d("onItemClicked","onItemClicked"+item);
//		
//		ExtraItem i = convertOptionItemToExtraItem(item);
//		
//		mOnOptionItemSelected.onSelected(2, i);
//		
//		notifyDataSetChanged();
//	}

	public static ExtraItem convertOptionItemToExtraItem(OptionItem item){
		ExtraItem i = new ExtraItem();
		i.setName(item.getName());
		i.setValue(item.getValue());
		i.setCreatedata(item.getCreatedata());
		i.setId(item.getId());
		i.setNote(item.getNote());
		i.setProductId(item.getProductId());
		i.setSelected(item.isSelected());
		i.setSkue(item.getSkue());
		i.setSort(item.getSort());
		i.setStatus(item.getStatus());
		i.setType(item.getType());
		
		return i;
	}
}
