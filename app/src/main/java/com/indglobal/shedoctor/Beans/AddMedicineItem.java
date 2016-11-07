package com.indglobal.shedoctor.Beans;

/**
 * Created by SONY on 04/10/2016.
 */
public class AddMedicineItem {

    int id, medicinetype, morningtab, afternoontab, eveningtab, nighttab;
    String medicinename,medicinnote;

    public AddMedicineItem(int id, int medicinetype, int morningtab, int afternoontab, int eveningtab, int nighttab, String medicinename, String medicinnote) {
        this.id = id;
        this.medicinetype = medicinetype;
        this.morningtab = morningtab;
        this.afternoontab = afternoontab;
        this.eveningtab = eveningtab;
        this.nighttab = nighttab;
        this.medicinename = medicinename;
        this.medicinnote = medicinnote;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicinetype() {
        return medicinetype;
    }

    public void setMedicinetype(int medicinetype) {
        this.medicinetype = medicinetype;
    }

    public int getMorningtab() {
        return morningtab;
    }

    public void setMorningtab(int morningtab) {
        this.morningtab = morningtab;
    }

    public int getAfternoontab() {
        return afternoontab;
    }

    public void setAfternoontab(int afternoontab) {
        this.afternoontab = afternoontab;
    }

    public int getEveningtab() {
        return eveningtab;
    }

    public void setEveningtab(int eveningtab) {
        this.eveningtab = eveningtab;
    }

    public int getNighttab() {
        return nighttab;
    }

    public void setNighttab(int nighttab) {
        this.nighttab = nighttab;
    }

    public String getMedicinename() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getMedicinnote() {
        return medicinnote;
    }

    public void setMedicinnote(String medicinnote) {
        this.medicinnote = medicinnote;
    }
}