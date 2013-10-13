package com.gyagapen.mrunews;

import java.util.ArrayList;

import com.androidquery.AQuery;

public class ArticleContent {
	
	
	private String Title;
	private String Redactor;
	private String Content;
	private String ImageLink;
	private String NewsName;
	private ArrayList<ArticleComment> comments;

	
	
	public ArrayList<ArticleComment> getComment() {
		return comments;
	}

	public void setComment(ArrayList<ArticleComment> comment) {
		this.comments = comment;
	}

	public String getNewsName() {
		return NewsName;
	}

	public void setNewsName(String newsName) {
		NewsName = newsName;
	}

	public ArticleContent() {
		comments = new ArrayList<ArticleComment>();
	}
	
	public ArticleContent(String title, String redactor, String content,
			String imageLink) {
		Title = title;
		Redactor = redactor;
		Content = content;
		ImageLink = imageLink;
		comments = new ArrayList<ArticleComment>();
	}
	
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getRedactor() {
		return Redactor;
	}
	public void setRedactor(String redactor) {
		Redactor = redactor;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		
		//replace breakline by line separators
		content.replace("#BREAKLINE#", System.getProperty ("line.separator"));
		
		Content = content;
	}
	public String getImageLink() {
		return ImageLink;
	}
	public void setImageLink(String imageLink) {
		ImageLink = imageLink;
	}
	
	public void addComment(ArticleComment comment)
	{
		comments.add(comment);
	}
	
	

}
