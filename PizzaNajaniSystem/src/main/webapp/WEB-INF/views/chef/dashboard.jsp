<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard - Cocinero | Pizzería Najani</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">
    </head>
    <body>

        <aside class="sidebar">
            <div class="sidebar-header">
                <div class="logo">
                    <i class="fas fa-pizza-slice"></i>
                </div>
                <h2>Pizzería Najani</h2>
                <p>Panel de Cocina</p>
            </div>

            <nav class="sidebar-menu">
                <div class="menu-section">
                    <div class="menu-section-title">Principal</div>
                    <a href="#" class="menu-item active" data-section="dashboard">
                        <i class="fas fa-home"></i>
                        <span>Dashboard</span>
                    </a>
                </div>

                <div class="menu-section">
                    <div class="menu-section-title">Cocina</div>
                    <a href="#" class="menu-item" data-section="pedidos-pendientes">
                        <i class="fas fa-clock"></i>
                        <span>Pedidos Pendientes</span>
                    </a>
                    <a href="#" class="menu-item" data-section="en-preparacion">
                        <i class="fas fa-fire"></i>
                        <span>En Preparación</span>
                    </a>
                    <a href="#" class="menu-item" data-section="listos">
                        <i class="fas fa-check-circle"></i>
                        <span>Pedidos Listos</span>
                    </a>
                </div>
            </nav>
        </aside>

        <div class="main-wrapper">

            <header class="top-header">
                <div class="header-left">
                    <h1>Bienvenido, ${sessionScope.fullName}</h1>
                </div>

                <div class="header-right">
                    <div class="user-info">
                        <div class="user-avatar">
                            <i class="fas fa-user"></i>
                        </div>
                        <div class="user-details">
                            <h4>${sessionScope.fullName}</h4>
                            <p>Cocinero</p>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/logout" method="POST" style="margin: 0;">
                        <button type="submit" class="btn-logout">
                            <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                        </button>
                    </form>
                </div>
            </header>

            <main class="content-area">
                <div id="dashboard-section" class="content-section active">
                    <h2>Panel de Cocina</h2>
                    <p>Gestión de pedidos próximamente...</p>
                </div>
            </main>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>