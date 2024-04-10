USE db_factura_electronica;

-- Datos de ejemplo para la tabla usuarios
INSERT INTO usuarios (nombre, email, contrasena, rol, estatus) VALUES
('Juan Pérez', 'juan@example.com', '1234', 'proveedor', TRUE),
('María García', 'maria@example.com', '1234', 'administrador', TRUE);

-- Datos de ejemplo para la tabla clientes
INSERT INTO clientes (id_proveedor, nombre, email) VALUES
(1, 'Cliente A', 'clienteA@example.com'),
(1, 'Cliente B', 'clienteB@example.com');

-- Datos de ejemplo para la tabla productos
INSERT INTO productos (id_proveedor, nombre, descripcion, precio) VALUES
(1, 'Producto X', 'Descripción del Producto X', 25.50),
(1, 'Producto Y', 'Descripción del Producto Y', 15.75);

-- Datos de ejemplo para la tabla facturas
INSERT INTO facturas (id_proveedor, id_cliente, fecha, total) VALUES
(1, 1, '2024-04-01', 60.00),
(1, 2, '2024-04-02', 31.25);

-- Datos de ejemplo para la tabla detalles_factura
INSERT INTO detalles_factura (id_factura, id_producto, cantidad, precio_unitario) VALUES
(1, 1, 2, 25.50),
(1, 2, 1, 15.75),
(2, 2, 2, 15.75);
