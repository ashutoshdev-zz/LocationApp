package com.mystores;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.activity.EditProfilePage;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.MaterialProgressBar;

public class StoreActivity extends FragmentActivity {

	CharSequence Titles[] = { "General", "Featured In", "Reviews" };
	int Numboftabs = 3;
	public static String store_id;
	Typeface font_regular, font_semibold;
	ImageView img_menu;
	RelativeLayout rel_back,rel_edt_profile;
	TextView txt_topbar;
	SimpleSideDrawer slide_me;
	MaterialProgressBar progressBar;
	SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name;
	CircleImageView pro_user_pic;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac, side_sign_out, side_about, side_sug_store, side_rate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stores);
		
		/**
		 * Intent Data
		 */
		Intent i = getIntent();
		store_id = i.getStringExtra("store_id");
		
		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");

		
		/**
		 * TopBar
		 */
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		img_menu = (ImageView) findViewById(R.id.img_menu);
		txt_topbar = (TextView) findViewById(R.id.txt);
		txt_topbar.setText("Store");
		
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
		 * Menu Click
		 */
		rel_edt_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent edt_profile = new Intent(StoreActivity.this, EditProfilePage.class);
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
		
		/**
		 * Set Typeface
		 */
		txt_topbar.setTypeface(typeface_semibold);
		side_user_name.setTypeface(typeface_bold);
		side_about.setTypeface(typeface_regular);
		side_con_ac.setTypeface(typeface_regular);
		side_edt_profile.setTypeface(typeface_regular);
		side_noti_settings.setTypeface(typeface_regular);
		side_rate.setTypeface(typeface_regular);
		side_sign_out.setTypeface(typeface_regular);
		side_sug_store.setTypeface(typeface_regular);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment main = new MainFragment();
		Bundle bnd = new Bundle();
		bnd.putBoolean("main", false);
		main.setArguments(bnd);
		ft.add(R.id.frag, main);
		ft.commit();
	}
	
	

}
