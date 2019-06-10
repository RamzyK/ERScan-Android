package com.example.ramzy.er_scan;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ramzy.er_scan.dto.LoginDTO;
import com.example.ramzy.er_scan.dto.UserDTO;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.UserService;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {
    private UserService userService;
    private SharedPreferences pref;
    private long currentDate;
    private long lastLoginDate;

    @BindView(R.id.email_et)
    EditText email_et;

    @BindView(R.id.password_et)
    EditText password;

    @BindView(R.id.sign_in_btn)
    Button signInButton;

    @OnClick(R.id.sign_in_btn)
    public void signIn(){
        if(checkParams()){
            logUser(email_et.getText().toString(), password.getText().toString());
        }else{
            Snackbar.make(signInButton,"Enter a coorect email please" , Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slpash_screen_layout);
        ButterKnife.bind(this);
        pref =  SharedPrefs.getInstance(this);

        lastLoginDate = pref.getLong("last_login_date", 0);
        currentDate = System.currentTimeMillis();

        if((currentDate - lastLoginDate) < 86400000){  // Si l'utilisateur a quitté l'app depuis plus de 24h
            pref.edit().putLong("last_login_date", currentDate).apply();
            Intent stillLoggedIn = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(stillLoggedIn);
        }

    }

    private void logUser(String email, String password) {
        userService = NetworkProvider.getClient().create(UserService.class);
        LoginDTO login = new LoginDTO(email, password);

        Call<UserDTO> userReponseCall = userService.login(login);
        userReponseCall.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        pref.edit().putString("firstname", response.body().getFirstname()).apply();
                        pref.edit().putString("lastname", response.body().getLastname()).apply();
                        pref.edit().putString("token", response.body().getToken()).apply();
                        pref.edit().putString("idUser", response.body().get_id()).apply();
                        pref.edit().putString("email", response.body().getEmail()).apply();
                        pref.edit().putString("idCompany", response.body().getId_company()).apply();
                        pref.edit().putLong("last_login_date", currentDate).apply();

                        Intent showHomePage = new Intent(SplashScreenActivity.this, HomeActivity.class);
                        startActivity(showHomePage);
                    }
                }else{
                    // Do a switch case for all cases
                    String error;
                    switch (response.code()){
                        case 400:
                            error = "You cannot login from this type of device";
                            break;
                        case 401:
                            error = "You have not been approved yet";
                            break;
                        case 404:
                            error = "No account with these credentials";
                            break;
                        default:
                            error = "Error server";
                            break;
                    }
                    Snackbar.make(signInButton,error , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(SplashScreenActivity.this, "Something went wring, try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkParams() {
        Boolean checked = false;

        if(!email_et.getText().toString().isEmpty() && isEmailValid(email_et.getText().toString())){
            if(!password.getText().toString().isEmpty() && password.getText().length() > 4){
                checked = true;
            }
        }
        return checked;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quit ER-Scan ?")
                .setMessage("You are going to quit the app, is that what you want ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();

    }
}
