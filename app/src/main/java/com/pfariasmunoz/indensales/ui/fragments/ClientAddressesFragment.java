package com.pfariasmunoz.indensales.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Adress;
import com.pfariasmunoz.indensales.ui.viewholders.AddressViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientAddressesFragment extends Fragment {

    RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Adress, AddressViewHolder> mAdapter;


    public ClientAddressesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_addresses, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_addresses);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setUpAdapter() {
        if (mAdapter == null) {
            mAdapter = new FirebaseRecyclerAdapter<Adress, AddressViewHolder>(
                    Adress.class,
                    R.layout.item_address,
                    AddressViewHolder.class,
                    FirebaseDb.sClientAdressRef
            ) {
                @Override
                protected void populateViewHolder(AddressViewHolder viewHolder, Adress model, int position) {
                    Bundle args = getArguments();
                    String clientId = args.getString(Constants.CLIENT_ID_KEY);
                }
            };
        } else {
            mAdapter.cleanup();
        }
    }
}
