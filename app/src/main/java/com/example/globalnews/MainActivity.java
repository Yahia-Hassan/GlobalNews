package com.example.globalnews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GlobalNewsAdapter.NewsOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();


    private RecyclerView mRecyclerView;
    private GlobalNewsAdapter mGlobalNewsAdapter;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    //private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recycler_view);

        mGlobalNewsAdapter = new GlobalNewsAdapter(this, this);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mGlobalNewsAdapter);

        showProgressBar();

//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
//
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });


        if (NetworkIsAvailable()) {
            NewsViewModel model = ViewModelProviders.of(this).get(NewsViewModel.class);
            model.getNews().observe(this, new Observer<ArrayList<News>>() {
                @Override
                public void onChanged(@Nullable ArrayList<News> newsArrayList) {
                    showRecyclerView();
                    mGlobalNewsAdapter.swapNewsArrayList(newsArrayList);
                }
            });
        } else {
            AlertUserDialog();

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

    private boolean NetworkIsAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkState = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            networkState = true;
        }
        return networkState;
    }

    private void AlertUserDialog() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "network_error");
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
