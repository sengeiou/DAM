package whyq.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import whyq.WhyqApplication;
import whyq.activity.FavouriteActivity;
import whyq.activity.ListActivity;
import whyq.activity.ListDetailActivity;
import whyq.adapter.WhyqAdapter.ViewHolder;
import whyq.model.Store;
import whyq.model.UserCheckBill;
import whyq.utils.UrlImageViewHelper;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.dam.R;

public class MapsActivity extends FragmentActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener {

	// Define elements
	private GoogleMap mMap;
	ArrayList<LatLng> markerPoints;
	private Marker mSydney;
	private HashMap<String, String> myMaker;
	private LatLng HOTEL_LOCAL = null;
	// private static final LatLng MY_HOME = new LatLng(10.72277, 106.710235);
	public static final String TAG_BUNDLEBRANCH = "branch_data";
	public static final String TAG_HOTELLAT = "lat";
	public static final String TAG_HOTELLON = "lon";
	public static final String TAG_HOTELTITLE_EN = "title_en";
	public static final String TAG_HOTELADDRESS_EN = "address_en";
	public static final String TAG_HOTELPHONE = "phone";
	public static final String TAG_HOTELFAX = "fax";
	public static final String TAG_HOTELEMAIL_EN = "email_en";
	public static final String TAG_HOTELICON = "thumbnail";

	// Dialog elements
	private Double lon, lat;
	private String title_en, address_en, phone, fax, email_en;
	private TextView tvHeader;
	private Context context;
	private Button btnDirection;

	/** Demonstrates customizing the info window and/or its contents. */
	class CustomInfoWindowAdapter implements InfoWindowAdapter {

		// These a both viewgroups containing an ImageView with id "badge" and
		// two TextViews with id
		// "title" and "snippet".
		private final View mWindow;

		CustomInfoWindowAdapter() {
			mWindow = getLayoutInflater().inflate(R.layout.custom_info_window,
					null);
		}

		@Override
		public View getInfoWindow(Marker marker) {
			render(marker, mWindow, myMaker);
			return mWindow;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}

		private void render(Marker marker, View view,
				HashMap<String, String> makerDetails) {

			// Init markerPoint
			markerPoints = new ArrayList<LatLng>();

			// Define elements
			TextView title = ((TextView) view.findViewById(R.id.title));
			TextView address = ((TextView) view.findViewById(R.id.address));
			TextView phone = ((TextView) view.findViewById(R.id.phone));
			TextView fax = ((TextView) view.findViewById(R.id.fax));
			TextView email = ((TextView) view.findViewById(R.id.email));
			Button btnDirection = ((Button) view
					.findViewById(R.id.btnDirection));

			// Put value to elements
			title.setText(makerDetails.get(TAG_HOTELTITLE_EN));
			address.setText(makerDetails.get(TAG_HOTELADDRESS_EN));
			phone.setText("Phone: " + makerDetails.get(TAG_HOTELPHONE));
			fax.setText("Fax: " + makerDetails.get(TAG_HOTELFAX));
			email.setText("Email: " + makerDetails.get(TAG_HOTELEMAIL_EN));

			btnDirection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					markerPoints = new ArrayList<LatLng>();
//					markerPoints.clear();
					mMap.clear();

					// Adding new item to the ArrayList
					LatLng myLocation = new LatLng(mMap.getMyLocation()
							.getLatitude(), mMap.getMyLocation().getLongitude());
					LatLng desLocation = HOTEL_LOCAL;

					// add to marker array
					for (int i = 0; i <= 1; i++) {
						switch (i) {
						case 0:
							// Creating MarkerOptions
							MarkerOptions options = new MarkerOptions();
							markerPoints.add(myLocation);
							// Setting the position of the marker
							options.position(myLocation);
							options.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
							// Add new marker to the Google Map Android API V2
							mMap.addMarker(options);
							break;

						case 1:
							// Creating MarkerOptions
							MarkerOptions options2 = new MarkerOptions();
							markerPoints.add(desLocation);
							// Setting the position of the marker
							options2.position(desLocation);
							options2.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_RED));
							// Add new marker to the Google Map Android API V2
							mMap.addMarker(options2);
							break;

						default:
							break;
						}
					}

