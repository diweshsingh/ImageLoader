package com.test.greedygames.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class DiskCache implements ImageCache {
    private static final String TAG = "DiskCache";
    private Context context;
    private DiskLruCache cache;


    public DiskCache(Context context) {
//        Log.d(TAG, "DiskCache Constructor called");
        this.context = context;
        try {
            cache = DiskLruCache.open(context.getCacheDir(), 1, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Bitmap get(String url) {
//        Log.d(TAG, "DiskCache get method called");
        String key = MD5(url);
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                BufferedInputStream buffIn = new BufferedInputStream(inputStream, 8 * 1024);
                Bitmap bitmap = BitmapFactory.decodeStream(buffIn);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Convert the String URL to unique key by replacing the unwanted String. As the key must match REJEX
     *
     * @param url String Url
     * @return Modified string as KEY
     */
    private String makeCompatibleStringKey(String url) {
//        Log.d(TAG,"DiskCache makeCompatibleStringKey method called");


        return url.replace(".", "-")
                .replaceAll("[^a-z0-9_-]", "");
    }

    /**
     * convert String to MD5.
     *
     * @param md5 String and it should be null
     * @return MD5 String
     */
    private String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
//        Log.d(TAG, "DiskCache put method called");

        String key = MD5(url);
        DiskLruCache.Editor editor = null;
        try {
            editor = cache.edit(key);
            if (editor == null) {
                return;
            }
            if (writeBitmapToFile(bitmap, editor)) {
                cache.flush();
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {

        }

    }

    @Override
    public void clear() {
        try {
            cache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will write Bitmap to File.
     *
     * @param bitmap Bitmap object and it should not be null
     * @param editor Lru cahce editor, It should not be null
     * @return The status of the operation.
     */

    private boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) {
//        Log.d(TAG, "DiskCache writeBitmapToFile method called");

        OutputStream out = null;
        boolean status = false;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), 8 * 1024);
            status = bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

}
