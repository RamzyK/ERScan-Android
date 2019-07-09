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

        erTypes.add(new ErTypes("Plane", R.drawable.avion_icon)); // 0

        erTypes.add(new ErTypes("Gas", R.drawable.carburant_icon));//1

        erTypes.add(new ErTypes("Others", R.drawable.divers_icon));//2

        erTypes.add(new ErTypes("Hôtel", R.drawable.hotel_icon));//3

        erTypes.add(new ErTypes("Distances", R.drawable.km_counter_icon));//4

        erTypes.add(new ErTypes("Car location", R.drawable.location_voiture_icon));//5

        erTypes.add(new ErTypes("Parking", R.drawable.parking_icon));//6

        erTypes.add(new ErTypes("Restauration", R.drawable.restaurant_icon));//7

        erTypes.add(new ErTypes("Taxi", R.drawable.taxi_icon));//8

        erTypes.add(new ErTypes("Train", R.drawable.train_icon));//9

        erTypes.add(new ErTypes("Péage", R.drawable.peage_icon));//10

        erTypes.add(new ErTypes("Documents", R.drawable.document_icon));//11

        erTypes.add(new ErTypes("letters or parcels", R.drawable.colis_icon));//12

        erTypes.add(new ErTypes("Formation", R.drawable.apprentissage_icon));//13
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
                drawable = 5;
                break;
            case "Parking":
                drawable = 6;
                break;
            case "Restauration":
                drawable = 7;
                break;
            case "Taxi":
                drawable = 8;
                break;
            case "Train":
                drawable = 9;
                break;
            case "Péage":
                drawable = 10;
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
