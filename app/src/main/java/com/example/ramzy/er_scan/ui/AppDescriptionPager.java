package com.example.ramzy.er_scan.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.SplashScreenActivity;

public class AppDescriptionPager extends AppCompatActivity {

    private ViewPager descriptionPager;
    private Button startAppBtn;
    private int[] listSlides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_description_pager);


        descriptionPager = findViewById(R.id.app_description_usage_viewpager);
        startAppBtn = findViewById(R.id.start_app_usage_btn);
        startAppBtn.setVisibility(View.INVISIBLE);
        startAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AppDescriptionPager.this, SplashScreenActivity.class);
                startActivity(i);
            }
        });

        //Array
       listSlides = new int[]{
           R.layout.pager_description_page_1,
           R.layout.pager_description_page_2,
           R.layout.pager_description_page_3,
       };

       descriptionPager.setAdapter(new AppDescriptionPagerAdapter(this, listSlides));
       descriptionPager.addOnPageChangeListener(viewlistener);
    }


    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == listSlides.length - 1){
                startAppBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(AppDescriptionPager.this, SplashScreenActivity.class);
        startActivity(i);

    }
}
