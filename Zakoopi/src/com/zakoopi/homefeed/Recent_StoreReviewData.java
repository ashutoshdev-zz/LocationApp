package com.zakoopi.homefeed;

import com.google.gson.annotations.SerializedName;

public class Recent_StoreReviewData {

	@SerializedName("id")
	private String id;

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("store_id")
	private String store_id;

	@SerializedName("review")
	private String review;

	@SerializedName("hits")
	private String hits;

	Recent_StoreReview_Users user;
	Recent_StoreReview_Store store;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public Recent_StoreReview_Users getUser() {
		return user;
	}

	public void setUser(Recent_StoreReview_Users user) {
		this.user = user;
	}

	public Recent_StoreReview_Store getStore() {
		return store;
	}

	public void setStore(Recent_StoreReview_Store store) {
		this.store = store;
	}
}
