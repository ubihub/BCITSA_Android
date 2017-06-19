package ca.bcitsa.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BCITSAWeb  extends Activity  {

	WebView myWebView ;
	Intent intent;
    private Handler mHandler = new Handler();
    private boolean mShowingBack = false;
    private static boolean showingFirst = true;
    ProgressBar mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
		setContentView(R.layout.bcitsaweb);
/*
        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing the
            // front of the card to this activity. If there is saved instance state,
            // this fragment will have already been added to the activity.
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new CardFrontFragment())
                    .commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }

        // Monitor back stack changes to ensure the action bar shows the appropriate
        // button (either "photo" or "info").
        getFragmentManager().addOnBackStackChangedListener(this);
 */  
		ImageView myImageView= (ImageView)findViewById(R.id.expanded_image);
//		myImageView.setImageResource(R.drawable.header_logo);
		mProgress = (ProgressBar) findViewById(R.id.progressBar3);
//		  Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
//		  myImageView.startAnimation(myFadeInAnimation); 
		overridePendingTransition(R.anim.slide_left, R.anim.main_menu);
		myWebView = (WebView) findViewById(R.id.webView3);
    	myWebView.setWebViewClient(new InnerWebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	myWebView.loadUrl("file:///android_asset/net_err.html");
            }
        });
    	WebSettings webSettings = myWebView.getSettings();
    	webSettings.setAppCacheEnabled(true);
    	webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//		  Animation ZoomInAni = AnimationUtils.loadAnimation(this, R.anim.enlarge_from_centre);
//		  myWebView.startAnimation(ZoomInAni); 
    	webSettings.setJavaScriptEnabled(true);
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(myWebView.getWindowToken(), 0);
    	// Declare a button "Send"
//        final Button button = (Button) findViewById(R.id.button_BCITSAWeb);
        // Add a On Click Listener 
//        button.setOnClickListener(this);
		String url = "http://bcitsa.ca";
    	goWeb(url);

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){        
	    super.onConfigurationChanged(newConfig);
	}	

	protected void onStart() {
        super.onStart();
  //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);     
	}
	
	   public void goHome(View v){
		String url = "http://bcitsa.ca";
	    	goWeb(url);
//	    	finish();
//		  super.onBackPressed();
	   }
	
 //   public void onClick(View v) {
 //   	String url = "http://m.bcitsa.ca";
 //   	goWeb(url);
 //   }
    
    public void goWeb(String url) {
    	// Check validation of the string
    	if ( Patterns.WEB_URL.matcher(url).matches() ) {
    		// Add "http://"
//    		url = "https://" + url;
    		// Get a WebView
        	myWebView.loadUrl(url);

    	}
    	else // Display a Toast
    	{
    		Toast.makeText(this, "The URL " + url + " is NOT valid!", Toast.LENGTH_LONG).show();
    	}
    }
    
   

   public void showCampusMap(View v){
//	   animatedStartActivity(CampusMap.class);
	   overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
	    intent = new Intent(this, CampusMap.class);
        startActivity(intent);  	   
   }


	       
   public void goFacebook(View v){
	   	String url = "https://m.facebook.com/bcitsa";
	   	goWeb(url);
		   
	   }
	       
	  	   
   public void showBCISTSAlist(View v){
   	String url = "https://mobile.twitter.com/bcitsa";
   	goWeb(url);
//	   animatedStartActivity(BCITSAlist.class);
//	   intent = new Intent(this, BCITSAlist.class);
//       startActivity(intent);  	   
   }
     
   public void showBusMap(View v){
	   animatedStartActivity(BusMap.class);
	   //intent = new Intent(this, BusMap.class);
       //startActivity(intent);  	   
   }
   
   public void showAbout(View v){
	   animatedStartActivity(About.class);
//	   intent = new Intent(this, About.class);
//        startActivity(intent);  	   
   }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
/*		
        MenuItem item = menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
                mShowingBack
                        ? R.string.action_photo
                        : R.string.action_info);
        item.setIcon(mShowingBack
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
*/
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




//    public void onBackStackChanged() {
//        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);

        // When the back stack changes, invalidate the options menu (action bar).
//        invalidateOptionsMenu();
//    }


@Override
public void onPause() {
	overridePendingTransition(R.anim.slidein, R.anim.slideout);
super.onPause();

}

   

	protected void onResume() {
		// ActivitySwitcher.animationIn(findViewById(R.id.container), getWindowManager());
//		  Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge_from_centre);
//		  findViewById(R.id.container).startAnimation(myFadeInAnimation); 
		super.onResume();
	}
 
	private void animatedStartActivity(Class<?> cls) {
		// we only animateOut this activity here.
		// The new activity will animateIn from its onResume() - be sure to implement it.
		final Intent intent = new Intent(getApplicationContext(), cls);
		// disable default animation for new intent
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.container), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
			@Override
			public void onAnimationFinished() {
				startActivity(intent);
			}
		});
	}
}
