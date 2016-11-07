package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 10/11/16.
 */
public class AddEducationItem {

    int id,rlid, year;
    String collgename,collegedegree;

    public AddEducationItem(int id,int rlid, int year, String collgename, String collegedegree) {
        this.id = id;
        this.rlid = rlid;
        this.year = year;
        this.collgename = collgename;
        this.collegedegree = collegedegree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRlid() {
        return rlid;
    }

    public void setRlid(int rlid) {
        this.rlid = rlid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCollgename() {
        return collgename;
    }

    public void setCollgename(String collgename) {
        this.collgename = collgename;
    }

    public String getCollegedegree() {
        return collegedegree;
    }

    public void setCollegedegree(String collegedegree) {
        this.collegedegree = collegedegree;
    }
}
