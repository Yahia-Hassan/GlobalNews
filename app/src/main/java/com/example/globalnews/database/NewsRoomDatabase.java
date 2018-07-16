package com.example.globalnews.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.globalnews.News;
import com.example.globalnews.R;

@Database(entities = {News.class}, version = 1)
public abstract class NewsRoomDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    private static NewsRoomDatabase INSTANCE;

    public static NewsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NewsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NewsRoomDatabase.class, "news_database")
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
