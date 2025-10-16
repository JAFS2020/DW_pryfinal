<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard - Administrador | Pizzería Najani</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard.css">

        <style>
            .badge {
                padding: 6px 12px;
                border-radius: 12px;
                font-size: 0.85rem;
                font-weight: 500;
            }

            .badge-success {
                background: #E8F5E9 !important;
                color: #2E7D32 !important;
            }

            .badge-danger {
                background: #FFEBEE !important;
                color: #C62828 !important;
            }

            .bg-primary {
                background: #E3F2FD !important;
                color: #1565C0 !important;
            }

            .btn {
                padding: 6px 12px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                transition: all 0.3s ease;
                margin: 0 2px;
            }

            .btn-sm {
                padding: 4px 8px;
                font-size: 0.875rem;
            }

            .btn-info {
                background: #0288D1;
                color: white;
            }

            .btn-info:hover {
                background: #01579B;
                transform: translateY(-2px);
            }

            .btn-warning {
                background: #FFA000;
                color: white;
            }

            .btn-warning:hover {
                background: #F57C00;
                transform: translateY(-2px);
            }

            .btn-danger {
                background: #D32F2F;
                color: white;
            }

            .btn-danger:hover {
                background: #B71C1C;
                transform: translateY(-2px);
            }

            .btn-success {
                background: #388E3C;
                color: white;
            }

            .btn-success:hover {
                background: #2E7D32;
                transform: translateY(-2px);
            }

            .btn-secondary {
                background: #757575;
                color: white;
            }

            .btn-secondary:hover {
                background: #616161;
                transform: translateY(-2px);
            }

            .fa-spinner.fa-spin {
                animation: fa-spin 1s infinite linear;
            }

            @keyframes fa-spin {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }
        </style>
    </head>
    <body>

        <aside class="sidebar">
            <div class="sidebar-header">
                <div class="logo">
                    <i class="fas fa-pizza-slice"></i>
                </div>
                <h2>Pizzería Najani</h2>
                <p>Panel de Administración</p>
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
                    <div class="menu-section-title">Gestión</div>
                    <a href="#" class="menu-item" data-section="usuarios">
                        <i class="fas fa-users"></i>
                        <span>Usuarios</span>
                    </a>
                    <a href="#" class="menu-item" data-section="productos">
                        <i class="fas fa-box"></i>
                        <span>Productos</span>
                    </a>
                    <a href="#" class="menu-item" data-section="categorias">
                        <i class="fas fa-tags"></i>
                        <span>Categorías</span>
                    </a>
                    <a href="#" class="menu-item" data-section="inventario">
                        <i class="fas fa-warehouse"></i>
                        <span>Inventario</span>
                    </a>
                </div>

                <div class="menu-section">
                    <div class="menu-section-title">Reportes</div>
                    <a href="#" class="menu-item" data-section="ventas">
                        <i class="fas fa-chart-line"></i>
                        <span>Ventas</span>
                    </a>
                    <a href="#" class="menu-item" data-section="reportes">
                        <i class="fas fa-file-alt"></i>
                        <span>Reportes</span>
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
                            <p>${sessionScope.userRole}</p>
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
                    <div class="stats-grid">
                        <div class="stat-card">
                            <div class="stat-info">
                                <h3>${totalUsers}</h3>
                                <p>Usuarios Registrados</p>
                            </div>
                            <div class="stat-icon users">
                                <i class="fas fa-users"></i>
                            </div>
                        </div>

                        <div class="stat-card">
                            <div class="stat-info">
                                <h3>${totalProducts}</h3>
                                <p>Productos Activos</p>
                            </div>
                            <div class="stat-icon products">
                                <i class="fas fa-box"></i>
                            </div>
                        </div>

                        <div class="stat-card">
                            <div class="stat-info">
                                <h3>${totalCategories}</h3>
                                <p>Categorías</p>
                            </div>
                            <div class="stat-icon orders">
                                <i class="fas fa-tags"></i>
                            </div>
                        </div>

                        <div class="stat-card">
                            <div class="stat-info">
                                <h3>S/. 0.00</h3>
                                <p>Ventas del Día</p>
                            </div>
                            <div class="stat-icon revenue">
                                <i class="fas fa-dollar-sign"></i>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <div class="table-container">
                                <h3>Actividad Reciente</h3>
                                <p class="text-muted">Sistema funcionando correctamente</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div id="usuarios-section" class="content-section">
                    <div class="section-header">
                        <h2>Gestión de Usuarios</h2>
                        <button class="btn-primary" onclick="openCreateUserModal()">
                            <i class="fas fa-plus"></i> Nuevo Usuario
                        </button>
                    </div>

                    <div class="table-container">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Usuario</th>
                                    <th>Nombre Completo</th>
                                    <th>Email</th>
                                    <th>Rol</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody id="usersTableBody">
                                <tr>
                                    <td colspan="7" class="text-center">Cargando usuarios...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="productos-section" class="content-section">
                    <div class="section-header">
                        <h2>Gestión de Productos</h2>
                        <button class="btn-primary">
                            <i class="fas fa-plus"></i> Nuevo Producto
                        </button>
                    </div>

                    <div class="table-container">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Código</th>
                                    <th>Producto</th>
                                    <th>Categoría</th>
                                    <th>Precio</th>
                                    <th>Stock</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td colspan="7" class="text-center">Función de productos próximamente...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="categorias-section" class="content-section">
                    <div class="section-header">
                        <h2>Gestión de Categorías</h2>
                        <button class="btn-primary">
                            <i class="fas fa-plus"></i> Nueva Categoría
                        </button>
                    </div>
                    <div class="table-container">
                        <p class="text-center text-muted">Función próximamente...</p>
                    </div>
                </div>

                <div id="inventario-section" class="content-section">
                    <div class="section-header">
                        <h2>Control de Inventario</h2>
                    </div>
                    <div class="table-container">
                        <p class="text-center text-muted">Función próximamente...</p>
                    </div>
                </div>

                <div id="ventas-section" class="content-section">
                    <div class="section-header">
                        <h2>Reporte de Ventas</h2>
                    </div>
                    <div class="table-container">
                        <p class="text-center text-muted">Función próximamente...</p>
                    </div>
                </div>

                <div id="reportes-section" class="content-section">
                    <div class="section-header">
                        <h2>Reportes Generales</h2>
                    </div>
                    <div class="table-container">
                        <p class="text-center text-muted">Función próximamente...</p>
                    </div>
                </div>

            </main>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                            const contextPath = '${pageContext.request.contextPath}';

                            document.querySelectorAll('.menu-item').forEach(item => {
                                item.addEventListener('click', function (e) {
                                    e.preventDefault();

                                    document.querySelectorAll('.menu-item').forEach(i => i.classList.remove('active'));
                                    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));

                                    this.classList.add('active');
                                    const section = this.getAttribute('data-section');
                                    document.getElementById(section + '-section').classList.add('active');

                                    if (section === 'usuarios') {
                                        loadUsers();
                                    } else if (section === 'productos') {
                                        loadProducts();
                                    }
                                });
                            });

                            function loadUsers() {
                                const tbody = document.getElementById('usersTableBody');
                                tbody.innerHTML = '<tr><td colspan="7" class="text-center"><i class="fas fa-spinner fa-spin"></i> Cargando usuarios...</td></tr>';

                                fetch(contextPath + '/admin/users?action=listAll')
                                        .then(response => response.json())
                                        .then(data => {
                                            if (data.success) {
                                                displayUsers(data.data);
                                            } else {
                                                tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error: ' + data.message + '</td></tr>';
                                            }
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error al cargar usuarios</td></tr>';
                                        });
                            }

                            function displayUsers(users) {
                                const tbody = document.getElementById('usersTableBody');

                                if (users.length === 0) {
                                    tbody.innerHTML = '<tr><td colspan="7" class="text-center">No hay usuarios registrados</td></tr>';
                                    return;
                                }

                                let html = '';
                                users.forEach(user => {
                                    const roleName = user.roles && user.roles.length > 0 ? user.roles[0].roleName : 'Sin rol';
                                    const statusBadge = user.isActive
                                            ? '<span class="badge badge-success">Activo</span>'
                                            : '<span class="badge badge-danger">Inactivo</span>';

                                    html += '<tr>';
                                    html += '<td>' + user.userId + '</td>';
                                    html += '<td><strong>' + user.username + '</strong></td>';
                                    html += '<td>' + user.fullName + '</td>';
                                    html += '<td>' + user.email + '</td>';
                                    html += '<td><span class="badge bg-primary">' + roleName + '</span></td>';
                                    html += '<td>' + statusBadge + '</td>';
                                    html += '<td>';
                                    html += '<button class="btn btn-sm btn-info" onclick="viewUser(' + user.userId + ')" title="Ver">';
                                    html += '<i class="fas fa-eye"></i>';
                                    html += '</button>';
                                    html += '<button class="btn btn-sm btn-warning" onclick="editUser(' + user.userId + ')" title="Editar">';
                                    html += '<i class="fas fa-edit"></i>';
                                    html += '</button>';
                                    html += '<button class="btn btn-sm btn-' + (user.isActive ? 'secondary' : 'success') + '" ';
                                    html += 'onclick="toggleUserStatus(' + user.userId + ', ' + !user.isActive + ')" ';
                                    html += 'title="' + (user.isActive ? 'Desactivar' : 'Activar') + '">';
                                    html += '<i class="fas fa-' + (user.isActive ? 'ban' : 'check') + '"></i>';
                                    html += '</button>';
                                    if (user.userId !== 1) {
                                        html += '<button class="btn btn-sm btn-danger" onclick="deleteUser(' + user.userId + ')" title="Eliminar">';
                                        html += '<i class="fas fa-trash"></i>';
                                        html += '</button>';
                                    }
                                    html += '</td>';
                                    html += '</tr>';
                                });

                                tbody.innerHTML = html;
                            }

                            function viewUser(userId) {
                                fetch(contextPath + '/admin/users?action=findById&id=' + userId)
                                        .then(response => response.json())
                                        .then(data => {
                                            if (data.success) {
                                                const user = data.data;
                                                let message = 'INFORMACIÓN DEL USUARIO\n\n';
                                                message += 'Usuario: ' + user.username + '\n';
                                                message += 'Nombre: ' + user.fullName + '\n';
                                                message += 'Email: ' + user.email + '\n';
                                                message += 'Teléfono: ' + (user.phone || 'No registrado') + '\n';
                                                message += 'Estado: ' + (user.isActive ? 'Activo' : 'Inactivo');
                                                alert(message);
                                            } else {
                                                alert('Error: ' + data.message);
                                            }
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            alert('Error al cargar el usuario');
                                        });
                            }

                            function editUser(userId) {
                                alert('Función de editar próximamente. ID: ' + userId);
                            }

                            function toggleUserStatus(userId, newStatus) {
                                if (!confirm('¿Está seguro de cambiar el estado del usuario?')) {
                                    return;
                                }

                                const formData = new FormData();
                                formData.append('action', 'updateStatus');
                                formData.append('userId', userId);
                                formData.append('isActive', newStatus);

                                fetch(contextPath + '/admin/users', {
                                    method: 'POST',
                                    body: formData
                                })
                                        .then(response => response.json())
                                        .then(data => {
                                            if (data.success) {
                                                alert(data.message);
                                                loadUsers();
                                            } else {
                                                alert('Error: ' + data.message);
                                            }
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            alert('Error al cambiar el estado');
                                        });
                            }

                            function deleteUser(userId) {
                                if (!confirm('¿Está seguro de eliminar este usuario? Esta acción no se puede deshacer.')) {
                                    return;
                                }

                                const formData = new FormData();
                                formData.append('action', 'delete');
                                formData.append('userId', userId);

                                fetch(contextPath + '/admin/users', {
                                    method: 'POST',
                                    body: formData
                                })
                                        .then(response => response.json())
                                        .then(data => {
                                            if (data.success) {
                                                alert(data.message);
                                                loadUsers();
                                            } else {
                                                alert('Error: ' + data.message);
                                            }
                                        })
                                        .catch(error => {
                                            console.error('Error:', error);
                                            alert('Error al eliminar el usuario');
                                        });
                            }

                            function openCreateUserModal() {
                                alert('Modal de crear usuario próximamente...');
                            }


                            function loadProducts() {
                                console.log('Cargar productos...');
                            }
        </script>
    </body>
</html>