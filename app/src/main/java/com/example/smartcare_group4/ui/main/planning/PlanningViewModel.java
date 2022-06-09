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

    private static MutableLiveData<ArrayList<Event>> eventsList;

    public PlanningViewModel() {

        eventsList = new MutableLiveData<>();// new ArrayList<Event>();
        eventsList.setValue(new ArrayList<>());
    }

    public static MutableLiveData<ArrayList<Event>> getEventsList() {
        return eventsList;
    }

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList.getValue()) {
            if(event.getDate().equals(date)) {
                events.add(event);
            }
        }
        return events;
    }


    public MutableLiveData<ArrayList<EventDAO>> subscribePlanning() {

        MutableLiveData<ArrayList<EventDAO>> observable = (MutableLiveData<ArrayList<EventDAO>>) FirebaseRepository.firebaseInstance.subscribeToPlanning();
        return observable;
    }

    public void setPlanning(ArrayList<EventDAO> planning) {

        ArrayList<Event> events = new ArrayList<>();

        Log.d("PLANNING", "size Events: "+ events.size());
        Log.d("PLANNING", "size Planning: "+ planning.size());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

        for (EventDAO eventDAO: planning) {

            Log.d("PLANNING", eventDAO.getName());

            if (!eventDAO.getName().equals("empty")) {
                LocalDate date = LocalDate.parse(eventDAO.getDate(), formatter);
                events.add(new Event(eventDAO.getName(), date));
            }
        }
        Log.d("PLANNING", "size Events 2: "+ events.size());
        Log.d("PLANNING", "size EventsList: "+ eventsList.getValue().size());

        eventsList.getValue().clear();
        Log.d("PLANNING", "size EventsList 2: "+ eventsList.getValue().size());

        eventsList.setValue(events);
        Log.d("PLANNING", "size EventsList 3: "+ eventsList.getValue().size());

    }
    public boolean isPatient() {
        return FirebaseRepository.firebaseInstance.isPatient();
    }

    public MutableLiveData<String> saveEvent(String medSelected, LocalDate selectedDate) {
        MutableLiveData<String> data = new MutableLiveData<>();
        data = FirebaseRepository.firebaseInstance.saveEvent(medSelected, selectedDate);
        return data;
    }

    public LiveData<String> deleteEvent(LocalDate selectedDate) {

        MutableLiveData<String> data = new MutableLiveData<>();
        data = FirebaseRepository.firebaseInstance.deleteEvent(selectedDate);
        return data;
    }
}