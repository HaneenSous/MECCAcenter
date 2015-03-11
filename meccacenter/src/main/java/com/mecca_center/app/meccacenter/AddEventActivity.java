package com.mecca_center.app.meccacenter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.IconButton;
import android.widget.TextView;

import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import com.mecca_center.app.utils.EndpointData;
import com.mecca_center.app.utils.ImageFilePath;
import com.mecca_center.app.utils.ServiceUtils;
import com.mecca_center.app.utils.UploadImage;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.google.api.client.util.DateTime;
import com.the_dev.mecca_center.api.event.model.EventEntity;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by The_Dev on 2/15/2015.
 */

@EActivity(R.layout.add_event)
public class AddEventActivity extends ActionBarActivity implements DatePickerDialogFragment.DatePickerDialogHandler, TimePickerDialogFragment.TimePickerDialogHandler, ServiceUtils.EndpointCallback, UploadImage.OnCancelListener {

    @ViewById(R.id.UIAdd_Event)
    UploadImage uploadImage;
    @ViewById(R.id.EtNameAdd_event)
    EditText EtEventName;
    @ViewById(R.id.EtDescriptionAdd_Event)
    EditText EtEventDescription;
    @ViewById(R.id.EtDate_Add_Event)
    IconButton IBEventDate;
    @ViewById(R.id.EtTime_Add_Event)
    IconButton IBEventTime;
    @ViewById(R.id.tvEventNameError)
    TextView tvNameError;
    @ViewById
    TextView tvDateTimeError;
    @ViewById
    TextView tvDescriptionError;
    @ViewById
    Toolbar toolbar;

    @Bean
    ServiceUtils serviceUtils;
    @InstanceState
    String filePath=null;

    @InstanceState
    String mEventTicket;
    @InstanceState
    String mBlobKeyST;
    @InstanceState
    String mBlobUrl;

    EventEntity event;

    int UploadingTimes = 0;

    Calendar SelectedCalendar;
    @StringRes(R.string.add_event)
    String Title;

    SuperCardToast superCardToast;

