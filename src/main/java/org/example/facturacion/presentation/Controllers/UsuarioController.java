package org.example.facturacion.presentation.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.logic.Services.UsuarioService;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/presentation/register")
    public String getRegisterUsuarioPage(Model model) {
        model.addAttribute("newUser", new UsuarioEntity());
        // en el registro.html donde vayan a poner el form ponen algo asi:
//        <form method="post" action="/register" th:object="${newUser}">

        return "presentation/registro";
    }

    @PostMapping("/presentation/register/registrar")
    public String registerUsuario(UsuarioEntity usuarioEntity, Model model) {
        try {
            usuarioService.registerUsuario(usuarioEntity);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/presentation/register";
        }
        return "redirect:/presentation/login";
    }

    @GetMapping("/presentation/login")
    public String getLoginUsuarioPage(Model model) {
        model.addAttribute("loginForm", new UsuarioEntity());
        // en el login.html donde vayan a poner el form ponen algo asi:
//        <form method="post" action="/login" th:object="${loginForm}">
        return "presentation/login";
    }

    @PostMapping("/presentation/login/loggear")
    public String loginUsuario(UsuarioEntity usuarioEntity, Model model) {
        try {
            UsuarioEntity usuario = usuarioService.login(usuarioEntity.getEmail(), usuarioEntity.getContrasena());
            httpSession.setAttribute("sessionUser", usuario);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/presentation/login";
        }

        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");

        if (sessionUser.getRol().equals("administrador")) {
            System.out.println("Es administrador");
            return "redirect:/presentation/admin/users";
        }

        System.out.println("Es proveedor");
        if (!sessionUser.getEstatus()) {
            System.out.println("Usuario inactivo");
            System.out.println(sessionUser.getEstatus());
            httpSession.removeAttribute("sessionUser");
            return "redirect:/presentation/login";
        }

        System.out.println("Usuario activo");
        return "redirect:/presentation/clientes";
    }

    @GetMapping("/logout")
    public String logout() {
        httpSession.removeAttribute("sessionUser");
        return "redirect:login";  // redirige a la pagina principal
    }

    @GetMapping("/presentation/profile")
    public String getProfilePage(Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor") ) {
            return "redirect:/presentation/login";
        }
        model.addAttribute("user", sessionUser);
        return "presentation/perfil";
    }

    @PostMapping("/presentation/profile/change")
    public String changeInfo(UsuarioEntity usuarioEntity, Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null || !sessionUser.getRol().equals("proveedor") ) {
            return "redirect:/login";
        }

        try {
            usuarioService.actualizarDatos(sessionUser, usuarioEntity);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/presentation/profile";
    }


    // para mostrar la tabla con los usuarios (que sean proveedores) registrados
    @GetMapping("presentation/admin/users")
    public String getUsersPage(Model model) {
        UsuarioEntity sessionUser = (UsuarioEntity) httpSession.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/login";
        }
        if (!sessionUser.getRol().equals("administrador")) {
            return "redirect:/facturar";
        }
        model.addAttribute("user", sessionUser);
        model.addAttribute("users", usuarioService.getAllProveedores());
        return "presentation/admin";
    }
}
