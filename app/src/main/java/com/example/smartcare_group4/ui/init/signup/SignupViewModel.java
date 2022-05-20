package com.example.smartcare_group4.ui.init.signup;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class SignupViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private FirebaseRepository firebase = new FirebaseRepository();

    public SignupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is signup fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> signUp(String email, String password) {

        MutableLiveData<String> data = (MutableLiveData<String>) firebase.signUpFirebase(email, password);

        return data;

    }
}