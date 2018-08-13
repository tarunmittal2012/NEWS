package com.example.tarunmittal.news;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
public class QueryUtils {

    private final static String LOG_TAG = QueryUtils.class.getName();

    private static final int READ_TIMEOUT = 10000;

    private static final int CONNECTION_TIMEOUT = 10000;

    private static final String KEY_TITLE = "webTitle";

    private static final String KEY_TYPE = "type";

    private static final String KEY_URL = "webUrl";

    private static final String KEY_SECTION_NAME = "sectionName";

    private static final String KEY_FIELD = "fields";

    private static final String KEY_BYLINE = "byline";

    private static final String KEY_RESPONSE = "response";

    private static final String KEY_GET = "GET";

    private static final String KEY_RESULT = "results";

    private static final String KEY_PUBLICATION_DATE = "webPublicationDate";

    private QueryUtils() {

    }

    private static String makeHTTPRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            httpURLConnection.setRequestMethod(KEY_GET);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code:" + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the NEWS JSON results.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.data_msg2), e);

            e.printStackTrace();
        }
        return url;
    }

    public static List<News> fetchEarthquakeData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.http_request_problem_message), e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    private static List<News> extractFeatureFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<News> news = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject(KEY_RESPONSE);
            JSONArray newsArray = response.getJSONArray(KEY_RESULT);

            for (int i = 0; i < newsArray.length(); i++) {

                Log.e(LOG_TAG, newsArray.length() + "");
                JSONObject currentNews = newsArray.getJSONObject(i);
                String type = currentNews.getString(KEY_TYPE);
                String webUrl = currentNews.getString(KEY_URL);
                String sectionName = currentNews.getString(KEY_SECTION_NAME);
                String webPublicationDate = currentNews.getString(KEY_PUBLICATION_DATE);
                String webTitle = currentNews.getString(KEY_TITLE);
                String author = "";
                JSONObject elements = newsArray.getJSONObject(i);
                if (elements.has(KEY_FIELD)) {
                    JSONObject fields = elements.getJSONObject(KEY_FIELD);
                    if (fields.has(KEY_BYLINE)) {
                        author = fields.getString(KEY_BYLINE);
                    }
                } else {
                    author = "No Author ..";
                }

                Log.e(LOG_TAG, type + "\n" +
                        sectionName + "\n" +
                        webPublicationDate + "\n" +
                        webTitle + "\n" +
                        author + "");
                News mNews = new News(webTitle, webPublicationDate, webUrl, type, sectionName, author);
                news.add(mNews);

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, Resources.getSystem().getString(R.string.problem_json_msg), e);
            e.printStackTrace();

        }
        return news;
    }

}
