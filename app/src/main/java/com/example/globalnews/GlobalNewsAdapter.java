package com.example.globalnews;


import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class GlobalNewsAdapter extends RecyclerView.Adapter<GlobalNewsAdapter.GlobalNewsViewHolder> {

    private static final String TAG = GlobalNewsAdapter.class.getSimpleName();

    private Context mContext;
    private List<News> mNewsList;
    private List<News> mStarredNewsList;

    final private NewsOnClickHandler mNewsOnClickHandler;

    public interface NewsOnClickHandler {
        void onClickHandler(String url);

        void onStarClickHandler(News news, boolean isStarred);
    }


    public GlobalNewsAdapter(Context context, NewsOnClickHandler newsOnClickHandler) {
        mContext = context;
        mNewsOnClickHandler = newsOnClickHandler;
    }

    @NonNull
    @Override
    public GlobalNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);

        return new GlobalNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GlobalNewsViewHolder holder, int position) {
            holder.articleTitle.setText(mNewsList.get(position).getTitle());
            Picasso.get()
                    .load(mNewsList.get(position).getUrlToImage())
                    .error(R.color.imageError)
                    .into(holder.articleImage);

            if (isArticleStarred(mNewsList.get(position))) {
                holder.articleStarIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_24dp));
            }
    }

    private boolean isArticleStarred(News news) {
        boolean isStarred = false;
        if (mStarredNewsList != null) {
            for (int i = 0; i < mStarredNewsList.size(); i++) {
                if (news.getTitle().equals(mStarredNewsList.get(i).getTitle())) {
                    isStarred = true;
                    break;
                }
            }
        }
        return isStarred;
    }

    @Override
    public int getItemCount() {
        if (mNewsList == null) {
            return 0;
        } else {
            return mNewsList.size();
        }
    }


    public void swapStarredNewsList(List<News> starredNewsList) {
        if (starredNewsList != null) {
            mStarredNewsList = starredNewsList;
        }
    }

    public void swapNewsList(List<News> newsList) {
        if (newsList != null) {
            mNewsList = newsList;
        }
    }

    public class GlobalNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView articleTitle;
        ImageView articleImage;
        ImageButton articleStarIcon;
        public GlobalNewsViewHolder(View item) {
            super(item);
            articleTitle = item.findViewById(R.id.article_title);
            articleImage = item.findViewById(R.id.article_image);
            articleStarIcon = item.findViewById(R.id.article_star_icon);
            item.setOnClickListener(this);
            articleStarIcon.setOnClickListener(this);

        }

        // Multiple onClick events inside a RecyclerView: https://stackoverflow.com/a/30285361
        @Override
        public void onClick(View v) {
            if (v.getId() == articleStarIcon.getId()) {
                News news = mNewsList.get(getAdapterPosition());
                boolean isStarred = isArticleStarred(news);
                if (isStarred) {
                    articleStarIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_border_24dp));
                } else {
                    articleStarIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_star_24dp));
                }
                mNewsOnClickHandler.onStarClickHandler(news, isStarred);

            } else {
                String articleUrl = mNewsList.get(getAdapterPosition()).getUrl();
                mNewsOnClickHandler.onClickHandler(articleUrl);
            }
        }
    }
}
