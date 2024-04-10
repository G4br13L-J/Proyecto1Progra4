package org.example.facturacion.logic.Exportar;

import org.example.facturacion.logic.Entities.DetallesFacturaEntity;
import org.example.facturacion.logic.Entities.FacturaEntity;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ExportarFacturas {

    public byte[] exportToPDF(FacturaEntity factura, List<DetallesFacturaEntity> detallesFactura) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, outputStream);
            document.open();

            com.itextpdf.text.Font titleFont = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 18, com.itextpdf.text.BaseColor.BLACK);
            com.itextpdf.text.Font infoFont = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA, 12, com.itextpdf.text.BaseColor.BLACK);

            com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("Factura", titleFont);
            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(title);

            document.add(new com.itextpdf.text.Paragraph("\n"));

            document.add(new com.itextpdf.text.Paragraph("Detalles de la Factura:", infoFont));
            document.add(new com.itextpdf.text.Paragraph("ID de Factura: " + factura.getId(), infoFont));
            document.add(new com.itextpdf.text.Paragraph("Fecha de Emisión: " + factura.getFecha(), infoFont));
            document.add(new com.itextpdf.text.Paragraph("ID de Proveedor: " + factura.getIdProveedor().getId(), infoFont));
            document.add(new com.itextpdf.text.Paragraph("ID de Cliente: " + factura.getIdCliente().getId(), infoFont));
            document.add(new com.itextpdf.text.Paragraph("Total: " + factura.getTotal(), infoFont));

            for (DetallesFacturaEntity detalle : detallesFactura) {
                document.add(new com.itextpdf.text.Paragraph("\n"));
                document.add(new com.itextpdf.text.Paragraph("ID Producto: " + detalle.getIdProducto().getId(), infoFont));
                document.add(new com.itextpdf.text.Paragraph("Cantidad: " + detalle.getCantidad(), infoFont));
                document.add(new com.itextpdf.text.Paragraph("Precio Unitario: " + detalle.getPrecioUnitario(), infoFont));
                document.add(new com.itextpdf.text.Paragraph("Total Detalle: " + detalle.getPrecioUnitario().multiply(new java.math.BigDecimal(detalle.getCantidad())), infoFont));
            }


            document.close();
        } catch (com.itextpdf.text.DocumentException e) {
            throw new RuntimeException("Error al exportar la factura a PDF: " + e.getMessage());
        }

        return outputStream.toByteArray();
    }

    public byte[] exportToXML(FacturaEntity factura, List<DetallesFacturaEntity> detallesFactura) {
        try {
            org.w3c.dom.Document document = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            org.w3c.dom.Element rootElement = document.createElement("factura");
            document.appendChild(rootElement);

            org.w3c.dom.Element idElement = document.createElement("id");
            idElement.appendChild(document.createTextNode(factura.getId().toString()));
            rootElement.appendChild(idElement);

            org.w3c.dom.Element fechaElement = document.createElement("fecha");
            fechaElement.appendChild(document.createTextNode(factura.getFecha().toString()));
            rootElement.appendChild(fechaElement);

            org.w3c.dom.Element idProveedorElement = document.createElement("idProveedor");
            idProveedorElement.appendChild(document.createTextNode(factura.getIdProveedor().getId().toString()));
            rootElement.appendChild(idProveedorElement);

            org.w3c.dom.Element idClienteElement = document.createElement("idCliente");
            idClienteElement.appendChild(document.createTextNode(factura.getIdCliente().getId().toString()));
            rootElement.appendChild(idClienteElement);

            org.w3c.dom.Element totalElement = document.createElement("total");
            totalElement.appendChild(document.createTextNode(factura.getTotal().toString()));
            rootElement.appendChild(totalElement);

            for (DetallesFacturaEntity detalle : detallesFactura) {
                org.w3c.dom.Element detalleElement = document.createElement("detalle");
                rootElement.appendChild(detalleElement);

                org.w3c.dom.Element idProductoElement = document.createElement("idProducto");
                idProductoElement.appendChild(document.createTextNode(detalle.getIdProducto().getId().toString()));
                detalleElement.appendChild(idProductoElement);

                org.w3c.dom.Element cantidadElement = document.createElement("cantidad");
                cantidadElement.appendChild(document.createTextNode(detalle.getCantidad().toString()));
                detalleElement.appendChild(cantidadElement);

                org.w3c.dom.Element precioUnitarioElement = document.createElement("precioUnitario");
                precioUnitarioElement.appendChild(document.createTextNode(detalle.getPrecioUnitario().toString()));
                detalleElement.appendChild(precioUnitarioElement);

                org.w3c.dom.Element totalDetalleElement = document.createElement("totalDetalle");
                totalDetalleElement.appendChild(document.createTextNode(detalle.getPrecioUnitario().multiply(new java.math.BigDecimal(detalle.getCantidad())).toString()));
                detalleElement.appendChild(totalDetalleElement);
            }

            // Convertir el documento a bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
            transformer.transform(new javax.xml.transform.dom.DOMSource(document), new javax.xml.transform.stream.StreamResult(outputStream));

            return outputStream.toByteArray();
        } catch (javax.xml.parsers.ParserConfigurationException | javax.xml.transform.TransformerException e) {
            throw new RuntimeException("Error al exportar la factura a XML: " + e.getMessage());
        }
    }
}
