package com.example.globalnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class RetryListener implements View.OnClickListener {
    private Context mContext;

    public RetryListener(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, MainActivity.class);
        Activity activity = (Activity) mContext;
        activity.finish();
        mContext.startActivity(intent);
    }
}
