package com.zakoopi.search;

import com.google.gson.annotations.SerializedName;

public class marketsSearch implements Comparable<marketsSearch>{

	@SerializedName("id")
	private String id;
	
	@SerializedName("market_name")
	private String market_name;
	
	/*@SerializedName("category")
	private String category;*/
	
	public String get_id() {
		return id;
	}
	
	public void set_id(String _id) {
		this.id = _id;
	}
	
	public String get_market_name(){
		return market_name;
	}
	
	public void set_name(String _market_name){
		this.market_name = _market_name;
	}
	
	/*public String get_category() {
		return category;
	}
	
	public void set_category(String _category) {
		this.category = _category;
	}*/
	
	@Override
	public int compareTo(marketsSearch offer) {
		// TODO Auto-generated method stub
		return offer.get_id().compareTo(id);
	}

}
