package com.shaksoni.libex;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

/**
 * Created by shaksoni on 9/22/17.
 */

public class SearchTextListener implements SearchView.OnQueryTextListener {


    Activity parentActivity;

    public SearchTextListener(Activity parent) {
        parentActivity = parent;

    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        // newText is text entered by user to SearchView
        Toast.makeText(parentActivity.getApplicationContext(), query, Toast.LENGTH_LONG).show();
        ((LibexMain)parentActivity).selectItem(2);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
