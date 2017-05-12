package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ArticleSaleAdapter extends RecyclerView.Adapter<ArticleSaleAdapter.ArticleViewHolder> {

    public static final String TAG = ArticleSaleAdapter.class.getSimpleName();

    Context mContext;
    List<ArticleSale> mArticleSaleList = new ArrayList<>();
    List<Article> mArticleList = new ArrayList<>();
    List<String> mArticlesKeys = new ArrayList<>();
    Query mQuery;
    ValueEventListener mEventListener;

    // Values for updating the activity views
    private long mTotalPrice = 0;
    private int mTotalAmount = 0;
    private Map<String, ArticleSale> mArticlesForSale = new HashMap<>();


    public ArticleSaleAdapter(Context context, Query articlesQuery) {
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
                mArticleList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    String articleKey = snapshot.getKey();
                    mArticleList.add(article);
                    ArticleSale articleSale = new ArticleSale(0, 0L);
                    mArticleSaleList.add(articleSale);
                    mArticlesKeys.add(articleKey);
                }
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
        String stringDescription = TextHelper.capitalizeFirestLetter(article.getDescripcion());
        holder.mArticleDescriptionTextView.setText(stringDescription);
        String stringArticlePrice = MathHelper.getLocalCurrency(article.getPrecio());
        holder.mArticlePriceTextView.setText(stringArticlePrice);
        holder.mArticleCodeTextView.setText(code);
        String stringArticleTotalPrice = MathHelper.getLocalCurrency(String.valueOf(articleSale.getTotal()));
        holder.mArticlesTotalPriceTextView.setText(stringArticleTotalPrice);
        holder.mArticlesAmountTextView.setText(String.valueOf(articleSale.getCantidad()));
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

    public List<ArticleSale> getArticleSaleList() {
        if (mArticleSaleList != null) {
            return mArticleSaleList;
        } else {
            return null;
        }
    }

    public List<String> getArticlesKeys() {
        return mArticlesKeys;
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


    class ArticleViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_article_image)
        ImageView mArticleImageView;
        @BindView(R.id.tv_article_code)
        TextView mArticleCodeTextView;
        @BindView(R.id.tv_article_description)
        TextView mArticleDescriptionTextView;
        @BindView(R.id.tv_article_price)
        TextView mArticlePriceTextView;
        @BindView(R.id.tv_article_total_price)
        TextView mArticlesTotalPriceTextView;
        @BindView(R.id.tv_article_amount)
        TextView mArticlesAmountTextView;
        @BindView(R.id.imb_up_arrow)
        ImageButton mAddArticleButton;
        @BindView(R.id.imb_down_arrow)
        ImageButton mSubtractArticleButton;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
