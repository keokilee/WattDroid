package edu.hawaii.wattdroid.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Handles the events that occur while the SAX parser parses the source list data.
 * 
 * @author George Lee
 *
 */
public class XmlSourceListHandler extends DefaultHandler {
  /**The list of sources.*/
  private List<Bundle> sourceList = new ArrayList<Bundle>();

  /**
   * Gets the source list.
   * 
   * @return List of sources in Dictionary format.
   */
  public List<Bundle> getSourceList() {
    return this.sourceList;
  }
  
  /**
   * Called when the document is started.  In this case, we will just clear the source list.
   */
  @Override
  public void startDocument() throws SAXException {
    sourceList.clear();
  }
  
  /**
   * Called when an opening XML tag is encountered.
   * 
   * @param namespaceURI the namespace of the element.
   * @param localName the local name of the element.
   * @param qName the qualified name of the element.
   * @param attrs the attributes of the element.
   */
  @Override
  public void startElement(String namespaceURI, String localName, String qName, Attributes attrs) 
              throws SAXException {
    
    if (localName.equals("SourceRef")) {
      //We encountered a new source.
      Log.d("wattdroid", "Found new source.");
      Bundle source = new Bundle();
      
      //Need to iterate over attributes and add them to the list.
      for (int i = 0; i < attrs.getLength(); i++) {
        source.putString(attrs.getLocalName(i), attrs.getValue(i));
      }
      
      this.sourceList.add(source);
    }
  }
}
