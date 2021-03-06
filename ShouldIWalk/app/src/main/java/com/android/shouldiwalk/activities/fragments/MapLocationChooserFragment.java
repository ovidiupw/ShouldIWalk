package com.android.shouldiwalk.activities.fragments;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.activities.ShouldIWalkActivity;
import com.android.shouldiwalk.core.exceptions.InvalidArgumentException;
import com.android.shouldiwalk.utils.ToastUtils;
import com.android.shouldiwalk.utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

public class MapLocationChooserFragment
        extends ShouldIWalkFragment
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    public static final String INITIAL_LOCATION = "DefaultLocation";
    private static final String CLASS_TAG = MapLocationChooserFragment.class.getCanonicalName() + "-TAG";

    private MapView mapView;
    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private LatLng mapMarkerLatLng;

    private OnMapMarkerLocationChangeListener mapMarkerLocationChangeListener;
    private LatLng initialLocation;

    public interface OnMapMarkerLocationChangeListener {
        void onMarkerPositionChanged(String sourceFragment, LatLng position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mapMarkerLocationChangeListener = (OnMapMarkerLocationChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMapMarkerLocationChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.fragmentId = getArguments().getString(FRAGMENT_ID);
        this.initialLocation = getArguments().getParcelable(INITIAL_LOCATION);

        if (this.fragmentId == null) {
            throw new InvalidArgumentException("The fragment id must be supplied as argument!");
        }
        if (this.initialLocation == null) {
            throw new InvalidArgumentException("The initial location must be supplied as argument!");
        }

        if (savedInstanceState != null) {
            mapMarkerLatLng = savedInstanceState.getParcelable(INITIAL_LOCATION);
        }

        View rootView = inflater.inflate(R.layout.map_fragment, container, false);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        if (!Util.isRoboUnitTest()) {
            /* Desperate attempt to get rid of exception thrown if robolectric test by the method below */
            mapView.onCreate(savedInstanceState);
        }
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(this.getContext(), Toast.LENGTH_LONG, e.getMessage());
        }

        initializeMap();
        buildGoogleApiClient();

        return rootView;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastUtils.showToast(this.getActivity(), Toast.LENGTH_LONG, connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        ToastUtils.showToast(this.getActivity(), Toast.LENGTH_LONG,
                "Cannot contact GMaps servers. Check your internet connection!");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(INITIAL_LOCATION, mapMarkerLatLng);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView == null)
            return;
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView == null)
            return;
        mapView.onPause();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView == null)
            return;
        mapView.onDestroy();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView == null)
            return;
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView == null)
            return;
        mapView.onLowMemory();
    }

    private void initializeMap() {
        final MapLocationChooserFragment that = this;

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                that.googleMap = googleMap;

                gotoDefaultLocationOnMap(googleMap);
                initializeMapInteractions(googleMap);
            }
        });
    }

    private void gotoDefaultLocationOnMap(GoogleMap googleMap) {
        if (mapMarkerLatLng != null) {
            initialLocation = mapMarkerLatLng;
        }
        CameraPosition cameraPosition
                = new CameraPosition.Builder().target(initialLocation).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        addMarkerAtPosition(initialLocation, googleMap);
    }

    private void addMarkerAtPosition(LatLng latLng, GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();

        if (((ShouldIWalkActivity) getActivity()).isNetworkConnectionAvailable()) {
            Geocoder geocoder = new Geocoder(this.getActivity(), Locale.ENGLISH);
            try {
                String crtAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0).getAddressLine(0);
                markerOptions.title(crtAddress);
                Log.d(CLASS_TAG, String.format("User added marker for address {%s} at location {%s}", crtAddress, latLng));
            } catch (IOException e) {
                ToastUtils.showToast(this.getActivity(), Toast.LENGTH_LONG, e.getMessage());
            }
        }

        markerOptions.position(latLng);
        googleMap.clear();
        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();

        this.mapMarkerLatLng = latLng;
        this.mapMarkerLocationChangeListener.onMarkerPositionChanged(fragmentId, latLng);
    }

    private void initializeMapInteractions(final GoogleMap googleMap) {
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarkerAtPosition(latLng, googleMap);
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addMarkerAtPosition(latLng, googleMap);
            }
        });
    }

    private synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    @VisibleForTesting
    LatLng getMapMarkerLatLng() {
        return mapMarkerLatLng;
    }

    @VisibleForTesting
    void setMapMarkerLatLng(LatLng mapMarkerLatLng) {
        this.mapMarkerLatLng = mapMarkerLatLng;
    }
}
