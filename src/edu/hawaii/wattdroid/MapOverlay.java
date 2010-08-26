package edu.hawaii.wattdroid;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Class for overlaying an item on a map.
 * @author George Lee
 *
 */
public class MapOverlay extends ItemizedOverlay<OverlayItem> {
  
  /**The current item*/
  private OverlayItem item;
  
  /**Context of the current application.*/
  private Context context;
  
  public MapOverlay(Drawable defaultMarker) {
    super(boundCenterBottom(defaultMarker));
  }
  
  public MapOverlay(Drawable defaultMarker, Context context) {
    super(defaultMarker);
    this.context = context;
  }
  
  @Override
  protected boolean onTap(int index) {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this.context);
    dialog.setTitle(this.item.getTitle());
    dialog.setMessage(this.item.getSnippet());
    dialog.show();

    return true;
  }

  @Override
  protected OverlayItem createItem(int i) {
    // Will simply return the current item.
    return this.item;
  }

  @Override
  public int size() {
    //If item is not set, return 0.
    if (this.item == null) {
      return 0;
    }
    else {
      return 1;
    }
  }

  /**
   * Sets the current overlay item.
   * 
   * @param item The new item to set.
   */
  public void setOverlay(OverlayItem item) {
    this.item = item;
    populate();
  }

}
