-- =====================================================
-- SEED DATA - ROLES
-- =====================================================
INSERT INTO roles (role_name, description) VALUES
('ADMINISTRADOR', 'Administrador del sistema con acceso completo'),
('VENDEDOR', 'Vendedor que gestiona pedidos y clientes'),
('COCINERO', 'Cocinero que prepara los pedidos');

-- =====================================================
-- SEED DATA - USUARIOS
-- Password: todos tienen "Pizza123!" hasheado con bcrypt
-- En producción, usar bcrypt real con costo 10-12
-- =====================================================
INSERT INTO users (username, email, password_hash, full_name, phone, is_active) VALUES
('admin', 'admin@pizzanajani.pe', '$2a$10$rZJ8KJbM9xvLq5z7Y3H3uOXKj5L8N9mP0qR1sT2uV3wX4yZ5aB6cC', 'Administrador Najani', '999888777', TRUE),
('vendedor1', 'vendedor1@pizzanajani.pe', '$2a$10$rZJ8KJbM9xvLq5z7Y3H3uOXKj5L8N9mP0qR1sT2uV3wX4yZ5aB6cC', 'Ivonne Vendedor', '999888776', TRUE),
('vendedor2', 'vendedor2@pizzanajani.pe', '$2a$10$rZJ8KJbM9xvLq5z7Y3H3uOXKj5L8N9mP0qR1sT2uV3wX4yZ5aB6cC', 'Abel Ventas', '999888775', TRUE),
('cocinero1', 'cocinero1@pizzanajani.pe', '$2a$10$rZJ8KJbM9xvLq5z7Y3H3uOXKj5L8N9mP0qR1sT2uV3wX4yZ5aB6cC', 'José Cocinero', '999888774', TRUE),
('cocinero2', 'cocinero2@pizzanajani.pe', '$2a$10$rZJ8KJbM9xvLq5z7Y3H3uOXKj5L8N9mP0qR1sT2uV3wX4yZ5aB6cC', 'Nicole Chef', '999888773', TRUE);

-- =====================================================
-- SEED DATA - ASIGNACIÓN DE ROLES
-- =====================================================
INSERT INTO user_roles (user_id, role_id, assigned_by) VALUES
(1, 1, 1), -- admin tiene rol ADMINISTRADOR
(2, 2, 1), -- vendedor1 tiene rol VENDEDOR
(3, 2, 1), -- vendedor2 tiene rol VENDEDOR
(4, 3, 1), -- cocinero1 tiene rol COCINERO
(5, 3, 1); -- cocinero2 tiene rol COCINERO

-- =====================================================
-- SEED DATA - CATEGORÍAS DE PRODUCTOS
-- =====================================================
INSERT INTO product_categories (category_name, description, display_order, is_active) VALUES
('PIZZAS', 'Pizzas artesanales con masa fresca', 1, TRUE),
('BEBIDAS', 'Bebidas frías y calientes', 2, TRUE),
('POSTRES', 'Postres caseros', 3, TRUE),
('ENTRADAS', 'Entradas y aperitivos', 4, TRUE),
('ENSALADAS', 'Ensaladas frescas', 5, TRUE);

-- =====================================================
-- SEED DATA - PRODUCTOS
-- =====================================================
INSERT INTO products (category_id, product_code, product_name, description, price, cost, is_available, preparation_time) VALUES
-- PIZZAS
(1, 'PIZ001', 'Pizza Margarita', 'Salsa de tomate, mozzarella, albahaca fresca', 25.00, 12.00, TRUE, 15),
(1, 'PIZ002', 'Pizza Pepperoni', 'Salsa de tomate, mozzarella, pepperoni italiano', 30.00, 15.00, TRUE, 15),
(1, 'PIZ003', 'Pizza Hawaiana', 'Salsa de tomate, mozzarella, jamón, piña', 28.00, 14.00, TRUE, 15),
(1, 'PIZ004', 'Pizza Najani Especial', 'Salsa de tomate, mozzarella, pepperoni, champiñones, pimiento, aceitunas', 35.00, 18.00, TRUE, 20),
(1, 'PIZ005', 'Pizza Vegetariana', 'Salsa de tomate, mozzarella, verduras frescas de temporada', 27.00, 13.00, TRUE, 15),
(1, 'PIZ006', 'Pizza Cuatro Quesos', 'Mozzarella, parmesano, gorgonzola, provolone', 32.00, 16.00, TRUE, 15),

-- BEBIDAS
(2, 'BEB001', 'Coca Cola 500ml', 'Gaseosa Coca Cola', 3.50, 1.50, TRUE, 1),
(2, 'BEB002', 'Inca Kola 500ml', 'Gaseosa Inca Kola', 3.50, 1.50, TRUE, 1),
(2, 'BEB003', 'Agua Mineral 625ml', 'Agua mineral sin gas', 2.50, 1.00, TRUE, 1),
(2, 'BEB004', 'Limonada Frozen', 'Limonada frozen con hierbabuena', 6.00, 2.50, TRUE, 5),
(2, 'BEB005', 'Chicha Morada', 'Chicha morada natural 500ml', 5.00, 2.00, TRUE, 2),

