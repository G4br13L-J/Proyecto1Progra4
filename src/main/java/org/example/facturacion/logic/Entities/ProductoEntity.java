package org.example.facturacion.logic.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "productos")
public class ProductoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private UsuarioEntity idProveedor;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoEntity that = (ProductoEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(idProveedor, that.idProveedor) && Objects.equals(nombre, that.nombre) && Objects.equals(descripcion, that.descripcion) && Objects.equals(precio, that.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProveedor, nombre, descripcion, precio);
    }

    @Override
    public String toString() {
        return "ProductoEntity{" +
                "id=" + id +
                ", idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                '}';
    }
}