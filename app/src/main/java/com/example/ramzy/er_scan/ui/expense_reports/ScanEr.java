package com.example.ramzy.er_scan.ui.expense_reports;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ramzy.er_scan.BaseActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ImageDTO;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ErService;
import com.shuhart.stepview.StepView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanEr extends BaseActivity {
    private int EXTERNAL_STORAGE_PERMISSION = 1;

    private Bitmap bitmap;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    private ErService erService;
    private Uri fileUri;
    private File destination = null;
    // Step view
    @BindView(R.id.step_view)
    StepView stepView;

    // Back button
    @BindView(R.id.stepper_back_button)
    Button backBtn;

    @OnClick(R.id.stepper_back_button)
    public void beckPress(){
        this.stepView.go(stepView.getCurrentStep() - 1, true);
    }


    @OnClick(R.id.stepper_next_button)
    public void nextPress(){
        this.stepView.go(stepView.getCurrentStep() + 1, true);

        if(this.stepView.getCurrentStep() == 0){
            expense_vat_et.setEnabled(true);
            expense_vat_et.requestFocus();
        } else if(this.stepView.getCurrentStep() == 1){
            expense_place_et.setEnabled(true);
            expense_place_et.requestFocus();
        } else if(this.stepView.getCurrentStep() == 2){
            expense_image.setClickable(true);
            LinearLayout ll = findViewById(R.id.back_and_next_buttonList_ll);
            ll.setVisibility(View.INVISIBLE);
        }
    }

    // Submit button
    @BindView(R.id.submit_er_button)
    Button submitErButton;

    @OnClick(R.id.submit_er_button)
    public void submit(){
        sendEr();
    }

    // Price EditText
    @BindView(R.id.expense_report_price_et)
    EditText expense_price_et;

    // VAT EditText
    @BindView(R.id.expense_report_vat_et)
    EditText expense_vat_et;

    // Place EditText
    @BindView(R.id.expense_report_place_et)
    EditText expense_place_et;

    // ER image
    @BindView(R.id.expense_report_image)
    ImageView expense_image;

    @OnClick(R.id.expense_report_image)
    public void chooseImage(){
        if(this.stepView.getCurrentStep() == 3){
            askMediaPermission();
            submitErButton.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "You cannot choose image step is not here yet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_er_activity);
        ButterKnife.bind(this);
        this.setToolBar(R.id.adder_activity_toolbar,
                R.id.drawer_layout,
                R.id.nav_view_add_er_activity, this);

        setStepper();
        submitErButton.setVisibility(View.INVISIBLE);
    }


    private void askMediaPermission() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("Media access needed")
                        .setMessage("We need this permission to let you take a foto of your receive.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ScanEr.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.ACCESS_COARSE_LOCATION}, EXTERNAL_STORAGE_PERMISSION);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                onBackPressed();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(ScanEr.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, EXTERNAL_STORAGE_PERMISSION);

            }
        } else {
            showChoiceDialog();
        }

    }


    public void showChoiceDialog(){
        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                } else if (options[item].equals("Choose From Gallery")) {
                    dialog.dismiss();
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void sendEr(){
        erService = NetworkProvider.getClient().create(ErService.class);

        int price = Integer.valueOf(expense_price_et.getText().toString());
        int vat = Integer.valueOf(expense_vat_et.getText().toString());
        String address = expense_place_et.getText().toString();


        ExpenseReportDTO newExpense = new ExpenseReportDTO(price, vat, address, "");
        String token = pref.getString("token", "");


        uploadFile(this.fileUri, this.destination, newExpense, token);
    }

    private void setStepper(){
        stepView = findViewById(R.id.step_view);
        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleRadius(getResources().getDimensionPixelSize(R.dimen.dp14))
                .selectedStepNumberColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .selectedCircleColor(ContextCompat.getColor(this, R.color.circle_background_color))
                .steps(new ArrayList<String>() {{
                    add("ER price");
                    add("ER vat");
                    add("ER place");
                    add("Insert ER picture");
                }})
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .stepLineWidth(getResources().getDimensionPixelSize(R.dimen.dp1))
                .textSize(getResources().getDimensionPixelSize(R.dimen.sp14))
                .stepNumberTextSize(getResources().getDimensionPixelSize(R.dimen.sp16))
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showChoiceDialog();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");

                //uploadFile(fileUri, destination);
                this.fileUri = selectedImage;
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                expense_image.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());

                //uploadFile(fileUri, destination);
                this.fileUri = selectedImage;
                FileOutputStream fo;
                expense_image.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadFile(Uri fileUri, File destination, final ExpenseReportDTO expense, final String token) {
        // create upload service client
        erService = NetworkProvider.getClient().create(ErService.class);

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
        Call<ImageDTO> call = erService.upload(description, body);
        call.enqueue(new Callback<ImageDTO>() {
            @Override
            public void onResponse(Call<ImageDTO> call,
                                   Response<ImageDTO> response) {
                String external_image_url = response.body().getUrl();
                expense.setImageID(external_image_url);
                submitExpenseReport(expense, token);
            }

            @Override
            public void onFailure(Call<ImageDTO> call, Throwable t) {

            }
        });
    }

    private void submitExpenseReport(ExpenseReportDTO expense, String token){
        Call<Response<String>> call = erService.submitExpense(expense, token);
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                Snackbar.make(submitErButton,"Expense successfully submitted :)" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                Snackbar.make(submitErButton,"Upload error" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
