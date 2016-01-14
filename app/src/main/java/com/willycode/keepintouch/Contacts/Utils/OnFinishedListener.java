package com.willycode.keepintouch.Contacts.Utils;

import com.willycode.keepintouch.Contacts.Model.Contact;

import java.util.List;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public interface OnFinishedListener {
    void onFinished(List<Contact> contact);
    void onError(Exception error);
}
