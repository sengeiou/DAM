/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package whyq.utils.location;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gso.gpstracker.GPSTracker;
import com.whyq.R;

/**
 * This the app's main Activity. It provides buttons for requesting the various features of the
 * app, displays the current location, the current address, and the status of the location client
 * and updating services.
 *
 * {@link #getLocation} gets the current location using the Location Services getLastLocation()
 * function. {@link #getAddress} calls geocoding to get a street address for the current location.
 * {@link #startUpdates} sends a request to Location Services to send periodic location updates to
 * the Activity.
 * {@link #stopUpdates} cancels previous periodic update requests.
 *
 * The update interval is hard-coded to be 5 seconds.
 */
public class LocationActivityNew extends FragmentActivity {

    protected static final int ENABLE_GPS = 0;
	private ProgressBar mActivityIndicator;
    boolean mUpdatesRequested = false;
	private GPSTracker gps;

	public static Location currentLocation;

    /*
     * Initialize the Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        getLocation();
        
    }

	public void showGPSDisabledAlertToUser(final Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivityForResult(callGPSSettingIntent, ENABLE_GPS);
								dialog.cancel();
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
    	Log.d("onActivityResult","onActivityResult"+resultCode);
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:
                        Log.d(LocationUtils.APPTAG, getString(R.string.resolved));
                    break;
                    default:
                        Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));

                    break;
                }
            case ENABLE_GPS:
            	switch (resultCode) {
				case RESULT_OK:
					getLocation();
					break;
					
				default:
					finishActivity();
					break;
				}
            // If any other request code was received
            default:
               // Report that this Activity received an unknown requestCode
               Log.d(LocationUtils.APPTAG,
                       getString(R.string.unknown_activity_request_code, requestCode));

               break;
        }
    }

    public Location getLocation() {

   	 gps = new GPSTracker(getApplicationContext());	
		// check if GPS enabled		
	        if(gps.canGetLocation()){
	        	
	        	double latitude = gps.getLatitude();
	        	double longitude = gps.getLongitude();
	        	
	        	// \n is for new line
//	        	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
	        	if(latitude!=0.0){
	        		if(currentLocation==null)
	        			currentLocation = new Location("dummyprovider");
		        	currentLocation.setLatitude(latitude);
		        	currentLocation.setLongitude(longitude);
	        	}
	        	gps.stopUsingGPS();
	        	finishActivity();
	        }else{
	        	// can't get location
	        	// GPS or Network is not enabled
	        	// Ask user to enable GPS/network in settings
	        	showGPSDisabledAlertToUser(LocationActivityNew.this);
	        }
			
       return currentLocation;
    }

    public void finishActivity(){
    	Intent i = getIntent();
    	Log.d("LocationActivityNew","location: "+currentLocation);
    	i.putExtra("have_location", currentLocation!=null?true:false);
    	setResult(RESULT_OK,i);
    	finish();
    }
	
  
  
  

    public void onCloseClicked(View v){
    	finish();
    }
}
