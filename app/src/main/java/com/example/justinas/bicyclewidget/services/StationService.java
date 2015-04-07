package com.example.justinas.bicyclewidget.services;

import com.example.justinas.bicyclewidget.entities.Station;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by Justinas on 4/6/2015.
 */
public class StationService {
    public Station[] getStations(){

        WebService webService = new WebService();
        String response = "";
        try {
            response = webService.getString("https://api.jcdecaux.com/vls/v1/stations?contract=Vilnius&apiKey=0421eb3ae0784bab99af659dbfe826f7cd48a7df");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Gson gson = new Gson();

        return gson.fromJson(response, Station[].class);
    }
}
