package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.activities.CreateSaleActivity;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



public class ArticleSaleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    public static final String TAG = ArticleSaleAdapter.class.getSimpleName();

    Context mContext;
    // New Data
    private List<ArticleSale> mArticleSaleList = new ArrayList<>();
    private List<Article> mArticleList = new ArrayList<>();
    private List<String> mArticlesKeys = new ArrayList<>();
    private Query mQuery;
    private ValueEventListener mEventListener;

    // Values for updating the activity views
    private long mTotalPrice = 0;
    private int mTotalAmount = 0;
    private Map<String, ArticleSale> mArticlesForSale = new HashMap<>();


    public ArticleSaleAdapter(Context context, Query articlesQuery) {
        // Initialize current data
        this.mContext = context;
        this.mQuery = articlesQuery;
        mEventListener = setUpListener();
        mQuery.addValueEventListener(mEventListener);
    }

    public ArticleSaleAdapter(Context context, Query articlesQuery, List<ArticleSale> articleSales, List<Article> articleList, List<String> articlesKeys) {
        // Initialize current data
        mArticleSaleList.addAll(articleSales);
        mArticleList.addAll(articleList);
        mArticlesKeys.addAll(articlesKeys);
        this.mContext = context;
        this.mQuery = articlesQuery;
        mEventListener = setUpListener();
        mQuery.addValueEventListener(mEventListener);
    }


    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_article_sale;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    private ValueEventListener setUpListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    String articleKey = snapshot.getKey();
                    if (!isKeyInList(articleKey, mArticlesKeys)) {
                        mArticleList.add(article);
                        ArticleSale articleSale = new ArticleSale(0, 0L);
                        mArticleSaleList.add(articleSale);
                        mArticlesKeys.add(articleKey);
                    }

                }
                Log.i(TAG, "LENGHT OF KEY LIST: " + String.valueOf(mArticlesKeys.size()));
                Log.i(TAG, "LENGHT OF ARTICLE LIST: " + String.valueOf(mArticleList.size()));
                Log.i(TAG, "LENGHT OF ARTICLE SALE LIST: " + String.valueOf(mArticleSaleList.size()));
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return listener;
    }

    public void cleanup() {
        mQuery.removeEventListener(mEventListener);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {

        final ArticleSale articleSale = mArticleSaleList.get(position);
        final Article article = mArticleList.get(position);
        String code = mArticlesKeys.get(position);

        holder.bind(article, articleSale, code);
        holder.mAddArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int amount = articleSale.getCantidad() + 1;
                long total = Long.valueOf(article.getPrecio().trim()) * amount;
                mArticleSaleList.set(position, new ArticleSale(amount, total));
                notifyDataSetChanged();
                setToalPriceAndAmount();
            }
        });
        holder.mSubtractArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articleSale.getCantidad() <= 1) {
                    mArticleSaleList.set(position, new ArticleSale(0, 0L));
                } else {
                    int amount = articleSale.getCantidad() - 1;
                    long total = Long.valueOf(article.getPrecio()) * amount;
                    mArticleSaleList.set(position, new ArticleSale(amount, total));
                }
                notifyDataSetChanged();
                setToalPriceAndAmount();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mArticleList ? mArticleList.size() : 0);
    }

    private Context getContext() {
        return mContext;
    }

    public Map<String, ArticleSale> getArticlesForSale() {
        boolean areListEqual = mArticlesKeys.size() == mArticleSaleList.size();
        if (areListEqual) {
            for (int i = 0; i < mArticlesKeys.size(); i++) {
                for (int j = 0; j < mArticleSaleList.size(); j++) {
                    ArticleSale sale = mArticleSaleList.get(i);
                    String key = mArticlesKeys.get(i);
                    if (sale.getCantidad() > 0) {
                        mArticlesForSale.put(key, sale);
                    }
                }
            }
        }
        return (mArticlesForSale.size() > 0 ? mArticlesForSale : null);
    }

    private void setToalPriceAndAmount() {
        mTotalPrice = 0;
        mTotalAmount = 0;
        if (getArticlesForSale() != null) {
            Iterator it = getArticlesForSale().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                ArticleSale sale = (ArticleSale) pair.getValue();
                mTotalPrice += sale.getTotal();
                mTotalAmount += sale.getCantidad();
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
        ((CreateSaleActivity)getContext()).setTotals(mTotalPrice, mTotalAmount);
    }

    public long getTotalPrice() {
        return mTotalPrice;
    }

    public List<ArticleSale> getArticleSaleList() {
        return mArticleSaleList;
    }

    public List<Article> getArticleList() {
        return mArticleList;
    }

    public List<String> getArticlesKeys() {
        return mArticlesKeys;
    }

    private boolean isKeyInList(String search, List<String> keys) {
        for (String key : keys) {
            if (keys != null && keys.size() > 0) {
                if (key.trim().contains(search)) {
                    return true;
                }
            }
        }
        return false;
    }
}
