package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class NewsItemsAdapter extends ArrayAdapter<Integer> {

	Context context;
	LayoutInflater inflater;
	int layoutResourceId;
	float imageWidth;
	ArrayList<News> newsList;
	
	//start position for right row
	int leftItemsCount = 0;
	

	public NewsItemsAdapter(Context context, int layoutResourceId,
			Integer[] items, ArrayList<News> news, int leftCount) {
		super(context, layoutResourceId, items);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		newsList = news;
		leftItemsCount = leftCount;

		float width = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getWidth();
		float margin = (int) convertDpToPixel(10f, (Activity) context);
		// two images, three margins of 10dips
		imageWidth = ((width - (3 * margin)) / 2);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FrameLayout row = (FrameLayout) convertView;
		ItemHolder holder;
		Integer item = getItem(position);

		if (row == null) {
			holder = new ItemHolder();
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (FrameLayout) inflater.inflate(layoutResourceId, parent,
					false);
			ImageView itemImage = (ImageView) row.findViewById(R.id.item_image);
			holder.itemImage = itemImage;
		} else {
			holder = (ItemHolder) row.getTag();
		}

		row.setTag(holder);

		// add onclick listener
		NewsOnClickListener newsClickListener = new NewsOnClickListener(
				newsList.get(position+leftItemsCount));
		holder.itemImage.setOnClickListener(newsClickListener);
		
		Animation anim = AnimationUtils.loadAnimation(getContext(),
				R.animator.push_up_in);
		holder.itemImage.setAnimation(anim);
		anim.start();

		setImageBitmap(item, holder.itemImage);
		return row;
	}

	public static class ItemHolder {
		ImageView itemImage;
	}

	// resize the image proportionately so it fits the entire space
	private void setImageBitmap(Integer item, ImageView imageView) {
		Bitmap bitmap = BitmapFactory.decodeResource(getContext()
				.getResources(), item);
		float i = ((float) imageWidth) / ((float) bitmap.getWidth());
		float imageHeight = i * (bitmap.getHeight());
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView
				.getLayoutParams();
		params.height = (int) imageHeight;
		params.width = (int) imageWidth;
		imageView.setLayoutParams(params);
		imageView.setImageResource(item);
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

}