package com.mystores;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.store.model.LookbookCards;
import com.store.model.RelatedlookbookArrays;
import com.store.model.StoreImageArrays;
import com.store.model.StoreofferingArrays;
import com.store.model.StoreofferingDetais;
import com.tabbar.cam.NestedListView;
import com.zakoopi.R;

public class General extends Fragment {

	// Google Map
	//GoogleMap googleMap;
	NestedListView catalogue;
	View gen;
	MyAdapter adapter;
	ArrayList<String> menoffername = new ArrayList<String>();
	ArrayList<String> menofferrangefrom = new ArrayList<String>();
	ArrayList<String> menofferrangeto = new ArrayList<String>();

	ArrayList<String> womenoffername = new ArrayList<String>();
	ArrayList<String> womenofferrangefrom = new ArrayList<String>();
	ArrayList<String> womenofferrangeto = new ArrayList<String>();

	ArrayList<String> kidoffername = new ArrayList<String>();
	ArrayList<String> kidofferrangefrom = new ArrayList<String>();
	ArrayList<String> kidofferrangeto = new ArrayList<String>();

	ImageView kids, women, men;
	ImageView img1, img2, img3, img4;
	RelativeLayout rel_img_count, rel_store_image;
	TextView image_count, txt_catalogue,txt_photos,txt_map;
	double lat, lng;
	ArrayList<StoreImageArrays> store_images = new ArrayList<StoreImageArrays>();
	ArrayList<RelatedlookbookArrays> lookbook_images = new ArrayList<RelatedlookbookArrays>();
	ArrayList<StoreImageArrays> storeImage = new ArrayList<StoreImageArrays>();
	ArrayList<StoreofferingArrays> store_offerings = new ArrayList<StoreofferingArrays>();
	public static ArrayList<String> image_url_list;
	ImageLoader imageLoader;
	boolean menbool= false;
	boolean womenbool= false;
	boolean kidbool= false;
	Typeface typeface_semibold, typeface_black, typeface_bold, typeface_light,
	typeface_regular;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		gen = inflater.inflate(R.layout.general, null);
	//	googleMap = ((SupportMapFragment) getChildFragmentManager()
	//			.findFragmentById(R.id.location_map)).getMap();
		catalogue = (NestedListView) gen.findViewById(R.id.benchmarksList);
		catalogue.setFocusable(false);
		kids = (ImageView) gen.findViewById(R.id.imageView1);
		women = (ImageView) gen.findViewById(R.id.imageView2);
		men = (ImageView) gen.findViewById(R.id.imageView3);
		img1 = (ImageView) gen.findViewById(R.id.img_store_1);
		img2 = (ImageView) gen.findViewById(R.id.img_store_2);
		img3 = (ImageView) gen.findViewById(R.id.img_store_3);
		img4 = (ImageView) gen.findViewById(R.id.img_store_4);
		rel_store_image = (RelativeLayout) gen
				.findViewById(R.id.rel_store_image);
		rel_img_count = (RelativeLayout) gen.findViewById(R.id.rel_img_count);
		image_count = (TextView) gen.findViewById(R.id.txt_photo_count);
		txt_catalogue = (TextView) gen.findViewById(R.id.txt_catalogue);
		txt_photos = (TextView) gen.findViewById(R.id.txt_photos);
		txt_map = (TextView) gen.findViewById(R.id.txt_map);
		kids.setVisibility(View.GONE);
		men.setVisibility(View.GONE);
		women.setVisibility(View.GONE);
		lat = Double.parseDouble(MainFragment.detail.getLatitude());
		lng = Double.parseDouble(MainFragment.detail.getLongitude());
		image_url_list = new ArrayList<String>();

		/**
		 * Typeface
		 */
		typeface_semibold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");
		typeface_black = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Black.ttf");
		typeface_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Bold.ttf");
		typeface_light = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Light.ttf");
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		
		/**
		 * Set Font on TextView
		 */
		
