package com.project.noxusfrosted.quicktest.model;

/**
 * Created by Noxusfrosted on 11/21/2017.
 */

public class UserScore {
    private int score;
    private String userName;

    private String facebookID;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }
}
