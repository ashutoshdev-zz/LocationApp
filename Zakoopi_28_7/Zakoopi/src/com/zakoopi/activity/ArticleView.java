package com.zakoopi.activity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.navdrawer.SimpleSideDrawer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.zakoopi.R;
import com.zakoopi.article.model.Article;
import com.zakoopi.article.model.ArticleImage;
import com.zakoopi.article.model.ArticleMain;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.DynamicImageView;

public class ArticleView extends Activity {

	ImageView img_menu;
	RelativeLayout rel_back, rel_edt_profile;
	TextView txt_head;
	SimpleSideDrawer slide_me;
	CircleImageView pro_user_pic;
	LinearLayout lin_main;
	static AsyncHttpClient client = new AsyncHttpClient();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	TextView side_user_name, side_edt_profile, side_noti_settings, side_con_ac,
			side_sign_out, side_about, side_sug_store, side_rate;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
			typeface_regular;
	String article_id, user_name, user_pic_url, article_title, view_count,
			like_count, comment_count = null, description, is_new;
	private DisplayImageOptions options;
	ArrayList<ArticleImage> url_list = new ArrayList<ArticleImage>();
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.article_view_main);

		Intent i = getIntent();
		article_id = i.getStringExtra("article_id");
		user_name = i.getStringExtra("username");
		user_pic_url = i.getStringExtra("userpicurl");
		article_title = i.getStringExtra("title");
		view_count = i.getStringExtra("hits");
		like_count = i.getStringExtra("likes");
		description = i.getStringExtra("description");
		is_new = i.getStringExtra("is_new");

		Log.e("article_id", article_id);
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
		 * Typeface
		 */
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

		findId();
		// setMenuData();
		click();

		slide_me = new SimpleSideDrawer(this);
		slide_me.setRightBehindContentView(R.layout.side_menu);

		if (user_name != null) {
			lin_main.addView(setHeader());
		}

		if (is_new.equals("false")) {
			lin_main.addView(addOldImage());
		} else {
			lin_main.addView(setArticleData());
			lin_main.addView(addComment());
		}
		if (comment_count != null) {
			lin_main.addView(viewComment());
		}
		

	}

	public void findId() {

		img_menu = (ImageView) findViewById(R.id.img_menu);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);
		txt_head = (TextView) findViewById(R.id.txt);
		lin_main = (LinearLayout) findViewById(R.id.article_lin);
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

	}

	/*
	 * public void setMenuData() {
	 * Picasso.with(getApplicationContext()).load(pro_user_pic_url)
	 * .into(pro_user_pic); side_user_name.setText(pro_user_name);
	 * txt_head.setText("Article"); }
	 */

	public void click() {

		rel_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		/*
		 * side_edt_profile.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent edt_profile = new Intent(ArticleView.this,
		 * EditProfilePage.class); startActivity(edt_profile); } });
		 * 
		 * img_menu.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub slide_me.toggleRightDrawer(); } });
		 */
	}

	private View setHeader() {

		View view_header = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_view_header, null);

		TextView txt_user_name, txt_title, txt_view_count, txt_comment_count, txt_like_count;
		CircleImageView user_pic;

		txt_user_name = (TextView) view_header.findViewById(R.id.txt_user_name);
		user_pic = (CircleImageView) view_header
				.findViewById(R.id.img_user_profile);
		txt_title = (TextView) view_header.findViewById(R.id.txt_title);
		txt_view_count = (TextView) view_header.findViewById(R.id.txt_view);
		txt_comment_count = (TextView) view_header
				.findViewById(R.id.txt_comment);
		txt_like_count = (TextView) view_header.findViewById(R.id.txt_like);

		txt_user_name.setText(user_name);
		txt_title.setText(article_title);
		txt_comment_count.setText(comment_count);
		txt_like_count.setText(like_count);
		txt_view_count.setText(view_count);

		ImageLoader.getInstance().displayImage(user_pic_url, user_pic, options);

		return view_header;
	}

	private View setArticleData() {

		View view_article = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_show, null);

		WebView webview = (WebView) view_article
				.findViewById(R.id.article_webview);
		webview.setWebViewClient(new myWebClient());
		webview.getSettings().setJavaScriptEnabled(true);

		String css = description
				+ "<style type=\"text/css\"> img {width:100% !important; height: auto !important;}</style>";
		webview.loadDataWithBaseURL(getString(R.string.base_url), css,
				"text/html", "UTF-8", "");

		return view_article;
	}

	private View viewComment() {

		View view_comment = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_view_comment, null);

		return view_comment;
	}

	private View addComment() {

		View add_comment = LayoutInflater.from(getApplicationContext())
				.inflate(R.layout.article_footer, null);

		return add_comment;
	}

	private View addOldImage() {

		View old_image = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.article_old_image, null);
		pager = (ViewPager) old_image.findViewById(R.id.old_image);

		article_view();
		return old_image;
	}

	public class myWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}

	}

	public class ViewPagerAdapter extends PagerAdapter {
		// Declare Variables
		Context context;
		LayoutInflater inflater;
		ArrayList<ArticleImage> list;
		DynamicImageView imgflag;
		ProgressBar prog;

		public ViewPagerAdapter(Context context, ArrayList<ArticleImage> list) {
			this.context = context;
			this.list = list;
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((RelativeLayout) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			// Declare Variables

			// position=Integer.parseInt(pos);

			View itemView = inflater.inflate(R.layout.zoom_items, container,
					false);

			// Locate the ImageView in viewpager_item.xml
			imgflag = (DynamicImageView) itemView.findViewById(R.id.imageView2);
			prog = (ProgressBar) itemView.findViewById(R.id.progressBar1);

			Log.e("TAGGGGGGGGG", "ADAPter");
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.showImageOnLoading(R.drawable.ic_launcher).build();

			ImageLoader.getInstance().displayImage(
					list.get(position).getAndroid_api_img(), imgflag, options,
					new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							prog.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							prog.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							prog.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							prog.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							prog.setVisibility(View.GONE);
						}
					});

			((ViewPager) container).addView(itemView);

			return itemView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// Remove viewpager_item.xml from ViewPager
			((ViewPager) container).removeView((RelativeLayout) object);

		}
	}

	public void article_view() {
		Log.e("URL", getString(R.string.base_url) + "articles/view/"
				+ article_id + ".json");
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(getString(R.string.base_url) + "articles/view/" + article_id
				+ ".json", new AsyncHttpResponseHandler() {

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
					show_article(text2);
					Log.e("DATA", text2);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {
				// TODO Auto-generated method stub
				Log.e("FAIL", "FAIL");
			}
		});
	}

	/**
	 * 
	 * @show_lookbook show
	 */
	public void show_article(String show) {

		Gson gson = new Gson();
		JsonReader jsonReader = new JsonReader(new StringReader(show));
		jsonReader.setLenient(true);
		ArticleMain main = gson.fromJson(jsonReader, ArticleMain.class);
		Article article_sub = main.getArticle();
		url_list = article_sub.getArticle();

		PagerAdapter pagerAdapter = new ViewPagerAdapter(ArticleView.this,
				url_list);
		pager.setAdapter(pagerAdapter);
		
		lin_main.addView(setArticleData());
		lin_main.addView(addComment());
	}

}
