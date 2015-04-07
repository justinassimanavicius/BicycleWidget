package com.example.justinas.bicyclewidget;

import com.example.justinas.bicyclewidget.entities.SelectableStation;

import java.util.ArrayList;

/**
 * Created by Justinas on 4/6/2015.
 */
public interface OnCheckCallback {
    public void onCheck(ArrayList<SelectableStation> selectableStations);
}
