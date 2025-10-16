DROP DATABASE IF EXISTS pizza_najani_db;

CREATE DATABASE pizza_najani_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE pizza_najani_db;

-- =====================================================
-- TABLA: roles
-- Almacena los roles del sistema (administrador, vendedor, cocinero)
-- =====================================================
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_role_name (role_name)
) ENGINE=InnoDB COMMENT='Tabla de roles del sistema';

-- =====================================================
-- TABLA: users
-- Almacena información de usuarios del sistema
-- =====================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_active (is_active)
) ENGINE=InnoDB COMMENT='Tabla de usuarios del sistema';

-- =====================================================
-- TABLA: user_roles
-- Relación muchos a muchos entre usuarios y roles
-- =====================================================
CREATE TABLE user_roles (
    user_role_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    assigned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by INT,
    UNIQUE KEY unique_user_role (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB COMMENT='Relación usuarios-roles';

-- =====================================================
-- TABLA: product_categories
-- Categorías de productos (pizzas, bebidas, postres, etc.)
-- =====================================================
CREATE TABLE product_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category_name (category_name),
    INDEX idx_active_order (is_active, display_order)
) ENGINE=InnoDB COMMENT='Categorías de productos';

-- =====================================================
-- TABLA: products
-- Productos disponibles en el catálogo
-- =====================================================
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    product_code VARCHAR(20) NOT NULL UNIQUE,
    product_name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    cost DECIMAL(10, 2),
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    preparation_time INT DEFAULT 15 COMMENT 'Tiempo de preparación en minutos',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES product_categories(category_id) ON DELETE RESTRICT,
    INDEX idx_category (category_id),
    INDEX idx_product_code (product_code),
    INDEX idx_available (is_available),
    INDEX idx_name (product_name),
    CONSTRAINT chk_price_positive CHECK (price >= 0),
    CONSTRAINT chk_cost_positive CHECK (cost IS NULL OR cost >= 0)
) ENGINE=InnoDB COMMENT='Catálogo de productos';

-- =====================================================
-- TABLA: inventory
-- Control de inventario de productos
-- =====================================================
CREATE TABLE inventory (
    inventory_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL UNIQUE,
    current_stock INT DEFAULT 0,
    min_stock INT DEFAULT 5,
    max_stock INT DEFAULT 100,
    last_restock_date TIMESTAMP NULL,
    last_restock_quantity INT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id),
    INDEX idx_low_stock (current_stock, min_stock),
    CONSTRAINT chk_stock_non_negative CHECK (current_stock >= 0),
    CONSTRAINT chk_min_stock_valid CHECK (min_stock >= 0),
    CONSTRAINT chk_max_stock_valid CHECK (max_stock >= min_stock)
) ENGINE=InnoDB COMMENT='Inventario de productos';

-- =====================================================
-- TABLA: orders
-- Pedidos realizados
-- =====================================================
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(20) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    customer_phone VARCHAR(20),
    customer_address TEXT,
    created_by INT NOT NULL COMMENT 'Usuario vendedor que creó el pedido',
    order_status ENUM('PENDING', 'IN_PROGRESS', 'READY', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    order_type ENUM('DINE_IN', 'TAKEOUT', 'DELIVERY') DEFAULT 'DINE_IN',
    subtotal DECIMAL(10, 2) NOT NULL DEFAULT 0,
    tax DECIMAL(10, 2) NOT NULL DEFAULT 0,
    delivery_fee DECIMAL(10, 2) DEFAULT 0,
    total DECIMAL(10, 2) NOT NULL DEFAULT 0,
    payment_method ENUM('CASH', 'CARD', 'TRANSFER', 'OTHER') DEFAULT 'CASH',
    payment_status ENUM('PENDING', 'PAID', 'REFUNDED') DEFAULT 'PENDING',
    notes TEXT,
    assigned_cook INT COMMENT 'Cocinero asignado',
    started_at TIMESTAMP NULL COMMENT 'Inicio de preparación',
    completed_at TIMESTAMP NULL COMMENT 'Pedido completado',
    delivered_at TIMESTAMP NULL COMMENT 'Pedido entregado',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_cook) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_order_number (order_number),
    INDEX idx_status (order_status),
    INDEX idx_created_by (created_by),
    INDEX idx_assigned_cook (assigned_cook),
    INDEX idx_created_at (created_at),
    INDEX idx_status_created (order_status, created_at),
    CONSTRAINT chk_subtotal_positive CHECK (subtotal >= 0),
    CONSTRAINT chk_total_positive CHECK (total >= 0)
) ENGINE=InnoDB COMMENT='Pedidos de clientes';

