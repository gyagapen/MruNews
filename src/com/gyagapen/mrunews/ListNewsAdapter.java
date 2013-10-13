package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;


public class ListNewsAdapter  extends ArrayAdapter<News> implements ListAdapter, SectionIndexer	{

	private ArrayList<News> newsList = null;
	private  Activity activity = null;
	

	public ListNewsAdapter(ArrayList<News> someNews, Activity anActivity) {

		super(anActivity, 0);
		newsList = someNews;
		activity = anActivity;
	}

	public int getCount() {

		return newsList.size();
	}


	public News getItem(int position) {

		return newsList.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		final View listEntry = inflater.inflate(R.layout.news_entry, null);    // initialize the layout from xml

		final TextView newsName = (TextView) listEntry.findViewById(R.id.tvNewsName);
		final News currentEntry = newsList.get(position);
		newsName.setText(currentEntry.getNewsName());
		


		//add onclick listener
		NewsOnClickListener newsClickListener = new NewsOnClickListener(currentEntry);
		newsName.setOnClickListener(newsClickListener);

		return listEntry;

	}

	@Override
	public void notifyDataSetChanged() {

		//super.sort(new ApplicationNameComparator());
		super.notifyDataSetChanged();
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
