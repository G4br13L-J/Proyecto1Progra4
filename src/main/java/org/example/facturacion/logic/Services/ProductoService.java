package org.example.facturacion.logic.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.facturacion.logic.Entities.ProductoEntity;
import org.example.facturacion.logic.Entities.UsuarioEntity;
import org.example.facturacion.data.Repositories.ProductoRepository;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;

    public ProductoService() {}

    // To get all products by provider
    public List<ProductoEntity> getProductosByProveedor(UsuarioEntity userLogged) {
        return productoRepository.findAllByIdProveedor(userLogged);
    }

    public List<ProductoEntity> searchProductsByName(UsuarioEntity userLogged, String searchName) {
        if (searchName == null || searchName.isEmpty()) {
            return getProductosByProveedor(userLogged);
        }

        return productoRepository.findAllByIdProveedorAndNombreContaining(userLogged, searchName);
    }

    public void guardarProducto(ProductoEntity newProduct) {
        productoRepository.save(newProduct);
    }

    public void eliminarProducto(Integer productId) {
        productoRepository.deleteById(productId);
    }

    public List<ProductoEntity> buscarProductosPorNombre(String nombre, UsuarioEntity sessionUser) {
        return productoRepository.findAllByIdProveedorAndNombreContaining(sessionUser, nombre);
    }

    public void editarProducto(ProductoEntity producto) {
        productoRepository.save(producto);
    }

    public ProductoEntity getProductoById(Integer productId) {
        return productoRepository.findById(productId).orElse(null);
    }

    public ProductoEntity getProductosByNombreAndProveedor(String nombreDelProducto, UsuarioEntity sessionUser) {
        return productoRepository.findByIdProveedorAndNombre(sessionUser, nombreDelProducto);
    }
}
