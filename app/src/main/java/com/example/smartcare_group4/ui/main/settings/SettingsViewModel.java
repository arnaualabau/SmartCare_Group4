package com.example.smartcare_group4.ui.main.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class SettingsViewModel extends ViewModel {

    public SettingsViewModel() {

    }


    public MutableLiveData<String> checkOldPassword(String oldPSWtext) {

        return FirebaseRepository.firebaseInstance.checkCredentials(oldPSWtext);
    }

    //Check that the new passwords are ok
    public boolean checkNewPasswords(String newPSWtext, String newPSW2text) {
        //both passwords have to be the same
        if (newPSWtext.equals(newPSW2text)) {
            //passswords should at least have 6 characters (Firebase rules)
           if (newPSWtext.length() >= 6)  {
               return true;
           } else {
               return false;
           }
        }
        return false;
    }

    public MutableLiveData<String> changePassword(String newPSWtext) {
        MutableLiveData<String> resultat = FirebaseRepository.firebaseInstance.changePSW(newPSWtext);

        return resultat;
    }
    //Check if fields are empty
    public boolean emptyTexts(String s1) {
        if (s1.equals("")) {
            return true;
        }
        return false;
    }
}