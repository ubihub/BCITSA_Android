package ca.bcitsa.android;



import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

 
public class MainActivity  extends ListActivity implements ActionBar.TabListener {
    /**
     * This class describes an individual sample (the sample title, and the activity class that
     * demonstrates this sample).
     */
	static boolean  showingFirst = true;
    private class Sample  {
        private CharSequence title;
        private Class<? extends Activity> activityClass;

        public Sample(String string, Class<? extends Activity> activityClass) {
            this.activityClass = activityClass;
            this.title = string;
        }

        @Override
        public String toString() {
            return title.toString();
        }
    }

    /**
     * The collection of all samples in the app. This gets instantiated in {@link
     * #onCreate(android.os.Bundle)} because the {@link Sample} constructor needs access to {@link
     * android.content.res.Resources}.
     */
    private static Sample[] mSamples;  
    private static MobileArrayAdapter[] mArray;
    

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	if ( showingFirst ) {
//    		addShortcut();
//    		NetworkChangeReceiver recevier = new NetworkChangeReceiver(this.getBaseContext(), this.getIntent());
    		showDialog(); 
    		showingFirst = false;
    	}
    	

    	String last_activity = PreferenceManager.getDefaultSharedPreferences(this).getString("last_activity", "");
	    if (last_activity == BCITSAWeb.class.getSimpleName()) {
	    	startActivityForResult(new Intent(this, BCITSAWeb.class), 1);
		} else if (last_activity == TheLink.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, TheLink.class), 2);
    	} else if (last_activity == BusMap.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, BusMap.class), 3);
    	} else if (last_activity == CampusMap.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, CampusMap.class), 4);
    	} else if (last_activity == StudyRooms.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, StudyRooms.class), 5);
    	} else if (last_activity == BCITSAlist.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, BCITSAlist.class), 6);
    	} else if (last_activity == BCITWeb.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, BCITWeb.class), 7);
    	} else if (last_activity == PhoneList.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, PhoneList.class), 8);
    	} else if (last_activity == MyBCIT.class.getSimpleName()) {
    	    startActivityForResult(new Intent(this, MyBCIT.class), 9);
     	} else {
    	    // assume default activity
///    	    startActivityForResult(new Intent(this, MyActivity1.class));
    	}       
        // Instantiate the list of samples.
        mSamples = new Sample[]{
				new Sample("SA + SNS", BCITSAWeb.class),
                new Sample("School B + The Link", TheLink.class),
                new Sample("Real-time Bus Map", BusMap.class),
                new Sample("Campus Map", CampusMap.class),
                new Sample("Study Rooms", StudyRooms.class),
                new Sample("School B + D2L", BCITSAlist.class),
                new Sample("School B + SNS", BCITWeb.class),
                new Sample("Phone Book", PhoneList.class),
                new Sample("My BCIT", MyBCIT.class),
                new Sample("School B + About", About.class),
        };
         

      
        setListAdapter(new ArrayAdapter<Sample>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1, 
                mSamples));
        setListAdapter(new MobileArrayAdapter(this)); 
    }
    

    
    private void addShortcut() {
        //Adding shortcut for MainActivity 
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(),
                MainActivity.class);
         
        shortcutIntent.setAction(Intent.ACTION_MAIN);
     
        Intent addIntent = new Intent();
        addIntent.putExtra("duplicate", false); 
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "BCITSA");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.bcitsa_logo));

     
        addIntent
                .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
    
	private void showDialog(){ 
		LayoutInflater inflater = this.getLayoutInflater();
		 new AlertDialog.Builder(this)
				.setTitle("School B - All in One!")
	    .setView(inflater.inflate(R.layout.logo, null))
	    .setInverseBackgroundForced(true)
//	    .setMessage("Thank you for using BCITSA App. If you have opinions or found bugs, email to the developer \n    James Kwon(ubihub@gmail.com) \nThis app always needs the Internet connection so it uses lots of 3G or 4G data. Continue?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	finish();
	            System.exit(1);
	        }
	     })
	     .show();
	}

	

	private void animatedStartActivity() {
		// we only animateOut this activity here.
		// The new activity will animateIn from its onResume() - be sure to implement it.
		final Intent intent = new Intent(getApplicationContext(), BusMap.class);
		// disable default animation for new intent
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.container), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
			@Override
			public void onAnimationFinished() {
				startActivity(intent);
			}
		});
	}

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        // Launch the sample associated with this list position.
 //   	if ( position == 1) animatedStartActivity();
        startActivity(new Intent(MainActivity.this, mSamples[position].activityClass));
    }



	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
