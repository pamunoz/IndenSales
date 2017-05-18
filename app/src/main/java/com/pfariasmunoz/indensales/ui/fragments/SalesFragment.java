package com.pfariasmunoz.indensales.ui.fragments;

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
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.SaleReport;
import com.pfariasmunoz.indensales.ui.viewholders.SalesReportViewHolder;

import butterknife.BindView;

public class SalesFragment extends Fragment {

    public static final String TAG = SalesFragment.class.getSimpleName();

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    Query mSalesQuery;

    FirebaseRecyclerAdapter<SaleReport, SalesReportViewHolder> mRecyclerAdapter;


    public SalesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        return rootView;
    }

    private void setUpAdapter() {
        mRecyclerAdapter = new FirebaseRecyclerAdapter<SaleReport, SalesReportViewHolder>(
                SaleReport.class,
                R.layout.item_sale,
                SalesReportViewHolder.class,
                mSalesQuery
        ) {
            @Override
            protected void populateViewHolder(SalesReportViewHolder viewHolder, SaleReport model, int position) {
                viewHolder.bind(model);
            }
        };
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String userId = FirebaseDb.getUserId();

        mSalesQuery = FirebaseDb.sSaleReportRef.child(userId).limitToLast(30);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_content);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
        setUpAdapter();

    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerAdapter.cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.cleanup();
    }
}
