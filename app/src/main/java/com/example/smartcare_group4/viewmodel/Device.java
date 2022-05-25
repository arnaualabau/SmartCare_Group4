package com.example.smartcare_group4.viewmodel;

public class Device {

    public String hardwareId;
    public int lightSensor;
    public int tap;
    public int presenceSensor;


    public Device () {}

    public Device(String hardwareId) {
        this.hardwareId = hardwareId;
        this.lightSensor = 0;
        this.tap = 0;
        this.presenceSensor = 0;
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
}