					// Checks, whether start and end locations are captured
					if (markerPoints.size() >= 2) {
						LatLng origin = markerPoints.get(0);
						LatLng dest = markerPoints.get(1);

						// Getting URL to the Google Directions API
						String url = getDirectionsUrl(origin, dest);

						DownloadTask downloadTask = new DownloadTask();

						// Start downloading json data from Google Directions
						// API
						downloadTask.execute(url);
					}
				}
			});
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.RED);

			}

			// Drawing polyline in the Google Map for the i-th route
			mMap.addPolyline(lineOptions);
		}
	}

	public void goBack(View v) {
		MapsActivity.this.finish();
	}

	private void addMarkersToMap() {

		// define LetLng variable
		// change LatLng variable to value get from server

		// Uses a custom icon.
		mSydney = mMap.addMarker(new MarkerOptions()
				.position(HOTEL_LOCAL)
				.title("")
				.snippet("")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

		// Creates a marker rainbow demonstrating how to create default marker
		// icons of different
		// hues (colors).
		// int numMarkersInRainbow = 12;
		// for (int i = 0; i < numMarkersInRainbow; i++) {
		// mMap.addMarker(new MarkerOptions()
		// .position(new LatLng(
		// -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
		// 135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
		// .title("Marker " + i)
		// .icon(BitmapDescriptorFactory.defaultMarker(i * 360 /
		// numMarkersInRainbow)));
		// }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps_main);

		// Loader image - will be shown before loading image
		// ImageLoader class instance
		tvHeader = (TextView)findViewById(R.id.tvHeaderTitle);
		tvHeader.setText("Map");
		
		findViewById(R.id.btnDistance).setVisibility(View.INVISIBLE);
		ImageLoader imgLoader = new ImageLoader(WhyqApplication
				.Instance().getApplicationContext());
		int loader = R.drawable.ic_launcher;
//		ImageView icon_hotel = (ImageView) findViewById(R.id.icon_hotel);
//		
//		// Init Hashmap here
		myMaker = new HashMap<String, String>();
		Bundle bundle = getIntent().getBundleExtra(TAG_BUNDLEBRANCH);
//		if(bundle!=null){
//			if(bundle.getString(TAG_HOTELICON)!=null)
//			{
//				imgLoader.DisplayImage(
//						bundle.getString(TAG_HOTELICON), loader,
//						icon_hotel);
//				
//			}
//		}
		
		if (bundle != null && bundle.getString(TAG_HOTELTITLE_EN) != null) {

			myMaker.put(TAG_HOTELTITLE_EN, bundle.getString(TAG_HOTELTITLE_EN));
			myMaker.put(TAG_HOTELADDRESS_EN,
					bundle.getString(TAG_HOTELADDRESS_EN));
			myMaker.put(TAG_HOTELPHONE, bundle.getString(TAG_HOTELPHONE));
			myMaker.put(TAG_HOTELFAX, bundle.getString(TAG_HOTELFAX));
			myMaker.put(TAG_HOTELEMAIL_EN, bundle.getString(TAG_HOTELEMAIL_EN));

			try {

				if (bundle.getString(TAG_HOTELLAT) != null
						&& bundle.getString(TAG_HOTELLON) != null) {
					Double lat = Double.parseDouble(bundle
							.getString(TAG_HOTELLAT));
					Double lon = Double.parseDouble(bundle
							.getString(TAG_HOTELLON));
					HOTEL_LOCAL = new LatLng(lat, lon);
				} else {
					HOTEL_LOCAL = new LatLng(10.72277, 106.710235);
//					Toast.makeText(this, "Cannot find location", Toast.LENGTH_SHORT).show();
				}

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			setUpMapIfNeeded();

		} else {
			setUpMapIfNeeded();
			// ERROR HANDLING - put lat lng of default location in case no data
			// found
			HOTEL_LOCAL = new LatLng(10.72277, 106.710235);
			Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
			
		}
		context = MapsActivity.this;
		Store store = (Store)getIntent().getSerializableExtra("store");
		btnDirection = (Button)findViewById(R.id.btnDone);
		btnDirection.setText("Direction");
		if(store!=null)
			bindStoreInfo(store);
	}

	private void bindStoreInfo(final Store store) {
		// TODO Auto-generated method stub
		Store item = store;
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.id = item.getStoreId();
		viewHolder.imgThumb = (ImageView) findViewById(R.id.imgThumbnal2);
		viewHolder.tvItemName = (TextView)findViewById(R.id.tvItemName);
		viewHolder.tvItemAddress = (TextView)findViewById(R.id.tvItemAddress);
		viewHolder.tvNumberFavourite = (TextView)findViewById(R.id.tvNumberFavourite);
		viewHolder.tvVisited = (TextView)findViewById(R.id.tvVisited);
		viewHolder.tvDiscoutNumber = (TextView)findViewById(R.id.tvNumberDiscount);
		viewHolder.btnDistance = (Button)findViewById(R.id.btnDistance);
		viewHolder.imgFavouriteThumb = (ImageView)findViewById(R.id.imgFavourite);
		viewHolder.prgFavourite = (ProgressBar)findViewById(R.id.prgFavourite);
		viewHolder.rlDiscount = (RelativeLayout)findViewById(R.id.rlDiscount);
		viewHolder.tvItemName.setText(item.getNameStore().toUpperCase());
		viewHolder.tvItemAddress.setText(item.getAddress());
		viewHolder.tvNumberFavourite.setText(""+item.getCountFavaouriteMember());
		if(item.getPromotionList().size() > 0){
			viewHolder.tvDiscoutNumber.setVisibility(View.VISIBLE);
//			promotion = item.getPromotionList().get(0);
//			viewHolder.tvDiscoutNumber.setText(""+promotion.getValuePromotion()+promotion.getTypeValue()+ " for bill over $"+promotion.getConditionPromotion());
		}else{
			viewHolder.rlDiscount.setVisibility(View.GONE);
			
		}
		
		if(!item.getDistance().equals(""))
			viewHolder.btnDistance.setText((int)Float.parseFloat(item.getDistance())+" km");
		viewHolder.imgFriendThumb = (ImageView)findViewById(R.id.imgFriendThumb);
		
		UserCheckBill userCheckBill = item.getUserCheckBill();
		if(userCheckBill !=null){
			if(userCheckBill.getTotalMember()!=null && !userCheckBill.getTotalMember().equals("")){
				if(userCheckBill.getAvatar()!=null && !userCheckBill.getAvatar().equals("")){
					if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
						viewHolder.tvVisited.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" others visited");	
					}else{
						viewHolder.tvVisited.setText(userCheckBill.getFirstName()+" "+userCheckBill.getLastName()+ " & "+userCheckBill.getTotalMember()+" other visited");
					}
					
					viewHolder.imgFriendThumb.setVisibility(View.VISIBLE);
//					UrlImageViewHelper.setUrlDrawable(viewHolder.imgFriendThumb, userCheckBill.getAvatar());
					WhyqApplication.Instance().getImageLoader().DisplayImage(userCheckBill.getAvatar(),viewHolder.imgFriendThumb);
				}else{
					if(Integer.parseInt(userCheckBill.getTotalMember()) > 0){
						viewHolder.tvVisited.setText(userCheckBill.getTotalMember()+" others visited");
					}else{
						viewHolder.tvVisited.setText(userCheckBill.getTotalMember()+" other visited");
					}
					
				}

			}
		}

		if(item.getIsFavourite()){
			viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_enable);
		}else{
			viewHolder.imgFavouriteThumb.setImageResource(R.drawable.icon_fav_disable);
		}
		viewHolder.imgFavouriteThumb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if( FavouriteActivity.isFavorite)
					((FavouriteActivity)context).onFavouriteClicked(v);
				else
					((ListActivity)context).onFavouriteClicked(v);
			}
		});
