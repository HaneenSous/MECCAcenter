package com.mecca_center.app.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mecca_center.app.meccacenter.R;
import com.skocken.efficientadapter.lib.viewholder.AbsViewHolder;

/**
 * Created by The_Dev on 3/11/2015.
 */
public class NoteItem extends AbsViewHolder<NoteData> {


    public NoteItem(View itemView) {
        super(itemView);
    }

    @Override
    protected void updateView(Context context, NoteData noteData) {
        ((TextView)findViewByIdEfficient(R.id.tvNote)).setText(noteData.getNote());
        ((TextView)findViewByIdEfficient(R.id.tvSentDate)).setText(noteData.getSentDate());
    }
}
