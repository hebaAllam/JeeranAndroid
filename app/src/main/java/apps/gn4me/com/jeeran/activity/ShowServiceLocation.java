package apps.gn4me.com.jeeran.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.location.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import apps.gn4me.com.jeeran.R;

public class ShowServiceLocation extends FragmentActivity  {

    private GoogleMap mMap;
    Geocoder geocoder;
    List<Address> addresses;
    double lat;
    double lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_service_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
         mMap=mapFragment.getMap();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                lat=latLng.latitude;
                lang=latLng.longitude;


                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

            }
        });
        geocoder = new Geocoder(this, Locale.getDefault());
        String address="";
        String city="";
        String state="";
        String country="";
        String postalCode="";
        String knownName="";

        try {
            addresses = geocoder.getFromLocation(lat, lang, 1);
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             city = addresses.get(0).getLocality();
             state = addresses.get(0).getAdminArea();
             country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();// Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

         //
        Intent returnIntent = new Intent();
        returnIntent.putExtra("lat",lat+"");
        returnIntent.putExtra("long",lang+"");
        returnIntent.putExtra("address",address+"-"+city+"-"+state+"-"+country+"-"+knownName);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }



}
