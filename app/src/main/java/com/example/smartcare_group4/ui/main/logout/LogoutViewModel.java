package com.example.smartcare_group4.ui.main.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class LogoutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private FirebaseRepository firebase = new FirebaseRepository();

    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Are you sure you want to log out?");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void signOut() {
        firebase.signOut();
    }
}