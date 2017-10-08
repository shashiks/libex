package com.shaksoni.libex;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaksoni on 9/22/17.
 */

public class Book implements Serializable{
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", isbn10='" + isbn10 + '\'' +
                ", isbn13='" + isbn13 + '\'' +
                ", publisher='" + publisher + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", genres=" + genres +
                ", authorsAsString=" + authorsAsString +
                ", yearOfPublish=" + yearOfPublish +
                ", numPages=" + numPages +
                ", averageRating=" + averageRating +
                ", myRating=" + myRating +
                '}';
    }

    private String title,  isbn10, isbn13, publisher, thumbnailUrl;
    private List<String> genres = new ArrayList<String>();
    private List<String> authors = new ArrayList<String>();
    private StringBuilder authorsAsString = new StringBuilder();

    private int yearOfPublish, numPages;
    private float averageRating;
    private float myRating;



    public boolean addAuthor(String anAuthor) {
        if(!authors.contains(anAuthor)) {
            authors.add(anAuthor);
            authorsAsString.append(anAuthor);
            return true;
        }
        return  false;
    }


    public boolean addGenre(String aGenre) {
        if(!genres.contains(aGenre)) {
            genres.add(aGenre);
            return true;
        }
        return false;
    }

    public String getAllAuthors() {
        return authorsAsString.toString();
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getAuhtors() {
        return authors;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }

    public Book(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYearOfPublish() {
        return yearOfPublish;
    }

    public void setYearOfPublish(int yearOfPublish) {
        this.yearOfPublish = yearOfPublish;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }
}
