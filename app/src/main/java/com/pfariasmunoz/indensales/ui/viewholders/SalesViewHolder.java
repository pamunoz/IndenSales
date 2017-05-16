package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SalesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_total_price_sale)
    TextView mTotalSalePriceTextView;
    @BindView(R.id.tv_client_name_sale)
    TextView mClientNameSaleTextView;
    @BindView(R.id.tv_client_rut_sale)
    TextView mClientRutSaleTextView;
    @BindView(R.id.tv_client_address_sale)
    TextView mClientAddressSaleTextView;
    @BindView(R.id.tv_date)
    TextView mDateSaleTextView;
    @BindView(R.id.tv_time)
    TextView mTimeSaleTextView;


    public SalesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Sale sale, Address address, Client client) {
        String total = String.valueOf(sale.getTotal());
        String stringTotal = MathHelper.getLocalCurrency(total);
        mTotalSalePriceTextView.setText(stringTotal);
        mClientNameSaleTextView.setText(client.getNombre());
        mClientRutSaleTextView.setText(client.getRut());
        String direction = address != null ? address.getDireccion() : "unknown";
        mClientAddressSaleTextView.setText(direction);
        String date = TextHelper.formatDate(sale.getFecha());
        String time = TextHelper.formatTime(sale.getFecha());
        mDateSaleTextView.setText(date);
        mTimeSaleTextView.setText(time);
    }
}
