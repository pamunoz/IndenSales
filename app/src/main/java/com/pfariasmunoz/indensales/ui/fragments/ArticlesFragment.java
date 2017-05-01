package com.pfariasmunoz.indensales.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.pfariasmunoz.indensales.ui.activities.AddSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends Fragment {

    public static final String TAG = ArticlesFragment.class.getSimpleName();

    private RecyclerView mArticlesRecyclerView;
    private FirebaseRecyclerAdapter<Article, ArticleViewHolder> mArticleAdapter;
    private View mRootView;
    private AddSaleActivity mActivity;

    public ArticlesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_articles, container, false);
        mActivity = ((AddSaleActivity)getActivity());
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

                if (mActivity.getArticlesMap().isEmpty() || !mActivity.getArticlesMap().containsKey(key)) {
                    viewHolder.setAmount(0);
                    viewHolder.setTotalPrice(0);
                } else {
                    viewHolder.setTotalPrice(mActivity.getArticlesMap().get(key));
                    viewHolder.setAmount(mActivity.getArticlesMap().get(key));
                }


                viewHolder.getAddButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        mActivity.addArticle(articleKey, viewHolder);
                    }
                });

                viewHolder.getSubstractButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        mActivity.subtractArticle(articleKey, viewHolder);
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

}
