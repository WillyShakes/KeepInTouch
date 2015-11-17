package com.willycode.keepintouch.Contacts.Presenter;

import android.os.Handler;

import com.willycode.keepintouch.Contacts.Utils.OnFinishedListener;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class ContactListInteractorImpl implements  ContactListInteractor {

    @Override
    public void loadContacts(final OnFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                listener.onFinished(createArrayList());
            }
        }, 2000);
    }

    @Override
    public void addNewContact(String name) {

    }

    private List<String> createArrayList() {
        return Arrays.asList(
                "Contact 1",
                "Contact 2",
                "Contact 3",
                "Contact 4",
                "Contact 5",
                "Contact 6",
                "Contact 7",
                "Contact 8",
                "Contact 9",
                "Contact 10"
        );
    }
}
