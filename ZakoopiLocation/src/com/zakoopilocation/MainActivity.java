package com.zakoopilocation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity {

	EditText edt_search_store;
	ListView lv;
	RelativeLayout rel_back;
	TextView txt_back;
	private CountDownTimer countDownTimer;
	private final long startTime = 3 * 1000;
	private final long interval = 1 * 1000;
	ArrayList<SearchPojo> storeDetail = new ArrayList<SearchPojo>();
	ArrayList<SearchPojo> storeDB = new ArrayList<SearchPojo>();
	MyAdapter adapter;
	private static String SEARCH_REST_URL = " ";
	AsyncHttpClient client;
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	private SharedPreferences pro_user_pref;
	private String user_email;
	private String user_password;
	private SharedPreferences pref_location1;
	private String city;
	SearchStoreByCharHandler searchdb;
	boolean datacheck = false;
	//String lookbook, review;
	ProgressBar progressBar1;
	Boolean isInternetPresent = false;
	RelativeLayout rel_bt;
	TextView txt_net;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setContentView(R.layout.activity_main);
		
		client = ClientHttp.getInstance(MainActivity.this);
		// Countdown timer objet
		countDownTimer = new MyCountDownTimer(startTime, interval);
		

		LogDebug.showMessage("here", "here");
		try {

			Intent i = getIntent();
			//lookbook = i.getStringExtra("SearchStore");
			//review = i.getStringExtra("SearchStore");

			searchdb = new SearchStoreByCharHandler(this);

			pref_location1 = getSharedPreferences("location", 1);
			city = pref_location1.getString("city", "Delhi");

			pro_user_pref = this.getSharedPreferences("User_detail", 0);
			user_email = pro_user_pref.getString("user_email", "9089");
			user_password = pro_user_pref.getString("password", "sar");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		edt_search_store = (EditText) findViewById(R.id.edt_search_store);
		edt_search_store.setHint("Start typing the store name");
		
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		
		lv = (ListView) findViewById(R.id.listView1);

		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				LogDebug.showMessage("store name", storeDetail.get(arg2).getStorename());
				Intent inn = new Intent(MainActivity.this,GetLocationActivity.class);
				inn.putExtra("sid", storeDetail.get(arg2).getId());
				inn.putExtra("sname", storeDetail.get(arg2).getStorename());
				inn.putExtra("smarket", storeDetail.get(arg2).getStoreadd());
				inn.putExtra("sslug", storeDetail.get(arg2).getStorslug());
				inn.putExtra("scity", storeDetail.get(arg2).getStorecity());
				startActivity(inn);
			}
		});

		edt_search_store.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("unchecked")
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

				String st = edt_search_store.getText().toString();

				try {

					storeDB = searchdb.getAllDATA();

					if (storeDB.size() <= 0) {

						if (st.length() >= 2) {
							client.cancelAllRequests(true);
							progressBar1.setVisibility(View.VISIBLE);
							countDownTimer.cancel();
							countDownTimer.start();
						} else {
							progressBar1.setVisibility(View.GONE);
						}

					} else {

						if (st.length() >= 2) {
							client.cancelAllRequests(true);
							progressBar1.setVisibility(View.VISIBLE);
							datacheck = false;
							storeDetail.clear();
							storeDetail = storeDB;
							new MyApp11().execute(storeDB);
						} else {
							progressBar1.setVisibility(View.GONE);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			}
		});
	}

	
	private class MyApp11 extends AsyncTask<ArrayList<SearchPojo>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(ArrayList<SearchPojo>... params) {

			// storeDetail = params[0];
			// Log.e("gggrrr", storeDetail.size() + "");
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			try {

				adapter = new MyAdapter(MainActivity.this, storeDetail);
				String text = edt_search_store.getText().toString()
						.toLowerCase(Locale.getDefault());
				adapter.filter(text);

				if (datacheck == true) {
					for (int i = 0; i < storeDetail.size(); i++) {

						if (storeDetail.get(i).getStorecity().equals(city)) {
							client.setEnableRedirects(true);
							lv.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							progressBar1.setVisibility(View.GONE);
						} else {

							countDownTimer.cancel();
							countDownTimer.start();
							break;
						}
					}
				} else {
					// do another thing
					countDownTimer.cancel();
					countDownTimer.start();
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	public class MyCountDownTimer extends CountDownTimer {
		public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
		}

		@Override
		public void onFinish() {

			// write code what you want
			try {

				if (!edt_search_store.getText().toString().equals("")) {
					Log.e("here","here");
						
						SEARCH_REST_URL = getString(R.string.base_url)
								+ "stores/storeSearch.json?term="
								+ edt_search_store.getText().toString().trim();
						SEARCH_REST_URL = SEARCH_REST_URL
								.replaceAll(" ", "%20");
						// do stuff
						getSearchData();

					
				} else {
					progressBar1.setVisibility(View.GONE);
				}

			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {

			// write code what you want

		}
	}

	/**
	 * @popular_feed page
	 */
	@SuppressWarnings("deprecation")
	public void getSearchData() {

		long time = System.currentTimeMillis();
		client.setBasicAuth(user_email, user_password);
		client.setEnableRedirects(true);
		Log.e("there","there"+"---"+SEARCH_REST_URL);
		client.post(SEARCH_REST_URL + "&_=" + time,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {

						client.cancelAllRequests(true);
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] response) {
						// called when response HTTP status is "200 OK"

						try {

							String text = "";
							String line = "";

							BufferedReader br = new BufferedReader(
									new InputStreamReader(
											new ByteArrayInputStream(response)));
							while ((line = br.readLine()) != null) {

								text = text + line;
							}

							Log.e("rrrrr", text);
							showData(text);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] errorResponse, Throwable e) {
						progressBar1.setVisibility(View.GONE);
						Log.e("statusCode", statusCode+"");
					}

					@Override
					public void onRetry(int retryNo) {
						// called when request is retried
					}
				});

	}

	@SuppressWarnings("unchecked")
	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		SearchResult[] arr = gson.fromJson(reader, SearchResult[].class);

		new MyApp().execute(arr);
	}

	/**
	 * MyApp extends AsyncTask<List<searchdata>, Void, Void> for Showdata(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */
	private class MyApp extends AsyncTask<SearchResult[], Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(SearchResult[]... params) {

			try {

				storeDetail.clear();
				SearchResult[] res = params[0];
				for (int i = 0; i < res.length; i++) {

					SearchPojo sss = new SearchPojo(res[i].getId(),
							res[i].getStore_name(), res[i].getMarket_name(),
							city, res[i].getSlug());
					storeDetail.add(sss);

					try {
						searchdb.addAllDATA(sss);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void param) {

			try {
				adapter = new MyAdapter(MainActivity.this, storeDetail);
				lv.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				progressBar1.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				progressBar1.setVisibility(View.GONE);
			}

		}

	}

	public class MyAdapter extends BaseAdapter {

		ArrayList<SearchPojo> stores = new ArrayList<SearchPojo>();
		ArrayList<SearchPojo> storedeatils = new ArrayList<SearchPojo>();

		Context ctx;
		LayoutInflater inf;

		public MyAdapter(Context ctx, ArrayList<SearchPojo> ddd) {
			// TODO Auto-generated constructor stub

			this.ctx = ctx;
			this.stores = ddd;
			inf = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			try {
				storedeatils.addAll(stores);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return stores.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = arg1;
			ViewHolder holder = null;
			try {

				if (view == null) {

					view = inf.inflate(R.layout.search_store_items, null);
					holder = new ViewHolder();
					holder.storename = (TextView) view
							.findViewById(R.id.textView1);
					holder.storeaddress = (TextView) view
							.findViewById(R.id.textView2);
					holder.storename.setTypeface(typeface_semibold);
					holder.storeaddress.setTypeface(typeface_regular);
					view.setTag(holder);

				} else {

					holder = (ViewHolder) view.getTag();
				}

				try {

					holder.storename.setText(stores.get(arg0).getStorename()
							.trim()
							+ ",");
					holder.storeaddress.setText(stores.get(arg0).getStoreadd()
							.trim());
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return view;
		}

		// Filter Class
		public void filter(String charText) {

			try {

				charText = charText.toLowerCase(Locale.getDefault());
				stores.clear();
				if (charText.length() == 0) {
					stores.addAll(storedeatils);
				} else {
					for (SearchPojo wp : storedeatils) {
						if (wp.getStorename().toLowerCase(Locale.getDefault())
								.startsWith(charText)) {
							stores.add(wp);
							datacheck = true;
						}
					}
				}
				notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

	}

	static class ViewHolder {

		TextView storename;
		TextView storeaddress;
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(final Context context, String title,
			String message, Boolean status) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		// Setting Dialog Title
		alertDialog.setTitle("No Internet Connection!");

		// Setting Dialog Message
		alertDialog.setMessage("Enable Internet Connection.");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_SETTINGS);
						context.startActivity(intent);
						dialog.cancel();
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Dismiss",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
}
