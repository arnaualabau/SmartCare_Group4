package com.example.smartcare_group4.ui.main.planning;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.EventDAO;
import com.example.smartcare_group4.data.repository.FirebaseRepository;

import java.time.LocalDate;

public class PlanningViewModel extends ViewModel {

    public PlanningViewModel() {

    }

    public boolean isPatient() {
        return FirebaseRepository.firebaseInstance.isPatient();
    }

    public MutableLiveData<String> saveEvent(String medSelected, LocalDate selectedDate) {
        MutableLiveData<String> data = new MutableLiveData<>();
        data = FirebaseRepository.firebaseInstance.saveEvent(medSelected, selectedDate);
        return data;
    }

    public MutableLiveData<EventDAO> subscribePlanning() {
        MutableLiveData<EventDAO> observable = new MutableLiveData<>();

        observable = (MutableLiveData<EventDAO>) FirebaseRepository.firebaseInstance.subscribeToPlanning();
        return observable;
    }
}