		txt_catalogue.setTypeface(typeface_semibold);
		txt_photos.setTypeface(typeface_semibold);
		txt_map.setTypeface(typeface_semibold);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.resetViewBeforeLoading(true)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher).build();

		/**
		 * Getting all types images of store
		 */
		store_images = MainFragment.detail.getStore_images();
		for (int i = 0; i < store_images.size(); i++) {

			image_url_list.add(store_images.get(i).getAndroid_api_img());
		}

		lookbook_images = MainFragment.detail.getRelated_lookbooks();
		for (int i = 0; i < lookbook_images.size(); i++) {
			ArrayList<LookbookCards> cardss = lookbook_images.get(i).getCards();
			for (int j = 0; j < cardss.size(); j++) {

				image_url_list.add(cardss.get(j).getAndroid_api_img());
			}

		}

		storeImage = MainFragment.detail.getStore_images();
		for (int i = 0; i < storeImage.size(); i++) {

			image_url_list.add(storeImage.get(i).getAndroid_api_img());
		}

		for (int j = 0; j < image_url_list.size(); j++) {
			if (image_url_list.size() == 0) {
				rel_store_image.setVisibility(View.GONE);
			} else {
				if (image_url_list.size() >= 4) {
					img1.setVisibility(View.VISIBLE);
					img2.setVisibility(View.VISIBLE);
					img3.setVisibility(View.VISIBLE);
					img4.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.VISIBLE);

					rel_store_image.setVisibility(View.VISIBLE);
					imageLoader.displayImage(image_url_list.get(4), img4);
					imageLoader.displayImage(image_url_list.get(3), img3);
					imageLoader.displayImage(image_url_list.get(2), img2);
					imageLoader.displayImage(image_url_list.get(1), img1);
					image_count.setText("+" + (image_url_list.size()));

				} else if (image_url_list.size() == 3) {

					img2.setVisibility(View.VISIBLE);
					img3.setVisibility(View.VISIBLE);
					img4.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.VISIBLE);
					rel_store_image.setVisibility(View.VISIBLE);
					imageLoader.displayImage(image_url_list.get(4), img4);
					imageLoader.displayImage(image_url_list.get(3), img3);
					imageLoader.displayImage(image_url_list.get(2), img2);
					// imageLoader.displayImage(image_url_list.get(1), img1);
					image_count.setText("+" + (image_url_list.size()));
				}

				else if (image_url_list.size() == 2) {
					img3.setVisibility(View.VISIBLE);
					img4.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.VISIBLE);
					rel_store_image.setVisibility(View.VISIBLE);
					imageLoader.displayImage(image_url_list.get(4), img4);
					imageLoader.displayImage(image_url_list.get(3), img3);
					// imageLoader.displayImage(image_url_list.get(2), img2);

					image_count.setText("+" + (image_url_list.size()));
				}

				else if (image_url_list.size() == 1) {

					img4.setVisibility(View.VISIBLE);
					rel_img_count.setVisibility(View.VISIBLE);
					rel_store_image.setVisibility(View.VISIBLE);
					imageLoader.displayImage(image_url_list.get(4), img4);
					image_count.setText("+" + (image_url_list.size()));
				}

			}

		}

	//	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
	//			lat, lng), 14));

	//	MarkerOptions marker = new MarkerOptions().position(
	//			new LatLng(lat, lng)).title(
	//			MainFragment.detail.getStore_address());
		// adding marker
	//	googleMap.addMarker(marker);

		/**
		 * Getting all offerings of store
		 */

		
		store_offerings = MainFragment.detail.getStore_offerings();
		for (int k = 0; k < store_offerings.size(); k++) {

			StoreofferingDetais offering = store_offerings.get(k).getOffering();

			if (offering.getCategory().equals("Men")) {

				men.setVisibility(View.VISIBLE);
				menoffername.add(offering.getName());
				menofferrangefrom.add(store_offerings.get(k)
						.getPrice_range_from());
				menofferrangeto.add(store_offerings.get(k).getPrice_range_to());
				menbool=true;

			} else if (offering.getCategory().equals("Women")) {

				women.setVisibility(View.VISIBLE);
				womenoffername.add(offering.getName());
				womenofferrangefrom.add(store_offerings.get(k)
						.getPrice_range_from());
				womenofferrangeto.add(store_offerings.get(k)
						.getPrice_range_to());
				womenbool=true;

			} else if (offering.getCategory().equals("Kids")) {

				kids.setVisibility(View.VISIBLE);
				kidoffername.add(offering.getName());
				kidofferrangefrom.add(store_offerings.get(k)
						.getPrice_range_from());
				kidofferrangeto.add(store_offerings.get(k).getPrice_range_to());
				kidbool=true;
			}
		}

		kids.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				catalogue.setAdapter(null);
				adapter = new MyAdapter(getActivity(), kidoffername,
						kidofferrangefrom, kidofferrangeto);

				catalogue.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				kids.setImageResource(R.drawable.general_kids_active);
				men.setImageResource(R.drawable.general_men_inactive);
				women.setImageResource(R.drawable.general_women_inactive);
			}
		});

		men.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				catalogue.setAdapter(null);
				adapter = new MyAdapter(getActivity(), menoffername,
						menofferrangefrom, menofferrangeto);

				catalogue.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				kids.setImageResource(R.drawable.general_kids_inactive);
				men.setImageResource(R.drawable.general_men_active);
				women.setImageResource(R.drawable.general_women_inactive);

			}
		});

		women.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				catalogue.setAdapter(null);
				adapter = new MyAdapter(getActivity(), womenoffername,
						womenofferrangefrom, womenofferrangeto);

				catalogue.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				kids.setImageResource(R.drawable.general_kids_inactive);
				men.setImageResource(R.drawable.general_men_inactive);
				women.setImageResource(R.drawable.general_women_active);

			}
		});

	if(womenbool==true){
		adapter = new MyAdapter(getActivity(), womenoffername,
				womenofferrangefrom, womenofferrangeto);
		catalogue.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}else if(womenbool==false&&menbool==true){
		adapter = new MyAdapter(getActivity(), menoffername,
				menofferrangefrom, menofferrangeto);
		catalogue.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		men.setImageResource(R.drawable.general_men_active);
		
	}else if(womenbool==false&&menbool==false&&kidbool==true){
		
		adapter = new MyAdapter(getActivity(), kidoffername,
				kidofferrangefrom, kidofferrangeto);
		catalogue.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		kids.setImageResource(R.drawable.general_kids_active);
	}else if(womenbool==false&&menbool==true&&kidbool==true){
		adapter = new MyAdapter(getActivity(), menoffername,
				menofferrangefrom, menofferrangeto);
		catalogue.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		men.setImageResource(R.drawable.general_men_active);
		
	}
		image_count.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), ImageGallery.class);
				startActivity(in);
			}
		});
		return gen;
	}


	public class MyAdapter extends BaseAdapter {

		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		ArrayList<String> list3 = new ArrayList<String>();
		Context ctx;
		LayoutInflater inf;

		public MyAdapter(Context ctx, ArrayList<String> list1,
				ArrayList<String> list2, ArrayList<String> list3) {

			this.ctx = ctx;

			this.list1 = list1;
			this.list2 = list2;
			this.list3 = list3;
			inf = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list1.size();
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

			ViewHolder holder = null;
			View view = arg1;
			if (view == null) {
				view = inf.inflate(R.layout.gernal_item, null);
				holder = new ViewHolder();
				holder.tv1 = (TextView) view.findViewById(R.id.txt_price_start);
				holder.tv2 = (TextView) view.findViewById(R.id.txt_price_end);
				
				typeface_regular = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/SourceSansPro-Regular.ttf");
				holder.tv1.setTypeface(typeface_semibold);
				holder.tv2.setTypeface(typeface_semibold);
				view.setTag(holder);
			} else {

				holder = (ViewHolder) view.getTag();
			}

			holder.tv1.setText(list1.get(arg0));
			holder.tv2.setText("\u20B9 " + list2.get(arg0) + " - " + "\u20B9 "
					+ list3.get(arg0));

			return view;
		}

		class ViewHolder {
			TextView tv1;
			TextView tv2;
		}
	}

}
