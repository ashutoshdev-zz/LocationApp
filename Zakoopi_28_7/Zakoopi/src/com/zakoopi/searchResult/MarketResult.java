package com.zakoopi.searchResult;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MarketResult {

	@SerializedName("id")
	public String id;

	@SerializedName("city_id")
	public String city_id;

	@SerializedName("market_name")
	public String market_name;

	ArrayList<StoreDetail> stores;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getMarket_name() {
		return market_name;
	}

	public void setMarket_name(String market_name) {
		this.market_name = market_name;
	}

	public ArrayList<StoreDetail> getStoreDetails() {
		return stores;

	}

	public void setStoreDetails(ArrayList<StoreDetail> stores) {
		this.stores = stores;
	}

}
