package com.pfariasmunoz.indensales.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;

import java.util.List;

/**
 * Created by Pablo Farias on 02-05-17.
 */

public class AddSaleAdapter extends RecyclerView.Adapter<AddSaleAdapter.ArticleViewHolder> {

    private List<Article> mArticles;

    public AddSaleAdapter(List<Article> articleList) {
        this.mArticles = articleList;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = mArticles.get(position);
        holder.mArticlePriceTextView.setText(article.getPrecio());
        holder.mArticleDescriptionTextView.setText(article.getDescripcion());
        holder.mArticleTotalPriceTextView.setText(article.getPrecio());
        holder.mArticleAmountTextView.setText(String.valueOf(0));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView mArticleDescriptionTextView;
        TextView mArticlePriceTextView;
        TextView mArticleAmountTextView;
        TextView mArticleTotalPriceTextView;


        public ArticleViewHolder(View itemView) {
            super(itemView);
            mArticleDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_article_description);
            mArticlePriceTextView = (TextView) itemView.findViewById(R.id.tv_article_price);
            mArticleAmountTextView = (TextView) itemView.findViewById(R.id.tv_article_amount);
            mArticleTotalPriceTextView = (TextView) itemView.findViewById(R.id.tv_article_total_price);
        }
    }
}
