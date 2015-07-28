package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mystores.StoreActivity;
import com.zakoopi.R;
import com.zakoopi.helper.ExpandedListView;
import com.zakoopi.helper.MaterialProgressBar;
import com.zakoopi.searchResult.MarketResult;
import com.zakoopi.searchResult.StoreDetail;
import com.zakoopi.searchResult.TopMarket;
import com.zakoopi.utils.ClientHttp;

public class SearchResultStoreFragments extends Fragment {

	
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;;
	InputStream input;
	ExpandedListView lin_store_details;
	ArrayList<StoreDetail> details;
	String market_id, store_id;;
	//MaterialProgressBar progressBar;
	TextView txt_store;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	Dialog diaog;
	MaterialProgressBar bar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.search_result_store_inner, null);
		market_id = this.getArguments().getString("market_id");
		
		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-Semibold.ttf");
		
		lin_store_details = (ExpandedListView) view
				.findViewById(R.id.lin_store_detail_view);
		//progressBar = (MaterialProgressBar) view.findViewById(R.id.progressBar);
		//progressBar.setVisibility(View.VISIBLE);
		//progressBar.setColorSchemeResources(R.color.red, R.color.green,
		//		R.color.blue, R.color.orange);
		txt_store = (TextView) view.findViewById(R.id.txt_store);
		txt_store.setTypeface(typeface_semibold);
		
		search_store();
		click();
		
		return view;
	}
	
	/**
	 * Click Listener
	 */
	
	public void click(){
		lin_store_details.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent store_view = new Intent(getActivity(),StoreActivity.class);
				store_view.putExtra("store_id", details.get(position).getId());
				startActivity(store_view);
				
			}
		});
	}

	/**
	 * {@code} search_store()
	 * @return void
	 * 
	 */
	public void search_store() {
		String url=getString(R.string.base_url)+"markets/view/";
		client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(url + market_id
				+ ".json", new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			loadDialog();
				lin_store_details.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub
				try {
					BufferedReader bf = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bf.readLine()) != null) {
						text2 = text2 + text1;
					}
					
					show_store_details(text2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {
				diaog.dismiss();
			}
		});
	}

	/**
	 * 
	 * @show_store_details store_data
	 * @return void
	 */
	public void show_store_details(String store_data) {

		Gson store_gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(store_data));
		jsonReader.setLenient(true);
		TopMarket tm = store_gson.fromJson(jsonReader, TopMarket.class);
		MarketResult marketResult = tm.getMarket();
		details = marketResult.getStoreDetails();
		

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				lin_store_details.setAdapter(new ListAdapter1());
				diaog.dismiss();
				lin_store_details.setVisibility(View.VISIBLE);
			}
		}, 5000);
		
		
	}

	/**
	 * class ListAdapter1 extends BaseAdapter
	 * @author ZakoopiUser
	 *
	 */
	public class ListAdapter1 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return details.size();
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

	
		@SuppressLint("ViewHolder")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder holder = null;
			if (view == null) {
	
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			view = inflater.inflate(R.layout.user_store_item_cardview, null);
			holder = new ViewHolder();
			typeface_semibold = Typeface.createFromAsset(view.getContext().getAssets(),"fonts/SourceSansPro-Semibold.ttf" );
			typeface_bold = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Bold.ttf");
			typeface_regular = Typeface.createFromAsset(view.getContext().getAssets(),"fonts/SourceSansPro-Regular.ttf");
			
			holder.txtStoreName = (TextView) view
					.findViewById(R.id.txt_store_name);
			holder.txtStoreAddress = (TextView) view
					.findViewById(R.id.store_location);
			holder.txtRating = (TextView) view.findViewById(R.id.txt_rate);
			holder.txtLookbookCount = (TextView) view
					.findViewById(R.id.txt_lookbook_count);
			holder.txtReviewCount = (TextView) view
					.findViewById(R.id.txt_review_count);
			holder.txtPhotoCount = (TextView) view
					.findViewById(R.id.txt_photo_count);

			holder.txtLookbookCount.setTypeface(typeface_regular);
			holder.txtPhotoCount.setTypeface(typeface_regular);
			holder.txtRating.setTypeface(typeface_bold);
			holder.txtReviewCount.setTypeface(typeface_regular);
			holder.txtStoreAddress.setTypeface(typeface_regular);
			holder.txtStoreName.setTypeface(typeface_semibold);
			
			view.setTag(holder);
			} else {
				holder = (ViewHolder)view.getTag();
			}
			holder.txtStoreName.setText(details.get(position).getStoreName());
			holder.txtStoreAddress.setText(details.get(position).getMarket() + ", "
					+ details.get(position).getArea());
			holder.txtRating.setText(details.get(position).getRating());
			holder.txtReviewCount.setText(details.get(position).getReviewCount());
			holder.txtLookbookCount.setText(details.get(position).getLookbookCount());
			holder.txtPhotoCount.setText(details.get(position).getImageCount());

			return view;
		}

	}
	
	public class ViewHolder {
		TextView txtStoreName, txtStoreAddress, txtRating, txtLookbookCount, txtReviewCount, txtPhotoCount;
		ImageView img_follow;
	}
	
	public void loadDialog() {

		diaog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		diaog.setCancelable(false);
		diaog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		diaog.setCancelable(false);
		diaog.getWindow().setGravity(Gravity.FILL_VERTICAL);
		diaog.setContentView(R.layout.load_dialog);
		bar = (MaterialProgressBar) diaog.findViewById(R.id.progressBar1);
		bar.setColorSchemeResources(R.color.red, R.color.green,
				R.color.blue, R.color.orange);
		diaog.show();

		
	}

}
