package org.example.facturacion.logic.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "facturas")
public class FacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private UsuarioEntity idProveedor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteEntity idCliente;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UsuarioEntity getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(UsuarioEntity idProveedor) {
        this.idProveedor = idProveedor;
    }

    public ClienteEntity getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(ClienteEntity idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FacturaEntity that = (FacturaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(idProveedor, that.idProveedor) && Objects.equals(idCliente, that.idCliente) && Objects.equals(fecha, that.fecha) && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProveedor, idCliente, fecha, total);
    }

    @Override
    public String toString() {
        return "FacturaEntity{" +
                "id=" + id +
                ", idProveedor=" + idProveedor +
                ", idCliente=" + idCliente +
                ", fecha=" + fecha +
                ", total=" + total +
                '}';
    }
}