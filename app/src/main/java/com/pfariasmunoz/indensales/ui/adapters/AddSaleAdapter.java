package com.pfariasmunoz.indensales.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pfariasmunoz.indensales.R;

/**
 * Created by Pablo Farias on 02-05-17.
 */

public class AddSaleAdapter extends RecyclerView.Adapter<AddSaleAdapter.ArticleViewHolder> {
    

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView mClientNameTextView;
        TextView mClientRutTextView;
        TextView mClientAddressTextView;
        TextView mArticlesAmountTextView;
        TextView mTotalPriceTextView;
        ToggleButton mCreateSaleToggleButton;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            mClientNameTextView = (TextView) itemView.findViewById(R.id.tv_client_name);
            mClientRutTextView = (TextView) itemView.findViewById(R.id.tv_client_rut);
            mClientAddressTextView = (TextView) itemView.findViewById(R.id.tv_client_address);
            mArticlesAmountTextView = (TextView) itemView.findViewById(R.id.tv_article_amount);
            mTotalPriceTextView = (TextView) itemView.findViewById(R.id.tv_sale_total_price);
            mCreateSaleToggleButton = (ToggleButton) itemView.findViewById(R.id.tb_togle_crear_venta);
        }
    }
}
