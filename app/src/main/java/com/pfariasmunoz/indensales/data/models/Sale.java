package com.pfariasmunoz.indensales.data.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo Farias on 20-04-17.
 */
@IgnoreExtraProperties
public class Sale {

    public boolean aprob;
    public long timestamp;
    public String idcliente;
    public String iddireccion;
    public String idvendedor;
    public long total;

    public Sale() {
    }

    public Sale(boolean aprob, long timestamp, String idcliente, String iddireccion, String idvendedor, long total) {
        this.aprob = aprob;
        this.timestamp = timestamp;
        this.idcliente = idcliente;
        this.iddireccion = iddireccion;
        this.idvendedor = idvendedor;
        this.total = total;
    }
}
