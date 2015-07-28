package com.zakoopi.homefeed;

import com.google.gson.annotations.SerializedName;

public class Popular_StoreReviewData {

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
	
	@SerializedName("is_liked")
	private String is_liked;

	Popular_StoreReview_Users user;
	Popular_StoreReview_Store store;

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
	
	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	public Popular_StoreReview_Users getUser() {
		return user;
	}

	public void setUser(Popular_StoreReview_Users user) {
		this.user = user;
	}

	public Popular_StoreReview_Store getStore() {
		return store;
	}

	public void setStore(Popular_StoreReview_Store store) {
		this.store = store;
	}
}
