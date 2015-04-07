package com.example.justinas.bicyclewidget.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.justinas.bicyclewidget.entities.SelectableStation;

import java.util.ArrayList;

/**
 * Created by Justinas on 4/6/2015.
 */
public class SettingsStore {

    private static final String PREFS_NAME = "bcprefs";
    private final Context context;

    public SettingsStore(Context context){
        this.context = context;
    }

    public ArrayList<Integer> getSelectedStationNumbers(int widgetId) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME+"_"+widgetId, 0);
        String settingsString = settings.getString("selectedStationsNumbers", "");
        String[] split = settingsString.split(",");

        ArrayList<Integer> result = new ArrayList<>();

        for (String oneSplit : split) {
            if(isInt(oneSplit)) {
                result.add(Integer.parseInt(oneSplit));
            }
        }
        return result;
    }

    boolean isInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException nfe)
        {
            return false;
        }
    }

    public void saveSelectedStationNumbers(ArrayList<SelectableStation> selectedStations, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME+"_"+widgetId, 0);
        SharedPreferences.Editor editor = prefs.edit();

        String result = "";

        for (SelectableStation selectedStation : selectedStations) {
            if (result != "") {
                result += ",";
            }
            result += selectedStation.number;
        }

        editor.putString("selectedStationsNumbers", result);
        editor.commit();
    }

}
