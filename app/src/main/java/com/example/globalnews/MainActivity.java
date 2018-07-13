package com.example.globalnews;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recycler_view);
        CoordinatorLayout coordinatorLayout = findViewById(R.id.main_activity_coordinator_layout);

        mGlobalNewsAdapter = new GlobalNewsAdapter(this, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mGlobalNewsAdapter);


        if (NetworkIsAvailable()) {
            NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
            model.getNews().observe(this, new Observer<ArrayList<News>>() {
                @Override
                public void onChanged(@Nullable ArrayList<News> newsArrayList) {
                    showProgressBar();
                    mGlobalNewsAdapter.swapNewsArrayList(newsArrayList);
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
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
