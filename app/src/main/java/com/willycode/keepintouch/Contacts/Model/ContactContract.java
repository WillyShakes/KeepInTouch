package com.willycode.keepintouch.Contacts.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuel ELO'O on 18/11/2015.
 */
public class ContactContract {


    private Context context;

    // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public ContactContract(Context c) {
            this.context = c;
        }


    // Adding new contact
    public void addContact(Contact contact) {

        // Gets the data repository in write mode
        ContactDbHelper mDbHelper = ContactDbHelper.getInstance(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContactDbHelper.ContactEntry.COLUMN_NAME_TITLE, contact.getName()); // Contact Name
        values.put(ContactDbHelper.ContactEntry.COLUMN_NAME_SUBTITLE, contact.getPhoneNumber()); // Contact Phone Number

        // Inserting Row
        db.insert(ContactDbHelper.ContactEntry.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ContactDbHelper.ContactEntry.TABLE_NAME;

        ContactDbHelper mDbHelper = ContactDbHelper.getInstance(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
               // contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(cursor.getColumnIndex(ContactDbHelper.ContactEntry.COLUMN_NAME_TITLE) ));
                contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(ContactDbHelper.ContactEntry.COLUMN_NAME_SUBTITLE)));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
}