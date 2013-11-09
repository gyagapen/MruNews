package com.gyagapen.mrunews;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.ads.AdView;
import com.gyagapen.mrunews.adapters.MainNewsAdapter;
import com.gyagapen.mrunews.common.LogsProvider;
import com.gyagapen.mrunews.common.MenuHelper;
import com.gyagapen.mrunews.common.StaticValues;
import com.gyagapen.mrunews.parser.HTMLPageParser;

public class MainNewsActivity extends Activity implements Runnable {

	private ListView mainArticleListView;
	private LogsProvider logsProvider = null;
	private MenuHelper menuHelper = null;
	private AdView adView;
	
	// waiting dialog
	private ProgressDialog progressDialog;

	private Handler mHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//init menu helper
		menuHelper = new MenuHelper(this);
		
		setContentView(R.layout.news_main);
		
		//populate ads
		//LinearLayout linLayout = (LinearLayout)findViewById(R.id.main_linearlayout);
		//menuHelper.generateAdsBanner(adView, linLayout, this);;
		
		logsProvider = new LogsProvider(null, this.getClass());

		// create cache
		createCache();


		mainArticleListView = (ListView) findViewById(R.id.ArticleListViewMain);

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Checking Internet Connection...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quit",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainNewsActivity.this.finish();
					}
				});

		progressDialog.setOnCancelListener(new OnCancelListener() {

			// when progress dialog is cancelled
			public void onCancel(DialogInterface dialog) {
				// close activity
				MainNewsActivity.this.finish();
			}
		});

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {
					populateGridView();
				} else if (msg.what == 1) {
					displayErrorMessage("No Internet Connection");
				}
			}
		};

		// meanwhile retrieving article content
		Thread tRetrieveContent = new Thread(this);

		tRetrieveContent.start();

	}

	/**
	 * Create cache for http requests
	 */
	public void createCache() {

		try {
			File httpCacheDir = null;
			// try to install cache on external memory
			if (getExternalCacheDir() != null) {
				httpCacheDir = new File(getExternalCacheDir(), "http");
			} else // use internal cache
			{
				httpCacheDir = new File(getCacheDir(), "http");
			}
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB

			HttpResponseCache.install(httpCacheDir, httpCacheSize);
		} catch (IOException e) {
			Log.i("MruNews", "HTTP response cache installation failed:" + e);
		}
	}

	@Override
	protected void onStop() {

		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			try {
				cache.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;
		}

		super.onStop();
	}

	@Override
	public void run() {

		// if there is an internet connection
		if (HTMLPageParser.isInternetConnectionAvailable(logsProvider)) {
			mHandler.sendEmptyMessage(0);
		} else {
			mHandler.sendEmptyMessage(1);
		}
		progressDialog.dismiss();

	}

	private void populateGridView() {

		MainNewsAdapter adapter = new MainNewsAdapter(
				StaticValues.getNewsPapers(), MainNewsActivity.this);

		// populate grid
		mainArticleListView.setAdapter(adapter);

		mainArticleListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

			}
		});

	}

	public void displayErrorMessage(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setMessage(text);
		ad.setCanceledOnTouchOutside(false);
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainNewsActivity.this.finish();
					}
				});

		ad.show();
	}

	/**
	 * Color action bar text in white
	 */
	private void colorActionBarTextWhite() {
		int actionBarTitleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
		if (actionBarTitleId > 0) {
			TextView title = (TextView) findViewById(actionBarTitleId);
			if (title != null) {
				title.setTextColor(Color.WHITE);
			}
		}
	}
	
	//menu
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menuHelper.populatedMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//on menu on clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		menuHelper.executeMenuAction(item);
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		if(adView != null)
		{
			adView.destroy();
		}
		
		super.onDestroy();
	}
	
	
	
	
	
}

