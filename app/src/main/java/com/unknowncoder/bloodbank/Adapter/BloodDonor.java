package com.unknowncoder.bloodbank.Adapter;

public class BloodDonor
{
    private String fullname;
    private String imageurl;
    private int id;
    private String bloodgroup;


    public BloodDonor(String fullname, String imageurl, int id, String bloodgroup) {
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.id = id;
    }

    public BloodDonor() {
    }

    public String getFullname() {
        return fullname;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
