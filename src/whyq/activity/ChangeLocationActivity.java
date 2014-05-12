/**
 * Copyright (c)  TMA Mobile Solutions, TMA Solutions company. All Rights Reserved.
 * This software is the confidential and proprietary information of TMA Solutions, 
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with TMA Solutions.
 *
 *
 * TMA SOLUTIONS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, 
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TMA SOLUTIONS SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES
 */
package whyq.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;

import whyq.interfaces.IServiceListener;
import whyq.model.Location;
import whyq.service.ParseChangedLocationXMLFile;
import whyq.service.Service;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract.Calendars;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

/**
 * Class description: TODO
 * 
 * @created Jan 12, 2011
 * @version v2.0
 * @author dvthien
 * @copyright TMA Solutions, www.tmasolutions.com
 */
public class ChangeLocationActivity extends Activity implements
		IServiceListener {

	private EditText etSearchLocation;
	private String locationToChange;

	public ParseChangedLocationXMLFile tfh;
	private ListView lvLocation;
	private EfficientAdapter adapter;

	ArrayList<Location> locationList = new ArrayList<Location>();
	private ProgressBar progressBar;
	private Button btnDelete;
	private Calendar calendar;
	private long now = 0;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.changelocation);

		calendar = Calendar.getInstance();
		etSearchLocation = (EditText) findViewById(R.id.etTextSearch);
		lvLocation = (ListView) findViewById(R.id.lwLocationToChange);
		btnDelete = (Button) findViewById(R.id.btnCancel);
		progressBar = (ProgressBar) findViewById(R.id.prgBar);
		etSearchLocation
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							performSearch();
							return true;
						}
						// TODO Auto-generated method stub
						return false;
					}
				});
		etSearchLocation.addTextChangedListener(mTextEditorWatcher);
		// End update new location

		lvLocation.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Location location = (Location) view.getTag();
				etSearchLocation.setText(location.getName());

				AlertDialog.Builder builder = new AlertDialog.Builder(
						ChangeLocationActivity.this);
				builder.setMessage(
						"              Confirm\nAre you sure you want to change your location?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										ListActivity.latgitude = location
												.getLat();
										ListActivity.longitude = location
												.getLon();
										ListActivity.currentLocation = location
												.getName();

										Intent intent = new Intent();
										intent.putExtra("lat",
												location.getLat());
										intent.putExtra("lng",
												location.getLon());
										intent.putExtra("name",
												location.getName());
										intent.putExtra("country",
												location.getCountry());
										setResult(RESULT_OK, intent);

										finish();

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				AlertDialog alert = builder.show();
			}
		});

	}

	public void onCancelClicked(View v) {
		etSearchLocation.setText("");
		hideBtnCancel();
	}

	private void hideBtnCancel() {
		// TODO Auto-generated method stub
		btnDelete.setVisibility(View.GONE);
	}

	public void performSearch() {
		try {
			URL url;
			String queryString1 = "http://ws.geonames.org/search?q="
					+ etSearchLocation.getText().toString()
					+ "&style=full&maxRows=10";
			String queryString = queryString1.replace(" ", "+");
			url = new URL(queryString);
			Service service = new Service(ChangeLocationActivity.this);
			service.setLocation(etSearchLocation.getText().toString());
			showProgress();
			// tfh = new ParseChangedLocationXMLFile();
			// /* Get a SAXParser from the SAXPArserFactory. */
			// SAXParserFactory spf = SAXParserFactory.newInstance();
			// SAXParser sp = spf.newSAXParser();
			//
			// /* Get the XMLReader of the SAXParser we created. */
			// XMLReader xr = sp.getXMLReader();
			// xr.setContentHandler(tfh);
			// xr.parse(new InputSource(url.openStream()));
			// ParseChangedLocationXMLFile parseLocation = new
			// ParseChangedLocationXMLFile();
			// locationList = tfh.getResults();

		}

		catch (Exception e) {
			etSearchLocation.setText(e.getMessage());
		}

	}

	private final TextWatcher mTextEditorWatcher = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// exeSearchFocus();
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// This sets a textview to the current length

			Log.d("onTextChanged", before + " fdfd " + start);

		}

		public void afterTextChanged(Editable s) {
			try {
				final Editable s2 = s;
//				new Handler().postDelayed(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						calendar = Calendar.getInstance();
//						long now2 = calendar.getTimeInMillis();
//						if (now == 0) {
//							now = now2;
//						}
//						long a = now2 - now;
//						Log.d("afterTextChanged", now2 + " and " + now + "===" + a);
//
//						if (a > 1500) {
//							now = now2;
							String text = s2.toString();
							Log.d("Text serch", "Text " + text);

							if (text.equals("")) {
								exeDisableSearchFocus();

							} else {
								exeSearchFocus();
								performSearch();
							}
							now = 0;
//						}else{
//							now = now2;
//						}
//					}
//				}, 1500);

			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};

	private void showProgress() {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.VISIBLE);
	}

	protected void exeSearchFocus() {
		// TODO Auto-generated method stub
		btnDelete.setVisibility(View.VISIBLE);
	}

	protected void exeDisableSearchFocus() {
		// TODO Auto-generated method stub
		btnDelete.setVisibility(View.GONE);
	}

	private void hideProgress() {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.INVISIBLE);
	}

	public void updateListView() {
		adapter = new EfficientAdapter(ChangeLocationActivity.this,
				locationList);
		lvLocation.setAdapter(adapter);
	}

	public static class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<Location> locationList;

		public EfficientAdapter(Context context, ArrayList<Location> list) {
			mInflater = LayoutInflater.from(context);
			locationList = list;
		}

		public int getCount() {
			return locationList.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Location location = locationList.get(position);
			ViewHolder holder;
			if (convertView == null) {

				convertView = mInflater.inflate(
						R.layout.listviewchangelocation, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.tvName);
				Util.applyTypeface(holder.name, Util.sTypefaceRegular);
				holder.name.setText(location.getName());
				convertView.setTag(location);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		static class ViewHolder {
			TextView name;
		}
	}

	/**
	 * @Description TODO
	 * 
	 */
	public String locationToChange(String newLocation) {
		// TODO Auto-generated method stub

		setLocationToChange(newLocation);
		return newLocation;
	}

	public String getLocationToChange() {
		return locationToChange;
	}

	public void setLocationToChange(String locationToChange) {
		this.locationToChange = locationToChange;
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto- generated method stub
		hideProgress();
		locationList = (ArrayList<Location>) result.getData();
		if (locationList != null)
			updateListView();
	}

}
