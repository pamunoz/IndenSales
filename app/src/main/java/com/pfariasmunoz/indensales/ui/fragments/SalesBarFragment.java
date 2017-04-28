package com.pfariasmunoz.indensales.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.utils.Constants;

public class SalesBarFragment extends Fragment {
    TextView mClientNameTextView;
    TextView mClinetRutTextView;


    public SalesBarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales_bar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClientNameTextView = (TextView) view.findViewById(R.id.tv_client_name);
        mClinetRutTextView = (TextView) view.findViewById(R.id.tv_client_rut);
        final String clientId = getActivity().getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        FirebaseDb.sClientsRef.child(clientId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Client client = dataSnapshot.getValue(Client.class);
                String clientName = client.getNombre();
                String clientRut = client.getRut();
                mClientNameTextView.setText(clientName);
                mClinetRutTextView.setText(clientRut);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
