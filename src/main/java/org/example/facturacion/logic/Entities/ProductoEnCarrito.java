package org.example.facturacion.logic.Entities;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductoEnCarrito {
    private ProductoEntity producto;
    private int cantidad;

    public ProductoEnCarrito(ProductoEntity producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ProductoEnCarrito() {
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ProductoEnCarrito{" +
                "producto=" + producto +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoEnCarrito that = (ProductoEnCarrito) o;
        return cantidad == that.cantidad && Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, cantidad);
    }

    public BigDecimal getPrecioUnitario() {
        return producto.getPrecio();
    }
}
