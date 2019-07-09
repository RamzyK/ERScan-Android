package com.example.ramzy.er_scan.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ramzy.er_scan.BaseActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ExpenseReportResponseDTO;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ErService;
import com.example.ramzy.er_scan.ui.AppDescriptionPagerAdapter;
import com.example.ramzy.er_scan.ui.expense_reports.ChooseErTypeActivity;
import com.example.ramzy.er_scan.ui.user_history_er.adapter.ErDetailAdapter;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends BaseActivity {

    private String firstname;
    private String lastname;

    private ErService erService;
    private SharedPreferences pref;

    @BindView(R.id.home_last_expenses_rv)
    RecyclerView lastExpensesRv;

    @OnClick(R.id.fab)
    public void buttonClicked(){
        Intent i = new Intent(HomeActivity.this, ChooseErTypeActivity.class);
        startActivity(i);
    }

    @BindView(R.id.total_month_er_value_tv)
    TextView totalMontExpensesValue;

    @BindView(R.id.accepted_expenses_tv)
    TextView acceptedExpenses;

    @BindView(R.id.refused_expenses_tv)
    TextView refusedExpenses;

    @BindView(R.id.no_expenses_history_tv)
    TextView noExpenseHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        noExpenseHistory.setVisibility(View.VISIBLE);
        this.setToolBar(R.id.home_activity_toolbar,
                R.id.drawer_layout,
                R.id.nav_view_home_activity, this);


        pref =  SharedPrefs.getInstance(this);
        firstname = pref.getString("firstname", "NaN");
        lastname = pref.getString("lastname", "NaN");
        Snackbar.make(findViewById(R.id.fab), "Hello " + firstname + " " + lastname , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        lastExpensesRv.setLayoutManager(new LinearLayoutManager(this));
        getThreeLastExpenses();
        getUserExpenses();
    }

    private void getThreeLastExpenses() {
        erService = NetworkProvider.getClient().create(ErService.class);
        String token = pref.getString("token", "none");
        if(token != "none"){
            Call<ExpenseReportResponseDTO> call = erService.getLast3Expenses(token);
            call.enqueue(new Callback<ExpenseReportResponseDTO>() {
                @Override
                public void onResponse(Call<ExpenseReportResponseDTO> call, Response<ExpenseReportResponseDTO> response) {
                    ArrayList<ExpenseReportDTO> er_list = new ArrayList<>();
                    ExpenseReportDTO[] expenses = response.body().getErList();
                    if(expenses.length > 0){
                        noExpenseHistory.setVisibility(View.INVISIBLE);
                        Collections.addAll(er_list, expenses);
                        ErDetailAdapter detailErAdapter = new ErDetailAdapter(er_list, HomeActivity.this);
                        lastExpensesRv.setAdapter(detailErAdapter);
                    }else{
                        noExpenseHistory.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ExpenseReportResponseDTO> call, Throwable t) {

                }
            });
        }

    }

    private void getUserExpenses() {
        String token = pref.getString("token", "null");
        if (!token.equals("null")) {
            erService = NetworkProvider.getClient().create(ErService.class);

            Call<ExpenseReportResponseDTO> erReponse = erService.getUserExpenseReports(token);
            erReponse.enqueue(new Callback<ExpenseReportResponseDTO>() {
                @Override
                public void onResponse(Call<ExpenseReportResponseDTO> call, Response<ExpenseReportResponseDTO> response) {
                    ExpenseReportDTO[] expenseReports = response.body().getErList();
                    int totalExpensesValue = 0;
                    int acceptedErCount = 0;
                    int refusedErCount = 0;

                    for (int i = 0; i < expenseReports.length; i++) {
                        totalExpensesValue += expenseReports[i].getPrice();
                        if(expenseReports[i].getStatus() == 1){
                            acceptedErCount += 1;
                        }else if(expenseReports[i].getStatus() == 2){
                            refusedErCount += 1;
                        }
                    }

                    totalMontExpensesValue.setText("Your expenses (Month): " + totalExpensesValue + "€");
                    acceptedExpenses.setText("Validated expenses: " + acceptedErCount);
                    refusedExpenses.setText("Refused expenses: " + refusedErCount);

                }

                @Override
                public void onFailure(Call<ExpenseReportResponseDTO> call, Throwable t) {

                }
            });
        }


    }


    @Override
    public void onBackPressed() {

    }
}
