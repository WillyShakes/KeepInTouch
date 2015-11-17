package com.willycode.keepintouch.Contacts.Presenter;

import com.willycode.keepintouch.Contacts.Utils.OnFinishedListener;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public interface ContactListInteractor {
    public void loadContacts(OnFinishedListener listener);
    public void addNewContact(String name);
}
