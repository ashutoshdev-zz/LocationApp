package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mycam.LookBookTabsActivity;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.floatlib.FloatingActionButton;
import com.zakoopi.fragments.HomeDiscoverFragment;
import com.zakoopi.fragments.HomeFeedFragment;
import com.zakoopi.fragments.HomeProfileFrag;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.Variables;
import com.zakoopi.search.AllArea;
import com.zakoopi.search.Area;
import com.zakoopi.search.SearchUpdate;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.search.updateSearch;
import com.zakoopi.tab_layout.MaterialTab;
import com.zakoopi.tab_layout.MaterialTabHostIcon;
import com.zakoopi.tab_layout.MaterialTabListener;
import com.zakoopi.utils.ClientHttp;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements
		MaterialTabListener {
	private ViewPager pager;
	private ViewPagerAdapter pagerAdapter;
	MaterialTabHostIcon tabHost;
	private Resources res;
	ImageView img_menu;
	SimpleSideDrawer slide_me;
	FloatingActionButton float_btn;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	CircleImageView pro_user_pic;
	private SharedPreferences pro_user_pref;
	RelativeLayout rel_background, rel_edt_profile;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	TextView txt_city_name;
    AsyncHttpClient client = ClientHttp.getInstance();
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;
	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	private static String LAST_UPDATE_REST_URL = "";
	private static String ALL_AREA_REST_URL = "";
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	HomeSearchAllAreaDatabaseHandler home_search_area_db;
	String model_market1, time_market1;
	String save_model_market1, save_time_market1;
	Editor editor_market1;
	SharedPreferences prefs_area1;
	String text = "";
	String line = "";
	ArrayList<updateSearch> updateSearch = new ArrayList<updateSearch>();
	String market_name;
	SharedPreferences pref_city, pref_location1;
	Editor edit_city;
	String city;
	int fragment_pos;

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		 * pref_city = getSharedPreferences("CITYPREF", 1); edit_city =
		 * pref_city.edit();
		 */
		pref_location1 = getSharedPreferences("location", 1);
		edit_city = pref_location1.edit();
		/*
		 * edit_city.putString("loc", pref_location1.getString("city",
		 * "Delhi/NCR")); edit_city.commit();
		 */
		city = pref_location1.getString("city", "Delhi");
		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
	//	Log.e("CLIENT", user_email + "----" + user_password);

		prefs_area1 = getSharedPreferences("PREF", 1);
		editor_market1 = prefs_area1.edit();
		model_market1 = prefs_area1.getString("model_market1", "NA");
		time_market1 = prefs_area1.getString("time_market1", "N/A");

		save_model_market1 = prefs_area1.getString("model_market1", "gh");
		save_time_market1 = prefs_area1.getString("time_market1", "ghf");

		home_search_area_db = new HomeSearchAllAreaDatabaseHandler(
				getApplicationContext());

		/**
		 * Side Menu
		 */
		img_menu = (ImageView) findViewById(R.id.img_menu);
		res = this.getResources();
		txt_city_name = (TextView) findViewById(R.id.txt_city_name);
		slide_me = new SimpleSideDrawer(this);
		slide_me.setRightBehindContentView(R.layout.side_menu);
		pro_user_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
		side_user_name = (TextView) findViewById(R.id.txt_user_name);
		side_about = (TextView) findViewById(R.id.txt_about);
		side_con_ac = (TextView) findViewById(R.id.txt_con_ac);
		side_edt_profile = (TextView) findViewById(R.id.txt_edit);
		side_noti_settings = (TextView) findViewById(R.id.txt_noti);
		side_rate = (TextView) findViewById(R.id.txt_rate);
		side_sign_out = (TextView) findViewById(R.id.txt_sign_out);
		side_sug_store = (TextView) findViewById(R.id.txt_sug_store);
		rel_edt_profile = (RelativeLayout) findViewById(R.id.rel_edt_profile);
		Picasso.with(getApplicationContext()).load(pro_user_pic_url)
				.into(pro_user_pic);
		side_user_name.setText(pro_user_name);

		/**
		 * Typeface
		 */

		typeface_semibold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		typeface_bold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Bold.ttf");

		typeface_regular = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		/**
		 * set Font on TextView
		 */
		// txt_city_name.setTypeface(typeface_regular);
		side_user_name.setTypeface(typeface_bold);
		side_about.setTypeface(typeface_regular);
		side_con_ac.setTypeface(typeface_regular);
		side_edt_profile.setTypeface(typeface_regular);
		side_noti_settings.setTypeface(typeface_regular);
		side_rate.setTypeface(typeface_regular);
		side_sign_out.setTypeface(typeface_regular);
		side_sug_store.setTypeface(typeface_regular);
		/**
		 * SideMenu ClickListener
		 */
		rel_edt_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent edt_profile = new Intent(MainActivity.this,
						EditProfilePage.class);
				startActivity(edt_profile);

			}
		});

		img_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				slide_me.toggleRightDrawer();
			}
		});

		/**
		 * TabHost & ViewPager
		 */
		tabHost = (MaterialTabHostIcon) this.findViewById(R.id.tabHost);
		pager = (ViewPager) this.findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);

		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// when user do a swipe the selected tab change
				tabHost.setSelectedNavigationItem(position);
				Toast.makeText(getApplicationContext(), "" + position,
						Toast.LENGTH_SHORT).show();
				fragment_pos = position;
			}
		});
		// insert all tabs from pagerAdapter data
		for (int i = 0; i < pagerAdapter.getCount(); i++) {
			tabHost.addTab(tabHost.newTab().setIcon(getIcon(i))
					.setTabListener(this));
		}

		/**
		 * Add Lookbook Button
		 */
		float_btn = (FloatingActionButton) findViewById(R.id.float_button);
		float_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				final Dialog dialog_add_lookbook = new Dialog(MainActivity.this);
				dialog_add_lookbook
						.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog_add_lookbook
						.setContentView(R.layout.add_lookbook_dialog);
				/*
				 * TextView txt_upload_lookbook = (TextView) dialog_add_lookbook
				 * .findViewById(R.id.txt_upload_lookbook); TextView
				 * txt_write_review = (TextView) dialog_add_lookbook
				 * .findViewById(R.id.txt_write_review);
				 */
				// txt_upload_lookbook.setTypeface(font_regular);
				// txt_write_review.setTypeface(font_regular);
				RelativeLayout rel_upload_lookbook = (RelativeLayout) dialog_add_lookbook
						.findViewById(R.id.rel_upload);
				RelativeLayout rel_write_review = (RelativeLayout) dialog_add_lookbook
						.findViewById(R.id.rel_write);
				ImageView img_close = (ImageView) dialog_add_lookbook
						.findViewById(R.id.img_close);

				/**
				 * dialog ClcikListener
				 */
				rel_upload_lookbook.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent upload_lookbook = new Intent(MainActivity.this,
								LookBookTabsActivity.class);
						startActivity(upload_lookbook);
						dialog_add_lookbook.dismiss();
					}
				});
				rel_write_review.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						dialog_add_lookbook.dismiss();
					}
				});
				img_close.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog_add_lookbook.dismiss();
					}
				});
				dialog_add_lookbook.show();

			}
		});

		last_update();

		if (city.equals("Delhi")) {
			txt_city_name.setText("Delhi/NCR");
		} else {
			txt_city_name.setText(city);
		}

		txt_city_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				PopupMenu popup = new PopupMenu(MainActivity.this,
						txt_city_name);
				// Inflating the Popup using xml file
				popup.getMenuInflater().inflate(R.menu.popup_menu,
						popup.getMenu());
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(MainActivity.this,
								"You Clicked : " + item.getTitle(),
								Toast.LENGTH_SHORT).show();
						if (item.getTitle().equals("Delhi/NCR")) {
							edit_city.putString("city", "Delhi");
						} else {
							edit_city.putString("city",
									(String) item.getTitle());
						}

						edit_city.commit();
						city = pref_location1.getString("city", "Delhi");
						txt_city_name.setText(city);
						MainWebservice();
						return true;
					}
				});

				popup.show();// showing popup menu
			}

		});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.zakoopi.tab_layout.MaterialTabListener#onTabSelected(com.zakoopi.
	 *      tab_layout.MaterialTab)
	 */
	@Override
	public void onTabSelected(MaterialTab tab) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(MaterialTab tab) {

	}

	@Override
	public void onTabUnselected(MaterialTab tab) {

	}

	/**
	 * ViewPagerAdapter Class
	 * 
	 * Set Fragment 1. HomeFeedFragment 2. HomeDiscoverFragment 3.
	 * HomeProfileFrag
	 */
	private class ViewPagerAdapter extends FragmentStatePagerAdapter {
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int num) {
			if (num == 0) {
				HomeFeedFragment frag = new HomeFeedFragment();
				return frag;
			} else if (num == 1) {
				HomeDiscoverFragment fragmentSearch = new HomeDiscoverFragment();
				return fragmentSearch;
			} else {
				HomeProfileFrag fragmentProfile = new HomeProfileFrag();
				return fragmentProfile;
			}

		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "tab 1";
			case 1:
				return "tab 2";
			case 2:
				return "tab 3";

			default:
				return null;
			}
		}
	}

	/**
	 * Tab Drawable Icon
	 */
	private Drawable getIcon(int position) {
		switch (position) {
		case 0:
			return res.getDrawable(R.drawable.home_home_inactive);
		case 1:
			return res.getDrawable(R.drawable.home_discover_inactive);
		case 2:
			return res.getDrawable(R.drawable.home_profile_active);
		}
		return null;
	}

	public void last_update() {

		LAST_UPDATE_REST_URL = getString(R.string.base_url)
				+ "lastUpdates.json";

	//	Log.e("update_url", LAST_UPDATE_REST_URL);
		// client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
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
				} catch (Exception e) {

				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {

			}
		});
	}

	public void all_update(String data) {
		// String timeStamp = "";
		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);
		SearchUpdate searchUpdate = gson.fromJson(jsonReader,
				SearchUpdate.class);
		updateSearch = searchUpdate.getUpdate();

		for (int i = 0; i < updateSearch.size(); i++) {
			updateSearch ser = updateSearch.get(i);
			// Log.e("SER", "" + ser);
			if (ser.get_model().equals("Markets")) {
				// timeStamp = ser.get_timestamp();

				if (!time_market1.equals(ser.get_timestamp())) {
					// Log.e("AREA1", time_market1 + "--" +
					// ser.get_timestamp());
					editor_market1.putString("model_market1", ser.get_model());
					editor_market1.putString("time_market1",
							ser.get_timestamp());
					editor_market1.commit();
					// Log.e("EDi", "" + editor_market1);
					// Log.e("AREA", time_market1 + "--" + ser.get_timestamp());
					all_area();

				}

			}

		}
		/*
		 * if (time_market1.equals(timeStamp)) { areaList =
		 * home_search_area_db.getAllAreas1(); ArrayAdapter<String> adp = new
		 * ArrayAdapter<String>(getApplicationContext(),
		 * R.layout.home_search_spinner_item, R.id.txt_product, areaList);
		 * search_spinner_area.setAdapter(adp);
		 * 
		 * }
		 */

	}

	/**
	 * {@code} all_area()
	 * 
	 * @return void
	 */
	public void all_area() {
		ALL_AREA_REST_URL = getString(R.string.base_url) + "markets.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_AREA_REST_URL, new AsyncHttpResponseHandler() {

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
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {

						text2 = text2 + text1;
					}
					allArea_showData(text2);
				//	Log.e("AREA", text2);
				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

			}
		});
	}

	/**
	 * 
	 * @allArea_showData data1
	 * @return void
	 */
	public void allArea_showData(String data1) {

		markets.clear();
		home_search_area_db.allDelete();
		home_search_area_db.addAllArea(new AllArea("All Area"));
		Gson gson1 = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data1));
		jsonReader.setLenient(true);

		Area area1 = gson1.fromJson(jsonReader, Area.class);
		markets = area1.getMarkets();
		for (int i = 0; i < markets.size(); i++) {
			market_name = markets.get(i).get_market_name();
			home_search_area_db.addAllArea(new AllArea(market_name));
			// Log.e("ADDALL", ""+home_search_area_db);

		}
		Variables.areaList.clear();
		Variables.areaList = home_search_area_db.getAllAreas1();
		// Log.e("ADDALL", ""+home_search_area_db);
		/*
		 * areaList = home_search_area_db.getAllAreas1();
		 * 
		 * ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
		 * R.layout.home_search_spinner_item, R.id.txt_product, areaList);
		 * search_spinner_area.setAdapter(adp);
		 */
	}

	/**
	 * MainWebService()
	 */

	public void MainWebservice() {
		String MAIN_WEBSERVICE_REST_URL = "";
	//	Log.e("CITY", city);
	//	Log.e("MAIN", MAIN_WEBSERVICE_REST_URL);
		MAIN_WEBSERVICE_REST_URL = getString(R.string.base_url)
				+ "start/setClientLocation/" + city + ".json";
		// client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
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

				pager.setAdapter(null);
				pager.setAdapter(pagerAdapter);
				pager.setCurrentItem(fragment_pos);
				//all_area_city();
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
	
	
	
	/**
	 * {@code} all_area()
	 * 
	 * @return void
	 *//*
	public void all_area_city() {
		ALL_AREA_REST_URL = getString(R.string.base_url) + "markets.json";

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_AREA_REST_URL, new AsyncHttpResponseHandler() {

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
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {

						text2 = text2 + text1;
					}
					Log.e("AREA_CITY", text2);
					allArea_showData_city(text2);
					
				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {

			}
		});
	}

	*//**
	 * 
	 * @allArea_showData data1
	 * @return void
	 *//*
	public void allArea_showData_city(String data1) {

		markets.clear();
		home_search_area_db.allDelete();
		home_search_area_db.addAllArea(new AllArea("All Area"));
		Gson gson1 = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data1));
		jsonReader.setLenient(true);

		Area area1 = gson1.fromJson(jsonReader, Area.class);
		markets = area1.getMarkets();
		Log.e("MARKETS", ""+markets);
		for (int i = 0; i < markets.size(); i++) {
			market_name = markets.get(i).get_market_name();
			home_search_area_db.addAllArea(new AllArea(market_name));
			 Log.e("ADDALL", ""+home_search_area_db);

		}
		Variables.areaList.clear();
		Variables.areaList = home_search_area_db.getAllAreas1();
		// Log.e("ADDALL", ""+home_search_area_db);
		
		 * areaList = home_search_area_db.getAllAreas1();
		 * 
		 * ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
		 * R.layout.home_search_spinner_item, R.id.txt_product, areaList);
		 * search_spinner_area.setAdapter(adp);
		 
	}*/
}