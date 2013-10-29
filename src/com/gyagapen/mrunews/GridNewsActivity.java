package com.gyagapen.mrunews;

import java.io.File;
import java.io.IOException;

import com.androidquery.AQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridNewsActivity extends Activity implements Runnable {

	private GridView gridView;
	private LogsProvider logsProvider = null;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Handler mHandler = null;

	static final Integer[] numbers = new Integer[] {
			R.drawable.lemauricien_newspaper, R.drawable.lexpress_news };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		logsProvider = new LogsProvider(null, this.getClass());
		
		// create cache
		createCache();

		setContentView(R.layout.news_grid);

		gridView = (GridView) findViewById(R.id.gridViewNews);

		

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Checking Internet Connection...");
		progressDialog.setCancelable(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quit",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GridNewsActivity.this.finish();
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
	
	private void populateGridView()
	{
		
		GridNewsAdapter adapter = new GridNewsAdapter(
				StaticValues.getNewsPapers(), GridNewsActivity.this);
		
		// populate grid
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						((TextView) v).getText(),
						Toast.LENGTH_SHORT).show();
			}
		});

	}
	
	public void displayErrorMessage(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(text);
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GridNewsActivity.this.finish();
					}
				});

		ad.show();
	}
}
