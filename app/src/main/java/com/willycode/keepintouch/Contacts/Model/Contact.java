package com.willycode.keepintouch.Contacts.Model;

import java.util.Date;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class Contact {
    public static final String DAILY = "daily";
    public static final String WEEKLY = "weekly";
    public static final String MONTHLY = "monthly";
    public static final String YEARLY = "yearly";
    private String Name;
    private String Period;

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public Date getLastCallTime() {
        return lastCallTime;
    }

    public void setLastCallTime(Date lastCallTime) {
        this.lastCallTime = lastCallTime;
    }

    private Date lastCallTime;

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
