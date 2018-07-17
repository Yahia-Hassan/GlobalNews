package com.example.globalnews.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StarredArticlesWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StarredArticlesWidgetRemoteViewsFactory(this);
    }
}
