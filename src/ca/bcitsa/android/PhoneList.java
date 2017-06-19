package ca.bcitsa.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PhoneList extends FragmentActivity {

	org.w3c.dom.Document doc;
	ListView listview;;
	List<HashMap<String,String>> officeDataCollection = 
	           new ArrayList<HashMap<String,String>>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonebook);
        listview = (ListView) findViewById(R.id.listview2);

    	try {
        	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        	doc = docBuilder.parse (getAssets().open("officedata.xml"));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	getXML();
    		
//        listview.setAdapter(new PhoneAdapter(this, new String[] { "data1",
//                "data2" }));
    }
    
    
    public void getXML(){
    	doc.getDocumentElement().normalize();
        
    	NodeList officeList = doc.getElementsByTagName("officedata");
    	            
    	HashMap<String,String> map = null;
    	int length = officeList.getLength();
    	            
    	for (int i = 0; i < length ; i++) {
    	             
    	    map = new HashMap<String,String>(); 
    	               
    	    Node firstOfficeNode = officeList.item(i);
    	               
    	       if(firstOfficeNode.getNodeType() == Node.ELEMENT_NODE){

    	         Element firstOfficeElement = (Element)firstOfficeNode;
    	             //-------
    	         NodeList idList = firstOfficeElement.getElementsByTagName("id");
    	         Element firstIdElement = (Element)idList.item(0);
    	         NodeList textIdList = firstIdElement.getChildNodes();
    	         //--id
    	         map.put("id", ((Node)textIdList.item(0)).getNodeValue().trim());
    	                    
    	         //2.-------
    	         NodeList cityList = firstOfficeElement.getElementsByTagName("officename");
    	         Element firstCityElement = (Element)cityList.item(0);
    	         NodeList textCityList = firstCityElement.getChildNodes();
    	         //--city
    	         map.put("officename", ((Node)textCityList.item(0)).getNodeValue().trim());
    	                        
    	         //3.-------
    	         NodeList tempList = firstOfficeElement.getElementsByTagName("phone");
    	         Element firstTempElement = (Element)tempList.item(0);
    	         NodeList textTempList = firstTempElement.getChildNodes();
    	         //--temperature
    	         map.put("phone", ((Node)textTempList.item(0)).getNodeValue().trim());
    	                    
    	         //4.-------
    	         NodeList condList = firstOfficeElement.getElementsByTagName("email");
    	         Element firstCondElement = (Element)condList.item(0);
    	         NodeList textCondList = firstCondElement.getChildNodes();
    	         //--Office condition
    	         map.put("email", ((Node)textCondList.item(0)).getNodeValue().trim());
    	         officeDataCollection.add(map);
    	         PhoneAdapter bindingData = new PhoneAdapter(this, officeDataCollection);
    	         listview.setAdapter(bindingData);  
    	       }
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
	 
    protected void onListItemClick (ListView l, View v, int position, long id){

    }

    public void call_phone(View vi){
      Button bt_phone = (Button) vi.findViewById(R.id.Button_Phone);
      Intent callIntent = new Intent(Intent.ACTION_CALL);
  	  Toast.makeText(this, "Calling.." + bt_phone.getHint().toString(), Toast.LENGTH_LONG).show();
      callIntent.setData(Uri.parse("tel:"+bt_phone.getHint().toString()));
      startActivity(callIntent);
   }
    
    public void send_email(View vi){
        Button bt_email = (Button) vi.findViewById(R.id.Button_email);
        Intent intent = new Intent(Intent.ACTION_SEND); // it's not ACTION_SEND
        intent.setType("message/rfc822");
        String email_addr = bt_email.getContentDescription().toString();
 //       intent.setData(Uri.parse("mailto:" + email_addr)); // or just "mailto:" for blank
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email_addr});
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
    	Toast.makeText(this, "Sending Email to " + email_addr, Toast.LENGTH_LONG).show();
    	startActivity(Intent.createChooser(intent, "Choose an Email client :"));        
  	
    }

  }
