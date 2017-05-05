package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;

/**
 * Created by Pablo Farias on 05-05-17.
 */

public class ArticlesViewHolder extends RecyclerView.ViewHolder {

    private TextView mArticleDescriptionTextView;
    private TextView mArticlePriceTextView;
    private TextView mArticleAmountTextView;
    private TextView mArticleTotalPriceTextView;
    private ImageButton mAddArticleToSaleButton;
    private ImageButton mSubtractArticleFromSalesButton;


    public ArticlesViewHolder(View itemView) {
        super(itemView);
        mArticleDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_article_description);
        mArticlePriceTextView = (TextView) itemView.findViewById(R.id.tv_article_price);
        mArticleAmountTextView = (TextView) itemView.findViewById(R.id.tv_article_amount);
        mArticleTotalPriceTextView = (TextView) itemView.findViewById(R.id.tv_article_total_price);

        mAddArticleToSaleButton = (ImageButton) itemView.findViewById(R.id.imb_up_arrow);
        mSubtractArticleFromSalesButton = (ImageButton) itemView.findViewById(R.id.imb_down_arrow);
    }

    public ImageButton getAddArticleToSaleButton() {
        return mAddArticleToSaleButton;
    }

    public ImageButton getSubtractArticleFromSalesButton() {
        return mSubtractArticleFromSalesButton;
    }

    public void setAmountAndPriceToZero() {
        mArticleAmountTextView.setText("0");
        mArticleTotalPriceTextView.setText("0");
    }

    public void setArticleAmount(String amount) {
        mArticleAmountTextView.setText(amount);
    }

    public void setArticleTotalPrice(String price) {
        mArticleTotalPriceTextView.setText(price);
    }

    public void setDescriptionAndPrice(Article article) {
        mArticleDescriptionTextView.setText(article.getDescripcion());
        mArticlePriceTextView.setText(article.getPrecio());
    }

    public TextView getArticleAmountTextView() {
        return mArticleAmountTextView;
    }

    public TextView getArticleTotalPriceTextView() {
        return mArticleTotalPriceTextView;
    }
}
