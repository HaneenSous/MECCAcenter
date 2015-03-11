package com.mecca_center.app.utils;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.the_dev.mecca_center.api.event.Event;

import com.the_dev.mecca_center.api.eventticket.Eventticket;
import com.the_dev.mecca_center.api.messaging.Messaging;
import com.the_dev.mecca_center.api.registration.Registration;

/**
 * Created by The_Dev on 2/12/2015.
 */
public class BackendUtil {

    private static Registration registration;

    private static Messaging  messagingEndpoint;
    private static Event  event;
  private static Eventticket eventticket;


    public static Registration getRegistrationInstance() {
     if(registration==null){
       Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null);
        registration=builder.build();
     }

        return registration;
    }

    public static Messaging getMessageEndpointInstance() {
        if(messagingEndpoint==null){
            Messaging.Builder builder = new Messaging.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null);
            messagingEndpoint=builder.build();
        }

        return messagingEndpoint;
    }

    public static Event getEventEndpointInstance() {
        if(event==null){
            Event.Builder builder = new Event.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null);
            event=builder.build();
        }

        return event;
    }

    public static Eventticket getEventticket() {
        if(eventticket==null){
            Eventticket.Builder builder = new Eventticket.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(),null);
            eventticket=builder.build();
        }
        return eventticket;
    }
}
