package com.example.smartcare_group4.ui.main.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.User;
import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> nameText;
    private final MutableLiveData<String> emailText;
    private final MutableLiveData<String> hardwareText;
    private final MutableLiveData<String> roleText;


    //private FirebaseRepository firebase = new FirebaseRepository();


    public ProfileViewModel() {
        //initialize
        mText = new MutableLiveData<>();
        nameText = new MutableLiveData<>();
        emailText = new MutableLiveData<>();
        hardwareText = new MutableLiveData<>();
        roleText = new MutableLiveData<>();
        //setter
        mText.setValue("Your Profile");
    }

    public LiveData<String> getText(int value) {
        MutableLiveData<String> defaultString = new MutableLiveData<>();
        defaultString.setValue("ERROR");

        switch (value) {
            case 0: return mText;
            case 1: return nameText;
            case 2: return emailText;
            case 3: return hardwareText;
            case 4: return roleText;
            default: return defaultString;
        }


    }

    public LiveData<User> getUserInfo() {
        Log.d("PROFILE", "GET USER INFO");

        MutableLiveData<User> data = (MutableLiveData<User>) FirebaseRepository.firebaseInstance.getUserInfo();

        return data;
    }

    public void setNameValue(String name) {
        nameText.setValue(name);
    }

    public void setEmailValue(String email) {
        emailText.setValue(email);
    }

    public void setWDIdValue(String hardwareId) {
        hardwareText.setValue(hardwareId);
    }

    public void setRoleValue(boolean patient) {
        if (patient) {
            roleText.setValue("Patient");
        } else {
            roleText.setValue("Relative");

        }
    }
}