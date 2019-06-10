package com.example.ramzy.er_scan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramzy.er_scan.preferences.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountParamsActivity extends AppCompatActivity {
    private int LOCATION_PERMISSION_CODE = 1;
    private int NOTIFICATION_PERMISSION_CODE = 123;
    private SharedPreferences pref;

    @BindView(R.id.user_id_tv)
    TextView userIdTv;

    @BindView(R.id.user_firstname_tv)
    TextView userFirstnameTv;

    @BindView(R.id.user_lastname_tv)
    TextView userLastnameTv;

    @BindView(R.id.notification_switch_button)
    Switch notificationSwitch;

    @OnClick(R.id.notification_switch_button)
    public void notificationPermissionChanged(){

    }

    @BindView(R.id.position_switch_button)
    Switch positionSwitch;

    @OnClick(R.id.position_switch_button)
    public void positionPermissionChanged(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_params_layout);
        ButterKnife.bind(this);

        pref = SharedPrefs.getInstance(this);
        userIdTv.setText(String.format("ID: %s", pref.getString("idUser", "")));
        userFirstnameTv.setText(String.format("Firstname: %s", pref.getString("firstname", "NaN")));
        userLastnameTv.setText(String.format("Lastname: %s", pref.getString("lastname", "NaN")));

        notificationSwitch.setClickable(false);
        positionSwitch.setClickable(false);

        requestPositionPermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountParamsActivity.this, HomeActivity.class);
        startActivity(i);

    }


    private void requestPositionPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location needed")
                        .setMessage("Your location is needed only to show your expense reports on the map")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AccountParamsActivity.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                notificationSwitch.setChecked(false);
                            }
                        })
                        .create().show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            }
        } else {
            // Permission has already been granted
            Toast.makeText(AccountParamsActivity.this, "Position permission already granted",
                    Toast.LENGTH_SHORT).show();
            positionSwitch.setChecked(true);
        }
        requestNotificationPermission();
    }


    private void requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Notification GRANTED", Toast.LENGTH_SHORT).show();
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
            Toast.makeText(this, "Notification DENIED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location GRANTED", Toast.LENGTH_SHORT).show();
                positionSwitch.setChecked(true);
            } else {
                positionSwitch.setChecked(false);
                Toast.makeText(this, "Location DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