//		UrlImageViewHelper.setUrlDrawable(viewHolder.imgThumb, item.getLogo());
		WhyqApplication.Instance().getImageLoader().DisplayImage(item.getLogo(),viewHolder.imgThumb);


		viewHolder.imgFavouriteThumb.setTag(item);
		viewHolder.btnDistance.setTag(item);
	}

	public void onDoneClicked(View v){

		// TODO Auto-generated method stub
		markerPoints = new ArrayList<LatLng>();
		mMap.clear();

		// Adding new item to the ArrayList
		LatLng myLocation;
		if(mMap.getMyLocation()!=null){
			myLocation = new LatLng(mMap.getMyLocation()
					.getLatitude(), mMap.getMyLocation().getLongitude());
			LatLng desLocation = HOTEL_LOCAL;

			// add to marker array
			for (int i = 0; i <= 1; i++) {
				switch (i) {
				case 0:
					// Creating MarkerOptions
					MarkerOptions options = new MarkerOptions();
					markerPoints.add(myLocation);
					// Setting the position of the marker
					options.position(myLocation);
					options.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					// Add new marker to the Google Map Android API V2
					mMap.addMarker(options);
					break;

				case 1:
					// Creating MarkerOptions
					MarkerOptions options2 = new MarkerOptions();
					markerPoints.add(desLocation);
					// Setting the position of the marker
					options2.position(desLocation);
					options2.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					// Add new marker to the Google Map Android API V2
					mMap.addMarker(options2);
					break;

				default:
					break;
				}
			}

			// Checks, whether start and end locations are captured
			if (markerPoints.size() >= 2) {
				LatLng origin = markerPoints.get(0);
				LatLng dest = markerPoints.get(1);

				// Getting URL to the Google Directions API
				String url = getDirectionsUrl(origin, dest);

				DownloadTask downloadTask = new DownloadTask();

				// Start downloading json data from Google Directions
				// API
				downloadTask.execute(url);
			}
		}else{
			Toast.makeText(context, "Can not get your location for know\nTry again!", Toast.LENGTH_LONG).show();
		}

	
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();

//		HOTEL_LOCAL = new LatLng(10.72277, 106.710235);

		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Enable MyLocation Button in the Map
		mMap.setMyLocationEnabled(true);

		// Add lots of markers to the map.
		addMarkersToMap();

		// Setting an info window adapter allows us to change the both the
		// contents and look of the
		// info window.
		mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

		// Set listeners for marker events. See the bottom of this class for
		// their behavior.
		mMap.setOnMarkerClickListener(this);
		mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);

		// Pan to see all markers in view.
		// Cannot zoom to bounds until the map has a size.
		final View mapView = getSupportFragmentManager().findFragmentById(
				R.id.map).getView();
		if (mapView.getViewTreeObserver().isAlive()) {
			mapView.getViewTreeObserver().addOnGlobalLayoutListener(
					new OnGlobalLayoutListener() {
						@SuppressWarnings("deprecation")
						// We use the new method when supported
						@SuppressLint("NewApi")
						// We check which build version we are using.
						@Override
						public void onGlobalLayout() {
							LatLngBounds bounds = new LatLngBounds.Builder()
									.include(HOTEL_LOCAL).build();
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
								mapView.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							} else {
								mapView.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							}
							mMap.moveCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));
							mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
									HOTEL_LOCAL, 15));

							// Data will be loaded from sqlite database and
							// added to Hashmap
							myMaker.put(TAG_HOTELTITLE_EN,
									myMaker.get(TAG_HOTELTITLE_EN));
							myMaker.put(TAG_HOTELADDRESS_EN,
									myMaker.get(TAG_HOTELADDRESS_EN));
							myMaker.put(TAG_HOTELPHONE,
									myMaker.get(TAG_HOTELPHONE));
							myMaker.put(TAG_HOTELFAX, myMaker.get(TAG_HOTELFAX));
							myMaker.put(TAG_HOTELEMAIL_EN,
									myMaker.get(TAG_HOTELEMAIL_EN));
						}
					});
		}
	}

	//
	// Marker related listeners.
	//

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// This causes the marker at Perth to bounce into position when it is
		// clicked.
		// We return false to indicate that we have not consumed the event and
		// that we wish
		// for the default behavior to occur (which is for the camera to move
		// such that the
		// marker is centered and for the marker's info window to open, if it
		// has one).
		return false;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// Toast.makeText(getBaseContext(), "Click Info Window",
		// Toast.LENGTH_SHORT).show();

		markerPoints.clear();
		mMap.clear();

		// Adding new item to the ArrayList
		LatLng myLocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap
				.getMyLocation().getLongitude());
		LatLng desLocation = HOTEL_LOCAL;

		// add to marker array
		for (int i = 0; i <= 1; i++) {
			switch (i) {
			case 0:
				// Creating MarkerOptions
				MarkerOptions options = new MarkerOptions();
				markerPoints.add(myLocation);
				// Setting the position of the marker
				options.position(myLocation);
				options.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				// Add new marker to the Google Map Android API V2
				mMap.addMarker(options);
				break;

			case 1:
				// Creating MarkerOptions
				MarkerOptions options2 = new MarkerOptions();
				markerPoints.add(desLocation);
				// Setting the position of the marker
				options2.position(desLocation);
				options2.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				// Add new marker to the Google Map Android API V2
				mMap.addMarker(options2);
				break;

			default:
				break;
			}
		}

		// Checks, whether start and end locations are captured
		if (markerPoints.size() >= 2) {
			LatLng origin = markerPoints.get(0);
			LatLng dest = markerPoints.get(1);

			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(origin, dest);

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
	}

	@Override
	public void onMarkerDrag(Marker marker) {
	}

	public void onBack(View v){
		finish();
	}
}
