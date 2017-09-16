package com.example.naveedanwar.chatapp;

/**
 * Created by Naveed Anwar on 17/09/2017.
 */

public class Friends  {
    private String date;

    private Friends(){

    }
    public Friends(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
