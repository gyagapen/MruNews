package com.gyagapen.mrunews.parser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gyagapen.mrunews.common.LogsProvider;
import com.gyagapen.mrunews.common.StaticValues;
import com.gyagapen.mrunews.entities.ArticleComment;
import com.gyagapen.mrunews.entities.ArticleContent;

import android.os.StrictMode;

public class HTMLPageParser {

	private String linkToParse;
	private String parseType;

	static final String PING_HOST_1 = "http://www.google.com/";
	static final String PING_HOST_2 = "http://fr.yahoo.com/";

	private LogsProvider logsProvider = null;

	public HTMLPageParser(String linkToParse, String parseType) {
		super();
		this.linkToParse = linkToParse;
		this.parseType = parseType;
		logsProvider = new LogsProvider(null, this.getClass());
	}

	public String getLinkToParse() {
		return linkToParse;
	}

	public void setLinkToParse(String linkToParse) {
		this.linkToParse = linkToParse;
	}

	public String getParseType() {
		return parseType;
	}

	public void setParseType(String parseType) {
		this.parseType = parseType;
	}

	public ArticleContent parsePage() throws Exception {
		ArticleContent artContent = new ArticleContent();

		if (parseType.equals(StaticValues.LEXPRESS_CODE)) {
			artContent = parseExpressPage();
		} else if (parseType.equals(StaticValues.LEMAURICIEN_CODE)) {
			artContent = parseLeMauricienPage();

		} else if (parseType.equals(StaticValues.DEFI_PLUS_CODE)) {
			artContent = parseDefiPlusPage();
		} else if (parseType.equals(StaticValues.lE_MATINAL_CODE)) {
			artContent = parseLeMatinalPage();
		}

		return artContent;
	}

	private ArticleContent parseLeMauricienPage() throws Exception {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		artContent.setImageLink(getImageFromLink(linkToParse,
				StaticValues.LEMAURICIEN_CODE, doc));

		// get Content
		Elements ContentElements = doc.select("div.body-content p");
		String content = "";
		for (int i = 0; i < ContentElements.size(); i++) {
			content += System.getProperty("line.separator")
					+ ContentElements.get(i).html();
		}

		// add breaklines
		Pattern p = Pattern.compile("<br \\/>");
		Matcher matcher = p.matcher(content);
		content = matcher.replaceAll("#SPACE#");

		content = Jsoup.parse(content).text();

		Pattern paragraph = Pattern.compile("#SPACE#");
		Matcher spaceMatcher = paragraph.matcher(content);
		content = spaceMatcher.replaceAll(System.getProperty("line.separator")
				+ System.getProperty("line.separator"));

		artContent.setContent(content);

		// get Title
		Element TitleElement = doc.select("h1").first();
		String title = TitleElement.text();
		artContent.setTitle(title);

		return artContent;
	}

	private ArticleContent parseExpressPage() throws Exception {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		artContent.setImageLink(getImageFromLink(linkToParse,
				StaticValues.LEXPRESS_CODE, doc));

		// get Content
		Elements ContentElements = doc.select(".field-name-body");
		String content = "";
		for (int i = 0; i < ContentElements.size(); i++) {

			content += System.getProperty("line.separator")
					+ ContentElements.get(i).text();
		}

		ContentElements = doc.select(".field-name-body div.field-item div");
		for (int i = 0; i < ContentElements.size(); i++) {

			content += System.getProperty("line.separator")
					+ ContentElements.get(i).text();
		}
		

		artContent.setContent(content);

		// get Title
		Element TitleElement = doc.select("h1#page-title").first();
		String title = TitleElement.text();
		artContent.setTitle(title);

		// count number of comments
		int commentCount = doc.select("div#comments h3 a").size();

		// retrieve each comment
		for (int j = 0; j < commentCount; j++) {

			ArticleComment artComment = new ArticleComment();

			// comments - title
			Element ComTitleElement = doc.select("div#comments h3 a").get(j);
			String titleComment = ComTitleElement.text();
			artComment.setTitle(titleComment);

			// comments - Date & Author
			Element ComAuthDateElement = doc.select("div#comments footer").get(
					j);
			String dateAuthComment = ComAuthDateElement.text();
			String authorComment = dateAuthComment;
			artComment.setAuthor(authorComment);

			// comment content
			Element commentContentElement = doc.select(
					"div#comments div.field-items p").get(j);
			String commentElement = commentContentElement.text();
			artComment.setContent(commentElement);

			// add to list of comment
			artContent.addComment(artComment);
		}

		return artContent;
	}

