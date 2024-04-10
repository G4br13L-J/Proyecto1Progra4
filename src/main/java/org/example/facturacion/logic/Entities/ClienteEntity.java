package org.example.facturacion.logic.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "clientes")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proveedor", nullable = false)
    private UsuarioEntity idProveedor;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteEntity that = (ClienteEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(idProveedor, that.idProveedor) && Objects.equals(nombre, that.nombre) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idProveedor, nombre, email);
    }

    @Override
    public String toString() {
        return "ClienteEntity{" +
                "id=" + id +
                ", idProveedor=" + idProveedor +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}