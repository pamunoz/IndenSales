package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.viewholders.SalesViewHolder;

import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesViewHolder> {

    private Context mContext;
    private Query mSaleQuery;
    private Query mClientQuery;
    private List<String> mClientIdList;
    private ValueEventListener mSalesListener;
    private ValueEventListener mClientListener;
    private List<Sale> mSaleList;
    private List<Client> mClientList;

    public SalesAdapter(Context context, Query query) {
        mContext = context;
        mSaleQuery = query;
        mClientQuery = FirebaseDb.sClientsRef;
        setupData();
    }

    private void setupData() {
        mSalesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sale sale = snapshot.getValue(Sale.class);
                    String idClient = sale.getIdcliente();
                    mClientIdList.add(idClient);
                    mSaleList.add(sale);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mSaleQuery.addValueEventListener(mSalesListener);
        mClientListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Client client = snapshot.getValue(Client.class);
                    for (int i = 0; i < mClientIdList.size(); i++) {
                        if (mClientIdList.get(i).equals(dataSnapshot.getKey())) {
                            mClientList.add(client);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mClientQuery.addValueEventListener(mClientListener);

    }

    @Override
    public SalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SalesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (mSaleList != null ? mSaleList.size() : 0);
    }
}
