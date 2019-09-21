package com.test.greedygames.imageloader;

import android.content.Context;
import android.graphics.Bitmap;

public class DoubleCache implements ImageCache {
    private Context context;

    private MemoryCache memCache;
    private DiskCache diskCache;


    public DoubleCache(Context context) {
        this.context = context;
        memCache = new MemoryCache();
        diskCache = new DiskCache(context);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        memCache.put(url, bitmap);
        diskCache.put(url, bitmap);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap diskBitmap = memCache.get(url);

        return diskBitmap != null ? diskBitmap : diskCache.get(url);
    }

    @Override
    public void clear() {
        memCache.clear();
        diskCache.clear();
    }
}
