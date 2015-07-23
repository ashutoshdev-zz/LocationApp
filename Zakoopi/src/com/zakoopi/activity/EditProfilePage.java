package com.zakoopi.activity;

import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.helper.CircleImageView;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditProfilePage extends FragmentActivity {

	RelativeLayout rel_back, rel_submit;
	TextView txt_edit_profile, txt_submit, txt_user_name, txt_location_name,
			txt_connected_ac, txt_fb, txt_google_plus, txt_twitter,
			txt_connect, txt_instagram, txt_connect1;
	EditText edt_yourself;
	CircleImageView user_pic;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_edit_user);
		
		user_pref();
		findId();
		setTypeface();
		setUserPref_data();
		click();

	}
	
	/**
	 * User Login SharedPreferences
	 */
	
	public void user_pref(){
	pro_user_pref = getSharedPreferences("User_detail", 0);
	pro_user_pic_url = pro_user_pref.getString("user_image", "123");
	pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
			+ pro_user_pref.getString("user_lastName", "458");
	pro_user_location = pro_user_pref.getString("user_location", "4267");
	}
	
	/**
	 * Set User Pref Data
	 */
	public void setUserPref_data(){
		Picasso.with(getApplicationContext()).load(pro_user_pic_url)
		.into(user_pic);
		txt_user_name.setText(pro_user_name);
		txt_location_name.setText(pro_user_location);
		
	}

	/**
	 * Find ID's
	 */

	public void findId() {
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		rel_submit = (RelativeLayout) findViewById(R.id.rel_submit);
		txt_edit_profile = (TextView) findViewById(R.id.txt_edit_profile);
		txt_submit = (TextView) findViewById(R.id.txt_submit);
		txt_user_name = (TextView) findViewById(R.id.txt_user_name);
		txt_location_name = (TextView) findViewById(R.id.txt_location_name);
		txt_connected_ac = (TextView) findViewById(R.id.txt_connected_ac);
		txt_fb = (TextView) findViewById(R.id.txt_fb);
		txt_google_plus = (TextView) findViewById(R.id.txt_google_plus);
		txt_twitter = (TextView) findViewById(R.id.txt_twitter);
		txt_connect = (TextView) findViewById(R.id.txt_connect);
		txt_instagram = (TextView) findViewById(R.id.txt_instagram);
		txt_connect1 = (TextView) findViewById(R.id.txt_connect1);
		edt_yourself = (EditText) findViewById(R.id.edt_yourself);
		user_pic = (CircleImageView) findViewById(R.id.img_profile_pic);
	}
	
	/**
	 * Typeface
	 */
	public void type_face(){
	
	typeface_semibold = Typeface.createFromAsset(getAssets(),
			"fonts/SourceSansPro-Semibold.ttf");
	typeface_black = Typeface.createFromAsset(getAssets(),
			"fonts/SourceSansPro-Black.ttf");
	typeface_bold = Typeface.createFromAsset(getAssets(),
			"fonts/SourceSansPro-Bold.ttf");
	typeface_light = Typeface.createFromAsset(getAssets(),
			"fonts/SourceSansPro-Light.ttf");
	typeface_regular = Typeface.createFromAsset(getAssets(),
			"fonts/SourceSansPro-Regular.ttf");
	}
	
	/**
	 * Set Font on TextView
	 */
	
	public void setTypeface(){
		
		txt_edit_profile.setTypeface(typeface_semibold);
		txt_submit.setTypeface(typeface_bold);
		txt_user_name.setTypeface(typeface_semibold);
		txt_location_name.setTypeface(typeface_semibold);
		txt_connected_ac.setTypeface(typeface_bold);
		txt_fb.setTypeface(typeface_semibold);
		txt_google_plus.setTypeface(typeface_semibold);
		txt_twitter.setTypeface(typeface_semibold);
		txt_connect.setTypeface(typeface_regular);
		txt_instagram.setTypeface(typeface_semibold);
		txt_connect1.setTypeface(typeface_regular);
		edt_yourself.setTypeface(typeface_regular);
		
	}

	/**
	 * Click Listener
	 */

	public void click() {
		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		rel_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
