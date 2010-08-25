package edu.hawaii.wattdroid.models;

import android.net.Uri;

/**
 * Class that represents a source in WattDepot.
 * 
 * @author George Lee
 */
public class Source {
  /**Source URL*/
  private Uri source;
  
  /**Description of the source.*/
  private String description;
  
  /**Location of the source.*/
  private String location;
  
  /**Coordinates of the source.*/
  private double latitude;
  private double longitude;
  
  /**Boolean that determines if the source is virtual.*/
  private boolean isVirtual;
  
  /**Boolean that determines if the source is public.*/
  private boolean isPublic;
  
  /**Owner of the source.*/
  private String owner;
  
  /**Name of the source.*/
  private String name;
  
  public Source() {
    this.source = null;
    this.description = "None";
    this.location = "None";
    this.latitude = 0;
    this.longitude = 0;
    this.isVirtual = false;
    this.isPublic = false;
    this.owner = "None";
    this.name = "None";
  }

  /**
   * Gets the source Uri.
   * @return the source
   */
  public Uri getSource() {
    return source;
  }

  /**
   * Gets the descriptioin of the source.
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Gets the location information of the source.
   * @return the location
   */
  public String getLocation() {
    return location;
  }

  /**
   * Gets the latitude of the source.
   * 
   * @return the latitude
   */
  public double getLatitude() {
    return latitude;
  }

  /**
   * Gets the longitude of the source.
   * 
   * @return the longitude
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   * Checks if the source is virtual.
   * 
   * @return true if the source is virtual, false otherwise.
   */
  public boolean isVirtual() {
    return isVirtual;
  }

  /**
   * Checks if the source is public.
   * 
   * @return True if the source is public, false otherwise.
   */
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * Returns the owner of the source.
   * 
   * @return the owner
   */
  public String getOwner() {
    return owner;
  }

  /**
   * Returns the name of the source.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }
  
}
