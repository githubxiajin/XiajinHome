package com.xiajin.home.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

/**
 * image
 *
 * @author liu
 */
public class MyImageLoader {

    private static MyImageLoader MyImageLoader;
    private static RequestQueue mQueue;
    private BitmapCache bitmapCache;
    private ImageLoader imageLoader;

    public BitmapCache getBitmapCache() {
        return bitmapCache;
    }

    private MyImageLoader() {
    }

    public static MyImageLoader getInstance(Context context) {

        if (MyImageLoader == null) {
            MyImageLoader = new MyImageLoader();
            mQueue = Volley.newRequestQueue(context);

        }
        return MyImageLoader;

    }


    public void display(ImageView imageView,
                        int default_image, int failed_image, String url, int width,
                        int height) {
        if (bitmapCache == null) {
            bitmapCache = new BitmapCache();
        }
        if (imageLoader == null) {
            imageLoader = new ImageLoader(mQueue, bitmapCache);
        }
            ImageListener listener = ImageLoader.getImageListener(imageView,
                    default_image, failed_image);

        imageLoader.get(url, listener, width, height);

    }

    public class BitmapCache implements ImageCache {

        private LruCache<String, Bitmap> cache;

        public BitmapCache() {
            cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }
}
