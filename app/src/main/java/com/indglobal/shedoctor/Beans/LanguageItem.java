package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 9/24/16.
 */
public class LanguageItem {

    String id,language;

    public LanguageItem(String id, String language) {
        this.id = id;
        this.language = language;
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
}
