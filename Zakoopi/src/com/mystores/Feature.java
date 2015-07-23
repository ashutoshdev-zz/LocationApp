package com.mystores;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.store.model.LookbookCards;
import com.store.model.LookbookUser;
import com.store.model.RelatedlookbookArrays;
import com.zakoopi.R;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.ExpandableHeightListView;
import com.zakoopi.helper.MaterialProgressBar;
import com.zakoopi.roundimagelib.SelectableRoundedImageView;

public class Feature extends Fragment {
	View feat;
	ExpandableHeightListView show_item;
	MyAdapter adapter;
	MaterialProgressBar progressBar;
	TextView txt_no_lookbook;
	ArrayList<RelatedlookbookArrays> lookbook = new ArrayList<RelatedlookbookArrays>();
	ArrayList<HashMap<String, String>> listmap = new ArrayList<HashMap<String, String>>();
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-featureerated method stub

		feat = inflater.inflate(R.layout.feature, null);
		show_item = (ExpandableHeightListView) feat
				.findViewById(R.id.PhoneImageGrid);
		progressBar = (MaterialProgressBar) feat.findViewById(R.id.progressBar);
		txt_no_lookbook = (TextView) feat.findViewById(R.id.txt_nodata);

//		progressBar.setColorSchemeResources(R.color.red, R.color.green,
//				R.color.blue, R.color.orange);

		show_item.setFocusable(false);

