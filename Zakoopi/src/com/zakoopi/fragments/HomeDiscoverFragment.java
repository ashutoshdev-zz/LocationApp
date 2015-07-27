package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.database.HomeSearchAllAreaDatabaseHandler;
import com.zakoopi.database.HomeSearchAllProductDatabaseHandler;
import com.zakoopi.helper.HomeSearchSpinnerSectionStructure;
import com.zakoopi.helper.MaterialProgressBar;
import com.zakoopi.helper.Variables;
import com.zakoopi.search.AllArea;
import com.zakoopi.search.AllProduct;
import com.zakoopi.search.Area;
import com.zakoopi.search.marketsSearch;
import com.zakoopi.utils.ClientHttp;

public class HomeDiscoverFragment extends Fragment {
	LinearLayout lin1, lin2;
	RelativeLayout rel2_pop, rel3_rec, rel_first, rel_search_second;
	TextView txt_market, txt_trends, txt_search, some_txt, some_txt1;
	View view_market, view_trends;
	AutoCompleteTextView edt_search;
	// private static String ALL_AREA_REST_URL =
	// "http://v3.zakoopi.com/api/markets.json";
	private static String ALL_AREA_REST_URL = "";
	private static String LAST_UPDATE_REST_URL = "";
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
	user_password;
	String text = "";
	String line = "";
	List<AllProduct> list = new ArrayList<AllProduct>();
	HomeSearchAllProductDatabaseHandler home_search_product_db;
	HomeSearchAllAreaDatabaseHandler home_search_area_db;
	MaterialProgressBar bar;
	String name, category, market_name;
	Spinner search_spinner_area;
	Spinner search_spinner_product;
	String[] sectionHeader = { "All Product", "Women", "Men", "Kids" };
	List<marketsSearch> markets = new ArrayList<marketsSearch>();
	ArrayList<HomeSearchSpinnerSectionStructure> sectionList = new ArrayList<HomeSearchSpinnerSectionStructure>();
	HomeSearchSpinnerSectionStructure spinner_section_strcture;

	List<String> productList = new ArrayList<String>();
	//List<String> areaList = new ArrayList<String>();
	List<String> all_area = new ArrayList<String>();

