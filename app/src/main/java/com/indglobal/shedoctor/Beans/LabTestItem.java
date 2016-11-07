package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 10/7/16.
 */
public class LabTestItem {

    public String labName;
    public boolean labSelected;

    public LabTestItem(String labName) {
        this.labName = labName;
    }

    public LabTestItem() {
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }
}
