package com.example.smartcare_group4.ui.init.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;
import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.User;

public class LoginViewModel extends ViewModel {
    //es un controlador
    //aixo ha de cridar el repository
    private final MutableLiveData<String> mText;
    //private FirebaseRepository firebase = new FirebaseRepository();

    public LoginViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is login fragment");
    }



    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> login(String email, String password) {
        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.loginFirebase(email, password);
        return data;
    }

    public LiveData<User> getUserInfo(String id) {

        MutableLiveData<User> data = (MutableLiveData<User>) FirebaseRepository.firebaseInstance.getUserInfo();

        return data;
    }

    public LiveData<Device> getDeviceInfo() {

        MutableLiveData<Device> data = (MutableLiveData<Device>) FirebaseRepository.firebaseInstance.getDeviceInfo();

        return data;
    }

    public void setHWId(String hardwareId) {
        FirebaseRepository.firebaseInstance.setHWId(hardwareId);
    }

    public boolean emptyTexts(String s1) {
        if (s1.equals("")) {
            return true;
        }

        return false;
    }
}