		show_item.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				MainFragment.scroll.setScrollingEnabled(true);
				return false;
			}
		});
		lookbook = MainFragment.detail.getRelated_lookbooks();
		if (lookbook.size() > 0) {
			new getImages().execute();
		} else {
			progressBar.setVisibility(View.GONE);
			txt_no_lookbook.setVisibility(View.VISIBLE);
		}

		return feat;
	}

	private class getImages extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String, String> map = null;
			int count = 0;
			int k = 0;
			lookbook = MainFragment.detail.getRelated_lookbooks();
		//	Log.e("LOOKBOOK", "" + lookbook.size());
			for (int i = 0; i < lookbook.size(); i++) {
				map = new HashMap<String, String>();

				ArrayList<LookbookCards> cardss = lookbook.get(i).getCards();

				for (int j = 0; j < cardss.size(); j++) {
					if (lookbook.get(i).getId()
							.equals(cardss.get(j).getLookbook_id())) {

						map.put("img" + k++, cardss.get(j).getAndroid_api_img());
						count++;

					}

				}
				map.put("count", String.valueOf(count));
				// Log.e("url", map.get("count"));
				listmap.add(map);
				// Log.e("url", listmap.get(i) + "");
				count = 0;
				k = 0;
			}

			return null;
		}

		protected void onPostExecute(Void result) {
			progressBar.setVisibility(View.GONE);
			adapter = new MyAdapter(getActivity(), lookbook, listmap);

			show_item.setAdapter(adapter);
			show_item.setExpanded(true);
			adapter.notifyDataSetChanged();
		}

	}

	public class MyAdapter extends BaseAdapter {

		LayoutInflater inf;
		Context ctx;
		ArrayList<RelatedlookbookArrays> list;
		ArrayList<HashMap<String, String>> listmapp = new ArrayList<HashMap<String, String>>();
		ViewHolder holder = null;

		// ArrayList<HashMap<String, String>> mapcountt = new
		// ArrayList<HashMap<String, String>>();

		public MyAdapter(Context ctx, ArrayList<RelatedlookbookArrays> list,
				ArrayList<HashMap<String, String>> listmap) {

			this.ctx = ctx;
			this.list = list;
			this.listmapp = listmap;

			inf = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return listmapp.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub

			View view = arg1;

			// if (view == null) {

			view = inf.inflate(R.layout.feature_item, null);
			holder = new ViewHolder();
			
			/**
			 * Typeface
			 */
			typeface_semibold = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Semibold.ttf");
			typeface_black = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Black.ttf");
			typeface_bold = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Bold.ttf");
			typeface_light = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Light.ttf");
			typeface_regular = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Regular.ttf");
			
			/**
			 * Find ID's
			 */
			holder.user_img = (CircleImageView) view.findViewById(R.id.img_profile);
			holder.img1 = (SelectableRoundedImageView) view.findViewById(R.id.post_img1);
			holder.img2 = (SelectableRoundedImageView) view.findViewById(R.id.post_img2);
			holder.full_img = (DynamicImageView) view
					.findViewById(R.id.img_flash);
			holder.username = (TextView) view.findViewById(R.id.user_name);
			holder.hits = (TextView) view.findViewById(R.id.user_view);
			holder.likes = (TextView) view.findViewById(R.id.txt_like_count);
			holder.img_count = (TextView) view.findViewById(R.id.txt_count);
			holder.title = (TextView) view.findViewById(R.id.txt_title);
			holder.rel_post_img_count = (RelativeLayout) view.findViewById(R.id.rel_post_img_count);
			//holder.progress = (ProgressBar) view.findViewById(R.id.progressBar1);
			
			
			holder.username.setTypeface(typeface_semibold);
			holder.hits.setTypeface(typeface_regular);
			holder.likes.setTypeface(typeface_regular);
			holder.img_count.setTypeface(typeface_bold);
			holder.title.setTypeface(typeface_semibold);
			view.setTag(holder);
			// } else {
			//
			// holder = (ViewHolder) view.getTag();
			// }

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.showImageOnLoading(R.drawable.ic_launcher).build();

			LookbookUser user = list.get(arg0).getUser();
			// Picasso.with(getActivity()).load(user.getAndroid_api_img())
			// .into(holder.user_img);

			imageLoader
					.displayImage(user.getAndroid_api_img(), holder.user_img);

			holder.username.setText(user.getFirst_name());

			holder.title.setText(list.get(arg0).getTitle());
			holder.hits.setText(list.get(arg0).getView_count());
			holder.likes.setText(list.get(arg0).getLookbooklike_count());

			if (Integer.parseInt(listmapp.get(arg0).get("count")) >= 3) {

				imageLoader.displayImage(listmapp.get(arg0).get("img0"),
						holder.full_img, options, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							//	holder.progress.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							//	holder.progress.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
							//	holder.progress.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							//	holder.progress.setVisibility(View.GONE);
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
							//	holder.progress.setVisibility(View.GONE);
							}
						});

				imageLoader.displayImage(listmapp.get(arg0).get("img1"),
						holder.img1);
				imageLoader.displayImage(listmapp.get(arg0).get("img2"),
						holder.img2);

				holder.img_count.setText("+" + listmapp.get(arg0).get("count"));

			} else if (Integer.parseInt(listmapp.get(arg0).get("count")) == 2) {

				imageLoader.displayImage(listmapp.get(arg0).get("img0"),
						holder.full_img, options, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								//holder.progress.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							//	holder.progress.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
							//	holder.progress.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							//	holder.progress.setVisibility(View.GONE);
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {

							}
						});

imageLoader.displayImage(listmapp.get(arg0).get("img1"),
		holder.img2);
				holder.img_count.setText("+" + listmapp.get(arg0).get("count"));
				holder.img1.setVisibility(View.GONE);
				//holder.img2.setVisibility(View.VISIBLE);

			} else {

				imageLoader.displayImage(listmapp.get(arg0).get("img0"),
						holder.full_img, options, new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							//	holder.progress.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
						//		holder.progress.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
						//		holder.progress.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
						//		holder.progress.setVisibility(View.GONE);
							}
						}, new ImageLoadingProgressListener() {
							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {

							}
						});

				holder.img2.setVisibility(View.GONE);
				holder.img1.setVisibility(View.GONE);
				holder.rel_post_img_count.setVisibility(View.GONE);
				holder.rel_post_img_count.setVisibility(View.GONE);

			}

			return view;
		}

		class ViewHolder {
			SelectableRoundedImageView img1, img2;
			CircleImageView user_img;
			DynamicImageView full_img;
			TextView username, hits, likes, img_count, title;
			ProgressBar progress;
			RelativeLayout rel_post_img_count;

		}
	}

}
