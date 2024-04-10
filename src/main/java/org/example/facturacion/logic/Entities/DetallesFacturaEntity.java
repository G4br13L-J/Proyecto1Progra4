package org.example.facturacion.logic.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "detalles_factura")
public class DetallesFacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_factura", nullable = false)
    private FacturaEntity factura;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_producto", nullable = false)
    private ProductoEntity producto;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FacturaEntity getIdFactura() {
        return factura;
    }

    public void setIdFactura(FacturaEntity idFactura) {
        this.factura = idFactura;
    }

    public ProductoEntity getIdProducto() {
        return producto;
    }

    public void setIdProducto(ProductoEntity idProducto) {
        this.producto = idProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallesFacturaEntity that = (DetallesFacturaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(factura, that.factura) && Objects.equals(producto, that.producto) && Objects.equals(cantidad, that.cantidad) && Objects.equals(precioUnitario, that.precioUnitario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, factura, producto, cantidad, precioUnitario);
    }

    @Override
    public String toString() {
        return "DetallesFacturaEntity{" +
                "id=" + id +
                ", factura=" + factura +
                ", producto=" + producto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}