package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.adapters.ArticlesAdapter;
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

    private ListView mArticlesListView;
    private ArticlesAdapter mArticlesAdapter;

    private TextView mClientNameTextView;
    private TextView mClientRutTextView;
    private TextView mClientAddressTextView;
    private TextView mArticleAmountTextView;
    private TextView mTotalSalesPriceTextView;

    private List<Article> mArticles = new ArrayList<>();
    private String mUsername;
    private ValueEventListener mArticleValueEventListener;
    private DatabaseReference mArticlesReference;

    // Firebase instance variables
    private DatabaseReference mArticlesDatabaseReference;
    private ChildEventListener mChildArticlesEventListener;
    private DatabaseReference mClientDatabaseReference;
    private ValueEventListener mClientValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        // Initialize Firebase components
        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mClientAddressId = getIntent().getStringExtra(Constants.ADDRESS_ID_KEY);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser.getUid();

        //mArticlesDatabaseReference = mFirebaseDatabase.getReference().child("articulos");
        mArticlesDatabaseReference = FirebaseDb.sArticlesRef;
        mClientDatabaseReference = FirebaseDb.sClientsRef.child(mClientId);


        // initialize refererences to views
        //mArticlesRecyclerView = (RecyclerView) findViewById(R.id.rv_articles);
        mArticlesListView = (ListView) findViewById(R.id.lv_articles_list);

        List<Article> articleList = new ArrayList<>();
        mArticlesAdapter = new ArticlesAdapter(this, R.layout.item_article, articleList);
        mArticlesListView.setAdapter(mArticlesAdapter);

        // set the other views
        mClientNameTextView = (TextView) findViewById(R.id.tv_client_name);
        mClientRutTextView = (TextView) findViewById(R.id.tv_client_rut);
        mClientAddressTextView = (TextView) findViewById(R.id.tv_client_address);
        mArticleAmountTextView = (TextView) findViewById(R.id.tv_article_amount);
        mTotalSalesPriceTextView = (TextView) findViewById(R.id.tv_sale_total_price);






        Log.i(TAG, "CLIENT ID: " + mClientId);

        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildArticlesEventListener == null) {
            mChildArticlesEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Article article = dataSnapshot.getValue(Article.class);
                    mArticlesAdapter.add(article);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mArticlesDatabaseReference.addChildEventListener(mChildArticlesEventListener);
        }
        if (mClientValueEventListener == null) {
            mClientValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Client client = dataSnapshot.getValue(Client.class);
                    mClientNameTextView.setText(client.getNombre());
                    mClientRutTextView.setText(client.getRut());
                    Log.i(TAG, "NOMBRE CLIENTE: " + client.getNombre() + " RUT CLIENTE" + client.getRut());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mClientDatabaseReference.addValueEventListener(mClientValueEventListener);
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
