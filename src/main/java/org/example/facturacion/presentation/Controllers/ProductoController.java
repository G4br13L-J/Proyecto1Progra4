package org.example.facturacion.presentation.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.facturacion.logic.Entities.ProductoEntity;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.logic.Services.ProductoService;

import java.util.List;

@Controller
@SessionAttributes({"sessionUser", "productoSeleccionado", "productos"})
public class ProductoController {
    @ModelAttribute("productoSeleccionado")
    public ProductoEntity currentProduct() {
        return new ProductoEntity();
    }

    @Autowired
    private ProductoService productoService;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/presentation/productos")
    public String getProductosPage(Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        if (model.getAttribute("productos") == null) {
            model.addAttribute("productos", productoService.getProductosByProveedor(sessionUser));
        }

        List<ProductoEntity> productos = (List<ProductoEntity>) model.getAttribute("productos");
        model.addAttribute("productos", productos);

        model.addAttribute("error", httpSession.getAttribute("error"));
        httpSession.removeAttribute("error");

        return "presentation/productos";
    }

    @PostMapping("/presentation/productos/agregar")
    public String agregarProducto(@ModelAttribute("productoSeleccionado") ProductoEntity producto, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        producto.setIdProveedor(sessionUser);

        try {
            productoService.guardarProducto(producto);
            model.addAttribute("productos", productoService.getProductosByProveedor(sessionUser));
        } catch (IllegalArgumentException e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/productos";
    }

    @GetMapping("/presentation/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer productoId, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        try {
            productoService.eliminarProducto(productoId);
            model.addAttribute("productos", productoService.getProductosByProveedor(sessionUser));
        } catch (IllegalArgumentException e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/productos";
    }

    @PostMapping("/presentation/productos/buscar")
    public String buscarProducto(@RequestParam("nombre") String nombre, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        List<ProductoEntity> productos = productoService.buscarProductosPorNombre(nombre, sessionUser);
        model.addAttribute("productos", productos);

        return "presentation/productos";
    }

    @PostMapping("/presentation/productos/editar/{id}")
    public String editarProducto(@PathVariable("id") Integer productoId, @ModelAttribute("productoSeleccionado") ProductoEntity productoEditado, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        ProductoEntity productoExistente = productoService.getProductoById(productoId);
        if (productoExistente == null) {
            return "redirect:/presentation/productos";
        }

        productoEditado.setIdProveedor(sessionUser);
        productoEditado.setId(productoId);

        try {
            productoService.editarProducto(productoEditado);
            model.addAttribute("productos", productoService.getProductosByProveedor(sessionUser));
        } catch (IllegalArgumentException e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/productos";
    }
}
