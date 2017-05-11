package com.pfariasmunoz.indensales.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.adapters.ArticleSaleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateSaleActivity extends AppCompatActivity {

    public static final String TAG = CreateSaleActivity.class.getSimpleName();

    @BindView(R.id.tv_client_name)
    TextView mClientNameTextView;
    @BindView(R.id.tv_client_rut)
    TextView mClientRutTextView;
    @BindView(R.id.tv_client_address)
    TextView mClientAddressTextView;
    @BindView(R.id.tv_articles_amount)
    TextView mSaleArticlesAmountTextView;
    @BindView(R.id.tv_sale_total_price)
    TextView mTotalPriceSaleTextView;
    @BindView(R.id.tv_is_sales_aproved)
    TextView mIsSaleAprovedTextView;

    private String mClientId;
    private String mUserId;
    private String mClientAddressId;

    private long mTotalSalePrice;
    private int mTotalAmount;

    private ValueEventListener mClientListener;
    private ValueEventListener mClientAddressListener;


    List<ArticleSale> mArticleSaleList;
    List<Article> mArticleList;

    ArticleSaleAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sale);

        ButterKnife.bind(this);
        mAdapter = new ArticleSaleAdapter(FirebaseDb.sArticlesRef.limitToFirst(30));

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.bt_create_sale)
    public void createSale() {
        List<ArticleSale> sales = mAdapter.getArticleSaleList();
        List<String> articleskeys = mAdapter.getArticlesKeys();
        List<String> newKeys = new ArrayList<>();
        List<ArticleSale> articlesForSale = new ArrayList<>();
        if (sales.size() == articleskeys.size()) {
            for (int i = 0; i < sales.size(); i++) {
                if (sales.get(i).getCantidad() > 0) {
                    articlesForSale.add(sales.get(i));
                    newKeys.add(articleskeys.get(i));
                }
            }
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.cleanup();
    }
}
