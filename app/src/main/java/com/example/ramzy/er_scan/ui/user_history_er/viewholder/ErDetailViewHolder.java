package com.example.ramzy.er_scan.ui.user_history_er.viewholder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.ExpenseReportDTO;
import com.example.ramzy.er_scan.dto.ImageDTO;
import com.example.ramzy.er_scan.providers.Constants;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.ImageUploadService;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErDetailViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ImageUploadService imageService;

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
        String image_path = Constants.loacl_URL + "images/" + er.getImageID();

        if(er.getStatus() == 0){
            er_status.setImageDrawable(context.getDrawable(R.drawable.unverified));
        }else{
            er_status.setImageDrawable(context.getDrawable(R.drawable.verified));
        }
        er_address.setText("Place: " + er.getAddress());
        er_price.setText("Price: " + er.getPrice() + "€");
        if(er.getImageID() == null){
            er_image.setImageDrawable(context.getDrawable(R.drawable.not_found_icon));
        }else{
            Glide.with(context).load(image_path).into(er_image);
        }

    }

}
