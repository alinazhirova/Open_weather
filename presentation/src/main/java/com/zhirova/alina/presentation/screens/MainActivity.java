package com.zhirova.alina.presentation.screens;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.zhirova.alina.local.database.CityContract;
import com.zhirova.alina.local.database.DatabaseApi;
import com.zhirova.alina.local.database.DatabaseHelper;
import com.zhirova.alina.presentation.R;
import com.zhirova.alina.presentation.screens.start.StartPresenter;


public class MainActivity extends AppCompatActivity {

    private final String PREFS_NAME = "COORDINATES";
    private final String BUNDLE_LOCATION_LAT = "CUR_POSITION_LATITUDE";
    private final String BUNDLE_LOCATION_LONG = "CUR_POSITION_LONG";
    private final int PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 0;
    private View mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        showPermissionPreview();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPermissionPreview();
            } else {
                setContentView(R.layout.activity_main_understudy);
                mainLayout = findViewById(R.id.container_understudy);
                Snackbar.make(mainLayout, R.string.location_permission_denied,
                        Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    private void showPermissionPreview() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationClient = LocationServices
                    .getFusedLocationProviderClient(this);
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME,
                                MODE_PRIVATE).edit();
                            editor.putString(BUNDLE_LOCATION_LAT, String.valueOf(location.getLatitude()));
                            editor.putString(BUNDLE_LOCATION_LONG, String.valueOf(location.getLongitude()));
                            editor.apply();
                            setContentView(R.layout.activity_main);
                            mainLayout = findViewById(R.id.container);
                        })
                    .addOnFailureListener(e -> {
                        Log.e("ERROR", "Error trying to get last GPS location!");
                        setContentView(R.layout.activity_main_understudy);
                        mainLayout = findViewById(R.id.container_understudy);
                        Snackbar.make(mainLayout,
                                getResources().getString(R.string.location_error),
                                Snackbar.LENGTH_LONG);
                    });
        } else {
            requestLocationPermission();
        }
    }


    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            setContentView(R.layout.activity_main_understudy);
            mainLayout = findViewById(R.id.container_understudy);

            Snackbar.make(mainLayout, R.string.location_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok,
                    view -> ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_ACCESS_COARSE_LOCATION)).show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }


    public void onSettingsClick(View view) {
        EditText editTextSettings = findViewById(R.id.edit_text_settings);
        String delayString = String.valueOf(editTextSettings.getText());

        if (delayString.isEmpty()) {
            Snackbar.make(mainLayout, R.string.empty, Snackbar.LENGTH_SHORT).show();
        } else {
            if (NumberUtils.isNumeric(delayString)) {
                StartPresenter.delay = Long.parseLong(delayString);
                editTextSettings.clearFocus();

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Snackbar.make(mainLayout, R.string.integers, Snackbar.LENGTH_SHORT).show();
            }
        }
    }


}
