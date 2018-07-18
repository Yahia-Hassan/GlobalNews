package com.example.globalnews;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.globalnews.database.NewsRepository;
import com.example.globalnews.database.NewsRoomDatabase;
import com.example.globalnews.utils.WidgetUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

/*
    Note: Please run the app on a real device (not an emulator)
    because the emulator is having issues with location as you can see here:
    https://github.com/googlemaps/android-samples/issues/73#issuecomment-346664533
 */

public class MainActivity extends AppCompatActivity implements GlobalNewsAdapter.NewsOnClickHandler {

    public static final String TAG = MainActivity.class.getSimpleName();


    private RecyclerView mRecyclerView;
    private GlobalNewsAdapter mGlobalNewsAdapter;
    private ProgressBar mProgressBar;
    private AdView mAdView;
    private NewsDatabaseViewModel mNewsDatabaseViewModel;
    private List<News> mStarredNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.main_recycler_view);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_activity_coordinator_layout);

        MobileAds.initialize(this, getString(R.string.adMob_app_id));

        mGlobalNewsAdapter = new GlobalNewsAdapter(this, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mGlobalNewsAdapter);

        mNewsDatabaseViewModel = ViewModelProviders.of(this).get(NewsDatabaseViewModel.class);
        mNewsDatabaseViewModel.getAllStarredNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                mStarredNewsList = news;
                mGlobalNewsAdapter.swapStarredNewsList(mStarredNewsList);
                WidgetUtils.updateWidget(getBaseContext());
            }
        });


        if (NetworkIsAvailable()) {
            displayBannerAd();
            NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
            model.getNews().observe(this, new Observer<List<News>>() {
                @Override
                public void onChanged(@Nullable List<News> news) {
                    showProgressBar();
                    mGlobalNewsAdapter.swapNewsList(news);
                    mGlobalNewsAdapter.notifyDataSetChanged();
                    showRecyclerView();
                }
            });
        } else {
            hideProgressBarAndRecyclerView();
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.snackbar_text, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(R.string.retry_snackbar_action, new RetryListener(this));
            snackbar.show();

        }

    }

    private void displayBannerAd() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void showRecyclerView() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBarAndRecyclerView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    private boolean NetworkIsAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkState = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            networkState = true;
        }
        return networkState;
    }

    @Override
    public void onClickHandler(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onStarClickHandlerInsert(News news) {
        mNewsDatabaseViewModel.insert(news);
    }

    @Override
    public void onStarClickHandlerDelete(int starredItemIndex) {
        mNewsDatabaseViewModel.delete(mStarredNewsList.get(starredItemIndex));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startSettingsActivity();
                return true;

            case R.id.menu_action_favorite:
                startStarredNewsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

    private void startStarredNewsActivity() {
        Intent intent = new Intent(this, StarredNewsActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }
}
