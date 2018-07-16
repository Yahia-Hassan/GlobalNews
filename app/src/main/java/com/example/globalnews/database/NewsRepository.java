package com.example.globalnews.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.globalnews.News;

import java.util.ArrayList;

public class NewsRepository {
    private NewsDao mNewsDao;
    private LiveData<ArrayList<News>> mAllNews;

    public NewsRepository(Application application) {
        NewsRoomDatabase db = NewsRoomDatabase.getDatabase(application);
        mNewsDao = db.newsDao();
        mAllNews = mNewsDao.getAllStarredNews();
    }

    public LiveData<ArrayList<News>> getAllStarredNews() {
        return mAllNews;
    }

    public void insert(News news) {
        new insertAsyncTask(mNewsDao).execute(news);
    }

    private static class insertAsyncTask extends AsyncTask<News, Void, Void> {

        private NewsDao mAsyncTaskDao;

        insertAsyncTask(NewsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final News... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }



    public void delete(News news) {
        new deleteAsyncTask(mNewsDao).execute(news);
    }

    private static class deleteAsyncTask extends AsyncTask<News, Void, Void> {

        private NewsDao mAsyncTaskDao;

        deleteAsyncTask(NewsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final News... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
