package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientViewHolder>{

    private Query mQuery;
    private ChildEventListener mChildEventListener;
    private List<Client> mClientList = new ArrayList<>();
    private List<String> mClientKeysList;
    private Context mContext;

    public ClientsAdapter(Context context, Query query) {
        mQuery = query;
        mContext = context;
        initData();
    }

    private void initData() {
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Client client = dataSnapshot.getValue(Client.class);
                mClientList.add(client);
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
        mQuery.addChildEventListener(mChildEventListener);
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return (mClientList != null ? mClientList.size() : 0);
    }

    public void cleanup() {
        if (mChildEventListener != null) {
            mQuery.removeEventListener(mChildEventListener);
        }
    }
}
