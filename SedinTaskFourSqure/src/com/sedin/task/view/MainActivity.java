package com.sedin.task.view;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.sedin.task.adapter.VenuAdapter;
import com.sedin.task.bussinessobjects.BussinessModel;
import com.sedin.task.bussinessobjects.VenueDetails;
import com.sedin.task.cinterface.CallBack;
import com.sedin.task.utils.HttpConnection;
import com.sedin.task.utils.JsonParser;
import com.sedin.task.utils.SharedPreference;

public class MainActivity extends FragmentActivity implements LocationListener,
		CallBack {
	ArrayList<VenueDetails> mList;

	String latitude = "";
	String longtitude = "";
	private ProgressBar mBar;
	private GoogleMap googleMap;
	Location mLocation;
	Set<String> mSet = new HashSet<>();
	BussinessModel model;

	enum RefrechView {
		MAP_VIEW, VENU_VIEW

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.venuImg:
			if (isOnline()) {
				initLayout(RefrechView.VENU_VIEW);
				initCurrentLocation();
			}

			break;
		case R.id.favoriteImg:
			initFavorates();

			break;
		case R.id.mapImg:
			if (mBar != null) {
				if (!mBar.isShown()) {
					initilizeMap();
				}
			}

			break;
		default:
			break;
		}
	}

	private void initFavorates() {

		ArrayList<VenueDetails> mDetails = new ArrayList<>();
		for (int i = 0; i < mSet.size(); i++) {

			mDetails.add(mList.get(i));
			Log.v("name", "get name" + mList.get(i).getName());

		}

		model.setContext(this);
		if (!mDetails.isEmpty()) {
			model.setmList(mDetails);
		}

		// initAdapter(model.getmList(),true);

		SharedPreference mPreference = new SharedPreference();
		if (!model.getmList().isEmpty()) {
			mPreference.saveFavorites(this, model.getmList());
			;

		}
		if (mPreference.getFavorites(this) != null) {
			initAdapter(mPreference.getFavorites(this), true);

		}

	}

	private void initAdapter(ArrayList<VenueDetails> mList, boolean isfavorate) {
		((ListView) findViewById(R.id.list)).setAdapter(new VenuAdapter(
				MainActivity.this, mList, MainActivity.this, isfavorate));

	}

	private void initProgesStatus() {
		mBar = (ProgressBar) findViewById(R.id.progress);
		if (mBar.getVisibility() == View.GONE) {
			mBar.setVisibility(View.VISIBLE);
		} else {
			mBar.setVisibility(View.GONE);
		}
	}

	private boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		}
		Toast.makeText(MainActivity.this, "Please check internet connection",
				Toast.LENGTH_LONG).show();
		return false;
	}

	private void initLayout(RefrechView type) {
		RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.mapLayout);
		switch (type) {
		case MAP_VIEW:
			mLayout.setVisibility(View.VISIBLE);
			((ListView) findViewById(R.id.list)).setVisibility(View.GONE);

			break;
		case VENU_VIEW:
			mLayout.setVisibility(View.GONE);
			((ListView) findViewById(R.id.list)).setVisibility(View.VISIBLE);

			break;

		}

	}

	@Override
	protected void onStart() {
		if (isOnline()) {
			initCurrentLocation();
		}
		model = (BussinessModel) getApplicationContext();

		super.onStart();
	}

	@SuppressLint("NewApi")
	private void initilizeMap() {
		initLayout(RefrechView.MAP_VIEW);

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		if (!mList.isEmpty()) {
			for (VenueDetails mDetails : mList) {

				googleMap.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.valueOf(mDetails.getLat()),
										Double.valueOf(mDetails.getLang())))
						.title(mDetails.getName()).snippet(mDetails.getCity()));
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(Double.valueOf(mDetails.getLat()), Double
								.valueOf(mDetails.getLang())), 16));
			}

		}

		if (googleMap == null) {
			Toast.makeText(getApplicationContext(),
					"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		}

	}

	private void initCurrentLocation() {

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		initProgesStatus();
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 1, 100, MainActivity.this);
			mLocation = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (mLocation != null) {
				initClient(String.valueOf(mLocation.getLatitude()),
						String.valueOf(mLocation.getLongitude()));
			}

		} else {
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1, 100, MainActivity.this);
			mLocation = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (mLocation != null) {
				initClient(String.valueOf(mLocation.getLatitude()),
						String.valueOf(mLocation.getLongitude()));
			}

		}

	}

	private void initClient(String lat, String longt) {
		latitude = lat;
		longtitude = longt;

		new Loading().execute();
	}

	private class Loading extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... urls) {
			Log.v("url", "=======");
			;
			return new HttpConnection()
					.httpConnection(getString(R.string.url)
							+ "KJATQDEKFHWQY0XIEP0MVUPBHFR0UUMPTGQ1E1OFCUMXNYHQ&client_secret=WIXQPUKFVIUEEETIQGYHNWME4B1B05SRE3I1BBIYMQUUCSKW%20&v=20130815&ll="
							+ latitude + "," + longtitude);

		}

		@Override
		protected void onPostExecute(String result) {

			if (result != null) {

				mList = new JsonParser().parseJson(result);
				Toast.makeText(MainActivity.this, "Completed process",
						Toast.LENGTH_LONG).show();
				Log.v("set adpter", "===========" + mList.size());
				initAdapter(mList, false);
			}
			initProgesStatus();

		}
	}

	@Override
	public void onLocationChanged(Location location) {

		if (mLocation == null) {
			latitude = String.valueOf(location.getLatitude());
			longtitude = String.valueOf(location.getLongitude());

			new Loading().execute();
		}

		// Log.v("g", msg);
		Toast.makeText(this, "Connecting to server...", Toast.LENGTH_LONG)
				.show();

	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "No network connection or GPS enabled",
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// Toast.makeText(this, "ssssssssssssss", Toast.LENGTH_LONG).show();

	}

	@Override
	public void setListion(String name, boolean isFavorate) {
		if (isFavorate) {
			mSet.add(name);
		}

	}
}
