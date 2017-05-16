package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 16-05-17.
 */

public class SaleReport {
    public String idcliente;
    public String idvendedor;
    public String idventa;
    public String nombre_cliente;
    public String rut_cliente;
    public String total;
    public long timestamp;
    public String direccion;

    public SaleReport() {
    }

    public SaleReport(
            String idcliente,
            String idvendedor,
            String idventa,
            String nombre_cliente,
            String rut_cliente,
            String total,
            long timestamp,
            String direccion) {
        this.idcliente = idcliente;
        this.idvendedor = idvendedor;
        this.idventa = idventa;
        this.nombre_cliente = nombre_cliente;
        this.rut_cliente = rut_cliente;
        this.total = total;
        this.timestamp = timestamp;
        this.direccion = direccion;
    }
}
