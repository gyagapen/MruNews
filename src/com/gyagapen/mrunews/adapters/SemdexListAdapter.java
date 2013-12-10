package com.gyagapen.mrunews.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.opengl.Visibility;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.id;
import com.gyagapen.mrunews.R.layout;
import com.gyagapen.mrunews.entities.ArticleHeader;
import com.gyagapen.mrunews.entities.SemdexEntity;
import com.gyagapen.mrunews.parser.GetImageAsync;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class SemdexListAdapter  extends ArrayAdapter<SemdexEntity> implements ListAdapter	{

	private ArrayList<SemdexEntity> semdexEntities = null;
	private  Activity activity = null;


	public SemdexListAdapter(ArrayList<SemdexEntity> someEntities, Activity anActivity) {

		super(anActivity,0);
		semdexEntities = someEntities;
		activity = anActivity;

	}

	public int getCount() {

		return semdexEntities.size();
	}


	public SemdexEntity getItem(int position) {

		return semdexEntities.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		
		final SemdexEntity currentEntry = semdexEntities.get(position);
		
		if( convertView == null ){
	        //We must create a View:
	        convertView = inflater.inflate(R.layout.semdex_list_entry, parent, false);    // initialize the layout from xml
	    }
		
		//Image
		/*final ImageView imageView = (ImageView)convertView.findViewById(R.id.headerImageView);
		
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
		}*/

		
		//Title
		final TextView tvSemName = (TextView) convertView.findViewById(R.id.tvSemdexName);		
		tvSemName.setText(currentEntry.getName());
		
		final TextView tvSemNominal = (TextView) convertView.findViewById(R.id.tvSemdexNominal);		
		tvSemNominal.setText(currentEntry.getNominal());
		
		final TextView semdexLCPrice = (TextView) convertView.findViewById(R.id.tvSemdexLastClosingPrice);		
		semdexLCPrice.setText(currentEntry.getLastClosingPrice());
		
		final TextView semdexLatestPrice = (TextView) convertView.findViewById(R.id.tvSemdexLatest);		
		semdexLatestPrice.setText(currentEntry.getLatestPrice());

		return convertView;

	}
	
	public void updateArticleLink(int position, String imageLink)
	{

		/*final ArticleHeader currentEntry = semdexEntities.get(position);
		currentEntry.setImageLink(imageLink);
		semdexEntities.set(position, currentEntry);
		
		PullToRefreshListView listView = (PullToRefreshListView)activity.findViewById(R.id.listViewArticle);
		View v = listView.getRefreshableView().getChildAt(position - 
				listView.getRefreshableView().getFirstVisiblePosition()+1);
		
		aq = new AQuery(v);
		aq.id(R.id.headerImageView).image(currentEntry.getImageLink());*/
	}

	
	//stop all asynctask
	public void stopAllASyncTask()
	{
		/*for(int i=0; i<asyncTaskList.size();i++)
		{
			asyncTaskList.get(i).cancel(true);
		}*/
	}



	
	



}
