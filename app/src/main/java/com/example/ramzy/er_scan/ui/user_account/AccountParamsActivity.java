package com.example.ramzy.er_scan.ui.user_account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramzy.er_scan.HomeActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.Tools;
import com.example.ramzy.er_scan.dto.ImageDTO;
import com.example.ramzy.er_scan.dto.UserDTO;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.Constants;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ImageUploadService;
import com.example.ramzy.er_scan.services.UserService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountParamsActivity extends FragmentActivity {


    private SharedPreferences pref;
    private UserService userService;
    private ImageUploadService imageService;

    private String imgPath = null;
    private Bitmap bitmap;
    private InputStream inputStreamImg;

    private Uri fileUri;
    private File image_file = null;
    private String user_token;

    private Tools imageTools;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_params_layout);
        ButterKnife.bind(this);

        imageTools = Tools.getInstance(this);
        save_button.setVisibility(View.INVISIBLE);

        pref = SharedPrefs.getInstance(this);

        if(!pref.getString("imageID", "none").equals("none")){
            String user_picture = pref.getString("imageID", "none");
            String image_path = Constants.loacl_URL + "images/" + user_picture;
            Glide.with(this).load(image_path).into(userPicture);
        }
        userIdTv.setText(String.format("ID: %s", pref.getString("idUser", "")));
        userFirstnameTv.setText(String.format("Firstname: %s", pref.getString("firstname", "NaN")));
        userLastnameTv.setText(String.format("Lastname: %s", pref.getString("lastname", "NaN")));

        notificationSwitch.setClickable(false);

        imageTools.requestNotificationPermission(notificationSwitch);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AccountParamsActivity.this, HomeActivity.class);
        startActivity(i);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;

        if (requestCode == imageTools.PICK_IMAGE_CAMERA) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                userPicture.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                this.fileUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                imgPath = getRealPathFromURI(this.fileUri);
                image_file = new File(imgPath);
                save_button.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == imageTools.PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                imgPath = getRealPathFromURI(selectedImage);
                image_file = new File(imgPath);

                this.fileUri = selectedImage;
                userPicture.setImageBitmap(bitmap);
                save_button.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void uploadNewUserImage(Uri fileUri, File destination) {
        // create upload service client
        imageService = NetworkProvider.getClient().create(ImageUploadService.class);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        destination
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", destination.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);

        // finally, execute the request
        Call<ImageDTO> call = imageService.upload(description, body);
        call.enqueue(new Callback<ImageDTO>() {
            @Override
            public void onResponse(Call<ImageDTO> call,
                                   Response<ImageDTO> response) {
                String external_image_url = response.body().getImage_name();
                pref.edit().putString("imageID", external_image_url).apply();
                saveUserAccount(external_image_url);
            }

            @Override
            public void onFailure(Call<ImageDTO> call, Throwable t) {

            }
        });
    }


    private void saveUserAccount(String userImageName) {
        userService = NetworkProvider.getClient().create(UserService.class);
        String token = pref.getString("token", "none");

        if(!token.equals("none")){
            UserDTO userUpdate = new UserDTO();
            String user_email = pref.getString("email", "none");

            userUpdate.setEmail(user_email);
            userUpdate.setImageID(userImageName);

            Call<UserDTO> call = userService.updateUserAccountParams(userUpdate, token);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    Snackbar.make(save_button, "Your account has been updated!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Snackbar.make(save_button, "An error happened, try again later.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == imageTools.NOTIFICATION_PERMISSION)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notificationSwitch.setChecked(true);
            }
        }
    }



    // VIEW BINDING

    @BindView(R.id.user_id_tv)
    TextView userIdTv;

    @BindView(R.id.user_firstname_tv)
    TextView userFirstnameTv;

    @BindView(R.id.user_lastname_tv)
    TextView userLastnameTv;

    @BindView(R.id.notification_switch_button)
    Switch notificationSwitch;

    @BindView(R.id.user_image)
    ImageView userPicture;

    @OnClick(R.id.user_image)
    public void userImageTouched(){
        imageTools.askMediaPermission(null);
    }

    @OnClick(R.id.back_to_home_activity)
    public void backPressed(){
        onBackPressed();
    }

    @BindView(R.id.save_updates)
    Button save_button;

    @OnClick(R.id.save_updates)
    public void save_updates(){
        uploadNewUserImage(this.fileUri, this.image_file);
    }


    @OnClick(R.id.update_password_button)
    public void updateUserPassword(){
        Intent updatePass = new Intent(this, UpdateUserPasswordActivity.class);
        startActivity(updatePass);
    }

}
