package com.zakoopi.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
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

public class RecentAdapter extends BaseAdapter {

	private List<POJO> mList;
	private LayoutInflater mInflater;
	Context ctx;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;

	ArrayList<Integer> colorlist;

	public RecentAdapter(Context context, List<POJO> list,
			ArrayList<Integer> color) {
		ctx = context;
		mList = list;
		this.colorlist = color;

		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// options = new DisplayImageOptions.Builder()
		// .showStubImage(R.color.clouds)
		// //.showImageOnLoading(R.drawable.background)
		// .showImageForEmptyUri(R.drawable.ic_launcher)
		// .showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		// .cacheOnDisk(true).considerExifParams(true).build();

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
		DataObjectHolder holder;
		final POJO pojo = mList.get(position);

		// if (result == null) {

		// LayoutInflater inflater = LayoutInflater.from(ctx);
		result = mInflater.inflate(R.layout.home_feed_item_layout, null, false);
		holder = new DataObjectHolder();
		holder.user_name = (TextView) result.findViewById(R.id.user_name);
		holder.lookbook_view = (TextView) result.findViewById(R.id.user_view);
		holder.lookbook_like = (TextView) result
				.findViewById(R.id.txt_like_count);
		holder.user_image = (ImageView) result.findViewById(R.id.img_profile);
		holder.title = (TextView) result.findViewById(R.id.txt_title);
		holder.look_image = (DynamicImageView) result
				.findViewById(R.id.img_flash);
		holder.like_image = (ImageView) result.findViewById(R.id.img_like);
		holder.like_image.setImageResource(R.drawable.home_like_inactive);
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
		holder.rel_store_rate = (RelativeLayout) result
				.findViewById(R.id.rel_rate);
		holder.rel_title = (RelativeLayout) result.findViewById(R.id.rel_title);
		holder.rel_img_count = (RelativeLayout) result
				.findViewById(R.id.rel_113);

		// result.setTag(holder);
		//
		// } else {
		//
		// holder = (DataObjectHolder) result.getTag();
		//
		// }

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(colorlist.get(position))
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.delayBeforeLoading(500).cacheOnDisk(true).build();

		// BgImageAware imageAware = new BgImageAware(holder.look_image);

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

						holder.last_text.setVisibility(View.GONE);
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
				//
				holder.look_image
						.setOnClickListener(new View.OnClickListener() {
							//
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
				holder.review.setVisibility(View.GONE);
				holder.look_image.setVisibility(View.VISIBLE);
				holder.rel_store_rate.setVisibility(View.GONE);

				holder.user_name.setText(pojo.getUsername());
				//
				ImageLoader.getInstance().displayImage(pojo.getUserimg(),
						holder.user_image, options);
				//
				holder.lookbook_view.setText(pojo.getHits());
				holder.title.setText(pojo.getTitle());
				holder.lookbook_like.setText(pojo.getLikes());

				holder.title.setOnClickListener(new OnClickListener() {

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

						ctx.startActivity(in);

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

				if (pojo.getis_liked().equals("false")) {

					holder.like_image
							.setImageResource(R.drawable.home_like_inactive);
				} else {
					holder.like_image
							.setImageResource(R.drawable.home_like_active);
				}

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
		ImageView like_image, share_image;
		ImageView img1, img2;
		RelativeLayout last_text, rel_hit, rel_store_rate, rel_title,
				rel_img_count;
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

}
