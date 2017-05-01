package com.pfariasmunoz.indensales.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends BaseFragment {

    public static final String TAG = ArticlesFragment.class.getSimpleName();
    private Sale mSale;

    private ArrayList<ArticleSale> mArticleSaleList = new ArrayList<>();
    private Map<String, Integer> mArticlesMap = new HashMap<>();

    private int mAmount;
    private String mTotalPrice;

    private RecyclerView mArticlesRecyclerView;
    private FirebaseRecyclerAdapter<Article, ArticleViewHolder> mArticleAdapter;
    private View mRootView;

    public ArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_articles, container, false);
        mAmount = 0;
        initializeViews();
        return mRootView;
    }

    private void initializeViews() {
        mArticlesRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_articles);
        mArticlesRecyclerView.setHasFixedSize(false);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mArticlesRecyclerView.setLayoutManager(layoutManager);
        setupAdapter();
    }

    private void setupAdapter() {
        mArticleAdapter = new FirebaseRecyclerAdapter<Article, ArticleViewHolder>(
                Article.class,
                R.layout.item_article,
                ArticleViewHolder.class,
                FirebaseDb.sArticlesRef.limitToFirst(20)) {
            @Override
            protected void populateViewHolder(
                    final ArticleViewHolder viewHolder,
                    Article model, final int position) {

                viewHolder.setTextOnViews(model);
                String key = getRef(position).getKey();
                Log.i(TAG, "THE KEY IS: " + key);

                if (mArticlesMap.isEmpty() || !mArticlesMap.containsKey(key)) {
                    viewHolder.setAmount(0);
                    viewHolder.setTotalPrice(0);
                } else {
                    viewHolder.setTotalPrice(mArticlesMap.get(key));
                    viewHolder.setAmount(mArticlesMap.get(key));
                }


                viewHolder.getAddButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        addArticle(articleKey, viewHolder);
                    }
                });

                viewHolder.getSubstractButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        subtractArticle(articleKey, viewHolder);
                    }
                });
            }
        };
        mArticlesRecyclerView.setAdapter(mArticleAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mArticleAdapter != null) mArticleAdapter.cleanup();
    }

    private void addArticle(String articleKey, ArticleViewHolder viewHolder) {
        if (!mArticlesMap.isEmpty()) {
            if (mArticlesMap.containsKey(articleKey)) {
                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) + 1);
            } else {
                mArticlesMap.put(articleKey, 1);
            }
        } else {
            mArticlesMap.put(articleKey, 1);
            Log.i(TAG, "Key: " + articleKey + " and value: " + mArticlesMap.get(articleKey) + " Added");
        }
        viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
        viewHolder.setAmount(mArticlesMap.get(articleKey));
    }

    private void subtractArticle(String articleKey, ArticleViewHolder viewHolder) {
        if (mArticlesMap.size() == 0) {
            return;
        } else if (mArticlesMap.containsKey(articleKey)) {
            // if the amount of this article is 0, remove it from the map
            if (mArticlesMap.get(articleKey) <= 1) {
                mArticlesMap.remove(articleKey);
                viewHolder.setTotalPrice(0);
                viewHolder.setAmount(0);
            } else {
                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) - 1);
                viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
                viewHolder.setAmount(mArticlesMap.get(articleKey));
            }
        } else {
            return;
        }
    }



}
