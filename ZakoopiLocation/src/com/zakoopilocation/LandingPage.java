package com.zakoopilocation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LandingPage extends Activity {

	Button curr;
	TextView lat;
	TextView lng;
	Button store;

	public LandingPage() {
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(1);
		setContentView(R.layout.landing_page);
	}
}
