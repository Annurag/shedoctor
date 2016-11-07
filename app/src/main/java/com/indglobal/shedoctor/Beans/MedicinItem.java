package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 10/1/16.
 */
public class MedicinItem {

    String name,type,morning,afternoon,evening,night,note;

    public MedicinItem(String name, String type, String morning, String afternoon, String evening, String night, String note) {
        this.name = name;
        this.type = type;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
