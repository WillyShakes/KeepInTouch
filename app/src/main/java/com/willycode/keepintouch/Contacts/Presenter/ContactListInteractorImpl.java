package com.willycode.keepintouch.Contacts.Presenter;

import android.content.Context;
import android.os.Handler;

import com.willycode.keepintouch.Contacts.Model.Contact;
import com.willycode.keepintouch.Contacts.Model.ContactContract;
import com.willycode.keepintouch.Contacts.Utils.OnFinishedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class ContactListInteractorImpl implements  ContactListInteractor {

    @Override
    public void loadContacts(final OnFinishedListener listener, final Context c) {
        listener.onFinished(createArrayList(c));
    }

    @Override
    public void addNewContact(String name) {

    }

    private List<String> createArrayList(Context c) {
        ContactContract cc = new ContactContract(c);
        List<Contact> l = cc.getAllContacts();
        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < l.size(); i++) {
            ret.add(l.get(i).getName());
        }
        return ret;
    }
}
