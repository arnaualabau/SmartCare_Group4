package com.example.smartcare_group4.data.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcare_group4.data.user.UserItem;

public class FirebaseRepository {


    public FirebaseRepository(){
        //init firebase libraries
    }

    public LiveData<UserItem> registerUser(String name, String email, String password){

        MutableLiveData<UserItem> observable = new MutableLiveData<>();
        UserItem userItem = new UserItem();

        //firebase method to regiter user with callback
        if(true){
            userItem.setName(name);
            userItem.setEmail(email);
            userItem.setPassword(password);
        }else{
            userItem.setError("error with firebase...");
        }
        observable.setValue(userItem);

        return observable;
    }


}
