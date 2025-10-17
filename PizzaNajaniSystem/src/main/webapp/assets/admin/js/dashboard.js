let userModalInstance, productModalInstance, categoryModalInstance, confirmModalInstance, inventoryModalInstance, editInventoryModalInstance;

document.addEventListener('DOMContentLoaded', function () {
    userModalInstance = new bootstrap.Modal(document.getElementById('userModal'));
    productModalInstance = new bootstrap.Modal(document.getElementById('productModal'));
    categoryModalInstance = new bootstrap.Modal(document.getElementById('categoryModal'));
    confirmModalInstance = new bootstrap.Modal(document.getElementById('confirmModal'));
    inventoryModalInstance = new bootstrap.Modal(document.getElementById('inventoryModal'));
    editInventoryModalInstance = new bootstrap.Modal(document.getElementById('editInventoryModal'));

    document.getElementById('restockQuantity').addEventListener('input', function () {
        const currentStock = parseInt(document.getElementById('currentStockDisplay').value) || 0;
        const quantity = parseInt(this.value) || 0;
        document.getElementById('newStockPreview').textContent = currentStock + quantity;
    });
});

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
        } else if (section === 'categorias') {
            loadCategories();
        } else if (section === 'inventario') {
            loadInventory();
        }
    });
});

function showConfirmModal(message, onConfirm) {
    document.getElementById('confirmMessage').textContent = message;
    const confirmBtn = document.getElementById('confirmButton');

    const newBtn = confirmBtn.cloneNode(true);
    confirmBtn.parentNode.replaceChild(newBtn, confirmBtn);

    newBtn.addEventListener('click', function () {
        confirmModalInstance.hide();
        onConfirm();
    });

    confirmModalInstance.show();
}

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
        const statusBadge = user.isActive ? '<span class="badge badge-success">Activo</span>' : '<span class="badge badge-danger">Inactivo</span>';

        html += '<tr>';
        html += '<td>' + user.userId + '</td>';
        html += '<td><strong>' + user.username + '</strong></td>';
        html += '<td>' + user.fullName + '</td>';
        html += '<td>' + user.email + '</td>';
        html += '<td><span class="badge bg-primary">' + roleName + '</span></td>';
        html += '<td>' + statusBadge + '</td>';
        html += '<td>';
        html += '<button class="btn btn-sm btn-warning" onclick="editUser(' + user.userId + ')" title="Editar">';
        html += '<i class="fas fa-edit"></i>';
        html += '</button>';
        html += '<button class="btn btn-sm btn-' + (user.isActive ? 'secondary' : 'success') + '" ';
        html += 'onclick="showConfirmModal(\'¿Cambiar estado del usuario?\', () => toggleUserStatus(' + user.userId + ', ' + !user.isActive + '))" ';
        html += 'title="' + (user.isActive ? 'Desactivar' : 'Activar') + '">';
        html += '<i class="fas fa-' + (user.isActive ? 'ban' : 'check') + '"></i>';
        html += '</button>';
        if (user.userId !== 1) {
            html += '<button class="btn btn-sm btn-danger" onclick="showConfirmModal(\'¿Eliminar este usuario permanentemente?\', () => deleteUser(' + user.userId + '))" title="Eliminar">';
            html += '<i class="fas fa-trash"></i>';
            html += '</button>';
        }
        html += '</td>';
        html += '</tr>';
    });
    tbody.innerHTML = html;
}

function openCreateUserModal() {
    document.getElementById('userForm').reset();
    document.getElementById('userId').value = '';
    document.getElementById('userAction').value = 'create';
    document.getElementById('userModalTitle').textContent = 'Nuevo Usuario';
    document.getElementById('username').readOnly = false;
    document.getElementById('passwordField').style.display = 'block';
    document.getElementById('password').required = true;
    userModalInstance.show();
}

function editUser(userId) {
    fetch(contextPath + '/admin/users?action=findById&id=' + userId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const user = data.data;
                    document.getElementById('userId').value = user.userId;
                    document.getElementById('username').value = user.username;
                    document.getElementById('username').readOnly = true;
                    document.getElementById('fullName').value = user.fullName;
                    document.getElementById('email').value = user.email;
                    document.getElementById('phone').value = user.phone || '';

                    if (user.roles && user.roles.length > 0) {
                        document.getElementById('roleId').value = user.roles[0].roleId;
                    }

                    document.getElementById('userAction').value = 'update';
                    document.getElementById('userModalTitle').textContent = 'Editar Usuario';
                    document.getElementById('passwordField').style.display = 'none';
                    document.getElementById('password').required = false;
                    userModalInstance.show();
                } else {
                    showCustomAlert('error', 'load', 'user');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'load', 'user');
            });
}

