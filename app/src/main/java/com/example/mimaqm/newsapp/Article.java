package com.example.mimaqm.newsapp;

public class Article {

    private String mTitle;
    private String mAuthorName;
    private String mCategory;
    private String mUrl;
    private String mDate;

    public Article(String title, String authorName, String category, String url, String date){
        mTitle = title;
        mAuthorName = authorName;
        mCategory = category;
        mUrl = url;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
