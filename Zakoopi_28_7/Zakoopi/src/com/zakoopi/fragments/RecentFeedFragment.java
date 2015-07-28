package com.zakoopi.fragments;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.zakoopi.R;
import com.zakoopi.database.HomeFeedLikeDatabaseHandler;
import com.zakoopi.endlist.EndlessListView;
import com.zakoopi.helper.MaterialProgressBar;
import com.zakoopi.helper.POJO;
import com.zakoopi.homefeed.Recent;
import com.zakoopi.homefeed.Recent_ArticleData;
import com.zakoopi.homefeed.Recent_Article_Images;
import com.zakoopi.homefeed.Recent_Article_User;
import com.zakoopi.homefeed.Recent_Lookbook_Cards;
import com.zakoopi.homefeed.Recent_Lookbook_User;
import com.zakoopi.homefeed.Recent_Lookbookdata;
import com.zakoopi.homefeed.Recent_StoreReviewData;
import com.zakoopi.homefeed.Recent_StoreReview_Store;
import com.zakoopi.homefeed.Recent_StoreReview_Users;
import com.zakoopi.homefeed.Recent_Teamsdata;
import com.zakoopi.homefeed.recentfeed;
import com.zakoopi.utils.ClientHttp;
import com.zakoopi.utils.RecentAdapter1;

@SuppressWarnings("deprecation")
public class RecentFeedFragment extends Fragment {

	private RecentAdapter1 adapter;
	private EndlessListView endlessListView;
	// private static final String FILENAME = "myFile.txt";
	// private int mCount;
	private boolean mHaveMoreDataToLoad;
	// private static final String RECENT_REST_URL =
	// "http://v3.zakoopi.com/api/feedRecent.json?page=";
	private static String RECENT_REST_URL = " ";
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	String text = "";
	String line = "";
	int page = 1;
	MaterialProgressBar bar;
	Fragment popularFrag, recentFrag;
	LinearLayout popular_linear, recent_linear;
	private SharedPreferences pro_user_pref;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	View include_view;
	Typeface typeface_semibold;
	RelativeLayout rel_pop, rel_recent;
	TextView txt_popular, txt_recent, txt_popular1, txt_recent1;
	View view_popular, view_recent, view_popular1, view_recent1;
	View header_view;
	ArrayList<POJO> pojolist = null;

	ArrayList<Integer> colorlist = null;
	Integer[] colors = { R.color.brown, R.color.purple, R.color.bgreen,
			R.color.olive, R.color.silver, R.color.iridium, R.color.blue1,
			R.color.green1, R.color.gray, R.color.maroon };
	HomeFeedLikeDatabaseHandler likeDatabaseHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		/**
		 * StrictMode for smooth list scroll
		 */

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		View view = inflater.inflate(R.layout.popular_feed_home, null);

		RECENT_REST_URL = getString(R.string.base_url)
				+ "feedRecent.json?page=";
		likeDatabaseHandler = new HomeFeedLikeDatabaseHandler(getActivity());
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

		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		bar = (MaterialProgressBar) view.findViewById(R.id.progressBar1);
		bar.setColorSchemeResources(R.color.red, R.color.green, R.color.blue,
				R.color.orange);
		include_view = (View) view.findViewById(R.id.previewLayout);
		popular_linear = (LinearLayout) include_view
				.findViewById(R.id.lin_popular);
		recent_linear = (LinearLayout) include_view
				.findViewById(R.id.lin_recent);

		/**
		 * header_view
		 */
		header_view = (View) view.findViewById(R.id.previewHeader);
		header_view.setVisibility(View.VISIBLE);
		txt_popular1 = (TextView) header_view.findViewById(R.id.text);
		txt_recent1 = (TextView) header_view.findViewById(R.id.text1);
		view_popular1 = (View) header_view.findViewById(R.id.view1);
		view_recent1 = (View) header_view.findViewById(R.id.view2);
		txt_recent1.setTextColor(Color.parseColor("#4d4d49"));
		txt_popular1.setTextColor(Color.parseColor("#acacac"));
		view_recent1.setBackgroundColor(Color.parseColor("#26B3AD"));
		view_popular1.setBackgroundColor(Color.parseColor("#acacac"));
		txt_popular1.setTypeface(typeface_semibold);
		txt_recent1.setTypeface(typeface_semibold);

		/**
		 * feed_header
		 */
		View feed_header = inflater.inflate(R.layout.feed_header, null);

