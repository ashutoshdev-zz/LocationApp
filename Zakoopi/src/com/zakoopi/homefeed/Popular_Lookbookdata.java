package com.zakoopi.homefeed;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Popular_Lookbookdata {

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("tags")
	private String tags;

	@SerializedName("lookbooklike_count")
	private String lookbooklike_count;

	@SerializedName("hits_text")
	private String hits_text;

	@SerializedName("is_liked")
	private String is_liked;

	Popular_Lookbook_User user;
	ArrayList<Popular_Lookbook_Cards> cards;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLookbooklike_count() {
		return lookbooklike_count;
	}

	public void setLookbooklike_count(String lookbooklike_count) {
		this.lookbooklike_count = lookbooklike_count;
	}

	public String getView_count() {
		return hits_text;
	}

	public void setView_count(String view_count) {
		this.hits_text = view_count;
	}

	public String getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(String is_liked) {
		this.is_liked = is_liked;
	}

	public Popular_Lookbook_User getUser() {
		return user;
	}

	public void setUser(Popular_Lookbook_User user) {
		this.user = user;
	}

	public ArrayList<Popular_Lookbook_Cards> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Popular_Lookbook_Cards> cards) {
		this.cards = cards;
	}
}
