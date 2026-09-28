document.addEventListener('DOMContentLoaded', () => {
    // Función auxiliar para mostrar el modal de notificación
    function mostrarModal(titulo, mensaje, urlRedireccion) {
        const modalElement = document.getElementById('modalNotificacion');
        if (!modalElement || typeof bootstrap === 'undefined') return;

        document.getElementById('modalNotificacionLabel').textContent = titulo;
        document.getElementById('modalNotificacionBody').innerHTML = mensaje;

        const btnContinuar = document.getElementById('btnModalContinuar');
        // Asignar redirección al hacer clic en el botón Continuar
        btnContinuar.onclick = function () {
            if (urlRedireccion) {
                window.location.href = urlRedireccion;
            }
        };

        const modalInstancia = bootstrap.Modal.getOrCreateInstance(modalElement);
        modalInstancia.show();
    }

    // FORGOT PASSWORD (PASO 1)
    const forgotForm = document.getElementById('forgotForm');
    if (forgotForm) {
        const emailInput = document.getElementById('email');

        forgotForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const emailVal = emailInput.value.trim().toLowerCase();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

            const userPart = emailVal.split('@')[0] || '';
            if (userPart.length < 3 || !emailRegex.test(emailVal)) {
                mostrarModal('Aviso', 'Por favor, ingresa un correo electrónico válido (mínimo 3 caracteres antes del @).', null);
                emailInput.focus();
                return;
            }

            // Verificar si el correo realmente existe en los usuarios registrados de create.html
            const usuarios = JSON.parse(localStorage.getItem('dokyUsuarios') || '[]');
            const usuarioExiste = usuarios.some(u => u.email.toLowerCase() === emailVal);

            if (!usuarioExiste) {
                mostrarModal('Cuenta no encontrada', 'No existe ninguna cuenta registrada con este correo electrónico. Por favor, crea una cuenta primero.', null);
                emailInput.focus();
                return;
            }

            // Guardar el correo en sesión para saber a quién actualizarle la contraseña al final
            sessionStorage.setItem('doky_recovery_email', emailVal);

            // Generar código de 6 dígitos y guardarlo en la sesión para validarlo en el siguiente paso
            const codigoGenerado = Math.floor(100000 + Math.random() * 900000).toString();
            sessionStorage.setItem('doky_verify_code', codigoGenerado);

            // Mostrar modal de éxito
            mostrarModal(
                '¡Código Generado!',
                `Su código de verificación es: <strong>${codigoGenerado}</strong>.<br><br>Presione continuar para verificarlo.`,
                forgotForm.getAttribute('action')
            );
        });
    }

    // VERIFY CODE (PASO 2)
    const verifyForm = document.getElementById('verifyForm');
    if (verifyForm) {
        const codeInput = document.getElementById('codigo');

        // Permitir solo números en el input
        codeInput.addEventListener('input', function () {
            this.value = this.value.replace(/[^0-9]/g, '');
        });

        verifyForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const codeVal = codeInput.value.trim();
            const codigoGuardado = sessionStorage.getItem('doky_verify_code');

            if (codeVal.length !== 6) {
                mostrarModal('Código Inválido', 'El código debe tener exactamente 6 dígitos numéricos.', null);
                codeInput.focus();
                return;
            }

            // Validación de autenticación real contra el código generado
            if (codigoGuardado && codeVal !== codigoGuardado) {
                mostrarModal('Error de Verificación', 'El código ingresado es incorrecto. Por favor verifique e intente nuevamente.', null);
                codeInput.focus();
                return;
            }

            // Modal de éxito
            mostrarModal(
                '¡Verificación Exitosa!',
                `El código puesto es correcto.<br>Ahora puede restablecer su contraseña.`,
                verifyForm.getAttribute('action')
            );
        });
    }

    // RESET PASSWORD (PASO 3)
    const resetForm = document.getElementById('resetForm');
    if (resetForm) {
        const newPassword = document.getElementById('newPassword');
        const confirmPassword = document.getElementById('confirmPassword');

        resetForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const passVal = newPassword.value;
            const confirmVal = confirmPassword.value;

            const hasUpperCase = /[A-Z]/.test(passVal);
            const hasNumber = /[0-9]/.test(passVal);
            const hasSpecialChar = /[!@°#$%&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(passVal);

            if (passVal.length < 8) {
                mostrarModal('Contraseña Débil', 'La contraseña debe tener un mínimo de 8 caracteres.', null);
                newPassword.focus();
                return;
            }

            if (!hasUpperCase || !hasNumber || !hasSpecialChar) {
                mostrarModal('Requisitos Faltantes', 'La contraseña debe incluir al menos una letra mayúscula, un número y un carácter especial (!, @, °, #, etc.).', null);
                newPassword.focus();
                return;
            }

            if (passVal !== confirmVal) {
                mostrarModal('Error', 'Las contraseñas no coinciden. Por favor verifíquelas.', null);
                confirmPassword.focus();
                return;
            }

            // Actualizar la contraseña en el localStorage para que el usuario pueda ingresar con ella en login.html
            const emailRecuperacion = sessionStorage.getItem('doky_recovery_email');
            const usuarios = JSON.parse(localStorage.getItem('dokyUsuarios') || '[]');
            if (emailRecuperacion) {
                const usuario = usuarios.find(u => u.email.toLowerCase() === emailRecuperacion.toLowerCase());
                if (usuario) {
                    usuario.password = passVal;
                    localStorage.setItem('dokyUsuarios', JSON.stringify(usuarios));
                }
            }

            // Limpiar datos temporales de la sesión
            sessionStorage.removeItem('doky_verify_code');
            sessionStorage.removeItem('doky_recovery_email');

            // Modal de confirmación final
            mostrarModal(
                '¡Operación Exitosa!',
                'Su contraseña ha sido cambiada correctamente.<br>Ya puede iniciar sesión con sus nuevas credenciales.',
                resetForm.getAttribute('action')
            );
        });
    }

});