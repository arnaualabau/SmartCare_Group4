package com.example.smartcare_group4.ui.main.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private FirebaseRepository firebase = new FirebaseRepository();


    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<User> getUserInfo(String id) {

        MutableLiveData<User> data = (MutableLiveData<User>) firebase.getUserInfo(id);

        return data;
    }
}