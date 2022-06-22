package com.example.smartcare_group4.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class MainViewModel extends ViewModel {


    public MainViewModel() {
    }

    public MutableLiveData<byte[]> getProfilePicture() {

        MutableLiveData<byte[]> observable = (MutableLiveData<byte[]>) FirebaseRepository.firebaseInstance.getProfilePicture();
        return observable;
    }

    public LiveData<String> setValuesEmergency() {

        MutableLiveData<String> observable = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.setValuesEmergency();
        return observable;
    }

    public boolean imageTaken() {
        return FirebaseRepository.firebaseInstance.imageTaken();
    }

    public LiveData<Boolean> subscribeEmergency() {
        MutableLiveData<Boolean> observable = new MutableLiveData<>();

        observable = (MutableLiveData<Boolean>) FirebaseRepository.firebaseInstance.subscribeEmergency();
        return observable;
    }

    public LiveData<String> setEmergencyOff() {
        MutableLiveData<String> observable = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.setEmergencyOff();
        return observable;

    }
}
