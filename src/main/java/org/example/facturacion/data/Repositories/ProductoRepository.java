package org.example.facturacion.data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.facturacion.logic.Entities.ProductoEntity;
import org.example.facturacion.logic.Entities.UsuarioEntity;

import java.util.List;

public interface ProductoRepository extends JpaRepository<ProductoEntity, Integer> {
    List<ProductoEntity> findAllByIdProveedor(UsuarioEntity userLogged);

    List<ProductoEntity> findAllByIdProveedorAndNombreContaining(UsuarioEntity userLogged, String searchName);

    ProductoEntity findByIdProveedorAndNombre(UsuarioEntity sessionUser, String nombreDelProducto);
}