-- POSTRES
(3, 'POS001', 'Tiramisú', 'Tiramisú italiano casero', 12.00, 5.00, TRUE, 5),
(3, 'POS002', 'Brownie con Helado', 'Brownie de chocolate con helado de vainilla', 10.00, 4.00, TRUE, 8),
(3, 'POS003', 'Cheesecake', 'Cheesecake de frutos rojos', 11.00, 4.50, TRUE, 5),

-- ENTRADAS
(4, 'ENT001', 'Pan al Ajo', 'Pan al ajo con queso gratinado', 8.00, 3.00, TRUE, 10),
(4, 'ENT002', 'Alitas BBQ', '10 alitas de pollo BBQ', 15.00, 7.00, TRUE, 15),
(4, 'ENT003', 'Tequeños', '8 unidades con salsa golf', 12.00, 5.00, TRUE, 12),

-- ENSALADAS
(5, 'ENS001', 'Ensalada César', 'Lechuga romana, pollo, parmesano, crutones', 14.00, 6.00, TRUE, 8),
(5, 'ENS002', 'Ensalada Caprese', 'Tomate, mozzarella, albahaca, aceite de oliva', 13.00, 5.50, TRUE, 5);

-- =====================================================
-- SEED DATA - INVENTARIO
-- =====================================================
INSERT INTO inventory (product_id, current_stock, min_stock, max_stock) VALUES
(1, 50, 10, 100), (2, 45, 10, 100), (3, 40, 10, 100),
(4, 35, 10, 100), (5, 38, 10, 100), (6, 30, 10, 100),
(7, 100, 20, 200), (8, 95, 20, 200), (9, 120, 30, 250),
(10, 50, 10, 100), (11, 60, 15, 150),
(12, 25, 5, 50), (13, 30, 5, 50), (14, 20, 5, 50),
(15, 40, 10, 80), (16, 45, 10, 80), (17, 50, 10, 80),
(18, 35, 8, 70), (19, 40, 8, 70);

-- =====================================================
-- SEED DATA - PEDIDOS DE EJEMPLO
-- =====================================================
INSERT INTO orders (order_number, customer_name, customer_phone, customer_address, created_by, order_status, order_type, subtotal, tax, delivery_fee, total, payment_method, payment_status, notes) VALUES
('ORD-20251016-001', 'Juan Pérez', '987654321', 'Av. Principal 123, Huánuco', 2, 'DELIVERED', 'DELIVERY', 72.00, 12.96, 5.00, 89.96, 'CASH', 'PAID', 'Sin cebolla en la pizza'),
('ORD-20251016-002', 'María García', '987654322', NULL, 2, 'READY', 'TAKEOUT', 39.00, 7.02, 0.00, 46.02, 'CARD', 'PAID', NULL),
('ORD-20251016-003', 'Pedro López', '987654323', 'Jr. Comercio 456, Huánuco', 3, 'IN_PROGRESS', 'DELIVERY', 62.00, 11.16, 5.00, 78.16, 'CASH', 'PENDING', 'Urgente'),
('ORD-20251016-004', 'Ana Torres', '987654324', NULL, 3, 'PENDING', 'DINE_IN', 35.00, 6.30, 0.00, 41.30, 'CASH', 'PENDING', NULL);

-- Actualizar assigned_cook y timestamps para pedidos en progreso
UPDATE orders SET assigned_cook = 4, started_at = NOW() WHERE order_id IN (2, 3);
UPDATE orders SET completed_at = DATE_SUB(NOW(), INTERVAL 30 MINUTE), delivered_at = DATE_SUB(NOW(), INTERVAL 15 MINUTE) WHERE order_id = 1;

-- =====================================================
-- SEED DATA - ITEMS DE PEDIDOS
-- =====================================================
-- Pedido 1: Pizza Najani Especial + Pizza Pepperoni + 2 Coca Cola
INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price) VALUES
(1, 4, 'Pizza Najani Especial', 1, 35.00),
(1, 2, 'Pizza Pepperoni', 1, 30.00),
(1, 7, 'Coca Cola 500ml', 2, 3.50);

-- Pedido 2: Pizza Vegetariana + 2 Limonada Frozen
INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price) VALUES
(2, 5, 'Pizza Vegetariana', 1, 27.00),
(2, 10, 'Limonada Frozen', 2, 6.00);

-- Pedido 3: Pizza Cuatro Quesos + Pizza Margarita + Chicha Morada
INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price) VALUES
(3, 6, 'Pizza Cuatro Quesos', 1, 32.00),
(3, 1, 'Pizza Margarita', 1, 25.00),
(3, 11, 'Chicha Morada', 1, 5.00);

-- Pedido 4: Pizza Hawaiana + 2 Inca Kola
INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price) VALUES
(4, 3, 'Pizza Hawaiana', 1, 28.00),
(4, 8, 'Inca Kola 500ml', 2, 3.50);

-- =====================================================
-- SEED DATA - LOGS DE AUDITORÍA
-- =====================================================
INSERT INTO audit_logs (user_id, action_type, table_name, record_id, ip_address) VALUES
(1, 'LOGIN', 'users', 1, '192.168.1.100'),
(2, 'LOGIN', 'users', 2, '192.168.1.101'),
(2, 'CREATE_ORDER', 'orders', 1, '192.168.1.101'),
(4, 'LOGIN', 'users', 4, '192.168.1.102'),
(4, 'UPDATE_ORDER_STATUS', 'orders', 1, '192.168.1.102');