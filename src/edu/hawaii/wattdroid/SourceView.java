package edu.hawaii.wattdroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import edu.hawaii.wattdroid.utils.XmlSourceDetailHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Displays detailed information about a source.
 * 
 * @author George Lee
 *
 */
public class SourceView extends Activity {
  
  private Bundle source = new Bundle();
  private Bundle sourceProperties = new Bundle();
  protected static final String INVALID_COORDINATES = "0,0,0";
  
  /**
   * Called when the activity is started.
   */
  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.sources);

    //Retrieve the passed in source and load the extra data if available.
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      this.source = (Bundle) extras.getBundle("source");
      try {
        this.loadDetails(new URL(this.source.getString("Href")));
      }
      catch (MalformedURLException e) {
        // If the URL is malformed, log and continue.
        Log.w("wattdroid", "Encountered bad url " + this.source.get("Href"));
      }
      catch (ParserConfigurationException e) {
        //Shouldn't happen, but log it anyway.
        Log.e("wattdroid", "Misconfigured parser.");
      }
      catch (SAXException e) {
        Log.w("wattdroid", "Received invalid XML");
      }
      catch (IOException e) {
        Log.w("wattdroid", "Lost connection while getting data.");
      }
    }
  }

  /**
   * Loads the detailed source information from the source url.
   * 
   * @param url The url path to load.
   * @throws SAXException if there is an error while parsing.
   * @throws ParserConfigurationException if the parser was misconfigured.
   * @throws IOException if we lose connection while getting data.
   */
  private void loadDetails(URL url) throws ParserConfigurationException, SAXException, IOException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();

    // Set up the XML reader.
    XMLReader reader = parser.getXMLReader();
    XmlSourceDetailHandler handler = new XmlSourceDetailHandler(this.source);
    reader.setContentHandler(handler);

    reader.parse(new InputSource(url.openStream()));
    
    //Retrieve parsed data.
    this.source = handler.getSource();
    this.sourceProperties = handler.getSourceProperties();
    
    //Update the view.
    updateView();
  }

  /**
   * Updates the view based on the data in the source.
   */
  private void updateView() {
    TextView nameView = (TextView)this.findViewById(R.id.sourcename);
    TextView locationView = (TextView)this.findViewById(R.id.location);
    TextView descriptionView = (TextView)this.findViewById(R.id.description);
    Button map = (Button) this.findViewById(R.id.map);
    
    nameView.setText(this.source.getString("Name"));
    locationView.setText(this.source.getString("Location"));
    descriptionView.setText(this.source.getString("Description"));
    
    //Check if we have valid coordinates before setting the click handler.
    if (!this.source.getString("Coordinates").equals(INVALID_COORDINATES)) {
      map.setOnClickListener(new OnClickListener() {
        public void onClick(View view) {
          Intent mapView = new Intent(view.getContext(), SourceMapView.class);
          mapView.putExtra("source", source);
          startActivity(mapView);
        }
      });
    }
  }
  
  @Override
  public void onStop() {
    super.onStop();
  }
}