function saveUser() {
    const form = document.getElementById('userForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const action = document.getElementById('userAction').value;
    const params = new URLSearchParams();
    params.append('action', action);

    if (action === 'update') {
        params.append('userId', document.getElementById('userId').value);
    }

    params.append('username', document.getElementById('username').value);
    params.append('fullName', document.getElementById('fullName').value);
    params.append('email', document.getElementById('email').value);
    params.append('phone', document.getElementById('phone').value);
    params.append('roleId', document.getElementById('roleId').value);

    if (action === 'create' || document.getElementById('password').value) {
        params.append('password', document.getElementById('password').value);
    }

    fetch(contextPath + '/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('success', action, 'user');
                    userModalInstance.hide();
                    loadUsers();
                    form.reset();
                    document.getElementById('username').readOnly = false;
                } else {
                    showCustomAlert('error', 'error', 'user');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'user');
            });
}

function toggleUserStatus(userId, newStatus) {
    const params = new URLSearchParams();
    params.append('action', 'updateStatus');
    params.append('userId', userId);
    params.append('isActive', newStatus);

    fetch(contextPath + '/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert(newStatus ? 'success' : 'warning', newStatus ? 'activate' : 'deactivate', 'user');
                    loadUsers();
                } else {
                    showCustomAlert('error', 'error', 'user');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'user');
            });
}

function deleteUser(userId) {
    const params = new URLSearchParams();
    params.append('action', 'delete');
    params.append('userId', userId);

    fetch(contextPath + '/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('delete', 'delete', 'user');
                    loadUsers();
                } else {
                    showCustomAlert('error', 'error', 'user');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'user');
            });
}

function loadProducts() {
    const tbody = document.getElementById('productsTableBody');
    tbody.innerHTML = '<tr><td colspan="7" class="text-center"><i class="fas fa-spinner fa-spin"></i> Cargando productos...</td></tr>';

    fetch(contextPath + '/admin/products?action=listAll')
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    displayProducts(data.data);
                } else {
                    tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error: ' + data.message + '</td></tr>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                tbody.innerHTML = '<tr><td colspan="7" class="text-center text-danger">Error al cargar productos</td></tr>';
            });
}

function displayProducts(products) {
    const tbody = document.getElementById('productsTableBody');
    if (products.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center">No hay productos registrados</td></tr>';
        return;
    }

    let html = '';
    products.forEach(product => {
        const categoryName = product.categoryName || 'Sin categoría';
        const statusBadge = product.isAvailable ? '<span class="badge badge-success">Disponible</span>' : '<span class="badge badge-danger">No disponible</span>';
        const price = product.price ? 'S/. ' + parseFloat(product.price).toFixed(2) : 'N/A';

        html += '<tr>';
        html += '<td><strong>' + (product.productCode || '#' + product.productId) + '</strong></td>';
        html += '<td>' + product.productName + '</td>';
        html += '<td><span class="badge bg-primary">' + categoryName + '</span></td>';
        html += '<td>' + price + '</td>';
        html += '<td>' + (product.preparationTime || 15) + ' min</td>';
        html += '<td>' + statusBadge + '</td>';
        html += '<td>';
        html += '<button class="btn btn-sm btn-warning" onclick="editProduct(' + product.productId + ')" title="Editar">';
        html += '<i class="fas fa-edit"></i>';
        html += '</button>';
        html += '<button class="btn btn-sm btn-' + (product.isAvailable ? 'secondary' : 'success') + '" ';
        html += 'onclick="showConfirmModal(\'¿Cambiar disponibilidad del producto?\', () => toggleProductAvailability(' + product.productId + ', ' + !product.isAvailable + '))" ';
        html += 'title="' + (product.isAvailable ? 'Desactivar' : 'Activar') + '">';
        html += '<i class="fas fa-' + (product.isAvailable ? 'ban' : 'check') + '"></i>';
        html += '</button>';
        html += '<button class="btn btn-sm btn-danger" onclick="showConfirmModal(\'¿Eliminar este producto permanentemente?\', () => deleteProduct(' + product.productId + '))" title="Eliminar">';
        html += '<i class="fas fa-trash"></i>';
        html += '</button>';
        html += '</td>';
        html += '</tr>';
    });
    tbody.innerHTML = html;
}

