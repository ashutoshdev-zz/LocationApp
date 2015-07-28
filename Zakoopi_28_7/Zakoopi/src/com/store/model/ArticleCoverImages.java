package com.store.model;

import com.google.gson.annotations.SerializedName;

public class ArticleCoverImages {

	@SerializedName("article_id")
	private String article_id;
	@SerializedName("title")
	private String title;
	@SerializedName("android_api_img")
	private String android_api_img;

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAndroid_api_img() {
		return android_api_img;
	}

	public void setAndroid_api_img(String android_api_img) {
		this.android_api_img = android_api_img;
	}
}
