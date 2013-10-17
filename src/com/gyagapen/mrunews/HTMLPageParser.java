package com.gyagapen.mrunews;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class HTMLPageParser {

	private String linkToParse;
	private String parseType;

	public HTMLPageParser(String linkToParse, String parseType) {
		super();
		this.linkToParse = linkToParse;
		this.parseType = parseType;
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

	public ArticleContent parsePage() {
		ArticleContent artContent = new ArticleContent();

		try {

			if (parseType.equals(StaticValues.LEXPRESS_CODE)) {
				artContent = parseExpressPage();
			} else if (parseType.equals(StaticValues.LEMAURICIEN_CODE)) {
				artContent = parseLeMauricienPage();

			} else if (parseType.equals(StaticValues.DEFI_PLUS_CODE)) {
				artContent = parseDefiPlusPage();
			} else if (parseType.equals(StaticValues.lE_MATINAL_CODE)) {
				artContent = parseLeMatinalPage();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return artContent;
	}

	private ArticleContent parseLeMauricienPage() throws IOException {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		Element imgElement = doc.select("div.main-image img").first();
		String imgLiString = imgElement.attr("src").toString();
		artContent.setImageLink(imgLiString);

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

		Log.i("IMAGE", "link " + imgLiString);
		Log.i("CONTENT", content);
		Log.i("TITLE", title);

		return artContent;
	}

	private ArticleContent parseExpressPage() throws IOException {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		Element imgElement = doc.select("img[src]").first();
		String imgLiString = imgElement.attr("src").toString();
		artContent.setImageLink(imgLiString);

		// get Content
		Elements ContentElements = doc.select(".field-name-body p");
		String content = "";
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

		Log.i("IMAGE", "link " + imgLiString);
		Log.i("CONTENT", content);
		Log.i("TITLE", title);

		return artContent;
	}

	private ArticleContent parseDefiPlusPage() throws IOException {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		artContent.setImageLink(getImageFromLink(linkToParse,
				StaticValues.DEFI_PLUS_CODE));

		// get Content
		Element IntroElement = doc.select("div.itemIntroText").first();
		String intro = IntroElement.text();

		Element ContentElements = doc.select("div.itemFullText").first();
		String content = "";
		content = intro + "#SPACE#" + ContentElements.html();

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

		Element TitleElement = doc.select("h2.itemTitle").first();
		String title = TitleElement.text();
		artContent.setTitle(title);

		// count number of comments
		int commentCount = doc.select("div.post-body header span").size();

		return artContent;
	}

	private ArticleContent parseLeMatinalPage() throws IOException {

		ArticleContent artContent = new ArticleContent();

		// get page content
		Document doc = Jsoup.connect(linkToParse).timeout(10 * 2500).get();

		// get image
		artContent.setImageLink(getImageFromLink(linkToParse,
				StaticValues.lE_MATINAL_CODE));

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

	public static String getImageFromLink(String url, String newsCode)
			throws IOException {
		String imageLink = "";

		try {

			if (newsCode.equals(StaticValues.LEMAURICIEN_CODE)) {
				// get page content
				Document doc = Jsoup.connect(url).timeout(10 * 2500).get();

				// get image
				Element imgElement = doc.select("div.main-image img").first();
				imageLink = imgElement.attr("src").toString();
			} else if (newsCode.equals(StaticValues.LEXPRESS_CODE)) {
				// get page content
				Document doc = Jsoup.connect(url).timeout(10 * 2500).get();

				// get image
				Element imgElement = doc.select("img[src]").first();
				imageLink = imgElement.attr("src").toString();

			} else if (newsCode.equals(StaticValues.DEFI_PLUS_CODE)) {
				// get page content
				Document doc = Jsoup.connect(url).timeout(10 * 2500).get();

				// get image
				Element imgElement = doc.select("span.itemImage img[src]")
						.first();
				imageLink = "http://www.defimedia.info"
						+ imgElement.attr("src").toString();
			} else if (newsCode.equals(StaticValues.lE_MATINAL_CODE)) {
				// get page content
				Document doc = Jsoup.connect(url).timeout(10 * 2500).get();

				// get image
				Element imgElement = doc.select("div.image img[src]").first();
				imageLink = imgElement.attr("src").toString();

			}
		} catch (Exception ex) {
			imageLink = "";
		}

		return imageLink;
	}

}