		rel_pop = (RelativeLayout) feed_header.findViewById(R.id.rel2);
		rel_recent = (RelativeLayout) feed_header.findViewById(R.id.rel3);
		txt_popular = (TextView) feed_header.findViewById(R.id.text);
		txt_recent = (TextView) feed_header.findViewById(R.id.text1);
		view_popular = (View) feed_header.findViewById(R.id.view1);
		view_recent = (View) feed_header.findViewById(R.id.view2);
		txt_recent.setTextColor(Color.parseColor("#4d4d49"));
		txt_popular.setTextColor(Color.parseColor("#acacac"));
		view_recent.setBackgroundColor(Color.parseColor("#26B3AD"));
		view_popular.setBackgroundColor(Color.parseColor("#acacac"));
		txt_popular.setTypeface(typeface_semibold);
		txt_recent.setTypeface(typeface_semibold);

		mHaveMoreDataToLoad = true;
		endlessListView = (EndlessListView) view
				.findViewById(R.id.endlessListView);
		endlessListView.addHeaderView(feed_header);
		endlessListView.setOnLoadMoreListener(loadMoreListener);
		recent_homeFeed(page);
		header_click();
		return view;

	}

	/**
	 * @void header_click()
	 */
	public void header_click() {

		rel_pop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popular_linear.setVisibility(View.VISIBLE);
				recent_linear.setVisibility(View.GONE);
				txt_popular.setTextColor(Color.parseColor("#4d4d49"));
				txt_recent.setTextColor(Color.parseColor("#acacac"));
				view_popular.setBackgroundColor(Color.parseColor("#26B3AD"));
				view_recent.setBackgroundColor(Color.parseColor("#acacac"));
				txt_popular.setTypeface(typeface_semibold);
				txt_recent.setTypeface(typeface_semibold);

				/**
				 * Add Fragment
				 */
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				fm.beginTransaction();
				popularFrag = new PopularFeedFragment();
				Bundle arguments = new Bundle();
				arguments.putBoolean("shouldYouCreateAChildFragment", false);
				popularFrag.setArguments(arguments);
				ft.replace(R.id.lin_popular, popularFrag);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

	}

	/**
	 * @void onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		// scroll speed decreases as friction increases. a value of 2 worked
		// well in an emulator; i need to test it on a real device
		endlessListView.setFriction((float) (ViewConfiguration
				.getScrollFriction() * 1.5));
	}

	/**
	 * @void loadMoreData()
	 */
	private void loadMoreData() {
		// new LoadMore().execute((Void) null);
		page++;
		recent_loadmoreFeed(page);
		// Log.e("kkkk", page + "");

	}

	/**
	 * EndlessListView LoadMoreListener
	 */
	private EndlessListView.OnLoadMoreListener loadMoreListener = new EndlessListView.OnLoadMoreListener() {

		@Override
		public boolean onLoadMore() {
			if (true == mHaveMoreDataToLoad) {

				loadMoreData();

			} else {

				Toast.makeText(getActivity(), "No more data to load",
						Toast.LENGTH_SHORT).show();
			}
			endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			return mHaveMoreDataToLoad;
		}
	};

	/**
	 * 
	 * recent_homeFeed page
	 */
	public void recent_homeFeed(int page) {
		// Log.e("url", RECENT_REST_URL + page);
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(RECENT_REST_URL + page, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				bar.setVisibility(View.VISIBLE);
				endlessListView.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					while ((line = br.readLine()) != null) {

						text = text + line;
					}

					showData(text);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				endlessListView.loadMoreCompleat();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	/**
	 * Recent Load More Feed
	 * 
	 * @recent_loadmoreFeed page
	 */
	public void recent_loadmoreFeed(int page) {
		// Log.e("url", RECENT_REST_URL + page);
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.get(RECENT_REST_URL + page, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"

				try {

					BufferedReader br = new BufferedReader(
							new InputStreamReader(new ByteArrayInputStream(
									response)));
					String st = "";
					String st1 = "";
					while ((st = br.readLine()) != null) {

						st1 = st1 + st;

					}

					showmoreData(st1);

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)

				endlessListView.loadMoreCompleat();

			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	/**
	 * Recent Feed Show Data
	 * 
	 * @showData data
	 */
	@SuppressWarnings("unchecked")
	public void showData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		Recent ppp = gson.fromJson(reader, Recent.class);
		List<recentfeed> feeds = ppp.getFeedRecent();

		new MyApp().execute(feeds);
	}

	/**
	 * MyApp extends AsyncTask<List<recentfeed>, Void, Void> for Showdata(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */

	private class MyApp extends AsyncTask<List<recentfeed>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<recentfeed>... params) {
			pojolist = new ArrayList<POJO>();
			pojolist.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();

			for (int i = 0; i < params[0].size(); i++) {
				recentfeed pop = params[0].get(i);

				/**
				 * For Lookbooks
				 */
				if (pop.getModel().equals("Lookbooks")) {

					try {

						Recent_Lookbookdata look = pop.getLookbookdata();
						Recent_Lookbook_User user = look.getUser();
						ArrayList<Recent_Lookbook_Cards> cards = look
								.getCards();
						// Log.e("size", cards.size() + "");
						if (cards.size() >= 3) {

							Recent_Lookbook_Cards ccll = cards.get(0);
							Recent_Lookbook_Cards ccll1 = cards.get(1);
							Recent_Lookbook_Cards ccll2 = cards.get(2);
							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();
							String img2 = ccll2.getMedium_img();
							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLookbooklike_count();
							String hits = look.getView_count();
							String title = look.getTitle();
							String description = ccll.getDescription();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Lookbooks", username, userimg,
									lookimg, img1, img2, likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, "na", is_liked);

							pojolist.add(pp);
							likeDatabaseHandler.allDelete();
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);

						} else if (cards.size() == 2) {

							Recent_Lookbook_Cards ccll = cards.get(0);
							Recent_Lookbook_Cards ccll1 = cards.get(1);

							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLookbooklike_count();
							String hits = look.getView_count();
							String title = look.getTitle();
							String description = ccll.getDescription();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Lookbooks", username, userimg,
									lookimg, img1, "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, "na", is_liked);

							pojolist.add(pp);
							likeDatabaseHandler.allDelete();
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);

						} else {

							Recent_Lookbook_Cards ccll = cards.get(0);

							String lookimg = ccll.getLarge_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLookbooklike_count();
							String hits = look.getView_count();
							String title = look.getTitle();
							String description = ccll.getDescription();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Lookbooks", username, userimg,
									lookimg, "na", "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, "na", is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.allDelete();
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);

						}
					} catch (Exception e) {

					}
				}
				/**
				 * For Articles
				 */
				else if (pop.getModel().equals("Articles")) {

					try {

						Recent_ArticleData look = pop.getArticaldata();
						Recent_Article_User user = look.getUser();
						ArrayList<Recent_Article_Images> cards = look
								.getArticle_images();

						if (cards.size() >= 3) {

							Recent_Article_Images ccll = cards.get(0);
							Recent_Article_Images ccll1 = cards.get(1);
							Recent_Article_Images ccll2 = cards.get(2);
							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();
							String img2 = ccll2.getMedium_img();
							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLikes_count();
							String hits = look.getHits();
							String title = look.getTitle();
							String description = look.getDescription();
							String is_new = look.getIsNew();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Articles", username, userimg,
									lookimg, img1, img2, likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, is_new, is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.allDelete();
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);

						} else if (cards.size() == 2) {

							Recent_Article_Images ccll = cards.get(0);
							Recent_Article_Images ccll1 = cards.get(1);

							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLikes_count();
							String hits = look.getHits();
							String title = look.getTitle();
							String description = look.getDescription();
							String is_new = look.getIsNew();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Articles", username, userimg,
									lookimg, img1, "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, is_new, is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.allDelete();
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						} else {

							Recent_Article_Images ccll = cards.get(0);

							String lookimg = ccll.getLarge_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLikes_count();
							String hits = look.getHits();
							String title = look.getTitle();
							String description = look.getDescription();
							String is_new = look.getIsNew();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Articles", username, userimg,
									lookimg, "na", "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, is_new, is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.allDelete();
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						}
					} catch (Exception e) {

					}
				}
				/**
				 * For StoreReviews
				 */
				else if (pop.getModel().equals("StoreReviews")) {
					try {

						Recent_StoreReviewData store = pop.getStorereviewdata();
						Recent_StoreReview_Users user = store.getUser();
						Recent_StoreReview_Store likes = store.getStore();

						String username = user.getFirst_name() + " "
								+ user.getLast_name();
						String userimg = user.getAndroid_api_img();
						// String lookimg = "na";
						String likes1 = likes.getLikes_count();
						String hits = store.getHits();
						String review = store.getReview();
						String store_name = likes.getStore_name();
						// String description = "na";
						String store_location = likes.getMarket();
						String store_rate = likes.getOverall_ratings();
						String idd = store.getStore_id();
						String is_liked = store.getIs_liked();
						POJO pp = new POJO("StoreReviews", username, userimg,
								"na", "na", "na", likes1, hits, "na",
								store_name, store_location, store_rate, review,
								"na", idd, "na", "na", is_liked);
						pojolist.add(pp);
						likeDatabaseHandler.allDelete();
						likeDatabaseHandler.insertLike(pp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(R.color.tabscolor);

					} catch (Exception e) {

					}

				}
				/**
				 * For Teams
				 */
				else if (pop.getModel().equals("Teams")) {

					try {

						Recent_Teamsdata team = pop.getTeamsdata();

						String username = "Zakoopi Team";
						String userimg = "na";
						String lookimg = team.getAndroid_api_img();
						String likes1 = team.getLikes_count();
						String hits = team.getHits();
						String title = team.getTitle();
						// String description = "na";
						String idd = team.getId();

						POJO pp = new POJO("Teams", username, userimg, lookimg,
								"na", "na", likes1, hits, title, "na", "na",
								"na", "na", "na", idd, "na", "na", "false");
						pojolist.add(pp);
						likeDatabaseHandler.allDelete();
						likeDatabaseHandler.insertLike(pp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
					} catch (Exception e) {

					}
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			adapter = new RecentAdapter1(getActivity(), pojolist, colorlist,likeDatabaseHandler);

			endlessListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			// endlessListView.loadMoreCompleat();
			bar.setVisibility(View.GONE);
			header_view.setVisibility(View.GONE);
			endlessListView.setVisibility(View.VISIBLE);

		}
	}

	/**
	 * Recent Feed Show More Data
	 * 
	 * @showmoreData data
	 */
	@SuppressWarnings("unchecked")
	public void showmoreData(String data) {

		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(data));
		reader.setLenient(true);
		Recent ppp = gson.fromJson(reader, Recent.class);
		List<recentfeed> feeds = ppp.getFeedRecent();
		new MyApp1().execute(feeds);

	}

	/**
	 * MyApp1 extends AsyncTask<List<recentfeed>, Void, Void> for
	 * showmoreData(data)
	 * 
	 * @author ZakoopiUser
	 *
	 */

	private class MyApp1 extends AsyncTask<List<recentfeed>, Void, Void> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();

		}

		@SuppressWarnings("unchecked")
		@Override
		protected Void doInBackground(List<recentfeed>... params) {
			pojolist = new ArrayList<POJO>();
			pojolist.clear();
			colorlist = new ArrayList<Integer>();
			colorlist.clear();
			for (int i = 0; i < params[0].size(); i++) {
				recentfeed pop = params[0].get(i);

				/**
				 * For Lookbooks
				 */
				if (pop.getModel().equals("Lookbooks")) {

					try {

						Recent_Lookbookdata look = pop.getLookbookdata();
						Recent_Lookbook_User user = look.getUser();
						ArrayList<Recent_Lookbook_Cards> cards = look
								.getCards();

						if (cards.size() >= 3) {

							Recent_Lookbook_Cards ccll = cards.get(0);
							Recent_Lookbook_Cards ccll1 = cards.get(1);
							Recent_Lookbook_Cards ccll2 = cards.get(2);
							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();
							String img2 = ccll2.getMedium_img();
							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLookbooklike_count();
							String hits = look.getView_count();
							String title = look.getTitle();
							String description = ccll.getDescription();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Lookbooks", username, userimg,
									lookimg, img1, img2, likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, "na", is_liked);

							pojolist.add(pp);
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						} else if (cards.size() == 2) {

							Recent_Lookbook_Cards ccll = cards.get(0);
							Recent_Lookbook_Cards ccll1 = cards.get(1);

							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLookbooklike_count();
							String hits = look.getView_count();
							String title = look.getTitle();
							String description = ccll.getDescription();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Lookbooks", username, userimg,
									lookimg, img1, "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, "na", is_liked);

							pojolist.add(pp);
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						} else {

							Recent_Lookbook_Cards ccll = cards.get(0);

							String lookimg = ccll.getLarge_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLookbooklike_count();
							String hits = look.getView_count();
							String title = look.getTitle();
							String description = ccll.getDescription();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Lookbooks", username, userimg,
									lookimg, "na", "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, "na", is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						}
					} catch (Exception e) {

					}
				}
				/**
				 * For Articles
				 */
				else if (pop.getModel().equals("Articles")) {

					try {

						Recent_ArticleData look = pop.getArticaldata();
						Recent_Article_User user = look.getUser();
						ArrayList<Recent_Article_Images> cards = look
								.getArticle_images();

						if (cards.size() >= 3) {

							Recent_Article_Images ccll = cards.get(0);
							Recent_Article_Images ccll1 = cards.get(1);
							Recent_Article_Images ccll2 = cards.get(2);
							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();
							String img2 = ccll2.getMedium_img();
							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLikes_count();
							String hits = look.getHits();
							String title = look.getTitle();
							String description = look.getDescription();
							String is_new = look.getIsNew();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Articles", username, userimg,
									lookimg, img1, img2, likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, is_new, is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);

						} else if (cards.size() == 2) {

							Recent_Article_Images ccll = cards.get(0);
							Recent_Article_Images ccll1 = cards.get(1);

							String lookimg = ccll.getLarge_img();
							String img1 = ccll1.getMedium_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLikes_count();
							String hits = look.getHits();
							String title = look.getTitle();
							String description = look.getDescription();
							String is_new = look.getIsNew();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Articles", username, userimg,
									lookimg, img1, "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, is_new, is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						} else {

							Recent_Article_Images ccll = cards.get(0);

							String lookimg = ccll.getLarge_img();

							String username = user.getFirst_name() + " "
									+ user.getLast_name();
							String userimg = user.getAndroid_api_img();

							String likes = look.getLikes_count();
							String hits = look.getHits();
							String title = look.getTitle();
							String description = look.getDescription();
							String is_new = look.getIsNew();
							String idd = look.getId();
							String is_liked = look.getIs_liked();
							POJO pp = new POJO("Articles", username, userimg,
									lookimg, "na", "na", likes, hits, title,
									"na", "na", "na", "na",
									String.valueOf(cards.size()), idd,
									description, is_new, is_liked);
							pojolist.add(pp);
							likeDatabaseHandler.insertLike(pp);
							int rnd = new Random().nextInt(colors.length);
							colorlist.add(colors[rnd]);
						}
					} catch (Exception e) {

					}
				}
				/**
				 * For StoreReviews
				 */
				else if (pop.getModel().equals("StoreReviews")) {

					try {

						Recent_StoreReviewData store = pop.getStorereviewdata();
						Recent_StoreReview_Users user = store.getUser();
						Recent_StoreReview_Store likes = store.getStore();

						String username = user.getFirst_name() + " "
								+ user.getLast_name();
						String userimg = user.getAndroid_api_img();
						// String lookimg = "na";
						String likes1 = likes.getLikes_count();
						String hits = store.getHits();
						String review = store.getReview();
						String store_name = likes.getStore_name();
						String store_location = likes.getMarket();
						String store_rate = likes.getOverall_ratings();
						// String description = "na";
						String idd = store.getStore_id();
						String is_liked = store.getIs_liked();
						POJO pp = new POJO("StoreReviews", username, userimg,
								"na", "na", "na", likes1, hits, "na",
								store_name, store_location, store_rate, review,
								"na", idd, "na", "na", is_liked);
						pojolist.add(pp);
						likeDatabaseHandler.insertLike(pp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(R.color.tabscolor);
					} catch (Exception e) {
					}
				}
				/**
				 * For Teams
				 */
				else if (pop.getModel().equals("Teams")) {

					try {

						Recent_Teamsdata team = pop.getTeamsdata();

						String username = "Zakoopi Team";
						String userimg = "na";
						String lookimg = team.getAndroid_api_img();
						String likes1 = team.getLikes_count();
						String hits = team.getHits();
						String title = team.getTitle();
						// String description = "na";
						String idd = team.getId();
						POJO pp = new POJO("Teams", username, userimg, lookimg,
								"na", "na", likes1, hits, title, "na", "na",
								"na", "na", "na", idd, "na", "na", "false");
						pojolist.add(pp);
						likeDatabaseHandler.insertLike(pp);
						int rnd = new Random().nextInt(colors.length);
						colorlist.add(colors[rnd]);
					} catch (Exception e) {

					}
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			adapter.addItems(pojolist);
			adapter.addColors(colorlist);
			endlessListView.loadMoreCompleat();

			endlessListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
			endlessListView.setVerticalScrollBarEnabled(false);
		}
	}

}
