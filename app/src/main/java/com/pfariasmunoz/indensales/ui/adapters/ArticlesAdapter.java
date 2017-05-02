package com.pfariasmunoz.indensales.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;

import java.util.List;

/**
 * Created by Pablo Farias on 02-05-17.
 */

public class ArticlesAdapter extends ArrayAdapter<Article> {

    // Constructor
    public ArticlesAdapter(@NonNull Context context,
                           @LayoutRes int resource,
                           @NonNull List<Article> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.item_article, parent, false);
        }

        ImageView articleImageView = (ImageView) convertView.findViewById(R.id.iv_article_image);
        TextView articleDescriptionTextView = (TextView) convertView.findViewById(R.id.tv_article_description);
        TextView articlePriceTextView = (TextView) convertView.findViewById(R.id.tv_article_price);
        ImageButton addButton = (ImageButton) convertView.findViewById(R.id.imb_up_arrow);
        ImageButton subtractButton = (ImageButton) convertView.findViewById(R.id.imb_down_arrow);
        TextView articleAmountTextView = (TextView) convertView.findViewById(R.id.tv_article_amount);

        Article article = getItem(position);

        articleDescriptionTextView.setText(article.getDescripcion());
        articlePriceTextView.setText(article.getPrecio());



        return convertView;
    }
}
