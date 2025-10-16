package pe.edu.pizzanajani.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import pe.edu.pizzanajani.config.AppConstants;
import pe.edu.pizzanajani.dao.UserDAO;
import pe.edu.pizzanajani.dao.impl.UserDAOImpl;
import pe.edu.pizzanajani.model.User;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppConstants.SESSION_USER) != null) {
            response.sendRedirect(request.getContextPath() + AppConstants.PATH_DASHBOARD);
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Por favor, complete todos los campos");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = userDAO.validateCredentials(username.trim(), password);

            if (user != null) {

                userDAO.updateLastLogin(user.getUserId());

                HttpSession session = request.getSession(true);
                session.setAttribute(AppConstants.SESSION_USER, user);
                session.setAttribute(AppConstants.SESSION_USER_ID, user.getUserId());
                session.setAttribute(AppConstants.SESSION_USERNAME, user.getUsername());
                session.setAttribute(AppConstants.SESSION_FULL_NAME, user.getFullName());

                if (!user.getRoles().isEmpty()) {
                    session.setAttribute(AppConstants.SESSION_USER_ROLE,
                            user.getRoles().get(0).getRoleName());
                }

                session.setMaxInactiveInterval(AppConstants.SESSION_TIMEOUT * 60);

                response.sendRedirect(request.getContextPath() + AppConstants.PATH_DASHBOARD);

            } else {
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos");
                request.setAttribute("username", username);
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error del sistema. Por favor, intente más tarde.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
