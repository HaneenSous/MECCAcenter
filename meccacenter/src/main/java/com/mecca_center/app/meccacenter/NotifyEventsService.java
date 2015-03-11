package com.mecca_center.app.meccacenter;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.ContentValues;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;


import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.EIntentService;
import org.joda.time.LocalDateTime;

import java.text.DateFormat;

/**
 * Created by The_Dev on 2/13/2015.
 */
@EIntentService
public class NotifyEventsService extends IntentService {

    int NOTIFICATION_ID = 1;

    NotificationManager notificationManager;

    public NotifyEventsService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        GoogleCloudMessaging GCM = GoogleCloudMessaging.getInstance(this);
        String GCMType = GCM.getMessageType(intent);

        if (!bundle.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(GCMType)) {

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(GCMType)) {

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(GCMType)) {
                if(bundle.getString("message")!=null){
                    sendNotification("message", bundle.getString("message"));
                    ContentValues values = new ContentValues();

                    values.put(NoteProvider.Note,
                            bundle.getString("message") );

                    values.put(NoteProvider.SentDate,
                            DateFormat.getDateTimeInstance().format(LocalDateTime.now().toDate()));

                    Uri uri = getContentResolver().insert(
                            NoteProvider.CONTENT_URI, values);
                }else{
                    sendNotification("Event", bundle.getString("Event"));
                }


            }

        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);

    }


    void sendNotification(String key, String message) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent;
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        if (key.equals("message")) {
            taskStackBuilder.addParentStack(NoticeActivity_.class);
            taskStackBuilder.addNextIntent(NoticeActivity_.intent(this).get());
            pendingIntent = taskStackBuilder.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            taskStackBuilder.addParentStack(ShowEventActivity_.class);
            taskStackBuilder.addNextIntent(ShowEventActivity_.intent(this).eventName(message).get());
            pendingIntent = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle("MECCA Center")
                .setOngoing(false)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentText(message)
                .setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }


}
