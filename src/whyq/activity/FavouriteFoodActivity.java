package whyq.activity;

import java.util.ArrayList;

import whyq.controller.WhyqListController;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.model.Store;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;

import com.dam.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FavouriteFoodActivity extends FragmentActivity implements
		IServiceListener {
	ProgressBar pgbLoading;
	ExpandableListView mListView;

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
		//set loading
		pgbLoading = (ProgressBar) findViewById(R.id.prgBar);
		pgbLoading.setVisibility(View.VISIBLE);
		
		mListView = (ExpandableListView) findViewById(R.id.lv_favourite_foods);

		getData();
	}

	private void getData() {
		new Service().getFavouriteFoods(1);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetFavouriteFoods) {
			pgbLoading.setVisibility(View.GONE);
			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {

			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else if (data.getStatus().equals("204")) {

			} else {
				Util.showDialog(getParent(), data.getMessage());
			}
		}
	}
}
