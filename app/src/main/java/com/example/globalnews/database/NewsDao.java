package com.example.globalnews.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.globalnews.News;

import java.util.ArrayList;

@Dao
public interface NewsDao {
    @Query("SELECT * FROM news_table")
    LiveData<ArrayList<News>> getAllStarredNews();

    @Insert
    void insert(News news);

    @Delete
    void delete(News news);
}
