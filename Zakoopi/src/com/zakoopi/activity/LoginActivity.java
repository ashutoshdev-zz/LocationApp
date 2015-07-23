package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mycam.ImageDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zakoopi.R;
import com.zakoopi.helper.POJO;
import com.zakoopi.helper.ProgressHUD;
import com.zakoopi.homefeed.Popular_ArticleData;
import com.zakoopi.homefeed.Popular_Article_Images;
import com.zakoopi.homefeed.Popular_Article_User;
import com.zakoopi.homefeed.Popular_Lookbook_Cards;
import com.zakoopi.homefeed.Popular_Lookbook_User;
import com.zakoopi.homefeed.Popular_Lookbookdata;
import com.zakoopi.homefeed.Popular_StoreReviewData;
import com.zakoopi.homefeed.Popular_StoreReview_Store;
import com.zakoopi.homefeed.Popular_StoreReview_Users;
import com.zakoopi.homefeed.Popular_Teamsdata;
import com.zakoopi.homefeed.popularfeed;
import com.zakoopi.user.model.User;
import com.zakoopi.user.model.UserDetails;
import com.zakoopi.utils.ClientHttp;

public class LoginActivity extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, OnCancelListener {

	CallbackManager callbackmanager;
	String str_id;
	TextView title;
	ImageView img1, img2, img3, img4;
	private ViewFlipper viewFlipper;
	private float lastX;
	ImageView fb, gp;
	ProgressHUD mProgressHUD;
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	// private static final String LOOKBOOK_URL =
	// "http://v3.zakoopi.com/api/users/androidlogin.json";
	private static String LOOKBOOK_URL = "";
	String password;
	// for g+ login
	private static final int RC_SIGN_IN = 0;
	private static final String TAG = "MainActivity";
	private static final int PROFILE_PIC_SIZE = 400;
	private GoogleApiClient mGoogleApiClient;
	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private ConnectionResult mConnectionResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		setContentView(R.layout.landing);

		// Initializing google plus api client
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		title = (TextView) findViewById(R.id.tiltle);
		img1 = (ImageView) findViewById(R.id.imageView2);
		img2 = (ImageView) findViewById(R.id.imageView3);
		img3 = (ImageView) findViewById(R.id.imageView4);
		img4 = (ImageView) findViewById(R.id.imageView5);

		viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
		viewFlipper.setDisplayedChild(3);
		fb = (ImageView) findViewById(R.id.imageView6);
		gp = (ImageView) findViewById(R.id.imageView7);

		LOOKBOOK_URL = getString(R.string.base_url) + "users/androidlogin.json";

