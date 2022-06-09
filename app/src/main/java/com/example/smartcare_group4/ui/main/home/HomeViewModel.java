package com.example.smartcare_group4.ui.main.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment. Jo aqui posaria tres botons: sensors, planning i emergencia.");


    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<String> setValuesEmergency() {
        MutableLiveData<String> data = new MutableLiveData<String>();
        data = FirebaseRepository.firebaseInstance.setValuesEmergency();

        return data;
    }
}