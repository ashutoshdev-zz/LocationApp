package com.zakoopi.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mystores.StoreActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zakoopi.R;
import com.zakoopi.activity.ArticleView;
import com.zakoopi.activity.LookbookView;
import com.zakoopi.database.HomeFeedLikeDatabaseHandler;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.POJO;

@SuppressWarnings("deprecation")
public class PopularAdapter1 extends BaseAdapter {

	
	private List<POJO> mList;
	Context ctx;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email,
			user_password;
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;
	SharedPreferences pro_user_pref;
	String article_color;
	ArrayList<Integer> colorlist;
	
	ArrayList<String> main_like = new ArrayList<String>();
	
	int mode_position;
	String like;
	String[] ststus_pos;

	public PopularAdapter1(Context context, List<POJO> list,
			ArrayList<Integer> color, 
			HomeFeedLikeDatabaseHandler likeDatabaseHandler) {
		ctx = context;
		mList = list;
		this.colorlist = color;
		
		main_like = likeDatabaseHandler.getAllLike();
		ststus_pos = new String[main_like.size()];
		for (int i = 0; i < main_like.size(); i++) {
			ststus_pos[i] = main_like.get(i);
		}
		

		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
		pro_user_pic_url = pro_user_pref.getString("user_image", "123");
		pro_user_name = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		pro_user_location = pro_user_pref.getString("user_location", "4267");
		user_email = pro_user_pref.getString("user_email", "9089");
		user_password = pro_user_pref.getString("password", "sar");
	}

	public void addItems(List<POJO> newItems) {
		if (null == newItems || newItems.size() <= 0) {
			return;
		}

		if (null == mList) {
			mList = new ArrayList<POJO>();
		}

		mList.addAll(newItems);
		notifyDataSetChanged();
	}

	public void addColors(List<Integer> newItems) {
		if (null == newItems || newItems.size() <= 0) {
			return;
		}

		if (null == mList) {
			colorlist = new ArrayList<Integer>();
		}

		colorlist.addAll(newItems);
		// notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null == mList) {
			return 0;
		}

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		int type = this.getItemViewType(position);
		

