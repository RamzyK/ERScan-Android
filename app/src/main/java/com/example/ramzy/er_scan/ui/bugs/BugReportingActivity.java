package com.example.ramzy.er_scan.ui.bugs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramzy.er_scan.BaseActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.dto.BugDTO;
import com.example.ramzy.er_scan.preferences.SharedPrefs;
import com.example.ramzy.er_scan.providers.NetworkProvider;
import com.example.ramzy.er_scan.services.BugService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BugReportingActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private String currentBugType;
    private BugService bugService;
    private SharedPreferences pref;


    @BindView(R.id.bug_description)
    EditText bugContent;

    @BindView(R.id.send_comment)
    Button sendBug;

    @BindView(R.id.bug_type_spinner)
    Spinner bugTypeSpinner;


    @OnClick(R.id.send_comment)
    public void submitComment(){
        // Api call to send bug
        if(currentBugType != "None" && !bugContent.getText().toString().isEmpty()){
            submitBug();
        }else{
            Toast.makeText(this, "You must fill all informations avbout the bug", Toast.LENGTH_SHORT).show();
        }
        
    }

    private void submitBug() {
        bugService = NetworkProvider.getClient().create(BugService.class);
        String content = bugContent.getText().toString();
        String userId = pref.getString("idUser", "");
        String bugType = currentBugType;
        String token = pref.getString("token", "");

        BugDTO newBug = new BugDTO(content, userId, bugType);
        Call<Response<String>> call = bugService.submitBug(newBug, token);
        
        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                Snackbar.make(sendBug,"Thank you for your comment, we'll try to fix it as quick as possible :)" , Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bug_activity);
        ButterKnife.bind(this);

        pref = SharedPrefs.getInstance(this);
        this.setToolBar(R.id.bug_activity_toolbar,
                        R.id.drawer_layout,
                        R.id.nav_view_bug_activity, this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.but_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bugTypeSpinner.setAdapter(adapter);
        bugTypeSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
     currentBugType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
