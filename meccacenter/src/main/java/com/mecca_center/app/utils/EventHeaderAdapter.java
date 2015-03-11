package com.mecca_center.app.utils;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mecca_center.app.meccacenter.R;
import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.the_dev.mecca_center.api.event.model.EventEntity;

import org.joda.time.LocalDate;

import java.util.List;

/**
 * Created by The_Dev on 2/23/2015.
 */
public class EventHeaderAdapter  implements StickyHeadersAdapter<EventHeaderAdapter.HeaderItem> {


    List<EventEntity> data;

    public EventHeaderAdapter(List<EventEntity> data) {
        this.data = data;
    }

    @Override
    public HeaderItem onCreateViewHolder(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_header,viewGroup,false);
        return new HeaderItem(v);
    }

    @Override
    public void onBindViewHolder(HeaderItem headerItem, int i) {
        if(data.size()==0)return;
        EventEntity event = data.get(i);
            if(LocalDate.now().isEqual(new LocalDate(event.getDate().getValue()))){
                headerItem.Header.setText("Today");
            return;
            }else if(LocalDate.now().plusDays(1).isEqual(new LocalDate(event.getDate().getValue()))){
                headerItem.Header.setText("Tomorrow");
                return;
            }

        headerItem.Header.setText(DateFormat.getDateFormat(headerItem.Header.getContext()).format(event.getDate().getValue()));

    }

    @Override
    public long getHeaderId(int i) {

        return new LocalDate(data.get(i).getDate().getValue()).toDate().getTime();
    }

    public static class HeaderItem extends RecyclerView.ViewHolder{
      TextView Header;
        public HeaderItem(View itemView) {
            super(itemView);
            Header= (TextView) itemView.findViewById(R.id.tvEventHeader);
        }
    }
}
