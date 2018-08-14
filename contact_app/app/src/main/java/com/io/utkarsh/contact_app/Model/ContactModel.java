package com.io.utkarsh.contact_app.Model;

/**
 * Created by Utkarsh-PC on 8/14/2018.
 */

public class ContactModel {
    public ContactModel(String userName, String contactNumber) {
        this.userName = userName;
        this.contactNumber = contactNumber;
    }

    public String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String contactNumber;



}
