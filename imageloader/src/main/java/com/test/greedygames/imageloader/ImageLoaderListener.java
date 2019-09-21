package com.test.greedygames.imageloader;

import android.graphics.Bitmap;

public interface ImageLoaderListener {
    public void onSuccess(Bitmap bitmap);
    public void onFailure(Exception e);
}
