/**
 * 
 */
package whyq.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * @author Linh Nguyen
 *
 */
public class AdapterWrapper extends BaseAdapter {

	private ListAdapter wrapped = null;
	
	public AdapterWrapper(ListAdapter wrapped) {
		this.wrapped = wrapped;
		wrapped.registerDataSetObserver(new DataSetObserver() {

			/* (non-Javadoc)
			 * @see android.database.DataSetObserver#onChanged()
			 */
			@Override
			public void onChanged() {
				AdapterWrapper.this.notifyDataSetChanged();
			}

			/* (non-Javadoc)
			 * @see android.database.DataSetObserver#onInvalidated()
			 */
			@Override
			public void onInvalidated() {
				AdapterWrapper.this.notifyDataSetInvalidated();
			}			
		});
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.wrapped.getCount();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return this.wrapped.getItem(position);
	}
	
	public int getViewTypeCount() {
		return this.wrapped.getViewTypeCount();
	}
	
	public int getItemViewType(int position) {
		return this.wrapped.getItemViewType(position);
	}
	
	public boolean areAllItemsEnabled() {
		return this.wrapped.areAllItemsEnabled();
	}

	public boolean isEnabled(int position) {
		return this.wrapped.isEnabled(position);
	}
	
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return this.wrapped.getItemId(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return this.wrapped.getView(position, convertView, parent);
	}

	protected ListAdapter getWrappedAdapter() {
		return this.wrapped;
	}
}
