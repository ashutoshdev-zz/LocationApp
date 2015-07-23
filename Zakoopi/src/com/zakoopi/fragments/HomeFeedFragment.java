package com.zakoopi.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zakoopi.R;

public class HomeFeedFragment extends Fragment {
	LinearLayout lin_pop, lin_recent;
	RelativeLayout rel_pop, rel_recent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_feed_main_frag, null);

		lin_pop = (LinearLayout) view.findViewById(R.id.lin_popular);
		lin_recent = (LinearLayout) view.findViewById(R.id.lin_recent);
		lin_pop.setVisibility(View.VISIBLE);
		lin_recent.setVisibility(View.GONE);

		/**
		 * Add popularFeedFragment
		 */
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		fm.beginTransaction();
		Fragment fragTwo = new PopularFeedFragment();
		Bundle arguments = new Bundle();
		arguments.putBoolean("shouldYouCreateAChildFragment", false);
		fragTwo.setArguments(arguments);
		ft.add(R.id.lin_popular, fragTwo);

		ft.commit();

		return view;
	}
}
