// ── GESTIÓN GLOBAL DEL CARRITO (LITTLE DOKY'S HUB) ──

// 1. Obtener productos del carrito desde localStorage
function obtenerCarrito() {
    return JSON.parse(localStorage.getItem('dokyCarrito')) || [
        { id: 1, nombre: 'Pizza Americana', detalle: 'Familiar', precio: 32.00, img: 'americana.jpg' },
        { id: 2, nombre: 'Pizza Pepperoni', detalle: 'Mediana', precio: 36.00, img: 'pepperoni.jpg' }
    ];
}

// 2. Guardar y refrescar la vista
function guardarCarrito(carrito) {
    localStorage.setItem('dokyCarrito', JSON.stringify(carrito));
    renderizarCarrito();
}

// 3. Renderizar productos dentro del Modal Lateral
function renderizarCarrito() {
    const carrito = obtenerCarrito();
    const contenedor = document.querySelector('#modalCarrito .flex-grow-1');
    const badge = document.querySelector('.badge.rounded-pill.bg-danger');
    const totalElemento = document.querySelector('#modalCarrito .text-danger');
    const subtotalElemento = document.querySelector('#modalCarrito .text-muted.small span:last-child');

    // Actualizar badge del header si existe
    if (badge) {
        badge.textContent = carrito.length;
    }

    if (!contenedor) return;

    if (carrito.length === 0) {
        contenedor.innerHTML = '<p class="text-center text-muted my-4 py-5"><i class="bi bi-cart-x fs-1 d-block mb-2"></i>Tu carrito está vacío.</p>';
        if (totalElemento) totalElemento.textContent = 'S/ 0.00';
        if (subtotalElemento) subtotalElemento.textContent = 'S/ 0.00';
        return;
    }

    // Determinar la ruta relativa según el nivel del HTML
    const rutaBase = window.location.pathname.includes('/shop/') || 
                     window.location.pathname.includes('/contact/') || 
                     window.location.pathname.includes('/account/') || 
                     window.location.pathname.includes('/admin/') ? '../../assets/images/' : 'assets/images/';

    let total = 0;
    contenedor.innerHTML = carrito.map((item, index) => {
        total += Number(item.precio);
        return `
            <div class="d-flex align-items-center justify-content-between border-bottom py-2">
                <div class="d-flex align-items-center gap-2">
                    <img src="${rutaBase}${item.img}" alt="${item.nombre}" class="rounded" style="width: 50px; height: 50px; min-width: 50px; max-width: 50px; object-fit: cover; flex-shrink: 0;">
                    <div>
                        <h6 class="mb-0 fw-bold fs-6">${item.nombre}</h6>
                        <small class="text-muted">${item.detalle} - S/ ${Number(item.precio).toFixed(2)}</small>
                    </div>
                </div>
                <div class="text-end">
                    <span class="fw-bold d-block">S/ ${Number(item.precio).toFixed(2)}</span>
                    <button class="btn btn-sm text-danger p-0 border-0" onclick="eliminarDelCarrito(${index})" title="Eliminar">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </div>
        `;
    }).join('');

    const totalFormateado = `S/ ${total.toFixed(2)}`;
    if (totalElemento) totalElemento.textContent = totalFormateado;
    if (subtotalElemento) subtotalElemento.textContent = totalFormateado;
}

// 4. Eliminar producto individual
window.eliminarDelCarrito = function(indice) {
    const carrito = obtenerCarrito();
    carrito.splice(indice, 1);
    guardarCarrito(carrito);
};

// 5. Agregar producto desde cualquier tarjeta con botón "Agregar"
document.addEventListener('click', (e) => {
    const btnAgregar = e.target.closest('button');
    if (btnAgregar && btnAgregar.textContent.includes('Agregar') && !btnAgregar.closest('#modalCarrito')) {
        const card = btnAgregar.closest('.card');
        if (card) {
            const nombre = card.querySelector('.card-title')?.textContent.trim() || 'Pizza Doky';
            const precioTexto = card.querySelector('.fs-5')?.textContent.replace('S/', '').trim() || '30.00';
            const imgEl = card.querySelector('img');
            const imgNombre = imgEl ? imgEl.getAttribute('src').split('/').pop() : 'americana.jpg';

            const nuevoItem = {
                id: Date.now(),
                nombre: nombre,
                detalle: 'Tradicional',
                precio: parseFloat(precioTexto),
                img: imgNombre
            };

            const carrito = obtenerCarrito();
            carrito.push(nuevoItem);
            guardarCarrito(carrito);

            // Abrir automáticamente el modal del carrito para mostrar la adición
            const modalEl = document.getElementById('modalCarrito');
            if (modalEl && typeof bootstrap !== 'undefined') {
                const modalInstancia = bootstrap.Modal.getOrCreateInstance(modalEl);
                modalInstancia.show();
            }
        }
    }
});

// Inicializar vista al cargar la pantalla
document.addEventListener('DOMContentLoaded', renderizarCarrito);