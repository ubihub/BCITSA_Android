package ca.bcitsa.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;
import ca.bcitsa.android.StudyRooms.InnerWebViewClient;

public class MyBCIT  extends Activity  {
	
	WebView myWebView ;
	Intent intent;
	ProgressBar mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mybcit);
		myWebView = (WebView) findViewById(R.id.webView2);
		mProgress = (ProgressBar) findViewById(R.id.progressBar1);
//		  Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge_from_centre);
//		  myWebView.startAnimation(myFadeInAnimation); 
			overridePendingTransition(R.anim.slide_left, R.anim.main_menu);
    	myWebView.setWebViewClient(new InnerWebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	myWebView.loadUrl("file:///android_asset/net_err.html");
            }
        });
    	WebSettings webSettings = myWebView.getSettings();
    	webSettings.setJavaScriptEnabled(true);
    	webSettings.setAppCacheEnabled(true);
    	webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(myWebView.getWindowToken(), 0);
    	// Declare a button "Send"
    	String url = "https://my.bcit.ca";
    	goWeb(url);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){        
	    super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onKeyDown(int KeyCode, KeyEvent event) {
		if ((KeyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
			myWebView.goBack();
			return true;
		}
		return super.onKeyDown(KeyCode, event);
	}

	public class InnerWebViewClient extends WebViewClient {
		public boolean shouldOverriderUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mProgress.setVisibility(ProgressBar.VISIBLE);
	    }
		
		public void onPageFinished(WebView view, String url) {
			mProgress.setVisibility(ProgressBar.GONE);
			myWebView.clearHistory();
		}
		
		
	}
	
	   public void goHome(View v){
//		   animatedStartActivity();
		  finish();
		  super.onBackPressed();
	   }
	   
    public void goWeb(String url) {
    	// Check validation of the string
    	if ( Patterns.WEB_URL.matcher(url).matches() ) {

        	myWebView.loadUrl(url);

    	}
    	else // Display a Toast
    	{
    		Toast.makeText(this, "The URL " + url + " is NOT valid!", Toast.LENGTH_LONG).show();
    	}
    }
    
	protected void onResume() {
		// animateIn this activity
		//ActivitySwitcher.animationIn(findViewById(R.id.container), getWindowManager());
		overridePendingTransition(R.anim.slide_left, R.anim.main_menu);
//		Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge_from_centre);
//		  findViewById(R.id.container).startAnimation(myFadeInAnimation); 
		super.onResume();
	}
 
	protected void onPause(){
		overridePendingTransition(R.anim.slidein, R.anim.slideout);
		super.onPause();
	}
	 
		private void animatedStartActivity() {
			// we only animateOut this activity here.
			// The new activity will animateIn from its onResume() - be sure to implement it.
			final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			// disable default animation for new intent
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			  Animation slideout = AnimationUtils.loadAnimation(this, R.anim.slideout);
			  findViewById(R.id.container).startAnimation(slideout);  
//			  while ( !slideout.hasEnded()) { 
				 
//			  }
//			  finish();
//			  super.onBackPressed();
//			ActivitySwitcher.animationOut(findViewById(R.id.container), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
//				@Override
//				public void onAnimationFinished() {
//					startActivity(intent);
//				}
//			});
		}  


}

