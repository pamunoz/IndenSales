package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Address;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 28-04-17.
 */

public class AddressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_client_address)
    TextView mClientAddressTextView;
    @BindView(R.id.tv_client_city)
    TextView mClinetCityTextView;
    @BindView(R.id.tv_client_commune)
    TextView mClientCommuneTextView;
    @BindView(R.id.tv_client_phone)
    TextView mClientPhoneTextView;
    @BindView(R.id.tv_client_zone)
    TextView mClientZoneTextView;
    private View mRootView;

    public AddressViewHolder(View itemView) {
        super(itemView);
        mRootView = itemView;
        ButterKnife.bind(this, mRootView);
    }

    public void bind(Address address) {
        mClientAddressTextView.setText(address.direccion);
        mClinetCityTextView.setText(address.ciudad);
        mClientCommuneTextView.setText(address.comuna);
        mClientPhoneTextView.setText(address.telefono);
        mClientZoneTextView.setText(address.zona);
    }

    public View getRootView() {
        return mRootView;
    }
}
