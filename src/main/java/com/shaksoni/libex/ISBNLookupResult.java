package com.shaksoni.libex;

/**
 * Created by shaksoni on 9/26/17.
 */

public interface ISBNLookupResult {
    void onSuccess(String bookInfo);
    void onFailure(Exception e);
}
