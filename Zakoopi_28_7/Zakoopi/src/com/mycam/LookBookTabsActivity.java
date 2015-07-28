package com.mycam;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.cam.imagedatabase.DBHelper;
import com.cam.model.LastUpdate;
import com.cam.model.LastUpdateDetail;
import com.cam.model.StoreDeatil;
import com.cam.model.Stores;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tabbar.cam.SlidingTabLayout;
import com.tabbar.cam.ViewPagerAdapter;
import com.zakoopi.R;
import com.zakoopi.helper.CustomViewPager;

public class LookBookTabsActivity extends FragmentActivity {

	CustomViewPager pager;
	ViewPagerAdapter adapter;
	SlidingTabLayout tabs;
	CharSequence Titles[] = { "GALLERY", "CAMERA", "INSTAGRAM" };
	int Numboftabs = 3;
	private SQLiteDatabase db;
	public static final String DBTABLE1 = "Stores";
	private SQLiteStatement stm;
	ArrayList<String> storenames = new ArrayList<String>();
	int page = 1;
	static AsyncHttpClient client = new AsyncHttpClient();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	ProgressDialog bar;
	private static final String LAST_UPDATE_URL = "http://zakoopi.com/api/lastUpdates.json";
	private static final String STORE_URL = "http://zakoopi.com/api/stores.json?limit=100&page=";
	String text = "";
	String line = "";
	SharedPreferences pref;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);*/
		setContentView(R.layout.lookbook_tabs);
		
		pref = getApplicationContext().getSharedPreferences("lastupdate",
				MODE_PRIVATE);
		editor = pref.edit();
		DBHelper hp = new DBHelper(LookBookTabsActivity.this);
		db = hp.getWritableDatabase();
		stm = db.compileStatement("insert into  " + DBTABLE1
				+ " (id,market) values (?, ?)");

		lastUpdateCheck();
		
		adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles,
				Numboftabs);
		pager = (CustomViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(2);
		pager.setAdapter(adapter);

		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true);

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(pager);
		pager.setCurrentItem(1);
	}

	public void lastUpdateCheck() {
		client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(LAST_UPDATE_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = br.readLine()) != null) {

						text = text + line;

					}

					showData(text);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				Log.e("falied", e.getMessage() + "");
				// bar.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void updateStores(int page) {
		client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(STORE_URL + page, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started

				// bar = new ProgressDialog(ImageDetail.this);
				// bar.setMessage("Wait for some moments...");
				// bar.setIndeterminate(true);
				// bar.setCancelable(false);
				// bar.show();

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st1 = "";
					String st2 = "";
					while ((st1 = br.readLine()) != null) {

						st2 = st2 + st1;

					}
				//	Log.e("kkk", "service");
					// bar.dismiss();
					storeData(st2);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				// bar.dismiss();

			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void showData(String data) {
		//Log.e("hhhhdata", data);
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		LastUpdate ppp = gson.fromJson(reader, LastUpdate.class);
		List<LastUpdateDetail> feeds1 = ppp.getLastUpdates();
		String time = pref.getString("timestamp", "123456");

		for (int i = 0; i < feeds1.size(); i++) {
			LastUpdateDetail detail = feeds1.get(i);
			if (detail.getModel().equals("Stores")) {

				if (!time.equals(detail.getTimestamp())) {

					editor.putString("model", detail.getModel());
					editor.putString("timestamp", detail.getTimestamp());
					editor.commit();
                   
					updateStores(page);

					break;
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void storeData(String data) {
		//Log.e("hhhfffffffffhdata", data);
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		Stores ppp = gson.fromJson(reader, Stores.class);
		List<StoreDeatil> feeds1 = ppp.getStores();
	
		new setData().execute(feeds1);
		

	}
	
	private class setData extends AsyncTask<List<StoreDeatil>, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(List<StoreDeatil>... params) {

			DBHelper hp = new DBHelper(LookBookTabsActivity.this);
			db = hp.getWritableDatabase();
			for (int i = 0; i < params[0].size(); i++) {

				StoreDeatil detail = params[0].get(i);
				String id = detail.getId();
				String store_market = detail.getMarket();
				stm.bindString(1, id);
				stm.bindString(2, store_market);
				stm.executeInsert();
	        //   Log.e("DOIN", "BAX+CK");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			page++;
			updateStores(page);

		}

	}


}
