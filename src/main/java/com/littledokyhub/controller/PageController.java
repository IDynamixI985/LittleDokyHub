package com.littledokyhub.controller;

import com.littledokyhub.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/")
    public String index(Model model) {
        // Pasa la lista simulada a index.html
        model.addAttribute("favoritas", productoService.obtenerFavoritas());
        return "index";
    }

    // Mantén las demás rutas que ya tenías para tu equipo:
    @GetMapping("/categorias")
    public String categorias() {
        return "shop/categorias";
    }

    @GetMapping("/detalles")
    public String detalles() {
        return "shop/detalles";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contact/contacto";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "account/create";
    }

    @GetMapping("/administracion")
    public String administracion() {
        return "admin/admin";
    }
}