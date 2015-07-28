package com.store.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class RelatedlookbookArrays {

	@SerializedName("id")
	private String id;
	@SerializedName("title")
	private String title;
	@SerializedName("lookbookcomment_count")
	private String lookbookcomment_count;
	@SerializedName("lookbooklike_count")
	private String lookbooklike_count;
	@SerializedName("view_count")
	private String view_count;
	LookbookUser user;
	ArrayList<LookbookCards> cards;

	public ArrayList<LookbookCards> getCards() {
		return cards;
	}

	public void setCards(ArrayList<LookbookCards> cards) {
		this.cards = cards;
	}

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

	public String getLookbookcomment_count() {
		return lookbookcomment_count;
	}

	public void setLookbookcomment_count(String lookbookcomment_count) {
		this.lookbookcomment_count = lookbookcomment_count;
	}

	public String getLookbooklike_count() {
		return lookbooklike_count;
	}

	public void setLookbooklike_count(String lookbooklike_count) {
		this.lookbooklike_count = lookbooklike_count;
	}

	public String getView_count() {
		return view_count;
	}

	public void setView_count(String view_count) {
		this.view_count = view_count;
	}

	public LookbookUser getUser() {
		return user;
	}

	public void setUser(LookbookUser user) {
		this.user = user;
	}

	
}
