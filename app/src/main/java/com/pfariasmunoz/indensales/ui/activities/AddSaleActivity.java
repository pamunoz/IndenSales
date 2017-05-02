package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.adapters.AddSaleAdapter;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {
    public static final String TAG = AddSaleActivity.class.getSimpleName();

    private Map<String, Integer> mArticlesMap = new HashMap<>();
    private ArrayList<ArticleSale> mArticleSales = new ArrayList<>();
    private String mClientId;
    private FirebaseUser mUser;
    private String mUserId;
    private String mClientAddressId;

    private RecyclerView mArticlesRecyclerView;
    private AddSaleAdapter mAddSaleAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView mClientNameTextView;
    private TextView mClientRutTextView;
    private TextView mClientAddressTextView;
    private TextView mArticleAmountTextView;
    private TextView mTotalSalesPriceTextView;

    private List<Article> mArticles = new ArrayList<>();
    private String mUsername;
    private ChildEventListener mChildArticlesEventListener;
    private ValueEventListener mArticleValueEventListener;
    private DatabaseReference mArticlesReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);


        mArticlesReference = FirebaseDb.sArticlesRef;

        // initialize refererences to views
        mArticlesRecyclerView = (RecyclerView) findViewById(R.id.rv_articles_2);
        mClientNameTextView = (TextView) findViewById(R.id.tv_client_name);
        mClientRutTextView = (TextView) findViewById(R.id.tv_client_rut);
        mClientAddressTextView = (TextView) findViewById(R.id.tv_client_address);
        mArticleAmountTextView = (TextView) findViewById(R.id.tv_article_amount);
        mTotalSalesPriceTextView = (TextView) findViewById(R.id.tv_sale_total_price);

        // initialize articles listview and its adapter

        mAddSaleAdapter = new AddSaleAdapter(mArticles);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mArticlesRecyclerView.setLayoutManager(mLayoutManager);
        mArticlesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mArticlesRecyclerView.setAdapter(mAddSaleAdapter);


        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser.getUid();
        mClientAddressId = getIntent().getStringExtra(Constants.ADDRESS_ID_KEY);

        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mArticleValueEventListener == null) {
            mArticleValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Article article = dataSnapshot.getValue(Article.class);
                    Log.i(TAG, " ||||||||||||||Article descriptions: " + article.getDescripcion());
                    mArticles.add(article);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            FirebaseDb.sArticlesRef.addValueEventListener(mArticleValueEventListener);

        }
    }

    private void detachDatabaseReadListner() {
        if (mArticleValueEventListener != null) {
            mArticlesReference.removeEventListener(mArticleValueEventListener);
            mArticleValueEventListener = null;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListner();
    }

//    public void addArticle(String articleKey, ArticleViewHolder1 viewHolder) {
//        if (!mArticlesMap.isEmpty()) {
//            if (mArticlesMap.containsKey(articleKey)) {
//                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) + 1);
//            } else {
//                mArticlesMap.put(articleKey, 1);
//            }
//        } else {
//            mArticlesMap.put(articleKey, 1);
//        }
//        viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
//        viewHolder.setAmount(mArticlesMap.get(articleKey));
//    }
//
//    public void subtractArticle(String articleKey, ArticleViewHolder1 viewHolder) {
//        if (mArticlesMap.size() == 0) {
//            return;
//        } else if (mArticlesMap.containsKey(articleKey)) {
//            // if the amount of this article is 0, remove it from the map
//            if (mArticlesMap.get(articleKey) <= 1) {
//                mArticlesMap.remove(articleKey);
//                viewHolder.setTotalPrice(0);
//                viewHolder.setAmount(0);
//            } else {
//                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) - 1);
//                viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
//                viewHolder.setAmount(mArticlesMap.get(articleKey));
//            }
//        } else {
//            return;
//        }
//    }
//
//    public Map<String, Integer> getArticlesMap() {
//        return mArticlesMap;
//    }
//
//    public String getClientId() {
//        return mClientId;
//    }
//
//    public FirebaseUser getUser() {
//        return mUser;
//    }
//
//    public String getUserId() {
//        return mUserId;
//    }
//
//    public Sale createSale() {
//        boolean aprob = false;
//        long currentTime = System.currentTimeMillis();
//        String time = String.valueOf(currentTime);
//        String clientId = mClientId;
//        String clientAddressId = mClientAddressId;
//        String userId = mUserId;
//        Long totalPrice = getTotalPrice();
//        return new Sale(aprob, time, clientId, clientAddressId, userId, totalPrice);
//    }
//
//    public void addArticleSale(Article article, String articleId) {
//        Iterator it = mArticlesMap.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            int amount = mArticlesMap.get(articleId);
//            long price = Long.valueOf(article.getPrecio());
//            Long totalPrice = amount * price;
//            ArticleSale articleSale = new ArticleSale(amount, totalPrice);
//            mArticleSales.add(articleSale);
//            it.remove(); // avoids a ConcurrentModificationException
//        }
//    }
//
//    private Long getTotalPrice() {
//        Long result = 0L;
//        for (ArticleSale articleSale : mArticleSales) {
//            result += articleSale.getTotal();
//        }
//        return result;
//    }

}
