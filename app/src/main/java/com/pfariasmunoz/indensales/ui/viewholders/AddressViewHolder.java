package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Address;

/**
 * Created by Pablo Farias on 28-04-17.
 */

public class AddressViewHolder extends RecyclerView.ViewHolder {

    TextView mClientAddressTextView;
    TextView mClinetCityTextView;
    TextView mClientCommuneTextView;
    TextView mClientPhoneTextView;
    TextView mClientZoneTextView;

    public AddressViewHolder(View itemView) {
        super(itemView);
        mClientAddressTextView = (TextView) itemView.findViewById(R.id.tv_client_address);
        mClinetCityTextView = (TextView) itemView.findViewById(R.id.tv_client_city);
        mClientCommuneTextView = (TextView) itemView.findViewById(R.id.tv_client_commune);
        mClientPhoneTextView = (TextView) itemView.findViewById(R.id.tv_client_phone);
        mClientZoneTextView = (TextView) itemView.findViewById(R.id.tv_client_zone);
    }

    public void setTextsOnViews(Address address) {
        mClientAddressTextView.setText(address.getDireccion());
        mClinetCityTextView.setText(address.getCiudad());
        mClientCommuneTextView.setText(address.getComuna());
        mClientPhoneTextView.setText(address.getTelefono());
        mClientZoneTextView.setText(address.getZona());
    }
}
