package com.example.globalnews;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.globalnews.database.NewsRepository;

import java.util.ArrayList;

public class NewsDatabaseViewModel extends AndroidViewModel {

    private NewsRepository mRepository;

    private LiveData<ArrayList<News>> mAllNews;

    public NewsDatabaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NewsRepository(application);
        mAllNews = mRepository.getAllStarredNews();
    }

    public LiveData<ArrayList<News>> getAllNews() {
        return mAllNews;
    }

    public void insert(News news) {
        mRepository.insert(news);
    }

    public void delete(News news) {
        mRepository.delete(news);
    }
}
