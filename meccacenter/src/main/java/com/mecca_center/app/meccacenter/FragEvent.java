package com.mecca_center.app.meccacenter;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;


import com.doomonafireball.betterpickers.datepicker.DatePickerBuilder;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.github.johnpersano.supertoasts.SuperToast;
import com.mecca_center.app.utils.EndpointData;
import com.mecca_center.app.utils.EventHeaderAdapter;
import com.mecca_center.app.utils.EventHolder;

import com.mecca_center.app.utils.ServiceUtils;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mecca_center.app.utils.SlideFilter;
import com.skocken.efficientadapter.lib.adapter.AbsViewHolderAdapter;
import com.skocken.efficientadapter.lib.adapter.SimpleAdapter;
import com.the_dev.mecca_center.api.event.model.EventEntity;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.frag_event)
public class FragEvent extends Fragment implements ServiceUtils.EndpointCallback, DatePickerDialogFragment.DatePickerDialogHandler {


    @ViewById
    SuperRecyclerView list;


    SimpleAdapter<EventEntity> adapter;

    EventHeaderAdapter headerAdapter;
    @ViewById
    SlideFilter Container;


    private StickyHeadersItemDecoration top;

    @Bean
    ServiceUtils serviceUtils;

    LocalDate localDate = new LocalDate();


    LocalDate FromDate, ToDate;


    @ViewById
    Button btnFromDate;

    @ViewById
    Button btnToDate;

    @ViewById
    Button btnFilter;


