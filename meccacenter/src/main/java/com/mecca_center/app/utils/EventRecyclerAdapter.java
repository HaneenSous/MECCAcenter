package com.mecca_center.app.utils;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mecca_center.app.meccacenter.R;
import com.squareup.picasso.Picasso;
import com.the_dev.mecca_center.api.event.model.EventEntity;

import java.util.List;

/**
 * Created by The_Dev on 2/23/2015.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.EventItem> {

List<EventEntity> data;
    public EventRecyclerAdapter(List<EventEntity> data) {
    this.data=data;
        setHasStableIds(true);
    }

    @Override
    public EventItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_row,parent,false);
        return new EventItem(v);
    }

    @Override
    public void onBindViewHolder(EventItem holder, int position) {

            EventEntity event = data.get(position);
        holder.tvEventName.setText(event.getEventName());
        holder.tvEventDate.setText(DateFormat.getDateFormat(holder.EventImage.getContext()).format(event.getDate().getValue()));
      if(event.getImageUrl()!=null){
          Picasso.with(holder.EventImage.getContext()).load(event.getImageUrl()).fit().into(holder.EventImage);
      }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class EventItem extends RecyclerView.ViewHolder{
        TextView tvEventName,tvEventDate;
        ImageView EventImage;

        public EventItem(View itemView) {
            super(itemView);
           tvEventName= (TextView) itemView.findViewById(R.id.EventRow_tvEventName);
            tvEventDate= (TextView) itemView.findViewById(R.id.EventRow_tvEventDate);
            EventImage= (ImageView) itemView.findViewById(R.id.EventRow_imEventphoto);
        }
    }
}
