package com.example.smartcare_group4.ui.init.signup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class SignupViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SignupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is signup fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> signUp(String email, String password) {

        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.signUpFirebase(email, password);

        return data;

    }

    public LiveData<String> registerUser(String name, String email, String password, String id, String hardwareId, boolean patient) {

        //MutableLiveData<String> data = (MutableLiveData<String>) firebase.registerUser(name, email, password, id, hardwareId, patient);
        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.registerUser(name, email, password, id, hardwareId, patient);

        return data;

    }

    /*public LiveData<String> profilePicture(String name, String email, String password, String id, String hardwareId, boolean patient) {


        return ;

    }*/

    public boolean checkPSW(String psw1, String psw2) {

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

    public boolean checkPermissions() {



        return true;
    }

    public LiveData<String> startCamera(String email, String password) {

        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.signUpFirebase(email, password);

        return data;

    }

}