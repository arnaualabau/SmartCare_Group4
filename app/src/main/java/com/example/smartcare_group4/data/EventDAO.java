package com.example.smartcare_group4.data;

public class EventDAO {

    private String name;
    private String date;

    public EventDAO(){
    }

    public EventDAO(String name, String date) {

        this.name = name;
        this.date = date;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}