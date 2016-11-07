package com.indglobal.shedoctor.Beans;

import java.util.Comparator;

/**
 * Created by Aman on 13-10-2016.
 */
public class DoctorSlotTimeModelDoctor {

    String date, time;
    long timeStamp;
    int timeDetails;
    int slotStatus;


    public DoctorSlotTimeModelDoctor(String date, String time, long timeStamp, int timeDetails, int slotStatus) {
        this.date = date;
        this.time = time;
        this.timeStamp = timeStamp;
        this.timeDetails = timeDetails;
        this.slotStatus = slotStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getTimeDetails() {
        return timeDetails;
    }

    public void setTimeDetails(int timeDetails) {
        this.timeDetails = timeDetails;
    }

    public int getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(int slotStatus) {
        this.slotStatus = slotStatus;
    }

    public static Comparator<DoctorSlotTimeModelDoctor> sortBySlot = new Comparator<DoctorSlotTimeModelDoctor>() {

        public int compare(DoctorSlotTimeModelDoctor dr1, DoctorSlotTimeModelDoctor dr2) {

            long timeSlotOne =  dr1.getTimeStamp();
            long timeSlotTwo =  dr2.getTimeStamp();
            return Long.valueOf(timeSlotOne).compareTo(Long.valueOf(timeSlotTwo));
            // return dr1.getTimeStamp()- dr2.getTimeStamp();
        }};
}
