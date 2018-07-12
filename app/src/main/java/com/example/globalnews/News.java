package com.example.globalnews;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;


public class News {
    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
    private Uri urlToImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Uri getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(Uri urlToImage) {
        this.urlToImage = urlToImage;
    }
}

