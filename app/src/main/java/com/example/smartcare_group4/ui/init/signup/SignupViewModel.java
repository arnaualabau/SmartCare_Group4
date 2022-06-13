package com.example.smartcare_group4.ui.init.signup;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class SignupViewModel extends ViewModel {

    public SignupViewModel() {}

    public LiveData<String> signUp(String email, String password) {

        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.signUpFirebase(email, password);

        return data;

    }

    public LiveData<String> registerUser(String name, String email, String id, String hardwareId, boolean patient) {

        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.registerUser(name, email, id, hardwareId, patient);

        return data;

    }

    public LiveData<String> storeProfilePicture(String email, Bitmap imageBitmap) {

        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.storeProfilePicture(email, imageBitmap);

        return data;

    }

    public boolean checkPSW(String psw1, String psw2) {
        //Firebase password requirements
        if(psw1.equals(psw2)) {
            if (psw1.length() < 6) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public boolean emptyTexts(String s1) {
        if (s1.equals("")) {
            return true;
        }
        return false;
    }
}