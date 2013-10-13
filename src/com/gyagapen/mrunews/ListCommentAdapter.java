package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.app.Activity;
import android.opengl.Visibility;
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


public class ListCommentAdapter  extends ArrayAdapter<ArticleComment> implements ListAdapter, SectionIndexer	{

	private ArrayList<ArticleComment> comments = null;
	private  Activity activity = null;
	

	public ListCommentAdapter(ArrayList<ArticleComment> someComments, Activity anActivity) {

		super(anActivity, 0);
		comments = someComments;
		activity = anActivity;
	}

	public int getCount() {

		return comments.size();
	}


	public ArticleComment getItem(int position) {

		return comments.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();
		
		final ArticleComment currentEntry = comments.get(position);
		
		if( convertView == null ){
	        convertView = inflater.inflate(R.layout.comment, parent, false);    // initialize the layout from xml
	    }

		
		//Title
		final TextView commentTitle = (TextView) convertView.findViewById(R.id.tvCommentTitle);		
		commentTitle.setText(currentEntry.getTitle());
		
		//Author - Date
		final TextView commentAuthDate = (TextView) convertView.findViewById(R.id.tvCommentAuthDate);
		String authDateText = currentEntry.getAuthor()+" - "+currentEntry.getDate();
		commentAuthDate.setText(authDateText);
		
		//Content
		final TextView commentContent = (TextView) convertView.findViewById(R.id.tvCommentContent);
		commentContent.setText(currentEntry.getContent());
		

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
