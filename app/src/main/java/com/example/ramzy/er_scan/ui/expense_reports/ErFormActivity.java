package com.example.ramzy.er_scan.ui.expense_reports;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.Tools;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ImageDTO;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ErService;
import com.example.ramzy.er_scan.ui.home.HomeActivity;
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

public class ErFormActivity extends FragmentActivity {
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
        expenseImage.setVisibility(View.INVISIBLE);
        setEnterKeyListenenrs();
        expensePriceEt.requestFocus();
    }

    private void setEnterKeyListenenrs() {

        expensePriceEt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    expensePriceEt.clearFocus();
                    // Perform action on key press
                    if (!expenseVatEt.isEnabled()) {
                        stepView.go(stepView.getCurrentStep() + 1, true);
                        expenseVatEt.setEnabled(true);
                        expenseVatEt.requestFocus();
                    }
                    expenseVatEt.requestFocus();
                    return true;
                }
                return false;
            }
        });

        expenseVatEt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    expenseVatEt.clearFocus();
                    // Perform action on key press
                    if (!expensePlaceEt.isEnabled()) {
                        stepView.go(stepView.getCurrentStep() + 1, true);
                        expensePlaceEt.setEnabled(true);

                    }
                    expensePlaceEt.requestFocus();
                    return true;
                }
                return false;
            }
        });

        expensePlaceEt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(expenseImage.getWindowToken(), 0);
                    stepView.go(stepView.getCurrentStep() + 1, true);
                    expenseImage.setClickable(true);
                    expensePlaceEt.clearFocus();
                    expenseImage.setVisibility(View.VISIBLE);

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

        int price = Integer.valueOf(expensePriceEt.getText().toString());
        int vat = Integer.valueOf(expenseVatEt.getText().toString());
        String address = expensePlaceEt.getText().toString();


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
        String descriptionString = "Image description";
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
                        Intent i = new Intent(ErFormActivity.this, HomeActivity.class);
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
                expenseImage.setImageBitmap(photo);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                this.fileUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                imgPath = getRealPathFromURI(this.fileUri);
                image_file = new File(imgPath);
                submitErButton.setVisibility(View.VISIBLE);
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
                expenseImage.setImageBitmap(bitmap);
                submitErButton.setVisibility(View.VISIBLE);
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
                imageTools.showChoiceDialog();
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
            expenseVatEt.setEnabled(true);
            expenseVatEt.requestFocus();
        } else if (this.stepView.getCurrentStep() == 1) {
            expensePlaceEt.setEnabled(true);
            expensePlaceEt.requestFocus();
        } else if (this.stepView.getCurrentStep() == 2) {
            expenseImage.setClickable(true);
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
    EditText expensePriceEt;

    // VAT EditText
    @BindView(R.id.expense_report_vat_et)
    EditText expenseVatEt;

    // Place EditText
    @BindView(R.id.expense_report_place_et)
    EditText expensePlaceEt;

    @OnClick(R.id.back_to_types_grid)
    public void backToGrid(){
        onBackPressed();
    }

    // ER image
    @BindView(R.id.expense_report_image)
    ImageView expenseImage;

    @OnClick(R.id.expense_report_image)
    public void chooseImage() {
        if (this.stepView.getCurrentStep() == 3) {
            //askMediaPermission();
            imageTools.askMediaPermission();
        } else {
            Toast.makeText(this, "You cannot choose image step is not here yet", Toast.LENGTH_SHORT).show();
        }
    }


}
