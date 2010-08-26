package edu.hawaii.wattdroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import edu.hawaii.wattdroid.utils.XmlSourceListHandler;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast; //import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * WattDroid - Main WattDroid activity.
 * 
 * @author Remy Baumgarten
 * @author Kevin Chiogioji
 * @author George Lee
 * 
 */
public class WattDroid extends ListActivity {
  /**
   * Debug tag for log writing.
   */
  private final String MY_DEBUG_TAG = "wattdroid";
  private static final int EDIT_ID = Menu.FIRST + 2;
  private String text = null;
  //private String delay = null;
  private List<Bundle> sourceList = new ArrayList<Bundle>();

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    text = prefs.getString("text", "<undefined>");
    //delay = prefs.getString("delay", "30");

    try {
      /* Create a URL we want to load some xml-data from. */
      Log.e("wattdroid", "URL from Prefs is: " + text.toString());

      Toast
          .makeText(getApplicationContext(), prefs.getString("text", "<unset>"), Toast.LENGTH_LONG)
          .show();
      if (text.equals("<undefined>")) {
        Toast.makeText(getApplicationContext(), "Please set a URL in the Preferences",
            Toast.LENGTH_SHORT).show();
        Intent prefActivity = new Intent(this, EditPreferences.class);
        startActivity(prefActivity);
      }
      
      //Get the source list.
      retrieveSources();

      // Set up the list view.
      ListView lv = getListView();
      lv.setTextFilterEnabled(true);
      lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          Intent sourceview = new Intent(view.getContext(), SourceView.class);
          sourceview.putExtra("source", sourceList.get(position));
          startActivity(sourceview);
        }
      });

    }
    catch (Exception e) {
      Toast.makeText(getApplicationContext(), "Please set a URL in the Preferences",
          Toast.LENGTH_SHORT).show();
      Intent prefActivity = new Intent(this, EditPreferences.class);
      startActivity(prefActivity);
      Log.e(MY_DEBUG_TAG, "wattdroid", e);
    }
  }

  /**
   * Private method that retrieves the sources from the URL.
   * 
   * @throws MalformedURLException if the url is not set.
   * @throws ParserConfigurationException should not be thrown.
   * @throws SAXException if the parser encounters an error.
   * @throws IOException if we lose connection while the XML data is downloaded.
   */
  private void retrieveSources() throws MalformedURLException, ParserConfigurationException,
      SAXException, IOException {
    // Test URL: http://server.wattdepot.org:8182/wattdepot/sources.xml
    URL url = new URL(this.text);

    /* Get a SAXParser from the SAXPArserFactory. */
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();

    /* Setup the XML reader. */
    XMLReader xmlReader = parser.getXMLReader();
    XmlSourceListHandler sourceHandler = new XmlSourceListHandler();
    xmlReader.setContentHandler(sourceHandler);
    
    xmlReader.parse(new InputSource(url.openStream()));

    /*Grab the sources from the parsed data.*/
    this.sourceList = sourceHandler.getSourceList();

    //Need to iterate over the sources to just get the titles.
    List<String> names = new ArrayList<String>();
    for (Bundle source : this.sourceList) {
      names.add(source.getString("Name"));
    }
    
    Log.d("wattdroid", "I just placed sources into an array");
    setListAdapter(new ArrayAdapter<String>(this, R.layout.item, names));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, EDIT_ID, Menu.NONE, "Edit Prefs").setIcon(R.drawable.misc)
        .setAlphabeticShortcut('e');

    return (super.onCreateOptionsMenu(menu));
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case EDIT_ID:
      startActivity(new Intent(this, EditPreferences.class));
      return (true);
    }
    return (super.onOptionsItemSelected(item));
  }

  @Override
  public void onResume() {
    super.onResume();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    text = prefs.getString("text", "<unset>");
  }

}
