package com.zakoopilocation;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

public class LoginActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnCancelListener {
	static final int DEFAULT_TIMEOUT = 40000;
	private static String LOOKBOOK_URL = null;
	private static final int PROFILE_PIC_SIZE = 400;
	private static final int RC_SIGN_IN = 0;
	private static final String TAG = "MainActivity";
	ArrayList<String> city_list;
	private String city_name;
	AsyncHttpClient client;
	Editor editor;
	Editor editor_loc;
	Spinner loc_spin;
	ImageView login;
	private ConnectionResult mConnectionResult;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	ProgressHUD mProgressHUD;
	private boolean mSignInClicked;
	private String password;
	private SharedPreferences pref_location;
	SharedPreferences preferences;
	RelativeLayout rel;
	TextView textView1;
	String user_id;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(1);
		setContentView(R.layout.login);
		this.login = (ImageView) findViewById(R.id.imageView1);
		this.textView1 = (TextView) findViewById(R.id.textView1);
		this.loc_spin = (Spinner) findViewById(R.id.spinner1);
		this.rel = (RelativeLayout) findViewById(R.id.gggg);
		this.pref_location = getSharedPreferences("location", 1);
		this.editor_loc = this.pref_location.edit();
		this.city_list = new ArrayList<String>();
		this.city_list.clear();
		this.city_list.add("Delhi");
		this.city_list.add("Mumbai");
		this.city_list.add("Kolkata");
		this.loc_spin.setAdapter(new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_items,
				R.id.textView1, this.city_list));
		loc_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

		
			public void onItemSelected(AdapterView adapterview, View view,
					int i, long l) {
				String city = city_list.get(i);
				editor_loc.putString("city", city);
				editor_loc.commit();
			}

			public void onNothingSelected(AdapterView adapterview) {
			}

			
		});
		this.preferences = getSharedPreferences("User_detail", RC_SIGN_IN);
		this.editor = this.preferences.edit();
		this.user_id = this.preferences.getString("user_id", "noid");
		if (this.user_id.equals("noid")) {
			this.login.setVisibility(RC_SIGN_IN);
			this.textView1.setVisibility(RC_SIGN_IN);
			this.rel.setVisibility(RC_SIGN_IN);
		} else {
			this.login.setVisibility(8);
			this.textView1.setVisibility(8);
			this.rel.setVisibility(8);
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		this.client = ClientHttp.getInstance(this);
		this.mGoogleApiClient = new Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		LOOKBOOK_URL = getString(R.string.base_url) + "users/androidlogin.json";
		this.login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signInWithGplus();
			}
		});
	}

	private void signInWithGplus() {
		if (!this.mGoogleApiClient.isConnecting()) {
			this.mSignInClicked = true;
			resolveSignInError();
		}
	}

	private void resolveSignInError() {
		try {
			if (this.mConnectionResult.hasResolution()) {
				try {
					this.mIntentInProgress = true;
					this.mConnectionResult.startResolutionForResult(this,
							RC_SIGN_IN);
				} catch (SendIntentException e) {
					this.mIntentInProgress = false;
					this.mGoogleApiClient.connect();
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	protected void onStart() {
		super.onStart();

		this.mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (this.mGoogleApiClient.isConnected()) {
			this.mGoogleApiClient.disconnect();
		}
	}

	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					RC_SIGN_IN).show();
		} else if (!this.mIntentInProgress) {
			this.mConnectionResult = result;
			if (this.mSignInClicked) {
				resolveSignInError();
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode != -1) {
				this.mSignInClicked = false;
			}
			this.mIntentInProgress = false;
			if (!this.mGoogleApiClient.isConnecting()) {
				this.mGoogleApiClient.connect();
			}
		}
	}

	public void onConnected(Bundle arg0) {
		this.mSignInClicked = false;
		getProfileInformation();
	}

	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(this.mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(this.mGoogleApiClient);

				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				int personGender = currentPerson.getGender();
				String personDOB = currentPerson.getBirthday();
				String email = Plus.AccountApi
						.getAccountName(this.mGoogleApiClient);
				String str;
				if (personGender == 0) {
					str = "Male";
				} else {
					str = "Female";
				}
				personPhotoUrl = personPhotoUrl.substring(RC_SIGN_IN,
						personPhotoUrl.length() - 2) + PROFILE_PIC_SIZE;
				this.city_name = this.pref_location.getString("city", "123");
				upload(currentPerson.toString(), personGender, personPhotoUrl,
						"google", email, this.city_name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onConnectionSuspended(int arg0) {
		this.mGoogleApiClient.connect();
	}

	@SuppressWarnings("deprecation")
	public void upload(String result, int personGender, String imgurl,
			String type, String email, String city_name) {
		// client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");

		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		password = telephonyManager.getDeviceId();
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("vendor", type);
		params.put("alldata", result);
		params.put("gender", personGender);
		params.put("imgurl", imgurl);
		params.put("email", email);
		params.put("location", city_name);
		params.put("deviceid", password);

		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started
				mProgressHUD = ProgressHUD.show(LoginActivity.this,
						"Processing...", true, true, LoginActivity.this);
				mProgressHUD.setCancelable(false);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;
						// Log.e("success", "success");

					}
					// Log.e("RES_LOGIN", st1);
					show_user(st1);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				// Log.e("FAIL", "FAIl" + statusCode);
				mProgressHUD.dismiss();

			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void show_user(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		User main_user1 = gson.fromJson(jsonReader, User.class);
		UserDetails main_user = main_user1.getUser();

		new MyApp1().execute(main_user);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	private class MyApp1 extends AsyncTask<UserDetails, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(LoginActivity.this,
					"Processing...", true, true, LoginActivity.this);
			mProgressHUD.setCancelable(false);

		}

		@Override
		protected Void doInBackground(UserDetails... params) {
			UserDetails main_user = params[0];
			SharedPreferences preferences = getSharedPreferences("User_detail",
					0);
			Editor editor = preferences.edit();

			try {
				// Log.e("LOCA", ""+main_user.getLocation());
				editor.putString("user_id", main_user.getId());
				editor.putString("user_email", main_user.getEmail());
				editor.putString("user_firstName", main_user.getFirst_name());
				editor.putString("user_lastName", main_user.getLast_name());
				editor.putString("user_location", main_user.getLocation());
				editor.putString("user_uid", main_user.getUid());
				editor.putString("user_age", main_user.getAge());
				editor.putString("user_gender", main_user.getGender());
				editor.putString("user_rewardPoints", main_user.getPoints());
				editor.putString("user_reviewCount",
						main_user.getPro_review_count());
				editor.putString("user_likeCount",
						main_user.getPro_likes_count());
				editor.putString("user_fbLink", main_user.getFb_link());
				editor.putString("user_twitterLink",
						main_user.getTwitter_link());
				editor.putString("user_otherWebsite",
						main_user.getOther_website());
				editor.putString("user_image", main_user.getAndroid_api_img());
				editor.putString("about", main_user.getAbout());
				editor.putString("password", password);
				// Log.e("USER_IMG", main_user.getAndroid_api_img());
				editor.commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			try {
				LoginActivity.this.mProgressHUD.dismiss();
				LoginActivity.this.startActivity(new Intent(LoginActivity.this,
						MainActivity.class));
				LoginActivity.this.finish();
			} catch (Exception e) {
				LoginActivity.this.mProgressHUD.dismiss();
				e.printStackTrace();
			}
		}

	}

}
