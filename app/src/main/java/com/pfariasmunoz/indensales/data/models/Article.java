package com.pfariasmunoz.indensales.data.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Pablo Farias on 17-04-17.
 */

@IgnoreExtraProperties
public class Article {
    public String descripcion;
    public String precio;

    public Article() {
    }

    public Article(String descripcion, String precio) {
        this.descripcion = descripcion;
        this.precio = precio;
    }
}
