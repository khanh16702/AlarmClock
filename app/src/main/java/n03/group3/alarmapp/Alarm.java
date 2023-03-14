package n03.group3.alarmapp;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private int hour;
    private int minute;
    private boolean status;
    private String name;
    public Alarm(){}

    public Alarm(int hour, int minute, boolean status, String name) {
        this.hour = hour;
        this.minute = minute;
        this.status = status;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        String hourString, minitueString, format;
        if (hour > 12) {
            hourString = (hour - 12) + "";
            format = "PM";
        } else if (hour == 0) {
            hourString = "12";
            format = "PM";
        } else {
            hourString = hour + "";
            format = "AM";
        }

        if (minute < 10) {
            minitueString = "0" + minute;
        } else {
            minitueString = "" + minute;
        }
        return hourString + ":" + minitueString + format;
    }
}