	private ArticleContent parseDefiPlusPage() throws Exception {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		artContent.setImageLink(getImageFromLink(linkToParse,
				StaticValues.DEFI_PLUS_CODE, doc));

		// get Content
		Element IntroElement = doc.select("div.itemIntroText").first();
		String intro = IntroElement.text();

		Elements ContentElements = doc.select("div.itemFullText");
		String content = intro;
		
		/*for(int i=0;i<ContentElements.size();i++)
		{
			content = "#SPACE#" + ContentElements.get(i).html();
		}*/
		
		content += "#SPACE#" + ContentElements.html();

		// add breaklines
		Pattern p = Pattern.compile("<br />");
		Matcher matcher = p.matcher(content);
		content = matcher.replaceAll("#SPACE#");
		
		Pattern parah = Pattern.compile("<p>");
		Matcher matcherParah = parah.matcher(content);
		content = matcherParah.replaceAll("#SPACE#");

		content = Jsoup.parse(content).text();

		Pattern paragraph = Pattern.compile("#SPACE#");
		Matcher spaceMatcher = paragraph.matcher(content);
		content = spaceMatcher.replaceAll(System.getProperty("line.separator")
				+ System.getProperty("line.separator"));
		

		artContent.setContent(content);

		// get Title

		Element TitleElement = doc.select("h1.itemTitle").first();
		String title = TitleElement.text();
		artContent.setTitle(title);

		// count number of comments
		int commentCount = doc.select("div.post-body header span").size();

		return artContent;
	}

	private ArticleContent parseLeMatinalPage() throws Exception {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		artContent.setImageLink(getImageFromLink(linkToParse,
				StaticValues.lE_MATINAL_CODE, doc));

		// get Content

		Element ContentElements = doc.select("div#article_body").first();
		String content = "";
		content = ContentElements.html();

		// add breaklines
		Pattern p = Pattern.compile("<br \\/>");
		Matcher matcher = p.matcher(content);
		content = matcher.replaceAll("#SPACE#");

		content = Jsoup.parse(content).text();

		Pattern paragraph = Pattern.compile("#SPACE#");
		Matcher spaceMatcher = paragraph.matcher(content);
		content = spaceMatcher.replaceAll(System.getProperty("line.separator")
				+ System.getProperty("line.separator"));

		artContent.setContent(content);

		// get Title

		Element TitleElement = doc.select("div#article_holder h1").first();
		String title = TitleElement.text();
		artContent.setTitle(title);

		// count number of comments
		int commentCount = doc.select("div.post-body header span").size();

		return artContent;
	}

	public static String getImageFromLink(String url, String newsCode,
			Document doc) throws Exception {
		String imageLink = "";

		try {

			// get page content if not given
			if (doc == null) {
				doc = Jsoup.connect(url).timeout(10 * 2500).get();
			}

			if (newsCode.equals(StaticValues.LEMAURICIEN_CODE)) {

				// get image
				Element imgElement = doc.select("div.main-image img").first();
				imageLink = imgElement.attr("src").toString();
			} else if (newsCode.equals(StaticValues.LEXPRESS_CODE)) {
				// get image
				Element imgElement = doc.select("img[src]").first();
				imageLink = imgElement.attr("src").toString();

			} else if (newsCode.equals(StaticValues.DEFI_PLUS_CODE)) {
				// get image
				Element imgElement = doc.select("span.itemImage img[src]")
						.first();
				imageLink = "http://www.defimedia.info"
						+ imgElement.attr("src").toString();
			} else if (newsCode.equals(StaticValues.lE_MATINAL_CODE)) {
				// get image
				Element imgElement = doc.select("div.image img[src]").first();
				imageLink = imgElement.attr("src").toString();

			}
		} catch (Exception ex) {
			imageLink = "";
		}

		return imageLink;
	}

	static public boolean isInternetConnectionAvailable(
			LogsProvider logsProvider) {
		boolean isInternetAvailaible = false;

		// avoid androidblockguard policy error
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		URL url;
		try {
			url = new URL(PING_HOST_1);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 3 seconds time out
			conn.setConnectTimeout(3000);

			int responseCode = conn.getResponseCode();

			isInternetAvailaible = (responseCode == 200);

		} catch (MalformedURLException e) {
			logsProvider.info("net check err : " + e.getMessage());
			isInternetAvailaible = false;
		} catch (IOException e) {
			logsProvider.info("net check err : " + e.getMessage());
			isInternetAvailaible = false;
		}

		logsProvider.info("Internet connection check: " + isInternetAvailaible);

		return isInternetAvailaible;
	}

}
