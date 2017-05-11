package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticlesViewHolder;

import java.util.ArrayList;
import java.util.List;

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

    public ArticleSaleAdapter(Query articlesQuery) {
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
        holder.mArticleDescriptionTextView.setText(article.getDescripcion());
        holder.mArticlePriceTextView.setText(article.getPrecio());
        holder.mArticleCodeTextView.setText(code);
        holder.mArticlesTotalPriceTextView.setText(String.valueOf(articleSale.getTotal()));
        holder.mArticlesAmountTextView.setText(String.valueOf(articleSale.getCantidad()));
        holder.mAddArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int amount = articleSale.getCantidad() + 1;
                long total = Long.valueOf(article.getPrecio().trim()) * amount;
                mArticleSaleList.set(position, new ArticleSale(amount, total));
                notifyDataSetChanged();
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

    public void logArticlesSales() {
        List<ArticleSale> sales = getArticleSaleList();
        for (ArticleSale sale : sales) {
            Log.i(TAG, "TOTAL: " + String.valueOf(sale.getTotal()) + " AMOUNT: " + String.valueOf(sale.getCantidad()));
        }
    }

    public List<Article> getArticleList() {
        return mArticleList;
    }

    public List<String> getArticlesKeys() {
        return mArticlesKeys;
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
