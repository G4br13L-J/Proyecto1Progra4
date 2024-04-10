package org.example.facturacion.presentation.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.facturacion.logic.Entities.ClienteEntity;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.logic.Services.ClienteService;

import java.util.ArrayList;

@Controller
@SessionAttributes({"sessionUser", "clienteSeleccionado", "clientes"})
public class ClienteController {
    @ModelAttribute("clienteSeleccionado") public ClienteEntity currentClient() { return new ClienteEntity(); }
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/presentation/clientes")
    public String getClientesPage(Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");  // el proveedor loggeado
        System.out.println("sessionUser: " + sessionUser);
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor") ) {
            return "redirect:/presentation/login";
        }

        if (model.getAttribute("clientes") == null) {
            // obtener la lista de clientes del proveedor loggeado
            model.addAttribute("clientes", clienteService.getClientesByProveedor(sessionUser));
        }

        ArrayList<ClienteEntity> clientes = ArrayList.class.cast(model.getAttribute("clientes"));
        model.addAttribute("clientes", clientes);

        model.addAttribute("error", httpSession.getAttribute("error"));
        httpSession.removeAttribute("error");

        return "presentation/clientes";
    }

    @PostMapping("/presentation/clientes/agregar")
    public String agregarCliente(@ModelAttribute("clienteSeleccionado") ClienteEntity cliente, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor") ) {
            return "redirect:/presentation/login";
        }

        cliente.setIdProveedor(sessionUser);

        try {
            clienteService.guardarCliente(cliente);
            // clientes actualizados
            model.addAttribute("clientes", clienteService.getClientesByProveedor(sessionUser));

        } catch (IllegalArgumentException e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/clientes";
    }

    @GetMapping("/presentation/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Integer clienteId, Model model) {
        System.out.println("clienteId: " + clienteId);
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor") ) {
            return "redirect:/presentation/login";
        }

        try {
            clienteService.eliminarCliente(clienteId);
            // clientes actualizados
            model.addAttribute("clientes", clienteService.getClientesByProveedor(sessionUser));
        } catch (IllegalArgumentException e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/clientes";
    }

    @PostMapping("/presentation/clientes/buscar")
    public String buscarCliente(@RequestParam("nombre") String nombre, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor") ) {
            return "redirect:/presentation/login";
        }

        ArrayList<ClienteEntity> clientes = clienteService.buscarClientesPorNombre(nombre, sessionUser);
        model.addAttribute("clientes", clientes);

        return "presentation/clientes";
    }

    @PostMapping("/presentation/clientes/editar/{id}")
    public String editarCliente(@PathVariable("id") Integer clienteId, @ModelAttribute("clienteSeleccionado") ClienteEntity clienteEditado, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor")) {
            return "redirect:/presentation/login";
        }

        ClienteEntity clienteExistente = clienteService.getClienteById(clienteId);
        if (clienteExistente == null) {
            // Manejar el caso en el que el cliente no existe
            return "redirect:/presentation/clientes";
        }

        clienteEditado.setIdProveedor(sessionUser);
        clienteEditado.setId(clienteId); // Establecer el ID del cliente

        try {
            clienteService.editarCliente(clienteEditado);
            // lista actualizada de clientes
            model.addAttribute("clientes", clienteService.getClientesByProveedor(sessionUser));
        } catch (IllegalArgumentException e) {
            httpSession.setAttribute("error", e.getMessage());
        }

        return "redirect:/presentation/clientes";
    }

}
