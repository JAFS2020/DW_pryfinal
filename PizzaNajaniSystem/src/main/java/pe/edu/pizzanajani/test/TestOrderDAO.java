package pe.edu.pizzanajani.test;

import pe.edu.pizzanajani.dao.OrderDAO;
import pe.edu.pizzanajani.dao.OrderItemDAO;
import pe.edu.pizzanajani.dao.impl.OrderDAOImpl;
import pe.edu.pizzanajani.dao.impl.OrderItemDAOImpl;

public class TestOrderDAO {
    
    public static void main(String[] args) {
        System.out.println("=== TEST DE ORDER DAO ===\n");
        
        OrderDAO orderDAO = new OrderDAOImpl();
        OrderItemDAO orderItemDAO = new OrderItemDAOImpl();
        
        try {
            System.out.println("1. Listar todas las órdenes:");
            var orders = orderDAO.findAll();
            System.out.println("   Total de órdenes: " + orders.size());
            orders.forEach(o -> {
                System.out.println("      - " + o.getOrderNumber() + 
                                 " | Cliente: " + o.getCustomerName() + 
                                 " | Estado: " + o.getOrderStatus() + 
                                 " | Total: S/. " + o.getTotal());
            });
            
            System.out.println("\n2. Buscar orden ID = 1:");
            var order = orderDAO.findById(1);
            if (order != null) {
                System.out.println("   Orden encontrada: " + order.getOrderNumber());
                System.out.println("   Cliente: " + order.getCustomerName());
                System.out.println("   Estado: " + order.getOrderStatus());
                System.out.println("   Total: S/. " + order.getTotal());
                System.out.println("   Items: " + order.getItems().size());
                
                System.out.println("   Productos:");
                order.getItems().forEach(item -> 
                    System.out.println("      - " + item.getProductName() + 
                                     " x" + item.getQuantity() + 
                                     " = S/. " + item.getSubtotal())
                );
            }
            
            System.out.println("\n3. Órdenes pendientes para cocina:");
            var pendingOrders = orderDAO.findPendingOrders();
            System.out.println("   Total pendientes: " + pendingOrders.size());
            pendingOrders.forEach(o -> 
                System.out.println("      - " + o.getOrderNumber() + 
                                 " | " + o.getCustomerName() + 
                                 " | Estado: " + o.getOrderStatus())
            );
            
            System.out.println("\n4. Órdenes entregadas:");
            var deliveredOrders = orderDAO.findByStatus("DELIVERED");
            System.out.println("   Total entregadas: " + deliveredOrders.size());
            
            System.out.println("\n5. Órdenes del día de hoy:");
            var todayOrders = orderDAO.findTodayOrders();
            System.out.println("   Total hoy: " + todayOrders.size());
            
            /*System.out.println("\n6. Generar número de orden:");
            String newOrderNumber = orderDAO.generateOrderNumber();
            System.out.println("   Nuevo número: " + newOrderNumber); */
            
            // Test - Crear una orden de prueba (opcional - descomenta para probar)
            /*
            System.out.println("\n7. Crear nueva orden:");
            Order newOrder = new Order();
            newOrder.setOrderNumber(newOrderNumber);
            newOrder.setCustomerName("Cliente Test");
            newOrder.setCustomerPhone("999999999");
            newOrder.setCreatedBy(2); // Vendedor
            newOrder.setOrderStatus("PENDING");
            newOrder.setOrderType("DINE_IN");
            newOrder.setSubtotal(new BigDecimal("25.00"));
            newOrder.setTax(new BigDecimal("4.50"));
            newOrder.setDeliveryFee(new BigDecimal("0.00"));
            newOrder.setTotal(new BigDecimal("29.50"));
            newOrder.setPaymentMethod("CASH");
            newOrder.setPaymentStatus("PENDING");
            
            Integer orderId = orderDAO.create(newOrder);
            if (orderId != null) {
                System.out.println("   Orden creada con ID: " + orderId);
                
                // Agregar item a la orden
                OrderItem item = new OrderItem();
                item.setOrderId(orderId);
                item.setProductId(1); // Pizza Margarita
                item.setProductName("Pizza Margarita");
                item.setQuantity(1);
                item.setUnitPrice(new BigDecimal("25.00"));
                
                Integer itemId = orderItemDAO.create(item);
                System.out.println("   Item agregado con ID: " + itemId);
            }
            */
            
            System.out.println("\nTodos los tests completados exitosamente");
            
        } catch (Exception e) {
            System.err.println("Error en test: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIN DEL TEST ===");
    }
}