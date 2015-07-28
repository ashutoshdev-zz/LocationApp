package com.zakoopi.utils;

import com.loopj.android.http.AsyncHttpClient;

public class ClientHttp {
	public static AsyncHttpClient client;
	public static AsyncHttpClient getInstance() {
		if (client == null) {
			client = new AsyncHttpClient();
		}
		return client;
	}
}
