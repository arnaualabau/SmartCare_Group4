package com.example.smartcare_group4.data.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserItemDao {

    @Query("SELECT * FROM UserItem")
    List<UserItem> getAll();

    @Query("SELECT * FROM UserItem WHERE id IN (:userIds)")
    List<UserItem> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM UserItem WHERE name LIKE :name AND " +
            "email LIKE :email LIMIT 1")
    UserItem findByName(String name, String email);

    @Insert
    void insertAll(List<UserItem> userItems);

    @Insert
    void insert(UserItem userItem);

    @Delete
    void delete(UserItem user);
}
