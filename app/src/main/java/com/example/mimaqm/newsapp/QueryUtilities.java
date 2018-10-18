package com.example.mimaqm.newsapp;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtilities {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtilities.class.getSimpleName();

    static Resources mResources = App.getContext().getResources();
    static String UTF = mResources.getString(R.string.UTF);

    private QueryUtilities() {
    }

    /**
     * Query the Guardian database for JSON objects.
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, mResources.getString(R.string.error_HTTP_making), e);
        }

        // Return the list of {Articles}
        return extractFeatureFromJson(jsonResponse);
    }

    private static String formatDate(String date) {
        String jsonPattern = mResources.getString(R.string.date_format);
        SimpleDateFormat jsonDateFormatter = new SimpleDateFormat(jsonPattern, Locale.UK);
        try {
            Date parsedJsonDate = jsonDateFormatter.parse(date);
            String finalDatePattern = mResources.getString(R.string.final_date_format);
            SimpleDateFormat finalDateFormatter = new SimpleDateFormat(finalDatePattern, Locale.UK);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, mResources.getString(R.string.error_JSON_parsing_date), e);
            return "";
        }
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, mResources.getString(R.string.error_URL_building), e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod(mResources.getString(R.string.get));
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, mResources.getString(R.string.error_URL_response) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, mResources.getString(R.string.error_JSON_retrieving_article), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(UTF));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Article> extractFeatureFromJson(String articleJSON) {
        // If the JSON is null or empty, leave prematurely
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Create an ArrayList in which we can store the articles
        List<Article> articles = new ArrayList<>();

        // Try parsing the JSONObject and catch it if it fails, throwing an exception
        try {

            // Create the JSONObject from a JSON string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            JSONObject response = baseJsonResponse.getJSONObject(mResources.getString(R.string.response));
            JSONArray results = response.getJSONArray(mResources.getString(R.string.results));

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject baseJSONArticle = results.getJSONObject(i);
                String articleTitle = baseJSONArticle.getString(mResources.getString(R.string.webTitle));
                JSONArray articleTags = baseJSONArticle.getJSONArray(mResources.getString(R.string.tags));
                String articleAuthorName;
                if(articleTags.length() != 0)
                {
                    articleAuthorName = articleTags.getJSONObject(0).getString(mResources.getString(R.string.webTitle));
                }
                else
                {
                    articleAuthorName = mResources.getString(R.string.no_author);
                }
                String articleCategory = baseJSONArticle.getString(mResources.getString(R.string.sectionName));
                String articleUrl = baseJSONArticle.getString(mResources.getString(R.string.webUrl));
                String articleDate = baseJSONArticle.getString(mResources.getString(R.string.webPublicationDate ));
                articleDate = formatDate(articleDate);

                Article article = new Article(articleTitle, articleAuthorName, articleCategory, articleUrl, articleDate);
                articles.add(article);

            }

        } catch (JSONException e) {
            // If something goes wrong, we make an Error Log with the following text
            Log.e(LOG_TAG, mResources.getString(R.string.error_JSON_parsing_results), e);
        }

        // Return the list of articles
        return articles;
    }

}
