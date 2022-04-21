package com.example.smartcare_group4.data.user;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {UserItem.class}, version = 1)
public abstract class UserItemDatabase extends RoomDatabase {
    public abstract UserItemDao userDao();
}
