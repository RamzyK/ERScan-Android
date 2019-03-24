package com.example.ramzy.er_scan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    private EditText email_et;
    private EditText password;
    private Button signInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slpash_screen_layout);

        email_et = findViewById(R.id.email_et);
        password = findViewById(R.id.password_et);
        signInButton = findViewById(R.id.sign_in_btn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email_et.getText().toString() != ""){
                    Toast.makeText(SplashScreenActivity.this, "Hello " + email_et.getText(), Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

    }
}
