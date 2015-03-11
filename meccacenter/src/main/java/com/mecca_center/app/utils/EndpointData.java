package com.mecca_center.app.utils;

import com.the_dev.mecca_center.api.event.model.ConfirmedData;
import com.the_dev.mecca_center.api.event.model.EventEntity;
import com.the_dev.mecca_center.api.eventticket.model.EventTicket;

import java.util.List;

/**
 * Created by The_Dev on 2/15/2015.
 */
public class EndpointData {


    private ConfirmedData confirmedData;

    private List<EventEntity> eventEntityList;

    private EventTicket eventticket;

    private String CursorSt;


    public void setEventticket(EventTicket eventticket) {
        this.eventticket = eventticket;
    }

    public EventTicket getEventticket() {
        return eventticket;
    }

    public void setCursorSt(String cursorSt) {
        CursorSt = cursorSt;
    }

    public String getCursorSt() {
        return CursorSt;
    }

    public void setEventEntityList(List<EventEntity> eventEntityList) {
        this.eventEntityList = eventEntityList;
    }

    public List<EventEntity> getEventEntityList() {
        return eventEntityList;
    }

    public void setConfirmedData(ConfirmedData confirmedData) {
        this.confirmedData = confirmedData;
    }

    public ConfirmedData getConfirmedData() {
        return confirmedData;
    }
}
