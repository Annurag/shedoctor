package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 9/26/16.
 */
public class SpecialityItem {

    String id,specialty,slug,parent;

    public SpecialityItem(String id, String specialty, String slug, String parent) {
        this.id = id;
        this.specialty = specialty;
        this.slug = slug;
        this.parent = parent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
