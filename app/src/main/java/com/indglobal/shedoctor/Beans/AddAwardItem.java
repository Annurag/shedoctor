package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 10/11/16.
 */
public class AddAwardItem {

    int id,rlid, year;
    String awardname,awarddtl;

    public AddAwardItem(int id, int rlid,String awardname,String awarddtl, int year) {
        this.id = id;
        this.rlid = rlid;
        this.awardname = awardname;
        this.awarddtl = awarddtl;
        this.year = year;
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

    public String getAwardname() {
        return awardname;
    }

    public void setAwardname(String awardname) {
        this.awardname = awardname;
    }

    public String getAwarddtl() {
        return awarddtl;
    }

    public void setAwarddtl(String awarddtl) {
        this.awarddtl = awarddtl;
    }
}

