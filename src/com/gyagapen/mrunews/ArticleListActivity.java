package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ArticleListActivity extends Activity implements Runnable {

	private ArrayList<String> rssFeed;
	private String rssId;
	private ArrayList<ArticleHeader> articleList = null;
	// waiting dialog
	private ProgressDialog progressDialog;

	private Animation anim;

	private Handler mHandler = null;

	private ListView HeaderListView;
	
	String newsTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_list);

		// getting info from intent
		Intent myIntent = getIntent();
		rssFeed = myIntent.getStringArrayListExtra("rssFeed");
		rssId = myIntent.getStringExtra("rssCode");
		newsTitle = myIntent.getStringExtra("newsTitle"); 
		
		//set activity title
		setTitle(newsTitle);
		
		initUIComponent();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					populateArticleList(articleList);
				}
			}
		};

		// meanwhile retrieving list of articles
		Thread tRetrieveList = new Thread(this);

		tRetrieveList.start();

	}

	public void initUIComponent() {
		HeaderListView = (ListView) findViewById(R.id.listViewArticle);
	}

	public void populateArticleList(ArrayList<ArticleHeader> articleList) {
		ListArticleAdapter listArticleAdapter = new ListArticleAdapter(
				articleList, this, newsTitle);
		HeaderListView.setAdapter(listArticleAdapter);

		// scroll bar
		HeaderListView.setFastScrollEnabled(true);
		HeaderListView.setScrollbarFadingEnabled(false);
		HeaderListView.setScrollContainer(true);
		HeaderListView.setTextFilterEnabled(true);

	}

	@Override
	public void run() {
		RSSReader rssReader = new RSSReader();
		articleList = new ArrayList<ArticleHeader>();

		try {
			articleList = rssReader.readFeed(rssFeed, rssId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mHandler.sendEmptyMessage(0);
		progressDialog.dismiss();

		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.push_right_in);
		HeaderListView.setAnimation(anim);
		anim.start();
	

	}

	
	public void finish() {
	    super.finish();
	    //transition animation
	    overridePendingTransition(R.animator.push_right_in, R.animator.push_right_out);
	}

}
