package com.pfariasmunoz.indensales.ui.viewholders;

import android.provider.UserDictionary;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class ClientViewHolder extends RecyclerView.ViewHolder {
    private TextView mNameTextView;
    private TextView mRutTextView;
    private TextView mAdressTextView;
    private TextView mDiscountTextView;

    public ClientViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) itemView.findViewById(R.id.tv_client_name);
        mRutTextView = (TextView) itemView.findViewById(R.id.tv_client_rut);
        //mAdressTextView = (TextView) itemView.findViewById(R.id.tv_client_adress);
        //mDiscountTextView = (TextView) itemView.findViewById(R.id.tv_client_discount);
    }

    public void setAdresText(String adress) {
        //mAdressTextView.setText(adress);
    }

    public void setTextOnViews(Client client) {
        //String clientName = TextHelper.capitalizeFirestLetter(client.getNombre());
        mNameTextView.setText(client.getNombre().trim());
        mRutTextView.setText(client.getRut());
        //mDiscountTextView.setText(client.getDescuento());
    }
}
