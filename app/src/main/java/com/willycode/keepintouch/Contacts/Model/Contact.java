package com.willycode.keepintouch.Contacts.Model;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class Contact {
    private String Name;

    public Contact() {

    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    private String PhoneNumber;

    public Contact(String name, String phoneNumber) {
        Name = name;
        this.PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
