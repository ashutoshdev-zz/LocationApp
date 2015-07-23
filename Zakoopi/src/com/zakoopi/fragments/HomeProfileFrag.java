package com.zakoopi.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zakoopi.R;
import com.zakoopi.activity.EditProfilePage;
import com.zakoopi.activity.UserFollowPage;
import com.zakoopi.activity.UserNotificationPage;
import com.zakoopi.adapter.ProfileRecyclerViewAdapter;
import com.zakoopi.helper.CircleImageView;
import com.zakoopi.helper.MyTextViewFont;
import com.zakoopi.helper.RecyclerViewHeader;
import com.zakoopi.model.HomeLookbookDataObject;

public class HomeProfileFrag extends Fragment{
	
	private RecyclerViewHeader mRecyclerHeader;
    private RecyclerView mRecycler;
    private RecyclerView.Adapter lookbookAdapter;
    ImageView img_edit_profile;
    RelativeLayout rel_noti,  rel_follow;
    TextView txt;
    CircleImageView  img_profile;
    MyTextViewFont txt_userName, txt_userAge, txt_userGender, txt_userLocation, txt_rewardPoint, txt_reviewCount, txt_likeCount;
    private SharedPreferences pro_user_pref;
    String userPic_url, userName, userLocation, userAge, userGender, userRewardPoint, userReviewCount, userLikeCount;
    private DisplayImageOptions options;
    
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View view = inflater.inflate(R.layout.home_profile_frag, null);
		
		/**
		 * User Login SharedPreferences
		 */
		pro_user_pref = getActivity().getSharedPreferences("User_detail", 0);
		userPic_url = pro_user_pref.getString("user_image", "123");
		userName = pro_user_pref.getString("user_firstName", "012") + " "
				+ pro_user_pref.getString("user_lastName", "458");
		userLocation = pro_user_pref.getString("user_location", "4267");
		userAge = pro_user_pref.getString("user_age","hgjh" );
		userGender = pro_user_pref.getString("user_gender","123458" );
		userRewardPoint = pro_user_pref.getString("user_rewardPoints","agty" );
		userReviewCount = pro_user_pref.getString("user_reviewCount","poi" );
		userLikeCount = pro_user_pref.getString("user_likeCount","tyu" );
		
		
		setupViews(view);
		
		ImageLoader.getInstance().displayImage(userPic_url, img_profile, options);
		txt_userName.setText(userName);
		txt_userAge.setText(userAge);
		txt_userGender.setText(userGender);
		txt_userLocation.setText(userLocation);
		txt_rewardPoint.setText(userRewardPoint);
		txt_reviewCount.setText(userReviewCount);
		txt_likeCount.setText(userLikeCount);
		
		return view;
	}
	 private void setupViews(View view) {
	        mRecycler = (RecyclerView) view.findViewById(R.id.recycler);
	        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
	        lookbookAdapter = new ProfileRecyclerViewAdapter(getDataSet());
	        mRecycler.setAdapter(lookbookAdapter);

	        mRecyclerHeader = (RecyclerViewHeader) view.findViewById(R.id.header);
	        img_edit_profile = (ImageView) view.findViewById(R.id.img_edt_profile);
	        rel_noti = (RelativeLayout) view.findViewById(R.id.rel_noti);
	        rel_follow = (RelativeLayout) view.findViewById(R.id.rel_follow);
	        txt_userName = (MyTextViewFont) view.findViewById(R.id.txt_user_name);
	        txt_userAge = (MyTextViewFont) view.findViewById(R.id.txt_user_age);
	        txt_userGender = (MyTextViewFont) view.findViewById(R.id.txt_user_gander);
	        txt_userLocation = (MyTextViewFont) view.findViewById(R.id.txt_user_location);
	        txt_rewardPoint = (MyTextViewFont) view.findViewById(R.id.txt_point);
	        txt_reviewCount = (MyTextViewFont) view.findViewById(R.id.txt_review_count);
	        txt_likeCount = (MyTextViewFont) view.findViewById(R.id.txt_like_count);
	        img_profile = (CircleImageView) view.findViewById(R.id.img_profile);
	        
	        mRecyclerHeader.attachTo(mRecycler, true);
	        
	        click();
	    }
	
	 public void click() {
		 
		 img_edit_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent edt_profile = new Intent(getActivity(), EditProfilePage.class);
				startActivity(edt_profile);
			}
		});
		 
		 rel_noti.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent noti = new Intent(getActivity(), UserNotificationPage.class);
				startActivity(noti);
			}
		});
		 
		
		
		rel_follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent follow = new Intent(getActivity(), UserFollowPage.class);
				startActivity(follow);
			}
		});
	 }
	 private ArrayList<HomeLookbookDataObject> getDataSet() {
	        ArrayList results = new ArrayList<HomeLookbookDataObject>();
	        for (int index = 0; index < 20; index++) {
	        	HomeLookbookDataObject obj = new HomeLookbookDataObject("Reviewed",
	                    "" + index );
	            results.add(index, obj);
	        }
	        return results;
	    }
}
