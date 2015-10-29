package com.zakoopilocation;

import android.content.Context;
import android.content.SharedPreferences;
import com.loopj.android.http.AsyncHttpClient;

public class ClientHttp {

	private static String city_name;
	public static AsyncHttpClient client;
	static Context ctx;
	private static SharedPreferences pref_location;

	public ClientHttp() {
	}

	public static AsyncHttpClient getInstance(Context context) {
		if (client == null) {
			client = new AsyncHttpClient();
		}
		try {
			ctx = context;
			pref_location = ctx.getSharedPreferences("location", 1);
			city_name = pref_location.getString("city", "delhi");
			client.addHeader("loc", city_name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return client;
	}
}
