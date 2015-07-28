package com.mystores;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zakoopi.R;
import com.zakoopi.helper.SquareImageView;

public class ImageGallery extends FragmentActivity {
	GridView gridview;
	MyAdapter adapter;
	RelativeLayout rel_back;
	RelativeLayout rel_pro;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_gallery);

		gridview = (GridView) findViewById(R.id.PhoneImageGrid);
		rel_pro = (RelativeLayout) findViewById(R.id.rel_pro);
		rel_back = (RelativeLayout) findViewById(R.id.rel_back);

		adapter = new MyAdapter(getApplicationContext(), General.image_url_list);
		gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		rel_pro.setVisibility(View.GONE);
		gridview.setVisibility(View.VISIBLE);
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	public class MyAdapter extends BaseAdapter {

		Context ctx;
		ArrayList<String> list;
		LayoutInflater inf;

		public MyAdapter(Context ctx, ArrayList<String> list) {

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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = arg1;
			ViewHolder holder = null;
			if (view == null) {

				view = inf.inflate(R.layout.galleryitem, null);
				holder = new ViewHolder();
				holder.img = (SquareImageView) view
						.findViewById(R.id.thumbImage);
				view.setTag(holder);

			} else {

				holder = (ViewHolder) view.getTag();

			}

			
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisc(true).resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.showImageOnLoading(R.drawable.ic_launcher).build();
			
//			Ion.with(ctx).load(list.get(arg0)).withBitmap()
//					.placeholder(R.drawable.ic_launcher)
//					.error(R.drawable.ic_launcher).intoImageView(holder.img);
			
//			Picasso.with(ctx).load(list.get(arg0))
//			.placeholder(R.drawable.ic_launcher)
//			.error(R.drawable.ic_launcher).into(holder.img);
			
			imageLoader.displayImage(list.get(arg0), holder.img);

			holder.img.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent in=new Intent(ImageGallery.this,ZoomImages.class);
					in.putExtra("pos", String.valueOf(arg0));
					startActivity(in);
				}
			});
			return view;
		}

		class ViewHolder {

			SquareImageView img;
		}
	}
}
