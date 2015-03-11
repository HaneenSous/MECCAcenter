package com.mecca_center.app.meccacenter;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.mecca_center.app.utils.NoteData;
import com.mecca_center.app.utils.NoteItem;
import com.skocken.efficientadapter.lib.adapter.SimpleAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_notice)
public class NoticeActivity extends ActionBarActivity {


@ViewById
SuperRecyclerView listNotes;

    List<NoteData> list = new ArrayList<>();


    @AfterViews
    public void setMessage(){
        String URL = "content://com.mecca_center.app.meccacenter/Notes";
        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, "Note");
        if (c.moveToFirst()) {
            do{
                list.add(new NoteData(c.getString(c.getColumnIndex( NoteProvider.Note)),c.getString(c.getColumnIndex( NoteProvider.SentDate))));

            } while (c.moveToNext());
        }
        SimpleAdapter<NoteData> simpleAdapter = new SimpleAdapter<NoteData>(R.layout.note_item_list, NoteItem.class,list);
        listNotes.setLayoutManager(new LinearLayoutManager(this));
        listNotes.setAdapter(simpleAdapter);

    }
}
