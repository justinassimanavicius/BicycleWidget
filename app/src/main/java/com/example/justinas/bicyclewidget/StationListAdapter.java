package com.example.justinas.bicyclewidget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.justinas.bicyclewidget.entities.SelectableStation;

import java.util.ArrayList;

/**
 * Created by Justinas on 4/6/2015.
 */

public class StationListAdapter extends ArrayAdapter<SelectableStation> {

    private ArrayList<SelectableStation> stationList;
    private Context context;

    public OnCheckCallback onCheckCallback;

    public StationListAdapter(Context context, int textViewResourceId,
                               ArrayList<SelectableStation> stationList) {
        super(context, textViewResourceId, stationList);
        this.context = context;
        this.stationList = new ArrayList<SelectableStation>();
        this.stationList.addAll(stationList);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.station_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.address);
            holder.name = (CheckBox) convertView.findViewById(R.id.stationCheckBox);
            convertView.setTag(holder);

            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    SelectableStation station = (SelectableStation) cb.getTag();
                    station.isSelected =(cb.isChecked());

                    if(onCheckCallback!= null){

                        ArrayList<SelectableStation> selectedStations = new ArrayList<SelectableStation>();
                        for(int i=0;i<stationList.size();i++){
                            SelectableStation currentStation = stationList.get(i);
                            if(currentStation.isSelected){
                                selectedStations.add(currentStation);
                            }
                        }

                        onCheckCallback.onCheck(selectedStations);
                    }
                }
            });
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectableStation country = stationList.get(position);
        holder.code.setText(" (" + country.address + ")");
        holder.name.setText(country.name);
        holder.name.setChecked(country.isSelected);
        holder.name.setTag(country);

        return convertView;

    }

}