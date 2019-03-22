package com.example.ramzy.er_scan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class AccountParamsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_params_layout);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountParamsActivity.this, HomeActivity.class);
        startActivity(i);

    }
}
