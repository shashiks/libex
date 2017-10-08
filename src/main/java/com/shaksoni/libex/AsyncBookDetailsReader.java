package com.shaksoni.libex;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shaksoni on 9/26/17.
 *
 * Next time try this for http calls but thats sync
 * https://kylewbanks.com/blog/tutorial-parsing-json-on-android-using-gson-and-volley
 */

public class AsyncBookDetailsReader extends AsyncTask<String, String, String> {

    String isbn = "9780963270702";//stanaic verses


    private View rootView;
    private ISBNLookupResult lookupResult;
    private FragmentAddBook.SEARCH_TYPES currentSearchType;

    public AsyncBookDetailsReader(FragmentAddBook.SEARCH_TYPES srchTypes, View rootView, ISBNLookupResult lookupResult) {
        this.rootView = rootView;
        this.lookupResult = lookupResult;
        currentSearchType = srchTypes;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressBar progressBar = rootView.findViewById(R.id.pBarSearchByDetails);
        progressBar.setVisibility(View.VISIBLE);
        if(currentSearchType == FragmentAddBook.SEARCH_TYPES.BY_ISBN.BY_ISBN) {
            rootView.findViewById(R.id.pnlIsbnSearch).setEnabled(false);
        } else {
            rootView.findViewById(R.id.pnlDetailSearch).setEnabled(false);
        }
        /*
        rootView.findViewById(R.id.btnLookupBookDetails).setEnabled(false);
        rootView.findViewById(R.id.txtLookupIsbn).setEnabled(false);
        */

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(currentSearchType == FragmentAddBook.SEARCH_TYPES.BY_ISBN.BY_ISBN) {
            rootView.findViewById(R.id.pnlIsbnSearch).setEnabled(true);
        } else {
            rootView.findViewById(R.id.pnlDetailSearch).setEnabled(true);
        }
        ProgressBar progressBar = rootView.findViewById(R.id.pBarSearchByDetails);
        progressBar.setVisibility(View.INVISIBLE);

//        ProgressBar progressBar = rootView.findViewById(R.id.progBarLookupBookDetails);
//        rootView.findViewById(R.id.btnLookupBookDetails).setEnabled(true);
//        rootView.findViewById(R.id.txtLookupIsbn).setEnabled(true);
//        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        InputStreamReader isw = null;
        boolean success = true;
        String isbnLookupAddress = strings[0];
        char[] content = new char[4096];
        try {
            URL url = new URL(isbnLookupAddress);
            urlConnection = (HttpURLConnection) url.openConnection();
            isw = new InputStreamReader(urlConnection.getInputStream());


            int ind = 0;
            int data = isw.read();
            while (data != -1) {
                char current = (char) data;
                    data = isw.read();
                    content[ind++] = current; // handle greater than 4096
            }


        }catch (Exception e) {
            Log.e("Error connecting" ,e.toString());
            success = false;
            lookupResult.onFailure(e);
        } finally {
            if(isw != null) {
                try {
                    isw.close();
                } catch (Exception e) {
                    Log.e("Err clsing isw ", e.getStackTrace().toString());

                }
            }
            if(urlConnection != null)
                urlConnection.disconnect();
        }

        if(success) {
            //what shit is this. do something better here!!!!
            String details = new String(content);
            details = details.replaceAll("(\\r\\n|\\n|\\r)", "");
            details = details.substring(0, (details.lastIndexOf('}')+1));
            lookupResult.onSuccess(details);
        }
        return null;
    }

}
