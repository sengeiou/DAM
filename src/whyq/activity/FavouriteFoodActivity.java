package whyq.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import whyq.adapter.FavouriteFoodAdapter;
import whyq.interfaces.IServiceListener;
import whyq.model.Product;
import whyq.model.ProductGroup;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dealadelivery.whyq.R;

public class FavouriteFoodActivity extends FragmentActivity implements
		IServiceListener {
	ProgressBar pgbLoading;
	ExpandableListView mListView;
	ArrayList<ProductGroup> productGroups;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_favourite_foods);
		// set header title
		TextView tvHeader = ((TextView) findViewById(R.id.tvHeaderTittle));
		tvHeader.setText(getResources().getString(R.string.favorite_foods));
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvHeader
				.getLayoutParams();
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// set loading
		pgbLoading = (ProgressBar) findViewById(R.id.prgBar);
		pgbLoading.setVisibility(View.VISIBLE);

		mListView = (ExpandableListView) findViewById(R.id.lv_favourite_foods);
		mListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// do nothing
				return true;
			}
		});

		// get favourite foods
		getData();
	}

	private void getData() {
		new Service(this).getFavouriteFoods(1);
	}

	private void setListViewAdapter(ArrayList<Product> productList) {
		if (productList != null && productList.size() > 0) {
			mListView.setAdapter(new FavouriteFoodAdapter(
					getApplicationContext(), initProductGroup(productList)));

			for (int i = 0; i < productGroups.size(); i++) {
				mListView.expandGroup(i);
			}
		}
	}

	private ArrayList<ProductGroup> initProductGroup(
			ArrayList<Product> productList) {
		HashMap<String, ArrayList<Product>> hm = new HashMap<String, ArrayList<Product>>();
		if (productList != null) {
			Product product;
			for (int i = 0; i < productList.size(); i++) {
				product = productList.get(i);
				if (!hm.containsKey(product.groupItemName)) {
					ArrayList<Product> list = new ArrayList<Product>();
					list.add(product);
					hm.put(product.groupItemName, list);
				} else {
					hm.get(product.groupItemName).add(product);
				}
			}
		}

		productGroups = convertHashMapToArray(hm);
		return productGroups;
	}

	private ArrayList<ProductGroup> convertHashMapToArray(
			HashMap<String, ArrayList<Product>> hm) {
		ArrayList<ProductGroup> productGroups = new ArrayList<ProductGroup>();
		String[] keySet =  hm.keySet().toArray(new String[hm.size()]);
		ProductGroup group;
		for (int i = 0; i < keySet.length; i++) {
			group = new ProductGroup();
			group.groupName = keySet[i];
			group.productList = hm.get(keySet[i]);

			productGroups.add(group);
		}

		return productGroups;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetFavouriteFoods) {
			pgbLoading.setVisibility(View.GONE);
			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
				setListViewAdapter((ArrayList<Product>) data.getData());

			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else if (data.getStatus().equals("204")) {

			} else {
				Util.showDialog(getParent(), data.getMessage());
			}
		}
	}
	
	public void onBack(View v){
		finish();
	}
}
