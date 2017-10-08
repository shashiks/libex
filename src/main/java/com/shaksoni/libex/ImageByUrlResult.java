package com.shaksoni.libex;

import android.graphics.Bitmap;

/**
 * Created by shaksoni on 9/28/17.
 */

public interface ImageByUrlResult {


    public void onSuccess(Bitmap downloadedImage);

    public void onFailure(Exception e);
}
