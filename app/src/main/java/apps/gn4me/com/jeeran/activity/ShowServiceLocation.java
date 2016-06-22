package apps.gn4me.com.jeeran.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.location.Address;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import apps.gn4me.com.jeeran.R;

public class ShowServiceLocation extends FragmentActivity implements OnMapReadyCallback {
private  GoogleMap mMap;
    private  double lang;
   private double lat;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
          mapFragment.getMapAsync( this);
//        Intent i=getIntent();
//       lang=  i.getExtras().getDouble("lang");
//       lat=   i.getExtras().getDouble("lat");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng servicePlace = new LatLng(30.077024, 31.020114);
        mMap.addMarker(new MarkerOptions().position(servicePlace).title("service Place"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(servicePlace));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(servicePlace, 10);
        mMap.animateCamera(cameraUpdate);
    }
}
