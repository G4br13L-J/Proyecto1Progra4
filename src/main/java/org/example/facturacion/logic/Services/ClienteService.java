package org.example.facturacion.logic.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.facturacion.logic.Entities.ClienteEntity;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.data.Repositories.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public ClienteService() {}

    // para obtener todos los clientes de un proveedor
    public List<ClienteEntity> getClientesByProveedor(UsuarioEntity userLogged) {
        return clienteRepository.findAllByIdProveedor(userLogged);
    }

    public List<ClienteEntity> searchClientsByName(UsuarioEntity userLogged, String searchName) {
        if (searchName == null || searchName.isEmpty()) {
            return getClientesByProveedor(userLogged);
        }

        return clienteRepository.findAllByIdProveedorAndNombreContaining(userLogged, searchName);
    }

    public void guardarCliente(ClienteEntity newClient) {
        // Tal vez puedan comprobar algo que les interese
        clienteRepository.save(newClient);
    }

    public void eliminarCliente(Integer clienteId) {
        clienteRepository.deleteById(clienteId);
    }

    public ArrayList<ClienteEntity> buscarClientesPorNombre(String nombre, UsuarioEntity sessionUser) {
        return (ArrayList<ClienteEntity>) clienteRepository.findAllByIdProveedorAndNombreContaining(sessionUser, nombre);
    }

    public void editarCliente(ClienteEntity cliente) {
        clienteRepository.save(cliente);
    }

    public ClienteEntity getClienteById(Integer clienteId) {
        return clienteRepository.findById(clienteId).orElse(null);
    }
}