		switch (type) {

		case 1:
			View result = convertView;
			LookbookHolder holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();

			if (result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				result = inf.inflate(R.layout.lookbook_feed_item, parent, false);
				holder = new LookbookHolder();
				holder.user_name = (TextView) result
						.findViewById(R.id.user_name);
				holder.lookbook_view = (TextView) result
						.findViewById(R.id.user_view);
				holder.lookbook_like = (TextView) result
						.findViewById(R.id.txt_like_count);
				holder.user_image = (ImageView) result
						.findViewById(R.id.img_profile);
				holder.title = (TextView) result.findViewById(R.id.txt_title);
				holder.look_image = (DynamicImageView) result
						.findViewById(R.id.img_flash);
				holder.like_image = (ImageView) result
						.findViewById(R.id.img_like);

				holder.share_image = (ImageView) result
						.findViewById(R.id.img_share);
				holder.img1 = (ImageView) result.findViewById(R.id.post_img1);
				holder.img2 = (ImageView) result.findViewById(R.id.post_img2);
				holder.last_text = (RelativeLayout) result
						.findViewById(R.id.rel_post_img_count);
				holder.image_count = (TextView) result
						.findViewById(R.id.txt_count);

				holder.rel_img_count = (RelativeLayout) result
						.findViewById(R.id.rel_113);

				result.setTag(holder);
			} else {
				holder = (LookbookHolder) result.getTag();
			}

			final POJO pojo = mList.get(position);
			holder.like_image.setTag(position);

			try {

				if (Integer.parseInt(pojo.getImage_count()) >= 3) {

					ImageLoader.getInstance().displayImage(pojo.getLookimg(),
							holder.look_image, options, animateFirstListener);

					holder.img1.setVisibility(View.VISIBLE);
					holder.img2.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(pojo.getImg1(),
							holder.img1, options, animateFirstListener);

					ImageLoader.getInstance().displayImage(pojo.getImg2(),
							holder.img2, options, animateFirstListener);

					if (Integer.parseInt(pojo.getImage_count()) == 3) {

						holder.last_text.setVisibility(View.INVISIBLE);
					} else {
						holder.last_text.setVisibility(View.VISIBLE);
						holder.image_count.setText("+" + pojo.getImage_count());
					}

				}

				else if (Integer.parseInt(pojo.getImage_count()) == 2) {

					ImageLoader.getInstance().displayImage(pojo.getLookimg(),
							holder.look_image, options, animateFirstListener);
					ImageLoader.getInstance().displayImage(pojo.getImg1(),
							holder.img1, options, animateFirstListener);
					holder.img1.setVisibility(View.VISIBLE);

				} else {

					ImageLoader.getInstance().displayImage(pojo.getLookimg(),
							holder.look_image, options, animateFirstListener);

				}

				holder.user_name.setText(pojo.getUsername());

				ImageLoader.getInstance().displayImage(pojo.getUserimg(),
						holder.user_image, options);

				holder.lookbook_view.setText(pojo.getHits());
				holder.title.setText(pojo.getTitle());
				holder.lookbook_like.setText(pojo.getLikes());

				holder.look_image
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								Intent in = new Intent(ctx, LookbookView.class);
								in.putExtra("lookbook_id", pojo.getIdd());
								in.putExtra("username", pojo.getUsername());
								in.putExtra("userpicurl", pojo.getUserimg());
								in.putExtra("title", pojo.getTitle());
								in.putExtra("hits", pojo.getHits());
								in.putExtra("likes", pojo.getLikes());
								// in.putExtra("comments", pojo.getIdd());
								ctx.startActivity(in);

							}
						});

				holder.title.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						Intent in = new Intent(ctx, LookbookView.class);
						in.putExtra("lookbook_id", pojo.getIdd());
						in.putExtra("username", pojo.getUsername());
						in.putExtra("userpicurl", pojo.getUserimg());
						in.putExtra("title", pojo.getTitle());
						in.putExtra("hits", pojo.getHits());
						in.putExtra("likes", pojo.getLikes());
						// in.putExtra("comments", pojo.getIdd());
						ctx.startActivity(in);
						//
					}
				});
				//
				holder.rel_img_count
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								Intent in = new Intent(ctx, LookbookView.class);
								in.putExtra("lookbook_id", pojo.getIdd());
								in.putExtra("username", pojo.getUsername());
								in.putExtra("userpicurl", pojo.getUserimg());
								in.putExtra("title", pojo.getTitle());
								in.putExtra("hits", pojo.getHits());
								in.putExtra("likes", pojo.getLikes());
								// in.putExtra("comments", pojo.getIdd());
								ctx.startActivity(in);
								//
							}
						});

				holder.share_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent share = new Intent(Intent.ACTION_SEND);
						share.setType("text/plain");
						share.putExtra(Intent.EXTRA_TEXT, "I'm being sent!!");
						ctx.startActivity(Intent.createChooser(share,
								"Share Text"));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return result;
			
		case 2:
			View article_result = convertView;
			final ArticleHolder article_holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();

			if (article_result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				article_result = inf.inflate(R.layout.article_feed_item, parent, false);
				article_holder = new ArticleHolder();
				article_holder.user_name = (TextView) article_result
						.findViewById(R.id.user_name);
				article_holder.lookbook_view = (TextView) article_result
						.findViewById(R.id.user_view);
				article_holder.lookbook_like = (TextView) article_result
						.findViewById(R.id.txt_like_count);
				article_holder.user_image = (ImageView) article_result
						.findViewById(R.id.img_profile);
				article_holder.title = (TextView) article_result
						.findViewById(R.id.txt_title);
				article_holder.look_image = (DynamicImageView) article_result
						.findViewById(R.id.img_flash);
				article_holder.like_image = (ImageView) article_result
						.findViewById(R.id.img_like);

				article_holder.share_image = (ImageView) article_result
						.findViewById(R.id.img_share);
				article_holder.img1 = (ImageView) article_result
						.findViewById(R.id.post_img1);
				article_holder.img2 = (ImageView) article_result
						.findViewById(R.id.post_img2);
				article_holder.last_text = (RelativeLayout) article_result
						.findViewById(R.id.rel_post_img_count);
				article_holder.image_count = (TextView) article_result
						.findViewById(R.id.txt_count);

				article_holder.rel_img_count = (RelativeLayout) article_result
						.findViewById(R.id.rel_113);

				article_result.setTag(article_holder);
			} else {
				article_holder = (ArticleHolder) article_result.getTag();
			}

			final POJO article_pojo = mList.get(position);
			article_holder.like_image.setTag(position);
			
			try {
				if (Integer.parseInt(article_pojo.getImage_count()) >= 3) {
					ImageLoader.getInstance().displayImage(
							article_pojo.getLookimg(),
							article_holder.look_image, options,
							animateFirstListener);
					article_holder.img1.setVisibility(View.VISIBLE);
					article_holder.img2.setVisibility(View.VISIBLE);

					ImageLoader.getInstance().displayImage(
							article_pojo.getImg1(), article_holder.img1,
							options, animateFirstListener);
					ImageLoader.getInstance().displayImage(
							article_pojo.getImg2(), article_holder.img2,
							options, animateFirstListener);

					if (Integer.parseInt(article_pojo.getImage_count()) == 3) {

						article_holder.last_text.setVisibility(View.INVISIBLE);
					} else {
						article_holder.last_text.setVisibility(View.VISIBLE);
						article_holder.image_count.setText("+"
								+ article_pojo.getImage_count());
					}

				}

				else if (Integer.parseInt(article_pojo.getImage_count()) == 2) {

					ImageLoader.getInstance().displayImage(
							article_pojo.getLookimg(),
							article_holder.look_image, options,
							animateFirstListener);
					ImageLoader.getInstance().displayImage(
							article_pojo.getImg1(), article_holder.img1,
							options, animateFirstListener);
					article_holder.img1.setVisibility(View.VISIBLE);
				} else {

					ImageLoader.getInstance().displayImage(
							article_pojo.getLookimg(),
							article_holder.look_image, options,
							animateFirstListener);
					article_holder.img1.setVisibility(View.GONE);
					article_holder.img2.setVisibility(View.GONE);
					
					article_holder.last_text.setVisibility(View.GONE);
				}

				

				article_holder.user_name.setText(article_pojo.getUsername());

				ImageLoader.getInstance().displayImage(
						article_pojo.getUserimg(), article_holder.user_image,
						options);

				article_holder.lookbook_view.setText(article_pojo.getHits());
				article_holder.title.setText(article_pojo.getTitle());
				article_holder.lookbook_like.setText(article_pojo.getLikes());

				article_holder.title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Toast.makeText(ctx, article_pojo.getImage_count(),
								Toast.LENGTH_SHORT).show();
						// title.setText("ABCD");
						/*
						 * Intent in = new Intent(ctx, ArticleView.class);
						 * in.putExtra("article_id", article_pojo.getIdd());
						 * in.putExtra("username", article_pojo.getUsername());
						 * in.putExtra("userpicurl", article_pojo.getUserimg());
						 * in.putExtra("title", article_pojo.getTitle());
						 * in.putExtra("hits", article_pojo.getHits());
						 * in.putExtra("likes", article_pojo.getLikes());
						 * in.putExtra("description",
						 * article_pojo.getDescription()); in.putExtra("is_new",
						 * article_pojo.getIsnew());
						 * 
						 * // in.putExtra("comments", article_pojo.getIdd());
						 * ctx.startActivity(in);
						 */

					}
				});

				article_holder.look_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent in = new Intent(ctx, ArticleView.class);
								in.putExtra("article_id", article_pojo.getIdd());
								in.putExtra("username",
										article_pojo.getUsername());
								in.putExtra("userpicurl",
										article_pojo.getUserimg());
								in.putExtra("title", article_pojo.getTitle());
								in.putExtra("hits", article_pojo.getHits());
								in.putExtra("likes", article_pojo.getLikes());
								in.putExtra("description",
										article_pojo.getDescription());
								in.putExtra("is_new", article_pojo.getIsnew());

								// in.putExtra("comments",
								// article_pojo.getIdd());
								ctx.startActivity(in);

							}
						});

				article_holder.rel_img_count
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent in = new Intent(ctx, ArticleView.class);
								in.putExtra("article_id", article_pojo.getIdd());
								in.putExtra("username",
										article_pojo.getUsername());
								in.putExtra("userpicurl",
										article_pojo.getUserimg());
								in.putExtra("title", article_pojo.getTitle());
								in.putExtra("hits", article_pojo.getHits());
								in.putExtra("likes", article_pojo.getLikes());
								in.putExtra("description",
										article_pojo.getDescription());
								in.putExtra("is_new", article_pojo.getIsnew());

								// in.putExtra("comments",
								// article_pojo.getIdd());
								ctx.startActivity(in);

							}
						});

				article_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("text/plain");
								share.putExtra(Intent.EXTRA_TEXT,
										"I'm being sent!!");
								ctx.startActivity(Intent.createChooser(share,
										"Share Text"));
							}
						});

				  
					if (article_pojo.getLikes().equals("true")) {
						
						Log.e("TRUE", ststus_pos[position]);
						article_holder.like_image
								.setImageResource(R.drawable.home_like_active);
					} else {
						
						Log.e("FALSE", ststus_pos[position]);
						article_holder.like_image
								.setImageResource(R.drawable.home_like_inactive);
					}
					
				
					
				article_holder.like_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								
								if (article_pojo.getLikes().equals("true")) {
									int like_pos = (Integer) v.getTag();
									Log.e("IFFFF", article_pojo.getLikes());
									article_holder.like_image
											.setImageResource(R.drawable.home_like_inactive);
									//likeDatabaseHandler.updateLike(id, "false");
									
									ststus_pos[like_pos] = "false";
									

								} else {
									// likeDatabaseHandler.allDelete();
									Log.e("ELSEEEEE", article_pojo.getLikes());
									int like_pos = (Integer) v.getTag();
									article_holder.like_image
											.setImageResource(R.drawable.home_like_active);
									//likeDatabaseHandler.updateLike(id, "true");
									
									ststus_pos[like_pos] = "true";
									
								}

								
							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return article_result;

		case 3:
			View review_result = convertView;
			final ReviewHolder review_holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();

			if (review_result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				review_result = inf.inflate(R.layout.review_feed_item, parent, false);
				review_holder = new ReviewHolder();
				review_holder.user_name = (TextView) review_result
						.findViewById(R.id.user_name);
				review_holder.lookbook_view = (TextView) review_result
						.findViewById(R.id.user_view);
				review_holder.lookbook_like = (TextView) review_result
						.findViewById(R.id.txt_like_count);
				review_holder.user_image = (ImageView) review_result
						.findViewById(R.id.img_profile);

				review_holder.like_image = (ImageView) review_result
						.findViewById(R.id.img_like);
				review_holder.rel_store_rate = (RelativeLayout) review_result
						.findViewById(R.id.rel_rate);
				review_holder.share_image = (ImageView) review_result
						.findViewById(R.id.img_share);
				review_holder.review = (TextView) review_result
						.findViewById(R.id.txt_review);
				review_holder.store_name = (TextView) review_result
						.findViewById(R.id.txt_store_name);
				review_holder.store_address = (TextView) review_result
						.findViewById(R.id.txt_store_location);
				review_holder.store_rate = (TextView) review_result
						.findViewById(R.id.txt_rate);

				review_result.setTag(review_holder);
			} else {
				review_holder = (ReviewHolder) review_result.getTag();
			}

			final POJO review_pojo = mList.get(position);
			review_holder.like_image.setTag(position);
			try {

				review_holder.user_name.setText(review_pojo.getUsername());
				ImageLoader.getInstance().displayImage(
						review_pojo.getUserimg(), review_holder.user_image,
						options);
				review_holder.lookbook_like.setText(review_pojo.getLikes());
				review_holder.lookbook_view.setText(review_pojo.getHits());
				review_holder.review.setText(Html.fromHtml(review_pojo
						.getReview()));
				review_holder.store_name.setText(review_pojo.getStore_name());
				review_holder.store_address.setText(review_pojo
						.getStore_location());
				review_holder.store_rate.setText(review_pojo.getStore_rate());

				review_holder.rel_store_rate
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {

								Intent in = new Intent(ctx, StoreActivity.class);
								in.putExtra("store_id", review_pojo.getIdd());
								ctx.startActivity(in);

							}
						});

				review_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("text/plain");
								share.putExtra(Intent.EXTRA_TEXT,
										"I'm being sent!!");
								ctx.startActivity(Intent.createChooser(share,
										"Share Text"));
							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return review_result;

		case 4:
			View team_result = convertView;
			final TeamHolder team_holder;

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(colorlist.get(position))
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.cacheInMemory(true).delayBeforeLoading(500)
					.cacheOnDisk(true).build();

			if (team_result == null) {
				LayoutInflater inf = (LayoutInflater) ctx
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				team_result = inf.inflate(R.layout.team_feed_item, null, false);
				team_holder = new TeamHolder();
				team_holder.user_name = (TextView) team_result
						.findViewById(R.id.user_name);
				team_holder.lookbook_view = (TextView) team_result
						.findViewById(R.id.user_view);
				team_holder.lookbook_like = (TextView) team_result
						.findViewById(R.id.txt_like_count);
				team_holder.title = (TextView) team_result
						.findViewById(R.id.txt_title);
				team_holder.look_image = (DynamicImageView) team_result
						.findViewById(R.id.img_flash);

				team_holder.like_image = (ImageView) team_result
						.findViewById(R.id.img_like);

				team_holder.share_image = (ImageView) team_result
						.findViewById(R.id.img_share);

				team_result.setTag(team_holder);
			} else {
				team_holder = (TeamHolder) team_result.getTag();
			}

			final POJO team_pojo = mList.get(position);
			team_holder.like_image.setTag(position);
			try {

				team_holder.user_name.setText(team_pojo.getUsername());
				team_holder.lookbook_like.setText(team_pojo.getLikes());
				team_holder.lookbook_view.setText(team_pojo.getHits());
				team_holder.title.setText(team_pojo.getTitle());

				ImageLoader.getInstance().displayImage(team_pojo.getLookimg(),
						team_holder.look_image, options, animateFirstListener);

				team_holder.share_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("text/plain");
								share.putExtra(Intent.EXTRA_TEXT,
										"I'm being sent!!");
								ctx.startActivity(Intent.createChooser(share,
										"Share Text"));
							}
						});

			} catch (Exception e) {
				e.printStackTrace();
			}

			return team_result;

		default:

		}
		return null;
		

	}

	public boolean checkURL(CharSequence input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		Pattern URL_PATTERN = Patterns.WEB_URL;
		boolean isURL = URL_PATTERN.matcher(input).matches();
		if (!isURL) {
			String urlString = input + "";
			if (URLUtil.isNetworkUrl(urlString)) {
				try {
					new URL(urlString);
					isURL = true;
				} catch (Exception e) {
				}
			}
		}
		return isURL;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public void article_like(String article_id, final String like_get) {
		/*Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/"
				+ article_id + ".json");*/
		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
		String user_id = pro_user_pref.getString("user_id", "0");

		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams()
				.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		//Log.e("user_ID", user_id);
		//Log.e("TIME", String.valueOf(System.currentTimeMillis()));
		params.put("user_id", user_id);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("like", like_get);

		client.post(ctx.getString(R.string.base_url) + "articles/like/"
				+ article_id + ".json", params, new AsyncHttpResponseHandler() {

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

					/*
					 * if (like_get.equals("0")){ long count =
					 * Long.parseLong(get_likes)-1;
					 * lookbook_like.setText(""+count); gjh } else { long count
					 * = Long.parseLong(get_likes)+1;
					 * lookbook_like.setText(""+count); }
					 */
				//	Log.e("DATA", text2);
				} catch (Exception e) {

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {

			//	Log.e("FAIL", "FAIL");
			}
		});
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		if (getCount() != 0)
			return getCount();

		return 1;

	}

	public class LookbookHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		DynamicImageView look_image;
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_img_count;
		TextView image_count;

	}

	public class ArticleHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		DynamicImageView look_image;
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_img_count;
		TextView image_count;

	}

	public class ReviewHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		ImageView like_image, share_image;
		RelativeLayout rel_store_rate;
		TextView review, store_name, store_address, store_rate;

	}

	public class TeamHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		TextView title;
		DynamicImageView look_image;
		ImageView like_image, share_image;

	}

	@Override
	public int getItemViewType(int position) {

		final POJO pojo = mList.get(position);

		if (pojo.getMode().equals("Lookbooks")) {
			mode_position = 1;
		} else if (pojo.getMode().equals("Articles")) {
			mode_position = 2;
		} else if (pojo.getMode().equals("StoreReviews")) {
			mode_position = 3;
		} else if (pojo.getMode().equals("Teams")) {
			mode_position = 4;
		}

		return mode_position;
	}

}
