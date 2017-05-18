package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.SaleReport;
import com.pfariasmunoz.indensales.ui.viewholders.SalesReportViewHolder;


import java.util.ArrayList;
import java.util.List;

public class SalesAdapter extends RecyclerView.Adapter<SalesReportViewHolder> {

    public static final String TAG = SalesAdapter.class.getSimpleName();

    private Context mContext;
    private Query mSaleReportsQuery;



    private List<SaleReport> mSaleReportList = new ArrayList<>();

    private ChildEventListener mChildEventListener;
    private ValueEventListener mValueEventListener;

    public SalesAdapter(Context context, Query query) {
        mContext = context;
        mSaleReportsQuery = query;
        initData();
    }

    private void initData() {
        mSaleReportList.clear();
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SaleReport saleReport = dataSnapshot.getValue(SaleReport.class);
                    mSaleReportList.add(0, saleReport);
                    notifyItemInserted(mSaleReportList.size() -1);
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
            mSaleReportsQuery.addChildEventListener(mChildEventListener);
        }
        notifyDataSetChanged();
//        mValueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    SaleReport saleReport = snapshot.getValue(SaleReport.class);
//                    mSaleReportList.add(0, saleReport);
//                    notifyItemInserted(mSaleReportList.size() -1);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mSaleReportsQuery.addValueEventListener(mValueEventListener);

    }


    @Override
    public SalesReportViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_sale;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        SalesReportViewHolder viewHolder = new SalesReportViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SalesReportViewHolder holder, int position) {
        SaleReport saleReport = mSaleReportList.get(position);
        holder.bind(saleReport);

    }

    @Override
    public int getItemCount() {
        return (mSaleReportList != null ? mSaleReportList.size() : 0);
    }

    public void cleanup() {
        if (mValueEventListener != null) {
            mSaleReportsQuery.removeEventListener(mValueEventListener);
        }

        if (mChildEventListener != null) {
            mSaleReportsQuery.removeEventListener(mChildEventListener);
        }
    }
}