    @AfterViews
    void init() {


        serviceUtils.setEndpointCallback(this);


        Container.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in));
        Container.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out));

        btnFromDate.setText(DateFormat.getDateFormat(getActivity()).format(LocalDate.now().toDate()));
        btnToDate.setText(DateFormat.getDateFormat(getActivity()).format(LocalDate.now().plusMonths(1).toDate()));
        FromDate = LocalDate.now();
        ToDate = LocalDate.now().plusMonths(1);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (adapter == null) {
            serviceUtils.getEvents(100, getDateTime(LocalDateTime.now().toDate()), getLastDayMonth(LocalDateTime.now()), null);
            btnFilter.setText("This Month");
            btnFilter.setEnabled(false);
        } else {
            list.setAdapter(adapter);
            list.getRecyclerView().removeItemDecoration(top);
            list.addItemDecoration(top);
        }


    }


    @Override
    public void onResult(int request, int result, EndpointData data) {

        if (result == ServiceUtils.SUCCESS) {
            if (request == ServiceUtils.GET_EVENTS) {
                if (adapter == null) {
                    btnFilter.setEnabled(true);
                    adapter = new SimpleAdapter<EventEntity>(R.layout.event_row, EventHolder.class, data.getEventEntityList());
                    adapter.setHasStableIds(true);
                    adapter.setOnItemClickListener(new AbsViewHolderAdapter.OnItemClickListener<EventEntity>() {
                        @Override
                        public void onItemClick(AbsViewHolderAdapter<EventEntity> eventEntityAbsViewHolderAdapter, View view, EventEntity eventEntity, int i) {
                            ShowEventActivity_.intent(view.getContext())
                                    .Id(eventEntity.getId())
                                    .eventName(eventEntity.getEventName())
                                    .eventDescription(eventEntity.getDescription())
                                    .dateTime(eventEntity.getDate())
                                    .Ticket(eventEntity.getEventTicket())
                                    .BlobKeySt(eventEntity.getImageKey())
                                    .ImageURL(eventEntity.getImageUrl())
                                    .start();

                        }
                    });

                    headerAdapter = new EventHeaderAdapter(data.getEventEntityList());
                    top = new StickyHeadersBuilder()
                            .setAdapter(adapter)
                            .setRecyclerView(list.getRecyclerView())
                            .setStickyHeadersAdapter(headerAdapter)
                            .build();


                    list.setAdapter(adapter);
                    list.addItemDecoration(top);
                    return;
                }


                if (data.getEventEntityList() != null) {
                    adapter.addAll(data.getEventEntityList());
                    adapter.notifyItemRangeInserted(0, adapter.size());
                    list.addItemDecoration(top);
                }
                adapter.notifyDataSetChanged();
                list.showRecycler();
            }
        }

    }

    public com.google.api.client.util.DateTime getDateTime(Date date) {
        return new com.google.api.client.util.DateTime(date.getTime());

    }

    public com.google.api.client.util.DateTime getLastDayWeek(LocalDateTime localDate) {
        return new com.google.api.client.util.DateTime(localDate.dayOfWeek().withMaximumValue().hourOfDay().withMaximumValue().minuteOfHour().withMaximumValue().toDate().getTime());
    }

    public com.google.api.client.util.DateTime getLastDayMonth(LocalDateTime localDate) {
        return new com.google.api.client.util.DateTime(localDate.dayOfMonth().withMaximumValue().hourOfDay().withMaximumValue().minuteOfHour().withMaximumValue().toDate().getTime());
    }


    public com.google.api.client.util.DateTime getDayStart(LocalDateTime date) {

        return new com.google.api.client.util.DateTime(date.hourOfDay().withMinimumValue().minuteOfHour().withMinimumValue().toDate());

    }

    public com.google.api.client.util.DateTime getDayEnd(LocalDateTime date) {

        return new com.google.api.client.util.DateTime(date.hourOfDay().withMaximumValue().minuteOfHour().withMaximumValue().toDate());

    }


    @Click
    public void btnFilter() {

        if (Container.getVisibility() != View.VISIBLE) {
            Container.setVisibility(View.VISIBLE);
        } else {
            Container.setVisibility(View.GONE);
        }

    }

    @Click({R.id.btnToday, R.id.btnTomorrow, R.id.btnWeek, R.id.btnMonth, R.id.btnFromDate, R.id.btnToDate, R.id.btnSearch})
    public void Filters(View v) {
        if (v.getId() == R.id.btnToday) {
            btnFilter.setText("Today");
            serviceUtils.getEvents(100, getDayStart(LocalDateTime.now()), getDayEnd(LocalDateTime.now()), null);
            adapter.clear();
            adapter.notifyItemRangeRemoved(0, adapter.size());
            list.getRecyclerView().removeItemDecoration(top);
            list.showProgress();
            Container.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnTomorrow) {
            btnFilter.setText("Tomorrow");
            serviceUtils.getEvents(20, getDayStart(LocalDateTime.now().plusDays(1)), getDayEnd(LocalDateTime.now().plusDays(1)), null);
            adapter.clear();
            adapter.notifyItemRangeRemoved(0, adapter.size());
            list.getRecyclerView().removeItemDecoration(top);
            list.showProgress();

            Container.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnWeek) {
            btnFilter.setText("This Week");
            serviceUtils.getEvents(20, getDateTime(LocalDateTime.now().toDate()), getLastDayWeek(LocalDateTime.now()), null);
            adapter.clear();
            adapter.notifyItemRangeRemoved(0, adapter.size());
            list.getRecyclerView().removeItemDecoration(top);
            list.showProgress();
            Container.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnMonth) {
            btnFilter.setText("This Month");
            serviceUtils.getEvents(20, getDateTime(LocalDateTime.now().toDate()), getLastDayMonth(LocalDateTime.now()), null);
            adapter.clear();
            adapter.notifyItemRangeRemoved(0, adapter.size());
            list.getRecyclerView().removeItemDecoration(top);
            list.showProgress();
            Container.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnFromDate) {
            new DatePickerBuilder().addDatePickerDialogHandler(this)
                    .setFragmentManager(getChildFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setYear(localDate.getYear())
                    .setMonthOfYear(localDate.getMonthOfYear() - 1)
                    .setDayOfMonth(localDate.getDayOfMonth())
                    .setReference(0)
                    .show();
        } else if (v.getId() == R.id.btnToDate) {
            LocalDate mLocalDate = localDate.plusMonths(1);
            new DatePickerBuilder().addDatePickerDialogHandler(this)
                    .setFragmentManager(getChildFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setYear(mLocalDate.getYear())
                    .setMonthOfYear(mLocalDate.getMonthOfYear() - 1)
                    .setDayOfMonth(mLocalDate.getDayOfMonth())
                    .setReference(1)
                    .show();

        } else if (v.getId() == R.id.btnSearch) {
            if (ToDate.isBefore(FromDate)) {
                SuperToast.create(getActivity(), "Invalid Date", SuperToast.Duration.MEDIUM, SuperToast.Animations.POPUP).show();
            } else {
                btnFilter.setText(" Selected Dates");
                serviceUtils.getEvents(100, getDateTime(FromDate.toDate()), getDateTime(ToDate.toDate()), null);
                adapter.clear();
                adapter.notifyItemRangeRemoved(0, adapter.size());
                list.getRecyclerView().removeItemDecoration(top);
                list.showProgress();
                Container.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDialogDateSet(int reference, int year, int month, int day) {
        if (reference == 0) {
            FromDate = new LocalDate(year, month + 1, day);
            btnFromDate.setText(DateFormat.getDateFormat(getActivity()).format(FromDate.toDate()));
        } else {

            ToDate = new LocalDate(year, month + 1, day);
            btnToDate.setText(DateFormat.getDateFormat(getActivity()).format(ToDate.toDate()));
        }
    }


}
