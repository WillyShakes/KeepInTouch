package com.willycode.keepintouch.Contacts.Presenter;

import com.willycode.keepintouch.Contacts.Utils.OnFinishedListener;
import com.willycode.keepintouch.Contacts.View.ContactListView;

import java.util.List;

/**
 * Created by Manuel ELO'O on 17/11/2015.
 */
public class ContactPresenterImpl implements ContactPresenter,OnFinishedListener{
    private ContactListView contactListView;
    private ContactListInteractor contactListInteractor;

    public ContactPresenterImpl(ContactListView contactListView) {
        this.contactListView = contactListView;
        contactListInteractor = new ContactListInteractorImpl();
    }

    @Override public void onResume() {
        contactListView.showProgress();
        contactListInteractor.loadContacts(this);
    }

    @Override public void onItemClicked(int position) {
        contactListView.showMessage(String.format("Position %d clicked", position + 1));
    }

    @Override public void onFinished(List<String> contacts) {
        contactListView.setContacts(contacts);
        contactListView.hideProgress();
    }

    @Override
    public void onError(Exception error) {
        contactListView.hideProgress();
        contactListView.showMessage(error.getMessage());
    }
}
