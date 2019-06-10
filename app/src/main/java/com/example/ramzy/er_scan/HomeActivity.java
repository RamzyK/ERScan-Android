package com.example.ramzy.er_scan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.TextView;

import com.example.ramzy.er_scan.preferences.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends BaseActivity {
    private String firstname;
    private String lastname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         firstname = pref.getString("firstname", "NaN");
         lastname = pref.getString("lastname", "NaN");

        findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        Snackbar.make(findViewById(R.id.fab), "Hello " + firstname + " " + lastname , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        this.setToolBar(R.id.home_activity_toolbar,
                        R.id.drawer_layout,
                        R.id.nav_view_home_activity, this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Snackbar.make(findViewById(R.id.fab), "Welcome back " + firstname + " " + lastname , Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
