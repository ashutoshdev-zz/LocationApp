package com.zakoopilocation;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GetLocationActivity extends FragmentActivity implements
		OnCancelListener {

	TextView acc;
	String city;
	TextView cityname;
	Button sendloc;
	String id;
	TextView lat;
	LocationListener ll;
	LocationManager lm;
	TextView lng;
	double longitude;
	TextView marketname;
	String slug;
	String smarket;
	String sname;
	TextView storename;
	static final int DEFAULT_TIMEOUT = 40000;
	private static String LOOKBOOK_URL = null;
	AsyncHttpClient client;
	Boolean isInternetPresent = false;
	ConnectionDetector cd;
	ProgressHUD mProgressHUD;
	private SharedPreferences pro_user_pref;
	String userid;
	String lati = "no", lngi = "no";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.detail_page);

		Intent in = getIntent();
		this.id = in.getStringExtra("sid");
		this.sname = in.getStringExtra("sname");
		this.smarket = in.getStringExtra("smarket");
		this.slug = in.getStringExtra("sslug");
		this.city = in.getStringExtra("scity");
		this.storename = (TextView) findViewById(R.id.textView1);
		this.marketname = (TextView) findViewById(R.id.textView2);
		this.cityname = (TextView) findViewById(R.id.textView3);
		this.lat = (TextView) findViewById(R.id.textView4);
		this.lng = (TextView) findViewById(R.id.textView5);
		this.acc = (TextView) findViewById(R.id.textView6);
		this.storename.setText(this.sname);
		this.marketname.setText("Market : " + this.smarket);
		this.cityname.setText("City : " + this.city);
		this.sendloc = (Button) findViewById(R.id.button1);
		cd = new ConnectionDetector(getApplicationContext());
		this.client = ClientHttp.getInstance(this);

		pro_user_pref = this.getSharedPreferences("User_detail", 0);
		userid = pro_user_pref.getString("user_id", "no");
		sendloc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isInternetPresent = cd.isConnectingToInternet();
				if (isInternetPresent) {
					if (lati.equals("no") || lngi.equals("no")) {

						Toast.makeText(getApplicationContext(),
								"Location not found try again ", 5000).show();
					}else{
						
						sendLocation();
					}
					
				} else {

					Toast.makeText(getApplicationContext(),
							"Internet is Slow /OFF ", 5000).show();
				}
			}
		});

	}

	@SuppressWarnings("deprecation")
	public void sendLocation() {

		// Log.e("GE_MAIN", edtgender.getText().toString());
		String profile_URL = getResources().getString(R.string.base_url)
				+ "stores/updateGpsCords/" + id + ".json";

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		Toast.makeText(getApplicationContext(), lati + "---" + lngi, 5000)
				.show();
		params.put("lat", lati);
		params.put("lng", lngi);
		/*
		 * params.put("lat", "28.56521207"); params.put("lng", "77.19104426");
		 */
		params.put("user_id", userid);

		client.post(profile_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				mProgressHUD = ProgressHUD.show(GetLocationActivity.this,
						"Processing...", true, true, GetLocationActivity.this);
				mProgressHUD.setCancelable(false);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				mProgressHUD.dismiss();

				Toast.makeText(getApplicationContext(),
						"Store Location Updated Succesfully", 5000).show();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

				mProgressHUD.dismiss();
				
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (((LocationManager) getSystemService("location"))
				.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			this.lm = (LocationManager) getSystemService("location");
			this.ll = new mylocationlistener();
			this.lm.requestLocationUpdates("gps", 0, 0.0f, this.ll);

		} else {
			showGPSDisabledAlertToUser();
		}

	//	this.sendloc.setEnabled(false);

	}

	private void showGPSDisabledAlertToUser() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Enable GPS",
						new android.content.DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialoginterface, int i) {
								Intent in = new Intent(
										"android.settings.LOCATION_SOURCE_SETTINGS");
								startActivity(in);
								dialoginterface.cancel();
							}

						});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialoginterface, int i) {
						dialoginterface.cancel();
					}

				});
		builder.create().show();
	}

	private class mylocationlistener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {

				if (location.getAccuracy() <= 10.0f) {

					lati = String.valueOf(location.getLatitude());
					lngi = String.valueOf(location.getLongitude());
					GetLocationActivity.this.lat.setText("Latitude : "
							+ location.getLatitude());
					GetLocationActivity.this.lng.setText("Longitude : "
							+ location.getLongitude());
					GetLocationActivity.this.acc.setText("Accuracy : "
							+ location.getAccuracy() + " m");
					GetLocationActivity.this.lm
							.removeUpdates(GetLocationActivity.this.ll);
					GetLocationActivity.this.lm = null;
					GetLocationActivity.this.sendloc.setEnabled(true);

				} else {

					try {
						GetLocationActivity.this.lat.setText("Latitude : "
								+ location.getLatitude());
						GetLocationActivity.this.lng.setText("Longitude : "
								+ location.getLongitude());
						GetLocationActivity.this.acc.setText("Accuracy : "
								+ location.getAccuracy() + " m");
						GetLocationActivity.this.sendloc.setEnabled(false);
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "exception1",
								5000).show();
					}
				}

			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(getApplicationContext(), "exception2", 5000).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "exception3", 5000).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Toast.makeText(getApplicationContext(), "exception4", 5000).show();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

}
