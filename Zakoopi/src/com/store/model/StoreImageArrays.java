package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreImageArrays {

	@SerializedName("id")
	private String id;
	@SerializedName("store_id")
	private String store_id;
	@SerializedName("desc")
	private String desc;
	@SerializedName("android_api_img")
	private String android_api_img;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}
}
