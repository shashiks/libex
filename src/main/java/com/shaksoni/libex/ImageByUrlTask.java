package com.shaksoni.libex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

/**
 * Created by shaksoni on 9/28/17.
 */

public class ImageByUrlTask extends AsyncTask {



    private String imageUrl;
    private  ImageByUrlResult result;


    public ImageByUrlTask(String url, ImageByUrlResult result) {
        imageUrl = url;
        this.result = result;

    }


    @Override
    protected Object doInBackground(Object[] objects) {


        try {
            URL imgUrl = new URL(imageUrl);
            Bitmap bitmap = BitmapFactory.decodeStream(imgUrl.openStream());
            result.onSuccess(bitmap);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Cant read image at " + imageUrl, e);
            result.onFailure(e);
        }

        return null;
    }


}
