package com.example.smartcare_group4.data;

import java.time.LocalDate;

public class Event {


    private String name;
    private LocalDate date;
    private String taken;

    public Event(String name, LocalDate date, String taken) {

        this.name = name;
        this.date = date;
        this.taken = taken;
    }

    public String getTaken() {
        return taken;
    }

    public void setTaken(String taken) {
        this.taken = taken;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
