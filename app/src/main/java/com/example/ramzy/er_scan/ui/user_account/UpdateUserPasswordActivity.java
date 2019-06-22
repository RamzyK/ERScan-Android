package com.example.ramzy.er_scan.ui.user_account;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.LoginDTO;
import com.example.ramzy.er_scan.dto.UserDTO;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.UserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserPasswordActivity extends FragmentActivity {

    private SharedPreferences pref;
    private UserService userService;


    @BindView(R.id.old_password_et)
    EditText oldPasswordEt;

    @BindView(R.id.new_password_et)
    EditText newPasswordEt;

    @BindView(R.id.update_user_password)
    Button updatePasswordBtn;

    @OnClick(R.id.update_user_password)
    public void updateUserPassword(){
        checkPassword();
    }

    @OnClick(R.id.back_to_account_settings)
    public void backToAccoutnActivity(){
        onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_password);
        ButterKnife.bind(this);

        pref = SharedPrefs.getInstance(this);
    }


    private void checkPassword() {
        userService = NetworkProvider.getClient().create(UserService.class);

        final String token = pref.getString("token", "none");
        final String oldUserPassword = oldPasswordEt.getText().toString();
        final String userPassword = newPasswordEt.getText().toString();
        final String userEmail = pref.getString("email", "none");

        LoginDTO login = new LoginDTO(userEmail, oldUserPassword);


        Call<Boolean> call = userService.chekUserPasswordInDb(login, token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()){
                    updateUserPassword(userEmail, userPassword, token);
                }else{
                    Snackbar.make(updatePasswordBtn, "Wrong old password, try again please!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Snackbar.make(updatePasswordBtn, "An error happened, try again later!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });


    }

    private void updateUserPassword(String userEmail, String userPassword, String token){
        LoginDTO newUserLogin = new LoginDTO(userEmail, userPassword);

        Call<UserDTO> call = userService.updateUserPassword(newUserLogin, token);
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                onBackPressed();
                Toast.makeText(UpdateUserPasswordActivity.this, "Your password has been updated\"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Snackbar.make(updatePasswordBtn, "An error happend try again later", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });
    }
}
