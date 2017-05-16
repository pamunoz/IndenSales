package com.pfariasmunoz.indensales.ui.viewholders;

import android.provider.UserDictionary;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_client_name)
    TextView mNameTextView;
    @BindView(R.id.tv_client_rut)
    TextView mRutTextView;
    @BindView(R.id.bt_add_sale)
    Button mAddSaleButton;

    private View mItemView;

    public ClientViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        ButterKnife.bind(this, mItemView);
    }


    public void bind(Client client) {
        String clientName = TextHelper.capitalizeFirestLetter(client.nombre);
        mNameTextView.setText(clientName);
        mRutTextView.setText(client.rut);
    }


    @OnClick(R.id.imbt_client_location)
    public void showMap() {
        Toast.makeText(mItemView.getContext(), "funcion no disponible", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.imbt_edit_client)
    public void goToEditClientActivity() {
        Toast.makeText(mItemView.getContext(), "funcion no disponible", Toast.LENGTH_SHORT).show();
    }
}
