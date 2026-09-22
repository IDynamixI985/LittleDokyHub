package com.littledokyhub.service;

import com.littledokyhub.model.Producto;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    public List<Producto> obtenerFavoritas() {
        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto(1, "Pizza Americana", 
            "Abundante queso mozzarella fundido, jamón inglés seleccionado y salsa pomodoro.", 
            32.00, "/images/americana.jpg"));
        lista.add(new Producto(2, "Pizza Pepperoni", 
            "Capa generosa de queso mozzarella, salsa pomodoro y rodajas de pepperoni crocante.", 
            36.00, "/images/pepperoni.jpg"));
        lista.add(new Producto(3, "Pizza Hawaiana", 
            "Queso mozzarella, trozos de jamón inglés y piña dulce caramelizada al horno.", 
            35.00, "/images/hawaiana.jpg"));
        return lista;
    }
}