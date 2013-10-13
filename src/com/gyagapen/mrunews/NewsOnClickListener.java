package com.gyagapen.mrunews;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class NewsOnClickListener implements OnClickListener {

	private News newspaper = null;

	public NewsOnClickListener(News aNewspaper) {
		newspaper = aNewspaper;
	}

	
	/**
	 * Trigerred when clicking on a article
	 */

	public void onClick(View v) {
		
		Intent intent = new Intent(v.getContext(), ArticleListActivity.class);
		intent.putExtra("rssFeed", newspaper.getNewsRssFeed());
		intent.putExtra("rssCode", newspaper.getNewsId());
		intent.putExtra("newsTitle", newspaper.newsName);
	    v.getContext().startActivity(intent);
	}

}