	int sp_position;
	View view;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_search_frag_main, null);

		findId();

		lin1.setVisibility(View.VISIBLE);
		lin2.setVisibility(View.GONE);

		rel_first.setFocusable(true);
		
		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
		
		/*
		 * home_search_product_db = new HomeSearchAllProductDatabaseHandler(
		 * getActivity()); home_search_area_db = new
		 * HomeSearchAllAreaDatabaseHandler( getActivity());
		 */
		Log.e("onCREATE", "DISCOVER_onCreate");
		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");

		setFont();

		/*
		 * home_search_area_db = new HomeSearchAllAreaDatabaseHandler(
		 * getActivity()); areaList = home_search_area_db.getAllAreas1();
		 * Log.e("AREALIST", ""+areaList); AreaAdapterClass adp = new
		 * AreaAdapterClass(getActivity(), areaList);
		 * search_spinner_area.setAdapter(adp);
		 */
		/**
		 * @author ZakoopiUser Add Fragment
		 */
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment top_market = new HomeSearchTopMarket();
		Bundle bundle = new Bundle();
		bundle.putBoolean("market", false);
		top_market.setArguments(bundle);
		ft.replace(R.id.lin_top_market, top_market);
		ft.commit();

		tab_click();

		return view;
	}

	@Override
	public void setUserVisibleHint(boolean visible) {
		super.setUserVisibleHint(visible);
		if (visible && isResumed()) {
			// Only manually call onResume if fragment is already visible
			// Otherwise allow natural fragment lifecycle to call onResume
			onResume();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!getUserVisibleHint()) {
			return;
		}
		home_search_product_db = new HomeSearchAllProductDatabaseHandler(
				getActivity());
		home_search_area_db = new HomeSearchAllAreaDatabaseHandler(
				getActivity());
		list = home_search_product_db.getAllProducts();
		new Allproduct().execute();
		all_area_city();

		

	}

	/**
	 * Find ID's
	 * 
	 * @return void
	 */

	public void findId() {
		rel2_pop = (RelativeLayout) view.findViewById(R.id.rel2_pop);
		rel3_rec = (RelativeLayout) view.findViewById(R.id.rel3_rec);
		rel_first = (RelativeLayout) view.findViewById(R.id.rel_first);
		rel_search_second = (RelativeLayout) view
				.findViewById(R.id.rel_search_second);
		txt_market = (TextView) view.findViewById(R.id.text_market);
		txt_trends = (TextView) view.findViewById(R.id.text_trends);
		txt_search = (TextView) view.findViewById(R.id.txt_search);
		some_txt = (TextView) view.findViewById(R.id.some_txt);
		some_txt1 = (TextView) view.findViewById(R.id.some_txt1);
		edt_search = (AutoCompleteTextView) view.findViewById(R.id.edt_search);
		view_market = (View) view.findViewById(R.id.view_market);
		view_trends = (View) view.findViewById(R.id.view_trends);
		search_spinner_product = (Spinner) view
				.findViewById(R.id.spinner_all_products);
		search_spinner_area = (Spinner) view
				.findViewById(R.id.spinner_all_areas);
		lin1 = (LinearLayout) view.findViewById(R.id.lin_top_market);
		lin2 = (LinearLayout) view.findViewById(R.id.lin_top_trends);
	}

	/**
	 * Set Font on TextView
	 */

	public void setFont() {

		txt_market.setTypeface(typeface_semibold);
		txt_trends.setTypeface(typeface_semibold);
		txt_search.setTypeface(typeface_regular);
		some_txt.setTypeface(typeface_regular);
		some_txt1.setTypeface(typeface_regular);
		edt_search.setTypeface(typeface_regular);
	}

	/**
	 * tab_click()
	 * 
	 * @return void
	 */
	public void tab_click() {

		rel3_rec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lin1.setVisibility(View.GONE);
				lin2.setVisibility(View.VISIBLE);

				txt_trends.setTextColor(Color.parseColor("#4d4d49"));
				txt_market.setTextColor(Color.parseColor("#acacac"));
				view_trends.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_market.setBackgroundColor(Color.parseColor("#acacac"));

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Fragment top_trends = new HomeSearchTopTrends();
				Bundle bundle = new Bundle();
				bundle.putBoolean("trend", false);
				top_trends.setArguments(bundle);
				ft.replace(R.id.lin_top_trends, top_trends);
				ft.commit();
			}
		});

		rel2_pop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lin1.setVisibility(View.VISIBLE);
				lin2.setVisibility(View.GONE);

				txt_market.setTextColor(Color.parseColor("#4d4d49"));
				txt_trends.setTextColor(Color.parseColor("#acacac"));
				view_market.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_trends.setBackgroundColor(Color.parseColor("#acacac"));

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Fragment top_market = new HomeSearchTopMarket();
				Bundle bundle = new Bundle();
				bundle.putBoolean("market", false);
				top_market.setArguments(bundle);
				ft.replace(R.id.lin_top_market, top_market);
				ft.commit();
			}
		});

		/*
		 * edt_search.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub final Dialog dialog = new Dialog(getActivity());
		 * dialog.setContentView(R.layout.search_autocomplete_dialog);
		 * ArrayAdapter<String> adapter = new
		 * ArrayAdapter<String>(getActivity(),
		 * R.layout.select_dialog_singlechoice_material,language);
		 * complete.setThreshold(3); complete.setAdapter(adapter);
		 * AutoCompleteTextView complete = (AutoCompleteTextView)
		 * dialog.findViewById(R.id.autoCompleteTextView1); ArrayAdapter<String>
		 * adapter = new ArrayAdapter<String>(getActivity(),
		 * R.layout.select_dialog_singlechoice_material,language);
		 * complete.setThreshold(3); complete.setAdapter(adapter);
		 * dialog.show(); } });
		 */
	}

	public class Allproduct extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < sectionHeader.length; i++) {

				productList = home_search_product_db
						.listDataByCategory(sectionHeader[i]);

				for (int j = 0; j < productList.size(); j++) {
					spinner_section_strcture = new HomeSearchSpinnerSectionStructure();

					if (j == 0) {

						spinner_section_strcture
								.setSectionName(sectionHeader[i]);
						spinner_section_strcture.setSectionValue("");
						sectionList.add(spinner_section_strcture);
					} else {

						if (i == 1 && j == 2) {

						} else {
							spinner_section_strcture.setSectionName("");
							spinner_section_strcture
									.setSectionValue(productList.get(j));
							sectionList.add(spinner_section_strcture);
						}

					}

				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			search_spinner_product.setAdapter(new AdapterClass(sectionList));
			
			new AdapterClass(sectionList).notifyDataSetChanged();

		}

	}

	/**
	 * 
	 * {@code} class AdapterClass extends BaseAdapter
	 * 
	 * @return view
	 */
	public class AdapterClass extends BaseAdapter {

		ArrayList<HomeSearchSpinnerSectionStructure> sectionList1 = new ArrayList<HomeSearchSpinnerSectionStructure>();
		LayoutInflater inf;

		public AdapterClass(ArrayList<HomeSearchSpinnerSectionStructure> list) {
			super();
			sectionList1 = list;
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sectionList1.size();
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

		@SuppressLint({ "ResourceAsColor", "ViewHolder" })
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			View vi = arg1;
			// inf = (LayoutInflater)
			// getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
			inf = LayoutInflater.from(getActivity());
			vi = inf.inflate(R.layout.home_search_spinner_item, null);
			// typeface_regular = Typeface.createFromAsset(vi.getContext()
			// .getAssets(), "fonts/SourceSansPro-Regular.ttf");
			// typeface_semibold = Typeface.createFromAsset(vi.getContext()
			// .getAssets(), "fonts/SourceSansPro-Semibold.ttf");
			TextView textView = (TextView) vi.findViewById(R.id.txt_product);
			RelativeLayout relativeLayout = (RelativeLayout) vi
					.findViewById(R.id.rel_1);
			if (sectionList1.get(arg0).getSectionValue() != null
					&& sectionList1.get(arg0).getSectionValue()
							.equalsIgnoreCase("")) {
				textView.setText(sectionList1.get(arg0).getSectionName());
				relativeLayout.setBackgroundColor(Color.TRANSPARENT);
				textView.setTextColor(Color.BLACK);
				textView.setTypeface(typeface_semibold);
				if (textView.getText().equals("All Product")) {
					textView.setTextColor(Color.GRAY);
					relativeLayout.setBackgroundColor(Color.WHITE);
				}

			} else {
				textView.setText(sectionList1.get(arg0).getSectionValue());
				relativeLayout.setBackgroundColor(Color.WHITE);
				textView.setTypeface(typeface_regular);
			}

			return vi;
		}
	}

	/**
	 * class AreaAdapterClass extends BaseAdapter
	 * 
	 * @author ZakoopiUser
	 * @return view
	 */
	// public class AreaAdapterClass extends ArrayAdapter<String> {
	//
	// List<String>list=new ArrayList<String>();
	// Context txt;
	// LayoutInflater inf;
	//
	//
	// public AreaAdapterClass(Context context, List<String> areaList) {
	// super(context, 0, areaList);
	// // TODO Auto-generated constructor stub
	//
	// list=areaList;
	// txt=context;
	// inf = LayoutInflater.from(txt);
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	//
	// TextView txt_area = null;
	//
	// if (convertView == null) {
	// convertView = inf.inflate(R.layout.home_search_spinner_item, null);
	// txt_area = (TextView) convertView.findViewById(R.id.txt_product);
	//
	// }
	//
	//
	// txt_area.setText(String.valueOf(list.get(position)));
	//
	// if (txt_area.getText().equals("All Area")) {
	// txt_area.setTextColor(Color.GRAY);
	// // relativeLayout.setBackgroundColor(Color.WHITE);
	// }
	// return convertView;
	//
	// }
	// }

	class AreaAdapterClass extends BaseAdapter {

		Context ctx;
		List<String> list;
		LayoutInflater inf;

		public AreaAdapterClass(Context ctx, List<String> list) {

			this.ctx = ctx;
			this.list = list;
			inf = (LayoutInflater) this.ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			convertView = inf.inflate(R.layout.home_search_spinner_item, null);
			TextView txt_area = (TextView) convertView
					.findViewById(R.id.txt_product);
			txt_area.setText(list.get(position));

			if (txt_area.getText().equals("All Area")) {
				txt_area.setTextColor(Color.GRAY);
				txt_area.setTypeface(typeface_semibold);
			}

			return convertView;
		}

	}
	
	
	/**
	 * {@code} all_area()
	 * 
	 * @return void
	 */
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

	/**
	 * 
	 * @allArea_showData data1
	 * @return void
	 */
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
		Log.e("AREA*****", ""+Variables.areaList);
		AreaAdapterClass adp = new AreaAdapterClass(getActivity(), Variables.areaList);
		search_spinner_area.setAdapter(adp);
		// Log.e("ADDALL", ""+home_search_area_db);
		/*
		 * areaList = home_search_area_db.getAllAreas1();
		 * 
		 * ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(),
		 * R.layout.home_search_spinner_item, R.id.txt_product, areaList);
		 * search_spinner_area.setAdapter(adp);
		 */
	}

}
