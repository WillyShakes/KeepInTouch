package com.willycode.keepintouch.Contacts.Receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.willycode.keepintouch.Contacts.View.MainActivity;
import com.willycode.keepintouch.R;

/**
 * Created by Manuel ELO'O on 19/11/2015.
 */
public class NotificationReceiver extends BroadcastReceiver {

        private static final String DEBUG_TAG = "AlarmReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(DEBUG_TAG, "Recurring alarm; notify.");
            Intent resultIntent = new Intent(context, MainActivity.class);
            String title = context.getString(R.string.app_name);
            String notification_msg = context.getString(R.string.notification_msg);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_notify)
                            .setContentTitle(title)
                            .setContentText(notification_msg);
            // Because clicking the notification opens a new ("special") activity, there's
            // no need to create an artificial back stack.
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            // Sets an ID for the notification
            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
}
