package com.pfariasmunoz.indensales.ui.viewholders;

import android.provider.UserDictionary;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientViewHolder extends RecyclerView.ViewHolder {
    private TextView mNameTextView;
    private TextView mRutTextView;
    private Button mAddSaleButton;

    public ClientViewHolder(View itemView) {
        super(itemView);
        mNameTextView = (TextView) itemView.findViewById(R.id.tv_client_name);
        mRutTextView = (TextView) itemView.findViewById(R.id.tv_client_rut);
        mAddSaleButton = (Button) itemView.findViewById(R.id.bt_add_sale);
    }


    public void setTextOnViews(Client client) {
        String clientName = TextHelper.capitalizeFirestLetter(client.getNombre());
        mNameTextView.setText(clientName);
        mRutTextView.setText(client.getRut());
        //mDiscountTextView.setText(client.getDescuento());
    }

    public Button getAddSaleButton() {
        return mAddSaleButton;
    }
}
