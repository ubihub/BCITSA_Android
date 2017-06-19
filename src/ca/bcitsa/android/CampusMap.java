package ca.bcitsa.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class CampusMap extends FragmentActivity
implements
ConnectionCallbacks,
OnConnectionFailedListener,
LocationListener,
OnMyLocationButtonClickListener,
OnMarkerClickListener,
OnInfoWindowClickListener,
OnMarkerDragListener,
OnItemSelectedListener,
OnCameraChangeListener,
OnMapClickListener,
OnSeekBarChangeListener{
private static final LatLng Burnaby = new LatLng(49.2514456, -123.00208);
private static final LatLng Downtown = new LatLng(49.283546, -123.114789);
private static final LatLng Richmond = new LatLng(49.185099, -123.144330);
private static final LatLng Marine = new LatLng(49.312814, -123.086319);
private LatLng Target ;
private static final Location lBurnaby = new Location("LocBurnaby");
private static final Location lDowntown = new Location("LocDowntown");
private static final Location lRichmond = new Location("LocRichmond");
private static final Location lMarine = new Location("LocMarine");
private Location currentLocation, lTarget;
private GoogleMap mMap;
boolean bFirst = true;
private LocationClient mLocationClient;
private TextView mMessageView;
public Marker mBurnaby, mDowntown, mRichmond, mMarine, mSpot;
private final List<Marker> mMarkerRainbow = new ArrayList<Marker>();
private TextView mTopText;
private SeekBar mRotationBar;
private CheckBox mFlatBox;
private Polyline polyline ;
private Spinner spinner1;
private int angle = 0;
private final Random mRandom = new Random();
private boolean userIntervention = false;
private int count = 0;

/** Demonstrates customizing the info window and/or its contents. */
class CustomInfoWindowAdapter implements InfoWindowAdapter {
private final RadioGroup mOptions;

// These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
// "title" and "snippet".
private final View mWindow;
private final View mContents;


CustomInfoWindowAdapter() {
    mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
    mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
    mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
}

@Override
public View getInfoWindow(Marker marker) {
    if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
        // This means that getInfoContents will be called.
        return null;
    }
    render(marker, mWindow);
    return mWindow;
}

@Override
public View getInfoContents(Marker marker) {
    if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
        // This means that the default info contents will be used.
        return null;
    }
    render(marker, mContents);
    return mContents;
}

private void render(Marker marker, View view) {
    int badge;
    // Use the equals() method on a Marker to check for equals.  Do not use ==.
    if (marker.equals(mBurnaby)) {
        badge = R.drawable.badge_wa;
    } else {
        // Passing 0 to setImageResource will clear the image view.
        badge = 0;
    }
    ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

    String title = marker.getTitle();
    TextView titleUi = ((TextView) view.findViewById(R.id.title));
    if (title != null) {
        // Spannable string allows us to edit the formatting of the text.
        SpannableString titleText = new SpannableString(title);
        titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
        titleUi.setText(titleText);
    } else {
        titleUi.setText("");
    }

    String snippet = marker.getSnippet();
    TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
    if (snippet != null && snippet.length() > 12) {
        SpannableString snippetText = new SpannableString(snippet);
        snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
        snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
        snippetUi.setText(snippetText);
    } else {
        snippetUi.setText("");
    }
}
}

// These settings are the same as the settings for the map. They will in fact give you updates
// at the maximal rates currently possible.
private static final LocationRequest REQUEST = LocationRequest.create()
    .setInterval(5000)         // 5 seconds
    .setFastestInterval(16)    // 16ms = 60fps
    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
static final int DAY_VIEW_MODE = 0;
static final int WEEK_VIEW_MODE = 1;

private SharedPreferences mPrefs;
public int mCurViewMode;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState); 

//overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
overridePendingTransition(R.anim.slide_left, R.anim.main_menu);
setContentView(R.layout.campus_map);
lBurnaby.setLatitude(Burnaby.latitude);
lBurnaby.setLongitude(Burnaby.longitude);
lDowntown.setLatitude(Downtown.latitude);
lDowntown.setLongitude(Downtown.longitude);
lRichmond.setLatitude(Richmond.latitude);
lRichmond.setLongitude(Richmond.longitude);
lMarine.setLatitude(Marine.latitude);
lMarine.setLongitude(Marine.longitude);
spinner1 = (Spinner) findViewById(R.id.spinner1);
//spinner1.setOnItemSelectedListener(new MapItemSelectedListener());
spinner1.setOnItemSelectedListener(this);
setUpMapIfNeeded();
}


public void onActivityResult() {
    // kill the activity if they go "back" to here
    finish();
}

@Override
protected void onResume() {
    Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
    e.putString("last_activity", getClass().getSimpleName());
    e.commit();
	overridePendingTransition(R.anim.slide_left, R.anim.main_menu);
super.onResume();
setUpMapIfNeeded();
setUpLocationClientIfNeeded();
mLocationClient.connect();

}


protected void onStart() {
    super.onStart();
//  overridePendingTransition(R.anim.fadein, R.anim.fadeout);     
}


@Override
public void onPause() {
//    SharedPreferences.Editor ed = mPrefs.edit();
//    ed.putInt("view_mode", mCurViewMode);
//    ed.commit();
	overridePendingTransition(R.anim.slidein, R.anim.slideout);
	super.onPause();
	if (mLocationClient != null) {
	    mLocationClient.disconnect();
	}
}

private void setUpMapIfNeeded() {
// Do a null check to confirm that we have not already instantiated the map.
if (mMap == null) {
    // Try to obtain the map from the SupportMapFragment.
    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
            .getMap();
    // Check if we were successful in obtaining the map.
    if (mMap != null) {
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);

        setUpMap();
    }
}
}


private void setUpMap() {
// Hide the zoom controls as the button panel will cover it.
//mMap.getUiSettings().setZoomControlsEnabled(false);

// Add lots of markers to the map.
addMarkersToMap();

// Setting an info window adapter allows us to change the both the contents and look of the
// info window.
//mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

// Set listeners for marker events.  See the bottom of this class for their behavior.
//mMap.setOnMarkerClickListener(this);
//mMap.setOnInfoWindowClickListener(this);
//mMap.setOnMarkerDragListener(this);


// Pan to see all markers in view.
// Cannot zoom to bounds until the map has a size.
final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
if (mapView.getViewTreeObserver().isAlive()) {
    mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
        @SuppressWarnings("deprecation") // We use the new method when supported
        @SuppressLint("NewApi") // We check which build version we are using.
        @Override
        public void onGlobalLayout() {
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(Burnaby)
                    .include(Downtown)
                    .include(Richmond)
                    .include(Marine)
                    .build();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
              mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            } else {
              mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
           mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    });
}
}


