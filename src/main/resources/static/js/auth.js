console.log("AUTH.JS CARGADO");

const API_BASE = "";

document.addEventListener("DOMContentLoaded", () => {
    const registerForm     = document.getElementById("registerForm");
    const loginForm        = document.getElementById("loginForm");
    const tagForm          = document.getElementById("tagForm");
    const cuentasContainer = document.getElementById("cuentasContainer");
    const statsContainer   = document.getElementById("statsContainer");
    const mapasRotacionContainer = document.getElementById("mapasRotacionContainer");
    const topBrawlersContainer   = document.getElementById("topBrawlersContainer");
    const brawlersContainer = document.getElementById("brawlersContainer");

    if (registerForm) {
        registerForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = {
                username: document.getElementById("reg_usuario").value,
                email:    document.getElementById("reg_email").value,
                password: document.getElementById("reg_password").value
            };
            const res = await fetch(`${API_BASE}/auth/register`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });
            if (res.ok) {
                alert("Registro correcto");
                window.location.href = "login.html";
            } else {
                alert(await res.text());
            }
        });
    }

    if (loginForm) {
        loginForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            const data = {
                username: document.getElementById("username").value,
                password: document.getElementById("password").value
            };
            const res = await fetch(`${API_BASE}/auth/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            });
            if (!res.ok) { alert("Credenciales incorrectas"); return; }
            let result;
            try { result = await res.json(); }
            catch (error) { console.error("Error al parsear JSON:", error); alert("Respuesta inválida del servidor."); return; }
            localStorage.setItem("userId", result.userId);
            localStorage.setItem("username", result.username);
            localStorage.setItem("token", result.token);
            window.location.href = "dashboard.html";
        });
    }

    if (tagForm) {
        const tagInput     = document.getElementById("tagInput");
        const resultadoDiv = document.getElementById("resultadoCuenta");
        tagForm.addEventListener("submit", async (e) => {
            e.preventDefault();
            let tag = tagInput.value.trim();
            if (!tag) return;
            tag = tag.replace("#", "").toUpperCase();
            resultadoDiv.innerHTML = '<div class="text-center" style="color:var(--text-muted);">Buscando...</div>';
            const res = await fetch(`${API_BASE}/brawl/player?tag=${tag}`);
            if (!res.ok) {
                let msg = "Error desconocido.";
                try { const ej = await res.json(); msg = ej.message ?? msg; } catch { msg = `Error ${res.status}`; }
                resultadoDiv.innerHTML = `<p style="color:var(--red); text-align:center;">${msg}</p>`;
                return;
            }
            const player = await res.json();
            resultadoDiv.innerHTML = `
                <div class="glass-static" style="padding:1.5rem; text-align:center;">
                    <p style="font-weight:700; font-size:1.1rem; margin-bottom:0.5rem;">${player.name}</p>
                    <p style="color:var(--cyan); font-size:0.85rem;">#${player.tag}</p>
                    <p style="color:var(--gold); font-size:0.85rem;">Trofeos: ${player.trophies}</p>
                    <button id="btn-vincular" class="btn btn-cyan mt-2">Vincular esta cuenta</button>
                </div>`;
            document.getElementById("btn-vincular").addEventListener("click", async () => {
                const userId = localStorage.getItem("userId");
                if (!userId) { alert("Debes iniciar sesión."); return; }
                const body = { tag: player.tag, nombre: player.name, trofeos: player.trophies, nivel: player.expLevel ?? 0 };
                const token = localStorage.getItem("token");
                const resV = await fetch(`${API_BASE}/cuentas/vincular`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${token}`
                    },
                    credentials: 'include',
                    body: JSON.stringify(body)
                });
                if (resV.ok) alert("Cuenta vinculada correctamente");
                else alert("Error vinculando cuenta: " + await resV.text());
            });
        });
    }

    if (cuentasContainer) cargarCuentasVinculadas(cuentasContainer);
    if (statsContainer) inicializarPaginaEstadisticas(statsContainer);
    if (mapasRotacionContainer) inicializarPaginaMapas(mapasRotacionContainer, topBrawlersContainer);
    if (brawlersContainer) cargarBrawlersParaTierlist();
});

async function logout() {
    try { await fetch(`${API_BASE}/auth/logout`, { method: "POST", credentials: 'include' }); }
    catch (e) { console.error("Error during logout:", e); }
    localStorage.clear();
    window.location.href = "login.html";
}

