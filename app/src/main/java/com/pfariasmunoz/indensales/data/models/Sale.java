package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 20-04-17.
 */

public class Sale {

    private boolean aprob;
    private String fecha;
    private long idcliente;
    private long iddireccion;
    private long idvendedor;
    private long total;

    public Sale() {
    }

    public Sale(boolean aprob,
                String fecha,
                long idcliente,
                long iddireccion,
                long idvendedor,
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

    public long getIdcliente() {
        return idcliente;
    }

    public long getIddireccion() {
        return iddireccion;
    }

    public long getIdvendedor() {
        return idvendedor;
    }

    public long getTotal() {
        return total;
    }
}
