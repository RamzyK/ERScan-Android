package com.example.ramzy.er_scan.ui.user_history_er;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ExpenseReportResponseDTO;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ErService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryMapUser extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private int FINE_LOCATION_PERMISSION_CODE = 1;

    private GoogleMap mMap;
    private ImageView back_btn;
    private ErService erService;
    protected SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_map_user);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pref = SharedPrefs.getInstance(this);

        back_btn = findViewById(R.id.back_to_last_page_btn);
        back_btn.setOnClickListener(this);
    }

    private void requestFineLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location needed")
                        .setMessage("Your location is needed only to show your expense reports on the map")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(HistoryMapUser.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION}, FINE_LOCATION_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(HistoryMapUser.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_CODE);

            }
        } else {
            Toast.makeText(HistoryMapUser.this, "Position permission already granted",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void getUserExpenses() {
        String token = pref.getString("token", "null");
        if (token != "null") {
            erService = NetworkProvider.getClient().create(ErService.class);

            Call<ExpenseReportResponseDTO> erReponse = erService.getUserExpenseReports(token);
            erReponse.enqueue(new Callback<ExpenseReportResponseDTO>() {
                @Override
                public void onResponse(Call<ExpenseReportResponseDTO> call, Response<ExpenseReportResponseDTO> response) {
                    ExpenseReportDTO[] address_list = new ExpenseReportDTO[response.body().getErList().length];

                    for (int i = 0; i < response.body().getErList().length; i++) {
                        address_list[i] = response.body().getErList()[i];
                    }
                    getPositionFromAdress(address_list);
                }

                @Override
                public void onFailure(Call<ExpenseReportResponseDTO> call, Throwable t) {

                }
            });
        }


    }

    private void getPositionFromAdress(ExpenseReportDTO[] address_list) {
        Geocoder geocoder = new Geocoder(this);
        double longitude = 0.0;
        double latitude = 0.0;

        for (ExpenseReportDTO er : address_list) {
            try {
                longitude = geocoder.
                        getFromLocationName(er.getAddress(), 1)
                        .get(0)
                        .getLongitude();
                latitude = geocoder.
                        getFromLocationName(er.getAddress(), 1)
                        .get(0)
                        .getLatitude();
            } catch (IOException e) {
                e.printStackTrace();
            }
            showPinsOnMap(er,longitude, latitude);
        }
    }


    private void showPinsOnMap(ExpenseReportDTO er, double longitude, double latitude) {
        if (mMap != null) {
            LatLng erLocation = new LatLng(latitude, longitude);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(erLocation)
                    .title(pref.getString("firstname", "null")
                            + ", "
                            + pref.getString("lastname", "null"))
                    .snippet("Expense : " + er.getPrice() + "€ " + "VAT: " + er.getVat() + "%"));

            marker.showInfoWindow();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (pref.getBoolean("firstTimeMapOpened", true)) {
            requestFineLocationPermission();
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            getUserPosition(lm);
            getUserExpenses();
        }
    }


    private void getUserPosition(LocationManager lm) {
        if (ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        LatLng erLocation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(erLocation)
                .title("Employee's expense report"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_last_page_btn:
                onBackPressed();
                break;
            default:
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                pref.edit().putBoolean("firstTimeMapOpened", false).apply();
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                getUserPosition(lm);
                getUserExpenses();
            }
        }
    }
}
