document.getElementById('loginForm').addEventListener('submit', function (e) {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (!username || !password) {
        e.preventDefault();
        alert('Por favor, complete todos los campos');
        return false;
    }

    const btnLogin = document.getElementById('btnLogin');
    const btnText = document.getElementById('btnText');

    btnLogin.disabled = true;
    btnText.innerHTML = '<span class="spinner"></span>Iniciando sesión...';
});

window.addEventListener('load', function () {
    document.getElementById('username').focus();
});