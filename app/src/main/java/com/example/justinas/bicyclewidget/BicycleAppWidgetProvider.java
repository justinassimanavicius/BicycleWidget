package com.example.justinas.bicyclewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.justinas.bicyclewidget.entities.Station;
import com.example.justinas.bicyclewidget.services.SettingsStore;
import com.example.justinas.bicyclewidget.services.StationService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Justinas on 4/6/2015.
 */
public class BicycleAppWidgetProvider extends AppWidgetProvider {


    public static final String WIDGET_UPDATE_ACTION = "com.example.justinas.bicyclewidget.APPWIDGET_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        setClickHandlers(context, appWidgetManager, appWidgetIds);

        //new DownloadStationsTask(context, appWidgetManager, appWidgetIds).execute();

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.i("BicycleAppWidetPRovicer", action);
        if (action == WIDGET_UPDATE_ACTION) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            new DownloadStationsTask(context, appWidgetManager, widgetIds).execute();
        }

        super.onReceive(context, intent);
    }


    private void setClickHandlers(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            setRefreshHandler(context, appWidgetId,appWidgetManager);
            setConfigHandler(context, appWidgetId,appWidgetManager);



            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
            Date d = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss") ;
            views.setTextViewText(R.id.refreshTextView, simpleDateFormat.format(d));



            appWidgetManager.updateAppWidget(appWidgetId, views);

            //setListAdapter(appWidgetId, context, appWidgetManager);
        }
    }

    private void setListAdapter(int appWidgetId , Context context, AppWidgetManager appWidgetManager) {

        // Set up the intent that starts the StackViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, WidgetService.class);
        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects
        // to a RemoteViewsService  through the specified intent.
        // This is how you populate the data.
        rv.setRemoteAdapter(R.id.grid, intent);
        Log.i("setListAdapter", "updated widget "+appWidgetId);
        //
        // Do additional processing specific to this app widget...
        //

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    private void setRefreshHandler(Context context, int appWidgetId, AppWidgetManager appWidgetManager) {
        Intent clickIntent = new Intent(context, BicycleAppWidgetProvider.class);

        clickIntent.setAction(WIDGET_UPDATE_ACTION);
        int[] ids = new int[1];
        ids[0] = appWidgetId;
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.refreshButton, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void setConfigHandler(Context context, int appWidgetId, AppWidgetManager appWidgetManager) {
        Intent configIntent = new Intent(context, StationSelectionActivity.class);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
        remoteViews.setOnClickPendingIntent(R.id.configureButton, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private HashMap<Integer, Station> getStations() {
        StationService stationService = new StationService();

        Station[] stations = stationService.getStations();

        HashMap<Integer, Station> stationsMap = new HashMap<>();

        for (Station station : stations) {
            stationsMap.put(station.number, station);
        }

        return stationsMap;
    }


    private class DownloadStationsTask extends AsyncTask<String, Void, HashMap<Integer, Station>> {

        private final Context context;
        private final AppWidgetManager appWidgetManager;
        private final int[] appWidgetIds;

        public DownloadStationsTask(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

            this.context = context;
            this.appWidgetManager = appWidgetManager;
            this.appWidgetIds = appWidgetIds;
        }

        @Override
        protected HashMap<Integer, Station> doInBackground(String... urls) {
            return getStations();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(HashMap<Integer, Station> result) {


            SettingsStore settingsStore = new SettingsStore(context);
            final int N = appWidgetIds.length;

            // Perform this loop procedure for each App Widget that belongs to this provider
            for (int i = 0; i < N; i++) {
                int appWidgetId = appWidgetIds[i];


                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);

                ArrayList<Integer> selectedStationNumbers = settingsStore.getSelectedStationNumbers(appWidgetId);
                if (selectedStationNumbers.size() > 0) {
                    int stationNumber = selectedStationNumbers.get(0);
                    Station station =
                        result.get(stationNumber);
                    String res = "";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss") ;
                    for(int number: selectedStationNumbers ){
                        station =  result.get(number);
                        Date lastUpdateDate = new Date(station.last_update);
                        String last_update = simpleDateFormat.format(lastUpdateDate);
                        res += station.available_bikes +"/"+station.bike_stands + " "+station.name + " "+ last_update + "\r\n ";
                    }
                    views.setTextViewText(R.id.stationsTextView, res);
                    Date d = new Date();

                    views.setTextViewText(R.id.refreshTextView, simpleDateFormat.format(d));
                }
                // Tell the AppWidgetManager to perform an update on the current app widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }
}