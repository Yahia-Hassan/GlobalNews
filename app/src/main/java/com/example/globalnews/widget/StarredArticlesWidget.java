package com.example.globalnews.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.globalnews.R;
import com.example.globalnews.StarredNewsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class StarredArticlesWidget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "com.example.globalnews.widget.EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.starred_articles_widget);

        Intent intent = new Intent(context, StarredArticlesWidgetRemoteViewsService.class);
        remoteViews.setRemoteAdapter(R.id.appwidget_list_view, intent);

        Intent starredActivityIntent = new Intent(context, StarredNewsActivity.class);
        PendingIntent starredActivityPendingIntent = PendingIntent.getActivity(context, 0, starredActivityIntent, 0);
        remoteViews.setPendingIntentTemplate(R.id.appwidget_list_view, starredActivityPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}

