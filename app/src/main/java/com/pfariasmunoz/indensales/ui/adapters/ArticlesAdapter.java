package com.pfariasmunoz.indensales.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;

import java.util.HashMap;
import java.util.List;

public class ArticlesAdapter extends ArrayAdapter<Article> {

    private static class ViewHolder {
        ImageView articleImageView;
        TextView articleDescriptionTextView;
        TextView articlePriceTextView;
        ImageButton addButton;
        ImageButton subtractButton;
        TextView articleAmountTextView;
        TextView articleTotalPrice;
    }

    // Constructor
    public ArticlesAdapter(@NonNull Context context,
                           @LayoutRes int resource,
                           @NonNull List<Article> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Article article = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article, parent, false);
            viewHolder.articleImageView = (ImageView) convertView.findViewById(R.id.iv_article_image);
            viewHolder.articleDescriptionTextView = (TextView) convertView.findViewById(R.id.tv_article_description);
            viewHolder.articlePriceTextView = (TextView) convertView.findViewById(R.id.tv_article_price);
            viewHolder.addButton = (ImageButton) convertView.findViewById(R.id.imb_up_arrow);
            viewHolder.subtractButton = (ImageButton) convertView.findViewById(R.id.imb_down_arrow);
            viewHolder.articleAmountTextView = (TextView) convertView.findViewById(R.id.tv_article_amount);
            viewHolder.articleTotalPrice = (TextView) convertView.findViewById(R.id.tv_article_total_price);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String description = "";
        String price = "";
        if (article != null) {
            description = article.getDescripcion();
            price = article.getPrecio();
        }
        viewHolder.articleDescriptionTextView.setText(description);
        viewHolder.articlePriceTextView.setText(price);
        // Set listener on add button
        viewHolder.addButton.setTag(article);
        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = (Article) v.getTag();
                Toast.makeText(getContext(), "Article to add: " + article.getPrecio(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set listener on subtract button
        viewHolder.subtractButton.setTag(article);
        viewHolder.subtractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article1 = (Article) v.getTag();
                Toast.makeText(getContext(), "Article to subtract" + article.getPrecio(), Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }



    public void addArticle(String articleKey, HashMap<String, Integer> map) {
        if (!map.isEmpty()) {
            if (map.containsKey(articleKey)) {
                map.put(articleKey, map.get(articleKey) + 1);
            } else {
                map.put(articleKey, 1);
            }
        } else {
            map.put(articleKey, 1);
        }
//        viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
//        viewHolder.setAmount(mArticlesMap.get(articleKey));
    }




}
