package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 10/8/16.
 */
public class EditLanguageItem {

    String id, language, selected;
    public boolean langSelected;

    public EditLanguageItem(String id, String language, String selected) {
        this.id = id;
        this.language = language;
        this.selected = selected;
    }

    public EditLanguageItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}

