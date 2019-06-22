package com.example.ramzy.er_scan.ui.expense_reports;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.ramzy.er_scan.BaseActivity;
import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.ui.expense_reports.adapter.ErTypeGridAdapter;
import com.example.ramzy.er_scan.ui.expense_reports.viewholder.ErTypes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseErType extends BaseActivity implements ErTypeGridAdapter.ItemClickListener {

    private ArrayList<ErTypes> types_list;

    @BindView(R.id.grid_types_rv)
    RecyclerView grid_types_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_er_type);
        ButterKnife.bind(this);

        this.setToolBar(R.id.choose_type_toolbar,
                R.id.drawer_layout,
                R.id.nav_view_add_er_activity, this);

        types_list = new ArrayList<>();

        types_list.add(new ErTypes("Plane", R.drawable.avion_icon));
        types_list.add(new ErTypes("Gas", R.drawable.carburant_icon));
        types_list.add(new ErTypes("Others", R.drawable.divers_icon));
        types_list.add(new ErTypes("Hôtel", R.drawable.hotel_icon));
        types_list.add(new ErTypes("Distances", R.drawable.km_counter_icon));
        types_list.add(new ErTypes("Car location", R.drawable.location_voiture_icon));
        types_list.add(new ErTypes("Parking", R.drawable.parking_icon));
        types_list.add(new ErTypes("Restauration", R.drawable.restaurant_icon));
        types_list.add(new ErTypes("Taxi", R.drawable.taxi_icon));
        types_list.add(new ErTypes("Train", R.drawable.train_icon));
        types_list.add(new ErTypes("Péage", R.drawable.peage_icon));
        types_list.add(new ErTypes("Documents", R.drawable.document_icon));
        types_list.add(new ErTypes("letters or parcels", R.drawable.colis_icon));
        types_list.add(new ErTypes("Formation", R.drawable.apprentissage_icon));



        grid_types_rv.setLayoutManager(new GridLayoutManager(this, 3));
        ErTypeGridAdapter adapter = new ErTypeGridAdapter(this, types_list);
        adapter.setClickListener(this);
        grid_types_rv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        // Do some
        Toast.makeText(this, types_list.get(position).getType_title(), Toast.LENGTH_SHORT).show();
    }
}
