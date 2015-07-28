package com.zakoopi.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zakoopi.R;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.MaterialProgressBar;

public class ViewHolder {

	View baseView;
	TextView user_name;
	TextView lookbook_view;
	TextView lookbook_like;
	ImageView user_image;
	TextView title;
	DynamicImageView look_image;
	ImageView like_image;
	ImageView share_image;
	ImageView img1, img2;
	RelativeLayout last_text;
	RelativeLayout rel_hit;
	RelativeLayout rel_store_rate;
	RelativeLayout rel_title;
	RelativeLayout rel_img_count;
	TextView image_count;
	TextView review;
	TextView store_name;
	TextView store_address;
	TextView store_rate;
	TextView txt_rated;
	MaterialProgressBar bar;
	

	public ViewHolder(View view) {

		this.baseView = view;
	}

	public RelativeLayout getlast_text() {
		if (last_text == null) {
			last_text = (RelativeLayout) baseView.findViewById(R.id.rel_post_img_count);
		}
		return last_text;
	}
	
	public RelativeLayout getrel_hit() {
		if (rel_hit == null) {
			rel_hit = (RelativeLayout) baseView.findViewById(R.id.rel_view);
		}
		return rel_hit;
	}
	public RelativeLayout getrel_store_rate() {
		if (rel_store_rate == null) {
			rel_store_rate = (RelativeLayout) baseView.findViewById(R.id.rel_rate);
		}
		return rel_store_rate;
	}
	public RelativeLayout getrel_title() {
		if (rel_title == null) {
			rel_title = (RelativeLayout) baseView.findViewById(R.id.rel_title);
		}
		return rel_title;
	}
	
	public RelativeLayout getrel_img_count() {
		if (rel_img_count == null) {
			rel_img_count = (RelativeLayout) baseView.findViewById(R.id.rel_113);
		}
		return rel_img_count;
	}
	

	
	public ImageView getuser_image() {
		if (user_image == null) {
			user_image = (ImageView) baseView.findViewById(R.id.img_profile);
		}
		return user_image;
	}

	public DynamicImageView getlook_image() {
		if (look_image == null) {
			look_image = (DynamicImageView) baseView.findViewById(R.id.img_flash);
		}
		return look_image;
	}

	public ImageView getlike_image() {
		if (like_image == null) {
			like_image = (ImageView) baseView.findViewById(R.id.img_like);
		}
		return like_image;
	}

	public ImageView getshare_image() {
		if (share_image == null) {
			share_image = (ImageView) baseView.findViewById(R.id.img_share);
		}
		return share_image;
	}

	public ImageView getimg1() {
		if (img1 == null) {
			img1 = (ImageView) baseView.findViewById(R.id.post_img1);
		}
		return img1;
	}
	
	public ImageView getimg2() {
		if (img2 == null) {
			img2 = (ImageView) baseView.findViewById(R.id.post_img2);
		}
		return img2;
	}
	

	public TextView getuser_name() {
		if (user_name == null) {
			user_name = (TextView) baseView.findViewById(R.id.user_name);
		}
		return user_name;
	}

	public TextView getlookbook_view() {
		if (lookbook_view == null) {
			lookbook_view = (TextView) baseView.findViewById(R.id.user_view);
		}
		return lookbook_view;
	}

	public TextView getlookbook_like() {
		if (lookbook_like == null) {
			lookbook_like = (TextView) baseView.findViewById(R.id.txt_like_count);
		}
		return lookbook_like;
	}

	public TextView gettitle() {
		if (title == null) {
			title = (TextView) baseView.findViewById(R.id.txt_title);
		}
		return title;
	}

	public TextView getimage_count() {
		if (image_count == null) {
			image_count = (TextView) baseView.findViewById(R.id.txt_count);
		}
		return image_count;
	}

	public TextView getreview() {
		if (review == null) {
			review = (TextView) baseView.findViewById(R.id.txt_review);
		}
		return review;
	}

	public TextView getstore_name() {
		if (store_name == null) {
			store_name = (TextView) baseView.findViewById(R.id.txt_store_name);
		}
		return store_name;
	}

	public TextView getstore_address() {
		if (store_address == null) {
			store_address = (TextView) baseView.findViewById(R.id.txt_store_location);
		}
		return store_address;
	}

	public TextView getstore_rate() {
		if (store_rate == null) {
			store_rate = (TextView) baseView.findViewById(R.id.txt_rate);
		}
		return store_rate;
	}
	
	public MaterialProgressBar getbar() {
		if (bar == null) {
			bar = (MaterialProgressBar) baseView.findViewById(R.id.progressBar1);
		}
		return bar;
	}
	
	public TextView gettext_rated() {
		if (txt_rated == null) {
			txt_rated = (TextView) baseView.findViewById(R.id.txt_rated);
		}
		return txt_rated;
	}

}
