package org.example.facturacion.data.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.facturacion.logic.Entities.DetallesFacturaEntity;
import org.example.facturacion.logic.Entities.FacturaEntity;

import java.util.List;

public interface DetallesFacturaRepository extends JpaRepository<DetallesFacturaEntity, Integer> {
    List<DetallesFacturaEntity> getDetallesFacturaEntitiesByFactura(FacturaEntity factura);
}