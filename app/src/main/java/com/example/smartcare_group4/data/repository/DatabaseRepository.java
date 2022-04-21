package com.example.smartcare_group4.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.smartcare_group4.data.user.UserItem;
import com.example.smartcare_group4.data.user.UserItemDao;
import com.example.smartcare_group4.data.user.UserItemDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseRepository {

    private UserItemDatabase userItemDatabase;
    private UserItemDao userItemDao;
    private Executor executor;

    public DatabaseRepository(Context context) {
        //user constants
        userItemDatabase = Room.databaseBuilder(context, UserItemDatabase.class, "USERITEMDATABASE.db").build();
        userItemDao = userItemDatabase.userDao();

        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<String> saveUser(UserItem userItem) {

        MutableLiveData<String> observable = new MutableLiveData<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                userItemDao.insert(userItem); //TODO return value from insert?
                observable.postValue("success");
            }
        });

        return observable;
    }


}