private void addMarkersToMap() {
    // Uses a colored icon.
    mDowntown = mMap.addMarker(new MarkerOptions()
    .position(Downtown)
    .title("Downtown Campus")
    .snippet("555 Seymour Street Vancouver")
    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

// Uses a custom icon with the info window popping out of the center of the icon.
    mBurnaby = mMap.addMarker(new MarkerOptions()
    .position(Burnaby)
    .title("Burnaby Main Campus")
    .snippet("3700 Winllingdon Avenue"));
//    .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
//    .infoWindowAnchor(0f, 0f));
	mRichmond = mMap.addMarker(new MarkerOptions()
            .position(Richmond)
            .title("Richmond Campus")
            .snippet("3800 Cessna Drive Richmond")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

    // Uses a custom icon with the info window popping out of the center of the icon.
    mMarine = mMap.addMarker(new MarkerOptions()
            .position(Marine)
            .title("Marine Campus")
            .snippet("265 West Esplanade North Vancouver"));
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
//            .infoWindowAnchor(0f, 0f));
	mDowntown.showInfoWindow();
}

private void setUpLocationClientIfNeeded() {
if (mLocationClient == null) {
    mLocationClient = new LocationClient(
            getApplicationContext(),
            this,  // ConnectionCallbacks
            this); // OnConnectionFailedListener
}
}


public void show_DowntownCampus(View v){
	Target = Downtown;
    CameraPosition cp = new CameraPosition.Builder().target(Target).tilt(45.0f)
            .zoom(18).bearing(0).build();
//	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
//	mMap.moveCamera(update);	
//	mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	int distance = (int) currentLocation.distanceTo(lDowntown);
	Toast.makeText(this, distance + " m from Downtown Campus", Toast.LENGTH_LONG).show();
	
	if ( ! mDowntown.isInfoWindowShown() )   
		mDowntown.showInfoWindow();

}

public void show_BurnabyCampus(View v){
	Target = Burnaby;
    CameraPosition cp = new CameraPosition.Builder().target(Target).tilt(45.0f)
            .zoom(17).bearing(90).build();
//	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        
//    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
//	mMap.moveCamera(update);	
//	mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	int distance = (int) currentLocation.distanceTo(lBurnaby);
	Toast.makeText(this, distance + " m from Burnaby Centre", Toast.LENGTH_LONG).show();
	if ( ! mBurnaby.isInfoWindowShown() ) {
		mDowntown.hideInfoWindow();
		mBurnaby.showInfoWindow();
	}

}


public void show_RichmondCampus(View v){
	Target = Richmond;
    CameraPosition cp = new CameraPosition.Builder().target(Target).tilt(45.0f)
            .zoom(17).bearing(180).build();
//	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
//	mMap.moveCamera(update);	
//	mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	int distance = (int) currentLocation.distanceTo(lRichmond);
	Toast.makeText(this, distance + " m from Richmond Campus", Toast.LENGTH_LONG).show();
	
	if ( ! mRichmond.isInfoWindowShown() )   
		mRichmond.showInfoWindow();

}
public void show_MarineCampus(View v){
	Target = Marine;
    CameraPosition cp = new CameraPosition.Builder().target(Target).tilt(45.0f)
            .zoom(18).bearing(0).build();
//	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
//	mMap.moveCamera(update);	
//	mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	int distance = (int) currentLocation.distanceTo(lMarine);
	Toast.makeText(this, distance + " m from Marine Campus", Toast.LENGTH_LONG).show();
	
	if ( ! mMarine.isInfoWindowShown() )   
		mMarine.showInfoWindow();
}


/**
* Button to get current Location. This demonstrates how to get the current Location as required
* without needing to register a LocationListener.
*/
public void showMyLocation(View view) {
if (mLocationClient != null && mLocationClient.isConnected()) {
    String msg = "Location = " + mLocationClient.getLastLocation();
    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
}
}

/**
* Implementation of {@link LocationListener}.
*/
@Override
public void onLocationChanged(Location location) {

	currentLocation = location;
//LatLng latlong = new LatLng(49.2514456, -123.00208) ;
LatLng latlong = new LatLng(49.283115, -123.115234) ;
float lat = (float) (location.getLatitude());
float lng = (float) (location.getLongitude());
float distance = location.distanceTo(lBurnaby);
String str = "Current location is ";
//Location location  = mMap.getMyLocation();
if( location != null) {
	if(bFirst) {

    CameraPosition cp = new CameraPosition.Builder().target(latlong).tilt(45.0f)
            .zoom(18).build();
    Target = Downtown;   
//    mDowntown.showInfoWindow();
//    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
//	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlong,17);
//	mMap.moveCamera(update);
    bFirst = false;

    }


}

if ( ! userIntervention && count > 0 ) {
	latlong = new LatLng(location.getLatitude(), location.getLongitude());
	CameraPosition cp = new CameraPosition.Builder().target(Target).tilt(45.0f)
	.zoom(18).bearing(angle += 90).build();
	//mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
	//mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
	//mMap.moveCamera(update);	
	//mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	if ( polyline != null ) polyline.remove();
	polyline = mMap.addPolyline((new PolylineOptions()).add(latlong, Target).color(android.graphics.Color.BLUE).width(1.0f));
//  mMap.addCircle((new CircleOptions()).fillColor(android.graphics.Color.RED).center(Burnaby)); 
	Toast.makeText(this, "Click the map to stop rotating", Toast.LENGTH_SHORT).show();
	Toast.makeText(this, "Use two fingers to zoom in and out", Toast.LENGTH_SHORT).show();
	distance = (int) currentLocation.distanceTo(lTarget);
	Toast.makeText(this, distance + " m from Current Location" , Toast.LENGTH_SHORT).show();
	

}
	count = 1;


}

/**
* Callback called when connected to GCore. Implementation of {@link ConnectionCallbacks}.
*/
@Override
public void onConnected(Bundle connectionHint) {
mLocationClient.requestLocationUpdates(
        REQUEST,
        this);  // LocationListener
}

/**
* Callback called when disconnected from GCore. Implementation of {@link ConnectionCallbacks}.
*/
@Override
public void onDisconnected() {
// Do nothing
}

/**
* Implementation of {@link OnConnectionFailedListener}.
*/
@Override
public void onConnectionFailed(ConnectionResult result) {
	userIntervention = true;
}

@Override
public boolean onMyLocationButtonClick() {
 userIntervention = true;
 
return false;
}

@Override
public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
if (!checkReady()) {
    return;
}
float rotation = seekBar.getProgress();
for (Marker marker : mMarkerRainbow) {
    marker.setRotation(rotation);
}
}

private boolean checkReady() {
if (mMap == null) {
    Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
    return false;
}
return true;
}


@Override
public void onStartTrackingTouch(SeekBar arg0) {
// TODO Auto-generated method stub

}

@Override
public void onStopTrackingTouch(SeekBar arg0) {
// TODO Auto-generated method stub

}

@Override
public void onMarkerDrag(Marker arg0) {
// TODO Auto-generated method stub

}

@Override
public void onMarkerDragEnd(Marker arg0) {
// TODO Auto-generated method stub

}

@Override
public void onMarkerDragStart(Marker arg0) {
// TODO Auto-generated method stub
	 userIntervention = true;
}

@Override
public void onInfoWindowClick(Marker arg0) {
// TODO Auto-generated method stub
	 userIntervention = true;
}

@Override
public boolean onMarkerClick(final Marker marker) {
try{
    final Handler handler = new Handler();
    final long start = SystemClock.uptimeMillis();
    final long duration = 1500;

    final Interpolator interpolator = new BounceInterpolator();
    userIntervention = true;
    handler.post(new Runnable() {
        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            float t = Math.max(1 - interpolator
                    .getInterpolation((float) elapsed / duration), 0);
            marker.setAnchor(0.5f, 1.0f + 2 * t);

            String str = marker.getSnippet();
            marker.setDraggable(true);

            if (t > 0.0) {
                // Post again 16ms later.
                handler.postDelayed(this, 16);
            }
        }
    });
    

} catch( Exception e){
	e.getCause().printStackTrace();
}
// We return false to indicate that we have not consumed the event and that we wish
// for the default behavior to occur (which is for the camera to move such that the
// marker is centered and for the marker's info window to open, if it has one).

return true;
}