-- =====================================================
-- TABLA: order_items
-- Detalle de items en cada pedido
-- =====================================================
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(100) NOT NULL COMMENT 'Nombre del producto al momento del pedido',
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL COMMENT 'Precio unitario al momento del pedido',
    subtotal DECIMAL(10, 2) NOT NULL,
    special_instructions TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    CONSTRAINT chk_item_quantity_positive CHECK (quantity > 0),
    CONSTRAINT chk_item_unit_price_positive CHECK (unit_price >= 0),
    CONSTRAINT chk_item_subtotal_positive CHECK (subtotal >= 0)
) ENGINE=InnoDB COMMENT='Detalle de items en pedidos';

-- =====================================================
-- TABLA: audit_logs
-- Registro de auditoría de operaciones críticas
-- =====================================================
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action_type VARCHAR(50) NOT NULL COMMENT 'LOGIN, LOGOUT, CREATE_ORDER, UPDATE_ORDER, etc.',
    table_name VARCHAR(50),
    record_id INT,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_action_type (action_type),
    INDEX idx_created_at (created_at),
    INDEX idx_table_record (table_name, record_id)
) ENGINE=InnoDB COMMENT='Registro de auditoría';

-- =====================================================
-- TABLA: sessions
-- Gestión de sesiones de usuario
-- =====================================================
CREATE TABLE sessions (
    session_id VARCHAR(128) PRIMARY KEY,
    user_id INT NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_expires_at (expires_at)
) ENGINE=InnoDB COMMENT='Sesiones activas de usuarios';

-- =====================================================
-- TRIGGERS
-- =====================================================

-- Trigger: Calcular subtotal de order_items antes de insertar
DELIMITER //
CREATE TRIGGER trg_order_items_before_insert
BEFORE INSERT ON order_items
FOR EACH ROW
BEGIN
    SET NEW.subtotal = NEW.quantity * NEW.unit_price;
END//
DELIMITER ;

-- Trigger: Calcular subtotal de order_items antes de actualizar
DELIMITER //
CREATE TRIGGER trg_order_items_before_update
BEFORE UPDATE ON order_items
FOR EACH ROW
BEGIN
    SET NEW.subtotal = NEW.quantity * NEW.unit_price;
END//
DELIMITER ;

-- Trigger: Actualizar total de orden después de insertar item
DELIMITER //
CREATE TRIGGER trg_update_order_total_after_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE orders 
    SET subtotal = (SELECT IFNULL(SUM(subtotal), 0) FROM order_items WHERE order_id = NEW.order_id),
        total = (SELECT IFNULL(SUM(subtotal), 0) FROM order_items WHERE order_id = NEW.order_id) + tax + IFNULL(delivery_fee, 0)
    WHERE order_id = NEW.order_id;
END//
DELIMITER ;

-- Trigger: Actualizar total de orden después de actualizar item
DELIMITER //
CREATE TRIGGER trg_update_order_total_after_update
AFTER UPDATE ON order_items
FOR EACH ROW
BEGIN
    UPDATE orders 
    SET subtotal = (SELECT IFNULL(SUM(subtotal), 0) FROM order_items WHERE order_id = NEW.order_id),
        total = (SELECT IFNULL(SUM(subtotal), 0) FROM order_items WHERE order_id = NEW.order_id) + tax + IFNULL(delivery_fee, 0)
    WHERE order_id = NEW.order_id;
END//
DELIMITER ;

-- Trigger: Actualizar total de orden después de eliminar item
DELIMITER //
CREATE TRIGGER trg_update_order_total_after_delete
AFTER DELETE ON order_items
FOR EACH ROW
BEGIN
    UPDATE orders 
    SET subtotal = (SELECT IFNULL(SUM(subtotal), 0) FROM order_items WHERE order_id = OLD.order_id),
        total = (SELECT IFNULL(SUM(subtotal), 0) FROM order_items WHERE order_id = OLD.order_id) + tax + IFNULL(delivery_fee, 0)
    WHERE order_id = OLD.order_id;
END//
DELIMITER ;

-- Trigger: Registrar auditoría al cambiar estado de orden
DELIMITER //
CREATE TRIGGER trg_audit_order_status_change
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.order_status != NEW.order_status THEN
        INSERT INTO audit_logs (user_id, action_type, table_name, record_id, old_value, new_value)
        VALUES (NEW.created_by, 'UPDATE_ORDER_STATUS', 'orders', NEW.order_id, OLD.order_status, NEW.order_status);
    END IF;
END//
DELIMITER ;

-- Trigger: Actualizar inventario al crear orden
DELIMITER //
CREATE TRIGGER trg_update_inventory_after_order
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE inventory 
    SET current_stock = current_stock - NEW.quantity
    WHERE product_id = NEW.product_id;
END//
DELIMITER ;