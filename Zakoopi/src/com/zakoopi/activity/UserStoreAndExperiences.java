package com.zakoopi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.fragments.SearchResultStoreFragments;
import com.zakoopi.helper.CircleImageView;

public class UserStoreAndExperiences extends FragmentActivity {

	RelativeLayout rel_store, rel_exp;
	TextView txt_store, txt_exp;
	View view_store, view_exp;
	LinearLayout lin_store, lin_exp;
	ImageView img_menu;
	RelativeLayout rel_back, rel_edt_profile;
	TextView txt_topbar, txt_city_name;
	SimpleSideDrawer slide_me;
	//MaterialProgressBar progressBar;
	SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location;
	CircleImageView pro_user_pic;
	TextView txt_pro_user_name;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_result_store_main);

		/**
		 * Login User Preferences
		 */
		pro_user_pref = getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");

		/**
		 * Get Data using Intent
		 */
		Intent i = getIntent();
		String market_id = i.getStringExtra("market_id");
		//Log.e("id_user", market_id);

		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_regular = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		
		slide_me = new SimpleSideDrawer(this);
		findId();
		//progressBar.setVisibility(View.VISIBLE);
	//	progressBar.setColorSchemeResources(R.color.red, R.color.green,
	//			R.color.blue, R.color.orange);
		/**
		 * Set Font on TextView
		 */
		txt_topbar.setTypeface(typeface_semibold);
	//	txt_city_name.setTypeface(typeface_regular);
		txt_exp.setTypeface(typeface_semibold);
		txt_store.setTypeface(typeface_semibold);
		
		lin_store.setVisibility(View.VISIBLE);
		lin_exp.setVisibility(View.GONE);

		txt_topbar.setText("Store & Experiences");
		Picasso.with(getApplicationContext()).load(pro_user_pic_url)
				.into(pro_user_pic);
		txt_pro_user_name.setText(pro_user_name);
		//txt_city_name.setText(pro_user_location);
		
		/**
		 * set Fragment SearchResultStoreFragments()
		 */
		Fragment fr;
		fr = new SearchResultStoreFragments();
		Bundle bundle = new Bundle();
		bundle.putString("market_id", market_id);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		fr.setArguments(bundle);
	//	progressBar.setVisibility(View.GONE);
		ft.add(R.id.lin_search_store, fr);
		ft.commit();

		 click();
	}

	/**
	 * Find ID's
	 * 
	 * @return void
	 */

	public void findId() {

		rel_store = (RelativeLayout) findViewById(R.id.rel_search_store);
		rel_exp = (RelativeLayout) findViewById(R.id.rel_search_exp);
		txt_store = (TextView) findViewById(R.id.txt_store);
		txt_exp = (TextView) findViewById(R.id.txt_exp);
		view_store = (View) findViewById(R.id.view_store);
		view_exp = (View) findViewById(R.id.view_exp);
		lin_store = (LinearLayout) findViewById(R.id.lin_search_store);
		lin_exp = (LinearLayout) findViewById(R.id.lin_search_expe);

		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		img_menu = (ImageView) findViewById(R.id.img_menu);
		txt_topbar = (TextView) findViewById(R.id.txt);
		txt_city_name = (TextView) findViewById(R.id.txt_city_name);
		slide_me.setRightBehindContentView(R.layout.side_menu);
		pro_user_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
		txt_pro_user_name = (TextView) findViewById(R.id.txt_user_name);
		rel_edt_profile = (RelativeLayout) findViewById(R.id.rel_edt_profile);
		
		//progressBar = (MaterialProgressBar) findViewById(R.id.progressBar);
		
	}

	/**
	 * click()
	 * 
	 * @return void
	 */
	public void click() {

		rel_store.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_store.setTextColor(Color.parseColor("#4d4d49"));
				txt_exp.setTextColor(Color.parseColor("#acacac"));
				view_store.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_exp.setBackgroundColor(Color.parseColor("#acacac"));
			}
		});

		rel_exp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				txt_exp.setTextColor(Color.parseColor("#4d4d49"));
				txt_store.setTextColor(Color.parseColor("#acacac"));
				view_exp.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_store.setBackgroundColor(Color.parseColor("#acacac"));
			}
		});

		rel_edt_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent edt_profile = new Intent(UserStoreAndExperiences.this,
						EditProfilePage.class);
				startActivity(edt_profile);

			}
		});
		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		img_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slide_me.toggleRightDrawer();
			}
		});
	}

}
