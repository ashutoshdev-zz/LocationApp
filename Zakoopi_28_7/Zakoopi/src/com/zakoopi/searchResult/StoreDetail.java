package com.zakoopi.searchResult;

import com.google.gson.annotations.SerializedName;

public class StoreDetail {

	@SerializedName("id")
	private String id;
	
	@SerializedName("store_name")
	private String store_name;
	
	@SerializedName("market")
	private String market;
	
	@SerializedName("area")
	private String area;
	
	@SerializedName("store_review_count")
	private String store_review_count;
	
	@SerializedName("overall_ratings")
	private String overall_ratings;
	
	@SerializedName("images_count")
	private String images_count;
	
	@SerializedName("lookbooks_count")
	private String lookbooks_count;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getStoreName(){
		return store_name;
	}
	
	public void setStoreName(String store_name){
		this.store_name = store_name;
	}
	
	public String getMarket(){
		return market;
	}
	
	public void setMarket(String market){
		this.market = market;
	}
	
	public String getArea(){
		return area;
	}
	
	public void setArea(String area){
		this.area = area;
	}
	
	public String getReviewCount(){
		return store_review_count;
	}
	
	public void setReviewCount(String store_review_count){
		this.store_review_count = store_review_count;
	}
	
	public String getRating(){
		return overall_ratings;
	}
	
	public void setRating(String overall_ratings){
		this.overall_ratings = overall_ratings;
	}
	
	public String getImageCount(){
		return images_count;
	}
	
	public void setImageCount(String images_count){
		this.images_count = images_count;
	}
	
	public String getLookbookCount(){
		return lookbooks_count;
	}
	
	public void setLookbookCount(String lookbooks_count){
		this.lookbooks_count = lookbooks_count;
	}
}
