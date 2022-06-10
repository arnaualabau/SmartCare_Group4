package com.example.smartcare_group4.data;

public class EventDAO {

    private String name;
    private String date;
    private String taken;

    public EventDAO(){
    }

    public EventDAO(String name, String date) {

        this.name = name;
        this.date = date;
        this.taken = "no";
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

    public String getDate()
    {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
