package com.example.justinas.bicyclewidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.justinas.bicyclewidget.entities.Station;

import java.util.ArrayList;

/**
 * Created by Justinas on 4/6/2015.
 */

public class StationInfoAdapter extends ArrayAdapter<Station> {

    private ArrayList<Station> stationList;
    private Context context;

    public StationInfoAdapter(Context context, int textViewResourceId,
                              ArrayList<Station> stationList) {
        super(context, textViewResourceId, stationList);
        this.context = context;
        this.stationList = new ArrayList<Station>();
        this.stationList.addAll(stationList);
    }

    private class ViewHolder {
        TextView totalStands;
        TextView availableBikes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.widget_station_info, null);

            holder = new ViewHolder();
            holder.totalStands = (TextView) convertView.findViewById(R.id.totalStands);
            holder.availableBikes = (TextView) convertView.findViewById(R.id.availableBikes);
            convertView.setTag(holder);

        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Station station = stationList.get(position);
        holder.availableBikes.setText(station.available_bikes);
        holder.totalStands.setText(station.bike_stands);

        return convertView;

    }

}