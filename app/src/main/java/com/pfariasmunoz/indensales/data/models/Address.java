package com.pfariasmunoz.indensales.data.models;

public class Address {
    public String ciudad;
    public String comuna;
    public String direccion;
    public String idcliente;
    public String telefono;
    public String zona;

    public Address() {
    }

    public Address(String ciudad, String comuna, String direccion, String idcliente, String telefono, String zona) {
        this.ciudad = ciudad;
        this.comuna = comuna;
        this.direccion = direccion;
        this.idcliente = idcliente;
        this.telefono = telefono;
        this.zona = zona;
    }
}
