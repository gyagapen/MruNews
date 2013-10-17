package com.gyagapen.mrunews;

import java.util.ArrayList;
import java.util.HashMap;

public class StaticValues {

	private static String LEXPRESS_NAME = "Lexpress";
	private static int LEXPRESS_IMG = R.drawable.lexpress_news;
	private static ArrayList<String> LEXPRESS_RSS = new ArrayList<String>() {
		{
			add("http://www.lexpress.mu/rss.xml");
		}
	};
	static String LEXPRESS_CODE = "LEX";
	
	static String LEMAURICIEN_CODE = "MAU";
	private static String LEMAURICIEN_NAME = "Le Mauricien ";
	private static int LEMAURICIEN_IMG = R.drawable.lemauricien_newspaper;
	private static ArrayList<NewsSubEntry> LEMAURICIEN_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Economie","http://www.lemauricien.com/rss/articles/Economie"));
			add(new NewsSubEntry("> Politique","http://www.lemauricien.com/rss/articles/Politique"));
			add(new NewsSubEntry("> Faits Divers","http://www.lemauricien.com/rss/articles/Faits%20divers"));
			add(new NewsSubEntry("> Société","http://www.lemauricien.com/rss/articles/Soci%C3%A9t%C3%A9"));
			add(new NewsSubEntry("> Magazine","http://www.lemauricien.com/rss/articles/Magazine"));
			add(new NewsSubEntry("> Sports","http://www.lemauricien.com/rss/articles/Sports"));
			add(new NewsSubEntry("> Sports","http://www.lemauricien.com/rss/articles/International"));

		}
	};
		

	
	public static String DEFI_PLUS_CODE = "DFP";
	private static String DEFI_PLUS_NAME = "Le Défi Groupe";
	private static int DEFI_PLUS_IMG = R.drawable.defiplus;
	private static ArrayList<String> DEFI_PLUS_RSS = new ArrayList<String>() {
		{
			add("http://www.defimedia.info/defi-plus.feed");

		}
	};
	
	public static String lE_MATINAL_CODE = "MAT";
	private static String LE_MATINAL_NAME = "Le Matinal";
	private static int LE_MATINAL_IMG = R.drawable.lematinal;
	private static ArrayList<String> LE_MATINAL_RSS = new ArrayList<String>() {
		{
			add("http://www.lematinal.com/feed/index.1.rss");

		}
	};


	

	static public ArrayList<News> getNewsPapers() {
		ArrayList<News> newsList = new ArrayList<News>();

		News lexpressNews = new News(LEXPRESS_NAME, LEXPRESS_CODE, LEXPRESS_RSS, LEXPRESS_IMG);
		newsList.add(lexpressNews);
		
		
		News defiPlusNews = new News(DEFI_PLUS_NAME, DEFI_PLUS_CODE,
				DEFI_PLUS_RSS, DEFI_PLUS_IMG);
		newsList.add(defiPlusNews);
		
		News leMatinalNews = new News(LE_MATINAL_NAME, lE_MATINAL_CODE,
				LE_MATINAL_RSS,LE_MATINAL_IMG);
		newsList.add(leMatinalNews);	
		
		News leMauricienNews = new News(LEMAURICIEN_NAME, LEMAURICIEN_CODE, LEMAURICIEN_IMG, LEMAURICIEN_SUBMENU);
		newsList.add(leMauricienNews);
		



		return newsList;
	}

}
