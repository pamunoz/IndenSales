package com.pfariasmunoz.indensales.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.ui.adapters.SalesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 15-05-17.
 */

public class SalesFragment extends Fragment {

    public static final String TAG = SalesFragment.class.getSimpleName();

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    SalesAdapter mAdapter;
    Query mSalesQuery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_content);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
        String userId = FirebaseDb.getUserId();
        Log.i(TAG, "MY ID: " + userId);
        mSalesQuery = FirebaseDb.sSalesRef.orderByChild("idvendedor").equalTo(userId);
        Log.i(TAG, "QUERY TEXT: " + mSalesQuery.getRef().toString());
        mAdapter = new SalesAdapter(getActivity(), mSalesQuery);
        mRecyclerView.setAdapter(mAdapter);
        Log.i(TAG, "ON CREATE VIEW");
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.cleanup();
    }
}
