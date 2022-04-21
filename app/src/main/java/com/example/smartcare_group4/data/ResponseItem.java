package com.example.smartcare_group4.data;

public class ResponseItem {

    public enum Status {SUCCESS, FAILURE, LOADING}

    public Status status;
    public String message;
    public Object data;

    private ResponseItem(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ResponseItem success(Object data) {
        return new ResponseItem(Status.SUCCESS, null, data);
    }

    public static ResponseItem error(String message) {
        return new ResponseItem(Status.FAILURE, message, null);
    }

    public static ResponseItem loading(String message) {
        return new ResponseItem(Status.LOADING, message, null);
    }

}
