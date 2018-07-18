package com.example.globalnews.utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import com.example.globalnews.R;
import com.example.globalnews.widget.StarredArticlesWidget;

// Update Widget Manually: https://developer.android.com/guide/topics/appwidgets/#fresh
public class WidgetUtils {
    public static void updateWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, StarredArticlesWidget.class));
        if (appWidgetIds.length > 0) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_view);
        }
    }
}
