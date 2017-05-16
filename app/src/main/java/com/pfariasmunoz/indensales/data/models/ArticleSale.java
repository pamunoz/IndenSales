package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 29-04-17.
 */

public class ArticleSale {
    public int cantidad;
    public String idarticulo;
    public String idventa;
    public Long total;


    public ArticleSale() {
    }

    public ArticleSale(int cantidad, String idarticulo, String idventa, Long total) {
        this.cantidad = cantidad;
        this.idarticulo = idarticulo;
        this.idventa = idventa;
        this.total = total;
    }
}
