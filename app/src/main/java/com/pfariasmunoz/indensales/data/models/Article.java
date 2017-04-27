package com.pfariasmunoz.indensales.data.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Pablo Farias on 17-04-17.
 */

@IgnoreExtraProperties
public class Article {
    private String descripcion;
    private String precio;

    public Article() {
    }

    public Article(String descripcion, String precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
