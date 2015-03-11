package com.mecca_center.app.utils;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mecca_center.app.meccacenter.R;
import com.skocken.efficientadapter.lib.viewholder.AbsViewHolder;
import com.squareup.picasso.Picasso;
import com.the_dev.mecca_center.api.event.model.EventEntity;

/**
 * Created by The_Dev on 2/27/2015.
 */
public class EventHolder extends AbsViewHolder<EventEntity> {

    public EventHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void updateView(Context context, EventEntity eventEntity) {

       ( (TextView) findViewByIdEfficient(R.id.EventRow_tvEventName)).setText(eventEntity.getEventName());;
        ((TextView) findViewByIdEfficient(R.id.EventRow_tvEventDate)).setText(DateFormat.getDateFormat(context).format(eventEntity.getDate().getValue()));

        if(eventEntity.getImageUrl()!=null){
            Picasso.with(context).load(eventEntity.getImageUrl()).fit().into(((ImageView) findViewByIdEfficient(R.id.EventRow_imEventphoto)));
        }
    }
}
