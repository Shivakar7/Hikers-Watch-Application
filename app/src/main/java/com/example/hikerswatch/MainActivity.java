package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    public void startListening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
             locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
             Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if(lastKnownLocation != null) {
                 updateLocationInfo(lastKnownLocation);
             }
        }
    }

    public void updateLocationInfo(Location location){
        TextView latitudeTextView = findViewById(R.id.latitudeTextView);
        TextView longitudeTextView = findViewById(R.id.longitudeTextView);
        TextView accuracyTextView = findViewById(R.id.accuracyTextView);
        TextView altitudeTextView = findViewById(R.id.altitudeTextView);
        TextView addressTextView = findViewById(R.id.addressTextView);

        latitudeTextView.setText("Latitude : " + Double.toString(location.getLatitude()));
        longitudeTextView.setText("Longitude : " + Double.toString(location.getLongitude()));
        accuracyTextView.setText("Altitude : " + Double.toString(location.getAltitude()));
        accuracyTextView.setText("Accuracy : " + Double.toString(location.getAccuracy()));

        String address = "Could not find address";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try{
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//            address = addressList.get(0).toString();

            if(addressList!=null && addressList.size()>0){
                address = "Address : ";
            }
            if(addressList.get(0).getThoroughfare()!=null){
               address += addressList.get(0).getThoroughfare().toString()+ ", ";
            }
            if(addressList.get(0).getLocality()!=null){
                address += addressList.get(0).getLocality().toString()+ ", ";
            }
            if(addressList.get(0).getSubAdminArea()!=null){
                address += addressList.get(0).getSubAdminArea().toString()+ ", ";
            }
            if(addressList.get(0).getAdminArea()!=null){
                address += addressList.get(0).getAdminArea().toString()+ "-";
            }
            if(addressList.get(0).getPostalCode()!=null){
                address += addressList.get(0).getPostalCode().toString();
            }


            addressTextView.setText(address);
//            Log.i("Address: " , address);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}