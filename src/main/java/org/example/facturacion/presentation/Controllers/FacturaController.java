package org.example.facturacion.presentation.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.facturacion.logic.Entities.ClienteEntity;
import org.example.facturacion.logic.Entities.FacturaEntity;
import org.example.facturacion.logic.Entities.ProductoEnCarrito;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.logic.Services.ClienteService;
import org.example.facturacion.logic.Services.FacturaService;
import org.example.facturacion.logic.Services.ProductoService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"sessionUser", "facturas", "carrito", "total", "factura", "clienteSeleccionado"})
public class FacturaController {
    @ModelAttribute("carrito") public ArrayList<ProductoEnCarrito> carrito() { return new ArrayList<>(); }
    @ModelAttribute("total") public double total() { return 0.0; }
    @ModelAttribute("factura") public FacturaEntity factura() { return new FacturaEntity(); }

    @Autowired
    private FacturaService facturaService;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    HttpSession httpSession;

    @GetMapping("/presentation/facturas")
    public String mostrarFacturas(Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) model.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        List<FacturaEntity> facturas = facturaService.getFacturasByProveedor(sessionUser);
        model.addAttribute("facturas", facturas);

        model.addAttribute("error", httpSession.getAttribute("error"));
        httpSession.removeAttribute("error");

        return "presentation/facturas";
    }

    @GetMapping("/presentation/facturas/eliminar/{id}")
    public String eliminarFactura(@PathVariable("id") Integer facturaId) {
        facturaService.eliminarFactura(facturaId);
        return "redirect:/presentation/facturas";
    }

    @PostMapping("/presentation/facturas/agregarProducto")
    public String agregarProductoAlCarrito(@RequestParam(name = "producto") String nombreDelProducto, @RequestParam(name = "cantidad") Integer cantidad, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) model.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

ProductoEnCarrito producto = new ProductoEnCarrito();
        try {
            producto.setProducto(productoService.getProductosByNombreAndProveedor(nombreDelProducto, sessionUser));
            producto.setCantidad(cantidad);
        } catch (Exception e) {
            httpSession.setAttribute("error", e.getMessage());
            return "redirect:/presentation/facturas";
        }

        // se agrega al carrito
        ArrayList<ProductoEnCarrito> carrito = ArrayList.class.cast(model.getAttribute("carrito"));
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        boolean yaExiste = false;
        for (ProductoEnCarrito p : carrito) {
            if (p.getProducto().equals(producto.getProducto())) {
                p.setCantidad(p.getCantidad() + producto.getCantidad());
                yaExiste = true;
                break;
            }
        }

        if (!yaExiste) {
            carrito.add(producto);
        }

        model.addAttribute("carrito", carrito);

        // actualizar el total
        double total = 0.0;
        for (ProductoEnCarrito p : carrito) {
            total += p.getProducto().getPrecio().multiply(new BigDecimal(p.getCantidad())).doubleValue();
        }

        model.addAttribute("total", total);

        return "redirect:/presentation/facturas";
    }

    @GetMapping("/presentation/facturas/eliminarProducto/{id}")
    public String eliminarProductoDelCarrito(@PathVariable("id") Integer productoId, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) model.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        ArrayList<ProductoEnCarrito> carrito = ArrayList.class.cast(model.getAttribute("carrito"));
        if (carrito == null) {
            carrito = new ArrayList<>();
        }

        ProductoEnCarrito producto = null;
        for (ProductoEnCarrito p : carrito) {
            if (p.getProducto().getId().equals(productoId)) {
                producto = p;
                break;
            }
        }

        if (producto != null) {
            carrito.remove(producto);
        }

        model.addAttribute("carrito", carrito);

        // actualizar el total
        double total = 0.0;
        for (ProductoEnCarrito p : carrito) {
            total += p.getProducto().getPrecio().multiply(new BigDecimal(p.getCantidad())).doubleValue();
        }

        model.addAttribute("total", total);

        return "redirect:/presentation/facturas";
    }

    // escoger cliente
    @PostMapping("/presentation/facturas/escogerCliente")
    public String escogerCliente(@RequestParam(name = "cliente") Integer clienteId, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) model.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        try {
            model.addAttribute("clienteSeleccionado", clienteService.getClienteById(clienteId));
        } catch (Exception e){
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/facturas";
    }

    @GetMapping("/presentation/facturas/crearFactura")
    public String crearFactura(Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) model.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        ArrayList<ProductoEnCarrito> carrito = ArrayList.class.cast(model.getAttribute("carrito"));
        if (carrito == null || carrito.isEmpty()) {
            httpSession.setAttribute("error", "No hay productos en el carrito");
            return "redirect:/presentation/facturas";
        }

        FacturaEntity factura = new FacturaEntity();
        factura.setIdProveedor(sessionUser);
        factura.setIdCliente((ClienteEntity) model.getAttribute("clienteSeleccionado"));
        factura.setTotal(new BigDecimal(model.getAttribute("total").toString()));

        facturaService.crearFactura(factura, carrito);

        return "redirect:/presentation/facturas";
    }

    @GetMapping("/presentation/facturas/exportar/pdf/{id}")
    public ResponseEntity<ByteArrayResource> exportInvoicePDF(@PathVariable("id") Integer id) {
        try {
            byte[] pdfBytes = facturaService.exportInvoice(id, "pdf");

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return null;
    }

    @GetMapping("/presentation/facturas/exportar/xml/{id}")
    public ResponseEntity<ByteArrayResource> exportInvoiceXML(@PathVariable("id") Integer id) {
        try {
            byte[] xmlBytes = facturaService.exportInvoice(id, "xml");

            ByteArrayResource resource = new ByteArrayResource(xmlBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura.xml");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_XML)
                    .body(resource);
        } catch (Exception e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return null;
    }

}