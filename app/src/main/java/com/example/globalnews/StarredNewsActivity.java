package com.example.globalnews;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.globalnews.database.NewsRepository;
import com.example.globalnews.database.NewsRoomDatabase;

import java.util.ArrayList;
import java.util.List;

public class StarredNewsActivity extends AppCompatActivity implements GlobalNewsAdapter.NewsOnClickHandler {

    private RecyclerView mStarredNewsRecyclerView;
    private GlobalNewsAdapter mGlobalNewsAdapter;
    private NewsDatabaseViewModel mNewsDatabaseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_news);

        mStarredNewsRecyclerView = findViewById(R.id.starred_news_recycler_view);

        mGlobalNewsAdapter = new GlobalNewsAdapter(this, this);
        mStarredNewsRecyclerView.setHasFixedSize(true);
        mStarredNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mStarredNewsRecyclerView.setAdapter(mGlobalNewsAdapter);

        mNewsDatabaseViewModel = ViewModelProviders.of(this).get(NewsDatabaseViewModel.class);
        mNewsDatabaseViewModel.getAllStarredNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                mGlobalNewsAdapter.swapStarredNewsList(news);
                mGlobalNewsAdapter.swapNewsList(news);
                mGlobalNewsAdapter.notifyDataSetChanged();
            }
        });
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
    public void onStarClickHandler(News news, boolean isStarred) {
        if (!isStarred) {
            mNewsDatabaseViewModel.insert(news);
        } else {
            mNewsDatabaseViewModel.delete(news);
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
