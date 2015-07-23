package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zakoopi.R;
import com.zakoopi.activity.UserStoreAndExperiences;
import com.zakoopi.helper.ExpandableHeightGridView;
import com.zakoopi.helper.MaterialProgressBar;
import com.zakoopi.roundimagelib.SelectableRoundedImageView;
import com.zakoopi.search.TopMarket;
import com.zakoopi.search.marketsTop;
import com.zakoopi.utils.ClientHttp;

public class HomeSearchTopMarket extends Fragment {
	//private static final String ALL_TOP_MARKET_REST_URL = "http://v3.zakoopi.com/api/markets/top.json";
	private static String ALL_TOP_MARKET_REST_URL = " ";
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email, user_password;
	ArrayList<marketsTop> marketsTop = new ArrayList<marketsTop>();
	MaterialProgressBar progressBar, bar;
	ExpandableHeightGridView grid;
	Typeface typeface_semibold;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_search_market_trend, null);
		grid = (ExpandableHeightGridView) view.findViewById(R.id.grid_view);
		grid.setFocusable(false);
		progressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setColorSchemeResources(R.color.red, R.color.green,
				R.color.blue, R.color.orange);
		
		ALL_TOP_MARKET_REST_URL = getString(R.string.base_url)+"markets/top.json";
		
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
		
		
		
		top_markets();
		click();

		return view;
	}

	/**
	 * {@code} click()
	 * 
	 * @return void
	 */
	public void click() {
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String market_id = marketsTop.get(position).get_id();
				Intent intent = new Intent(getActivity(),
						UserStoreAndExperiences.class);
				intent.putExtra("market_id", market_id);
				startActivity(intent);
			}
		});
	}

	/**
	 * {@code} top_markets()
	 * 
	 * @return void
	 */
	public void top_markets() {
		Log.e("Top Market Url", ALL_TOP_MARKET_REST_URL);
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(ALL_TOP_MARKET_REST_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub

				try {
					BufferedReader br1 = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = br1.readLine()) != null) {

						text = text + line;
					}
					// Log.e("url", "onSuccessArea");
					topMarket_showData(text);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * @return void
	 * @topMarket_showData data
	 */
	public void topMarket_showData(String data) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(data));
		jsonReader.setLenient(true);

		TopMarket top_market = gson.fromJson(jsonReader, TopMarket.class);
		marketsTop = top_market.getTopMarket();
		// Log.e("Top_Market_Size", "" + marketsTop.size());
		progressBar.setVisibility(View.GONE);
		grid.setAdapter(new GridTopMarketAdapter());
		grid.setExpanded(true);
		new GridTopMarketAdapter().notifyDataSetChanged();
	}

	/**
	 * Class GridTopMarketAdapter
	 * 
	 * @author ZakoopiUser
	 * 
	 */
	public class GridTopMarketAdapter extends BaseAdapter {
		DisplayImageOptions options;
		
		public GridTopMarketAdapter() {
			super();

			 options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.color.maroon)
					.delayBeforeLoading(500)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return marketsTop.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return marketsTop.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Log.e("TopM", "TopM");
			View view = convertView;
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			view = inflater.inflate(R.layout.home_search_grid_item, null);

			typeface_semibold = Typeface.createFromAsset(view.getContext()
					.getAssets(), "fonts/SourceSansPro-Semibold.ttf");
			SelectableRoundedImageView img_background = (SelectableRoundedImageView) view
					.findViewById(R.id.img_background);
			TextView txt_market_name = (TextView) view
					.findViewById(R.id.txt_grid_item);
			txt_market_name.setText(marketsTop.get(position).get_market_name());
			txt_market_name.setTypeface(typeface_semibold);
			
			
			ImageLoader.getInstance().displayImage(
					marketsTop.get(position).get_android_api_image(),
					img_background, options);

			return view;
		}

	}
	
	


}
