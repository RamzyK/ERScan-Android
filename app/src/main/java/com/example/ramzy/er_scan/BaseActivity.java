package com.example.ramzy.er_scan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.Constants;
import com.example.ramzy.er_scan.ui.bugs.BugReportingActivity;
import com.example.ramzy.er_scan.ui.expense_reports.ChooseErTypeActivity;
import com.example.ramzy.er_scan.ui.home.HomeActivity;
import com.example.ramzy.er_scan.ui.user_account.AccountParamsActivity;
import com.example.ramzy.er_scan.ui.user_history_er.HistoryMapUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected SharedPreferences pref;

    @Nullable
    @BindView(R.id.user_firstname_tv)
    TextView userFirstname;

    @Nullable
    @BindView(R.id.user_lastname_tv)
    TextView userLastName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = SharedPrefs.getInstance(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    protected void setToolBar(int toolbarId, int drawerId, int navViewId, Activity ctx){
        Toolbar toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(drawerId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(navViewId);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) ctx);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        ButterKnife.bind(this);
        ImageView image = findViewById(R.id.imageView);
        userFirstname.setText(pref.getString("firstname", "NaN"));
        userLastName.setText(pref.getString("lastname", "NaN"));
        if(!pref.getString("imageID", "none").equals("none")){
            String user_picture = pref.getString("imageID", "none");

            String image_path = Constants.loacl_URL + "images/" + user_picture;
            Glide.with(this).load(image_path).into(image);
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BaseActivity.this, AccountParamsActivity.class);
                startActivity(i);
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.scan_er) {
            Intent i = new Intent(BaseActivity.this, HomeActivity.class);
            startActivity(i);
            
        } else if (id == R.id.add_er) {
            Intent i = new Intent(BaseActivity.this, ChooseErTypeActivity.class);
            startActivity(i);
            
        } else if (id == R.id.er_history) {
            Intent i = new Intent(BaseActivity.this, HistoryMapUser.class);
            startActivity(i);
            
        } else if (id == R.id.bug_reporting) {
            Intent i = new Intent(BaseActivity.this, BugReportingActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Thanks for sharing :)", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.logout_btn) {
            pref.edit().clear().apply();
            pref.edit().putBoolean("first_open", false).apply();
            Intent intent = new Intent(BaseActivity.this, SplashScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
