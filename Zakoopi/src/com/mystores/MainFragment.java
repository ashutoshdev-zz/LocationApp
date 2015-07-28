package com.mystores;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.store.model.StoreDetail;
import com.store.model.Stores;
import com.zakoopi.R;
import com.zakoopi.helper.CustomScrollView;

public class MainFragment extends Fragment {

	LinearLayout lin1, lin2, lin3, lin4;
	RelativeLayout rel_feature, rel_genral, rel_review;
	private static String LAST_UPDATE_URL = "";
	static AsyncHttpClient client = new AsyncHttpClient();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	public static Stores stores;
	public static StoreDetail detail;
	TextView store_name, store_address, store_rate, store_like, store_timings,
			store_close, call,rate, review, follow, store_general, store_featured, store_review;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	View v1, v2, v3;
	public static CustomScrollView scroll;
	RelativeLayout rateme, reviewme, rel_rate_box;
	TextView ratecount;
	Dialog diaog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.main_fragment, null);
		lin1 = (LinearLayout) view.findViewById(R.id.lin1);
		lin2 = (LinearLayout) view.findViewById(R.id.lin2);
		lin3 = (LinearLayout) view.findViewById(R.id.lin3);
		lin4 = (LinearLayout) view.findViewById(R.id.lin4);
		rel_genral = (RelativeLayout) view.findViewById(R.id.tt1);
		rel_feature = (RelativeLayout) view.findViewById(R.id.tt2);
		rel_review = (RelativeLayout) view.findViewById(R.id.tt3);
		
		
		
		LAST_UPDATE_URL = getString(R.string.base_url)+"stores/view/"+StoreActivity.store_id+".json";
		//LAST_UPDATE_URL = getString(R.string.base_url)+"stores/view/"+844+".json";
		
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
		
		
		store_name = (TextView) view.findViewById(R.id.txt_store_name);
		store_address = (TextView) view.findViewById(R.id.store_location);
		store_rate = (TextView) view.findViewById(R.id.txt_rate);
		store_like = (TextView) view.findViewById(R.id.txt_vote);
		store_timings = (TextView) view.findViewById(R.id.txt_store_time);
		store_close = (TextView) view.findViewById(R.id.txt_store_close_day);
		call = (TextView) view.findViewById(R.id.txt_call);
		rate = (TextView) view.findViewById(R.id.txt_rate1);
		review = (TextView) view.findViewById(R.id.txt_review);
		follow = (TextView) view.findViewById(R.id.txt_follow);
		store_general = (TextView) view.findViewById(R.id.textView1);
		store_featured = (TextView) view.findViewById(R.id.textView2);
		store_review = (TextView) view.findViewById(R.id.textView3);
		scroll = (CustomScrollView) view.findViewById(R.id.scrollView1);
		rateme = (RelativeLayout) view.findViewById(R.id.rel_rate1);
		reviewme = (RelativeLayout) view.findViewById(R.id.rel_review);
		rel_rate_box = (RelativeLayout) view.findViewById(R.id.rel_rated_box1);

		v1 = (View) view.findViewById(R.id.view111);
		v2 = (View) view.findViewById(R.id.view11);
		v3 = (View) view.findViewById(R.id.view14);
		
		/**
		 * Set Typeface on TextView
		 */
		store_name.setTypeface(typeface_semibold);
		store_address.setTypeface(typeface_regular);
		store_rate.setTypeface(typeface_bold);
		store_like.setTypeface(typeface_regular);
		store_timings.setTypeface(typeface_regular);
		store_close.setTypeface(typeface_regular);
		call.setTypeface(typeface_regular);
		rate.setTypeface(typeface_regular);
		review.setTypeface(typeface_regular);
		follow.setTypeface(typeface_regular);
		store_general.setTypeface(typeface_semibold);
		store_featured.setTypeface(typeface_semibold);
		store_review.setTypeface(typeface_semibold );
		

		getStoresInfo();
		lin1.setVisibility(View.VISIBLE);
		lin2.setVisibility(View.GONE);
		lin3.setVisibility(View.GONE);
		lin4.setVisibility(View.GONE);

		store_general.setTextColor(Color.BLACK);
		store_featured.setTextColor(Color.GRAY);
		store_review.setTextColor(Color.GRAY);
		v1.setBackgroundResource(R.drawable.store_rating_bar_blue);
		v2.setBackgroundResource(R.drawable.store_rating_bar);
		v3.setBackgroundResource(R.drawable.store_rating_bar);
		
		rateme.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rateDialog();
			}
		});

		reviewme.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), ReviewMe.class);
				startActivity(in);
			}
		});

		rel_feature.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				lin1.setVisibility(View.GONE);
				lin2.setVisibility(View.VISIBLE);
				lin3.setVisibility(View.GONE);
				lin4.setVisibility(View.GONE);

				store_general.setTextColor(Color.GRAY);
				store_featured.setTextColor(Color.BLACK);
				store_review.setTextColor(Color.GRAY);
				v1.setBackgroundResource(R.drawable.store_rating_bar);
				v2.setBackgroundResource(R.drawable.store_rating_bar_blue);
				v3.setBackgroundResource(R.drawable.store_rating_bar);
			

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Feature gen = new Feature();
				Bundle bnd = new Bundle();
				bnd.putBoolean("gen23", false);
				gen.setArguments(bnd);
				ft.replace(R.id.lin2, gen);
				ft.commit();
			}
		});

		rel_genral.setOnClickListener(new View.OnClickListener() {
			//
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				lin1.setVisibility(View.VISIBLE);
				lin2.setVisibility(View.GONE);
				lin3.setVisibility(View.GONE);
				lin4.setVisibility(View.GONE);

				store_general.setTextColor(Color.BLACK);
				store_featured.setTextColor(Color.GRAY);
				store_review.setTextColor(Color.GRAY);
				v1.setBackgroundResource(R.drawable.store_rating_bar_blue);
				v2.setBackgroundResource(R.drawable.store_rating_bar);
				v3.setBackgroundResource(R.drawable.store_rating_bar);
				

				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				General gen = new General();
				Bundle bnd = new Bundle();
				bnd.putBoolean("gen23", false);
				gen.setArguments(bnd);
				ft.replace(R.id.lin1, gen);
				ft.commit();
			}
		});

		

		rel_review.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				lin1.setVisibility(View.GONE);
				lin2.setVisibility(View.GONE);
				lin3.setVisibility(View.VISIBLE);
				lin4.setVisibility(View.GONE);

				store_general.setTextColor(Color.GRAY);
				store_featured.setTextColor(Color.GRAY);
				store_review.setTextColor(Color.BLACK);
				v1.setBackgroundResource(R.drawable.store_rating_bar);
				v2.setBackgroundResource(R.drawable.store_rating_bar);
				v3.setBackgroundResource(R.drawable.store_rating_bar_blue);
				
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				Review gen = new Review();
				Bundle bnd = new Bundle();
				bnd.putBoolean("gen2333", false);
				gen.setArguments(bnd);
				ft.replace(R.id.lin3, gen);
				ft.commit();
			}
		});
		return view;
	}

	public void getStoresInfo() {
		client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		client.get(LAST_UPDATE_URL, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started
				loadDialog();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));

					String line = "";
					String text = "";

					while ((line = br.readLine()) != null) {

						text = text + line;

					}
                    diaog.dismiss();
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
				 diaog.dismiss();
				// bar.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		stores = gson.fromJson(reader, Stores.class);
		detail = stores.getStore();
		Log.e("jjjjjj",detail.getStore_name() + "::" + detail.getStore_address() + "::" + detail.getOverall_ratings());
		store_name.setText(detail.getStore_name());
		store_address.setText(detail.getStore_address() + ","
				+ detail.getArea() + "," + detail.getPin_code());
		
		String rated_color = detail.getRated_color();
		rel_rate_box.setBackgroundResource(R.drawable.rating_box_0);
		rel_rate_box.setBackgroundColor(Color.parseColor(rated_color));
		store_rate.setText(detail.getOverall_ratings());
		/*int rate_avg = Integer.parseInt(detail.getOverall_ratings());
		Log.e("RATE", ""+rate_avg);
		if (rate_avg == 0 || rate_avg <= 0.5) {
			Log.e("RATE__0", ""+rate_avg);
			if (rate_avg == 0) {
				rel_rate_box.setBackgroundResource(R.drawable.rating_box_0);
				store_rate.setText("-");
				Log.e("RATE__if", ""+rate_avg);
			} else {
				rel_rate_box.setBackgroundResource(R.drawable.rating_box_0);
				store_rate.setText(""+rate_avg);
				Log.e("RATE__else", ""+rate_avg);
			}
						
		} else if (rate_avg == 0.6 || rate_avg <= 1) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_0_6);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_0.6", ""+rate_avg);
			
		} else if (rate_avg == 1.1 || rate_avg <= 1.5) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_1_1);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_1.1", ""+rate_avg);
			
		} else if (rate_avg == 1.6 || rate_avg <= 2) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_1_6);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_1.6", ""+rate_avg);
			
		} else if (rate_avg == 2.1 || rate_avg <= 2.5) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_2_1);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_2.1", ""+rate_avg);
			
		} else if (rate_avg == 2.6 || rate_avg <= 3) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_2_6);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_2.6", ""+rate_avg);
			
		} else if (rate_avg == 3.1 || rate_avg <= 3.5) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_3_1);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_3.1", ""+rate_avg);
			
		} else if (rate_avg == 3.6 || rate_avg <= 4) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_3_6);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_3.6", ""+rate_avg);
			
		} else if (rate_avg == 4.1 || rate_avg <= 4.5) {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_4_1);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_4.1", ""+rate_avg);
			
		} else {
			rel_rate_box.setBackgroundResource(R.drawable.rating_box_4_6);
			store_rate.setText(""+rate_avg);
			Log.e("RATE__else_if_4.6", ""+rate_avg);
		}*/
		
		
		
		//store_rate.setText(detail.getOverall_ratings());
		store_like.setText(detail.getLikes_count() + " Votes");
		store_timings.setText(detail.getStore_timing_from() + " - "
				+ detail.getStore_timing_to());
		store_close.setText(detail.getClosed_day());

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		General gen = new General();
		Bundle bnd = new Bundle();
		bnd.putBoolean("gen", false);
		gen.setArguments(bnd);
		ft.replace(R.id.lin1, gen);
		ft.commit();
	}

	public void rateDialog() {

		final Dialog dd = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setCancelable(false);
		dd.getWindow().setGravity(Gravity.TOP);
		dd.setContentView(R.layout.ratestore_dialog);
		dd.show();

		ratecount = (TextView) dd.findViewById(R.id.textView2);
		final TextView ratemsg = (TextView) dd.findViewById(R.id.textView3);
		final Button cnl = (Button) dd.findViewById(R.id.button1);
		final Button smt = (Button) dd.findViewById(R.id.button2);
		final SeekBar ratebar = (SeekBar) dd.findViewById(R.id.seekBar1);
		ratebar.setMax(10);
		ratebar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// add here your implementation
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// add here your implementation
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				ratecount.setText(getConvertedValue(progress) + "");
			}
		});
		
		cnl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dd.dismiss();
			}
		});
		
smt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dd.dismiss();
			}
		});
	}
	
	public void loadDialog() {

		diaog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		diaog.setCancelable(false);
		diaog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		diaog.setCancelable(false);
		diaog.getWindow().setGravity(Gravity.FILL_VERTICAL);
		diaog.setContentView(R.layout.load_dialog);
		diaog.show();

		
	}
	
	public float getConvertedValue(int intVal){
	    float floatVal = (float) 0.0;
	    floatVal = .5f * intVal;
	    return floatVal;
	}
}
