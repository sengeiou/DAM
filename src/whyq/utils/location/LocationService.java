package whyq.utils.location;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

public class LocationService implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	LocationClient locationClient;
	private Context context;

	public LocationService(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
		locationClient = new LocationClient(context, this, this);
	}

	@Override
	public void onLocationChanged(android.location.Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	public android.location.Location getLocation() {

		// Get the current location
		android.location.Location currentLocation = locationClient
				.getLastLocation();
		return currentLocation;

	}

}
