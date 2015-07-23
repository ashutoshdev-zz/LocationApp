package com.zakoopi.lookbookView;

import java.util.ArrayList;

import com.zakoopi.homefeed.Recent_Lookbook_Cards;

public class LookbookRecent {

	ArrayList<RecentLookBookCardShow> cards;
	
	public ArrayList<RecentLookBookCardShow> getRecentCards(){
		return cards;
	}
	
	public void setRecentCards(ArrayList<RecentLookBookCardShow> cards){
		this.cards = cards;
	}
}
