package com.gyagapen.mrunews;

import java.util.ArrayList;

public class News {

	String newsName;
	String newsId;
	ArrayList<String> newsRssFeeds;
	int imageRessource;

	public News(String newsName, String newsId, ArrayList<String> newsRssFeed, int imgRes) {
		super();
		this.newsName = newsName;
		this.newsId = newsId;
		this.newsRssFeeds = newsRssFeed;
		imageRessource = imgRes;
	}

	public String getNewsName() {
		return newsName;
	}

	public void setNewsName(String newsName) {
		this.newsName = newsName;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public ArrayList<String> getNewsRssFeed() {
		return newsRssFeeds;
	}

	public void setNewsRssFeed(ArrayList<String> newsRssFeed) {
		this.newsRssFeeds = newsRssFeed;
	}

	public ArrayList<String> getNewsRssFeeds() {
		return newsRssFeeds;
	}

	public void setNewsRssFeeds(ArrayList<String> newsRssFeeds) {
		this.newsRssFeeds = newsRssFeeds;
	}

	public int getImageRessource() {
		return imageRessource;
	}

	public void setImageRessource(int imageRessource) {
		this.imageRessource = imageRessource;
	}

}
