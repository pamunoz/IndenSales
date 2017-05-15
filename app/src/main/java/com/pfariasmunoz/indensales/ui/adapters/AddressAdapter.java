package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.ui.activities.CreateSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.AddressViewHolder;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo Farias on 15-05-17.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressViewHolder> {

    private Query mQuery;
    private ValueEventListener mListener;
    private List<Address> mAddressList = new ArrayList<>();
    private List<String> mAddressKeyList = new ArrayList<>();
    private String mClientId;
    private Context mContext;

    public AddressAdapter(Context context, Query query, String clientId) {
        mContext = context;
        mQuery = query;
        mClientId = clientId;
        setUpData();
    }

    private void setUpData() {
        mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Address address = snapshot.getValue(Address.class);
                    String addressKey = snapshot.getKey();
                    mAddressList.add(address);
                    mAddressKeyList.add(addressKey);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mQuery.addValueEventListener(mListener);
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_address;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        AddressViewHolder viewHolder = new AddressViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddressViewHolder holder, final int position) {
        Address address = mAddressList.get(position);
        holder.bind(address);
        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressId = mAddressKeyList.get(position);
                Intent intent = new Intent(holder.getRootView().getContext(), CreateSaleActivity.class);
                intent.putExtra(Constants.CLIENT_ID_KEY, mClientId);
                if (addressId != null) {
                    intent.putExtra(Constants.ADDRESS_ID_KEY, addressId);
                }
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public void cleanup() {
        if (mListener != null) {
            mQuery.removeEventListener(mListener);
        }
    }
}
