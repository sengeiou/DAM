package whyq.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whyq.R;

public class ExampleAdapter extends BaseExpandableListAdapter {

    private final String TAG = "ExpAdapter";
    private Context context;
    static final String arrGroupelements[] = {"Australia", "England", "South Africa","India"};
    static final String arrChildelements[][] = { {"Sachin Tendulkar", "Raina", "Dhoni", "Yuvi" },
                                                 {"Ponting", "Adam Gilchrist", "Michael Clarke"},
                                                 {"Andrew Strauss", "kevin Peterson", "Nasser Hussain"},
                                                 {"Graeme Smith", "AB de villiers", "Jacques Kallis"} };

    public ExampleAdapter(Context context) {
        this.context = context;

        Log.i(TAG, "Adapter created.");
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.whyq_item_1, null);
        }

        TextView tvItem = (TextView) convertView.findViewById(R.id.tvType);

        tvItem.setText(arrChildelements[groupPosition][childPosition]);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
    	Log.i(TAG, "Count created."+arrChildelements[groupPosition].length);
        return arrChildelements[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return arrGroupelements.length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView tvItem = (TextView) convertView.findViewById(R.id.group_title);

        tvItem.setText(arrGroupelements[groupPosition]);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}