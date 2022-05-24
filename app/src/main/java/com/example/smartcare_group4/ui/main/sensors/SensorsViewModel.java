package com.example.smartcare_group4.ui.main.sensors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SensorsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SensorsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SENSORS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}