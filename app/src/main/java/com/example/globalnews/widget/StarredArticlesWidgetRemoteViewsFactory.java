package com.example.globalnews.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.globalnews.News;
import com.example.globalnews.R;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class StarredArticlesWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<News> mNewsList;

    public StarredArticlesWidgetRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        WidgetAsyncTask widgetAsyncTask = new WidgetAsyncTask(mContext);
        try {
            mNewsList = widgetAsyncTask.execute().get(2L, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mNewsList == null) return 0;
        else return mNewsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mNewsList != null) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            remoteViews.setTextViewText(R.id.widget_list_item_text_view, mNewsList.get(position).getTitle());

            // Widget on click handling: https://developer.android.com/guide/topics/appwidgets/#persisting-data - Adding behavior to individual items
            Intent fillInIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.widget_list_item_text_view, fillInIntent);

            return remoteViews;
        } else {
            return null;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (mNewsList != null) {
            return position;
        } else {
            return 0;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
