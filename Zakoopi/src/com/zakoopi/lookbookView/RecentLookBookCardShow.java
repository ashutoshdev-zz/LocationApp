package com.zakoopi.lookbookView;

import com.google.gson.annotations.SerializedName;

public class RecentLookBookCardShow {

	@SerializedName("id")
	private String id;
	
	@SerializedName("lookbook_id")
	private String lookbook_id;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("tags")
	private String tags;
	
	@SerializedName("android_api_img")
	private String android_api_img;
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getLookbookId(){
		return lookbook_id;
	}
	
	public void setLookbookId(String lookbook_id){
		this.lookbook_id = lookbook_id;
	}
	
	public String getdescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getTags(){
		return tags;
	}
	
	public void setTags(String tags){
		this.tags = tags;
	}
	
	public String getPic(){
		return android_api_img;
	}
	
	public void setPic(String android_api_img){
		this.android_api_img = android_api_img;
	}
}
