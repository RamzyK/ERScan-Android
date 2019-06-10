package com.example.ramzy.er_scan.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private static SharedPreferences instance;

    public static SharedPreferences getInstance(Activity ctx) {
        if (instance == null) {
            instance = ctx.getSharedPreferences("erPrefs", Context.MODE_PRIVATE);
        }
        return instance;
    }
}
