package com.example.tarunmittal.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull List<News> news) {

        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom, parent, false);

        }
        News currentNews = getItem(position);
        TextView webTitle = listItemView.findViewById(R.id.web_title);
        TextView webPublicationDate = listItemView.findViewById(R.id.publication_date);
        TextView type = listItemView.findViewById(R.id.news_type);
        TextView sectionName = listItemView.findViewById(R.id.section_name);
        TextView authorName = listItemView.findViewById(R.id.author_name);
        webTitle.setText(currentNews.getWebTitle());
        type.setText(currentNews.getType());
        authorName.setText(currentNews.getAuthorName());
        sectionName.setText(currentNews.getSectionName());
        webPublicationDate.setText(currentNews.getWebPublicationDate());
        return listItemView;
    }
}