function openCreateProductModal() {
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';
    document.getElementById('productAction').value = 'create';
    document.getElementById('productModalTitle').textContent = 'Nuevo Producto';
    loadCategoriesForSelect();
    productModalInstance.show();
}

function editProduct(productId) {
    fetch(contextPath + '/admin/products?action=findById&id=' + productId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const product = data.data;
                    document.getElementById('productId').value = product.productId;
                    document.getElementById('productCode').value = product.productCode || '';
                    document.getElementById('productName').value = product.productName;
                    document.getElementById('description').value = product.description || '';
                    document.getElementById('price').value = product.price;
                    document.getElementById('cost').value = product.cost || '';
                    document.getElementById('preparationTime').value = product.preparationTime || 15;
                    document.getElementById('imageUrl').value = product.imageUrl || '';
                    document.getElementById('productAction').value = 'update';
                    document.getElementById('productModalTitle').textContent = 'Editar Producto';

                    loadCategoriesForSelect(product.categoryId);
                    productModalInstance.show();
                }
            });
}

function saveProduct() {
    const form = document.getElementById('productForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const action = document.getElementById('productAction').value;
    const params = new URLSearchParams();

    params.append('action', action);

    if (action === 'update') {
        params.append('productId', document.getElementById('productId').value);
    }

    params.append('productCode', document.getElementById('productCode').value);
    params.append('categoryId', document.getElementById('categoryIdProduct').value);
    params.append('productName', document.getElementById('productName').value);
    params.append('description', document.getElementById('description').value);
    params.append('price', document.getElementById('price').value);
    params.append('cost', document.getElementById('cost').value);
    params.append('preparationTime', document.getElementById('preparationTime').value);
    params.append('imageUrl', document.getElementById('imageUrl').value);

    fetch(contextPath + '/admin/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('success', action, 'product');
                    productModalInstance.hide();
                    loadProducts();
                    form.reset();
                } else {
                    showCustomAlert('error', 'error', 'product');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'product');
            });
}

function toggleProductAvailability(productId, newStatus) {
    const params = new URLSearchParams();
    params.append('action', 'updateAvailability');
    params.append('productId', productId);
    params.append('isAvailable', newStatus);

    fetch(contextPath + '/admin/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert(newStatus ? 'success' : 'warning', newStatus ? 'activate' : 'deactivate', 'product');
                    loadProducts();
                } else {
                    showCustomAlert('error', 'error', 'product');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'product');
            });
}

function deleteProduct(productId) {
    const params = new URLSearchParams();
    params.append('action', 'delete');
    params.append('productId', productId);

    fetch(contextPath + '/admin/products', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('delete', 'delete', 'product');
                    loadProducts();
                } else {
                    showCustomAlert('error', 'error', 'product');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'product');
            });
}

function loadCategoriesForSelect(selectedId = null) {
    const select = document.getElementById('categoryIdProduct');
    select.innerHTML = '<option value="">Cargando...</option>';

    fetch(contextPath + '/admin/categories?action=listActive')
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    let options = '<option value="">Seleccione una categoría</option>';
                    data.data.forEach(category => {
                        const selected = selectedId && category.categoryId == selectedId ? 'selected' : '';
                        options += '<option value="' + category.categoryId + '" ' + selected + '>' + category.categoryName + '</option>';
                    });
                    select.innerHTML = options;
                } else {
                    select.innerHTML = '<option value="">Error al cargar categorías</option>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                select.innerHTML = '<option value="">Error al cargar categorías</option>';
            });
}

function loadCategories() {
    const container = document.getElementById('categoriesContainer');
    container.innerHTML = '<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Cargando categorías...</div>';

    fetch(contextPath + '/admin/categories?action=listAll')
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    displayCategories(data.data);
                } else {
                    container.innerHTML = '<div class="text-center text-danger">Error: ' + data.message + '</div>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                container.innerHTML = '<div class="text-center text-danger">Error al cargar categorías</div>';
            });
}

