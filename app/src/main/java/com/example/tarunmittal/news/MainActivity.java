package com.example.tarunmittal.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final String NEWS_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=6583ac2e-75a5-4032-bb9d-1da35a70e296&show-fields=byline";

    private static final int NEWS_LOADER_ID = 1;

    private static final String SHOW_TAG_KEY = "show-tags";

    private static final String PAGE_SIZE_KEY = "page-size";

    private static final String ORDER_BY_KEY = "order-by";

    private static final String SECTION_KEY = "section";

    private static final String SHOW_TAG_VALUE = "contributor";

    ListView newsView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    LinearLayout mLinearLayout;

    TextView mEmptyStateTextView;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsView = findViewById(R.id.news_list);
        mSwipeRefreshLayout = findViewById(R.id.refresh_layout);
        newsView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView = findViewById(R.id.error_textview);
        mLinearLayout = findViewById(R.id.linear);

        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.list_background));
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsView.setAdapter(mAdapter);
        newsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                News currentNews = mAdapter.getItem(i);
                Uri newUri = Uri.parse(currentNews.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, newUri);
                startActivity(intent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this).forceLoad();

        } else {
            View loadingIndicator = findViewById(R.id.indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        String minNews = preference.getString(getString(R.string.minimum_news_key), getString(R.string.minimum_news_value));
        String orderBy = preference.getString(getString(R.string.order_by_key), getString(R.string.order_by_default));
        String section = preference.getString(getString(R.string.section_key), getString(R.string.section_default));

        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(SHOW_TAG_KEY, SHOW_TAG_VALUE);
        uriBuilder.appendQueryParameter(PAGE_SIZE_KEY, minNews);
        uriBuilder.appendQueryParameter(ORDER_BY_KEY, orderBy);

        if (!section.equals(getString(R.string.section_default))) {
            uriBuilder.appendQueryParameter(SECTION_KEY, section);
        }
        Log.e(LOG_TAG, uriBuilder.toString());
        return new NewsLoader(this, uriBuilder.toString());

    }

    @Override
    public void onBackPressed() {

        getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this).forceLoad();
        super.onBackPressed();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> news) {

        View loadingIndicator = findViewById(R.id.indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_news_found);
        if (news != null && !news.isEmpty()) {
            mAdapter.setNotifyOnChange(false);
            mAdapter.clear();
            mAdapter.setNotifyOnChange(true);
            mAdapter.addAll(news);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.COLOR_GREEN))));
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.GONE);
        } else {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getString(R.string.COLOR_DARK_RED))));
            mLinearLayout.setVisibility(View.VISIBLE);
            newsView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {

        mAdapter.clear();
    }

    @Override
    public void onRefresh() {

        getSupportLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

