package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class NewsListActivity extends Activity {

	private ListView listViewLeft;
	private ListView listViewRight;
	private NewsItemsAdapter leftAdapter;
	private NewsItemsAdapter rightAdapter;

	int[] leftViewsHeights;
	int[] rightViewsHeights;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);

		listViewLeft = (ListView) findViewById(R.id.list_view_left);
		listViewRight = (ListView) findViewById(R.id.list_view_right);

		loadItems();

		listViewLeft.setOnTouchListener(touchListener);
		listViewRight.setOnTouchListener(touchListener);
		listViewLeft.setOnScrollListener(scrollListener);
		listViewRight.setOnScrollListener(scrollListener);

	}

	// Passing the touch event to the opposite list
	OnTouchListener touchListener = new OnTouchListener() {
		boolean dispatched = false;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(listViewLeft) && !dispatched) {
				dispatched = true;
				listViewRight.dispatchTouchEvent(event);
			} else if (v.equals(listViewRight) && !dispatched) {
				dispatched = true;
				listViewLeft.dispatchTouchEvent(event);
			}

			dispatched = false;
			return false;
		}
	};

	/**
	 * Synchronizing scrolling Distance from the top of the first visible
	 * element opposite list: sum_heights(opposite invisible screens) -
	 * sum_heights(invisible screens) + distance from top of the first visible
	 * child
	 */
	OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView v, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (view.getChildAt(0) != null) {
				if (view.equals(listViewLeft)) {
					leftViewsHeights[view.getFirstVisiblePosition()] = view
							.getChildAt(0).getHeight();

					int h = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						h += rightViewsHeights[i];
					}

					int hi = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						hi += leftViewsHeights[i];
					}

					int top = h - hi + view.getChildAt(0).getTop();
					listViewRight.setSelectionFromTop(
							listViewRight.getFirstVisiblePosition(), top);
				} else if (view.equals(listViewRight)) {
					rightViewsHeights[view.getFirstVisiblePosition()] = view
							.getChildAt(0).getHeight();

					int h = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						h += leftViewsHeights[i];
					}

					int hi = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						hi += rightViewsHeights[i];
					}

					int top = h - hi + view.getChildAt(0).getTop();
					listViewLeft.setSelectionFromTop(
							listViewLeft.getFirstVisiblePosition(), top);
				}

			}

		}
	};

	private void loadItems() {

		// build list of news
		ArrayList<News> newsList = StaticValues.getNewsPapers();

		Integer[] leftItems = new Integer[] { R.drawable.lexpress_news,
				R.drawable.defiplus};
		Integer[] rightItems = new Integer[] {
				R.drawable.lemauricien_newspaper_eco,
				R.drawable.lemauricien_newspaper_faits,
				R.drawable.lemauricien_newspaper_societe,
				R.drawable.lemauricien_newspaper_politique,
				R.drawable.lemauricien_newspaper_international,
				R.drawable.lemauricien_newspaper_sport,
				R.drawable.lemauricien_newspaper_magazine};

		leftAdapter = new NewsItemsAdapter(this, R.layout.news_item, leftItems,
				newsList, 0);
		rightAdapter = new NewsItemsAdapter(this, R.layout.news_item,
				rightItems, newsList, leftItems.length);
		listViewLeft.setAdapter(leftAdapter);
		listViewRight.setAdapter(rightAdapter);

		leftViewsHeights = new int[leftItems.length];
		rightViewsHeights = new int[rightItems.length];
	}

}