package com.example.ramzy.er_scan.ui.user_history_er.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.ui.user_history_er.viewholder.ErDetailViewHolder;

import java.util.ArrayList;

public class ErDetailAdapter extends RecyclerView.Adapter<ErDetailViewHolder> {
    private Context context;
    private ArrayList<ExpenseReportDTO> er_list;

    public ErDetailAdapter(ArrayList<ExpenseReportDTO> er_list, Context ctx){
        this.context = ctx;
        this.er_list = er_list;
    }


    @NonNull
    @Override
    public ErDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.er_detail_cell, viewGroup, false);
        return new ErDetailViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ErDetailViewHolder erDetailViewHolder, int i) {
        erDetailViewHolder.bind(er_list.get(i));
    }

    @Override
    public int getItemCount() {
        return er_list.size();
    }
}
