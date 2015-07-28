package com.store.model;

import com.google.gson.annotations.SerializedName;

public class LookbookCards {

	@SerializedName("id")
	private String id;
	@SerializedName("lookbook_id")
	private String lookbook_id;
	@SerializedName("description")
	private String description;
	@SerializedName("android_api_img")
	private String android_api_img;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLookbook_id() {
		return lookbook_id;
	}

	public void setLookbook_id(String lookbook_id) {
		this.lookbook_id = lookbook_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}
	
}
