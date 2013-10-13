package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {


	private ListView NewsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initUIComponent();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		populateNewsList(StaticValues.getNewsPapers());

	}
	
	
	public void initUIComponent()
	{
		NewsListView = (ListView)findViewById(R.id.NewsListView);
	}

	public void populateNewsList(ArrayList<News> newsList)
	{
		ListNewsAdapter listNewsAdapter = new ListNewsAdapter(newsList, this);
		NewsListView.setAdapter(listNewsAdapter);
		
		//scroll bar
		NewsListView.setFastScrollEnabled(true);
		NewsListView.setScrollbarFadingEnabled(false);
		NewsListView.setScrollContainer(true);
		NewsListView.setTextFilterEnabled(true);

	}

}
