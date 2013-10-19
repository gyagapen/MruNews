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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@SuppressLint("NewApi")
public class ArticleListActivity extends Activity implements Runnable {

	private ArrayList<String> rssFeed;
	private String rssId;
	private ArrayList<ArticleHeader> articleList = null;
	private ListArticleAdapter listArticleAdapter;
	private boolean isRefreshed = false;
	// waiting dialog
	private ProgressDialog progressDialog;

	private Animation anim;

	private Handler mHandler = null;

	private PullToRefreshListView HeaderListView;

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

		// set activity title
		setTitle(newsTitle);

		initUIComponent();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		loadArticles();

	}

	public void initUIComponent() {
		HeaderListView = (PullToRefreshListView) findViewById(R.id.listViewArticle);

		// set behavior on pull to refresh action
		HeaderListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				isRefreshed = true;
				loadArticles();
			}
		});
	}

	public void populateArticleList(ArrayList<ArticleHeader> articleList) {
		listArticleAdapter = new ListArticleAdapter(articleList, this,
				newsTitle);
		HeaderListView.getRefreshableView().setAdapter(listArticleAdapter);

		// scroll bar
		HeaderListView.getRefreshableView().setFastScrollEnabled(true);
		HeaderListView.getRefreshableView().setScrollbarFadingEnabled(false);
		HeaderListView.getRefreshableView().setScrollContainer(true);
		HeaderListView.getRefreshableView().setTextFilterEnabled(true);

	}

	@Override
	public void run() {
		RSSReader rssReader = new RSSReader();
		articleList = new ArrayList<ArticleHeader>();
		boolean useCache = true;

		try {
			
			//force network response
			if(isRefreshed)
			{
				useCache = false;
			}
			
			articleList = rssReader.readFeed(rssFeed, rssId, useCache);
			isRefreshed = false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mHandler.sendEmptyMessage(0);
		progressDialog.dismiss();

		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.push_right_in);
		HeaderListView.getRefreshableView().setAnimation(anim);
		anim.start();


	}

	public void finish() {
		super.finish();

		// stop asynctask
		if (listArticleAdapter != null) {
			listArticleAdapter.stopAllASyncTask();
		}

		// transition animation
		overridePendingTransition(R.animator.push_right_in,
				R.animator.push_right_out);

	}

	private void loadArticles() {

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					populateArticleList(articleList);
					HeaderListView.onRefreshComplete();
				}
			}
		};

		// meanwhile retrieving list of articles
		Thread tRetrieveList = new Thread(this);

		tRetrieveList.start();
	}

}
