package com.gyagapen.mrunews.entities;

public class ArticleComment {
	
	private String Title;
	private String Author;
	private String  commentDate;
	private String Content;
	
	
	public ArticleComment() {
		Title = "";
		Author = "";
		commentDate = "";
		Content = "";
	}


	public String getTitle() {
		return Title;
	}


	public void setTitle(String title) {
		Title = title;
	}


	public String getAuthor() {
		return Author;
	}


	public void setAuthor(String author) {
		Author = author;
	}


	public String getDate() {
		return commentDate;
	}


	public void setDate(String date) {
		commentDate = date;
	}


	public String getContent() {
		return Content;
	}


	public void setContent(String content) {
		Content = content;
	}
	
	

}
