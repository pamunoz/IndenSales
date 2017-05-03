package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.adapters.ArticlesAdapter;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class AddSaleActivity extends AppCompatActivity {
    public static final String TAG = AddSaleActivity.class.getSimpleName();

    private HashMap<String, Integer> mArticlesMap = new HashMap<>();
    private ArrayList<ArticleSale> mArticleSales = new ArrayList<>();
    private HashMap<String, ArticleSale> mArticlesSalesMap = new HashMap<>();
    private Long mTotalPrice;

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
    private Button mCreateSaleButton;

    private ValueEventListener mArticleValueEventListener;
    private DatabaseReference mArticlesReference;

    // Firebase instance variables
    private DatabaseReference mArticlesDatabaseReference;
    private ChildEventListener mChildArticlesEventListener;
    private DatabaseReference mClientDatabaseReference;
    private ValueEventListener mClientValueEventListener;
    private DatabaseReference mClientAddressDatabaseReference;
    private ValueEventListener mClientAddressValueEventListener;

    private FirebaseListAdapter<Article> mFirebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        mTotalPrice = 0L;

        // Initialize Firebase components
        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mClientAddressId = getIntent().getStringExtra(Constants.ADDRESS_ID_KEY);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser != null ? mUser.getUid() : "Unknown User";

        //mArticlesDatabaseReference = mFirebaseDatabase.getReference().child("articulos");
        mArticlesDatabaseReference = FirebaseDb.sArticlesRef;
        mClientDatabaseReference = FirebaseDb.sClientsRef.child(mClientId);
        mClientAddressDatabaseReference = FirebaseDb.sClientAdressRef.child(mClientId);

        initializeViews();
        setupAdapter();

        attachDatabaseReadListener();
    }

    private void setupAdapter() {
        mFirebaseListAdapter = new FirebaseListAdapter<Article>(
                this,
                Article.class,
                R.layout.item_article,
                FirebaseDb.sArticlesRef
        ) {
            @Override
            protected void populateView(View view, final Article model, final int position) {

                final TextView articleDescriptionTextView = (TextView) view.findViewById(R.id.tv_article_description);
                final TextView articlePriceTextView = (TextView) view.findViewById(R.id.tv_article_price);
                final TextView articleAmountTextView = (TextView) view.findViewById(R.id.tv_article_amount);
                final TextView articleTotalPriceTextView = (TextView) view.findViewById(R.id.tv_article_total_price);

                final ImageButton addArticleToSaleButton = (ImageButton) view.findViewById(R.id.imb_up_arrow);
                final ImageButton subtractArticleOfSaleButton = (ImageButton) view.findViewById(R.id.imb_down_arrow);

                if (mArticlesMap.isEmpty()) {
                    articleAmountTextView.setText("0");
                    articleTotalPriceTextView.setText("0");
                } else {
                    if (mArticlesMap.containsKey(getRef(position).getKey())) {
                        Long totalPrice = mArticlesMap.get(getRef(position).getKey()) * Long.valueOf(model.getPrecio());
                        String stringTotalPrice = String.valueOf(totalPrice);
                        articleTotalPriceTextView.setText(stringTotalPrice);
                        articleAmountTextView.setText(String.valueOf(mArticlesMap.get(getRef(position).getKey())));
                    } else {
                        articleAmountTextView.setText("0");
                        articleTotalPriceTextView.setText("0");
                    }
                }

                articleDescriptionTextView.setText(model.getDescripcion());
                articlePriceTextView.setText(model.getPrecio());

                addArticleToSaleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        if (!mArticlesMap.isEmpty()) {
                            if (mArticlesMap.containsKey(articleKey)) {
                                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) + 1);

                                Long totaArticlelPrice = mArticlesMap.get(articleKey) * Long.valueOf(model.getPrecio());
                                mTotalPrice += totaArticlelPrice;
                                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                                String stringTotalPrice = String.valueOf(totaArticlelPrice);
                                Toast.makeText(mActivity, "TOTAL: " + stringTotalPrice, Toast.LENGTH_SHORT).show();

                                articleTotalPriceTextView.setText(stringTotalPrice);
                                articleAmountTextView.setText(String.valueOf(mArticlesMap.get(articleKey)));
                            } else {
                                mArticlesMap.put(articleKey, 1);

                                Long totalPrice = mArticlesMap.get(articleKey) * Long.valueOf(model.getPrecio());
                                String stringTotalPrice = String.valueOf(totalPrice);

                                articleTotalPriceTextView.setText(stringTotalPrice);
                                articleAmountTextView.setText(String.valueOf(mArticlesMap.get(articleKey)));
                            }
                        } else {
                            mArticlesMap.put(articleKey, 1);

                            Long totaArticlelPrice = mArticlesMap.get(articleKey) * Long.valueOf(model.getPrecio());
                            mTotalPrice += totaArticlelPrice;
                            mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                            String stringTotalPrice = String.valueOf(totaArticlelPrice);
                            articleTotalPriceTextView.setText(stringTotalPrice);
                            articleAmountTextView.setText(String.valueOf(mArticlesMap.get(articleKey)));
                        }

                    }
                });
                subtractArticleOfSaleButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleKey = getRef(position).getKey();
                        if (mArticlesMap.size() == 0) {
                            return;
                        } else if (mArticlesMap.containsKey(articleKey)) {
                            // if the amount of this article is 0, remove it from the map
                            if (mArticlesMap.get(articleKey) <= 1) {
                                mArticlesMap.remove(articleKey);
                                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                                articleAmountTextView.setText(String.valueOf(0));
                                articleTotalPriceTextView.setText(String.valueOf(0));

                            } else {
                                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) - 1);

                                Long totaArticlelPrice = mArticlesMap.get(articleKey) * Long.valueOf(model.getPrecio());
                                mTotalPrice -= totaArticlelPrice;
                                mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
                                String stringTotalPrice = String.valueOf(totaArticlelPrice);

                                articleTotalPriceTextView.setText(stringTotalPrice);
                                articleAmountTextView.setText(String.valueOf(mArticlesMap.get(articleKey)));
                            }
                        } else {
                            return;
                        }

                    }
                });

            }
        };
        mArticlesListView.setAdapter(mFirebaseListAdapter);
    }

    private void initializeViews() {
        // set the other views
        mClientNameTextView = (TextView) findViewById(R.id.tv_client_name);
        mClientRutTextView = (TextView) findViewById(R.id.tv_client_rut);
        mClientAddressTextView = (TextView) findViewById(R.id.tv_client_address);
        mArticleAmountTextView = (TextView) findViewById(R.id.tv_article_amount);
        mTotalSalesPriceTextView = (TextView) findViewById(R.id.tv_sale_total_price);
        mCreateSaleButton = (Button) findViewById(R.id.bt_crear_venta);

        mArticlesListView = (ListView) findViewById(R.id.lv_articles_list);
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
                    Address address = dataSnapshot.getValue(Address.class);
                    String reference = dataSnapshot.getRef().toString();
                    mClientAddressTextView.setText(address.getDireccion());
                    String thekey = dataSnapshot.toString();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot data : iterable) {
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
            mTotalSalesPriceTextView.setText(String.valueOf(mTotalPrice));
            articlesTotalPriceTextView.setText(article.getPrecio());
            articlesAmountTextView.setText(String.valueOf(mArticlesSalesMap.get(key).getCantidad()));
        }
    }

}
