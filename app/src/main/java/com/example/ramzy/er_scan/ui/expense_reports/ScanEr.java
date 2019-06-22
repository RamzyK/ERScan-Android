package com.example.ramzy.er_scan.ui.expense_reports;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ramzy.er_scan.BaseActivity;
import com.example.ramzy.er_scan.HomeActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.Tools;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ImageDTO;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ErService;
import com.shuhart.stepview.StepView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanEr extends FragmentActivity {
    private int EXTERNAL_STORAGE_PERMISSION = 1;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    private String current_er_type;

    private String imgPath = null;
    private Bitmap bitmap;
    private InputStream inputStreamImg;

    private ErService erService;
    private Uri fileUri;
    private File image_file = null;
    private Tools imageTools;
    private String user_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_er_activity);
        ButterKnife.bind(this);


        if(getIntent() != null){
            Intent i = getIntent();
            current_er_type = i.getExtras().getString("er-type");
            user_token = i.getExtras().getString("token");
        }

        imageTools = Tools.getInstance(this);
        setStepper();
        submitErButton.setVisibility(View.INVISIBLE);
        expense_image.setVisibility(View.INVISIBLE);
        setEnterKeyListenenrs();
    }

    private void setEnterKeyListenenrs() {

        expense_price_et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if (!expense_vat_et.isEnabled()) {
                        stepView.go(stepView.getCurrentStep() + 1, true);
                        expense_vat_et.setEnabled(true);
                        expense_vat_et.requestFocus();
                    } else {
                        Toast.makeText(ScanEr.this, String.valueOf(stepView.getCurrentStep()), Toast.LENGTH_SHORT).show();
                        expense_vat_et.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        expense_vat_et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    if (!expense_place_et.isEnabled()) {
                        stepView.go(stepView.getCurrentStep() + 1, true);
                        expense_place_et.setEnabled(true);
                        expense_place_et.requestFocus();

                    } else {
                        Toast.makeText(ScanEr.this, "OK2", Toast.LENGTH_SHORT).show();
                        expense_place_et.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });

        expense_place_et.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Toast.makeText(ScanEr.this, "OK3", Toast.LENGTH_SHORT).show();
                    stepView.go(stepView.getCurrentStep() + 1, true);
                    expense_image.setClickable(true);
                    expense_place_et.clearFocus();
                    expense_image.setVisibility(View.VISIBLE);
                    LinearLayout ll = findViewById(R.id.back_and_next_buttonList_ll);
                    ll.setVisibility(View.INVISIBLE);

                    return true;
                }
                return false;
            }
        });


    }


    private void setStepper() {
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

    private void sendEr() {
        erService = NetworkProvider.getClient().create(ErService.class);

        int price = Integer.valueOf(expense_price_et.getText().toString());
        int vat = Integer.valueOf(expense_vat_et.getText().toString());
        String address = expense_place_et.getText().toString();


        ExpenseReportDTO newExpense = new ExpenseReportDTO(price, vat, address, current_er_type, "");
        String token = user_token;

        uploadFile(this.fileUri, this.image_file, newExpense, token);
    }


    private void uploadFile(Uri fileUri, File destination, final ExpenseReportDTO expense, final String token) {
        // create upload service client
        if (erService != null) {
            erService = NetworkProvider.getClient().create(ErService.class);
        }

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
                String external_image_url = response.body().getImage_name();
                expense.setImageID(external_image_url);
                submitExpenseReport(expense, token);
            }

            @Override
            public void onFailure(Call<ImageDTO> call, Throwable t) {

            }
        });
    }


    private void submitExpenseReport(ExpenseReportDTO expense, String token) {
        Call<Response<String>> call = erService.submitExpense(expense, token);
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                Snackbar.make(submitErButton, "Expense successfully submitted :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                newErSubmitAlert();
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                Snackbar.make(submitErButton, "Upload error", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void newErSubmitAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Submit another ER ?")
                .setMessage("Would you like to submit another ER ? If not you will be automaticly redirected to your homepage")
                .setPositiveButton("Yes, I want", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ScanEr.this, HomeActivity.class);
                        startActivity(i);
                    }
                })
                .create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;

        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                expense_image.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                this.fileUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                imgPath = getRealPathFromURI(this.fileUri);
                image_file = new File(imgPath);
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
                image_file = new File(imgPath);

                this.fileUri = selectedImage;
                expense_image.setImageBitmap(bitmap);

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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageTools.showChoiceDialog(submitErButton);
            }
        }
    }


    // BUTTERKNIFE COMPONENT BINDING

    // Step view
    @BindView(R.id.step_view)
    StepView stepView;

    // Back button
    @BindView(R.id.stepper_back_button)
    Button backBtn;

    @OnClick(R.id.stepper_back_button)
    public void beckPress() {
        this.stepView.go(stepView.getCurrentStep() - 1, true);
    }

    @OnClick(R.id.stepper_next_button)
    public void nextPress() {
        this.stepView.go(stepView.getCurrentStep() + 1, true);

        if (this.stepView.getCurrentStep() == 0) {
            expense_vat_et.setEnabled(true);
            expense_vat_et.requestFocus();
        } else if (this.stepView.getCurrentStep() == 1) {
            expense_place_et.setEnabled(true);
            expense_place_et.requestFocus();
        } else if (this.stepView.getCurrentStep() == 2) {
            expense_image.setClickable(true);
            LinearLayout ll = findViewById(R.id.back_and_next_buttonList_ll);
            ll.setVisibility(View.INVISIBLE);
        }
    }

    // Submit button
    @BindView(R.id.choose_type_button)
    Button submitErButton;

    @OnClick(R.id.choose_type_button)
    public void submit() {
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

    @OnClick(R.id.back_to_types_grid)
    public void backToGrid(){
        onBackPressed();
    }

    // ER image
    @BindView(R.id.expense_report_image)
    ImageView expense_image;

    @OnClick(R.id.expense_report_image)
    public void chooseImage() {
        if (this.stepView.getCurrentStep() == 3) {
            //askMediaPermission();
            imageTools.askMediaPermission(submitErButton);
        } else {
            Toast.makeText(this, "You cannot choose image step is not here yet", Toast.LENGTH_SHORT).show();
        }
    }


}
