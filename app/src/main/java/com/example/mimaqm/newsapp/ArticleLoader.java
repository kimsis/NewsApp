package com.example.mimaqm.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    static Resources mResources = App.getContext().getResources();
    private static String ARTICLE_URL = mResources.getString(R.string.URL);

    public ArticleLoader( Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Article> loadInBackground() {

        if (ARTICLE_URL == null)
        return null;

        List<Article> articleList = QueryUtilities.fetchArticleData(ARTICLE_URL);
        return articleList;
    }
}
