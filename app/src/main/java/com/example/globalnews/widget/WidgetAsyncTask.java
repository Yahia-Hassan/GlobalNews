package com.example.globalnews.widget;

import android.content.Context;
import android.os.AsyncTask;

import com.example.globalnews.News;
import com.example.globalnews.database.NewsRoomDatabase;

import java.util.List;

public class WidgetAsyncTask extends AsyncTask<Void, Void, List<News>> {

    private Context mContext;

    public WidgetAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected List<News> doInBackground(Void... voids) {
        NewsRoomDatabase newsRoomDatabase = NewsRoomDatabase.getDatabase(mContext);
        return newsRoomDatabase.newsDao().getAllStarredNewsForWidget();
    }
}