function displayCategories(categories) {
    const container = document.getElementById('categoriesContainer');
    if (categories.length === 0) {
        container.innerHTML = '<p class="text-center text-muted">No hay categorías registradas</p>';
        return;
    }

    let html = '<table class="data-table">';
    html += '<thead><tr>';
    html += '<th>ID</th><th>Categoría</th><th>Descripción</th><th>Estado</th><th>Acciones</th>';
    html += '</tr></thead><tbody>';

    categories.forEach(category => {
        const statusBadge = category.isActive ? '<span class="badge badge-success">Activo</span>' : '<span class="badge badge-danger">Inactivo</span>';
        html += '<tr>';
        html += '<td><strong>#' + category.categoryId + '</strong></td>';
        html += '<td>' + category.categoryName + '</td>';
        html += '<td>' + (category.description || 'Sin descripción') + '</td>';
        html += '<td>' + statusBadge + '</td>';
        html += '<td>';
        html += '<button class="btn btn-sm btn-warning" onclick="editCategory(' + category.categoryId + ')" title="Editar">';
        html += '<i class="fas fa-edit"></i>';
        html += '</button>';
        html += '<button class="btn btn-sm btn-' + (category.isActive ? 'secondary' : 'success') + '" ';
        html += 'onclick="showConfirmModal(\'¿Cambiar estado de la categoría?\', () => toggleCategoryStatus(' + category.categoryId + ', ' + !category.isActive + '))" ';
        html += 'title="' + (category.isActive ? 'Desactivar' : 'Activar') + '">';
        html += '<i class="fas fa-' + (category.isActive ? 'ban' : 'check') + '"></i>';
        html += '</button>';
        html += '<button class="btn btn-sm btn-danger" onclick="showConfirmModal(\'¿Eliminar esta categoría permanentemente?\', () => deleteCategory(' + category.categoryId + '))" title="Eliminar">';
        html += '<i class="fas fa-trash"></i>';
        html += '</button>';
        html += '</td>';
        html += '</tr>';
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

function openCreateCategoryModal() {
    document.getElementById('categoryForm').reset();
    document.getElementById('categoryIdHidden').value = '';
    document.getElementById('categoryAction').value = 'create';
    document.getElementById('categoryModalTitle').textContent = 'Nueva Categoría';
    categoryModalInstance.show();
}

function editCategory(categoryId) {
    fetch(contextPath + '/admin/categories?action=findById&id=' + categoryId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const category = data.data;
                    document.getElementById('categoryIdHidden').value = category.categoryId;
                    document.getElementById('categoryName').value = category.categoryName;
                    document.getElementById('categoryDescription').value = category.description || '';
                    document.getElementById('categoryAction').value = 'update';
                    document.getElementById('categoryModalTitle').textContent = 'Editar Categoría';
                    categoryModalInstance.show();
                }
            });
}

function saveCategory() {
    const form = document.getElementById('categoryForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const action = document.getElementById('categoryAction').value;
    const params = new URLSearchParams();

    params.append('action', action);

    if (action === 'update') {
        params.append('categoryId', document.getElementById('categoryIdHidden').value);
    }

    params.append('categoryName', document.getElementById('categoryName').value);
    params.append('description', document.getElementById('categoryDescription').value);

    fetch(contextPath + '/admin/categories', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('success', action, 'category');
                    categoryModalInstance.hide();
                    loadCategories();
                    form.reset();
                } else {
                    showCustomAlert('error', 'error', 'category');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'category');
            });
}

function toggleCategoryStatus(categoryId, newStatus) {
    const params = new URLSearchParams();
    params.append('action', 'updateStatus');
    params.append('categoryId', categoryId);
    params.append('isActive', newStatus);

    fetch(contextPath + '/admin/categories', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert(newStatus ? 'success' : 'warning', newStatus ? 'activate' : 'deactivate', 'category');
                    loadCategories();
                } else {
                    showCustomAlert('error', 'error', 'category');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'category');
            });
}

function deleteCategory(categoryId) {
    const params = new URLSearchParams();
    params.append('action', 'delete');
    params.append('categoryId', categoryId);

    fetch(contextPath + '/admin/categories', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('delete', 'delete', 'category');
                    loadCategories();
                } else {
                    showCustomAlert('error', 'error', 'category');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'category');
            });
}

