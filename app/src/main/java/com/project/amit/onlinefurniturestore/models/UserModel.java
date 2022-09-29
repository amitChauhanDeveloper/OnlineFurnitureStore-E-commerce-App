package com.project.amit.onlinefurniturestore.models;

public class UserModel {
    String fname;
    String pnumber;
    String email;
    String pass;
    String cpass;
    String profileImg;

    public UserModel() {
    }

    public UserModel(String fname, String pnumber, String email, String pass, String cpass) {
        this.fname = fname;
        this.pnumber = pnumber;
        this.email = email;
        this.pass = pass;
        this.cpass = cpass;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCpass() {
        return cpass;
    }

    public void setCpass(String cpass) {
        this.cpass = cpass;
    }
}
