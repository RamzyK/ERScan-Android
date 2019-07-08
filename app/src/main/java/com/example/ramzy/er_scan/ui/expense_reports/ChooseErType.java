package com.example.ramzy.er_scan.ui.expense_reports;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.ramzy.er_scan.BaseActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.ui.expense_reports.adapter.ErTypeGridAdapter;
import com.example.ramzy.er_scan.ui.expense_reports.viewholder.ErTypes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseErType extends BaseActivity implements ErTypeGridAdapter.ItemClickListener {

    private ArrayList<ErTypes> erTypesList;

    @BindView(R.id.grid_types_rv)
    RecyclerView grid_types_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_er_type);
        ButterKnife.bind(this);

        this.setToolBar(R.id.choose_type_toolbar,
                R.id.drawer_layout,
                R.id.nav_view_add_er_activity, this);

        erTypesList = ErTypesListManager.getInstance().getErTypes();

        grid_types_rv.setLayoutManager(new GridLayoutManager(this, 3));
        ErTypeGridAdapter adapter = new ErTypeGridAdapter(this, erTypesList);
        adapter.setClickListener(this);
        grid_types_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
