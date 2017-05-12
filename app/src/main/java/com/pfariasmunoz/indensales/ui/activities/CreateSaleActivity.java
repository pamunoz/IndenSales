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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.pfariasmunoz.indensales.ui.adapters.ArticleSaleAdapter;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private FirebaseUser mUser;

    private ValueEventListener mClientListener;
    private ValueEventListener mClientAddressListener;

    private Query mClientQuery;
    private Query mClientAddressQuery;

    ArticleSaleAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sale);

        ButterKnife.bind(this);


        // Initialize Firebase components
        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mClientAddressId = getIntent().getStringExtra(Constants.ADDRESS_ID_KEY);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser != null ? mUser.getUid() : "Unknown User";

        // Initialize the queries
        mClientQuery = FirebaseDb.sClientsRef.child(mClientId);
        mClientAddressQuery = FirebaseDb.sClientAdressRef.child(mClientId).child(mClientAddressId);

        mAdapter = new ArticleSaleAdapter(this, FirebaseDb.sArticlesRef.limitToFirst(30));

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        setTotals(0, 0);

        attachReadListeners();
    }

    private void attachReadListeners() {
        mClientListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Client client = dataSnapshot.getValue(Client.class);
                    String name = client.getNombre();
                    String rut = client.getRut();
                    String discount = client.getDescuento();
                    String stringName = TextHelper.capitalizeFirestLetter(name);
                    mClientNameTextView.setText(stringName);
                    mClientRutTextView.setText(rut);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mClientAddressListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Address address = dataSnapshot.getValue(Address.class);
                    String longAddress = address.getDireccion() + "\n" + address.getComuna() + " " + address.getCiudad();
                    String stringLongAddress = TextHelper.capitalizeFirestLetter(longAddress);
                    mClientAddressTextView.setText(stringLongAddress);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mClientQuery.addListenerForSingleValueEvent(mClientListener);
        mClientAddressQuery.addListenerForSingleValueEvent(mClientAddressListener);
    }

    @OnClick(R.id.bt_create_sale)
    public void createSale() {

        Map<String, ArticleSale> articlesForSale = mAdapter.getArticlesForSale();
        if (articlesForSale != null) {
            Sale sale = new Sale(
                    false, String.valueOf(System.currentTimeMillis()),
                    mClientId,
                    mClientAddressId,
                    mUserId,
                    mAdapter.getTotalPrice());
            DatabaseReference ref = FirebaseDb.sSalesRef.push();

            ref.setValue(sale);

            String saleUid = ref.getKey();

            Iterator it = articlesForSale.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                ArticleSale articleSale = (ArticleSale) pair.getValue();
                String key = (String) pair.getKey();
                FirebaseDb.sArticlesSalesRef.child(saleUid).child(key).setValue(articleSale);
                it.remove(); // avoids a ConcurrentModificationException
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.cleanup();
        detachReadListeners();
    }

    private void detachReadListeners() {
        if (mClientListener != null) {
            mClientQuery.removeEventListener(mClientListener);
        }
        if (mClientAddressListener != null) {
            mClientAddressQuery.removeEventListener(mClientAddressListener);
        }
    }

    public void setTotals(long totalPrice, int totalAmount) {
        String stringTotal = MathHelper.getLocalCurrency(String.valueOf(totalPrice));
        mSaleArticlesAmountTextView.setText(String.valueOf(totalAmount));
        mTotalPriceSaleTextView.setText(stringTotal);
    }

}
