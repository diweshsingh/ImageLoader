package com.test.greedygames.imageloader;

import android.graphics.Bitmap;

interface ImageCache {
    public void put(String url, Bitmap bitmap);

    public Bitmap get(String url);

    public void clear();
}
