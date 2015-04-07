package com.example.justinas.bicyclewidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Justinas on 4/6/2015.
 */
public class WidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }

}