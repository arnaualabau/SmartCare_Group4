package com.example.smartcare_group4.ui.main.planning;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.Event;
import com.example.smartcare_group4.data.EventDAO;
import com.example.smartcare_group4.data.repository.FirebaseRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

    public LiveData<ArrayList<EventDAO>> getPlanningInfo() {

        MutableLiveData<ArrayList<EventDAO>> data = (MutableLiveData<ArrayList<EventDAO>>) FirebaseRepository.firebaseInstance.getPlanningInfo();

        return data;
    }

    public void setPlanning(ArrayList<EventDAO> planning) {

        Event.eventsList.clear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        LocalDate date = LocalDate.parse(planning.get(0).getDate(), formatter);

        Log.d("PLAN", planning.get(0).getName() + " "+ planning.get(0).getDate());

        Event.eventsList.add(new Event(planning.get(0).getName(), date));
    }
}