package com.gyagapen.mrunews;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridNewsActivity extends Activity {

	private GridView gridView;

	static final Integer[] numbers = new Integer[] {
			R.drawable.lemauricien_newspaper, R.drawable.lexpress_news };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create cache
		createCache();

		setContentView(R.layout.news_grid);

		gridView = (GridView) findViewById(R.id.gridViewNews);

		GridNewsAdapter adapter = new GridNewsAdapter(
				StaticValues.getNewsPapers(), this);

		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						((TextView) v).getText(), Toast.LENGTH_SHORT).show();
			}
		});

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
}
