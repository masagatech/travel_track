package com.goyo.goyorider.forms;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.goyo.goyorider.common.GPSLocation;
import com.goyo.goyorider.R;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


public class googlemapexp extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemapexp);

       // mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
       // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
      /*  final LatLng CIU = new LatLng(35.21843892856462, 33.41662287712097);
        Marker ciu = mMap.addMarker(new MarkerOptions()
                .position(CIU).title("My Office"));*/

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        GPSLocation l = new GPSLocation(this.getApplicationContext());

        Location loc = l.getLocation();
        String lat = "";
        String lon = "";
        if (loc != null) {
            lat = "" + loc.getLatitude();
            lon = "" + loc.getLongitude();
            final LatLng CIU = new LatLng(23.072693, 72.552628);

            boundsBuilder.include( new LatLng(loc.getLatitude(), loc.getLongitude()));
            boundsBuilder.include(CIU);

            LatLngBounds bounds = boundsBuilder.build();
            //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
