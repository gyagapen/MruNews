package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.app.Activity;
import android.opengl.Visibility;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.androidquery.AQuery;


public class ListArticleAdapter  extends ArrayAdapter<ArticleHeader> implements ListAdapter, SectionIndexer	{

	private ArrayList<ArticleHeader> articles = null;
	private  Activity activity = null;
	private String newsName;
	private AQuery aq;
	
	//list of async task
	private ArrayList<GetImageAsync> asyncTaskList;

	public ListArticleAdapter(ArrayList<ArticleHeader> someArticles, Activity anActivity, String newsName) {

		super(anActivity, 0);
		articles = someArticles;
		activity = anActivity;
		this.newsName = newsName;
		asyncTaskList = new ArrayList<GetImageAsync>();
	}

	public int getCount() {

		return articles.size();
	}


	public ArticleHeader getItem(int position) {

		return articles.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		
		final ArticleHeader currentEntry = articles.get(position);
		
		if( convertView == null ){
	        //We must create a View:
	        convertView = inflater.inflate(R.layout.article_entry, parent, false);    // initialize the layout from xml
	    }
		
		//Image
		final ImageView imageView = (ImageView)convertView.findViewById(R.id.headerImageView);
		
		if (currentEntry.getImageLink() == null)
		{
			
			imageView.setTag(currentEntry.getLink());
			GetImageAsync getImgASync = new GetImageAsync(imageView, this, position);
			getImgASync.execute(currentEntry.getLink(), currentEntry.getId());
			
			asyncTaskList.add(getImgASync);
			
			//dummy link
			currentEntry.setImageLink("dummy");
			imageView.setImageBitmap(null);
			
		}
		else
		{		
			if (!currentEntry.getImageLink().equals("dummy"))
			{
				aq = new AQuery(convertView);
				aq.id(R.id.headerImageView).image(currentEntry.getImageLink());
			}
			else
			{
				imageView.setImageBitmap(null);
			}
		}

		
		//Title
		final TextView articleTitle = (TextView) convertView.findViewById(R.id.tvArticleTitle);		
		articleTitle.setText(currentEntry.getTitle());
		
		final TextView articleDate = (TextView) convertView.findViewById(R.id.tvArticleDate);		
		articleDate.setText(currentEntry.getPublishedDate());

		//add onclick listener
		ArticleOnClickListener articleClickListener = new ArticleOnClickListener(currentEntry, newsName);
		convertView.setOnClickListener(articleClickListener);
		
		

		return convertView;

	}
	
	public void updateArticleLink(int position, String imageLink)
	{

		final ArticleHeader currentEntry = articles.get(position);
		currentEntry.setImageLink(imageLink);
		articles.set(position, currentEntry);
		
		ListView listView = (ListView)activity.findViewById(R.id.listViewArticle);
		View v = listView.getChildAt(position - 
				listView.getFirstVisiblePosition());
		
		aq = new AQuery(v);
		aq.id(R.id.headerImageView).image(currentEntry.getImageLink());
	}

	
	//stop all asynctask
	public void stopAllASyncTask()
	{
		for(int i=0; i<asyncTaskList.size();i++)
		{
			asyncTaskList.get(i).cancel(true);
		}
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
