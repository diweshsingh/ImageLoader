package com.test.greedygames.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
ImageLoader is a simple library to cache the web images.
 */
public class ImageLoader {

    private static ImageCache cache;
    private static ExecutorService executorService;
    private static Handler uiHandler;
    public static ImageLoader instance;
    private static List<Future> queuedFutures;


    public static ImageLoader get(Context context) {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                    //if context is not null initialize the configuration
                    if (context != null)
                        init(context);
                }
            }
        }
        return instance;
    }

    /**
     * init the ImageLoader Lib configuration
     *
     * @param context
     */
    private static void init(Context context) {
        cache = new DoubleCache(context);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        uiHandler = new Handler(Looper.getMainLooper());
        queuedFutures = new ArrayList<>();

    }

    /**
     * displayimage() will download the bitmap asynchonously and will set to the the imageview
     *
     * @param url       Valid image url and it should not be null
     * @param imageView valid imageview refrence and it should not be null
     */
    public void displayImage(final String url, final ImageView imageView) {

        /*
        if imageUrl or imageview will be null, don't proceed
         */
        if (url == null || imageView == null)
            return;

        //If Image is already in cache update the ImageView
        Bitmap cached = cache.get(url);
        if (cached != null) {

            updateImageView(imageView, cached);
            return;
        }

        //If image is not in cache, Download image and add the cache

        imageView.setTag(url);
        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap != null) {
//                    Log.d("URL", "Bitmap is dowlaoded");
                    if (imageView.getTag() == url) {
                        updateImageView(imageView, bitmap);
                    }
                    cache.put(url, bitmap);
                }
            }
        });

//        queuedFutures.add(future);
    }

    /* Remove all tasks in the queue and stop all running threads
     * Notify UI thread about the cancellation
     */
    public void cancelAllTasks() {
        synchronized (this) {
            for (Future task : queuedFutures) {
                if (!task.isDone()) {
                    task.cancel(true);
                    Log.d("TAG", "thread is cancelled");
                }
            }
            queuedFutures.clear();
        }
    }


    /**
     * This method will clear the both DiskCache and MemoryCache
     */
    public void clearCache() {
        this.cache.clear();
    }

    private void updateImageView(final ImageView imageView, final Bitmap bitmap) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);

            }
        });
    }

    /**
     * Download Bitmap from web
     *
     * @param srcUrl Source image url and it should not be null
     * @return It will return the downloaded Bitmap
     */
    private Bitmap downloadImage(String srcUrl) {
        if (srcUrl.contains("http://"))
            srcUrl = getModifiedUrl(srcUrl);
        Bitmap bitmap = null;
        try {
            URL url = new URL(srcUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    /**
     * This method is used to convert http url to https urls
     *
     * @param url String url and it should not be null
     * @return Modified URL
     */
    private String getModifiedUrl(String url) {

        return url.replace("http://", "https://");

    }
}
