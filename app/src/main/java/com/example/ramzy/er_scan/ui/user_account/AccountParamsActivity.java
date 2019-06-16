package com.example.ramzy.er_scan.ui.user_account;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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

import com.example.ramzy.er_scan.HomeActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.preferences.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountParamsActivity extends AppCompatActivity {
    private int NOTIFICATION_PERMISSION = 1;
    private SharedPreferences pref;

    @BindView(R.id.user_id_tv)
    TextView userIdTv;

    @BindView(R.id.user_firstname_tv)
    TextView userFirstnameTv;

    @BindView(R.id.user_lastname_tv)
    TextView userLastnameTv;

    @BindView(R.id.notification_switch_button)
    Switch notificationSwitch;


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

        requestNotificationPermission();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountParamsActivity.this, HomeActivity.class);
        startActivity(i);

    }


    private void requestNotificationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location needed")
                        .setMessage("Your location is needed only to show your expense reports on the map")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AccountParamsActivity.this,
                                        new String[] {Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION);
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
                        new String[] {Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION);
            }
        } else {
            Toast.makeText(AccountParamsActivity.this, "Notification permission already granted",
                    Toast.LENGTH_SHORT).show();
            notificationSwitch.setChecked(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == NOTIFICATION_PERMISSION)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notificationSwitch.setChecked(true);
            }
        }
    }

}
