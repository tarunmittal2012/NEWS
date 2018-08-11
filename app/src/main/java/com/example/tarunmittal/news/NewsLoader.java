package com.example.tarunmittal.news;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {

        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<News> newsList = QueryUtils.fetchEarthquakeData(mUrl);
        return newsList;
    }
}