function loadInventory() {
    const container = document.getElementById('inventoryContainer');
    container.innerHTML = '<div class="text-center"><i class="fas fa-spinner fa-spin"></i> Cargando inventario...</div>';

    fetch(contextPath + '/admin/inventory?action=listAll')
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    displayInventory(data.data);
                } else {
                    container.innerHTML = '<div class="text-center text-danger">Error: ' + data.message + '</div>';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                container.innerHTML = '<div class="text-center text-danger">Error al cargar inventario</div>';
            });
}

function displayInventory(inventories) {
    const container = document.getElementById('inventoryContainer');
    if (inventories.length === 0) {
        container.innerHTML = '<p class="text-center text-muted">No hay inventario registrado</p>';
        return;
    }

    const lowStock = inventories.filter(inv => inv.isLowStock);
    let html = '';

    if (lowStock.length > 0) {
        html += '<div class="alert alert-warning mb-4">';
        html += '<h5><i class="fas fa-exclamation-triangle"></i> Productos con Stock Bajo (' + lowStock.length + ')</h5>';
        html += '<ul class="mb-0">';
        lowStock.forEach(inv => {
            html += '<li><strong>' + inv.productName + '</strong>: ' + inv.currentStock + ' unidades (Mínimo: ' + inv.minStock + ')</li>';
        });
        html += '</ul>';
        html += '</div>';
    }

    html += '<table class="data-table">';
    html += '<thead><tr>';
    html += '<th>Código</th><th>Producto</th><th>Stock Actual</th><th>Stock Mín.</th><th>Stock Máx.</th><th>Estado</th><th>Acciones</th>';
    html += '</tr></thead><tbody>';

    inventories.forEach(inventory => {
        const code = inventory.productCode || '#' + inventory.productId;
        const statusClass = inventory.isLowStock ? 'badge-danger' : 'badge-success';
        const statusText = inventory.isLowStock ? 'Stock Bajo' : 'Stock Normal';

        html += '<tr' + (inventory.isLowStock ? ' style="background: #FFF3E0;"' : '') + '>';
        html += '<td><strong>' + code + '</strong></td>';
        html += '<td>' + inventory.productName + '</td>';
        html += '<td><strong>' + inventory.currentStock + '</strong></td>';
        html += '<td>' + inventory.minStock + '</td>';
        html += '<td>' + inventory.maxStock + '</td>';
        html += '<td><span class="badge ' + statusClass + '">' + statusText + '</span></td>';
        html += '<td>';
        html += '<button class="btn btn-sm btn-success" onclick="openRestockModal(' + inventory.productId + ')" title="Reabastecer">';
        html += '<i class="fas fa-box"></i>';
        html += '</button>';
        html += '<button class="btn btn-sm btn-warning" onclick="openEditInventoryModal(' + inventory.inventoryId + ')" title="Editar">';
        html += '<i class="fas fa-edit"></i>';
        html += '</button>';
        html += '</td>';
        html += '</tr>';
    });

    html += '</tbody></table>';
    container.innerHTML = html;
}

function openRestockModal(productId) {
    fetch(contextPath + '/admin/inventory?action=findByProduct&productId=' + productId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const inventory = data.data;
                    document.getElementById('inventoryProductId').value = inventory.productId;
                    document.getElementById('inventoryProductName').value = inventory.productName;
                    document.getElementById('currentStockDisplay').value = inventory.currentStock;
                    document.getElementById('minStockDisplay').value = inventory.minStock;
                    document.getElementById('restockQuantity').value = '';
                    document.getElementById('newStockPreview').textContent = inventory.currentStock;
                    inventoryModalInstance.show();
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al cargar el inventario');
            });
}

function saveRestock() {
    const quantity = document.getElementById('restockQuantity').value;

    if (!quantity || quantity < 1) {
        showCustomAlert('error', 'error', 'inventory');
        return;
    }

    const productId = document.getElementById('inventoryProductId').value;

    const params = new URLSearchParams();
    params.append('action', 'restock');
    params.append('productId', productId);
    params.append('quantity', quantity);

    fetch(contextPath + '/admin/inventory', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('success', 'restock', 'inventory');
                    inventoryModalInstance.hide();
                    loadInventory();
                } else {
                    showCustomAlert('error', 'error', 'inventory');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'inventory');
            });
}

