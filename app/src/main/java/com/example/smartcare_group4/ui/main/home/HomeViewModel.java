package com.example.smartcare_group4.ui.main.home;

import android.os.Bundle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment. Jo aqui posaria tres botons: sensors, planning i emergencia.");


    }

    public LiveData<String> getText() {
        return mText;
    }
}