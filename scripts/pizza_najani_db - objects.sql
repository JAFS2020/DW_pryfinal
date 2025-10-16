-- =====================================================
-- VISTAS
-- =====================================================

-- Vista: Pedidos con detalles completos
CREATE OR REPLACE VIEW vw_order_details AS
SELECT 
    o.order_id,
    o.order_number,
    o.customer_name,
    o.customer_phone,
    o.order_status,
    o.order_type,
    o.total,
    o.payment_status,
    o.created_at,
    u.full_name AS created_by_name,
    c.full_name AS assigned_cook_name,
    COUNT(oi.order_item_id) AS total_items
FROM orders o
INNER JOIN users u ON o.created_by = u.user_id
LEFT JOIN users c ON o.assigned_cook = c.user_id
LEFT JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY o.order_id, o.order_number, o.customer_name, o.customer_phone, 
         o.order_status, o.order_type, o.total, o.payment_status, 
         o.created_at, u.full_name, c.full_name;

-- Vista: Productos con inventario bajo
CREATE OR REPLACE VIEW vw_low_stock_products AS
SELECT 
    p.product_id,
    p.product_code,
    p.product_name,
    pc.category_name,
    i.current_stock,
    i.min_stock,
    (i.min_stock - i.current_stock) AS stock_needed
FROM products p
INNER JOIN product_categories pc ON p.category_id = pc.category_id
INNER JOIN inventory i ON p.product_id = i.product_id
WHERE i.current_stock <= i.min_stock
ORDER BY stock_needed DESC;

-- Vista: Resumen de ventas por producto
CREATE OR REPLACE VIEW vw_product_sales AS
SELECT 
    p.product_id,
    p.product_name,
    pc.category_name,
    COUNT(oi.order_item_id) AS times_ordered,
    SUM(oi.quantity) AS total_quantity_sold,
    SUM(oi.subtotal) AS total_revenue
FROM products p
INNER JOIN product_categories pc ON p.category_id = pc.category_id
LEFT JOIN order_items oi ON p.product_id = oi.product_id
LEFT JOIN orders o ON oi.order_id = o.order_id AND o.order_status != 'CANCELLED'
GROUP BY p.product_id, p.product_name, pc.category_name;

-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS
-- =====================================================

-- Procedimiento: Obtener pedidos pendientes para cocina
DELIMITER //
CREATE PROCEDURE sp_get_pending_orders_for_kitchen()
BEGIN
    SELECT 
        o.order_id,
        o.order_number,
        o.customer_name,
        o.order_type,
        o.order_status,
        o.created_at,
        o.notes,
        u.full_name AS created_by_name
    FROM orders o
    INNER JOIN users u ON o.created_by = u.user_id
    WHERE o.order_status IN ('PENDING', 'IN_PROGRESS')
    ORDER BY o.created_at ASC;
END//
DELIMITER ;

-- Procedimiento: Crear nuevo pedido
DELIMITER //
CREATE PROCEDURE sp_create_order(
    IN p_order_number VARCHAR(20),
    IN p_customer_name VARCHAR(100),
    IN p_customer_phone VARCHAR(20),
    IN p_customer_address TEXT,
    IN p_created_by INT,
    IN p_order_type VARCHAR(20),
    IN p_payment_method VARCHAR(20),
    IN p_notes TEXT,
    OUT p_order_id INT
)
BEGIN
    INSERT INTO orders (
        order_number, customer_name, customer_phone, customer_address,
        created_by, order_type, payment_method, notes
    ) VALUES (
        p_order_number, p_customer_name, p_customer_phone, p_customer_address,
        p_created_by, p_order_type, p_payment_method, p_notes
    );
    SET p_order_id = LAST_INSERT_ID();
END//
DELIMITER ;

-- Procedimiento: Actualizar estado de pedido
DELIMITER //
CREATE PROCEDURE sp_update_order_status(
    IN p_order_id INT,
    IN p_new_status VARCHAR(20),
    IN p_user_id INT
)
BEGIN
    DECLARE v_old_status VARCHAR(20);
    
    -- Obtener el estado actual
    SELECT order_status INTO v_old_status FROM orders WHERE order_id = p_order_id;
    
    -- Actualizar el estado y timestamps correspondientes
    UPDATE orders 
    SET order_status = p_new_status,
        started_at = CASE WHEN p_new_status = 'IN_PROGRESS' AND started_at IS NULL THEN NOW() ELSE started_at END,
        completed_at = CASE WHEN p_new_status = 'READY' AND completed_at IS NULL THEN NOW() ELSE completed_at END,
        delivered_at = CASE WHEN p_new_status = 'DELIVERED' AND delivered_at IS NULL THEN NOW() ELSE delivered_at END
    WHERE order_id = p_order_id;
    
    -- Registrar en auditoría
    INSERT INTO audit_logs (user_id, action_type, table_name, record_id, old_value, new_value)
    VALUES (p_user_id, 'UPDATE_ORDER_STATUS', 'orders', p_order_id, v_old_status, p_new_status);
