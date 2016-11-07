package com.indglobal.shedoctor.Beans;

/**
 * Created by Aman on 13-10-2016.
 */
public class DoctorDoctorTimeSlots {
    long timeSlots;
    String time;
    int status;
    public boolean brandSelected;

    public DoctorDoctorTimeSlots(long timeSlots, String time, int status) {
        this.timeSlots = timeSlots;
        this.time = time;
        this.status = status;
    }

    public DoctorDoctorTimeSlots() {

    }

    public long getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(long timeSlots) {
        this.timeSlots = timeSlots;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
