package com.shaksoni.libex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaksoni on 9/27/17.
 */

public class BookDAO extends DBHelper{


    final static String
            ID = "_id",
            TITLE = "title",
            AUTHORS = "authors",
    PUBLISHER = "publisher",
    PUBLISH_YEAR= "publish_year",
    IMAGE_URL = "thumbnail_url",
    ISBN_10 = "isbn10",
    ISBN_13  ="isbn13",
    GENRES = "genres",
    PAGES = "num_pages",
    AVG_RATING = "avg_rating",
    MY_RATING = "my_rating";

    final static String QRY_BY_TITLE_AUTHOR = "select title, authors, avg_rating from ".concat("");




    public BookDAO(Context context) {
        super(context);
    }

    public String save(Book book) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TITLE, book.getTitle());
            contentValues.put(PUBLISHER, book.getPublisher());
            contentValues.put(AUTHORS, book.getAllAuthors());
            contentValues.put(PUBLISH_YEAR, book.getYearOfPublish());
            contentValues.put(IMAGE_URL, book.getThumbnailUrl());
            contentValues.put(ISBN_10, book.getIsbn10());
            contentValues.put(ISBN_13, book.getIsbn13());
            contentValues.put(GENRES, book.getGenres().toString());
            contentValues.put(PAGES, book.getNumPages());
            contentValues.put(AVG_RATING, book.getAverageRating());
            contentValues.put(MY_RATING, book.getMyRating());
            db.insertOrThrow(TABLE_BOOK, null, contentValues);
            return "Book Saved";
        } catch (Exception e) {
            Log.e(getClass().getName(), "Save Book Failed" , e);
            if(e instanceof SQLiteConstraintException) {
                return "Book already exists in Libex";
            }
            return "Can't save book because of :: " +e.getMessage();

        }
    }


    /**
     * Returns all the books.
     * <p>
     * Use limit row to read only specific number of rows </br>
     * Use -1 to get all rows
     * </p>
     * @param limitRow
     * @return
     */
    public List<Book> readAll(int limitRow) {

        List<Book> books = new ArrayList<Book>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crsr = null;
        String qry = "SELECT * FROM " + TABLE_BOOK + " order by _id desc";
        if(limitRow > -1) {
           crsr = db.rawQuery( qry.concat(" limit " + limitRow), null );
        } else {
            crsr = db.rawQuery(qry , null );
        }


        if(crsr != null && crsr.moveToFirst()) {

            while (!crsr.isAfterLast()) {
                Book book = new Book();
                book.setTitle(crsr.getString(crsr.getColumnIndex(TITLE)));
                //do the split and magic here
                book.addAuthor(crsr.getString(crsr.getColumnIndex(AUTHORS)));
                book.setPublisher(crsr.getString(crsr.getColumnIndex(PUBLISHER)));
                book.setYearOfPublish(crsr.getInt(crsr.getColumnIndex(PUBLISH_YEAR)));
                book.setIsbn10(crsr.getString(crsr.getColumnIndex(ISBN_10)));
                book.setIsbn13(crsr.getString(crsr.getColumnIndex(ISBN_13)));
                //magic fields do as requird
                book.addGenre(crsr.getString(crsr.getColumnIndex(GENRES)));
                book.setNumPages(crsr.getInt(crsr.getColumnIndex(PAGES)));
                book.setAverageRating(crsr.getFloat(crsr.getColumnIndex(AVG_RATING)));
                book.setMyRating(crsr.getFloat(crsr.getColumnIndex(MY_RATING)));
                book.setThumbnailUrl(crsr.getString(crsr.getColumnIndex(IMAGE_URL)));
                books.add(book);
                crsr.moveToNext();
            }
        }


            return books;
    }

    public Book read(Integer bookId) {

        Book book = new Book();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_BOOK + " WHERE " +
                ID + "=?", new String[] { new Integer(bookId).toString() } );
        book.setTitle(res.getString(res.getColumnIndex(TITLE)));
        //do the split and magic here
        book.addAuthor(res.getString(res.getColumnIndex(AUTHORS)));
        book.setPublisher(res.getString(res.getColumnIndex(PUBLISHER)));
        book.setYearOfPublish(res.getInt(res.getColumnIndex(PUBLISH_YEAR)));
        book.setIsbn10(res.getString(res.getColumnIndex(ISBN_10)));
        book.setIsbn13(res.getString(res.getColumnIndex(ISBN_13)));
        //magic fields do as requird
        book.addGenre(res.getString(res.getColumnIndex(GENRES)));
        book.setNumPages(res.getInt(res.getColumnIndex(PAGES)));
        book.setAverageRating(res.getFloat(res.getColumnIndex(AVG_RATING)));
        book.setMyRating(res.getFloat(res.getColumnIndex(MY_RATING)));
        book.setThumbnailUrl(res.getString(res.getColumnIndex(IMAGE_URL)));

        return book;
    }

}
