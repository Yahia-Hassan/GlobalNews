package com.example.globalnews;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.globalnews.utils.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsViewModel extends AndroidViewModel {

    private static final String API_KEY = BuildConfig.API_KEY;
    ArrayList<News> newsArrayList;

    private final MutableLiveData<ArrayList<News>> newsLiveData = new MutableLiveData<>();

    public NewsViewModel(@NonNull Application application) {
        super(application);
        loadNews(application);
    }

    public LiveData<ArrayList<News>> getNews() {
        return newsLiveData;
    }

    private void loadNews(final Context context) {
        new AsyncTask<Void, Void, String>() {
            String response;
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                String countryCode = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(context.getString(R.string.pref_choose_your_country_key), context.getString(R.string.pref_country_us));

                String newsCategory = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(context.getString(R.string.pref_category_key), context.getString(R.string.pref_category_general));

                //https://newsapi.org/v2/top-headlines?country=us&categeory=general&apiKey=" + APIKey.APIKey
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("https")
                        .authority("newsapi.org")
                        .appendPath("v2")
                        .appendPath("top-headlines")
                        .appendQueryParameter("country", countryCode)
                        .appendQueryParameter("categeory", newsCategory)
                        .appendQueryParameter("apiKey", API_KEY); // You can get your API key at "https://newsapi.org/register"
                String url = builder.build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {
                    response = client.newCall(request).execute().body().string();
                } catch (IOException e) {
                    response = null;
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String data) {
                super.onPostExecute(data);
                ArrayList<News> newsArrayList = JSONUtils.createNewsArrayList(data);
                newsLiveData.setValue(newsArrayList);
            }
        }.execute();
    }
}
