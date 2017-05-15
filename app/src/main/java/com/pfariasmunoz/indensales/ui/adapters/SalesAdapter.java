package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.ui.viewholders.SalesViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesViewHolder> {

    private Context mContext;
    private Query mSaleQuery;
    private Query mClientQuery;
    private Query mAddressQuery;


    private List<String> mAddressIdList = new ArrayList<>();
    private List<String> mClientIdList = new ArrayList<>();
    private List<Address> mAddressList = new ArrayList<>();
    private List<Sale> mSaleList = new ArrayList<>();
    private List<Client> mClientList = new ArrayList<>();

    private ValueEventListener mSalesListener;
    private ValueEventListener mClientListener;
    private ValueEventListener mAddressListener;

    public SalesAdapter(Context context, Query query) {
        mContext = context;
        mSaleQuery = query;
        mClientQuery = FirebaseDb.sClientsRef;
        mAddressQuery = FirebaseDb.sClientAdressRef;
        setupData();
    }

    private void setupData() {
        mSalesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sale sale = snapshot.getValue(Sale.class);
                    String idClient = sale.getIdcliente();
                    String idAddress = sale.getIddireccion();
                    mClientIdList.add(idClient);
                    mAddressIdList.add(idAddress);
                    mSaleList.add(sale);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mSaleQuery.addValueEventListener(mSalesListener);
        if (mClientIdList.size() > 0) {
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

        if (mAddressIdList.size() > 0) {
            mAddressListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (String clientId : mClientIdList) {
                            for (String addressId : mAddressIdList) {
                                Address address = snapshot.child(clientId).child(addressId).getValue(Address.class);
                                mAddressList.add(address);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mAddressQuery.addValueEventListener(mAddressListener);
        }
    }

    @Override
    public SalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_article_sale;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SalesViewHolder viewHolder = new SalesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SalesViewHolder holder, int position) {
        if (mAddressList.size() == mClientList.size() && mClientList.size() == mSaleList.size()) {
            Address address = mAddressList.get(position);
            Client client = mClientList.get(position);
            Sale sale = mSaleList.get(position);
            holder.bind(sale, address, client);
        }
    }

    @Override
    public int getItemCount() {
        return (mSaleList != null ? mSaleList.size() : 0);
    }

    public void cleanup() {
        if (mSalesListener != null) {
            mSaleQuery.removeEventListener(mSalesListener);
        }
        if (mAddressListener != null) {
            mAddressQuery.removeEventListener(mAddressListener);
        }
        if (mClientListener != null) {
            mClientQuery.removeEventListener(mClientListener);
        }
    }
}
