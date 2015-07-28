package com.store.model;

import com.google.gson.annotations.SerializedName;

public class StoreReviewArrays {

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
	@SerializedName("likes_count")
	private String likes_count;
	@SerializedName("is_liked")
	private String is_liked;
	StoreReviewUsers user;
	
	public StoreReviewUsers getUser() {
		return user;
	}
	public void setUser(StoreReviewUsers user) {
		this.user = user;
	}
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
	public String getLikes_count() {
		return likes_count;
	}
	public void setLikes_count(String likes_count) {
		this.likes_count = likes_count;
	}
	public String getIs_liked() {
		return is_liked;
	}
	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}
}
