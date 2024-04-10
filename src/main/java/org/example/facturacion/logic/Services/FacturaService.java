package org.example.facturacion.logic.Services;

import org.example.facturacion.logic.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.facturacion.logic.Entities.*;
import org.example.facturacion.logic.Exportar.ExportarFacturas;
import org.example.facturacion.data.Repositories.DetallesFacturaRepository;
import org.example.facturacion.data.Repositories.FacturaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetallesFacturaRepository detallesFacturaRepository;

    public FacturaEntity getFacturaById(Integer facturaId) {
        return facturaRepository.findById(facturaId).orElse(null);
    }

    public void guardarDetalleFactura(FacturaEntity factura, ProductoEntity producto, int cantidad, BigDecimal precioUnitario) {
        // Create a new DetalleFacturaEntity instance
        DetallesFacturaEntity detalleFactura = new DetallesFacturaEntity();
        detalleFactura.setIdFactura(factura);
        detalleFactura.setIdProducto(producto);
        detalleFactura.setCantidad(cantidad);
        detalleFactura.setPrecioUnitario(precioUnitario);

        // Save the detalle factura
        detallesFacturaRepository.save(detalleFactura);
    }

    public void guardarFactura(FacturaEntity factura) {
        facturaRepository.save(factura);
    }

    public List<FacturaEntity> getFacturasByProveedorAndCliente(UsuarioEntity proveedor, ClienteEntity cliente) {
        return facturaRepository.findAllByIdProveedorAndIdCliente(proveedor, cliente);
    }

    public List<FacturaEntity> getFacturasByProveedor(UsuarioEntity proveedor) {
        return facturaRepository.findAllByIdProveedor(proveedor);
    }

    public void eliminarFactura(Integer facturaId) {
        // eliminar los detalles de la factura
        List<DetallesFacturaEntity> detallesFactura = detallesFacturaRepository.getDetallesFacturaEntitiesByFactura(facturaRepository.findById(facturaId).orElse(null));
        detallesFacturaRepository.deleteAll(detallesFactura);
        // eliminar la factura
        facturaRepository.deleteById(facturaId);
    }

    public void crearFactura(FacturaEntity factura, ArrayList<ProductoEnCarrito> carrito) {
        // Save the factura
        facturaRepository.save(factura);

        // Save the detalles factura
        for (ProductoEnCarrito producto : carrito) {
            guardarDetalleFactura(factura, producto.getProducto(), producto.getCantidad(), producto.getPrecioUnitario());
        }

        // Clear the carrito
        carrito.clear();
    }

    // format => "pdf" o "xml"
    public byte[] exportInvoice(Integer facturaId, String format) {
        FacturaEntity factura = facturaRepository.findById(facturaId).orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        List<DetallesFacturaEntity> detalles = detallesFacturaRepository.getDetallesFacturaEntitiesByFactura(factura);

        ExportarFacturas invoiceExporter = new ExportarFacturas();
        try {
            switch (format) {
                case "pdf":
                    return invoiceExporter.exportToPDF(factura, detalles);
                case "xml":
                    return invoiceExporter.exportToXML(factura, detalles);
                default:
                    throw new RuntimeException("Formato no soportado");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
