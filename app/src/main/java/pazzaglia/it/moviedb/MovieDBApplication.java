package pazzaglia.it.moviedb;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by IO on 27/07/2016.
 */

public class MovieDBApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initPicasso();
    }

    private void initPicasso() {
        OkHttpClient picassoClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
        Picasso picasso = new Picasso.Builder(getApplicationContext()).downloader(new OkHttp3Downloader(picassoClient)).build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }
    }
}