package helpers;

import android.support.v4.app.FragmentActivity;

import com.android.shouldiwalk.activities.fragments.MapLocationChooserFragment;
import com.google.android.gms.maps.model.LatLng;

import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

public class MapLocationChooserFragmentUtil extends FragmentActivity
        implements MapLocationChooserFragment.OnMapMarkerLocationChangeListener {

    private String lastSourceFragment;
    private LatLng lastPosition;

    @Override
    public void onMarkerPositionChanged(String sourceFragment, LatLng position) {
        this.lastSourceFragment = sourceFragment;
        this.lastPosition = position;
    }

    public String getLastSourceFragment() {
        return lastSourceFragment;
    }

    public LatLng getLastPosition() {
        return lastPosition;
    }
}
