package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 20-04-17.
 */

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
}
