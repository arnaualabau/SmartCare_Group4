package com.example.smartcare_group4.ui.main.planning;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class PlanningViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PlanningViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is PLANNING fragment");
    }

    public boolean isPatient() {
        return FirebaseRepository.firebaseInstance.isPatient();
    }

    public LiveData<String> getText() {
        return mText;
    }
}