package com.example.globalnews;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "news_table")
public class News {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "urlToImage")
    private String mUrlToImage;

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

