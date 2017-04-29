package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 29-04-17.
 */

public class ArticleSale {
    private Long cantidad;
    private Long total;

    public ArticleSale() {
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
