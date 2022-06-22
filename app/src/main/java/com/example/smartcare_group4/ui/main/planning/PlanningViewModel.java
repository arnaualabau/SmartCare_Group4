package com.example.smartcare_group4.ui.main.planning;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.Event;
import com.example.smartcare_group4.data.EventDAO;
import com.example.smartcare_group4.data.repository.FirebaseRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class PlanningViewModel extends ViewModel {

    private static MutableLiveData<ArrayList<Event>> eventsList;

    public PlanningViewModel() {

        eventsList = new MutableLiveData<>();// new ArrayList<Event>();
        eventsList.setValue(new ArrayList<>());
    }

    public static MutableLiveData<ArrayList<Event>> getEventsList() {
        return eventsList;
    }

    //Get events of a specific date
    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList.getValue()) {
            if(event.getDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }

    //Subscribe to values of planning in firebase
    public MutableLiveData<ArrayList<EventDAO>> subscribePlanning() {

        MutableLiveData<ArrayList<EventDAO>> observable = (MutableLiveData<ArrayList<EventDAO>>) FirebaseRepository.firebaseInstance.subscribeToPlanning();
        return observable;
    }

    public void setPlanning(ArrayList<EventDAO> planning) {

        ArrayList<Event> events = new ArrayList<>();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        for (EventDAO eventDAO: planning) {
            if (!eventDAO.getName().equals("empty")) {
                LocalDate date = LocalDate.parse(eventDAO.getDate(), formatter);
                events.add(new Event(eventDAO.getName(), date, eventDAO.getTaken()));
            }
        }

        eventsList.getValue().clear();

        eventsList.setValue(events);

    }

    public boolean isPatient() {
        return FirebaseRepository.firebaseInstance.isPatient();
    }

    //Save event in firebase, using the medicine needed and the date
    public MutableLiveData<String> saveEvent(String medSelected, LocalDate selectedDate) {
        MutableLiveData<String> data = new MutableLiveData<>();
        data = FirebaseRepository.firebaseInstance.saveEvent(medSelected, selectedDate);
        return data;
    }

    //Delete event from firebase
    public LiveData<String> deleteEvent(LocalDate selectedDate) {

        MutableLiveData<String> data = new MutableLiveData<>();
        data = FirebaseRepository.firebaseInstance.deleteEvent(selectedDate);
        return data;
    }

    //Medicine taken, change status on firebase
    public LiveData<String> takeMedicine(LocalDate selectedDate, String medicineDetected, String medicinePlanned) {

        MutableLiveData<String> data = new MutableLiveData<>();

        String result = medicineDetected;

        if (medicinePlanned.toLowerCase(Locale.ROOT).equals(medicineDetected.toLowerCase(Locale.ROOT))) {
            result = "yes";
        }

        data = FirebaseRepository.firebaseInstance.takeMedicine(selectedDate, result);

        return data;
    }

    //Check if there is or not a med plan for the week ahead
    public boolean noWeekPlan() {

        for (int i = 1; i <= 8; i++) {

            LocalDate date = LocalDate.now().plusDays(i);
            for (Event event : eventsList.getValue()) {
                if (event.getDate().equals(date)) {
                    return false;
                }
            }
        }

        return true;
    }


    public LiveData<String> processMedPicture(Bitmap img, int rotation) {

        MutableLiveData<String> data = (MutableLiveData<String>) FirebaseRepository.firebaseInstance.processMedPicture(img, rotation);

        return data;
    }
}