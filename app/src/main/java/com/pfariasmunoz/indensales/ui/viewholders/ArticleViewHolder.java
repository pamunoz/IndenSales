package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 12-05-17.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.iv_article_image)
    public ImageView mArticleImageView;
    @BindView(R.id.tv_article_code)
    public TextView mArticleCodeTextView;
    @BindView(R.id.tv_article_description)
    public TextView mArticleDescriptionTextView;
    @BindView(R.id.tv_article_price)
    public TextView mArticlePriceTextView;
    @BindView(R.id.tv_article_total_price)
    public TextView mArticlesTotalPriceTextView;
    @BindView(R.id.tv_article_amount)
    public TextView mArticlesAmountTextView;
    @BindView(R.id.imb_up_arrow)
    public ImageButton mAddArticleButton;
    @BindView(R.id.imb_down_arrow)
    public ImageButton mSubtractArticleButton;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
