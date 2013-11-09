package com.gyagapen.mrunews;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.ads.AdView;
import com.gyagapen.mrunews.adapters.ListCommentAdapter;
import com.gyagapen.mrunews.common.MenuHelper;
import com.gyagapen.mrunews.entities.ArticleComment;
import com.gyagapen.mrunews.entities.ArticleContent;
import com.gyagapen.mrunews.parser.HTMLPageParser;

public class ArticleViewActivity extends Activity implements Runnable {

	private ImageView artImageView;
	private TextView artTitleView;
	private TextView artContentView;
	private Button buttonComments;
	private Button buttonLinkToWeb;
	private Button share = null;

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
	private MenuHelper menuHelper;
	private AdView adView;
	
	private AQuery aq;

	// SocialAuth Component
	SocialAuthAdapter adapter;

	// The "x" and "y" position of the "Show Button" on screen.
	Point p;

	private Animation anim;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Handler mHandler = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.article_view);
		
		//generate ad banner
		/*menuHelper = new MenuHelper(this);
		LinearLayout linLayout = (LinearLayout)findViewById(R.id.articleview_linlayout);
		menuHelper.generateAdsBanner(adView, linLayout, this);*/

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
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ArticleViewActivity.this.finish();
					}
				});

		progressDialog.setOnCancelListener(new OnCancelListener() {

			// when progress dialog is cancelled
			public void onCancel(DialogInterface dialog) {
				// close activity
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

		buttonComments.setBackgroundResource(R.drawable.blue_gradient);
		buttonComments.setTextColor(Color.WHITE);
		buttonComments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Open popup window
				if (p != null)
					showPopup(ArticleViewActivity.this, p);
			}
		});

		buttonLinkToWeb = (Button) findViewById(R.id.butLinkToWeb);
		buttonLinkToWeb.setTextColor(Color.WHITE);
		buttonLinkToWeb.setBackgroundResource(R.drawable.blue_gradient);
		buttonLinkToWeb.setOnClickListener(new OnClickListener() {

			// open link in browser
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(articleLink));
				startActivity(browserIntent);
			}
		});

		share = (Button) findViewById(R.id.butShare);

		share.setText("Share");
		share.setTextColor(Color.WHITE);
		share.setBackgroundResource(R.drawable.blue_gradient);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addProvider(Provider.EMAIL, R.drawable.email);

		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		adapter.enable(share);

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

			progressDialog.dismiss();

			// animation
			anim = AnimationUtils.loadAnimation(getApplicationContext(),
					R.animator.push_right_in);
			artTitleView.setAnimation(anim);
			artImageView.setAnimation(anim);
			artContentView.setAnimation(anim);
			buttonComments.setAnimation(anim);
			anim.start();

		} catch (Exception e) {

			progressDialog.dismiss();

			// fallback : open webview activity
			Intent webViewIntent = new Intent(this, WebViewActivity.class);
			webViewIntent.putExtra("link", articleLink);
			startActivity(webViewIntent);

			// end the activity
			finish();
		}

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

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void onComplete(Bundle values) {

			// Variable to receive message status
			boolean status = false;

			Log.d("ShareButton", "Authentication Successful");

			// Get name of provider after authentication
			String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);

			// Share via Email Intent
			if (providerName.equalsIgnoreCase("share_mail")) {

				Intent email = new Intent(Intent.ACTION_SEND);
				// email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
				email.putExtra(Intent.EXTRA_SUBJECT, ArtTitle + " via "
						+ R.string.app_name);
				String content = "Please check this news: " + articleLink;
				email.putExtra(Intent.EXTRA_TEXT, content);
				// need this to prompts email client only
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email,
						"Choose an Email client"));

				status = true;
				// post via twitter
			} else if (providerName.equals("twitter")) {
				adapter.updateStatus("test", new MessageListener(), true);
				status = true;
			} // post via facebook
			else {
				try {

					adapter.updateStory(ArtTitle, "", "", "", articleLink,
							Imagelink, new MessageListener());

					status = true;
				} catch (Exception e) {
					status = false;
				}

			}

			// Post Toast or Dialog to display on screen
			if (status)
				Toast.makeText(ArticleViewActivity.this,
						"Message posted on " + providerName, Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(ArticleViewActivity.this,
						"Message not posted on" + providerName,
						Toast.LENGTH_SHORT).show();

		}

		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error " + error.getMessage());
		}

		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			// TODO Auto-generated method stub

		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {

		public void onExecute(Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(ArticleViewActivity.this, "Message posted",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(ArticleViewActivity.this, "Message not posted",
						Toast.LENGTH_LONG).show();
		}

		public void onError(SocialAuthError e) {

		}

		@Override
		public void onExecute(String arg0, Integer arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onPause() {

		// close progress dialog if it's opened
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		
		if(adView != null)
		{
			adView.destroy();
		}
		
		super.onDestroy();
	}

}
