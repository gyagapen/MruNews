package com.gyagapen.mrunews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_web_content);

		webView = (WebView) findViewById(R.id.WebViewArticle);
		webView.getSettings().setJavaScriptEnabled(true);

		// getting link to display
		Intent myIntent = getIntent();
		String link = myIntent.getStringExtra("link");

		webView.loadUrl(link);
	}
}
