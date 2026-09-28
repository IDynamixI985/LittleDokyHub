package com.littledokyhub.service;

import com.littledokyhub.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CategoriaService {

    private final List<Categoria> categorias = new CopyOnWriteArrayList<>();
    private final AtomicInteger siguienteId = new AtomicInteger(1);

    public CategoriaService() {
        sembrar("clasicas", "Pizzas Clásicas", 
                "Las recetas tradicionales preparadas con masa madre y horneadas a la leña.", 
                "bi-pie-chart");
        sembrar("especiales", "Pizzas Especiales & Fusión", 
                "Combinaciones de autor, ingredientes premium e innovaciones culinarias.", 
                "bi-fire");
        sembrar("bebidas", "Bebidas", 
                "Gaseosas frías, chicha morada artesanal y cervezas locales para acompañar.", 
                "bi-cup-straw");
    }

    private void sembrar(String codigo, String nombre, String descripcion, String icono) {
        categorias.add(new Categoria(siguienteId.getAndIncrement(), codigo, nombre, descripcion, icono));
    }


    public List<Categoria> listar() {
        return List.copyOf(categorias);
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categorias.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Optional<Categoria> buscarPorCodigo(String codigo) {
        return categorias.stream().filter(c -> c.getCodigo().equalsIgnoreCase(codigo)).findFirst();
    }

}