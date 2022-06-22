package com.example.smartcare_group4.data;

public class Device {

    public String hardwareId;
    public int lightSensor;
    public int tap;
    public int presenceSensor;
    public float temperature;
    public float humidity;


    public Device () {}

    public Device(String hardwareId) {
        this.hardwareId = hardwareId;
        this.lightSensor = 0;
        this.tap = 0;
        this.presenceSensor = 0;
        this.temperature = 0.0f;
        this.humidity = 0.0f;
    }

    public Device(String hardwareId, int l, int t, int p, float temp, float hum) {
        this.hardwareId = hardwareId;
        this.lightSensor = l;
        this.tap = t;
        this.presenceSensor = p;
        this.temperature = temp;
        this.humidity = hum;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public int getLightSensor() {
        return lightSensor;
    }

    public void setLightSensor(int lightSensor) {
        this.lightSensor = lightSensor;
    }

    public int getTap() {
        return tap;
    }

    public void setTap(int tap) {
        this.tap = tap;
    }

    public int getPresenceSensor() {
        return presenceSensor;
    }

    public void setPresenceSensor(int presenceSensor) {
        this.presenceSensor = presenceSensor;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

}
