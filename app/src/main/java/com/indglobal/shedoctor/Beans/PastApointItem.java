package com.indglobal.shedoctor.Beans;

import java.util.ArrayList;

/**
 * Created by SONY on 27/09/2016.
 */
public class PastApointItem{

    String id,pname,pid,strt,date,time,type,height,bloodgroup,weight,purpose,medications,allergies,prescription,success_status,reason,details,status;
    ArrayList<String> lstReport;


    public PastApointItem(String id, String pname, String pid, String strt, String date, String time, String type, String height,
                          String bloodgroup, String weight, String purpose, String medications, String allergies,String prescription, String success_status, String reason, String details, String status, ArrayList<String> lstReport) {
        this.id = id;
        this.pname = pname;
        this.pid = pid;
        this.strt = strt;
        this.date = date;
        this.time = time;
        this.type = type;
        this.height = height;
        this.bloodgroup = bloodgroup;
        this.weight = weight;
        this.purpose = purpose;
        this.medications = medications;
        this.allergies = allergies;
        this.prescription = prescription;
        this.success_status = success_status;
        this.reason = reason;
        this.details = details;
        this.status = status;
        this.lstReport = lstReport;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStrt() {
        return strt;
    }

    public void setStrt(String strt) {
        this.strt = strt;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getSuccess_status() {
        return success_status;
    }

    public void setSuccess_status(String success_status) {
        this.success_status = success_status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getLstReport() {
        return lstReport;
    }

    public void setLstReport(ArrayList<String> lstReport) {
        this.lstReport = lstReport;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}