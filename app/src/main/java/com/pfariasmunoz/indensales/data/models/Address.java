package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 17-04-17.
 */

public class Address {
    private String ciudad;
    private String comuna;
    private String direccion;
    private String telefono;
    private String zona;

    public Address() {
    }

    public Address(String ciudad, String comuna, String direccion, String telefono, String zona) {
        this.ciudad = ciudad;
        this.comuna = comuna;
        this.direccion = direccion;
        this.telefono = telefono;
        this.zona = zona;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }
}
