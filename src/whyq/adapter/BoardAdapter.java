package whyq.adapter;

import java.util.ArrayList;

import whyq.model.WhyqBoard;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.whyq.R;

public class BoardAdapter extends ArrayAdapter<WhyqBoard> {

	private ArrayList<WhyqBoard> items;
	
	private int textViewResourceId ;
	private Context context;

	public BoardAdapter(Context context, int textViewResourceId, ArrayList<WhyqBoard> items) {
		super(context, textViewResourceId, items);
		this.context = context;
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
		
		if( position == 0 ){
			v.findViewById(R.id.categoryItemLayout).setBackgroundResource( R.drawable.explorer_item_bg_first );
		} else if( position == ( items.size() - 1 ) ) {
			v.findViewById(R.id.categoryItemLayout).setBackgroundResource( R.drawable.explorer_item_bg_end );
		} else {
			v.findViewById(R.id.categoryItemLayout).setBackgroundResource( R.drawable.explorer_item_bg );
		}
		
		WhyqBoard o = items.get(position);
		if (o != null) {
			TextView an = (TextView) v.findViewById(R.id.categoryName);
			an.setText(o.getName());
			
			TextView boardStat = (TextView) v.findViewById(R.id.boardStat);
			if( boardStat != null ){
				String str = context.getString(R.string.perm) +" "+ String.valueOf(o.getPins()) + " "+context.getString(R.string.followers)+" " + String.valueOf(o.getFollowers());
				boardStat.setText(str);
			}
		}
		
		return v;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public WhyqBoard getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}