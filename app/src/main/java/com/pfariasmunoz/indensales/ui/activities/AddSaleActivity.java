package com.pfariasmunoz.indensales.ui.activities;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticlesViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {
    public static final String TAG = AddSaleActivity.class.getSimpleName();

    private HashMap<String, ArticleSale> mArticlesSalesMap = new HashMap<>();
    private Long mTotalPrice;
    private int mTotalAmount;
    private String mDescriptionQuery;

    private String mClientId;
    private FirebaseUser mUser;
    private String mUserId;
    private String mClientAddressId;

    private RecyclerView mArticlesListView;

    private TextView mClientNameTextView;
    private TextView mClientRutTextView;
    private TextView mClientAddressTextView;
    private TextView mArticleAmountTextView;
    private TextView mTotalSalesPriceTextView;
    private Button mCreateSaleButton;


    // Firebase instance variables
    private DatabaseReference mClientDatabaseReference;
    private ValueEventListener mClientValueEventListener;
    private DatabaseReference mClientAddressDatabaseReference;
    private ValueEventListener mClientAddressValueEventListener;

    private FirebaseRecyclerAdapter<Article, ArticlesViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_sale);

        setTitle(getResources().getString(R.string.sales_activity_title));

        mTotalPrice = 0L;
        mTotalAmount = 0;
        mDescriptionQuery = "";


        // Initialize Firebase components
        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mClientAddressId = "unknown";

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser != null ? mUser.getUid() : "Unknown User";

        //mArticlesDatabaseReference = mFirebaseDatabase.getReference().child("articulos");
        mClientDatabaseReference = FirebaseDb.sClientsRef.child(mClientId);
        mClientAddressDatabaseReference = FirebaseDb.sClientAdressRef.child(mClientId);

        initializeViews();
        setupAdapter();
        mArticlesListView.setAdapter(mAdapter);
        attachDatabaseReadListener();
    }

    private void setupAdapter() {
        mAdapter = getAdapter(FirebaseDb.getArticlesDescriptionQuery(mDescriptionQuery).limitToFirst(50));
    }

    private FirebaseRecyclerAdapter<Article, ArticlesViewHolder> getAdapter(Query query) {
        return new FirebaseRecyclerAdapter<Article, ArticlesViewHolder>(
                Article.class,
                R.layout.item_article,
                ArticlesViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final ArticlesViewHolder viewHolder, final Article model, final int position) {

                if (mArticlesSalesMap.isEmpty()) {
                    viewHolder.setAmountAndPriceToZero();
                }


                Log.i(TAG, "THE ARTICLE KEY IS: " + getRef(position).getKey());

                if (mArticlesSalesMap.containsKey(getRef(position).getKey())) {
                    String currentKey = getRef(position).getKey();
                    String currentArticlesAmount =
                            String.valueOf(mArticlesSalesMap.get(currentKey).getCantidad());
                    String currentArticlesTotalPrice =
                            String.valueOf(mArticlesSalesMap.get(currentKey).getTotal());
                    viewHolder.setArticleAmount(currentArticlesAmount);
                    viewHolder.setArticleTotalPrice(currentArticlesTotalPrice);
                }

                viewHolder.setDescriptionAndPrice(model);

                viewHolder.getAddArticleToSaleButton()
                        .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        addToArticlesSaleAndUpdateViews(
                                articleKey, model,
                                viewHolder.getArticleAmountTextView(),
                                viewHolder.getArticleTotalPriceTextView());
                    }
                });

                viewHolder.getSubtractArticleFromSalesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String articleKey = getRef(position).getKey();
                        subtractToArticlesSalesAndUpdateViews(
                                articleKey, model,
                                viewHolder.getArticleAmountTextView(),
                                viewHolder.getArticleTotalPriceTextView());

                    }
                });

            }
        };

    }

    private void initializeViews() {
        // set the other views
        mClientNameTextView = (TextView) findViewById(R.id.tv_client_name);
        mClientRutTextView = (TextView) findViewById(R.id.tv_client_rut);
        mClientAddressTextView = (TextView) findViewById(R.id.tv_client_address);
        mArticleAmountTextView = (TextView) findViewById(R.id.tv_articles_amount);
        mArticleAmountTextView.setText(String.valueOf(mTotalAmount));
        mTotalSalesPriceTextView = (TextView) findViewById(R.id.tv_sale_total_price);
        mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
        mCreateSaleButton = (Button) findViewById(R.id.bt_crear_venta);
        mArticlesListView = (RecyclerView) findViewById(R.id.lv_articles_list);
        mArticlesListView.setLayoutManager(new LinearLayoutManager(this));
        mCreateSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sale sale = new Sale(
                        false, String.valueOf(System.currentTimeMillis()),
                        mClientId,
                        mClientAddressId,
                        mUserId,
                        mTotalPrice);
                DatabaseReference ref = FirebaseDb.sSalesRef.push();

                ref.setValue(sale);

                String uid = ref.getKey();

                saveAllSalesArticles(uid);
            }
        });
    }

    private void attachDatabaseReadListener() {

        // Add listener to the clients
        if (mClientValueEventListener == null) {
            mClientValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Client client = dataSnapshot.getValue(Client.class);
                    mClientNameTextView.setText(client.getNombre());
                    mClientRutTextView.setText(client.getRut());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mClientDatabaseReference.addValueEventListener(mClientValueEventListener);
        }
        // Add listener to the client adress
        if (mClientAddressValueEventListener == null) {
            mClientAddressValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot data : iterable) {
                        mClientAddressId = data.getKey();
                        String direccion = data.child("direccion").getValue(String.class);
                        String comuna = data.child("comuna").getValue(String.class);
                        String ciudad = data.child("ciudad").getValue(String.class);
                        String longAddress = direccion + "\n" + comuna + ", " + ciudad + ".";
                        mClientAddressTextView.setText(longAddress);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mClientAddressDatabaseReference.addValueEventListener(mClientAddressValueEventListener);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    private void subtractToArticlesSalesAndUpdateViews(
            String key,
            Article article,
            TextView articlesAmountTextView,
            TextView articlesTotalPriceTextView) {
        if (!mArticlesSalesMap.isEmpty() && mArticlesSalesMap.containsKey(key)) {
            if (mArticlesSalesMap.get(key).getCantidad() < 2) {
                mArticlesSalesMap.remove(key);
                articlesAmountTextView.setText("0");
                articlesTotalPriceTextView.setText("0");
                mTotalPrice -= Long.valueOf(article.getPrecio());
                mTotalAmount --;
                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                mArticleAmountTextView.setText(String.valueOf(mTotalAmount));
            } else {
                int currentArticleQuantity = mArticlesSalesMap.get(key).getCantidad();
                Long currentArticleTotalPrice = mArticlesSalesMap.get(key).getTotal();

                Long articlePrice = Long.valueOf(article.getPrecio());

                int newArticleQuantity = currentArticleQuantity - 1;
                Long newArticleTotalPrice = currentArticleTotalPrice - articlePrice;
                mArticlesSalesMap.put(key, new ArticleSale(newArticleQuantity, newArticleTotalPrice));

                mTotalPrice -= articlePrice;
                mTotalAmount --;
                // Update the views
                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                mArticleAmountTextView.setText(String.valueOf(mTotalAmount));
                articlesAmountTextView.setText(String.valueOf(newArticleQuantity));
                articlesTotalPriceTextView.setText(String.valueOf(newArticleTotalPrice));
            }
        }
    }

    private void addToArticlesSaleAndUpdateViews(
            String key,
            Article article,
            TextView articlesAmountTextView,
            TextView articlesTotalPriceTextView) {
        if (mArticlesSalesMap.isEmpty()) {
            int amount = 1;
            Long articlePrice = Long.valueOf(article.getPrecio());
            mArticlesSalesMap.put(key, new ArticleSale(amount, articlePrice));
            mTotalPrice += articlePrice;
            mTotalAmount ++;
            // Update views
            mArticleAmountTextView.setText(String.valueOf(mTotalAmount));
            mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
            articlesTotalPriceTextView.setText(article.getPrecio());
            articlesAmountTextView.setText(String.valueOf(mArticlesSalesMap.get(key).getCantidad()));
        } else {
            // if the Hashmap is NOT empty and DO contain the current article key
            if (mArticlesSalesMap.containsKey(key)) {
                int currentQuantity = mArticlesSalesMap.get(key).getCantidad() + 1;
                Long currentArticlePrice = Long.valueOf(article.getPrecio());
                Long currentTotal = currentArticlePrice * currentQuantity;
                // Replace the last value with the current one
                mArticlesSalesMap.put(key, new ArticleSale(currentQuantity, currentTotal));
                mTotalPrice += currentArticlePrice;
                mTotalAmount ++;
                // Update views
                mArticleAmountTextView.setText(String.valueOf(mTotalAmount));
                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                articlesTotalPriceTextView.setText(String.valueOf(currentTotal));
                articlesAmountTextView.setText(String.valueOf(currentQuantity));

            } else {
                // if the Hashmap is NOT empty but DO NOT contain the current article key
                int amount = 1;
                Long articlePrice = Long.valueOf(article.getPrecio());
                mArticlesSalesMap.put(key, new ArticleSale(amount, articlePrice));
                mTotalPrice += articlePrice;
                mTotalAmount ++;
                // Update views
                mArticleAmountTextView.setText(String.valueOf(mTotalAmount));
                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                articlesTotalPriceTextView.setText(article.getPrecio());
                articlesAmountTextView.setText(String.valueOf(mArticlesSalesMap.get(key).getCantidad()));

            }
        }
    }

    private void saveAllSalesArticles(String saleKey) {
        Iterator it = mArticlesSalesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String articlekey = (String) pair.getKey();
            ArticleSale articleSale = (ArticleSale) pair.getValue();
            FirebaseDb.sArticlesSalesRef.child(saleKey).child(articlekey).setValue(articleSale);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(this, getResources().getString(R.string.function_not_available), Toast.LENGTH_SHORT).show();
        } else if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getResources().getString(R.string.search_articles_hint));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    if (MathHelper.isNumeric(newText)) {
                        Query query = FirebaseDb.getArticlesCodeQuery(newText).limitToFirst(30);
                        mAdapter = getAdapter(query);
                    } else {
                        String text = newText.toUpperCase();
                        Query query = FirebaseDb.getArticlesDescriptionQuery(text);
                        mAdapter = getAdapter(query);
                    }
                    mAdapter.notifyDataSetChanged();
                    mArticlesListView.swapAdapter(mAdapter, false);
                    return false;
                }
            });
        }



        return super.onOptionsItemSelected(item);
    }
}
