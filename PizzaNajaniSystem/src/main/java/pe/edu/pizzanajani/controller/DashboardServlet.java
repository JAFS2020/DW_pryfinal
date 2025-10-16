package pe.edu.pizzanajani.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.edu.pizzanajani.config.AppConstants;
import pe.edu.pizzanajani.dao.*;
import pe.edu.pizzanajani.dao.impl.*;
import pe.edu.pizzanajani.model.User;

import java.io.IOException;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private UserDAO userDAO;
    private ProductDAO productDAO;
    private ProductCategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        productDAO = new ProductDAOImpl();
        categoryDAO = new ProductCategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(AppConstants.SESSION_USER) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute(AppConstants.SESSION_USER);
        String role = (String) session.getAttribute(AppConstants.SESSION_USER_ROLE);

        try {
            // Cargar estadísticas básicas
            int totalUsers = userDAO.findAll().size();
            int totalProducts = productDAO.findAll().size();
            int totalCategories = categoryDAO.findAll().size();

            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("totalCategories", totalCategories);

            if (role.equals(AppConstants.ROLE_ADMIN)) {
                request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
                       .forward(request, response);

            } else if (role.equals(AppConstants.ROLE_VENDEDOR)) {
                request.getRequestDispatcher("/WEB-INF/views/salesman/dashboard.jsp")
                       .forward(request, response);

            } else if (role.equals(AppConstants.ROLE_COCINERO)) {
                request.getRequestDispatcher("/WEB-INF/views/chef/dashboard.jsp")
                       .forward(request, response);

            } else {
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/login");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al cargar el dashboard");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}