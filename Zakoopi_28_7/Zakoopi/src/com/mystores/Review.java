package com.mystores;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.store.model.StoreReviewArrays;
import com.store.model.StoreReviewUsers;
import com.zakoopi.R;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.ExpandableHeightListView;

public class Review extends Fragment {

	View rev;
	ExpandableHeightListView show_item;
	MyAdapter adapter;
	ArrayList<StoreReviewArrays> storeReview = new ArrayList<StoreReviewArrays>();
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		rev = inflater.inflate(R.layout.review, null);
		show_item = (ExpandableHeightListView) rev
				.findViewById(R.id.PhoneImageGrid);
		
		show_item.setFocusable(false);

		show_item.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				MainFragment.scroll.setScrollingEnabled(true);
				return false;
			}
		});

		new getImages().execute();
		return rev;
	}

	private class getImages extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			storeReview = MainFragment.detail.getStore_reviews();

			return null;
		}

		protected void onPostExecute(Void result) {
			adapter = new MyAdapter(getActivity(), storeReview);
			show_item.setAdapter(adapter);
			show_item.setExpanded(true);
			adapter.notifyDataSetChanged();
		}

	}

	public class MyAdapter extends BaseAdapter {

		LayoutInflater inf;
		Context ctx;
		ArrayList<StoreReviewArrays> list;

		public MyAdapter(Context ctx, ArrayList<StoreReviewArrays> list) {

			this.ctx = ctx;
			this.list = list;

			inf = (LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return list.size();
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
			ViewHolder holder = null;

			view = inf.inflate(R.layout.review_item, null);
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
			holder.username = (TextView) view.findViewById(R.id.user_name);
			holder.hits = (TextView) view.findViewById(R.id.user_view);
			holder.likes = (TextView) view.findViewById(R.id.txt_like_count);
			holder.title = (TextView) view.findViewById(R.id.txt_review);
			
			/**
			 * Set Font on TextView
			 */
			holder.username.setTypeface(typeface_semibold);
			holder.hits.setTypeface(typeface_regular);
			holder.likes.setTypeface(typeface_regular);
			holder.title.setTypeface(typeface_regular);
			
			view.setTag(holder);
			
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisc(true).resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.showImageOnLoading(R.drawable.ic_launcher).build();
			
           StoreReviewUsers user=list.get(arg0).getUser();
			
			imageLoader.displayImage(user.getAndroid_api_img(), holder.user_img);
			
			holder.username.setText(user.getFirst_name());

			holder.title.setText(Html.fromHtml(list.get(arg0).getReview()));
			holder.hits.setText(list.get(arg0).getHits());
			holder.likes.setText(list.get(arg0).getLikes_count());
			
			
		//	holder.storename.setText(MainFragment.detail.getStore_name());
		//	holder.storeaddress.setText(MainFragment.detail.getMarket());
			//holder.storerate.setText(MainFragment.detail.getOverall_ratings());
			

			return view;
		}

		class ViewHolder {
			CircleImageView user_img;
			TextView username, hits, likes,title;

		}
	}

}
