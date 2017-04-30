package com.pfariasmunoz.indensales.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends BaseFragment {

    private ArrayList<Sale> mSalesList = new ArrayList<>();
    private ArrayList<ArticleSale> mArticleSaleList = new ArrayList<>();

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
                FirebaseDb.sArticlesRef) {
            @Override
            protected void populateViewHolder(final ArticleViewHolder viewHolder, Article model, final int position) {

                viewHolder.setTextOnViews(model);

                viewHolder.setAmount(mAmount);
                String totalPrice = MathHelper.setTotalPrice(mAmount, model.getPrecio());


                viewHolder.getAddButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        viewHolder.getTotalPriceTextView()
                                .setText(MathHelper.setTotalPrice(mAmount, viewHolder
                                        .getStringPrice()));
                        viewHolder.setAmount(mAmount);
                    }
                });

                viewHolder.getSubstractButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAmount > 0) {
                            mAmount--;
                        }
                        viewHolder.getTotalPriceTextView()
                                .setText(MathHelper.setTotalPrice(mAmount, viewHolder
                                        .getStringPrice()));
                        viewHolder.setAmount(mAmount);
                    }
                });

                viewHolder.getTotalPriceTextView().setText(totalPrice);
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
