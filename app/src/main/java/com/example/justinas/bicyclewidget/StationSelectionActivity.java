package com.example.justinas.bicyclewidget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.justinas.bicyclewidget.entities.SelectableStation;
import com.example.justinas.bicyclewidget.entities.Station;
import com.example.justinas.bicyclewidget.services.SettingsStore;
import com.example.justinas.bicyclewidget.services.StationService;

import java.util.ArrayList;


public class StationSelectionActivity extends ActionBarActivity implements OnCheckCallback {


    private ListView listView;

    private StationListAdapter adapter;
    private int widgetId;
    private SettingsStore settingsStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_selection);

        //https://api.jcdecaux.com/vls/v1/stations?contract=Vilnius&apiKey=0421eb3ae0784bab99af659dbfe826f7cd48a7df

        setResult(RESULT_CANCELED);

        widgetId = getWidgetId();

         settingsStore = new SettingsStore(getBaseContext());

        listView = (ListView) findViewById(R.id.listView);

        Button saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
                RemoteViews views = new RemoteViews(getBaseContext().getPackageName(),
                        R.layout.example_appwidget);
                appWidgetManager.updateAppWidget(widgetId, views);

                Intent resultValue = new Intent();
                resultValue.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
        new DownloadStationsTask().execute();


    }

    private int getWidgetId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            return extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        return AppWidgetManager.INVALID_APPWIDGET_ID;
    }

    @Override
    public void onCheck(ArrayList<SelectableStation> selectedStations) {
        settingsStore.saveSelectedStationNumbers(selectedStations, widgetId);
    }

    private class DownloadStationsTask extends AsyncTask<String, Void, Station[]> {
        @Override
        protected Station[] doInBackground(String... urls) {
            StationService service = new StationService();
            Station[] stations = service.getStations();
            Log.i("onCreate", stations.toString());
            return stations;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Station[] result) {

            ArrayList<SelectableStation> stations = new ArrayList<>();

            ArrayList<Integer> selectedStationNumbers = settingsStore.getSelectedStationNumbers(widgetId);

            for (Station station : result) {
                SelectableStation selectableStation = new SelectableStation();
                selectableStation.name = station.name;
                selectableStation.number = station.number;
                selectableStation.address = station.address;

                if (selectedStationNumbers.contains(selectableStation.number)) {
                    selectableStation.isSelected = true;
                }

                stations.add(selectableStation);
            }

            adapter = new StationListAdapter(getBaseContext(), R.layout.station_info, stations);
            adapter.onCheckCallback = StationSelectionActivity.this;
            listView.setAdapter(adapter);
        }
    }
}
