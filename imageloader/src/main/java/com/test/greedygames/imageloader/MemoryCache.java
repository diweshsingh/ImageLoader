package com.test.greedygames.imageloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class MemoryCache implements ImageCache {

    private final String TAG="MemoryCache";

    private LruCache<String, Bitmap> cache;

    public MemoryCache() {
        Log.d(TAG,"MemoryCache Constructor called");
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024;
        int cacheSize = (int) (maxMemory / 4);

        this.cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return (bitmap != null ? bitmap.getRowBytes() : 0) * (bitmap != null ? bitmap.getHeight() : 0) / 1024;
            }
        };
    }


    @Override
    public void put(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
        Log.d(TAG,"MemoryCache put method called");

    }

    @Override
    public Bitmap get(String url) {
        Log.d(TAG,"MemoryCache get method called");

        return cache.get(url);
    }

    @Override
    public void clear() {
        cache.evictAll();


    }
}
