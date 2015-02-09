package whyq.adapter;

import java.util.ArrayList;
import java.util.List;

import whyq.interfaces.OnnOptionItemSelected;
import whyq.model.ExtraItem;
import whyq.model.OptionItem;
import whyq.model.SizeItem;
import android.content.Context;

public class SizeItemAdapter extends OptionItemBasicAdapter {

	private OnnOptionItemSelected mOnOptionItemSelected;


	public SizeItemAdapter(Context context, int resource, List<SizeItem> objects) {

		super(context, resource, convertSizeToOption(objects));
		// TODO Auto-generated constructor stub
	}

	public void updateData(List<SizeItem> list) {
		// TODO Auto-generated method stub
		super.setDatas(convertSizeToOption(list));
	}
	
	public void setDelegate(OnnOptionItemSelected listener){
		mOnOptionItemSelected = listener;
	}
	
	private static List<OptionItem> convertSizeToOption(List<SizeItem> objects) {
		// TODO Auto-generated method stub
		List<OptionItem> result = new ArrayList<OptionItem>();
		for (SizeItem item : objects) {
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
//		SizeItem i = convertOptionItemToSizeItem(item);
//		
//		mOnOptionItemSelected.onSelected(0, i);
//		
//		notifyDataSetChanged();
//	}
	
	public static SizeItem convertOptionItemToSizeItem(OptionItem item){
		SizeItem i = new SizeItem();
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
