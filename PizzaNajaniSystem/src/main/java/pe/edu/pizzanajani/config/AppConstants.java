package pe.edu.pizzanajani.config;

public class AppConstants {

    public static final String ROLE_ADMIN = "ADMINISTRADOR";
    public static final String ROLE_VENDEDOR = "VENDEDOR";
    public static final String ROLE_COCINERO = "COCINERO";

    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String ORDER_STATUS_READY = "READY";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

    public static final String ORDER_TYPE_DINE_IN = "DINE_IN";
    public static final String ORDER_TYPE_TAKEOUT = "TAKEOUT";
    public static final String ORDER_TYPE_DELIVERY = "DELIVERY";

    public static final String PAYMENT_CASH = "CASH";
    public static final String PAYMENT_CARD = "CARD";
    public static final String PAYMENT_TRANSFER = "TRANSFER";

    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_PAID = "PAID";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";

    public static final String SESSION_USER = "user";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_USERNAME = "username";
    public static final String SESSION_USER_ROLE = "userRole";
    public static final String SESSION_FULL_NAME = "fullName";

    public static final String MSG_SUCCESS = "success";
    public static final String MSG_ERROR = "error";
    public static final String MSG_WARNING = "warning";
    public static final String MSG_INFO = "info";

    public static final String PATH_LOGIN = "/login.jsp";
    public static final String PATH_DASHBOARD = "/dashboard";
    public static final String PATH_ADMIN_HOME = "/dashboard";
    public static final String PATH_VENDEDOR_HOME = "/dashboard";
    public static final String PATH_COCINERO_HOME = "/dashboard";

    public static final int SESSION_TIMEOUT = 30;
    public static final double TAX_RATE = 0.18;
    public static final double DELIVERY_FEE = 5.00;
}
