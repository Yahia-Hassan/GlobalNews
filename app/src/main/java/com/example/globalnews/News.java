package com.example.globalnews;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news_table")
public class News {
    @PrimaryKey (autoGenerate = true)
    private int mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "urlToImage")
    private String mUrlToImage;


    @Ignore
    public News(String title, String url, String urlToImage) {
        mTitle = title;
        mUrl = url;
        mUrlToImage = urlToImage;
    }

    public News(int id, String title, String url, String urlToImage) {
        mId = id;
        mTitle = title;
        mUrl = url;
        mUrlToImage = urlToImage;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrlToImage() {
        return mUrlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.mUrlToImage = urlToImage;
    }
}

