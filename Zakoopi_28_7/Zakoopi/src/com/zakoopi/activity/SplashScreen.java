package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.database.HomeSearchAllProductDatabaseHandler;
import com.zakoopi.helper.HomeSearchSpinnerSectionStructure;
import com.zakoopi.search.AllProduct;
import com.zakoopi.search.Product;
import com.zakoopi.search.SearchUpdate;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.search.offerings;
import com.zakoopi.search.updateSearch;
import com.zakoopi.utils.ClientHttp;

@SuppressWarnings("deprecation")
public class SplashScreen extends Activity {

	private static String ALL_PRODUCT_REST_URL = "";
	private static String LAST_UPDATE_REST_URL = "";
	private static String MAIN_WEBSERVICE_REST_URL = "";
	// private static final String ALL_PRODUCT_REST_URL =
	// "http://v3.zakoopi.com/api/offerings.json";
	// private static final String LAST_UPDATE_REST_URL =
	// "http://v3.zakoopi.com/api/lastUpdates.json";
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	//private static String ALL_AREA_REST_URL = "";
	HomeSearchAllAreaDatabaseHandler home_search_area_db;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// Declaring a Location Manager
	protected LocationManager locationManager;

	// The minimum distance to change Updates in meters
	//private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	//private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	String market_name;
	String text = "";
	String line = "";
	List<AllProduct> list = new ArrayList<AllProduct>();
	HomeSearchAllProductDatabaseHandler home_search_product_db;
	String name, category;
	String[] sectionHeader = { "Men", "Women", "Kids" };

	ArrayList<HomeSearchSpinnerSectionStructure> sectionList = new ArrayList<HomeSearchSpinnerSectionStructure>();
	HomeSearchSpinnerSectionStructure spinner_section_strcture;
	List<String> productList = new ArrayList<String>();
	ArrayList<updateSearch> updateSearch = new ArrayList<updateSearch>();

	SharedPreferences prefs, prefs1, pref_location;
	Editor editor, editor_loc;

	String save_model_trends, save_time_trends;

	String model, time;
	String userid, city_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		home_search_product_db = new HomeSearchAllProductDatabaseHandler(
				getApplicationContext());

		prefs = getSharedPreferences("PREF", MODE_PRIVATE);
		prefs1 = getSharedPreferences("User_detail", 0);
		pref_location = getSharedPreferences("location", 1);
		editor = prefs.edit();
		editor_loc = pref_location.edit();

		ALL_PRODUCT_REST_URL = getString(R.string.base_url) + "offerings.json";

		model = prefs.getString("model", "NA");
		time = prefs.getString("time", "N/A");

		save_model_trends = prefs.getString("model_trends", "ghft");
		save_time_trends = prefs.getString("time_trends", "ghfty");

		userid = prefs1.getString("user_id", "NA");

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		city_name = pref_location.getString("city", "123");

