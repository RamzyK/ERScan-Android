package com.example.ramzy.er_scan;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.UserDTO;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ErService;
import com.example.ramzy.er_scan.services.UserService;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanEr extends BaseActivity {

    private ErService erService;

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

    // Next button
    @BindView(R.id.stepper_next_button)
    Button nextBtn;

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
            Toast.makeText(this, "Choose", Toast.LENGTH_SHORT).show();
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


    private void sendEr(){
        erService = NetworkProvider.getClient().create(ErService.class);

        int price = Integer.valueOf(expense_price_et.getText().toString());
        int vat = Integer.valueOf(expense_vat_et.getText().toString());
        String address = expense_place_et.getText().toString();

        ExpenseReportDTO newExpense = new ExpenseReportDTO(price, vat, address);
        String token = pref.getString("token", "");

        Call<Response<String>> erCall = erService.submitExpense(newExpense, token);
        erCall.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                Snackbar.make(submitErButton,"Expense successfully submitted :)" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
            }
        });
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
}
