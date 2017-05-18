package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.activities.ClientAddressesActivity;
import com.pfariasmunoz.indensales.ui.activities.CreateSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<ClientViewHolder>{

    public static final String TAG = ClientsAdapter.class.getSimpleName();

    private Query mQuery;
    private Query mAddressQuery;

    private ChildEventListener mClientEventListener;
    private List<String> mClientKeys = new ArrayList<>();
    private List<Client> mClientList = new ArrayList<>();
    private List<Long> mAddressNumList = new ArrayList<>();
    private Context mContext;

    public ClientsAdapter(Context context, Query query) {
        mContext = context;
        mQuery = query;
        mAddressQuery = FirebaseDb.sClientAdressRef;
        initData();
    }

    public void initData() {
        mClientKeys.clear();
        mClientList.clear();
        if (mClientEventListener == null) {
            mClientEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Client client = dataSnapshot.getValue(Client.class);
                    String key = dataSnapshot.getKey();
                    Log.i(TAG, "CLIENT: " + client.nombre + " ADDED");
                    mClientKeys.add(key);
                    mClientList.add(client);

                    notifyItemInserted(mClientList.size() - 1);
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
            mQuery.addChildEventListener(mClientEventListener);
        }
        notifyDataSetChanged();
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
    public void onBindViewHolder(ClientViewHolder holder, final int position) {
        Client client = mClientList.get(position);
        holder.bind(client);
        holder.getAddSaleButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startAddressesActivity(v, mClientKeys.get(position));

            }
        });
    }


    public void startAddressesActivity(View view, String clientKey) {
        Intent intent = new Intent(view.getContext(), ClientAddressesActivity.class);
        intent.putExtra(Constants.CLIENT_ID_KEY, clientKey);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return (mClientList != null ? mClientList.size() : 0);
    }

    public void cleanup() {

        if (mClientEventListener != null) {
            mAddressQuery.removeEventListener(mClientEventListener);
        }
    }
}
