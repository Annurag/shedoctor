package com.indglobal.shedoctor.Beans;

/**
 * Created by SONY on 30/09/2016.
 */
public class LedgerItem {

    String appointment_id, date, patient_id, transaction_id, consultation_type, patient_rs, shedoctr_rs, doctor_rs, other_details;

    public LedgerItem(String appointment_id, String date, String patient_id, String transaction_id, String consultation_type, String patient_rs, String shedoctr_rs, String doctor_rs, String other_details) {
        this.appointment_id = appointment_id;
        this.date = date;
        this.patient_id = patient_id;
        this.transaction_id = transaction_id;
        this.consultation_type = consultation_type;
        this.patient_rs = patient_rs;
        this.shedoctr_rs = shedoctr_rs;
        this.doctor_rs = doctor_rs;
        this.other_details = other_details;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getConsultation_type() {
        return consultation_type;
    }

    public void setConsultation_type(String consultation_type) {
        this.consultation_type = consultation_type;
    }

    public String getPatient_rs() {
        return patient_rs;
    }

    public void setPatient_rs(String patient_rs) {
        this.patient_rs = patient_rs;
    }

    public String getShedoctr_rs() {
        return shedoctr_rs;
    }

    public void setShedoctr_rs(String shedoctr_rs) {
        this.shedoctr_rs = shedoctr_rs;
    }

    public String getDoctor_rs() {
        return doctor_rs;
    }

    public void setDoctor_rs(String doctor_rs) {
        this.doctor_rs = doctor_rs;
    }

    public String getOther_details() {
        return other_details;
    }

    public void setOther_details(String other_details) {
        this.other_details = other_details;
    }
}