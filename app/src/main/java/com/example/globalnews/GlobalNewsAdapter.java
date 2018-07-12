package com.example.globalnews;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GlobalNewsAdapter extends RecyclerView.Adapter<GlobalNewsAdapter.GlobalNewsViewHolder> {

    private static final String TAG = GlobalNewsAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<News> mNewsArrayList;

    final private NewsOnClickHandler mNewsOnClickHandler;

    public interface NewsOnClickHandler {
        void onClickHandler(String url);
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
        holder.articleTitle.setText(mNewsArrayList.get(position).getTitle());
        Picasso.with(mContext)
                .load(mNewsArrayList.get(position).getUrlToImage())
                .into(holder.articleImage);
    }

    @Override
    public int getItemCount() {
        if (mNewsArrayList == null) {
            return 0;
        } else {
            return mNewsArrayList.size();
        }
    }


    public void swapNewsArrayList(ArrayList<News> newsArrayList) {
        if (newsArrayList != null) {
            mNewsArrayList = newsArrayList;
            notifyDataSetChanged();
        }
    }

    public class GlobalNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView articleTitle;
        ImageView articleImage;
        public GlobalNewsViewHolder(View item) {
            super(item);
            articleTitle = item.findViewById(R.id.article_title);
            articleImage = item.findViewById(R.id.article_image);
            item.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String articleUrl = mNewsArrayList.get(getAdapterPosition()).getUrl();
            mNewsOnClickHandler.onClickHandler(articleUrl);
        }
    }
}
