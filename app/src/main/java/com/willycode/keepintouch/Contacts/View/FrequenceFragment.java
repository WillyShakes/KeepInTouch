package com.willycode.keepintouch.Contacts.View;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.willycode.keepintouch.Contacts.Receivers.NotificationReceiver;
import com.willycode.keepintouch.R;

import java.util.Calendar;
import java.util.TimeZone;
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
            builder.setTitle(R.string.choose_frequence)
                    .setSingleChoiceItems(R.array.frequences_array, 1, this)
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
        editor.putString(MainActivity.Frequence, ""+which);
        editor.commit();
        //Set an ALARM for that period
        switch (which){
            case 0:
                //24 ore
                time = TimeUnit.DAYS.toMillis(1);
                break;
            case 1:
                //ogni 7 giorno
                time = TimeUnit.DAYS.toMillis(7);
                break;
            case 2:
                //ogni 4 settimane
                time = TimeUnit.DAYS.toMillis(7)*4;
                break;
            case 3:
                //ogni 365 giorni
                time = TimeUnit.DAYS.toMillis(365);
                break;
        }

        long recall = System.currentTimeMillis() + time;
        Calendar updateTime = Calendar.getInstance();
        updateTime.setTimeInMillis(recall);

        Intent notifyer = new Intent(getActivity(), NotificationReceiver.class);
        PendingIntent recurringNotifyer = PendingIntent.getBroadcast(getActivity(),
                0, notifyer, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarms = (AlarmManager) getActivity().getSystemService(
                Context.ALARM_SERVICE);
        alarms.cancel(recurringNotifyer);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                updateTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, recurringNotifyer);
    }
}
