-- Crear la base de datos
CREATE DATABASE db_factura_electronica;
USE db_factura_electronica;

-- Tabla para almacenar los usuarios
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    rol ENUM('proveedor', 'administrador') NOT NULL,
    estatus BOOLEAN DEFAULT TRUE
);

-- Tabla para almacenar los clientes
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    FOREIGN KEY (id_proveedor) REFERENCES usuarios(id_usuario)
);

-- Tabla para almacenar los productos
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES usuarios(id_usuario)
);

-- Tabla para almacenar las facturas
CREATE TABLE facturas (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    id_cliente INT NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

-- Tabla para almacenar los detalles de las facturas
CREATE TABLE detalles_factura (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_factura INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);