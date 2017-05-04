package com.pfariasmunoz.indensales.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.activities.AddSaleActivity;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientsFragment extends Fragment {

    private RecyclerView mClientRecyclerView;
    private FirebaseRecyclerAdapter<Client, ClientViewHolder> mClientAdapter;
    private ProgressBar mLoadingIndicatorProgressBar;
    private MainActivity mActivity;

    // client info to start the sales
    public static final String CLIENT_ID_KEY = "client_id_key";

    public ClientsFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = ((MainActivity)getActivity());
        mActivity.setTitle("Clientes");
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        mClientRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_clients);
        mLoadingIndicatorProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        mClientRecyclerView.setHasFixedSize(false);
        mClientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mClientRecyclerView.setVisibility(View.INVISIBLE);

        mClientRecyclerView.setAdapter(mClientAdapter);
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created,
    // or re-created. Use onCreate for any standard setup that does not
    // require the activity to be fully created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClientAdapter.cleanup();
    }

    private void setupAdapter() {
        Query clientsRef = FirebaseDb.sClientsRef;
        mClientAdapter = new FirebaseRecyclerAdapter<Client, ClientViewHolder>(
                Client.class,
                R.layout.item_client,
                ClientViewHolder.class,
                clientsRef) {
            @Override
            protected void populateViewHolder(
                    final ClientViewHolder viewHolder,
                    final Client model, final int position) {
                viewHolder.getAddSaleButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String clientId = getRef(position).getKey();



                        FirebaseDb.sClientAdressRef.child(clientId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                long clientAdressNum = dataSnapshot.getChildrenCount();
                                String addressId = FirebaseDb.sClientAdressRef.child(clientId).getKey();
                                mActivity.startSalesActivity(clientAdressNum, clientId, addressId);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });

                viewHolder.setTextOnViews(model);
                mLoadingIndicatorProgressBar.setVisibility(View.GONE);
                mClientRecyclerView.setVisibility(View.VISIBLE);
            }
        };
        mClientAdapter.notifyDataSetChanged();
    }



}
