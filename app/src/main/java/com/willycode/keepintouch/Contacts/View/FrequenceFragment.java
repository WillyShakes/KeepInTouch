package com.willycode.keepintouch.Contacts.View;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.willycode.keepintouch.Contacts.Model.Contact;
import com.willycode.keepintouch.Contacts.Model.ContactContract;
import com.willycode.keepintouch.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Manuel ELO'O on 18/11/2015.
 */
public class FrequenceFragment extends DialogFragment implements DialogInterface.OnClickListener{
     long time = 0;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            int pos = 1;
            ContactContract cc = new ContactContract(getContext());
            List<Contact> contacts = cc.getAllContacts();
            if(!contacts.isEmpty()) {
                String period = contacts.get(0).getPeriod();

                switch (period) {
                    case Contact.DAILY:
                        pos = 0;
                        break;
                    case Contact.WEEKLY:
                        pos = 1;
                        break;
                    case Contact.MONTHLY:
                        pos = 2;
                        break;
                    case Contact.YEARLY:
                        pos = 3;
                        break;
                }
            }
            builder.setTitle(R.string.choose_frequence)
                    .setSingleChoiceItems(R.array.frequences_array, pos, this)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(MainActivity.Frequence, "" + which);
        editor.commit();
        String period = null;
        switch (which){
            case 0:
                //24 ore
                time = TimeUnit.DAYS.toMillis(1);
                period = Contact.DAILY;
                break;
            case 1:
                //ogni 7 giorno
                time = TimeUnit.DAYS.toMillis(7);
                period = Contact.WEEKLY;
                break;
            case 2:
                //ogni 4 settimane
                time = TimeUnit.DAYS.toMillis(7)*4;
                period = Contact.MONTHLY;
                break;
            case 3:
                //ogni 365 giorni
                time = TimeUnit.DAYS.toMillis(365);
                period = Contact.YEARLY;
                break;
        }

        ContactContract cc = new ContactContract(getContext());
        List<Contact> contacts = cc.getAllContacts();
        for (Contact item : contacts) {
            item.setPeriod(period);
            item.setLastCallTime(new Date());
            cc.updateContact(item);
        }
    }
}
