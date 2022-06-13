package com.example.smartcare_group4.ui.main.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class HomeViewModel extends ViewModel {

    public HomeViewModel() {
    }


    public MutableLiveData<String> setValuesEmergency() {
        MutableLiveData<String> data = new MutableLiveData<String>();
        data = FirebaseRepository.firebaseInstance.setValuesEmergency();

        return data;
    }
}