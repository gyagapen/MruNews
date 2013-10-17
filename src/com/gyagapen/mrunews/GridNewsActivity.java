package com.gyagapen.mrunews;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridNewsActivity extends Activity {

	private GridView gridView;
	
	static final Integer[] numbers = new Integer[] { 
		R.drawable.lemauricien_newspaper,
		R.drawable.lexpress_news
		};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
 
		setContentView(R.layout.news_grid);
 
		gridView = (GridView) findViewById(R.id.gridViewNews);
 
		GridNewsAdapter adapter = new GridNewsAdapter(StaticValues.getNewsPapers(), this);
 
		gridView.setAdapter(adapter);
 
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
			   Toast.makeText(getApplicationContext(),
				((TextView) v).getText(), Toast.LENGTH_SHORT).show();
			}
		});
 
	}
}
