package com.willycode.keepintouch.Contacts.Utils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.willycode.keepintouch.BuildConfig;
import com.willycode.keepintouch.Contacts.Model.Contact;
import com.willycode.keepintouch.Contacts.Model.ContactContract;
import com.willycode.keepintouch.Contacts.Model.ContactDbHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Manuel ELO'O on 11/01/2016.
 */
public class CallEngineService extends IntentService {
    public CallEngineService() {
        super("CallEngineService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Do the task here
        Log.i("CallEngineService", "Service running");
        ContactContract cc = new ContactContract(getApplicationContext());
        List<Contact> contacts = cc.getAllContacts();
        for (Contact item : contacts) {
            Date lastCallDate = item.getLastCallTime();
            String Period = item.getPeriod();
            //calculate the time
            Date today = Calendar.getInstance().getTime();
            long diff = today.getTime() - lastCallDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            long weeks = days / 7;
            long months = weeks / 4;
            long years =  months / 12;
            boolean callDay = false;
            if(days >= 1 && item.getPeriod().equals(Contact.DAILY))
            {
                callDay = true;
            } else if(weeks >= 1 && item.getPeriod().equals(Contact.WEEKLY))
            {
                callDay = true;
            } else if(months >= 1 && item.getPeriod().equals(Contact.MONTHLY))
            {
                callDay = true;
            } else if(years >= 1 && item.getPeriod().equals(Contact.YEARLY))
            {
                callDay = true;
            }
            if(callDay || BuildConfig.DEBUG)
            {
                Intent i = new Intent("com.willycode.keepintouch.CALL_ACTION");
                i.putExtra(ContactDbHelper.ContactEntry.COLUMN_NAME,item.getName());
                sendBroadcast(i);
            }
        }
    }
}