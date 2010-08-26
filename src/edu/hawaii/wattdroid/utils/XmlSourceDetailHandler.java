/**
 * 
 */
package edu.hawaii.wattdroid.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import android.os.Bundle;

/**
 * Handles the events that occur while the SAX parser parses the source details.
 * 
 * @author George Lee
 *
 */
public class XmlSourceDetailHandler extends DefaultHandler {
  
  /**The source that will contain the information from the parsed XML.*/
  private Bundle source = new Bundle();
  
  /**The properties extracted from the parsed XML.*/
  private Bundle sourceProperties =  new Bundle();
  
  /**Tracks if we are in a property list or not.*/
  private boolean inProperty = false;
  
  /**The current string we are in.*/
  private String currentTag = null;
  
  /**The contents of the tag.*/
  private String currentContents = "";
  
  /**Key for the current property.*/
  private String currentKey;
  
  /**Value for the current property.*/
  private String currentValue;

  /**
   * Initializes the handler with a pre-existing source.
   * 
   * @param source The source to use to initialize the handler.
   */
  public XmlSourceDetailHandler(Bundle source) {
    this.source = source;
  }
  
  /**
   * Gets the source.
   * 
   * @return the source
   */
  public Bundle getSource() {
    return this.source;
  }
  
  /**
   * Gets the source's properties.
   * 
   * @return the source's properties.
   */
  public Bundle getSourceProperties() {
    return this.sourceProperties;
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
    
    //Ignore the following tags while parsing.
    if (localName.equals("Source") || localName.equals("Properties")) {
      return;
    }
    
    this.currentTag = localName;
    
    //Flag if we're inside of a property
    if (localName.equals("Property")) {
      this.inProperty = true;
    }
  }
  
  /**
   * Called when a closing XML tag is encountered.
   * 
   * @param namespaceURI - namespace
   * @param localName - xml tag
   * @param atts = attributes
   */
  @Override
  public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    //Ignore the following tags while parsing.
    if (localName.equals("Source") || localName.equals("Properties")) {
      return;
    }
    
    //Check if we are exiting a property.
    if (localName.equals("Property")) {
      this.inProperty = false;
    }
    
    //Finished parsing the contents of the tag.
    else if (!this.inProperty && this.source.get(localName) == null) {
      this.source.putString(this.currentTag, this.currentContents);
    }
    
    //Handle properties.
    else if (this.inProperty && this.currentTag.equals("Key")) {
      this.currentKey = this.currentContents;
    }
    
    else if (this.inProperty && this.currentTag.equals("Value")) {
      this.currentValue = this.currentContents;
    }
    
    //If both the current key and value are available, then insert into the property list.
    if (this.currentKey != null && this.currentValue != null) {
      this.sourceProperties.putString(this.currentKey, this.currentValue);
      this.currentKey = this.currentValue = null;
    }
  }
  
  /**
   * Called on the following structure: <tag>characters</tag>. Simply inserts the string into currentContents.
   * 
   * @param ch - characters in tag
   * @param start - where to start
   * @param length - how many to copy
   */
  @Override
  public void characters(char ch[], int start, int length) {
    this.currentContents = new String(ch, start, length);
  }
}
