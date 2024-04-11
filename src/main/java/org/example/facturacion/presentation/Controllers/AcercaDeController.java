package org.example.facturacion.presentation.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcercaDeController {
    @GetMapping("/presentation/acercaDe")
    public String getAcercaDePage() {
        return "redirect:/presentation/acercaDe";
    }
}
