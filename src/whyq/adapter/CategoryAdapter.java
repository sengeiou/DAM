package whyq.adapter;

import java.util.ArrayList;

import whyq.model.Category;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whyq.R;

public class CategoryAdapter extends ArrayAdapter<Category> {

	private ArrayList<Category> items;
	
	private int textViewResourceId ;

	public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Category> items) {
		super(context, textViewResourceId, items);
		this.textViewResourceId = textViewResourceId;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if( v == null ){
			LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(this.textViewResourceId, null);
		}
		if(items != null){
			if( position == 0 ){
				v.findViewById(R.id.categoryItemLayout).setBackgroundResource( R.drawable.explorer_item_bg_first );
				
			} else if( position == ( items.size() - 1 ) ) {
				v.findViewById(R.id.categoryItemLayout).setBackgroundResource( R.drawable.explorer_item_bg_end );
			} else {
				v.findViewById(R.id.categoryItemLayout).setBackgroundResource( R.drawable.explorer_item_bg );
			}
			
			
			Category o = items.get(position);
			if (o != null) {
				TextView an = (TextView) v.findViewById(R.id.categoryName);
				an.setText(o.getName());
			}
			
		}
		
		return v;
	}

}