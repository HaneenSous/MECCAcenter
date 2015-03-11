package com.mecca_center.app.utils;

import com.google.api.client.util.DateTime;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.the_dev.mecca_center.api.event.model.CollectionResponseEventEntity;
import com.the_dev.mecca_center.api.event.model.ConfirmedData;
import com.the_dev.mecca_center.api.event.model.EventEntity;
import com.the_dev.mecca_center.api.eventticket.model.EventTicket;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.IOException;

/**
 * Created by The_Dev on 2/12/2015.
 */
@EBean
public class ServiceUtils {


    public static int FAILED = 201;
    public static int SUCCESS = 200;

    public static int REGISTER = 1001;
    public static int UNREGISTER = 1002;

    public static int ADD_EVENT = 2001;
    public static int UPDATE_EVENT = 2002;
    public static int GET_EVENTS = 2003;
    public static int DELETE_EVENT = 2004;
    public static final int REQUEST_EVENT_TICKET = 2005;
    public  static final int GET_EVENT = 2006;

    public static int UPLOAD_URL = 3001;
    public static int UPLOADING_IMAGE = 3002;
    public static int SERVE_IMAGE = 3003;
    public static int DELETE_IMAGE = 3004;

    EndpointData data;


    EndpointCallback endpointCallback;


    @Background
    public void registerDevice(String registerId) {
        try {
            BackendUtil.getRegistrationInstance().register(registerId).execute();
            getResult(REGISTER, SUCCESS, null);
        } catch (IOException e) {
            e.printStackTrace();
            getResult(REGISTER, SUCCESS, null);
        }
    }


    @Background
    public void UnregisterDevice(String registerId) {
        try {
            BackendUtil.getRegistrationInstance().unregister(registerId).execute();
            getResult(UNREGISTER, SUCCESS, null);
        } catch (IOException e) {
            getResult(UNREGISTER, SUCCESS, null);
        }
    }

    @Background
    public void sendEvent(String Message) {
        try {
            BackendUtil.getMessageEndpointInstance().messagingEndpoint().sendMessage(Message).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Background
    public void requestEventTicket() {
        try {
            EndpointData data = new EndpointData();
            EventTicket ticket = BackendUtil.getEventticket().requestTicket().execute();
            data.setEventticket(ticket);
            getResult(REQUEST_EVENT_TICKET, SUCCESS, data);
        } catch (IOException e) {
            getResult(REQUEST_EVENT_TICKET, FAILED, null);
        }

    }

    @Background
    public void addEvent(EventEntity eventEntity) {
        try {
            BackendUtil.getEventEndpointInstance().addEvent(eventEntity).execute();
            getResult(ADD_EVENT, SUCCESS, null);
        } catch (IOException e) {
            e.printStackTrace();
            getResult(ADD_EVENT, FAILED, null);
        }

    }


    @Background
    public void deleteEvent(Long Id) {
        try {
            BackendUtil.getEventEndpointInstance().deleteEvents(Id).execute();
            getResult(DELETE_EVENT, SUCCESS, null);
        } catch (IOException e) {
            getResult(DELETE_EVENT, FAILED, null);
        }

    }

    @Background
    public void updateEvent(EventEntity eventEntity) {
        try {
            BackendUtil.getEventEndpointInstance().updateEvents(eventEntity).execute();
            getResult(UPDATE_EVENT, SUCCESS, null);
        } catch (IOException e) {
            getResult(UPDATE_EVENT, FAILED, null);
        }

    }


    @Background
    public void getEvents(int count, DateTime FromDate,DateTime ToDate, String CusorSt) {
        try {
            CollectionResponseEventEntity collectionResponseEventEntity;
            if(CusorSt==null){
                 collectionResponseEventEntity =
                        BackendUtil.getEventEndpointInstance().getEvents(count).setFromDate(FromDate).setToDate(ToDate).execute();

            }else{
                 collectionResponseEventEntity =
                        BackendUtil.getEventEndpointInstance().getEvents(count).setFromDate(FromDate).setToDate(ToDate).setCusrsorSt(CusorSt).execute();

            }
            data = new EndpointData();
            data.setEventEntityList(collectionResponseEventEntity.getItems());
            data.setCursorSt(collectionResponseEventEntity.getNextPageToken());
            getResult(GET_EVENTS, SUCCESS, data);
        } catch (IOException e) {
            e.printStackTrace();
            getResult(GET_EVENTS, FAILED, null);
        }

    }

    @Background
    public void getEvent(String eventName, DateTime date) {
        try {
            data = new EndpointData();
            if(date!=null) {
                data.setEventEntityList(BackendUtil.getEventEndpointInstance().getEvent(eventName).setDate(date).execute().getItems());
            }else{
                data.setEventEntityList(BackendUtil.getEventEndpointInstance().getEvent(eventName).execute().getItems());
            }

            getResult(GET_EVENT, SUCCESS, data);
        } catch (Exception e) {
            getResult(GET_EVENT, FAILED, null);
        }
    }


    @Background
    public void getUploadUrl() {
        try {
            ConfirmedData confirmedData = BackendUtil.getEventEndpointInstance().uploadUrlImage().execute();
            data = new EndpointData();
            data.setConfirmedData(confirmedData);
            getResult(UPLOAD_URL, SUCCESS, data);
        } catch (IOException e) {
            e.printStackTrace();
            getResult(UPLOAD_URL, FAILED, null);
        }

    }

    @Background(delay = 5000)
    public void getServeUrl(String fileName) {
        try {

            ConfirmedData confirmedData = BackendUtil.getEventEndpointInstance().getDownloadUrl(fileName).execute();

            data = new EndpointData();
            data.setConfirmedData(confirmedData);
            getResult(SERVE_IMAGE, SUCCESS, data);
        } catch (IOException e) {
            e.printStackTrace();
            getResult(SERVE_IMAGE, FAILED, null);
        }

    }

    @Background
    public void deleteImage(String filename) {
        try {
            BackendUtil.getEventEndpointInstance().deleteImage(filename).execute();
            getResult(DELETE_IMAGE, SUCCESS, null);
        } catch (IOException e) {
            getResult(DELETE_IMAGE, FAILED, null);
        }

    }


    @Background(serial = "Upload")
    public void UploadingImage(String URL, String file, String Name) {


        MediaType mediaType = MediaType.parse("image/png");
        final OkHttpClient client = new OkHttpClient();
        Response response = null;
        File file1 = new File(file);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"upload\"; filename=\"" + Name + "\""), RequestBody.create(mediaType, file1))
                .build();

        Request request1 = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        try {

            response = client.newCall(request1).execute();

            if (response.isSuccessful()) {

                getResult(UPLOADING_IMAGE, SUCCESS, null);
            } else {

                getResult(UPLOADING_IMAGE, SUCCESS, null);
            }
        } catch (IOException e) {
            getResult(UPLOADING_IMAGE, FAILED, null);
        }

    }


    @UiThread
    void getResult(int request, int success, EndpointData data) {
        endpointCallback.onResult(request, success, data);
    }

    public void setEndpointCallback(EndpointCallback callback) {
        this.endpointCallback = callback;

    }

    public interface EndpointCallback {
        void onResult(int request, int result, EndpointData data);
    }

}