async function cargarCuentasVinculadas(cuentasContainer) {
    const token  = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");
    if (!token || !userId) {
        cuentasContainer.innerHTML = '<div class="empty-state"><p>Debes iniciar sesión para ver tus cuentas.</p></div>';
        return;
    }
    try {
        const res = await fetch(`${API_BASE}/cuentas/mias`, {
            credentials: 'include',
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!res.ok) { cuentasContainer.innerHTML = `<div class="empty-state"><p>Error cargando cuentas (${res.status}).</p></div>`; return; }
        const cuentas = await res.json();
        if (!cuentas.length) { cuentasContainer.innerHTML = '<div class="empty-state"><p>Aún no has vinculado ninguna cuenta.</p></div>'; return; }
        let html = "";
        cuentas.forEach(c => {
            const tagSinAlmohadilla = (c.tag || "").replace("#", "");
            html += `
                <div class="account-card glass animate-in">
                    <h3>${c.nombre}</h3>
                    <p class="tag">${c.tag}</p>
                    <p class="trophies">Trofeos: ${c.trofeos}</p>
                    <button class="btn btn-sm" onclick="verEstadisticasDesdeCuenta('${tagSinAlmohadilla}')">Ver estadísticas</button>
                </div>`;
        });
        cuentasContainer.innerHTML = html;
    } catch (err) {
        console.error("Error cargando cuentas:", err);
        cuentasContainer.innerHTML = '<div class="empty-state"><p>Error inesperado.</p></div>';
    }
}

function verEstadisticasDesdeCuenta(tagSinAlmohadilla) {
    window.location.href = `estadisticas.html?tag=${encodeURIComponent(tagSinAlmohadilla)}`;
}

async function inicializarPaginaEstadisticas(statsContainer) {
    const params = new URLSearchParams(window.location.search);
    let tag = params.get("tag");
    if (!tag) {
        statsContainer.innerHTML = '<div class="empty-state"><p>No se ha seleccionado ninguna cuenta.</p><p>Ve a <a href="cuentas.html" style="color:var(--accent-light);">Cuentas vinculadas</a> y pulsa "Ver estadísticas".</p></div>';
        return;
    }
    tag = tag.replace("#", "").toUpperCase();
    statsContainer.innerHTML = '<div class="text-center" style="color:var(--text-muted); padding:2rem;">Cargando estadísticas...</div>';
    try {
        const res = await fetch(`${API_BASE}/brawl/player?tag=${tag}`);
        if (!res.ok) {
            let msg = "Error obteniendo estadísticas.";
            try { const ej = await res.json(); msg = ej.message ?? msg; } catch { msg = `Error ${res.status}`; }
            statsContainer.innerHTML = `<p style="color:var(--red); text-align:center;">${msg}</p>`;
            return;
        }
        const data = await res.json();
        pintarEstadisticasJugador(statsContainer, data);
    } catch (err) {
        console.error("Error cargando estadísticas:", err);
        statsContainer.innerHTML = '<p style="color:var(--red); text-align:center;">Error inesperado.</p>';
    }
}

function pintarEstadisticasJugador(container, data) {
    const name   = data.name;
    const tag    = data.tag;
    const club   = data.club?.name || "Sin club";
    const trophies        = data.trophies ?? 0;
    const highestTrophies = data.highestTrophies ?? 0;
    const expLevel        = data.expLevel ?? 0;
    const threeVsThree    = data["3vs3Victories"] ?? 0;
    const soloVictories   = data.soloVictories ?? 0;
    const duoVictories    = data.duoVictories ?? 0;
    const brawlers        = data.brawlers ?? [];
    const iconId = data.icon?.id ?? null;
    const avatarUrl = iconId
        ? `https://cdn.brawlify.com/profile-icons/regular/${iconId}.png`
        : "https://cdn.brawlify.com/profile-icons/regular/28000000.png";
    const totalVictorias = threeVsThree + soloVictories + duoVictories;

    let html = `
        <section class="stats-profile">
            <div class="stats-avatar-card glass-static">
                <div class="avatar-ring">
                    <img src="${avatarUrl}" alt="avatar">
                </div>
                <h3 style="font-size:1.2rem; margin-bottom:0.5rem;">${name}</h3>
                <p style="color:var(--cyan); font-size:0.82rem; margin-bottom:0.25rem;">${tag}</p>
                <p style="color:var(--text-dim); font-size:0.78rem;">${club}</p>
            </div>
            <div class="stats-data-card glass-static">
                <div class="stat-row"><span class="stat-label">Trofeos</span><span class="stat-value">${trophies}</span></div>
                <div class="stat-row"><span class="stat-label">M&aacute;x. trofeos</span><span class="stat-value">${highestTrophies}</span></div>
                <div class="stat-row"><span class="stat-label">Nivel XP</span><span class="stat-value">${expLevel}</span></div>
                <div class="stat-row"><span class="stat-label">Victorias 3v3</span><span class="stat-value">${threeVsThree}</span></div>
                <div class="stat-row"><span class="stat-label">Victorias Solo</span><span class="stat-value">${soloVictories}</span></div>
                <div class="stat-row"><span class="stat-label">Victorias Dúo</span><span class="stat-value">${duoVictories}</span></div>
                <div class="stat-row"><span class="stat-label">Total victorias</span><span class="stat-value">${totalVictorias}</span></div>
            </div>
        </section>
    `;

    html += `
        <div class="divider"></div>
        <section class="glass-static" style="padding:2rem;">
            <h3 style="margin-bottom:0.5rem;">Brawlers</h3>
            <p style="font-size:0.78rem; color:var(--text-dim); margin-bottom:1rem;">Desliza para verlos todos</p>
            <div class="brawler-carousel">
                <button class="carousel-btn prev" onclick="moverCarrusel(-1)">&#10094;</button>
                <div class="carousel-track" id="carouselTrack">
    `;
    brawlers.forEach(b => {
        const icon = b.iconUrl || `https://cdn.brawlify.com/brawlers/borderless/${b.id}.png`;
        html += `
            <article class="brawler-stat-card glass">
                <img src="${icon}" class="brawler-img" alt="${b.name}">
                <h4>${b.name}</h4>
                <div class="stat-row"><span class="stat-label">Poder</span><span class="stat-value">${b.power ?? "-"}</span></div>
                <div class="stat-row"><span class="stat-label">Rango</span><span class="stat-value">${b.rank ?? "-"}</span></div>
                <div class="stat-row"><span class="stat-label">Trofeos</span><span class="stat-value">${b.trophies ?? "-"}</span></div>
                <div class="stat-row"><span class="stat-label">M&aacute;x.</span><span class="stat-value">${b.highestTrophies ?? "-"}</span></div>
            </article>`;
    });
    html += `</div><button class="carousel-btn next" onclick="moverCarrusel(1)">&#10095;</button></div></section>`;
    container.innerHTML = html;
}

let carruselIndex = 0;
function moverCarrusel(dir) {
    const track = document.getElementById("carouselTrack");
    if (!track) return;
    const cards = track.querySelectorAll(".brawler-stat-card");
    if (!cards.length) return;
    const gap = 20;
    const cardW = cards[0].offsetWidth + gap;
    const visible = 4;
    const maxIdx = Math.max(0, cards.length - visible);
    carruselIndex = Math.max(0, Math.min(maxIdx, carruselIndex + dir));
    track.style.transform = `translateX(${-carruselIndex * cardW}px)`;
}

async function inicializarPaginaMapas(rotacionContainer, topContainer) {
    window._topBrawlersContainer = topContainer;
    try {
        const resRot = await fetch(`${API_BASE}/mapas/rotacion`);
        const mapasRot = resRot.ok ? await resRot.json() : [];
        pintarMapasRotacion(rotacionContainer, mapasRot);
    } catch (err) {
        console.error("Error cargando mapas en rotación:", err);
        rotacionContainer.innerHTML = '<div class="empty-state"><p>Error cargando mapas.</p></div>';
    }
    try {
        const resAll = await fetch(`${API_BASE}/mapas`);
        const mapas = resAll.ok ? await resAll.json() : [];
        const mapasPorModo = {};
        mapas.forEach(m => { const modo = m.modo || "Otros"; if (!mapasPorModo[modo]) mapasPorModo[modo] = []; mapasPorModo[modo].push(m); });
        pintarMapasPorModo(mapasPorModo, topContainer);
    } catch (err) {
        console.error("Error cargando todos los mapas:", err);
    }
}

async function pintarMapasRotacion(contenedor, mapasRot) {
    if (!mapasRot || !mapasRot.length) { contenedor.innerHTML = '<div class="empty-state"><p>No hay mapas en rotaci&oacute;n.</p></div>'; return; }
    contenedor.innerHTML = '<div class="text-center" style="color:var(--text-muted); padding:1rem;">Cargando mapas...</div>';
    const validMaps = [];
    const checks = mapasRot.map(r => {
        return new Promise(resolve => {
            if (!r.supercellId) { resolve(); return; }
            const img = new Image();
            img.onload = () => { validMaps.push(r); resolve(); };
            img.onerror = () => resolve();
            img.src = `https://cdn.brawlify.com/maps/regular/${r.supercellId}.png`;
        });
    });
    await Promise.all(checks);
    if (!validMaps.length) { contenedor.innerHTML = '<div class="empty-state"><p>No hay mapas con imagen disponible.</p></div>'; return; }
    let html = "";
    validMaps.forEach(r => {
        const img = `https://cdn.brawlify.com/maps/regular/${r.supercellId}.png`;
        html += `
            <div class="map-card" data-id="${r.id ?? -1}" onclick="toggleFlip(this)">
                <div class="map-card-inner">
                    <div class="map-front">
                        <img src="${img}" alt="${r.nombre}">
                        <h4>${r.nombre}</h4>
                        <span class="map-mode">${r.modo}</span>
                    </div>
                    <div class="map-back"><div class="stats-mini">Clic para ver stats</div></div>
                </div>
            </div>`;
    });
    contenedor.innerHTML = html;
}

async function toggleFlip(card) {
    const isFlipped = card.classList.toggle("flipped");
    if (!isFlipped) return;
    const mapaId = card.dataset.id;
    const statsBox = card.querySelector(".stats-mini");
    statsBox.innerHTML = "Cargando...";
    try {
        const res = await fetch(`${API_BASE}/mapas/${mapaId}/top-brawlers`);
        const lista = await res.json();
        if (!lista.length) { statsBox.innerHTML = "<p>No hay datos.</p>"; return; }
        let html = "";
        lista.slice(0, 5).forEach((b, i) => { html += `<p><strong>${i + 1}. ${b.brawlerNombre}</strong> &mdash; ${b.winRate.toFixed(1)}%</p>`; });
        statsBox.innerHTML = html;
    } catch (e) { statsBox.innerHTML = "<p>Error</p>"; }
}

function pintarMapasPorModo(mapasPorModo, topContainer) {
    const contenedor = document.getElementById("mapasPorModo");
    contenedor.innerHTML = "";
    Object.keys(mapasPorModo).forEach(modo => {
        const mapas = mapasPorModo[modo].filter(m => m.supercellId);
        if (!mapas.length) return;
        let html = `
            <div class="mode-block">
                <h4 class="mode-title">${modo}</h4>
                <div class="maps-carousel-wrapper">
                    <button class="maps-carousel-btn" onclick="scrollCarousel(this,-1)">&#10094;</button>
                    <div class="maps-carousel">`;
        mapas.forEach(m => {
            const img = `https://cdn.brawlify.com/maps/regular/${m.supercellId}.png`;
            html += `
                <div class="map-card" data-id="${m.id}" onclick="toggleFlip(this)">
                    <div class="map-card-inner">
                        <div class="map-front">
                            <img src="${img}" alt="${m.nombre}">
                            <h4>${m.nombre}</h4>
                        </div>
                        <div class="map-back"><div class="stats-mini">Clic para stats</div></div>
                    </div>
                </div>`;
        });
        html += `</div><button class="maps-carousel-btn" onclick="scrollCarousel(this,1)">&#10095;</button></div></div>`;
        contenedor.innerHTML += html;
    });
}

function scrollCarousel(btn, dir) {
    const wrapper = btn.closest('.maps-carousel-wrapper');
    const carousel = wrapper.querySelector('.maps-carousel');
    const card = carousel.querySelector('.map-card');
    const gap = 12;
    const step = card ? card.offsetWidth + gap : 172;
    const visible = 4;
    carousel.scrollBy({ left: dir * step * visible, behavior: 'smooth' });
}

async function cargarTopBrawlersMapa(mapaId, origen) {
    const topContainer = window._topBrawlersContainer || document.getElementById("topBrawlersContainer");
    if (!topContainer) return;
    const bloqueTop = document.getElementById("bloqueTopBrawlers");
    if (bloqueTop) bloqueTop.style.display = "block";
    if (origen === 'rotacion') { topContainer.innerHTML = '<p style="color:var(--text-muted);">Mapa en rotaci&oacute;n sin estad&iacute;sticas internas.</p>'; return; }
    topContainer.innerHTML = '<p style="color:var(--text-muted);">Cargando...</p>';
    try {
        const res = await fetch(`${API_BASE}/mapas/${mapaId}/top-brawlers`, { credentials: 'include' });
        if (!res.ok) { topContainer.innerHTML = `<p>Error (${res.status}).</p>`; return; }
        const lista = await res.json();
        pintarTopBrawlers(topContainer, lista);
    } catch (err) { topContainer.innerHTML = '<p>Error inesperado.</p>'; }
}

function pintarTopBrawlers(container, lista) {
    if (!lista || !lista.length) { container.innerHTML = '<p style="color:var(--text-muted);">No hay datos.</p>'; return; }
    let html = `
        <table class="top-brawlers-table">
            <thead><tr><th>#</th><th>Brawler</th><th>Winrate</th><th>Partidas</th></tr></thead>
            <tbody>`;
    lista.slice(0, 10).forEach((b, idx) => {
        const nombre = b.brawlerNombre || b.nombre || b.name || "Brawler";
        const winR = typeof b.winRate === "number" ? b.winRate : null;
        const winrate = winR == null ? "-" : winR.toFixed(2);
        const partidas = b.pickRate ?? b.partidas ?? b.matches ?? "-";
        html += `<tr><td class="rank-num">${idx + 1}</td><td>${nombre}</td><td class="winrate">${winrate}${winrate !== "-" ? "%" : ""}</td><td>${partidas}</td></tr>`;
    });
    html += `</tbody></table>`;
    container.innerHTML = html;
}

async function cargarTierlists() {
    const cont = document.getElementById("tierlistsContainer");
    try {
        const res = await fetch(`${API_BASE}/tierlists`);
        if (!res.ok) { cont.innerHTML = '<div class="empty-state"><p>Error cargando tierlists.</p></div>'; return; }
        const lista = await res.json();
        if (!lista.length) { cont.innerHTML = '<div class="empty-state"><p>No hay tierlists a&uacute;n.</p></div>'; return; }
        let html = "";
        lista.forEach(t => {
            html += `
                <div class="tierlist-card glass animate-in">
                    <h3>${t.nombre}</h3>
                    <p class="creator">Creador: ${t.usuario?.username ?? "An&oacute;nimo"}</p>
                    <div style="display:flex; gap:10px; margin-top:1rem;">
                        <button class="btn btn-sm" onclick="window.location.href='tierlist_ver.html?id=${t.id}'">Ver Tierlist</button>
                        <button class="btn btn-sm btn-danger" onclick="borrarTierlist(${t.id})">Eliminar</button>
                    </div>
                </div>`;
        });
        cont.innerHTML = html;
    } catch(err) { console.error(err); cont.innerHTML = '<div class="empty-state"><p>Error inesperado.</p></div>'; }
}

async function borrarTierlist(id) {
    const token = localStorage.getItem("token");
    if (!token) { alert("Debes iniciar sesión."); return; }
    if (!confirm("¿Seguro que quieres borrar esta tierlist?")) return;
    const res = await fetch(`${API_BASE}/tierlists/${id}`, { method: "DELETE", headers: { "Authorization": `Bearer ${token}` } });
    if (res.status === 403) { alert("No tienes permiso."); return; }
    if (!res.ok) { alert("Error al borrar."); return; }
    alert("Tierlist eliminada.");
    cargarTierlists();
}

async function cargarBrawlersParaTierlist() {
    const cont = document.getElementById("brawlersContainer");
    if (!cont) return;
    cont.innerHTML = '<p style="color:var(--text-muted);">Cargando brawlers...</p>';
    try {
        const res = await fetch(`${API_BASE}/brawl/brawlers`);
        if (!res.ok) { cont.innerHTML = '<p>Error.</p>'; return; }
        const brawlers = await res.json();
        let html = '<div class="brawler-pool">';
        brawlers.forEach(b => {
            html += `<div class="brawler-chip" draggable="true" data-id="${b.id}" title="${b.nombre}"><img src="${b.iconUrl}" alt="${b.nombre}" style="width:100%;height:100%;border-radius:6px;"></div>`;
        });
        html += "</div>";
        cont.innerHTML = html;
    } catch (error) { cont.innerHTML = '<p>Error.</p>'; }
}
