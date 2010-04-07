package edu.hawaii.wattdroid;


import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * ListView  is a ViewGroup  that creates a list of scrollable items.
 * The list items are automatically inserted to the list using a ListAdapter.
 * http://developer.android.com/resources/tutorials/views/hello-listview.html
 * Code adapted from:
 * http://www.warriorpoint.com/blog/2009/07/19/android-simplified-source-
 * code-for-parsing-and-working-with-xml-data-and-web-services-in-android/
 */
public class WattDroid extends ListActivity {
	static final String[] SOURCES = new String[] {"bob","mark"};
	private final String MY_DEBUG_TAG = "WeatherForcaster"; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

         /* Create a new TextView to display the parsingresult later. */
         TextView tv = new TextView(this);
    	
         try {
              /* Create a URL we want to load some xml-data from. */
              URL url = new URL("http://server.wattdepot.org:8182/wattdepot/sources.xml");

              /* Get a SAXParser from the SAXPArserFactory. */
              SAXParserFactory spf = SAXParserFactory.newInstance();
              SAXParser sp = spf.newSAXParser();

              /* Get the XMLReader of the SAXParser we created. */
              XMLReader xr = sp.getXMLReader();
              /* Create a new ContentHandler and apply it to the XML-Reader*/
              ExampleHandler myExampleHandler = new ExampleHandler();
              xr.setContentHandler(myExampleHandler);
              
              /* Parse the xml-data from our URL. */
              xr.parse(new InputSource(url.openStream()));
              /* Parsing has finished. */

              /* Our ExampleHandler now provides the parsed data to us. */
              ParsedExampleDataSet parsedExampleDataSet =
                                            myExampleHandler.getParsedData();

              /* Set the result to be displayed in our GUI. */
              tv.setText(parsedExampleDataSet.toString());
              String[] sources = parsedExampleDataSet.getAllSources();
              Log.d("wattdroid", "I just placed sources into an array");
              setListAdapter(new ArrayAdapter<String>(this, R.layout.item, sources ));
              
         } catch (Exception e) {
              /* Display any Error to the GUI. */
              tv.setText("Error: " + e.getMessage());
              Log.e(MY_DEBUG_TAG, "wattdroid", e);
         }
         /* Display the TextView. */
         //this.setContentView(tv);
    }
    

    
}