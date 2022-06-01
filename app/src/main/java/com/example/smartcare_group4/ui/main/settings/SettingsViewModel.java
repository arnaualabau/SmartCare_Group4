package com.example.smartcare_group4.ui.main.settings;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class SettingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is SETTINGS fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<String> checkOldPassword(String oldPSWtext) {

        return FirebaseRepository.firebaseInstance.checkCredentials(oldPSWtext);
    }

    public boolean checkNewPasswords(String newPSWtext, String newPSW2text) {
        if (newPSWtext.equals(newPSW2text)) {
           if (newPSWtext.length() >= 6)  {
               return true;
           } else {
               return false;
           }
        }
        return false;
    }

    public String  changePassword( String newPSWtext) {
        String resultat = FirebaseRepository.firebaseInstance.changePSW(newPSWtext);
        Log.d("setting change password, viewmodel", resultat);

        return resultat;
    }
}