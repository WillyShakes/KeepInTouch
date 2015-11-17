package com.willycode.keepintouch.Contacts.Model;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class Contact {
    private String Name;

    public Contact(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