function openEditInventoryModal(inventoryId) {
    fetch(contextPath + '/admin/inventory?action=findById&id=' + inventoryId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    const inventory = data.data;
                    document.getElementById('editInventoryId').value = inventory.inventoryId;
                    document.getElementById('editProductName').value = inventory.productName;
                    document.getElementById('editCurrentStock').value = inventory.currentStock;
                    document.getElementById('editMinStock').value = inventory.minStock;
                    document.getElementById('editMaxStock').value = inventory.maxStock;
                    editInventoryModalInstance.show();
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error al cargar el inventario');
            });
}

function saveInventoryEdit() {
    const form = document.getElementById('editInventoryForm');
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const params = new URLSearchParams(new FormData(form));
    params.append('action', 'update');

    fetch(contextPath + '/admin/inventory', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    showCustomAlert('success', 'update', 'inventory');
                    editInventoryModalInstance.hide();
                    loadInventory();
                } else {
                    showCustomAlert('error', 'error', 'inventory');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showCustomAlert('error', 'error', 'inventory');
            });
}

function showCustomAlert(type, action, entity) {
    const modal = document.getElementById('customAlertModal');
    const icon = document.getElementById('alertIcon');
    const iconElement = icon.querySelector('i');
    const title = document.getElementById('alertTitle');
    const subtitle = document.getElementById('alertSubtitle');
    const message = document.getElementById('alertMessage');
    const button = document.getElementById('alertButton');

    icon.className = 'custom-alert-icon';
    button.className = 'custom-alert-btn';

    if (type === 'success') {
        icon.classList.add('success');
        button.classList.add('success');
        iconElement.className = 'fas fa-check-circle';
        title.textContent = '¡Éxito!';

        if (action === 'create') {
            subtitle.textContent = 'Registro creado';
            message.textContent = getEntityMessage(entity, 'creó');
        } else if (action === 'update') {
            subtitle.textContent = 'Actualización exitosa';
            message.textContent = getEntityMessage(entity, 'actualizó');
        } else if (action === 'restock') {
            subtitle.textContent = 'Reabastecimiento exitoso';
            message.textContent = 'El inventario se rebasteció correctamente';
        } else if (action === 'activate') {
            subtitle.textContent = 'Activación exitosa';
            message.textContent = getEntityMessage(entity, 'activó');
        } else if (action === 'updateStatus') {
            subtitle.textContent = 'Estado actualizado';
            message.textContent = getEntityMessage(entity, 'actualizó el estado de');
        } else if (action === 'updateAvailability') {
            subtitle.textContent = 'Disponibilidad actualizada';
            message.textContent = getEntityMessage(entity, 'actualizó la disponibilidad de');
        }

    } else if (type === 'warning') {
        icon.classList.add('warning');
        button.classList.add('warning');
        iconElement.className = 'fas fa-exclamation-triangle';
        title.textContent = 'Acción Realizada';

        if (action === 'deactivate') {
            subtitle.textContent = 'Desactivación temporal';
            message.textContent = getEntityMessage(entity, 'desactivó temporalmente');
        }

    } else if (type === 'error') {
        icon.classList.add('error');
        button.classList.add('error');
        iconElement.className = 'fas fa-times-circle';
        title.textContent = 'Error';
        subtitle.textContent = 'Operación fallida';
        message.textContent = 'No se pudo completar la operación. Intente nuevamente.';

    } else if (type === 'delete') {
        icon.classList.add('error');
        button.classList.add('error');
        iconElement.className = 'fas fa-trash-alt';
        title.textContent = 'Eliminado';
        subtitle.textContent = 'Eliminación permanente';
        message.textContent = getEntityMessage(entity, 'eliminó permanentemente');
    }

    modal.classList.add('show');
}

function getEntityMessage(entity, action) {
    const messages = {
        'user': `El usuario se ${action} exitosamente`,
        'product': `El producto se ${action} exitosamente`,
        'category': `La categoría se ${action} exitosamente`,
        'inventory': `El inventario se ${action} exitosamente`
    };
    return messages[entity] || `El registro se ${action} exitosamente`;
}

function closeCustomAlert() {
    const modal = document.getElementById('customAlertModal');
    modal.classList.remove('show');
}

document.getElementById('customAlertModal').addEventListener('click', function (e) {
    if (e.target === this) {
        closeCustomAlert();
    }
});