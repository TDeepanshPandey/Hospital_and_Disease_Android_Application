package com.example.medicalemergency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SearchNew extends FragmentActivity implements LocationListener {

	GoogleMap mGoogleMap;
	String data = "";
	double mLatitude=0;
	double mLongitude=0;
	Location location;


	HashMap<String, String> mMarkerPlaceLink = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_hospital);
		Button btnFind;

		// Getting reference to Find Button
		btnFind = ( Button ) findViewById(R.id.btn1);

		// Getting Google Play availability status
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available	
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}else { // Google Play Services are available

			// Getting reference to the SupportMapFragment
			SupportMapFragment fragment = ( SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

			// Getting Google Map
			mGoogleMap = fragment.getMap();

			// Enabling MyLocation in Google Map
			mGoogleMap.setMyLocationEnabled(true);


			SupportMapFragment sf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			mGoogleMap = sf.getMap();
			mGoogleMap.setMyLocationEnabled(true);
			LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
			Criteria c = new Criteria();
			String provider = lm.getBestProvider(c, true);
			GPSTracker gpsTracker = new GPSTracker(SearchNew.this);


			location = gpsTracker.getLocation();
			System.out.println("==========loc............." + location);
			if (location != null) {
				onLocationChanged(location);
				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
			}

			btnFind.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mGoogleMap.clear();
					/*MarkerOptions marker=new MarkerOptions().position(new LatLng(28.704104,77.2186001)).title("INMAS");
					marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					mGoogleMap.addMarker(marker);
					MarkerOptions marker1=new MarkerOptions().position(new LatLng(28.70011,77.20825)).title("Hans Charitable");
					marker1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					mGoogleMap.addMarker(marker1);
					MarkerOptions marker2=new MarkerOptions().position(new LatLng(28.71128,77.21506)).title("Durga Hospital");
					marker2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
					mGoogleMap.addMarker(marker2);*/
					StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
					sb.append("location="+mLatitude+","+mLongitude);
					sb.append("&radius=3000");
					sb.append("&types=hospital");
					sb.append("&sensor=true");
					sb.append("&key=");

					// Creating a new non-ui thread task to download Google place json data
					PlacesTask placesTask = new PlacesTask();

					// Invokes the "doInBackground()" method of the class PlaceTask
					placesTask.execute(sb.toString());

				}

			});

		}


	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException{

		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream,Charset.forName("UTF-8")));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while( ( line = br.readLine()) != null){
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		}catch(Exception e){
			data="Exception "+e;
		}finally{
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}
	/** A class, to download Google Places */
	private class PlacesTask extends AsyncTask<String, Integer, String>{

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){

			ParserTask parserTask = new ParserTask();

			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}
	private class PlacesTask1 extends AsyncTask<String, Integer, String>{

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){

			ParserTask1 parserTask = new ParserTask1();

			// Start parsing the Google places in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {

			List<HashMap<String, String>> places = null;
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try{
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a List construct */
				places = placeJsonParser.parse(jObject);

			}catch(Exception e){
				Log.d("Exception",e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){
			//	Toast.makeText(MainActivity.this, ""+data, Toast.LENGTH_LONG).show();


			for(int i=0;i<list.size();i++){

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);

				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat"));

				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng"));

				// Getting name
				String name = hmPlace.get("place_name");

				// Getting vicinity
				String vicinity = hmPlace.get("vicinity");

				LatLng latLng = new LatLng(lat, lng);

				// Setting the position for the marker
				markerOptions.position(latLng);

				// Setting the title for the marker.
				//This will be displayed on taping the marker
				markerOptions.title(name + " : " + vicinity);

				// Placing a marker on the touched position
				Marker m = mGoogleMap.addMarker(markerOptions);

				// Linking Marker id and place reference
				mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));
				StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
				sb.append("location="+mLatitude+","+mLongitude);
				sb.append("&radius=3000");
				sb.append("&types=doctor");
				sb.append("&sensor=true");
				sb.append("&key=");
				PlacesTask1 p=new PlacesTask1();
				p.execute(sb.toString());

			}
		}
	}
	private class ParserTask1 extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

		JSONObject jObject;

		// Invoked by execute() method of this object
		@Override
		protected List<HashMap<String,String>> doInBackground(String... jsonData) {

			List<HashMap<String, String>> places = null;
			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try{
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a List construct */
				places = placeJsonParser.parse(jObject);

			}catch(Exception e){
				Log.d("Exception",e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String,String>> list){
			//	Toast.makeText(MainActivity.this, ""+data, Toast.LENGTH_LONG).show();


			for(int i=0;i<list.size();i++){

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				// Getting a place from the places list
				HashMap<String, String> hmPlace = list.get(i);

				// Getting latitude of the place
				double lat = Double.parseDouble(hmPlace.get("lat"));

				// Getting longitude of the place
				double lng = Double.parseDouble(hmPlace.get("lng"));

				// Getting name
				String name = hmPlace.get("place_name");

				// Getting vicinity
				String vicinity = hmPlace.get("vicinity");

				LatLng latLng = new LatLng(lat, lng);

				// Setting the position for the marker
				markerOptions.position(latLng);

				// Setting the title for the marker.
				//This will be displayed on taping the marker
				markerOptions.title(name + " : " + vicinity);

				// Placing a marker on the touched position
				Marker m = mGoogleMap.addMarker(markerOptions);

				// Linking Marker id and place reference
				mMarkerPlaceLink.put(m.getId(), hmPlace.get("reference"));


			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		LatLng latLng = new LatLng(mLatitude, mLongitude);

		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
