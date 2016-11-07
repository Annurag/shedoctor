package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 9/26/16.
 */
public class MedicinTypeItem {

    String id,medicin;

    public MedicinTypeItem(String id, String medicin) {
        this.id = id;
        this.medicin = medicin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicin() {
        return medicin;
    }

    public void setMedicin(String medicin) {
        this.medicin = medicin;
    }
}
