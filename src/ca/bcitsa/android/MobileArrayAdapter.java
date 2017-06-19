package ca.bcitsa.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MobileArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private static String[] title = new String[] { "SA + SNS", "The Link", "Bus Map", "Campus Map",
			"Study Rooms", "Desire 2 Learn", "School B + SNS", "Phone Book", "My BCIT", "About" };
	private Class<? extends Activity> activityClass;

	public MobileArrayAdapter(Context context) {
		super(context, R.layout.list_menu, title);
		this.context = context;
//		this.title = title;
		this.activityClass = activityClass;
	}

    @Override
    public String toString() {
        return title.toString();
    }
    
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_menu, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		textView.setText(title[position]);
//		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//			    getResources().getDimension(R.dimen.textSize));

		// Change icon based on name
		String s = title[position];

		System.out.println(s);

		if (s.equals("The Link")) {
			imageView.setImageResource(R.drawable.icon_thelink);
		} else if (s.equals("SA + SNS")) {
			imageView.setImageResource(R.drawable.bcitsa_logo);
		} else if (s.equals("Bus Map")) {
			imageView.setImageResource(R.drawable.icon_busmap);
		} else if (s.equals("Campus Map")) {
			imageView.setImageResource(R.drawable.icon_campusmap);
		} else if (s.equals("Study Rooms")) {
			imageView.setImageResource(R.drawable.icon_book);
		} else if (s.equals("Desire 2 Learn")) {
			imageView.setImageResource(R.drawable.bcitsalist_logo);
		} else if (s.equals("School B + SNS")) {
			imageView.setImageResource(R.drawable.bcit_logo);
		} else if (s.equals("Phone Book")) {
			imageView.setImageResource(R.drawable.icon_phonelist);
		} else if (s.equals("My BCIT")) {
			imageView.setImageResource(R.drawable.mybcit_icon400x400);
		} else if (s.equals("About")) {
			imageView.setImageResource(R.drawable.icon_about);
		}

		return rowView;
	}
}
