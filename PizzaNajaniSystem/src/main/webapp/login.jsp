<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>Iniciar Sesión - Pizzería Najani</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/general/css/theme.css">
    </head>
    <body>
        <div class="login-container">
            <div class="login-card">
                <div class="login-header">
                    <div class="login-logo">
                        <i class="fas fa-pizza-slice"></i>
                    </div>
                    <h1>Pizzería Najani</h1>
                    <p>Sistema de Gestión</p>
                </div>

                <div class="login-body">
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fas fa-exclamation-circle"></i>
                            <span>${errorMessage}</span>
                        </div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm">
                        <div class="form-group">
                            <label for="username" class="form-label">
                                <i class="fas fa-user"></i> Usuario
                            </label>
                            <input 
                                type="text" 
                                class="form-control" 
                                id="username" 
                                name="username" 
                                placeholder="Ingrese su usuario"
                                value="${username != null ? username : ''}"
                                required
                                autocomplete="username"
                                autofocus>
                        </div>

                        <div class="form-group">
                            <label for="password" class="form-label">
                                <i class="fas fa-lock"></i> Contraseña
                            </label>
                            <input 
                                type="password" 
                                class="form-control" 
                                id="password" 
                                name="password" 
                                placeholder="Ingrese su contraseña"
                                required
                                autocomplete="current-password">
                        </div>

                        <button type="submit" class="btn-login" id="btnLogin">
                            <span id="btnText">Iniciar Sesión</span>
                        </button>
                    </form>
                </div>

                <div class="login-footer">
                    <p>&copy; 2025 Pizzería Najani. Todos los derechos reservados.</p>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

           <script src="${pageContext.request.contextPath}/assets/general/js/login.js"></script>

    </body>
</html>