END//
DELIMITER ;

-- Procedimiento: Reporte de ventas por período
DELIMITER //
CREATE PROCEDURE sp_sales_report(
    IN p_start_date DATE,
    IN p_end_date DATE
)
BEGIN
    SELECT 
        DATE(o.created_at) AS sale_date,
        COUNT(DISTINCT o.order_id) AS total_orders,
        SUM(o.subtotal) AS total_subtotal,
        SUM(o.tax) AS total_tax,
        SUM(o.total) AS total_sales,
        AVG(o.total) AS average_order_value,
        SUM(CASE WHEN o.payment_status = 'PAID' THEN o.total ELSE 0 END) AS total_paid,
        SUM(CASE WHEN o.payment_status = 'PENDING' THEN o.total ELSE 0 END) AS total_pending
    FROM orders o
    WHERE DATE(o.created_at) BETWEEN p_start_date AND p_end_date
        AND o.order_status != 'CANCELLED'
    GROUP BY DATE(o.created_at)
    ORDER BY sale_date DESC;
END//
DELIMITER ;

-- =====================================================
-- FUNCIONES
-- =====================================================

-- Función: Generar número de orden único
DELIMITER //
CREATE FUNCTION fn_generate_order_number()
RETURNS VARCHAR(20)
READS SQL DATA
BEGIN
    DECLARE v_order_number VARCHAR(20);
    DECLARE v_date VARCHAR(8);
    DECLARE v_counter INT;
    
    SET v_date = DATE_FORMAT(NOW(), '%Y%m%d');
    
    SELECT COUNT(*) + 1 INTO v_counter
    FROM orders
    WHERE DATE(created_at) = CURDATE();
    
    SET v_order_number = CONCAT('ORD-', v_date, '-', LPAD(v_counter, 3, '0'));
    
    RETURN v_order_number;
END//
DELIMITER ;

-- Función: Calcular tiempo estimado de preparación de un pedido
DELIMITER //
CREATE FUNCTION fn_calculate_preparation_time(p_order_id INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE v_total_time INT;
    
    SELECT SUM(p.preparation_time * oi.quantity) INTO v_total_time
    FROM order_items oi
    INNER JOIN products p ON oi.product_id = p.product_id
    WHERE oi.order_id = p_order_id;
    
    RETURN IFNULL(v_total_time, 0);
END//
DELIMITER ;

-- =====================================================
-- CONFIGURACIÓN DE SEGURIDAD
-- =====================================================

-- Crear usuario para la aplicación (solo si no existe)
CREATE USER IF NOT EXISTS 'pizza_najani_app'@'localhost' IDENTIFIED BY 'PizzaN@jani2025!';
GRANT SELECT, INSERT, UPDATE, DELETE ON pizza_najani_db.* TO 'pizza_najani_app'@'localhost';
GRANT EXECUTE ON pizza_najani_db.* TO 'pizza_najani_app'@'localhost';
FLUSH PRIVILEGES;

-- =====================================================
-- VERIFICACIÓN DE INTEGRIDAD
-- =====================================================

-- Verificar que todos los usuarios tienen al menos un rol
SELECT 'Usuarios sin rol asignado:' AS verificacion;
SELECT u.user_id, u.username, u.full_name
FROM users u
LEFT JOIN user_roles ur ON u.user_id = ur.user_id
WHERE ur.user_role_id IS NULL;

-- Verificar productos sin inventario
SELECT 'Productos sin inventario configurado:' AS verificacion;
SELECT p.product_id, p.product_name
FROM products p
LEFT JOIN inventory i ON p.product_id = i.product_id
WHERE i.inventory_id IS NULL;

-- =====================================================
-- ESTADÍSTICAS INICIALES
-- =====================================================

SELECT 'RESUMEN DE BASE DE DATOS' AS info;

SELECT 'Roles configurados' AS item, COUNT(*) AS cantidad FROM roles
UNION ALL
SELECT 'Usuarios registrados', COUNT(*) FROM users
UNION ALL
SELECT 'Categorías de productos', COUNT(*) FROM product_categories
UNION ALL
SELECT 'Productos en catálogo', COUNT(*) FROM products
UNION ALL
SELECT 'Items en inventario', COUNT(*) FROM inventory
UNION ALL
SELECT 'Pedidos totales', COUNT(*) FROM orders
UNION ALL
SELECT 'Items en pedidos', COUNT(*) FROM order_items
UNION ALL
SELECT 'Registros de auditoría', COUNT(*) FROM audit_logs;