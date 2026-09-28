package com.littledokyhub.service;

import com.littledokyhub.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UsuarioService {

    private final List<Usuario> usuarios = new CopyOnWriteArrayList<>();
    private final AtomicInteger siguienteId = new AtomicInteger(1);

    public UsuarioService() {
        sembrar("Administrador Doky", "admin@littledokyhub.com", "Admin123!", "999888777", "ADMIN");

        sembrar("Carlos Mendoza", "carlos.mendoza@gmail.com", "123456", "987654321", "CLIENTE");
        sembrar("Lucía Fernández", "lucia.f@hotmail.com", "123456", "912345678", "CLIENTE");
    }

    private void sembrar(String nombre, String email, String password, String telefono, String rol) {
        usuarios.add(new Usuario(siguienteId.getAndIncrement(), nombre, email, password, telefono, rol, true));
    }


    public List<Usuario> listar() {
        return List.copyOf(usuarios);
    }

    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public boolean guardar(Usuario datos) {
        if (datos.getId() == null) {
            if (buscarPorEmail(datos.getEmail()).isPresent()) {
                return false;
            }
            datos.setId(siguienteId.getAndIncrement());
            if (datos.getRol() == null || datos.getRol().isBlank()) {
                datos.setRol("CLIENTE");
            }
            if (datos.getActivo() == null) {
                datos.setActivo(true);
            }
            usuarios.add(datos);
            return true;
        }

        Optional<Usuario> existente = buscarPorId(datos.getId());
        if (existente.isEmpty()) {
            return false;
        }

        Usuario u = existente.get();
        u.setNombre(datos.getNombre());
        u.setEmail(datos.getEmail());
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            u.setPassword(datos.getPassword());
        }
        u.setTelefono(datos.getTelefono());
        u.setRol(datos.getRol());
        u.setActivo(datos.getActivo());
        return true;
    }

    public boolean eliminar(Integer id) {
        return usuarios.removeIf(u -> u.getId().equals(id));
    }

    public Optional<Usuario> autenticar(String email, String password) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getPassword().equals(password)
                        && Boolean.TRUE.equals(u.getActivo()))
                .findFirst();
    }

    public long contarClientes() {
        return usuarios.stream().filter(u -> "CLIENTE".equalsIgnoreCase(u.getRol())).count();
    }
}