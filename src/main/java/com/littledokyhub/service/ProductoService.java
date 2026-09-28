package com.littledokyhub.service;

import com.littledokyhub.model.Categoria;
import com.littledokyhub.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ProductoService {

    private final List<Producto> productos = new CopyOnWriteArrayList<>();
    private final AtomicInteger siguienteId = new AtomicInteger(1);
    private final CategoriaService categoriaService;

    @Autowired
    public ProductoService(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;

        Categoria clasicas = this.categoriaService.buscarPorCodigo("clasicas").orElse(null);
        Categoria especiales = this.categoriaService.buscarPorCodigo("especiales").orElse(null);
        Categoria bebidas = this.categoriaService.buscarPorCodigo("bebidas").orElse(null);

        // --- Clásicas ---
        sembrar("Pizza Americana",
                "Abundante queso mozzarella fundido, jamón inglés seleccionado y salsa pomodoro casera.",
                32.00, "/images/americana.jpg", clasicas);
        sembrar("Pizza Pepperoni",
                "Capa generosa de queso mozzarella, salsa pomodoro y rodajas de pepperoni crocante al horno.",
                36.00, "/images/pepperoni.jpg", clasicas);
        sembrar("Pizza Hawaiana",
                "Queso mozzarella, trozos de jamón inglés y piña dulce caramelizada al horno de leña.",
                35.00, "/images/hawaiana.jpg", clasicas);

        // --- Especiales ---
        sembrar("Doky House Special",
                "Jamón serrano curado, champiñones al ajillo, queso gorgonzola y reducción balsámica.",
                44.00, "/images/doky_house.jpg", especiales);
        sembrar("Carnívora Suprema",
                "Carne sazonada, tocino ahumado crocante, salchicha italiana, pepperoni y mozzarella.",
                42.00, "/images/full_meat.jpg", especiales);
        sembrar("Cuatro Quesos",
                "Mozzarella de búfala, provolone ahumado, queso azul cremoso y parmesano madurado.",
                40.00, "/images/cuatro_quesos.jpg", especiales);
        sembrar("Pizza Lomo Saltado",
                "Tiras de lomo fino al wok, cebolla morada, tomate, ají amarillo y queso mozzarella.",
                46.00, "/images/lomo_saltado.jpg", especiales);
        sembrar("Pizza Ají de Gallina",
                "Crema casera de ají de gallina, hebras de pollo tierno, aceituna botija y mozzarella.",
                38.00, "/images/aji_gallina.jpg", especiales);
        sembrar("Pizza Huachana",
                "Salchicha huachana dorada artesanal, mozzarella, huevo de codorniz y cebollita china.",
                39.00, "/images/huachana.jpg", especiales);

        // --- Bebidas ---
        sembrar("Gaseosas 1.5 L",
                "Inca Kola o Coca-Cola en botella de 1.5 Litros, servidas bien heladas.",
                10.00, "/images/gaseosas.jpg", bebidas);
        sembrar("Chicha Morada (1 L)",
                "Elaborada artesanalmente con maíz morado, piña, membrillo, manzana y canela.",
                12.00, "/images/chicha.jpg", bebidas);
        sembrar("Cerveza Artesanal",
                "Botella de 330 ml. Variedades: IPA, Red Ale o Trigo artesanal local.",
                14.00, "/images/cerveza.jpg", bebidas);
    }

    private void sembrar(String nombre, String descripcion, Double precio, String imagenUrl, Categoria categoria) {
        productos.add(new Producto(siguienteId.getAndIncrement(), nombre, descripcion, precio, imagenUrl, categoria));
    }

    public List<Producto> listar() {
        return List.copyOf(productos);
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public boolean guardar(Producto datos) {
        if (datos.getId() == null) {
            datos.setId(siguienteId.getAndIncrement());
            productos.add(datos);
            return true;
        }
        Optional<Producto> existente = buscarPorId(datos.getId());
        if (existente.isEmpty()) {
            return false;
        }
        Producto p = existente.get();
        p.setNombre(datos.getNombre());
        p.setDescripcion(datos.getDescripcion());
        p.setPrecio(datos.getPrecio());
        p.setImagenUrl(datos.getImagenUrl());
        p.setCategoria(datos.getCategoria());
        return true;
    }

    public boolean eliminar(Integer id) {
        return productos.removeIf(p -> p.getId().equals(id));
    }

    private List<Producto> porCategoria(String codigoCategoria) {
        return productos.stream()
                .filter(p -> p.getCategoria() != null
                        && codigoCategoria.equalsIgnoreCase(p.getCategoria().getCodigo()))
                .toList();
    }

    public List<Producto> obtenerClasicas() {
        return porCategoria("clasicas");
    }

    public List<Producto> obtenerEspeciales() {
        return porCategoria("especiales");
    }

    public List<Producto> obtenerBebidas() {
        return porCategoria("bebidas");
    }

    public List<Producto> obtenerFavoritas() {
        return obtenerClasicas().stream().limit(3).toList();
    }
}