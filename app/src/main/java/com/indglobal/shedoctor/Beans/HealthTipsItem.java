package com.indglobal.shedoctor.Beans;

/**
 * Created by Android on 9/28/16.
 */
public class HealthTipsItem {

    String id,title,content,image,active,slug,posted_at;

    public HealthTipsItem(String id, String title, String content, String image, String active, String slug,String posted_at) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.active = active;
        this.slug = slug;
        this.posted_at = posted_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }
}
