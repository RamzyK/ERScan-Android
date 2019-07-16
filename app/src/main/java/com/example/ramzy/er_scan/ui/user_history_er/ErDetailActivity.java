package com.example.ramzy.er_scan.ui.user_history_er;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.providers.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ErDetailActivity extends FragmentActivity {

    @BindView(R.id.er_receipt_imageview)
    ImageView erReceiptImage;

    @BindView(R.id.er_detail_toolbar_back_btn)
    ImageView toolbarBackButton;

    @OnClick(R.id.er_detail_toolbar_back_btn)
    public void backPressed(){
        onBackPressed();
    }

    @BindView(R.id.er_amount_tv)
    TextView erAmountTv;

    @BindView(R.id.er_vat_tv)
    TextView erVatText;

    @BindView(R.id.er_place_tv)
    TextView erPlaceTv;

    @BindView(R.id.er_status_tv)
    TextView erStatusTv;

    @BindView(R.id.er_type_tv)
    TextView erTypeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_detail);
        ButterKnife.bind(this);


        Intent data = getIntent();

        String imageName = data.getExtras().getString("er image");
        int amountValue = data.getExtras().getInt("er price");
        int vatValue = data.getExtras().getInt("er vat");
        String erLocation = data.getExtras().getString("er place");
        String erType = data.getExtras().getString("er type");
        int erStatus = data.getExtras().getInt("er status");

        Glide.with(this)
                .load(Constants.prod_URL + "images/" + imageName)
                .into(erReceiptImage);
        erAmountTv.setText("Amount: " + amountValue + "€");
        erVatText.setText("VAT (%) : " + vatValue);
        erPlaceTv.setText("At: " + erLocation);
        erTypeTv.setText("Type: " + erType);
        if(erStatus == 0){
            erStatusTv.setText("Status: Waiting");
        }else if( erStatus == 1){
            erStatusTv.setText("Status: Accepted");
        }else if(erStatus == 2){
            erStatusTv.setText("Status: Refused");
        }



    }

}
