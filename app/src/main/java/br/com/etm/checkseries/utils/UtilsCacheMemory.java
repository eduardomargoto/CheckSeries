package br.com.etm.checkseries.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by EDUARDO_MARGOTO on 03/11/2015.
 */
public class UtilsCacheMemory {

    private static UtilsCacheMemory utilsCacheMemory = new UtilsCacheMemory();
    public final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    public final int cacheSize = maxMemory / 5;
    private LruCache<String, Bitmap> mMemoryCache;

    private UtilsCacheMemory() {}

    public static UtilsCacheMemory getInstance() {
        if (utilsCacheMemory == null)
            return new UtilsCacheMemory();
        else return utilsCacheMemory;
    }


    public LruCache<String, Bitmap> getLruCache() {
        utilsCacheMemory.mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                //return bitmap.getByteCount() / 1024;
                return UtilsImages.getBitmapByteCount(bitmap) / 1024;
            }
        };
        return utilsCacheMemory.mMemoryCache;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            getLruCache().put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return getLruCache().get(key);
    }
}
