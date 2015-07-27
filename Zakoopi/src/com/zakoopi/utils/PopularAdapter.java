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

import com.google.android.gms.drive.query.internal.InFilter;
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
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.POJO;

@SuppressWarnings("deprecation")
public class PopularAdapter extends BaseAdapter {

	private List<POJO> mList;
	private LayoutInflater mInflater;
	Context ctx;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;
	String pro_user_pic_url, pro_user_name, pro_user_location, user_email, user_password;
	AsyncHttpClient client = ClientHttp.getInstance();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	static byte[] res;
	SharedPreferences pro_user_pref;
	String article_color;
	ArrayList<Integer> colorlist;
	 DataObjectHolder holder = new DataObjectHolder();;
	public PopularAdapter(Context context, List<POJO> list,
			ArrayList<Integer> color) {
		ctx = context;
		mList = list;
		this.colorlist = color;

		

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
		// Debug.startMethodTracing("getViewOfTrace");
		View result = convertView;
		
		if (mInflater == null) {
			mInflater = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		

	//	 if (result == null) {

		 //LayoutInflater inflater = LayoutInflater.from(ctx);
		result = mInflater.inflate(R.layout.home_feed_item_layout, null, false);
		
		holder.user_name = (TextView) result.findViewById(R.id.user_name);
		holder.lookbook_view = (TextView) result.findViewById(R.id.user_view);
		holder.lookbook_like = (TextView) result
				.findViewById(R.id.txt_like_count);
		holder.user_image = (ImageView) result.findViewById(R.id.img_profile);
		holder.title = (TextView) result.findViewById(R.id.txt_title);
		holder.look_image = (DynamicImageView) result
				.findViewById(R.id.img_flash);
		holder.like_image = (ImageView) result.findViewById(R.id.img_like);
	//	holder.unlike_image = (ImageView) result.findViewById(R.id.img_unlike);
	//	holder.rel_like = (RelativeLayout) result.findViewById(R.id.rel_like);
		//holder.like_image.setImageResource(R.drawable.home_like_inactive);
		holder.share_image = (ImageView) result.findViewById(R.id.img_share);
		holder.img1 = (ImageView) result.findViewById(R.id.post_img1);
		holder.img2 = (ImageView) result.findViewById(R.id.post_img2);
		holder.last_text = (RelativeLayout) result
				.findViewById(R.id.rel_post_img_count);
		holder.image_count = (TextView) result.findViewById(R.id.txt_count);
		//
		holder.review = (TextView) result.findViewById(R.id.txt_review);
		holder.store_name = (TextView) result.findViewById(R.id.txt_store_name);
		holder.store_address = (TextView) result
				.findViewById(R.id.txt_store_location);
		holder.store_rate = (TextView) result.findViewById(R.id.txt_rate);
		holder.rel_hit = (RelativeLayout) result.findViewById(R.id.rel_view);
		
		holder.rel_title = (RelativeLayout) result.findViewById(R.id.rel_title);
		holder.rel_img_count = (RelativeLayout) result
				.findViewById(R.id.rel_113);

		/* result.setTag(holder);
		
		 } else {
		
		 holder = (DataObjectHolder) result.getTag();
		
		 }*/

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(colorlist.get(position))
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.delayBeforeLoading(500).cacheOnDisk(true).build();

		// BgImageAware imageAware = new BgImageAware(holder.look_image);
		 final POJO pojo = mList.get(position);
		if (pojo.getMode().equals("Lookbooks")) {
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
				holder.review.setVisibility(View.GONE);
				holder.look_image.setVisibility(View.VISIBLE);
				holder.rel_store_rate.setVisibility(View.GONE);

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
				
				holder.rel_title.setOnClickListener(new View.OnClickListener() {

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
		}
		// For article
		else if (pojo.getMode().equals("Articles")) {
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
				//Toast.makeText(ctx, pojo.getis_liked(), Toast.LENGTH_SHORT).show();
				Toast.makeText(ctx, pojo.getIdd(), Toast.LENGTH_SHORT).show();
				if (pojo.getis_liked().equals("true")) {
					article_color = "white";
					holder.like_image.setImageResource(R.drawable.home_like_active);
					//holder.like_image.setVisibility(View.VISIBLE);
				//	holder.unlike_image.setVisibility(View.GONE);
					Log.e("Image_true", ""+holder.like_image.getResources());
				} else {
					article_color = "red";
					holder.like_image.setImageResource(R.drawable.home_like_inactive);
					//holder.like_image.setVisibility(View.GONE);
				//	holder.unlike_image.setVisibility(View.VISIBLE);
					Log.e("Image_false", ""+holder.like_image.getResources());
				}
				
				holder.review.setVisibility(View.GONE);
				holder.look_image.setVisibility(View.VISIBLE);
				holder.rel_store_rate.setVisibility(View.GONE);

				holder.user_name.setText(pojo.getUsername());
				
				ImageLoader.getInstance().displayImage(pojo.getUserimg(),
						holder.user_image, options);
				
				holder.lookbook_view.setText(pojo.getHits());
				holder.title.setText(pojo.getTitle());
				holder.lookbook_like.setText(pojo.getLikes());
				

				holder.title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						holder.title.setText("ABCD");
						/*Intent in = new Intent(ctx, ArticleView.class);
						in.putExtra("article_id", pojo.getIdd());
						in.putExtra("username", pojo.getUsername());
						in.putExtra("userpicurl", pojo.getUserimg());
						in.putExtra("title", pojo.getTitle());
						in.putExtra("hits", pojo.getHits());
						in.putExtra("likes", pojo.getLikes());
						in.putExtra("description", pojo.getDescription());
						in.putExtra("is_new", pojo.getIsnew());

						// in.putExtra("comments", pojo.getIdd());
						ctx.startActivity(in);*/

					}
				});
				
				holder.look_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						

						Intent in = new Intent(ctx, ArticleView.class);
						in.putExtra("article_id", pojo.getIdd());
						in.putExtra("username", pojo.getUsername());
						in.putExtra("userpicurl", pojo.getUserimg());
						in.putExtra("title", pojo.getTitle());
						in.putExtra("hits", pojo.getHits());
						in.putExtra("likes", pojo.getLikes());
						in.putExtra("description", pojo.getDescription());
						in.putExtra("is_new", pojo.getIsnew());

						// in.putExtra("comments", pojo.getIdd());
						ctx.startActivity(in);

					}
				});
				
				holder.rel_img_count.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						

						Intent in = new Intent(ctx, ArticleView.class);
						in.putExtra("article_id", pojo.getIdd());
						in.putExtra("username", pojo.getUsername());
						in.putExtra("userpicurl", pojo.getUserimg());
						in.putExtra("title", pojo.getTitle());
						in.putExtra("hits", pojo.getHits());
						in.putExtra("likes", pojo.getLikes());
						in.putExtra("description", pojo.getDescription());
						in.putExtra("is_new", pojo.getIsnew());

						// in.putExtra("comments", pojo.getIdd());
						ctx.startActivity(in);

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
				
				holder.like_image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
						if (article_color.equals("white")) {
							article_color = "red";
							Toast.makeText(ctx, "Like", Toast.LENGTH_SHORT).show();
							holder.like_image.setImageResource(0);
							Log.e("NULL Image", ""+holder.like_image.getResources());
							holder.like_image.setImageResource(R.drawable.home_like_inactive);
							//holder.like_image.setVisibility(View.GONE);
							//holder.unlike_image.setVisibility(View.VISIBLE);
							Log.e("Image", ""+holder.like_image.getResources());
							
							
							//long count = Long.parseLong(holder.lookbook_like.getText().toString())+1;
							//holder.lookbook_like.setText(""+count);
						//	article_like(pojo.getIdd(), "1");
							
						} else {
							article_color = "white";
							holder.like_image.setImageResource(0);
							holder.like_image.setImageResource(R.drawable.home_like_active);
							//holder.like_image.setVisibility(View.VISIBLE);
							//holder.unlike_image.setVisibility(View.GONE);
							Toast.makeText(ctx, "UnLike", Toast.LENGTH_SHORT).show();
							//long count = Long.parseLong(holder.lookbook_like.getText().toString())-1;
							//holder.lookbook_like.setText(""+count);
						//	article_like(pojo.getIdd(), "0");
							
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// for Review
		else if (pojo.getMode().equals("StoreReviews")) {

			holder.review.setVisibility(View.VISIBLE);
			holder.look_image.setVisibility(View.GONE);
			holder.rel_img_count.setVisibility(View.GONE);
			holder.rel_store_rate.setVisibility(View.VISIBLE);
			holder.rel_title.setVisibility(View.GONE);

			try {

				holder.user_name.setText(pojo.getUsername());
				ImageLoader.getInstance().displayImage(pojo.getUserimg(),
						holder.user_image, options);
				holder.lookbook_like.setText(pojo.getLikes());
				holder.lookbook_view.setText(pojo.getHits());
				holder.review.setText(Html.fromHtml(pojo.getReview()));
				holder.store_name.setText(pojo.getStore_name());
				holder.store_address.setText(pojo.getStore_location());
				holder.store_rate.setText(pojo.getStore_rate());

				holder.rel_store_rate
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								

								Intent in = new Intent(ctx, StoreActivity.class);
								in.putExtra("store_id", pojo.getIdd());
								ctx.startActivity(in);

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

		}

		// for Zakoopi Teams
		else if ((pojo.getMode().equals("Teams"))) {
			try {

				holder.review.setVisibility(View.GONE);
				holder.look_image.setVisibility(View.VISIBLE);
				holder.rel_img_count.setVisibility(View.GONE);
				holder.rel_store_rate.setVisibility(View.GONE);
				holder.rel_title.setVisibility(View.VISIBLE);
				holder.user_image.setVisibility(View.GONE);
				//
				holder.user_name.setText(pojo.getUsername());
				holder.lookbook_like.setText(pojo.getLikes());
				holder.lookbook_view.setText(pojo.getHits());
				holder.title.setText(pojo.getTitle());

				ImageLoader.getInstance().displayImage(pojo.getLookimg(),
						holder.look_image, options, animateFirstListener);

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
		}

		return result;
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

	public class DataObjectHolder {
		// lookbook variables
		TextView user_name;
		TextView lookbook_view, lookbook_like;
		ImageView user_image;
		TextView title;
		DynamicImageView look_image;
		ImageView like_image, share_image, unlike_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_hit, rel_store_rate, rel_title,
				rel_img_count, rel_like;
		TextView image_count;
		TextView review, store_name, store_address, store_rate;

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
		Log.e("URL", ctx.getString(R.string.base_url) + "articles/like/"
				+ article_id + ".json");
		pro_user_pref = ctx.getSharedPreferences("User_detail", 0);
		String user_id = pro_user_pref.getString("user_id", "0");
		
		client.setBasicAuth(user_email, user_password);
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		Log.e("user_ID", user_id);
		Log.e("TIME", String.valueOf(System.currentTimeMillis()));
		params.put("user_id", user_id);
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("like", like_get);
				
		client.post(ctx.getString(R.string.base_url) + "articles/like/" + article_id
				+ ".json",params, new AsyncHttpResponseHandler() {

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
					
					/*if (like_get.equals("0")){
						long count = Long.parseLong(get_likes)-1;
						holder.lookbook_like.setText(""+count);
						gjh
					} else {
						long count = Long.parseLong(get_likes)+1;
						holder.lookbook_like.setText(""+count);
					}*/
					Log.e("DATA", text2);
				} catch (Exception e) {
					
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] response, Throwable e) {
				
				Log.e("FAIL", "FAIL");
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

}
