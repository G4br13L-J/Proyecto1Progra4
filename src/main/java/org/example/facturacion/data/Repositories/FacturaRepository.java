package org.example.facturacion.data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.facturacion.logic.Entities.ClienteEntity;
import org.example.facturacion.logic.Entities.FacturaEntity;
import org.example.facturacion.logic.Entities.UsuarioEntity;

import java.util.List;

public interface FacturaRepository extends JpaRepository<FacturaEntity, Integer> {
    List<FacturaEntity> findAllByIdProveedorAndIdCliente(UsuarioEntity proveedor, ClienteEntity cliente);

    List<FacturaEntity> findAllByIdProveedor(UsuarioEntity proveedor);
}