@Override
public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
	String info = " ";
	mSpot = mBurnaby;
	mSpot.setVisible(false);
	mSpot.remove();
	userIntervention = false;
	
	// TODO Auto-generated method stub
	
	String spot = parent.getItemAtPosition(pos).toString();
	String building = spot.substring(spot.lastIndexOf("(")+1, spot.lastIndexOf(")"));

	if ( building.equalsIgnoreCase("NE1")) {
		Target = new LatLng(49.254038, -123.000984);
	} else if ( building .equalsIgnoreCase("Downtown")) {
		Target = Downtown;
		info = "555 Seymour Street Vancouver";
	} else if ( building .equalsIgnoreCase("Burnaby")) {
		Target = Burnaby;
		info ="3700 Winllingdon Avenue Burnaby";
	} else if ( building .equalsIgnoreCase("Richmond")) {
		Target = Richmond;
		info = "3800 Cessna Drive, Richmond";
	} else if ( building .equalsIgnoreCase("Marine")) {
		Target = Marine;
		info = "265 West Esplanade North Vancouver";
	} else if ( building .equalsIgnoreCase("SE16")) {
		Target = new LatLng(49.248788, -123.000821);
	} else if ( building .equalsIgnoreCase("SW1")) {
		Target = new LatLng(49.251197, -123.002989);
	} else if ( building .equalsIgnoreCase("SE2")) {
		Target = new LatLng(49.251380, -123.001648);
	} else if ( building .equalsIgnoreCase("SE6")) {
		Target = new LatLng(49.250875, -123.000757);
	} else if ( building .equalsIgnoreCase("Security")) {
		Target = new LatLng(49.251418, -123.002554);
	} else if ( building .equalsIgnoreCase("SE1")) {
		Target = new LatLng(49.251187, -122.999352);
	} else if ( building .equalsIgnoreCase("SE14")) {
		Target = new LatLng(49.249478, -123.001100);
	} else if ( building .equalsIgnoreCase("Field")) {
		Target = new LatLng(49.248203, -123.001025);
	} else if ( building .equalsIgnoreCase("CARI")) {
		Target = new LatLng(49.246418, -123.004674);
	} else if ( building .equalsIgnoreCase("SE6")) {
		Target = new LatLng(49.250847, -123.000864);
	} else if ( building .equalsIgnoreCase("NE16")) {
		Target = new LatLng(49.251961, -123.000113);
	} else if ( building .equalsIgnoreCase("Dorm")) {
		Target = new LatLng(49.247071, -123.001939);
	} else {
		Target = Burnaby;
	}
	
	lTarget = new Location(building);
	lTarget.setLatitude(Target.latitude);
	lTarget.setLongitude(Target.longitude);

    CameraPosition cp = new CameraPosition.Builder().target(Target).tilt(45.0f)
            .zoom(18).bearing(angle += 90).build();
    
