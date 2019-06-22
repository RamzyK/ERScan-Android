package com.example.ramzy.er_scan.ui.expense_reports.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.ui.expense_reports.viewholder.ErTypeViewHolder;
import com.example.ramzy.er_scan.ui.expense_reports.viewholder.ErTypes;

import java.util.ArrayList;

public class ErTypeGridAdapter extends RecyclerView.Adapter<ErTypeViewHolder>{
    private Activity context;
    private ArrayList<ErTypes> types_list;
    private ItemClickListener mClickListener;


    public ErTypeGridAdapter(Activity c, ArrayList<ErTypes> t){
        this.context = c;
        this.types_list = t;
    }

    @NonNull
    @Override
    public ErTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.grid_er_types_cell, viewGroup, false);
        return new ErTypeViewHolder(v, context, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ErTypeViewHolder viewHolder, int i) {
        viewHolder.bind(types_list.get(i), i);
    }

    @Override
    public int getItemCount() {
        return types_list.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
