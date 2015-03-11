package com.mecca_center.app.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.TimeZone;

/**
 * Created by The_Dev on 2/24/2015.
 */
public class test {
    public static void main(String[] args) {

        LocalDateTime now = new LocalDateTime();
        DateTime time = new DateTime();
        time = time.withZone(DateTimeZone.forID(TimeZone.getDefault().getID()));
        System.out.println(time);
        System.out.println(now);
        System.out.println(now.withDayOfWeek(DateTimeConstants.SUNDAY));
        System.out.println(now.withDayOfWeek(DateTimeConstants.FRIDAY));
    }
}
