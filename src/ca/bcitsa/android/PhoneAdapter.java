package ca.bcitsa.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PhoneAdapter extends BaseAdapter {

    Context context;
    List<HashMap<String,String>> data;
    private static LayoutInflater inflater = null;
	List<HashMap<String,String>> officeDataCollection = 
	           new ArrayList<HashMap<String,String>>();
	String phone_number;
	
    public PhoneAdapter(Context context, List<HashMap<String,String>> officeDataCollection) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = officeDataCollection;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.rowview, null);
        TextView text = (TextView) vi.findViewById(R.id.office);
        Button bt_phone = (Button) vi.findViewById(R.id.Button_Phone);
        Button bt_email = (Button) vi.findViewById(R.id.Button_email);
        
        text.setText(data.get(position).get("officename"));
        phone_number = data.get(position).get("phone");
        String view_phone_number = "(" + phone_number.substring(0, 3) + ")" + phone_number.substring(3, 6) + "-" + phone_number.substring(6, phone_number.length());
        bt_phone.setText(view_phone_number);
        bt_phone.setHint(phone_number);
//        bt_email.setHint(data.get(position).get("email"));
        bt_email.setContentDescription(data.get(position).get("email"));
        
        return vi;
    }
    

}
