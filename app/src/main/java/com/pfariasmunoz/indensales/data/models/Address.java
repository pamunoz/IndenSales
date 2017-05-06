package com.pfariasmunoz.indensales.data.models;

public class Address {
    private String comuna;
    private String zona;
    private String direccion;
    private String telefono;
    private String ciudad;

    public Address() {
    }

    public Address(String comuna, String zona, String direccion, String telefono, String ciudad) {
        this.comuna = comuna;
        this.zona = zona;
        this.direccion = direccion;
        this.telefono = telefono;
        this.ciudad = ciudad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
