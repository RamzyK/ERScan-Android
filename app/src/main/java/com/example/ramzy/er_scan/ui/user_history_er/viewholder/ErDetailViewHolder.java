package com.example.ramzy.er_scan.ui.user_history_er.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.providers.Constants;
import com.example.ramzy.er_scan.ui.expense_reports.ErTypesListManager;
import com.example.ramzy.er_scan.ui.user_history_er.ErDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErDetailViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ErTypesListManager erTypesListManager;
    private ExpenseReportDTO expenseReportDTO;

    @BindView(R.id.er_image_cell)
    ImageView erImage;

    @BindView(R.id.address_er_tv)
    TextView erAdress;

    @BindView(R.id.price_er_tv)
    TextView erPrice;

    @BindView(R.id.er_status)
    ImageView er_status;

    @BindView(R.id.er_image_cell_constraint_layout)
    ConstraintLayout imageTypeConstraintLayout;


    public ErDetailViewHolder(@NonNull View itemView, Context ctx) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.context = ctx;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expenseReportDTO != null){
                    Intent i = new Intent(context, ErDetailActivity.class);
                    i.putExtra("er image", expenseReportDTO.getImageID());
                    i.putExtra("er price", expenseReportDTO.getPrice());
                    i.putExtra("er vat", expenseReportDTO.getVat());
                    i.putExtra("er place", expenseReportDTO.getAddress());
                    i.putExtra("er type", expenseReportDTO.getType());
                    i.putExtra("er status", expenseReportDTO.getStatus());
                    context.startActivity(i);
                }
            }
        });
    }

    public void bind(ExpenseReportDTO er){
        //erImage.setImageDrawable( R.drawable.logo_er_scan);
        this.expenseReportDTO = er;
        String image_path = Constants.loacl_URL + "images/" + er.getImageID();
        int typePosition = erTypesListManager.getInstance().getErType(er.getType());
        int typeDrawable = erTypesListManager.getInstance().getErTypes().get(typePosition).getId_drawable();
        int typeBackGround = setCellBackground(typePosition);

        if(er.getStatus() == 0){
            er_status.setImageDrawable(context.getDrawable(R.drawable.unverified));
        }else if(er.getStatus() == 1){
            er_status.setImageDrawable(context.getDrawable(R.drawable.verified));
        }else{
            er_status.setImageDrawable(context.getDrawable(R.drawable.refused));
        }
        erAdress.setText("Place: " + er.getAddress());
        erPrice.setText("Price: " + er.getPrice() + "€");

        imageTypeConstraintLayout.setBackgroundColor(typeBackGround);
        erImage.setImageDrawable(context.getDrawable(typeDrawable));
    }

    public int setCellBackground(int position){
        int backGroundColor = 0;
        switch (position){
            case 0:
                backGroundColor = context.getResources().getColor(R.color.cell1);
                break;
            case 1:
                backGroundColor = context.getResources().getColor(R.color.cell2);
                break;
            case 2:
                backGroundColor = context.getResources().getColor(R.color.cell3);
                break;
            case 3:
                backGroundColor = context.getResources().getColor(R.color.cell4);
                break;
            case 4:
                backGroundColor = context.getResources().getColor(R.color.cell5);
                break;
            case 5:
                backGroundColor = context.getResources().getColor(R.color.cell6);
                break;
            case 6:
                backGroundColor = context.getResources().getColor(R.color.cell7);
                break;
            case 7:
                backGroundColor = context.getResources().getColor(R.color.cell8);
                break;
            case 8:
                backGroundColor = context.getResources().getColor(R.color.cell9);
                break;
            case 9:
                backGroundColor = context.getResources().getColor(R.color.cell10);
                break;
            case 10:
                backGroundColor = context.getResources().getColor(R.color.cell11);
                break;
            case 11:
                backGroundColor = context.getResources().getColor(R.color.cell2);
                break;
            case 12:
                backGroundColor = context.getResources().getColor(R.color.cell6);
                break;
            case 13:
                backGroundColor = context.getResources().getColor(R.color.cell5);
                break;
            default:
                backGroundColor = context.getResources().getColor(R.color.cell1);
                break;
        }
        return backGroundColor;
    }

}
