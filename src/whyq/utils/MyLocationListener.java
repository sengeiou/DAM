package whyq.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import whyq.WhyqApplication;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {

	double longitude;
	public static double latitude;
    @Override
    public void onLocationChanged(Location loc) {
//        Toast.makeText(
//                WhyqApplication.Instance().getApplicationContext(),
//                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                    + loc.getLongitude(), Toast.LENGTH_SHORT).show();
        longitude = loc.getLongitude();
        latitude = loc.getLatitude();
        /*-------to get City-Name from coordinates -------- */
        String cityName = null;
        Geocoder gcd = new Geocoder(WhyqApplication.Instance().getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0)
//                System.out.println(addresses.get(0).getLocality());
            cityName = addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
            + cityName;
        Log.d("Location info","Location: "+s);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}