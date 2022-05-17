package com.example.smartcare_group4.ui.init.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignupViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SignupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is signup fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> signUp(String email, String password) {

        MutableLiveData<String> data = new MutableLiveData<>();
        return data;

    }
}