package com.example.ramzy.er_scan.ui.user_history_er;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.ui.user_history_er.adapter.ErDetailAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErDetail extends FragmentActivity {

    @OnClick(R.id.back_to_map_list)
    public void backToMAp(){
        onBackPressed();
    }

    @BindView(R.id.er_list_rv)
    RecyclerView er_list_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_detail);
        ButterKnife.bind(this);



        ArrayList<ExpenseReportDTO> er_list = getIntent().getParcelableArrayListExtra("er_list");


        er_list_rv.setLayoutManager(new LinearLayoutManager(this));
        ErDetailAdapter detailErAdapter = new ErDetailAdapter(er_list, this);
        er_list_rv.setAdapter(detailErAdapter);

    }
}
