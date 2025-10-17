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

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/admin/css/dashboard.css">
    </head>
    <body>

        <div class="sidebar-overlay" id="sidebarOverlay"></div>

        <aside class="sidebar" id="sidebar">
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
                    <button class="menu-toggle" id="menuToggle">
                        <i class="fas fa-bars"></i>
                    </button>
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
                            <i class="fas fa-sign-out-alt"></i>
                            <span>Cerrar Sesión</span>
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
                        <button class="btn-primary" onclick="openCreateProductModal()">
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
                                    <th>Tiempo Prep.</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                            <tbody id="productsTableBody">
                                <tr>
                                    <td colspan="7" class="text-center">Cargando productos...</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="categorias-section" class="content-section">
                    <div class="section-header">
                        <h2>Gestión de Categorías</h2>
                        <button class="btn-primary" onclick="openCreateCategoryModal()">
                            <i class="fas fa-plus"></i> Nueva Categoría
                        </button>
                    </div>
                    <div class="table-container" id="categoriesContainer">
                        <p class="text-center text-muted">Cargando categorías...</p>
                    </div>
                </div>

                <div id="inventario-section" class="content-section">
                    <div class="section-header">
                        <h2>Control de Inventario</h2>
                    </div>
                    <div class="table-container" id="inventoryContainer">
                        <p class="text-center text-muted">Cargando inventario...</p>
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

        <div class="modal fade" id="userModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="userModalTitle">Nuevo Usuario</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="userForm">
                            <input type="hidden" id="userId" name="userId">
                            <input type="hidden" id="userAction" name="userAction" value="create">

                            <div class="mb-3">
                                <label for="username" class="form-label">Usuario <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="username" name="username" required>
                            </div>

                            <div class="mb-3">
                                <label for="fullName" class="form-label">Nombre Completo <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="fullName" name="fullName" required>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email <span class="text-danger">*</span></label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>

                            <div class="mb-3">
                                <label for="phone" class="form-label">Teléfono</label>
                                <input type="text" class="form-control" id="phone" name="phone">
                            </div>

                            <div class="mb-3" id="passwordField">
                                <label for="password" class="form-label">Contraseña <span class="text-danger">*</span></label>
                                <input type="password" class="form-control" id="password" name="password">
                                <small class="text-muted">Mínimo 6 caracteres</small>
                            </div>

                            <div class="mb-3">
                                <label for="roleId" class="form-label">Rol <span class="text-danger">*</span></label>
                                <select class="form-select" id="roleId" name="roleId" required>
                                    <option value="">Seleccione un rol</option>
                                    <option value="1">Administrador</option>
                                    <option value="2">Vendedor</option>
                                    <option value="3">Cocinero</option>
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveUser()">
                            <i class="fas fa-save"></i> Guardar
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="productModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="productModalTitle">Nuevo Producto</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="productForm">
                            <input type="hidden" id="productId" name="productId">
                            <input type="hidden" id="productAction" name="action" value="create">

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="productCode" class="form-label">Código</label>
                                    <input type="text" class="form-control" id="productCode" name="productCode" placeholder="PIZ001">
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="categoryIdProduct" class="form-label">Categoría <span class="text-danger">*</span></label>
                                    <select class="form-select" id="categoryIdProduct" name="categoryId" required>
                                        <option value="">Cargando categorías...</option>
                                    </select>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="productName" class="form-label">Nombre del Producto <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="productName" name="productName" required>
                            </div>

                            <div class="mb-3">
                                <label for="description" class="form-label">Descripción</label>
                                <textarea class="form-control" id="description" name="description" rows="2"></textarea>
                            </div>

                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="price" class="form-label">Precio (S/.) <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" id="price" name="price" step="0.01" min="0" required>
                                </div>

                                <div class="col-md-4 mb-3">
                                    <label for="cost" class="form-label">Costo (S/.)</label>
                                    <input type="number" class="form-control" id="cost" name="cost" step="0.01" min="0">
                                </div>

                                <div class="col-md-4 mb-3">
                                    <label for="preparationTime" class="form-label">Tiempo Prep. (min)</label>
                                    <input type="number" class="form-control" id="preparationTime" name="preparationTime" value="15" min="1">
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="imageUrl" class="form-label">URL de Imagen</label>
                                <input type="text" class="form-control" id="imageUrl" name="imageUrl" placeholder="https://...">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveProduct()">
                            <i class="fas fa-save"></i> Guardar
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="categoryModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="categoryModalTitle">Nueva Categoría</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="categoryForm">
                            <input type="hidden" id="categoryIdHidden" name="categoryId">
                            <input type="hidden" id="categoryAction" name="action" value="create">

                            <div class="mb-3">
                                <label for="categoryName" class="form-label">Nombre de la Categoría <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                            </div>

                            <div class="mb-3">
                                <label for="categoryDescription" class="form-label">Descripción</label>
                                <textarea class="form-control" id="categoryDescription" name="description" rows="3"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveCategory()">
                            <i class="fas fa-save"></i> Guardar
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="confirmModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header bg-warning">
                        <h5 class="modal-title">
                            <i class="fas fa-exclamation-triangle"></i> Confirmación
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p id="confirmMessage"></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-danger" id="confirmButton">
                            <i class="fas fa-check"></i> Confirmar
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="inventoryModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-box"></i> Reabastecer Inventario
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="inventoryForm">
                            <input type="hidden" id="inventoryProductId" name="productId">

                            <div class="mb-3">
                                <label class="form-label">Producto</label>
                                <input type="text" class="form-control" id="inventoryProductName" readonly>
                            </div>

                            <div class="row">
                                <div class="col-6 mb-3">
                                    <label class="form-label">Stock Actual</label>
                                    <input type="number" class="form-control" id="currentStockDisplay" readonly>
                                </div>

                                <div class="col-6 mb-3">
                                    <label class="form-label">Stock Mínimo</label>
                                    <input type="number" class="form-control" id="minStockDisplay" readonly>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="restockQuantity" class="form-label">
                                    Cantidad a Agregar <span class="text-danger">*</span>
                                </label>
                                <input type="number" class="form-control" id="restockQuantity" name="quantity" min="1" required>
                            </div>

                            <div class="alert alert-info">
                                <i class="fas fa-info-circle"></i>
                                Nuevo stock: <strong><span id="newStockPreview">0</span></strong>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-success" onclick="saveRestock()">
                            <i class="fas fa-check"></i> Reabastecer
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="editInventoryModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-edit"></i> Editar Inventario
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="editInventoryForm">
                            <input type="hidden" id="editInventoryId" name="inventoryId">

                            <div class="mb-3">
                                <label class="form-label">Producto</label>
                                <input type="text" class="form-control" id="editProductName" readonly>
                            </div>

                            <div class="mb-3">
                                <label for="editCurrentStock" class="form-label">
                                    Stock Actual <span class="text-danger">*</span>
                                </label>
                                <input type="number" class="form-control" id="editCurrentStock" name="currentStock" min="0" required>
                            </div>

                            <div class="row">
                                <div class="col-6 mb-3">
                                    <label for="editMinStock" class="form-label">
                                        Stock Mínimo <span class="text-danger">*</span>
                                    </label>
                                    <input type="number" class="form-control" id="editMinStock" name="minStock" min="0" required>
                                </div>

                                <div class="col-6 mb-3">
                                    <label for="editMaxStock" class="form-label">
                                        Stock Máximo <span class="text-danger">*</span>
                                    </label>
                                    <input type="number" class="form-control" id="editMaxStock" name="maxStock" min="0" required>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveInventoryEdit()">
                            <i class="fas fa-save"></i> Guardar
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div class="custom-alert-modal" id="customAlertModal">
            <div class="custom-alert-content">
                <div class="custom-alert-header">
                    <div class="custom-alert-icon" id="alertIcon">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <div class="custom-alert-text">
                        <h3 id="alertTitle">Éxito</h3>
                        <p id="alertSubtitle">Operación completada</p>
                    </div>
                </div>
                <div class="custom-alert-body">
                    <p class="custom-alert-message" id="alertMessage">
                        La operación se realizó correctamente.
                    </p>
                </div>
                <div class="custom-alert-footer">
                    <button class="custom-alert-btn success" id="alertButton" onclick="closeCustomAlert()">
                        Entendido
                    </button>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <script>
                        const contextPath = '${pageContext.request.contextPath}';
        </script>

        <script src="${pageContext.request.contextPath}/assets/admin/js/dashboard.js"></script>
    </body>
</html>