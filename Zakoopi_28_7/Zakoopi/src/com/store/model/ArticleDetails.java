package com.store.model;

import com.google.gson.annotations.SerializedName;

public class ArticleDetails {

	@SerializedName("title")
	private String title;
	@SerializedName("hits")
	private String hits;
	@SerializedName("likes_count")
	private String likes_count;
	@SerializedName("is_liked")
	private String is_liked;
	ArticleCoverImages cover_img;
	ArticleCoverImages user;
	
	public ArticleCoverImages getUser() {
		return user;
	}

	public void setUser(ArticleCoverImages user) {
		this.user = user;
	}

	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	

	public ArticleCoverImages getCover_img() {
		return cover_img;
	}

	public void setCover_img(ArticleCoverImages cover_img) {
		this.cover_img = cover_img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
}
