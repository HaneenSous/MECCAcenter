package com.mecca_center.app.meccacenter;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.mecca_center.app.utils.EndpointData;
import com.mecca_center.app.utils.ServiceUtils;
import com.squareup.picasso.Picasso;
import com.the_dev.mecca_center.api.event.model.EventEntity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@EActivity(R.layout.activity_show_event)
public class ShowEventActivity extends ActionBarActivity implements ServiceUtils.EndpointCallback {

    @ViewById
    Toolbar toolbar;

    @ViewById()
    ImageView EventPhoto;

    @ViewById
    TextView EventName;

    @ViewById
    TextView EventDetails;

    @ViewById
    TextView EventDate;



    @Extra
    String eventName;
    @Extra
    Long Id;
    @Extra
    String eventDescription;
    @Extra
    String BlobKeySt;
    @Extra
    DateTime dateTime;
    @Extra
    String ImageURL;
    @Extra
    String Ticket;

    @Bean
    ServiceUtils serviceUtils;
@AfterViews
    public void init(){
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    if(Id!=null) {
        if (ImageURL != null) {
            Picasso.with(this).load(ImageURL).fit().into(EventPhoto);
        } else {
            Picasso.with(this).load(R.drawable.a).fit().into(EventPhoto);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("E MMM dd yyyy , hh:mm aa");
        EventName.setText(eventName);
        EventDetails.setText(eventDescription);
        EventDate.setText(dateTimeFormatter.print(dateTime.getValue()));
    }else{
        serviceUtils.setEndpointCallback(this);
        serviceUtils.getEvent(eventName,null);
    }


}


    @Override
    public void onResult(int request, int result, EndpointData data) {

        if(result==ServiceUtils.SUCCESS){
            if(request==ServiceUtils.GET_EVENT){
                if(data!=null){
                    if(data.getEventEntityList()!=null){
                        if(data.getEventEntityList().size()>0){
                            EventEntity eventEntity=data.getEventEntityList().get(0);
                            if (eventEntity.getImageUrl() != null) {
                                Picasso.with(this).load(ImageURL).fit().into(EventPhoto);
                            } else {
                                Picasso.with(this).load(R.drawable.a).fit().into(EventPhoto);
                            }
                            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("E MMM dd yyyy , hh:mm aa");
                            EventName.setText(eventName);
                            EventDetails.setText(eventEntity.getDescription());
                            EventDate.setText(dateTimeFormatter.print(eventEntity.getDate().getValue()));
                        }
                    }
                }
            }
        }

    }
}