		if (city_name.equals("123")) {

			getLocation();

		} else {

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					

				//	Log.e("HANDLE", city_name);
					MainWebservice();

				}
			}, 0);
		}

	}

	public void getLocation() {
		try {
			locationManager = (LocationManager) SplashScreen.this
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				showSettingsAlert();

			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {

					// Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();

							/*
							 * Toast.makeText(getApplicationContext(), latitude
							 * + ":::" + longitude, 5000).show();
							 */
							try {
								Geocoder geocoder = new Geocoder(
										SplashScreen.this, Locale.getDefault());
								List<Address> addresses = geocoder
										.getFromLocation(latitude, longitude, 1);
								String city = addresses.get(0).getLocality();
								/*
								 * String state =
								 * addresses.get(0).getAdminArea(); String zip =
								 * addresses.get(0).getPostalCode(); String
								 * country = addresses.get(0) .getCountryName();
								 */
								if (!city.contains("delhi")
										|| !city.contains("Mumbai")) {

									editor_loc.putString("city", "Delhi");
									editor_loc.commit();

								} else if (city.contains("delhi")) {
									editor_loc.putString("city", "Delhi");
									editor_loc.commit();

								} else if (city.contains("Mumbai")) {
									editor_loc.putString("city", "Mumbai");
									editor_loc.commit();

								} else {

									editor_loc.putString("city", "Delhi");
									editor_loc.commit();

								}

							} catch (Exception e) {
								
							}
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {

						// Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();

								/*
								 * Toast.makeText(getApplicationContext(),
								 * latitude + ":::" + longitude, 5000) .show();
								 */

								try {
									Geocoder geocoder = new Geocoder(
											SplashScreen.this,
											Locale.getDefault());
									List<Address> addresses = geocoder
											.getFromLocation(latitude,
													longitude, 1);
									String city = addresses.get(0)
											.getLocality();
									/*
									 * String state =
									 * addresses.get(0).getAdminArea(); String
									 * zip = addresses.get(0).getPostalCode();
									 * String country = addresses.get(0)
									 * .getCountryName();
									 */
									if (!city.contains("delhi")
											&& !city.contains("Mumbai")) {

										editor_loc.putString("city", "Delhi");
										editor_loc.commit();

									} else if (city.contains("delhi")) {
										editor_loc.putString("city", "Delhi");
										editor_loc.commit();

									} else if (city.contains("Mumbai")) {
										editor_loc.putString("city", "Mumbai");
										editor_loc.commit();

									} else {

										editor_loc.putString("city", "Delhi");
										editor_loc.commit();

									}

								} catch (Exception e) {
									
								}
							}
						}
					}
				}

				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						
						MainWebservice();

					}
				}, 0);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				SplashScreen.this);

		// Setting Dialog Title
		alertDialog.setTitle("GPS is settings");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is not enabled. Do you want to go to settings menu?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						SplashScreen.this.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	public void last_update() {
		LAST_UPDATE_REST_URL = getString(R.string.base_url)
				+ "lastUpdates.json";
		Log.e("update_url", LAST_UPDATE_REST_URL);
	//	client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(LAST_UPDATE_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = bufferedReader.readLine()) != null) {

						text = text + line;
					}
					all_update(text);
					//Log.e("RESp", text);
					
				} catch (Exception e) {
					
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				
//Log.e("FAIL", "FAIL");
			}
		});
	}

	public void all_update(String data) {
		String timeStamp = "";
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		SearchUpdate searchUpdate = gson.fromJson(jsonReader,
				SearchUpdate.class);
		updateSearch = searchUpdate.getUpdate();

		for (int i = 0; i < updateSearch.size(); i++) {
			updateSearch ser = updateSearch.get(i);

			if (ser.get_model().equals("Trends")) {
				timeStamp = ser.get_timestamp();
				if (!time.equals(ser.get_timestamp())) {
					editor.putString("model", ser.get_model());
					editor.putString("time", ser.get_timestamp());
					editor.commit();
					// Log.e("PRO", time + "--" + ser.get_timestamp());
					all_product();

				}

			}

		}

		if (time.equals(timeStamp)) {
			if (userid.equals("NA")) {

				Intent login_activity1 = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(login_activity1);
				finish();
				
			} else {
				Intent main_activity1 = new Intent(getApplicationContext(),MainActivity.class);
				  startActivity(main_activity1); 
				  finish();
			}
		}
	}

	public void all_product() {
		//Log.e("pro-url", ALL_PRODUCT_REST_URL);
		//client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_PRODUCT_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String txt1 = "";
					String txt2 = "";
					while ((txt1 = br.readLine()) != null) {

						txt2 = txt2 + txt1;
					}
					allProduct_showData(txt2);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});
	}

	public void allProduct_showData(String data) {

		home_search_product_db.allDelete();
		home_search_product_db.addAllProduct(new AllProduct("All Product",
				"All Product"));
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		Product ppp = gson.fromJson(jsonReader, Product.class);
		List<offerings> offerings = ppp.getOfferings();
		for (int i = 0; i < offerings.size(); i++) {

			name = offerings.get(i).get_name();
			category = offerings.get(i).get_category();
			home_search_product_db
					.addAllProduct(new AllProduct(name, category));
		}
		/*
		 * Intent iii = new Intent(getApplicationContext(), MainActivity.class);
		 * startActivity(iii); 
		 * finish();
		 */
		if (userid.equals("NA")) {

			Intent login_activity2 = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(login_activity2);
			finish();
			
		} else {
			Intent main_activity2 = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(main_activity2);
			finish();
		}
	}

	/**
	 * MainWebService()
	 */

	public void MainWebservice() {
		//Log.e("CITY", city_name);
		
		MAIN_WEBSERVICE_REST_URL = getString(R.string.base_url)
				+ "start/setClientLocation/" + city_name + ".json";
		//Log.e("MAIN", MAIN_WEBSERVICE_REST_URL);
		//client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(MAIN_WEBSERVICE_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				last_update();

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});
	}

}
