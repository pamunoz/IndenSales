package com.pfariasmunoz.indensales.ui.viewholders;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.utils.MathHelper;

/**
 * Created by Pablo Farias on 17-04-17.
 */

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    private TextView mDescriptionTextView;
    private TextView mPriceTextView;
    private TextView mTotalPriceTextView;
    private TextView mAmountTextView;
    private ImageButton mAddButton;
    private ImageButton mSubstractButton;
    private View mView;



    public ArticleViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mDescriptionTextView = (TextView) mView.findViewById(R.id.tv_article_description);
        mPriceTextView = (TextView) mView.findViewById(R.id.tv_article_price);
        mTotalPriceTextView = (TextView) mView.findViewById(R.id.tv_article_total_price);
        mAmountTextView = (TextView) mView.findViewById(R.id.tv_article_amount);
        mAddButton = (ImageButton) mView.findViewById(R.id.imb_up_arrow);
        mSubstractButton = (ImageButton) mView.findViewById(R.id.imb_down_arrow);
    }

    public void setTextOnViews(Article article) {
        mDescriptionTextView.setText(article.getDescripcion());
        mPriceTextView.setText(article.getPrecio());

    }

    public View getView() {
        return mView;
    }

    public ImageButton getAddButton() {
        return mAddButton;
    }

    public ImageButton getSubstractButton() {
        return mSubstractButton;
    }

    public void setAmount(int amount) {
        String stringPrice = String.valueOf(amount);
        mAmountTextView.setText(stringPrice);
    }

    public TextView getTotalPriceTextView() {
        return mTotalPriceTextView;
    }

    public String getStringPrice() {
        return mPriceTextView.getText().toString().trim();
    }

    public void setTotalPrice(int amount) {
        String totalPrice = MathHelper.setTotalPrice(amount, getStringPrice());
        mTotalPriceTextView.setText(totalPrice);
    }
}
