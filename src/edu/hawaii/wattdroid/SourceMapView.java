package edu.hawaii.wattdroid;

import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

public class SourceMapView extends MapActivity {
  /**The current source.*/
  private Bundle source;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map);
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      this.source = (Bundle)extras.getBundle("source");

      // Set up the map view.
      MapView mapView = (MapView) findViewById(R.id.mapview);
      mapView.setBuiltInZoomControls(true);

      List<Overlay> mapOverlays = mapView.getOverlays();
      Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
      MapOverlay overlay = new MapOverlay(drawable);

      // Extract the coordinates from the bundle extras.
      String coordinates = this.source.getString("Coordinates");
      Log.d("wattdroid", "Processing coordinates \"" + coordinates + "\"");
      if (coordinates != null && !coordinates.equals(SourceView.INVALID_COORDINATES)) {
        //Parse the coordinate string, which is of the format "0,0,0"
        String[] tokens = coordinates.split(",");
        double latitude = Double.parseDouble(tokens[0]) * 1e6;
        double longitude = Double.parseDouble(tokens[1]) * 1e6;
        GeoPoint point = new GeoPoint((int)latitude, (int)longitude);
        Log.d("wattdroid", "Created point with coordinates (" + point.getLatitudeE6() + 
                           "," + point.getLongitudeE6() + ")");
        OverlayItem item = new OverlayItem(point, this.source.getString("Name"), this.source.getString("Location"));
        overlay.setOverlay(item);
        mapOverlays.add(overlay);
        
        MapController controller = mapView.getController();
        controller.setCenter(point);
        controller.setZoom(13);
      }
    }
  }

  /**
   * Not used by this application.
   */
  @Override
  protected boolean isRouteDisplayed() {
    return false;
  }
}