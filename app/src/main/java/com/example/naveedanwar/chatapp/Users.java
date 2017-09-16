package com.example.naveedanwar.chatapp;

/**
 * Created by Naveed Anwar on 14/09/2017.
 */

public class Users {

    private String username;
    private String image;
    private String status;
    private String thumbnail_image;

    public Users() {
    }

    public Users(String username, String image, String status, String thumbnail_image) {
        this.username = username;
        this.image = image;
        this.status = status;
        this.thumbnail_image = thumbnail_image;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }
}
