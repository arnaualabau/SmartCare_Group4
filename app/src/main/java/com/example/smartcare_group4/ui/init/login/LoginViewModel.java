package com.example.smartcare_group4.ui.init.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    //es un controlador
    //aixo ha de cridar el repository
    private final MutableLiveData<String> mText;

    public LoginViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> login(String username, String password) {
        MutableLiveData<String> data = new MutableLiveData<>();
        return data;
    }
}