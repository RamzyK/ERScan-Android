package com.example.ramzy.er_scan.ui.expense_reports.viewholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.ui.expense_reports.ScanEr;
import com.example.ramzy.er_scan.ui.expense_reports.adapter.ErTypeGridAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Activity context;
    private ErTypeGridAdapter.ItemClickListener itemClickListener;

    @BindView(R.id.grid_cell_image)
    ImageView type_icon;

    @BindView(R.id.grid_cell_tv)
    TextView type_title;

    @BindView(R.id.er_category_grid_cell)
    ConstraintLayout constraintLayout;

    public ErTypeViewHolder(@NonNull View itemView, Activity c, ErTypeGridAdapter.ItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = c;
        this.itemClickListener = listener;
    }

    public void bind(final ErTypes type, int position){

        setCellBackground(position);
        type_icon.setImageDrawable(context.getDrawable(type.getId_drawable()));
        type_title.setText(type.getType_title());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent i = new Intent(context, ScanEr.class);
                i.putExtra("er-type", type.getType_title());
                i.putExtra("token", SharedPrefs.getInstance(context).getString("token", "none"));
                context.startActivity(i);
            }
        });

    }

    public void setCellBackground(int position){
        switch (position){
            case 0:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell1));
                break;
            case 1:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell2));
                break;
            case 2:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell3));
                break;
            case 3:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell4));
                break;
            case 4:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell5));
                break;
            case 5:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell6));
                break;
            case 6:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell8));
                break;
            case 7:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell7));
                break;
            case 8:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell9));
                break;
            case 9:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell10));
                break;
            case 10:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell11));
                break;
            case 11:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell5));
                break;
            case 12:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell11));
                break;
            case 13:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell5));
                break;
            default:
                this.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.cell6));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "AAAA", Toast.LENGTH_SHORT).show();
        if (itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());

    }
}
