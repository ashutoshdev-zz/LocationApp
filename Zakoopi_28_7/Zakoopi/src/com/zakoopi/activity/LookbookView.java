package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.navdrawer.SimpleSideDrawer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.FlowLayout;
import com.zakoopi.lookbookView.LookbookRecent;
import com.zakoopi.lookbookView.RecentLookBookCardShow;
import com.zakoopi.lookbookView.RecentLookbookMain;

public class LookbookView extends Activity {
	String lookbook_id, user_name, user_pic_url, lookbook_title, view_count,
			like_count, comment_count = "10";
	TextView txt_user_name, txt_title, txt_view_count, txt_like_count,
			txt_comment_count;
	CircleImageView user_pic;
	static AsyncHttpClient client = new AsyncHttpClient();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;;
	InputStream input;
	ListView listView;
	ArrayList<RecentLookBookCardShow> details;
	ImageView img_menu;
	SimpleSideDrawer slide_me;
	String pro_user_pic_url, pro_user_name, pro_user_location,user_email, user_password;
	CircleImageView pro_user_pic;
	TextView txt_top_bar,  txt_review_submit;
	private SharedPreferences pro_user_pref;
	RelativeLayout rel_edt_profile, rel_back;
	Typeface typeface_semibold, typeface_bold, typeface_regular;
	EditText edt_review;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookbook_details);

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

		/**
		 * Intent Data
		 */
		Intent data = getIntent();
		lookbook_id = data.getStringExtra("lookbook_id");
		user_name = data.getStringExtra("username");
		user_pic_url = data.getStringExtra("userpicurl");
		lookbook_title = data.getStringExtra("title");
		view_count = data.getStringExtra("hits");
		like_count = data.getStringExtra("likes");
		// comment_count = data.getStringExtra("comment_count");

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
		 * TopBar
		 */
		img_menu = (ImageView) findViewById(R.id.img_menu);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_top_bar = (TextView) findViewById(R.id.txt);
		txt_top_bar.setText("Lookbook View");

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
		 * Menu ClickListener
		 */
		rel_edt_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent edt_profile = new Intent(LookbookView.this,
						EditProfilePage.class);
				startActivity(edt_profile);

			}
		});

		img_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slide_me.toggleRightDrawer();
			}
		});

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		/**
		 * Find ID
		 */
		listView = (ListView) findViewById(R.id.list_lookbook);

		LayoutInflater inflater_header = LayoutInflater
				.from(getApplicationContext());
		LayoutInflater inflater_footer = LayoutInflater
				.from(getApplicationContext());
		View root_header = inflater_header.inflate(
				R.layout.lookbook_view_header, null);
		View root_footer = inflater_footer.inflate(
				R.layout.lookbook_view_footer, null);

		txt_user_name = (TextView) root_header.findViewById(R.id.txt_user_name);
		user_pic = (CircleImageView) root_header
				.findViewById(R.id.img_user_profile);
		txt_title = (TextView) root_header.findViewById(R.id.txt_title);
		txt_view_count = (TextView) root_header.findViewById(R.id.txt_view);
		txt_comment_count = (TextView) root_header
				.findViewById(R.id.txt_comment);
		txt_like_count = (TextView) root_header.findViewById(R.id.txt_like);
		txt_review_submit = (TextView) root_footer
				.findViewById(R.id.txt_submit);
		edt_review = (EditText) root_footer.findViewById(R.id.edt_comment);

		/**
		 * Set Value on View
		 */
		txt_user_name.setText(user_name);
		txt_title.setText(lookbook_title);
		txt_comment_count.setText(comment_count + " " + "Comments");
		txt_like_count.setText(like_count + " " + "Likes");
		txt_view_count.setText(view_count + " " + "Views");
		listView.addHeaderView(root_header);
		listView.addFooterView(root_footer);
		Picasso.with(getApplicationContext()).load(user_pic_url)
				.placeholder(R.drawable.zakoopiicon).into(user_pic);

		/**
		 * set Font on TextView
		 */
		txt_top_bar.setTypeface(typeface_semibold);
		txt_user_name.setTypeface(typeface_semibold);
		txt_title.setTypeface(typeface_semibold);
		txt_view_count.setTypeface(typeface_regular);
		txt_comment_count.setTypeface(typeface_regular);
		txt_like_count.setTypeface(typeface_regular);
		txt_review_submit.setTypeface(typeface_semibold);
		edt_review.setTypeface(typeface_regular);
		side_user_name.setTypeface(typeface_bold);
		side_about.setTypeface(typeface_regular);
		side_con_ac.setTypeface(typeface_regular);
		side_edt_profile.setTypeface(typeface_regular);
		side_noti_settings.setTypeface(typeface_regular);
		side_rate.setTypeface(typeface_regular);
		side_sign_out.setTypeface(typeface_regular);
		side_sug_store.setTypeface(typeface_regular);

		lookbook_view();
	}

	/**
	 * @void lookbook_view()
	 */
	public void lookbook_view() {

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(getString(R.string.base_url) + "lookbooks/view/"
				+ lookbook_id + ".json", new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// TODO Auto-generated method stub
				try {
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String text1 = "";
					String text2 = "";
					while ((text1 = bufferedReader.readLine()) != null) {
						text2 = text2 + text1;
					}
					show_lookbook(text2);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * 
	 * @show_lookbook show
	 */
	public void show_lookbook(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		RecentLookbookMain main = gson.fromJson(jsonReader,
				RecentLookbookMain.class);
		LookbookRecent lookbookRecent = main.getLookbookRecent();
		details = lookbookRecent.getRecentCards();

		listView.setAdapter(new lookbook_set());
	}

	/**
	 * lookbook_set extends BaseAdapter
	 * 
	 * @author ZakoopiUser
	 *
	 */
	public class lookbook_set extends BaseAdapter {

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

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder = null;
			if (view == null) {

				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				view = inflater.inflate(R.layout.lookbook_view, null);
				holder = new ViewHolder();
				holder.img_lookbook = (DynamicImageView) view
						.findViewById(R.id.img_lookbook);
				holder.desc = (TextView) view.findViewById(R.id.txt_has_tag);
				holder.flowLayout = (FlowLayout) view
						.findViewById(R.id.flow_layout);
				holder.txt_store_tag = (TextView) view
						.findViewById(R.id.txt_store);

				typeface_bold = Typeface.createFromAsset(view.getContext()
						.getAssets(), "fonts/SourceSansPro-Bold.ttf");
				typeface_semibold = Typeface.createFromAsset(view.getContext()
						.getAssets(), "fonts/SourceSansPro-Semibold.ttf");
				typeface_regular = Typeface.createFromAsset(view.getContext()
						.getAssets(), "fonts/SourceSansPro-Regular.ttf");

				holder.desc.setTypeface(typeface_semibold);
				holder.txt_store_tag.setTypeface(typeface_bold);
				String store_tag = details.get(position).getTags();
				String split[] = store_tag.split(",");

				for (int i = 0; i < split.length; i++) {
					final TextView t = new TextView(LookbookView.this);
					t.setText(split[i]);
					t.setBackgroundResource(R.drawable.green_store_tag);
					t.setPadding(8, 8, 8, 8);
					t.setTextSize(12);
					t.setSingleLine(true);
					t.setTag("" + i);
					t.setTypeface(typeface_regular);
					t.setTextColor(R.color.red);
					t.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Toast.makeText(getApplicationContext(),
									t.getText().toString(), Toast.LENGTH_SHORT)
									.show();
						}
					});
					holder.flowLayout.addView(t, new FlowLayout.LayoutParams(
							20, 10));
				}
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.desc.setText(details.get(position).getdescription());
			// store_tag.setText(details.get(position).getTags());
			DisplayImageOptions options = new DisplayImageOptions.Builder()
			
			.resetViewBeforeLoading(true)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher)
			.showImageOnLoading(R.drawable.ic_launcher).build();
			
			ImageLoader.getInstance().displayImage(details.get(position).getPic(),
					holder.img_lookbook, options);
			/*Picasso.with(getApplicationContext())
					.load(details.get(position).getPic())
					.placeholder(R.drawable.zakoopiicon)
					.into(holder.img_lookbook);*/

			return view;
		}

	}

	public class ViewHolder {
		FlowLayout flowLayout;
		TextView desc, txt_store_tag;
		DynamicImageView img_lookbook;
	}
}
