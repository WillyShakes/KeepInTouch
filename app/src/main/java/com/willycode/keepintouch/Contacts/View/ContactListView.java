package com.willycode.keepintouch.Contacts.View;

import java.util.List;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public interface  ContactListView {
    public void setContacts(List<String> contacts);

    public void showMessage(String message);

    public void hideProgress() ;

    public void showProgress() ;
    }
