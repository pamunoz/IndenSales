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

    private boolean aprob;
    private String fecha;
    private String idcliente;
    private String iddireccion;
    private String idvendedor;
    private long total;

    public Sale() {
    }

    public Sale(boolean aprob,
                String fecha,
                String idcliente,
                String iddireccion,
                String idvendedor,
                long total) {
        this.aprob = aprob;
        this.fecha = fecha;
        this.idcliente = idcliente;
        this.iddireccion = iddireccion;
        this.idvendedor = idvendedor;
        this.total = total;
    }

    public boolean isAprob() {
        return aprob;
    }

    public String getFecha() {
        return fecha;
    }

    public String getIdcliente() {
        return idcliente;
    }

    public String getIddireccion() {
        return iddireccion;
    }

    public String getIdvendedor() {
        return idvendedor;
    }

    public long getTotal() {
        return total;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("aprob", aprob);
        result.put("fecha", fecha);
        result.put("idcliente", idcliente);
        result.put("iddireccion", iddireccion);
        result.put("idvendedor", idvendedor);
        result.put("total", total);

        return result;
    }
}
