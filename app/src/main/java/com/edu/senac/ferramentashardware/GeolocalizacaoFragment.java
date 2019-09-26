package com.edu.senac.ferramentashardware;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GeolocalizacaoFragment extends Fragment {
    //Permissão do GPS
    private static final int REQUEST_ACCESS_FINE_LOCATION = 200;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 200;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    private String[] permission = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private boolean permissionGPS = false;
    LocationManager locationManager;

    //Declara variável
    TextView textViewLatitude, textViewLongitude;
    Button getlocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geolocalizacao, container, false);

        //Solicita as permissões para o usuário
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_ACCESS_FINE_LOCATION);
        ActivityCompat.requestPermissions(getActivity(), permission, REQUEST_ACCESS_COARSE_LOCATION);

        //find id
        getlocation = view.findViewById(R.id.getCoordenadas);
        textViewLatitude = view.findViewById(R.id.campoLatitude);
        textViewLongitude = view.findViewById(R.id.campoLongitude);

        //onclick
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostraCoordenadas();
            }
        });
        return view;
    }

    //solicitar permissão de GPS
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ACCESS_FINE_LOCATION:
                permissionGPS = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionGPS) {
            Toast.makeText(getActivity(), "Aceite as permissões", Toast.LENGTH_SHORT).show();
        }
    }


    public void mostraCoordenadas() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new GeolocalizacaoFragment.MyLocationListener();
        //LocationManager.GPS_PROVIDER Use GPS Targeting / LocationManager.NETWORK_PROVIDER Use Network Targeting
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1,locationListener);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        //This function is triggered when the coordinates change.
        // If the Provider passes the same coordinates, it will not be triggered.
        public void onLocationChanged(Location location) {
            String longitude = Double.toString(location.getLongitude());
            String latitude = Double.toString(location.getLatitude());
            textViewLongitude.setText(longitude);
            textViewLatitude.setText(latitude);
        }

        @Override
        //Esta função é acionada a localização do GPS está desligada
        public void onProviderDisabled(String provider) {
            Toast.makeText(getContext(), "Localização está Desabilitada", Toast.LENGTH_SHORT).show();
        }

        @Override
        //This function is triggered when the Provider is enabled, such as GPS is turned on.
        public void onProviderEnabled(String provider) {
            Toast.makeText(getContext(), "Localização foi Habilitada", Toast.LENGTH_SHORT).show();
        }

        @Override
        //Provider's transition state triggers this function when the three states of available,
        // temporarily unavailable, and no service are directly switched.
        // Essa função é acionada quando há uma mudança na situação do gps
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(getContext(), "Alterado Status da Localização", Toast.LENGTH_SHORT).show();
        }
    }
}