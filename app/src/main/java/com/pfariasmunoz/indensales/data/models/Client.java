package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class Client {
    public String descuento;
    public String nombre;
    public String rut;

    public Client() {
    }

    public Client(String descuento, String nombre, String rut) {
        this.descuento = descuento;
        this.nombre = nombre;
        this.rut = rut;
    }
}
