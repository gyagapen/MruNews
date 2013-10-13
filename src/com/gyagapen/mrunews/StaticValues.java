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

	public static String LEMAURICIEN_ECONOMIE_CODE = LEMAURICIEN_CODE+"ECO";
	private static String LEMAURICIEN_ECONOMIE_NAME = "Le Mauricien - Economie ";
	private static int LEMAURICIEN_ECONOMIE_IMG = R.drawable.lemauricien_newspaper_eco;
	private static ArrayList<String> LEMAURICIEN_ECONOMIE_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/Economie");

		}
	};

	public static String LEMAURICIEN_POLITIQUE_CODE = LEMAURICIEN_CODE+"POL";
	private static String LEMAURICIEN_POLITIQUE_NAME = "Le Mauricien - Politique";
	private static int LEMAURICIEN_POLITIQUE_IMG = R.drawable.lemauricien_newspaper_politique;
	private static ArrayList<String> LEMAURICIEN_POLITIQUE_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/Politique");

		}
	};

	public static String LEMAURICIEN_FAITS_CODE = LEMAURICIEN_CODE+"FAITS";
	private static String LEMAURICIEN_FAITS_NAME = "Le Mauricien - Faits";
	private static int LEMAURICIEN_FAITS_IMG = R.drawable.lemauricien_newspaper_faits;
	private static ArrayList<String> LEMAURICIEN_FAITS_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/Faits%20divers");

		}
	};

	public static String LEMAURICIEN_SOCIETE_CODE = LEMAURICIEN_CODE+"STE";
	private static String LEMAURICIEN_SOCIETE_NAME = "Le Mauricien - Societe";
	private static int LEMAURICIEN_SOCIETE_IMG = R.drawable.lemauricien_newspaper_societe;
	private static ArrayList<String> LEMAURICIEN_SOCIETE_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/Soci%C3%A9t%C3%A9");

		}
	};
	
	
	public static String LEMAURICIEN_MAGAZINE_CODE = LEMAURICIEN_CODE+"MAG";
	private static String LEMAURICIEN_MAGAZINE_NAME = "Le Mauricien - Magazine";
	private static int LEMAURICIEN_MAGAZINE_IMG = R.drawable.lemauricien_newspaper_magazine;
	private static ArrayList<String> LEMAURICIEN_MAGAZINE_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/Magazine");

		}
	};
	
	
	public static String LEMAURICIEN_SPORTS_CODE = LEMAURICIEN_CODE+"SP";
	private static String LEMAURICIEN_SPORTS_NAME = "Le Mauricien - Sports";
	private static int LEMAURICIEN_SPORTS_IMG = R.drawable.lemauricien_newspaper_sport;
	private static ArrayList<String> LEMAURICIEN_SPORTS_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/Sports");

		}
	};
	
	public static String LEMAURICIEN_INTERNATIONAL_CODE = LEMAURICIEN_CODE+"INT";
	private static String LEMAURICIEN_INTERNATIONAL_NAME = "Le Mauricien - International";
	private static int LEMAURICIEN_INTERNATIONAL_IMG = R.drawable.lemauricien_newspaper_international;
	private static ArrayList<String> LEMAURICIEN_INTERNATIONAL_RSS = new ArrayList<String>() {
		{
			add("http://www.lemauricien.com/rss/articles/International");

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


	

	static public ArrayList<News> getNewsPapers() {
		ArrayList<News> newsList = new ArrayList<News>();

		News lexpressNews = new News(LEXPRESS_NAME, LEXPRESS_CODE, LEXPRESS_RSS, LEXPRESS_IMG);
		newsList.add(lexpressNews);
		
		
		News defiPlusNews = new News(DEFI_PLUS_NAME, DEFI_PLUS_CODE,
				DEFI_PLUS_RSS, DEFI_PLUS_IMG);
		newsList.add(defiPlusNews);
		
		News leMauricienNewsEco = new News(LEMAURICIEN_ECONOMIE_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_ECONOMIE_RSS,LEMAURICIEN_ECONOMIE_IMG);
		newsList.add(leMauricienNewsEco);		
		
		News leMauricienNewsFaits = new News(LEMAURICIEN_FAITS_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_FAITS_RSS, LEMAURICIEN_FAITS_IMG);
		newsList.add(leMauricienNewsFaits);
		
		News leMauricienNewsSociete = new News(LEMAURICIEN_SOCIETE_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_SOCIETE_RSS, LEMAURICIEN_SOCIETE_IMG);
		newsList.add(leMauricienNewsSociete);
		
		News leMauricienNewsPo = new News(LEMAURICIEN_POLITIQUE_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_POLITIQUE_RSS, LEMAURICIEN_POLITIQUE_IMG);
		newsList.add(leMauricienNewsPo);
		
		News leMauricienNewsInt = new News(LEMAURICIEN_INTERNATIONAL_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_INTERNATIONAL_RSS, LEMAURICIEN_INTERNATIONAL_IMG);
		newsList.add(leMauricienNewsInt);
		
		News leMauricienNewsSports = new News(LEMAURICIEN_SPORTS_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_SPORTS_RSS, LEMAURICIEN_SPORTS_IMG);
		newsList.add(leMauricienNewsSports);
		
		News leMauricienNewsMag = new News(LEMAURICIEN_MAGAZINE_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_MAGAZINE_RSS, LEMAURICIEN_MAGAZINE_IMG);
		newsList.add(leMauricienNewsMag);
		


		return newsList;
	}

}
