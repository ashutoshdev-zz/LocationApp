package com.zakoopi.search;

import android.util.Log;

public class AllProduct {

	int _id;
	String _productName;
	String _productCategory;
	
	public AllProduct() {
		
	}
	
	public AllProduct(int id, String productName, String productCategory) {
		this._id = id;
		this._productName = productName;
		this._productCategory = productCategory;
		Log.e("all1", "all1");
	}
	
	public AllProduct(String productName, String productCategory){
		this._productName = productName;
		this._productCategory = productCategory;
		Log.e("all2", "all2");
	}
	
	public int getID(){
		return this._id;
	}
	
	public void setID(int id){
		this._id = id;
	}
	
	public String getProductName(){
		return this._productName;
	}
	
	public void setProductName(String productName){
		this._productName = productName;
	}
	
	public String getProductCategory(){
		return this._productCategory;
	}
	
	public void setProductCategory(String productCategory){
		this._productCategory = productCategory;
	}
	
	
}
