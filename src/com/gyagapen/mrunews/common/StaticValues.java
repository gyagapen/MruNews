package com.gyagapen.mrunews.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.drawable;
import com.gyagapen.mrunews.entities.News;
import com.gyagapen.mrunews.entities.NewsSubEntry;

public class StaticValues {

	private static String LEXPRESS_NAME = "Lexpress";
	private static int LEXPRESS_IMG = R.drawable.lexpress_news;
	private static ArrayList<String> LEXPRESS_RSS = new ArrayList<String>() {
		{
			add("http://www.lexpress.mu/rss.xml");
		}
	};
	public static String LEXPRESS_CODE = "LEX";
	
	public static String LEMAURICIEN_CODE = "MAU";
	private static String LEMAURICIEN_NAME = "Le Mauricien ";
	private static int LEMAURICIEN_IMG = R.drawable.lemauricien_newspaper;
	private static ArrayList<NewsSubEntry> LEMAURICIEN_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.lemauricien.com/rss/articles/all"));
			add(new NewsSubEntry("> Economie","http://www.lemauricien.com/rss/articles/Economie"));
			add(new NewsSubEntry("> Politique","http://www.lemauricien.com/rss/articles/Politique"));
			add(new NewsSubEntry("> Faits Divers","http://www.lemauricien.com/rss/articles/Faits%20divers"));
			add(new NewsSubEntry("> Société","http://www.lemauricien.com/rss/articles/Soci%C3%A9t%C3%A9"));
			add(new NewsSubEntry("> Magazine","http://www.lemauricien.com/rss/articles/Magazine"));
			add(new NewsSubEntry("> Sports","http://www.lemauricien.com/rss/articles/Sports"));
			add(new NewsSubEntry("> International","http://www.lemauricien.com/rss/articles/International"));

		}
	};
		

	
	public static String DEFI_PLUS_CODE = "DFP";
	private static String DEFI_PLUS_NAME = "Le Défi Plus";
	private static int DEFI_PLUS_IMG = R.drawable.defi_plus;
	private static ArrayList<NewsSubEntry> DEFI_PLUS_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.defimedia.info/defi-plus.feed"));
			add(new NewsSubEntry("> Actualités","http://www.defimedia.info/defi-plus/dp-actualites.feed"));
			add(new NewsSubEntry("> Economie","http://www.defimedia.info/defi-plus/dp-economie.feed"));
			add(new NewsSubEntry("> Education","http://www.defimedia.info/defi-plus/dp-education.feed"));
			add(new NewsSubEntry("> Enquête","http://www.defimedia.info/defi-plus/dp-enquete.feed"));
			add(new NewsSubEntry("> Faits Divers","http://www.defimedia.info/defi-plus/dp-faits-divers.feed"));
			add(new NewsSubEntry("> Interview","http://www.defimedia.info/defi-plus/dp-interview.feed"));
			add(new NewsSubEntry("> Magazine","http://www.defimedia.info/defi-plus/dp-magazine.feed"));
			add(new NewsSubEntry("> Travail","http://www.defimedia.info/defi-plus/dp-monde-travail.feed"));
			add(new NewsSubEntry("> Société","http://www.defimedia.info/defi-plus/dp-societe.feed"));
			add(new NewsSubEntry("> Tribunal","http://www.defimedia.info/defi-plus/dp-tribunaux.feed"));

		}
	};
	
	private static String DEFI_QUOTIDIEN_NAME = "Le Défi Quotidien";
	private static int DEFI_QUOTIDIEN_IMG = R.drawable.defiquotidien;
	private static ArrayList<NewsSubEntry> DEFI_QUOTIDIEN_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.defimedia.info/defi-quotidien.feed"));
			add(new NewsSubEntry("> Actualités","http://www.defimedia.info/defi-quotidien/dq-actualites.feed"));
			add(new NewsSubEntry("> Economie","http://www.defimedia.info/defi-quotidien/dq-economie.feed"));
			add(new NewsSubEntry("> Défi Zen","http://www.defimedia.info/defi-quotidien/dq-defi-zen.feed"));
			add(new NewsSubEntry("> Faits Divers","http://www.defimedia.info/defi-quotidien/dq-faits-divers.feed"));
			add(new NewsSubEntry("> Interview","http://www.defimedia.info/defi-quotidien/dq-interview.feed"));
			add(new NewsSubEntry("> Magazine","http://www.defimedia.info/defi-quotidien/dq-magazine.feed"));
			add(new NewsSubEntry("> Société","http://www.defimedia.info/defi-quotidien/dq-societe.feed"));
			add(new NewsSubEntry("> Tribunal","http://www.defimedia.info/defi-quotidien/dq-tribunaux.feed"));
			add(new NewsSubEntry("> Tribune","http://www.defimedia.info/defi-quotidien/dq-tribune.feed"));
			add(new NewsSubEntry("> Xplik ou k","http://www.defimedia.info/defi-quotidien/dq-xplik-cas.feed"));

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
		
		
		News leMatinalNews = new News(LE_MATINAL_NAME, lE_MATINAL_CODE,
				LE_MATINAL_RSS,LE_MATINAL_IMG);
		newsList.add(leMatinalNews);	
		
		News leMauricienNews = new News(LEMAURICIEN_NAME, LEMAURICIEN_CODE, LEMAURICIEN_IMG, LEMAURICIEN_SUBMENU);
		newsList.add(leMauricienNews);
		
		News defiQuotidienNews = new News(DEFI_QUOTIDIEN_NAME, DEFI_PLUS_CODE,
				 DEFI_QUOTIDIEN_IMG, DEFI_QUOTIDIEN_SUBMENU);
		newsList.add(defiQuotidienNews);
		
		News defiPlusNews = new News(DEFI_PLUS_NAME, DEFI_PLUS_CODE,
				 DEFI_PLUS_IMG, DEFI_PLUS_SUBMENU);
		newsList.add(defiPlusNews);
		



		return newsList;
	}

}
