package com.example.mimaqm.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);

        }

        Article currentArticle = getItem(position);

        TextView titleTextView = listItemView.findViewById(R.id.article_title);
        titleTextView.setText(currentArticle.getTitle());

        TextView categoryTextView = listItemView.findViewById(R.id.article_category);
        categoryTextView.setText(currentArticle.getCategory());

        TextView dateTextView = listItemView.findViewById(R.id.article_date);
        dateTextView.setText(currentArticle.getDate());

        TextView TextView = listItemView.findViewById(R.id.article_author);
        TextView.setText(currentArticle.getAuthorName());

        return listItemView;
    }
}