    @AfterViews
    void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        serviceUtils.setEndpointCallback(this);
        EtEventName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && EtEventName.getText().length() > 0 && SelectedCalendar != null) {
                    serviceUtils.getEvent(EtEventName.getText().toString(), new DateTime(SelectedCalendar.getTime()));
                }
            }
        });

        uploadImage.setOnCancelListener(this);


    }

    @Click({R.id.EtDate_Add_Event, R.id.EtTime_Add_Event, R.id.BTNSave_Add_event})
    void onClick(View v) {
        if (v.getId() == R.id.EtDate_Add_Event) {
            Calendar calendar = new GregorianCalendar();
            if (SelectedCalendar != null) {
                calendar = SelectedCalendar;
            }
            tvDateTimeError.setText("");
            new DatePickerBuilder().addDatePickerDialogHandler(this)
                    .setFragmentManager(getSupportFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setYear(calendar.get(Calendar.YEAR))
                    .setMonthOfYear(calendar.get(Calendar.MONTH))
                    .setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH))
                    .show();
        }
        if (v.getId() == R.id.EtTime_Add_Event) {

            tvDateTimeError.setText("");
            new TimePickerBuilder().addTimePickerDialogHandler(this)
                    .setFragmentManager(getSupportFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .show();

        }


        if (v.getId() == R.id.BTNSave_Add_event) {
            if(EtEventName.getText().length()==0){
                tvNameError.setText("Invalid name");
                return;
            }
            if(EtEventDescription.getText().length()==0){
                tvDescriptionError.setText("Invalid Description");
                return;
            }
            if(SelectedCalendar==null){
                tvDateTimeError.setText("Invalid Date");
                return;
            }

            if (SelectedCalendar.getTimeInMillis() - System.currentTimeMillis() < 0) {
                tvDateTimeError.setText("This date in the past");
                return;
            }

            event = new EventEntity();
            event.setEventName(EtEventName.getText().toString());

            event.setDescription(EtEventDescription.getText().toString());
            event.setDate(new DateTime(SelectedCalendar.getTime()));

            serviceUtils.requestEventTicket();
            uploadingToast();

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            filePath = ImageFilePath.getPath(this, data.getData());
            uploadImage.setImageSource(new File(filePath));


        }


    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        if (SelectedCalendar == null) {
            SelectedCalendar = new GregorianCalendar();
        }

        SelectedCalendar.set(Calendar.YEAR, year);
        SelectedCalendar.set(Calendar.MONTH, monthOfYear);
        SelectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        if (SelectedCalendar.getTimeInMillis() - System.currentTimeMillis() < 0) {
            tvDateTimeError.setText("Invalid Date ");
            return;
        }

    }

    @Override
    public void onDialogTimeSet(int reference, int hour, int minute) {
        if (SelectedCalendar == null) {
            SelectedCalendar = new GregorianCalendar();
        }
        SelectedCalendar.set(Calendar.HOUR_OF_DAY, hour);
        SelectedCalendar.set(Calendar.MINUTE, minute);
        if (EtEventName.getText().length() > 0 && SelectedCalendar != null) {
            serviceUtils.getEvent(EtEventName.getText().toString(), new DateTime(SelectedCalendar.getTime()));
        }
        if (SelectedCalendar.getTimeInMillis() - System.currentTimeMillis() < 0) {
            tvDateTimeError.setText("Invalid Date ");
            return;
        }

    }

    @Override
    public void onResult(int request, int result, EndpointData data) {
        if (result == ServiceUtils.SUCCESS) {
            if (request == ServiceUtils.GET_EVENT) {
                if (data.getEventEntityList() != null) {
                    EventEntity eventEntity = data.getEventEntityList().get(0);
                    tvNameError.setText("This Event is Already published "
                            + " Event Name :" + eventEntity.getEventName() + " Date " + new SimpleDateFormat("dd-MM-yyyy").format(eventEntity.getDate())
                            + " / Time :" + new SimpleDateFormat("HH:mm").format(eventEntity.getDate()));
                }
            }

            if (request == ServiceUtils.REQUEST_EVENT_TICKET) {
                mEventTicket = data.getEventticket().getTicketName();
                event.setEventTicket(mEventTicket);
              if(filePath==null){
                  serviceUtils.addEvent(event);
              }else{
                serviceUtils.getUploadUrl();
            }
            }

            if (request == ServiceUtils.UPLOAD_URL) {
                serviceUtils.UploadingImage(data.getConfirmedData().getData(), filePath, "IM" + mEventTicket);
            }

            if (request == ServiceUtils.UPLOADING_IMAGE) {
                serviceUtils.getServeUrl("IM" + mEventTicket);

            }

            if (request == ServiceUtils.SERVE_IMAGE) {
                if (data.getConfirmedData() != null) {
                    event.setImageKey(data.getConfirmedData().getData().toString());
                    event.setImageKey(data.getConfirmedData().getBlobKeySt());
                }
                serviceUtils.addEvent(event);
            }
            if (request == ServiceUtils.ADD_EVENT) {
                superCardToast.dismiss();
            }
        }

        if (result == ServiceUtils.FAILED) {

        }
    }


    @Override
    public void onCancel() {
        filePath = null;
    }

    public void uploadingToast() {
        superCardToast = new SuperCardToast(this, SuperToast.Type.PROGRESS);
        superCardToast.setAnimations(SuperToast.Animations.FADE);
        superCardToast.setText("Adding.......");
        superCardToast.setIndeterminate(true);
        superCardToast.show();
    }


    @FocusChange({R.id.EtNameAdd_event,R.id.EtDescriptionAdd_Event})
    void focusChange(View v, boolean hasFocus){
            if(v.getId()==R.id.EtNameAdd_event){
                if (!hasFocus && EtEventName.getText().length() > 0 && SelectedCalendar != null) {
                    serviceUtils.getEvent(EtEventName.getText().toString(), new DateTime(SelectedCalendar.getTime()));

                }
                if(hasFocus){
                    tvNameError.setText("");
                }

            }
            if(v.getId()==R.id.EtDescriptionAdd_Event){
                if(hasFocus){
                    tvDescriptionError.setText("");
                }
            }


    }

    @TextChange({R.id.EtNameAdd_event,R.id.EtDescriptionAdd_Event})
    void textChange(TextView  v){
        if(v.getId()==R.id.EtNameAdd_event){
                tvNameError.setText("");

        }
        if(v.getId()==R.id.EtDescriptionAdd_Event){
                tvDescriptionError.setText("");
        }


    }

}
