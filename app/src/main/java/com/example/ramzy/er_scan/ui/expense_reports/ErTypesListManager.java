package com.example.ramzy.er_scan.ui.expense_reports;

import com.example.ramzy.er_scan.R;
import com.example.ramzy.er_scan.ui.expense_reports.viewholder.ErTypes;

import java.util.ArrayList;

public class ErTypesListManager {

    private static ErTypesListManager instance = null;

    private ArrayList<ErTypes> erTypes;

    private ErTypesListManager(){
        this.fillErTypesList();
    }

    public static  ErTypesListManager getInstance(){
        if(instance == null){
            instance = new ErTypesListManager();
        }
        return instance;
    }

    public ArrayList<ErTypes> getErTypes() {
        return erTypes;
    }

    private void fillErTypesList(){
        erTypes = new ArrayList<>();

        erTypes.add(new ErTypes("Plane", R.drawable.avion_icon));
        erTypes.add(new ErTypes("Gas", R.drawable.carburant_icon));
        erTypes.add(new ErTypes("Others", R.drawable.divers_icon));
        erTypes.add(new ErTypes("Hôtel", R.drawable.hotel_icon));
        erTypes.add(new ErTypes("Distances", R.drawable.km_counter_icon));
        erTypes.add(new ErTypes("Car location", R.drawable.location_voiture_icon));
        erTypes.add(new ErTypes("Parking", R.drawable.parking_icon));
        erTypes.add(new ErTypes("Restauration", R.drawable.restaurant_icon));
        erTypes.add(new ErTypes("Taxi", R.drawable.taxi_icon));
        erTypes.add(new ErTypes("Train", R.drawable.train_icon));
        erTypes.add(new ErTypes("Péage", R.drawable.peage_icon));
        erTypes.add(new ErTypes("Documents", R.drawable.document_icon));
        erTypes.add(new ErTypes("letters or parcels", R.drawable.colis_icon));
        erTypes.add(new ErTypes("Formation", R.drawable.apprentissage_icon));
    }

    public int getErType(String type){
        int drawable;

        switch(type){

            case "Plane":
                drawable = 0;
                break;
            case "Gas":
                drawable = 1;
                break;
            case "Others":
                drawable = 2;
                break;
            case "Hôtel":
                drawable = 3;
                break;
            case "Distances":
                drawable = 4;
                break;
            case "Car location":
                drawable = 4;
                break;
            case "Parking":
                drawable = 5;
                break;
            case "Restauration":
                drawable = 6;
                break;
            case "Taxi":
                drawable = 7;
                break;
            case "Train":
                drawable = 8;
                break;
            case "Péage":
                drawable = 9;
                break;
            case "Documents":
                drawable = 11;
                break;
            case "letters or parcels":
                drawable = 12;
                break;
            case "Formation":
                drawable = 13;
                break;

            default:
                drawable = -1;
                break;
        }

        return drawable;
    }

}
