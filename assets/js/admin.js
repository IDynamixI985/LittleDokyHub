function cargarMensajes() { 
    const mensajes = JSON.parse(localStorage.getItem('dokyMensajes') || '[]');
    document.getElementById('messageCount').textContent = mensajes.length;
    const tbody = document.getElementById('messagesTable');
    if (!mensajes.length) { 
        tbody.innerHTML = '<tr><td colspan="6" class="text-center text-muted py-4">No hay mensajes registrados.</td></tr>';
        return; 
    } 
    tbody.innerHTML = mensajes.map((m, i) => `
        <tr>
            <td class="fw-semibold">
                ${esc(m.nombre)}
            </td>
            <td>
                ${esc(m.email)}
            </td>
            <td>
                ${esc(m.asunto)}
            </td>
            <td style="min-width:220px">
                ${esc(m.mensaje)}
            </td>
            <td>
                ${esc(m.fecha)}
            </td>
            <td>
                <button class="btn btn-sm ${m.estado==='Nuevo'?'btn-outline-danger':'btn-outline-secondary'}" onclick="cambiarEstado(${i})">
                    ${esc(m.estado)}
                </button>
            </td>
        </tr>`).join(''); 
}

function cambiarEstado(i) { 
    const mensajes = JSON.parse(localStorage.getItem('dokyMensajes') || '[]'); 
    mensajes[i].estado = mensajes[i].estado === 'Nuevo' ? 'Atendido' : 'Nuevo'; 
    localStorage.setItem('dokyMensajes', JSON.stringify(mensajes)); 
    cargarMensajes(); 
}

function limpiarMensajes() { 
    if (confirm('¿Eliminar todos los mensajes de contacto?')) { 
        localStorage.removeItem('dokyMensajes'); 
        cargarMensajes(); 
    } 
}

function esc(v) { 
    return String(v ?? '').replace(/[&<>"']/g, c => ({ 
        '&': '&amp;', 
        '<': '&lt;', 
        '>': '&gt;', 
        '"': '&quot;', 
        "'": '&#039;' 
    }[c])); 
} 

cargarMensajes();