package com.example.smartcare_group4.ui.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class HomeViewModel extends ViewModel {

    public HomeViewModel() {
    }


    public LiveData<String> setEmergency(String emergency) {
        MutableLiveData<String> observable = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.setEmergency(emergency);
        return observable;
    }
}