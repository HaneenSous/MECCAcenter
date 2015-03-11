package com.mecca_center.app.meccacenter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by The_Dev on 3/11/2015.
 */
public class NoteProvider extends ContentProvider {

    private SQLiteDatabase db;
    static final String PROVIDER_NAME = "com.mecca_center.app.meccacenter";
    static final String URL = "content://" + PROVIDER_NAME + "/Notes";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String Note = "Note";
    static final String SentDate = "SentDate";

    private static HashMap<String, String> NOTES_PROJECTION_MAP;

    static final int DATABASE_VERSION = 1;
    static final private String NoteDB = "NoteDB";
    static final private String Note_TABLE_NAME = "Notes";
    static final String CreateTable = " CREATE TABLE " + Note_TABLE_NAME +
            "       (_id INTEGER PRIMARY KEY AUTOINCREMENT,  " +
            "   Note TEXT NOT NULL," +
            " SentDate TEXT NOT NULL);";

    static final int NOTES = 1;
    static final int NOTES_ID = 2;
    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "Notes", NOTES);
        uriMatcher.addURI(PROVIDER_NAME, "Notes/#", NOTES_ID);

    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        OfferDBHelper dbHelper = new OfferDBHelper(context);

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Note_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case NOTES:
                qb.setProjectionMap(NOTES_PROJECTION_MAP);
                break;
            case NOTES_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){

            sortOrder = Note;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,
                null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){

            case NOTES:
                return "vnd.android.cursor.dir/com.mecca_center.app.meccacenter";

            case NOTES_ID:
                return "vnd.android.cursor.item/com.mecca_center.app.meccacenter";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(Note_TABLE_NAME, "", values);

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case NOTES:
                count = db.delete(Note_TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(Note_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case NOTES:
                count = db.update(Note_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case NOTES_ID:
                count = db.update(Note_TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


  private static  class OfferDBHelper extends SQLiteOpenHelper {




        OfferDBHelper(Context context) {
            super(context, NoteDB, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(CreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Note_TABLE_NAME);
            onCreate(db);
        }
    }

}
