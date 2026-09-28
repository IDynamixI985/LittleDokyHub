package com.littledokyhub.controller;

import com.littledokyhub.model.Producto;
import com.littledokyhub.service.CategoriaService;
import com.littledokyhub.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administracion/productos")
public class AdminProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/guardar")
    public String guardar(@RequestParam(value = "id", required = false) Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam("precio") Double precio,
            @RequestParam("codigoCategoria") String codigoCategoria,
            @RequestParam(value = "imagenUrl", required = false) String imagenUrl,
            RedirectAttributes flash) {

        // 1. Validar y obtener la Categoría
        var catOpt = categoriaService.buscarPorCodigo(codigoCategoria);
        if (catOpt.isEmpty()) {
            flash.addFlashAttribute("error", "Selecciona una categoría válida.");
            return "redirect:/administracion#productos";
        }

        // 2. Validar campos requeridos
        if (nombre == null || nombre.isBlank()) {
            flash.addFlashAttribute("error", "El nombre del producto es obligatorio.");
            return "redirect:/administracion#productos";
        }
        if (precio == null || precio <= 0) {
            flash.addFlashAttribute("error", "El precio debe ser mayor a 0.");
            return "redirect:/administracion#productos";
        }

        // 3. Crear el objeto Producto
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre.trim());
        producto.setDescripcion(descripcion != null ? descripcion.trim() : "");
        producto.setPrecio(precio);
        producto.setCategoria(catOpt.get());

        if (imagenUrl == null || imagenUrl.isBlank()) {
            producto.setImagenUrl("/images/logo.png");
        } else {
            producto.setImagenUrl(imagenUrl.trim());
        }

        // 4. Guardar o actualizar
        boolean esNuevo = (id == null);
        if (productoService.guardar(producto)) {
            flash.addFlashAttribute("exito",
                    esNuevo ? "Producto creado correctamente." : "Producto actualizado correctamente.");
        } else {
            flash.addFlashAttribute("error", "El producto que intentas editar no existe.");
        }

        return "redirect:/administracion#productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Integer id, RedirectAttributes flash) {
        if (productoService.eliminar(id)) {
            flash.addFlashAttribute("exito", "Producto eliminado correctamente.");
        } else {
            flash.addFlashAttribute("error", "No se encontró el producto a eliminar.");
        }
        return "redirect:/administracion#productos";
    }
}