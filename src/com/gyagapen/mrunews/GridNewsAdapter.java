package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;

public class GridNewsAdapter extends ArrayAdapter<News> implements ListAdapter,
		SectionIndexer {

	private ArrayList<News> news = null;
	private Activity activity = null;
	
	private int screenHeight;
	private int screenWidth;

	public GridNewsAdapter(ArrayList<News> news, Activity anActivity) {

		super(anActivity, 0);
		this.news = news;
		activity = anActivity;
		
		// screen size
		DisplayMetrics metrics = new DisplayMetrics();
		anActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;
	}

	public int getCount() {

		return news.size();
	}

	public News getItem(int position) {

		return news.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		final News currentEntry = news.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.news_item, parent, false); // initialize
																				// the
																				// layout
																				// from
		
			// xml
			
			int itemHeight = (int)(screenHeight*0.33);
			int itemWidth = (int)(screenWidth*0.47);
			
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(itemWidth, itemHeight);
	        convertView.setLayoutParams(lp);
		}
		
		
	

		// News image
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.item_image);
		imageView.setImageResource(currentEntry.getImageRessource());

		// add onclick listener
		NewsOnClickListener newsClickListener = new NewsOnClickListener(
				currentEntry);
		imageView.setOnClickListener(newsClickListener);
		
		

		Animation anim = AnimationUtils.loadAnimation(getContext(),
				R.animator.push_up_in);
		imageView.setAnimation(anim);
		anim.start();

		return convertView;

	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

}
