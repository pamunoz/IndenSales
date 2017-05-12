package com.pfariasmunoz.indensales.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.ui.activities.CreateSaleActivity;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.ui.viewholders.AddressViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientAddressesFragment extends Fragment {

    RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<Address, AddressViewHolder> mAdapter;
    private DividerItemDecoration mDividerItemDecoration;
    private MainActivity mActivity;


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
        mActivity = ((MainActivity)getActivity());
        mActivity.setTitle(mActivity.getResources().getString(R.string.client_addresses_fragment_title));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_addresses);
        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        setUpAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }



    public void setUpAdapter() {
        Bundle args = getArguments();
        final String clientId = args.getString(Constants.CLIENT_ID_KEY);
        if (mAdapter == null) {
            mAdapter = new FirebaseRecyclerAdapter<Address, AddressViewHolder>(
                    Address.class,
                    R.layout.item_address,
                    AddressViewHolder.class,
                    FirebaseDb.sClientAdressRef.child(clientId)
            ) {
                @Override
                protected void populateViewHolder(AddressViewHolder viewHolder, Address model, final int position) {
                    viewHolder.setTextsOnViews(model);
                    viewHolder.getRootView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), CreateSaleActivity.class);
                            intent.putExtra(Constants.CLIENT_ID_KEY, clientId);
                            intent.putExtra(Constants.ADDRESS_ID_KEY, getRef(position).getKey());
                            startActivity(intent);
                        }
                    });
                }

            };
        } else {
            mAdapter.cleanup();
        }
    }
}
