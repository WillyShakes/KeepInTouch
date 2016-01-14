package com.willycode.keepintouch.Contacts.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.willycode.keepintouch.Contacts.Utils.CallEngineService;

/**
 * Created by Manuel ELO'O on 12/01/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.willycode.keepintouch.service.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CallEngineService.class);
        context.startService(i);
    }
}