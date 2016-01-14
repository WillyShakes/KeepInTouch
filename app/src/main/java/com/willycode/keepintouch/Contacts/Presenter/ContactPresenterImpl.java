package com.willycode.keepintouch.Contacts.Presenter;

import android.content.Context;

import com.willycode.keepintouch.Contacts.Model.Contact;
import com.willycode.keepintouch.Contacts.Utils.OnFinishedListener;
import com.willycode.keepintouch.Contacts.View.ContactListView;

import java.util.List;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class ContactPresenterImpl implements ContactPresenter,OnFinishedListener{
    private ContactListView contactListView;
    private ContactListInteractor contactListInteractor;
    private Context context;

    public ContactPresenterImpl(ContactListView contactListView,Context c) {
        this.contactListView = contactListView;
        contactListInteractor = new ContactListInteractorImpl();
        this.context = c;
    }

    @Override public void onResume() {
        contactListView.showProgress();
        contactListInteractor.loadContacts(this,context);
    }

    @Override public void onItemClicked(int position) {
        contactListView.showMessage(String.format("Position %d clicked", position + 1));
    }

    @Override public void onFinished(List<Contact> contacts) {
        contactListView.setContacts(contacts);
        contactListView.hideProgress();
    }

    @Override
    public void onError(Exception error) {
        contactListView.hideProgress();
        contactListView.showMessage(error.getMessage());
    }
}
