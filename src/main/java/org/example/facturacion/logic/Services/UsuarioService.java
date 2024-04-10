package org.example.facturacion.logic.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.data.Repositories.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
    }

    public List<UsuarioEntity> getUsuarios() {
        return this.usuarioRepository.findAll();
    }

    // Guarda un usuario en la base de datos
    public UsuarioEntity registerUsuario(UsuarioEntity usuarioEntity) {
        // como los usuario que se registran SIEMPRE van a ser proveedores, porque no hace falta registrar administradores, se puede poner el rol directamente
        // con esto se ahorran campos en el formulario de registro
        usuarioEntity.setRol("proveedor");
        return this.usuarioRepository.save(usuarioEntity);
    }

    public UsuarioEntity login(String email, String password) {
        Optional<UsuarioEntity> usuarioOptional = this.usuarioRepository.findByEmail(email);

        if (usuarioOptional.isPresent()) {
            UsuarioEntity usuario = usuarioOptional.get();
            if (usuario.getContrasena().equals(password)) {
                if (!usuario.getEstatus()) {
                    throw new IllegalArgumentException("La cuenta no esta activa");
                }
                return usuario;
            } else {
                throw new IllegalArgumentException("Contrasena no valida");
            }
        } else {
            throw new IllegalArgumentException("No se encontró un usuario");
        }
    }

    // para actualizar los datos de usuario en la pagina de perfil
    public UsuarioEntity actualizarDatos(UsuarioEntity usuario, UsuarioEntity usuarioNuevo) {
        String correoAnterior = usuario.getEmail();
        if (usuarioNuevo.getEmail().equals(correoAnterior)) {
            throw new IllegalArgumentException("El correo proporcionado es igual al actual.");
        }
        usuario.setEmail(usuarioNuevo.getEmail());
        usuario.setNombre(usuarioNuevo.getNombre());
        usuario.setContrasena(usuarioNuevo.getContrasena());
        try {
            return this.usuarioRepository.save(usuario);
        } catch (Exception e) {
            // aqui pueden haber varios motivos, el correo ingresado ya esta en uso, o hubo un error en la base de datos
            if (e.getMessage().contains("email")) {
                throw new IllegalArgumentException("Parece que ya existe una cuenta con este correo.");
            }

            throw new IllegalArgumentException("Hubo un error al actualizar los datos del usuario. Intenta de nuevo.");
        }
    }

    public void cambiarEstatus(UsuarioEntity usuario) {
        Optional<UsuarioEntity> usuarioEntityOptional = usuarioRepository.findById(usuario.getId());

        if (usuarioEntityOptional.isPresent()) {
            UsuarioEntity usuarioEntity = usuarioEntityOptional.get();
            usuarioEntity.setEstatus(!usuarioEntity.getEstatus());
            usuarioRepository.save(usuarioEntity);
        } else {
            throw new IllegalArgumentException("No se encontró el usuario");
        }
    }

    public List<UsuarioEntity> getAllProveedores() {
        return usuarioRepository.findAllByRol("proveedor");
    }
}
