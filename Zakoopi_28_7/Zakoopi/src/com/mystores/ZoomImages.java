package com.mystores;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.zakoopi.R;
import com.zakoopi.helper.DynamicImageView;

public class ZoomImages extends Activity {

	ViewPager viewPager;
	PagerAdapter adapter;
    String pos="";
    RelativeLayout rel_back;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zoom_image);

		Intent in=getIntent();
		pos=in.getStringExtra("pos");
		//Log.e("pos", pos);
		
		// Locate the ViewPager in viewpager_main.xml
		viewPager = (ViewPager) findViewById(R.id.pager);
		rel_back = (RelativeLayout) findViewById(R.id.rel1);
		// Pass results to ViewPagerAdapter Class
		adapter = new ViewPagerAdapter(ZoomImages.this,General.image_url_list);
		// Binds the Adapter to the ViewPager
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(Integer.parseInt(pos));
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				finish();
			}
		});

	}
	
	public class ViewPagerAdapter extends PagerAdapter {
		// Declare Variables
		Context context;
		LayoutInflater inflater;
		ArrayList<String>list;
		DynamicImageView imgflag;
		ProgressBar prog;
	 
		public ViewPagerAdapter(Context context,ArrayList<String>list) {
			this.context = context;
			this.list=list;
			inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			
			//position=Integer.parseInt(pos);
			
			View itemView = inflater.inflate(R.layout.zoom_items, container,false);
	 
			// Locate the ImageView in viewpager_item.xml
			imgflag = (DynamicImageView) itemView.findViewById(R.id.imageView2);
			prog=(ProgressBar) itemView.findViewById(R.id.progressBar1);
			
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisc(true).resetViewBeforeLoading(true)
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.showImageOnLoading(R.drawable.ic_launcher).build();
			
			ImageLoader.getInstance().displayImage(list.get(position), imgflag, options, new ImageLoadingListener() {
			    @Override
			    public void onLoadingStarted(String imageUri, View view) {
			    	prog.setVisibility(View.VISIBLE);
			    }
			    @Override
			    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			    	prog.setVisibility(View.GONE);
			    }
			    @Override
			    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			    	prog.setVisibility(View.GONE);
			    }
			    @Override
			    public void onLoadingCancelled(String imageUri, View view) {
			    	prog.setVisibility(View.GONE);
			    }
			}, new ImageLoadingProgressListener() {
			    @Override
			    public void onProgressUpdate(String imageUri, View view, int current, int total) {
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
	
}
