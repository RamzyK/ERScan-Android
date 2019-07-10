package com.example.ramzy.er_scan;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class Tools {

    public int EXTERNAL_STORAGE_PERMISSION = 1;
    public final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    public final int NOTIFICATION_PERMISSION = 1;

    private Activity context;
    private final static Tools instance = null;

    private Tools(Activity c){
        this.context = c;
    }

    public static Tools getInstance(Activity c){
        if(instance == null){
            return new Tools(c);
        }
        return instance;
    }

    public void askMediaPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(context)
                        .setTitle("Media access needed")
                        .setMessage("We need this permission to let you take a foto of your receive.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(context,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                context.onBackPressed();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);

            }
        } else {
            showChoiceDialog();
        }

    }

    public void showChoiceDialog(){
        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    context.startActivityForResult(intent, PICK_IMAGE_CAMERA);

                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    context.startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();
    }


    public void requestNotificationPermission(final Switch notificationSwitch) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
                new AlertDialog.Builder(context)
                        .setTitle("Location needed")
                        .setMessage("Your location is needed only to show your expense reports on the map")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(context,
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
                ActivityCompat.requestPermissions(context,
                        new String[] {Manifest.permission.ACCESS_NOTIFICATION_POLICY}, NOTIFICATION_PERMISSION);
            }
        } else {
            notificationSwitch.setChecked(true);
        }
    }
}
