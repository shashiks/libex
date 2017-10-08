package com.shaksoni.libex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shaksoni on 9/27/17.
 */

public class DBHelper extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "my_library";

    // Contacts table name
    protected static final String TABLE_BOOK = "book";


    private static final String CREATE_BOOK_TABLE = "CREATE TABLE book( "+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                "title text not null, "+
                "authors text not null, "+
                "publisher text not null, "+
                "publish_year char(4), "+
                "isbn10 char(10) null, "+
                "isbn13 char(13) null, "+
                "genres varchar(255) null, "+
                "thumbnail_url varchar(500) null, "+
                "num_pages int, "+
                "avg_rating float, "+
                "my_rating float, "+
                "CONSTRAINT uniq_isbn10 UNIQUE (isbn10), "+
                "CONSTRAINT uniq_isbn13 UNIQUE (isbn13) "+
            "); ";


    public DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        Log.d("Book Table", "created book table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
