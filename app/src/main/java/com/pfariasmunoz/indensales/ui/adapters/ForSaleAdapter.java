package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo Farias on 12-05-17.
 */

public class ForSaleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    private Map<String, ArticleSale> mArticlesForSale = new HashMap<>();
    private Context mContext;

    public ForSaleAdapter(Map<String, ArticleSale> articlesForSale, Context context) {
        mArticlesForSale = articlesForSale;
        mContext = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