		fb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				onFblogin();
			}
		});

		gp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				signInWithGplus();
			}
		});

	}

	// for g+
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	// Using the following method, we will handle all screen swaps.
	public boolean onTouchEvent(MotionEvent touchevent) {
		switch (touchevent.getAction()) {

		case MotionEvent.ACTION_DOWN:
			lastX = touchevent.getX();
			break;
		case MotionEvent.ACTION_UP:
			float currentX = touchevent.getX();

			// Handling left to right screen swap.
			if (lastX < currentX) {

				// If there aren't any other children, just break.
				if (viewFlipper.getDisplayedChild() == (viewFlipper
						.getChildCount() - 1))
					break;

				// Next screen comes in from left.
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
				// Current screen goes out from right.
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

				// Display next screen.
				viewFlipper.showNext();

				if (viewFlipper.getDisplayedChild() == 3) {

					img1.setImageResource(R.drawable.slider_dot_blue);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_grey);
					img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title1));

				} else if (viewFlipper.getDisplayedChild() == 2) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_blue);
					img3.setImageResource(R.drawable.slider_dot_grey);
					img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title2));

				} else if (viewFlipper.getDisplayedChild() == 1) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_blue);
					img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title3));

				} else if (viewFlipper.getDisplayedChild() == 0) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_grey);
					img4.setImageResource(R.drawable.slider_dot_blue);
					title.setText(getString(R.string.title4));
				}

			}

			// Handling right to left screen swap.
			if (lastX > currentX) {

				// If there is a child (to the left), kust break.
				if (viewFlipper.getDisplayedChild() == 0)
					break;

				// Next screen comes in from right.
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
				// Current screen goes out from left.
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

				// Display previous screen.
				viewFlipper.showPrevious();

				if (viewFlipper.getDisplayedChild() == 3) {

					img1.setImageResource(R.drawable.slider_dot_blue);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_grey);
					img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title1));

				} else if (viewFlipper.getDisplayedChild() == 2) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_blue);
					img3.setImageResource(R.drawable.slider_dot_grey);
					img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title2));

				} else if (viewFlipper.getDisplayedChild() == 1) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_blue);
					img4.setImageResource(R.drawable.slider_dot_grey);
					title.setText(getString(R.string.title3));

				} else if (viewFlipper.getDisplayedChild() == 0) {

					img1.setImageResource(R.drawable.slider_dot_grey);
					img2.setImageResource(R.drawable.slider_dot_grey);
					img3.setImageResource(R.drawable.slider_dot_grey);
					img4.setImageResource(R.drawable.slider_dot_blue);
					title.setText(getString(R.string.title4));

				}
			}
			break;
		}
		return false;
	}

	private void onFblogin() {
		callbackmanager = CallbackManager.Factory.create();

		// Set permissions
		LoginManager.getInstance().logInWithReadPermissions(this,
				Arrays.asList("email", "user_photos", "public_profile"));

		LoginManager.getInstance().registerCallback(callbackmanager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {

						System.out.println("Success");
						GraphRequest.newMeRequest(loginResult.getAccessToken(),
								new GraphRequest.GraphJSONObjectCallback() {

									@Override
									public void onCompleted(JSONObject json,
											GraphResponse response) {
										// TODO Auto-generated method stub

										if (response.getError() != null) {
											// handle error
											System.out.println("ERROR");
										} else {
											System.out.println("Success");
											try {

												String jsonresult = String
														.valueOf(json);
												System.out
														.println("JSON Result"
																+ jsonresult);

												String str_email = json
														.getString("email");
												str_id = json.getString("id");
												String str_firstname = json
														.getString("first_name");
												String str_lastname = json
														.getString("last_name");
												// String str_location =
												// json.getString("location");
												getUserPic(str_id, jsonresult,
														str_email);
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}

								}).executeAsync();

					}

					@Override
					public void onCancel() {
						// Log.d("TAG_CANCEL", "On cancel");
					}

					@Override
					public void onError(FacebookException error) {
						// Log.d("TAG_ERROR", error.toString());
					}
				});
	}

	public void getUserPic(String userID, String result, String email) {
		String imageURL = "";
		imageURL = "http://graph.facebook.com/" + userID
				+ "/picture?type=large";
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(LoginActivity.this));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher).build();
		upload(result, imageURL, "facebook", email);
		// imageLoader.displayImage(imageURL, profilePictureView);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Logs 'install' and 'app activate' App Events.
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Logs 'app deactivate' App Event.
		AppEventsLogger.deactivateApp(this);
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		try {
			if (mConnectionResult.hasResolution()) {
				try {
					mIntentInProgress = true;
					mConnectionResult
							.startResolutionForResult(this, RC_SIGN_IN);
				} catch (SendIntentException e) {
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
					0).show();
			return;
		}

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		} else {

			callbackmanager.onActivityResult(requestCode, resultCode, data);
		}

	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		// Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

		// Get user's information
		getProfileInformation();

	}

	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation() {
		try {
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
				Person currentPerson = Plus.PeopleApi
						.getCurrentPerson(mGoogleApiClient);
				String personName = currentPerson.getDisplayName();
				String personPhotoUrl = currentPerson.getImage().getUrl();
				String personGooglePlusProfile = currentPerson.getUrl();
				String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

				// Log.e(TAG, "Name: " + personName + ", plusProfile: "
				// + personGooglePlusProfile + ", email: " + email
				// + ", Image: " + personPhotoUrl);
				// by default the profile url gives 50x50 px image only
				// we can replace the value with whatever dimension we want by
				// replacing sz=X
				personPhotoUrl = personPhotoUrl.substring(0,
						personPhotoUrl.length() - 2)
						+ PROFILE_PIC_SIZE;

				upload(currentPerson.toString(), personPhotoUrl, "google",
						email);

			} else {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * "Person information is null", Toast.LENGTH_LONG).show();
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();

	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
			// Log.e("G++++", "G+");
			resolveSignInError();
		}
	}

	@SuppressWarnings("deprecation")
	public void upload(String result, String imgurl, String type, String email) {
		// client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		Log.e("Login", LOOKBOOK_URL);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		password = telephonyManager.getDeviceId();
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("vendor", type);
		params.put("alldata", result);
		params.put("imgurl", imgurl);
		params.put("email", email);
		params.put("deviceid", password);

		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started
				/*mProgressHUD = ProgressHUD.show(LoginActivity.this,
						"Processing...", true, true, LoginActivity.this);
				mProgressHUD.setCancelable(false);*/
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
						Log.e("success", "success");

					}
					Log.e("RES", st1);
					show_user(st1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				Log.e("falied", e.getMessage() + "");
			//	mProgressHUD.dismiss();

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
		Log.e("SHOW", "SHOW");
		User main_user1 = gson.fromJson(jsonReader, User.class);
		// UserDetails main_user = gson.fromJson(jsonReader, UserDetails.class);
		Log.e("SHOW1", "SHOW1");
		UserDetails main_user = main_user1.getUser();

		new MyApp1().execute(main_user);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		// dialog.dismiss();
	}

	private class MyApp1 extends AsyncTask<UserDetails, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(UserDetails... params) {
			UserDetails main_user = params[0];
			SharedPreferences preferences = getSharedPreferences("User_detail",
					0);
			Editor editor = preferences.edit();
			editor.putString("user_id", main_user.getId());
			editor.putString("user_email", main_user.getEmail());
			editor.putString("user_firstName", main_user.getFirst_name());
			editor.putString("user_lastName", main_user.getLast_name());
			editor.putString("user_location", main_user.getLocation());
			editor.putString("user_uid", main_user.getUid());
			editor.putString("user_age", main_user.getAge());
			editor.putString("user_gender", main_user.getGender());
			editor.putString("user_rewardPoints", main_user.getReward_points());
			editor.putString("user_reviewCount",
					main_user.getPro_review_count());
			editor.putString("user_likeCount", main_user.getPro_likes_count());
			editor.putString("user_fbLink", main_user.getFb_link());
			editor.putString("user_twitterLink", main_user.getTwitter_link());
			editor.putString("user_otherWebsite", main_user.getOther_website());
			editor.putString("user_image", main_user.getAndroid_api_img());
			editor.putString("password", password);
			editor.commit();

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			Intent main_activity = new Intent(LoginActivity.this,
					MainActivity.class);
			startActivity(main_activity);
			finish();
			//mProgressHUD.dismiss();

		}

	}

}