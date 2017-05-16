package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientViewHolder>{

    public static final String TAG = ClientsAdapter.class.getSimpleName();

    private Query mQuery;
    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;
    private List<Client> mClientList = new ArrayList<>();
    private Context mContext;

    public ClientsAdapter(Context context, Query query) {
        mContext = context;
        mQuery = query;
        initData();
    }

    private void initData() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Client client = snapshot.getValue(Client.class);
                    Log.i(TAG, "CLIENT: " + client.nombre + " ADDED");

                    mClientList.add(client);
                    notifyItemInserted(mClientList.size() - 1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

//        mChildEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
        notifyDataSetChanged();
        //mQuery.addChildEventListener(mChildEventListener);
        mQuery.addValueEventListener(mValueEventListener);
    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_client;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ClientViewHolder viewHolder = new ClientViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = mClientList.get(position);
        holder.bind(client);
    }

    @Override
    public int getItemCount() {
        return (mClientList != null ? mClientList.size() : 0);
    }

    public void cleanup() {
        if (mChildEventListener != null) {
            mQuery.removeEventListener(mChildEventListener);
        }
        if (mValueEventListener != null) {
            mQuery.removeEventListener(mValueEventListener);
        }
    }
}
