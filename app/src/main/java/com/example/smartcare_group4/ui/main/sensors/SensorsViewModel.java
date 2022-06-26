package com.example.smartcare_group4.ui.main.sensors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.smartcare_group4.data.Device;
import com.example.smartcare_group4.data.repository.FirebaseRepository;

public class SensorsViewModel extends ViewModel {

    private final MutableLiveData<String> lightText;
    private final MutableLiveData<String> tapText;
    private final MutableLiveData<String> presenceText;
    private final MutableLiveData<String> temperatureText;
    private final MutableLiveData<String> humidityText;

    public SensorsViewModel() {
        lightText = new MutableLiveData<>();
        tapText = new MutableLiveData<>();
        presenceText = new MutableLiveData<>();
        temperatureText = new MutableLiveData<>();
        humidityText = new MutableLiveData<>();
    }

    //Subscribe to sensors values in firebase to be continuously updated
    public MutableLiveData<Device> subscribeValues() {
        MutableLiveData<Device> observable = new MutableLiveData<>();

        observable = (MutableLiveData<Device>) FirebaseRepository.firebaseInstance.subscribeToValues();
        return observable;
    }

    public void setLight(int value) {
        lightText.setValue(Integer.toString(value));
    }

    public void setTap(int value) {
        tapText.setValue(Integer.toString(value));
    }

    public void setPresence(int value) {

        if (value == 1) {
            presenceText.setValue("MOTION");
        } else {
            presenceText.setValue("NO MOTION");

        }

    }

    public void setTemperature(float value) {
        temperatureText.setValue(Float.toString(value)+"º");
    }

    public void setHumidity(float value) {
        humidityText.setValue(Float.toString(value)+"%");
    }



    public LiveData<String> getText(int value) {
        MutableLiveData<String> defaultString = new MutableLiveData<>();
        defaultString.setValue("ERROR");

        switch (value) {
            case 1: return lightText;
            case 2: return tapText;
            case 3: return presenceText;
            case 4: return humidityText;
            case 5: return temperatureText;
            default: return defaultString;
        }
    }


    public LiveData<Device> getDeviceInfo() {

        MutableLiveData<Device> data = (MutableLiveData<Device>) FirebaseRepository.firebaseInstance.getDeviceInfo();

        return data;
    }

    public boolean isPatient() {
        return FirebaseRepository.firebaseInstance.isPatient();
    }

    public MutableLiveData<String> changeLightValue(int value) {
        MutableLiveData<String> data = FirebaseRepository.firebaseInstance.changeLightValue(value);
        return data;
    }

    public LiveData<String> changeTapValue(int value) {
        MutableLiveData<String> data = FirebaseRepository.firebaseInstance.changeTapValue(value);
        return data;
    }

    //check if a value is between two others
    public boolean checkStep(int step, int min, int max) {
        if (step >= min && step <= max) {
            return true;
        } else {
            return false;
        }

    }


}