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

    public SalesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = FirebaseDb.getUserId();
        mSalesQuery = FirebaseDb.sSaleReportRef.orderByChild("idvendedor").equalTo(userId);
        Log.i(TAG, "REF: " + mSalesQuery.getRef().toString());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_content);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);

        mAdapter = new SalesAdapter(getActivity(), mSalesQuery);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.cleanup();
    }
}
