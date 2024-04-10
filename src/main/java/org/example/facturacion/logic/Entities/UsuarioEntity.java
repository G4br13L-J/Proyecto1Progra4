package org.example.facturacion.logic.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 100)
    @NotNull
    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @NotNull
    @Lob
    @Column(name = "rol", nullable = false)
    private String rol;

    @ColumnDefault("0")
    @Column(name = "estatus")
    private Boolean estatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntity that = (UsuarioEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(email, that.email) && Objects.equals(contrasena, that.contrasena) && Objects.equals(rol, that.rol) && Objects.equals(estatus, that.estatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, email, contrasena, rol, estatus);
    }

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", rol='" + rol + '\'' +
                ", estatus=" + estatus +
                '}';
    }
}