package com.zakoopi.article.model;

import java.util.ArrayList;

public class Article {

	ArrayList<ArticleImage> article_images = new ArrayList<ArticleImage>();

	public ArrayList<ArticleImage> getArticle() {
		return article_images;
	}

	public void setArticle(ArrayList<ArticleImage> article) {
		this.article_images = article;
	}
	
	
}
