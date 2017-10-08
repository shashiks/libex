package com.shaksoni.libex;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Created by shaksoni on 9/26/17.
 */

public class JsonBookMarshaller {


    public Book fromJson(String apiJsonResponse) throws BookParseException {

        Book book = new Book();

        JsonElement root = new com.google.gson.JsonParser().parse(apiJsonResponse);

        int totalItems = root.getAsJsonObject().get("totalItems").getAsInt();
        if(totalItems > 0) {

            JsonArray authorsList = root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().getAsJsonArray("authors");
            for (JsonElement authorName : authorsList) {
                book.addAuthor(authorName.getAsString());
            }

            book.setTitle(root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().get("title").getAsString());

            JsonElement publisherElement = root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().get("publisher");
            if (publisherElement != null) {
                book.setPublisher(publisherElement.getAsString());
            }


            JsonElement publishYearElement = root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().get("publishedDate");
            if (publishYearElement != null) {
//            check the format
                String pubDate = publishYearElement.getAsString();
                if (pubDate.matches("\\d{4}.*")) { //format 1990xxxxxx
                    book.setYearOfPublish(new Integer(pubDate.substring(0, 4)));
                } else if (pubDate.matches(".*\\d{4}")) {  //format xxxxxxx1980
                    book.setYearOfPublish(new Integer(pubDate.substring(pubDate.indexOf('-')+1)));
                }
            }

            JsonArray isbns =root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().getAsJsonArray("industryIdentifiers");

            if(isbns != null && isbns.size() > 0) {

                //check there are two isbns before doing this
                book.setIsbn13(isbns.get(0)
                        .getAsJsonObject().get("identifier").getAsString());

                if(isbns.size() > 1) {
                    book.setIsbn10(isbns.get(1)
                            .getAsJsonObject().get("identifier").getAsString());
                }

            }

            JsonArray genres = root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().getAsJsonArray("categories");
            if(genres != null) {
                for (JsonElement aGenre : genres) {
                    book.addGenre(aGenre.getAsString());
                }
            }

            JsonElement imgUrl = root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().get("imageLinks")
                    .getAsJsonObject().get("smallThumbnail");
            if(imgUrl != null) {
                book.setThumbnailUrl(imgUrl.getAsString());
            }


            JsonElement pages = root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().get("pageCount");

            if(pages != null) {
                book.setNumPages(pages.getAsInt());
            }


            JsonElement avgrate =root.getAsJsonObject().getAsJsonArray("items").get(0)
                    .getAsJsonObject().get("volumeInfo")
                    .getAsJsonObject().get("averageRating");
            if(avgrate !=null) {

                book.setAverageRating( avgrate.getAsFloat());
            }


            return book;
        } else {
            if(totalItems == 0)
                throw new BookParseException("No books found for given value(s)");
        }
        return  null;
    }




}
