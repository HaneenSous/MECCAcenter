package com.mecca_center.app.utils;

/**
 * Created by The_Dev on 3/11/2015.
 */
public class NoteData {

    private String Note;
    private String SentDate;

    public NoteData(String note, String sentDate) {
        Note = note;
        SentDate = sentDate;
    }

    public String getNote() {
        return Note;
    }

    public String getSentDate() {
        return SentDate;
    }
}
