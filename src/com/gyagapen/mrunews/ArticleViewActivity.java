package com.gyagapen.mrunews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.androidquery.AQuery;

public class ArticleViewActivity extends Activity implements Runnable {

	private ImageView artImageView;
	private TextView artTitleView;
	private TextView artContentView;
	private Button buttonComments;
	private TextView tvLinkToWeb;

	private String articleLink = null;
	private String articleId = null;

	private String Imagelink = null;
	private String ArtContent = null;
	private String ArtTitle = null;
	private String buttonCommentText = null;

	private ArrayList<ArticleComment> artComment = null;

	private int screenHeight;
	private int screenWidth;

	private FrameLayout frameLayout;

	private AQuery aq;

	// The "x" and "y" position of the "Show Button" on screen.
	Point p;

	private Animation anim;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Handler mHandler = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_view);

		// screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;

		// retrieve parameters
		Intent myIntent = getIntent();
		articleLink = myIntent.getStringExtra("ArticleLink");
		articleId = myIntent.getStringExtra("ArticleId");
		String newsName = myIntent.getStringExtra("NewsName");

		setTitle(newsName);

		initView();

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCancelable(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ArticleViewActivity.this.finish();
					}
				});

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					artTitleView.setText(ArtTitle);
					artContentView.setText(ArtContent);
					// loadImage(Imagelink, artImageView);
					aq = new AQuery(artImageView);
					aq.id(R.id.ImgViewArt).image(Imagelink);

					// set number of comments
					buttonComments.setText(buttonCommentText);
				} else if (msg.what == 1) {
					displayErrorMessage("Error while loading the page...");
				}
			}
		};

		// meanwhile retrieving article content
		Thread tRetrieveContent = new Thread(this);

		tRetrieveContent.start();

	}

	public void initView() {
		artImageView = (ImageView) findViewById(R.id.ImgViewArt);
		artTitleView = (TextView) findViewById(R.id.tvArtTitle);
		artContentView = (TextView) findViewById(R.id.tvArtContent);
		buttonComments = (Button) findViewById(R.id.butComment);
		frameLayout = (FrameLayout) findViewById(R.id.articleViewFrame);
		frameLayout.getForeground().setAlpha(0);

		buttonComments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Open popup window
				if (p != null)
					showPopup(ArticleViewActivity.this, p);
			}
		});
		
		tvLinkToWeb = (TextView) findViewById(R.id.tvLinkToWeb);
		tvLinkToWeb.setOnClickListener(new OnClickListener() {
			
			//open link in browser
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleLink));
				startActivity(browserIntent);
			}
		});

	}

	public static void loadImage(String url, ImageView image) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
					url).getContent());
			image.setImageBitmap(bitmap);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// getting info to display
		HTMLPageParser htmlPageParser = new HTMLPageParser(articleLink,
				articleId);
		ArticleContent artCont;
		try {
			artCont = htmlPageParser.parsePage();

			Imagelink = artCont.getImageLink();
			ArtContent = artCont.getContent();
			ArtTitle = artCont.getTitle();
			buttonCommentText = buttonComments.getText() + " ("
					+ artCont.getComment().size() + ")";
			artComment = artCont.getComment();

			mHandler.sendEmptyMessage(0);

		} catch (IOException e) {
			mHandler.sendEmptyMessage(1);
		}

		progressDialog.dismiss();

		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.push_right_in);
		artTitleView.setAnimation(anim);
		artImageView.setAnimation(anim);
		artContentView.setAnimation(anim);
		buttonComments.setAnimation(anim);
		anim.start();
	}

	public void finish() {
		super.finish();
		// transition animation
		overridePendingTransition(R.animator.push_right_in,
				R.animator.push_right_out);
	}

	// Get the x and y position after the button is draw on screen
	// (It's important to note that we can't get the position in the onCreate(),
	// because at that stage most probably the view isn't drawn yet, so it will
	// return (0, 0))
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		buttonComments.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = (int) (location[0] * 1.1);
		p.y = (int) (location[1] * 0.7);
	}

	// The method that displays the popup.
	private void showPopup(final Activity context, Point p) {
		int popupWidth = (int) (screenWidth * 0.9);
		int popupHeight = (int) (screenHeight * 0.75);

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) context
				.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.comment_list, viewGroup);

		// load comments
		ListView commentListView = (ListView) layout
				.findViewById(R.id.CommentListView);
		ListCommentAdapter listComAdapter = new ListCommentAdapter(artComment,
				this);
		commentListView.setAdapter(listComAdapter);

		// scroll bar
		commentListView.setFastScrollEnabled(true);
		commentListView.setScrollbarFadingEnabled(false);
		commentListView.setScrollContainer(true);
		commentListView.setTextFilterEnabled(true);

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		// Clear the default translucent background
		// popup.setBackgroundDrawable(new BitmapDrawable());

		// Some offset to align the popup a bit to the right, and a bit down,
		// relative to button's position.
		int OFFSET_X = 0;
		int OFFSET_Y = 0;

		popup.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {

				// undim background
				frameLayout.getForeground().setAlpha(0);
			}
		});

		// Displaying the popup at the specified location, + offsets.
		popup.showAtLocation(layout, Gravity.CENTER, p.x + OFFSET_X, p.y
				+ OFFSET_Y);

		// dim background
		frameLayout.getForeground().setAlpha(200);

	}

	public void displayErrorMessage(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(text);
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ArticleViewActivity.this.finish();
					}
				});

		ad.show();
	}

}
