package com.example.globalnews;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.globalnews.database.NewsRepository;

import java.util.List;

public class NewsDatabaseViewModel extends AndroidViewModel {

    private NewsRepository mRepository;

    private LiveData<List<News>> mAllStarredNews;

    public NewsDatabaseViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NewsRepository(application);
        mAllStarredNews = mRepository.getAllStarredNews();
    }

    public LiveData<List<News>> getAllStarredNews() {
        return mAllStarredNews;
    }

    public void insert(News news) {
        mRepository.insert(news);
    }

    public void delete(News news) {
        mRepository.delete(news);
    }
}