//	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
//	mMap.moveCamera(update);	
//	mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	if ( currentLocation != null ) {
		int distance = (int) currentLocation.distanceTo(lTarget);
		Toast.makeText(this, distance + " m from " + spot , Toast.LENGTH_LONG).show();
	}

    mSpot = mMap.addMarker(new MarkerOptions()
    .position(Target)
    .title(spot)
    .snippet(info));
    mSpot.showInfoWindow();
    

//	gotoPosition(spinner1.getItemAtPosition(pos).toString(), latlng);
}

public void gotoPosition(String str, LatLng target){

    CameraPosition cp = new CameraPosition.Builder().target(target).tilt(45.0f)
            .zoom(17).bearing(0).build();
//	mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
//  mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
	CameraUpdate update = CameraUpdateFactory.newCameraPosition(cp);
//	mMap.moveCamera(update);	
//	mMap.animateCamera(CameraUpdateFactory.zoomIn());
	mMap.animateCamera(update, 2000, null);
	final Location loc = new Location("Loctemp");
	loc.setLatitude(target.latitude);
	loc.setLongitude(target.longitude);
	int distance = (int) currentLocation.distanceTo(loc);
	Toast.makeText(this, distance + " m from " + str , Toast.LENGTH_LONG).show();
	
	if ( ! mDowntown.isInfoWindowShown() )   
		mDowntown.showInfoWindow();
	
}

@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void onCameraChange(CameraPosition arg0) {
	// TODO Auto-generated method stub
	
}


@Override
public void onMapClick(LatLng arg0) {
	// TODO Auto-generated method stub
	userIntervention = true;
}




}



