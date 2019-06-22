package com.example.ramzy.er_scan.ui.expense_reports.viewholder;

public class ErTypes {
    private String type_title;
    private int id_drawable;

    public ErTypes(String t, int id){
        this.type_title = t;
        this.id_drawable = id;
    }

    public String getType_title() {
        return type_title;
    }

    public void setType_title(String type_title) {
        this.type_title = type_title;
    }

    public int getId_drawable() {
        return id_drawable;
    }

    public void setId_drawable(int id_drawable) {
        this.id_drawable = id_drawable;
    }
}
