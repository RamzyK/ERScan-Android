package com.example.ramzy.er_scan.ui.user_history_er.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErDetailViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    @BindView(R.id.er_image_cell)
    ImageView er_image;

    @BindView(R.id.address_er_tv)
    TextView er_address;

    @BindView(R.id.price_er_tv)
    TextView er_price;

    @BindView(R.id.er_status)
    ImageView er_status;

    public ErDetailViewHolder(@NonNull View itemView, Context ctx) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = ctx;
    }

    public void bind(ExpenseReportDTO er){
        //er_image.setImageDrawable( R.drawable.logo_er_scan);


        er_address.setText(er.getAddress());
        er_price.setText(String.valueOf(er.getPrice()));

    }

}
