package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class Client {
    private String descuento;
    private String nombre;
    private String rut;

    public Client() {
    }

    public Client(String descuento, String nombre, String rut) {
        this.descuento = descuento;
        this.nombre = nombre;
        this.rut = rut;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
}
