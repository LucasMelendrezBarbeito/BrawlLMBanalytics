console.log("AUTH.JS CARGADO");

const API_BASE = "http://localhost:8080";


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

            const text = await res.text();

            if (res.ok) {
                alert("Registro correcto");
                window.location.href = "login.html";
            } else {
                alert(text);
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

            if (!res.ok) {
                alert("Credenciales incorrectas");
                return;
            }

            let result;
            try {
                result = await res.json();
            } catch (error) {
                console.error("Error al parsear JSON:", error);
                alert("Respuesta inválida del servidor.");
                return;
            }

            localStorage.setItem("userId",   result.userId);
            localStorage.setItem("username", result.username);

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

            resultadoDiv.innerHTML = "<h3>Buscando...</h3>";

            console.log("TAG enviado al backend:", tag);

            const res = await fetch(`${API_BASE}/brawl/player?tag=${tag}`);

            if (!res.ok) {
                resultadoDiv.innerHTML = "<h3>Error</h3>";

                let msg = "Error desconocido.";
                try {
                    const errorJson = await res.json();
                    msg = errorJson.message ?? msg;
                } catch {
                    msg = `Error ${res.status}`;
                }

                resultadoDiv.innerHTML += `<p style="color:red">${msg}</p>`;
                alert(msg);
                return;
            }

            const player = await res.json();

            resultadoDiv.innerHTML = `
                <h3>Resultado de búsqueda</h3>
                <p><strong>Nombre:</strong> ${player.name}</p>
                <p><strong>Tag:</strong> #${player.tag}</p>
                <p><strong>Trofeos:</strong> ${player.trophies}</p>
                <button id="btn-vincular" class="btn-vincular">Vincular esta cuenta</button>
            `;

            const btnVincular = document.getElementById("btn-vincular");

            btnVincular.addEventListener("click", async () => {

                const userId = localStorage.getItem("userId");

                if (!userId) {
                    alert("Debes iniciar sesión.");
                    return;
                }

                const body = {
                    tag:       player.tag,
                    nombre:    player.name,
                    trofeos:   player.trophies,
                    nivel:     player.expLevel ?? 0
                };

                const resV = await fetch(`${API_BASE}/cuentas/vincular`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: 'include',
                    body: JSON.stringify(body)
                });

                const txt = await resV.text();

                if (resV.ok) {
                    alert("Cuenta vinculada correctamente");
                } else {
                    alert("Error vinculando cuenta: " + txt);
                }
            });
        });
    }


    if (cuentasContainer) {
        cargarCuentasVinculadas(cuentasContainer);
    }

   
    if (statsContainer) {
        inicializarPaginaEstadisticas(statsContainer);
    }

    
   if (mapasRotacionContainer) {
    inicializarPaginaMapas(mapasRotacionContainer, topBrawlersContainer);
}


    if (brawlersContainer) {
        cargarBrawlersParaTierlist();
    }

});


async function logout() {
    try {
        await fetch(`${API_BASE}/auth/logout`, {
            method: "POST",
            credentials: 'include'
        });
    } catch (e) {
        console.error("Error during logout:", e);
    }
    localStorage.clear();
    window.location.href = "login.html";
}


async function cargarCuentasVinculadas(cuentasContainer) {

    const token  = localStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    if (!token || !userId) {
        cuentasContainer.innerHTML = `<p>Debes iniciar sesión para ver tus cuentas.</p>`;
        return;
    }

    try {
        const res = await fetch(`${API_BASE}/cuentas/mias`, {
            credentials: 'include'
        });

        if (!res.ok) {
            cuentasContainer.innerHTML = `<p>Error cargando cuentas (${res.status}).</p>`;
            return;
        }

        const cuentas = await res.json();

        if (!cuentas.length) {
            cuentasContainer.innerHTML = `<p>Aún no has vinculado ninguna cuenta de Brawl Stars.</p>`;
            return;
        }

        let html = "";
        cuentas.forEach(c => {
            const tagSinAlmohadilla = (c.tag || "").replace("#", "");
            html += `
                <div class="cuenta-card">
                    <h3>${c.nombre}</h3>
                    <p><strong>Tag:</strong> ${c.tag}</p>
                    <p><strong>Trofeos:</strong> ${c.trofeos}</p>
                    <button class="btn"
                            onclick="verEstadisticasDesdeCuenta('${tagSinAlmohadilla}')">
                        Ver estadísticas
                    </button>
                </div>
            `;
        });

        cuentasContainer.innerHTML = html;

    } catch (err) {
        console.error("Error cargando cuentas:", err);
        cuentasContainer.innerHTML = `<p>Error inesperado al cargar cuentas.</p>`;
    }
}

function verEstadisticasDesdeCuenta(tagSinAlmohadilla) {
    window.location.href = `estadisticas.html?tag=${encodeURIComponent(tagSinAlmohadilla)}`;
}



async function inicializarPaginaEstadisticas(statsContainer) {

    const params = new URLSearchParams(window.location.search);
    let tag = params.get("tag");

    if (!tag) {
        statsContainer.innerHTML = `
            <p>No se ha seleccionado ninguna cuenta.</p>
            <p>Ve a <a href="cuentas.html">Cuentas vinculadas</a> y pulsa en "Ver estadísticas".</p>
        `;
        return;
    }

    tag = tag.replace("#", "").toUpperCase();

    statsContainer.innerHTML = "<h3>Cargando estadísticas...</h3>";

    try {
        const res = await fetch(`${API_BASE}/brawl/player?tag=${tag}`);

        if (!res.ok) {
            let msg = "Error obteniendo estadísticas.";
            try {
                const errorJson = await res.json();
                msg = errorJson.message ?? msg;
            } catch {
                msg = `Error ${res.status}`;
            }
            statsContainer.innerHTML = `<p style="color:red">${msg}</p>`;
            return;
        }

        const data = await res.json();
        pintarEstadisticasJugador(statsContainer, data);

    } catch (err) {
        console.error("Error cargando estadísticas:", err);
        statsContainer.innerHTML = `<p style="color:red">Error inesperado al cargar las estadísticas.</p>`;
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
        ? `https://cdn.brawlify.com/profile/${iconId}.png`
        : "images/default_avatar.png";

    const totalVictorias = threeVsThree + soloVictories + duoVictories;
    const horasJugadas   = Math.max(1, Math.round(totalVictorias / 10));

    let html = `
        <section class="stats-perfil">
            <h2>Perfil de Brawl Stars de ${name}</h2>
            <div class="stats-perfil-contenido">
                <div class="stats-perfil-card">

                    <div class="stats-avatar">
                        <img src="${avatarUrl}" class="avatar-img" alt="avatar">
                    </div>

                    <div class="stats-perfil-datos">
                        <p><strong>Nombre:</strong> ${name}</p>
                        <p><strong>Tag:</strong> ${tag}</p>
                        <p><strong>Club:</strong> ${club}</p>
                        <p><strong>Trofeos:</strong> ${trophies}</p>
                        <p><strong>Mayor número de trofeos:</strong> ${highestTrophies}</p>
                        <p><strong>Nivel de XP:</strong> ${expLevel}</p>
                        <p><strong>Victorias 3c3:</strong> ${threeVsThree}</p>
                        <p><strong>Victorias en Solo:</strong> ${soloVictories}</p>
                        <p><strong>Victorias en Dúo:</strong> ${duoVictories}</p>
                    </div>
                </div>
            </div>
        </section>
    `;

 

    html += `
        <section class="stats-brawlers">
            <h2>Brawlers</h2>
            <p>Desliza con las flechas para verlos todos</p>

            <div class="carousel-wrapper">
                <button class="carousel-btn prev">&#10094;</button>

                <div class="carousel-container">
                    <div class="carousel-track">
    `;

    brawlers.forEach(b => {

        const icon = b.iconUrl || `https://cdn.brawlify.com/brawlers/borderless/${b.id}.png`;

        html += `
            <article class="brawler-card">
                <header class="brawler-header">
                    <img src="${icon}" class="brawler-img" alt="brawler">
                    <h3>${b.name}</h3>
                </header>

                <div class="brawler-body">
                    <p><strong>Poder:</strong> ${b.power ?? "-"}</p>
                    <p><strong>Rango:</strong> ${b.rank ?? "-"}</p>
                    <p><strong>Trofeos:</strong> ${b.trophies ?? "-"}</p>
                    <p><strong>Máx. trofeos:</strong> ${b.highestTrophies ?? "-"}</p>
                </div>
            </article>
        `;
    });

    html += `
                    </div>
                </div>

                <button class="carousel-btn next">&#10095;</button>
            </div>
        </section>
    `;

    container.innerHTML = html;

    inicializarCarrusel();
}


function inicializarCarrusel() {
    const track = document.querySelector(".carousel-track");
    if (!track) return;

    const prev = document.querySelector(".carousel-btn.prev");
    const next = document.querySelector(".carousel-btn.next");
    const cards = Array.from(track.children);

    let index = 0;
    const cardWidth = cards[0].getBoundingClientRect().width + 20;

    function actualizar() {
        track.style.transform = `translateX(${-index * cardWidth}px)`;
    }

    prev.addEventListener("click", () => {
        index = Math.max(0, index - 1);
        actualizar();
    });

    next.addEventListener("click", () => {
        index = Math.min(cards.length - 3, index + 1);
        actualizar();
    });
}
async function inicializarPaginaMapas(rotacionContainer, topContainer) {

    
    window._topBrawlersContainer = topContainer;

    
    try {
        const resRot = await fetch(`${API_BASE}/mapas/rotacion`);
        const mapasRot = resRot.ok ? await resRot.json() : [];

        pintarMapasRotacion(rotacionContainer, mapasRot, topContainer);

    } catch (err) {
        console.error("Error cargando mapas en rotación:", err);
        rotacionContainer.innerHTML = "<p>Error cargando mapas en rotación.</p>";
    }

    
    try {
        const resAll = await fetch(`${API_BASE}/mapas`);
        const mapas = resAll.ok ? await resAll.json() : [];

        
        const mapasPorModo = {};

        mapas.forEach(m => {
            const modo = m.modo || "Otros";
            if (!mapasPorModo[modo]) mapasPorModo[modo] = [];
            mapasPorModo[modo].push(m);
        });

        pintarMapasPorModo(mapasPorModo, topContainer);

    } catch (err) {
        console.error("Error cargando todos los mapas:", err);
        document.getElementById("mapasPorModo").innerHTML =
            "<p>Error cargando los mapas por modo.</p>";
    }
}
async function pintarMapasRotacion(contenedor, mapasRot) {
    if (!mapasRot || !mapasRot.length) {
        contenedor.innerHTML = "<p>No hay mapas en rotación ahora mismo.</p>";
        return;
    }

    let html = "";

    mapasRot.forEach(r => {

        const img = r.supercellId
            ? `https://cdn.brawlify.com/maps/regular/${r.supercellId}.png`
            : "images/map_placeholder.png";

        html += `
            <div class="mapa-card" data-id="${r.id ?? -1}" onclick="toggleFlip(this)">
                <div class="mapa-card-inner">

                    <div class="mapa-card-front">
                        <img src="${img}" class="mapa-img">
                        <h4>${r.nombre}</h4>
                        <p>${r.modo}</p>
                    </div>

                    <div class="mapa-card-back">
                        <div class="stats-mini">Haz clic para ver estadísticas</div>
                    </div>

                </div>
            </div>
        `;
    });

    contenedor.innerHTML = html;
}

async function toggleFlip(card) {
    const isFlipped = card.classList.toggle("flip");

    if (!isFlipped) return; 

    const mapaId = card.dataset.id;
    const statsBox = card.querySelector(".stats-mini");

    statsBox.innerHTML = "Cargando...";

    try {
        const res = await fetch(`${API_BASE}/mapas/${mapaId}/top-brawlers`);
        const lista = await res.json();

        if (!lista.length) {
            statsBox.innerHTML = "<p>No hay datos.</p>";
            return;
        }

        let html = "";
        lista.slice(0, 5).forEach((b, i) => {
            html += `<p><strong>${i + 1}. ${b.brawlerNombre}</strong> — ${b.winRate.toFixed(1)}%</p>`;
        });

        statsBox.innerHTML = html;

    } catch (e) {
        statsBox.innerHTML = "<p>Error cargando datos</p>";
    }
}

function pintarMapasPorModo(mapasPorModo, topContainer) {
    const contenedor = document.getElementById("mapasPorModo");
    contenedor.innerHTML = "";

    Object.keys(mapasPorModo).forEach(modo => {
        const mapas = mapasPorModo[modo];

        let html = `
            <div class="modo-bloque">
                <h3 class="modo-titulo">${modo}</h3>

                <div class="carrusel-wrapper" data-modo="${modo}">
                    <button class="carrusel-btn carrusel-btn-prev">&#10094;</button>
                    <div class="carrusel-mapas">
        `;

        mapas.forEach(m => {
            const img = `https://cdn.brawlify.com/maps/regular/${m.supercellId}.png`;

            html += `
                <div class="mapa-card" data-id="${m.id}" onclick="toggleFlip(this)">
                    <div class="mapa-card-inner">

                        <div class="mapa-card-front">
                            <img src="${img}" class="mapa-img">
                            <h4>${m.nombre}</h4>
                        </div>

                        <div class="mapa-card-back">
                            <div class="stats-mini">Haz clic para cargar estadísticas...</div>
                        </div>

                    </div>
                </div>
            `;
        });

        html += `
                    </div>
                    <button class="carrusel-btn carrusel-btn-next">&#10095;</button>
                </div>
            </div>
        `;

        contenedor.innerHTML += html;
    });

    inicializarCarruselesMapas();
}


function inicializarCarruselesMapas() {
    const wrappers = document.querySelectorAll(".carrusel-wrapper");

    wrappers.forEach(wrapper => {
        const carrusel = wrapper.querySelector(".carrusel-mapas");
        const btnPrev  = wrapper.querySelector(".carrusel-btn-prev");
        const btnNext  = wrapper.querySelector(".carrusel-btn-next");

        if (!carrusel || !btnPrev || !btnNext) return;

        const card = carrusel.querySelector(".mapa-card");
        const step = card ? card.offsetWidth + 16 : 220; // tamaño de salto

        btnPrev.onclick = () => {
            carrusel.scrollBy({ left: -step, behavior: "smooth" });
        };

        btnNext.onclick = () => {
            carrusel.scrollBy({ left: step, behavior: "smooth" });
        };
    });
}



async function cargarTopBrawlersMapa(mapaId, origen) {
    const topContainer = window._topBrawlersContainer || document.getElementById("topBrawlersContainer");
    if (!topContainer) return;

    const bloqueTop = document.getElementById("bloqueTopBrawlers");
    if (bloqueTop) {
        bloqueTop.style.display = "block";
    }

    if (origen === 'rotacion') {
        topContainer.innerHTML = `<p>Este mapa viene de la rotación en vivo y todavía no tiene estadísticas internas de winrate.</p>`;
        return;
    }

    topContainer.innerHTML = "<p>Cargando mejores brawlers...</p>";

    try {
        const res = await fetch(`${API_BASE}/mapas/${mapaId}/top-brawlers`, {
            credentials: 'include'
        });

        if (!res.ok) {
            topContainer.innerHTML = `<p>Error cargando top brawlers (${res.status}).</p>`;
            return;
        }

        const lista = await res.json();
        pintarTopBrawlers(topContainer, lista);

    } catch (err) {
        console.error("Error top brawlers:", err);
        topContainer.innerHTML = `<p>Error inesperado al cargar los mejores brawlers.</p>`;
    }
}

function pintarTopBrawlers(container, lista) {
    if (!lista || !lista.length) {
        container.innerHTML = `<p>No hay datos de winrate para este mapa.</p>`;
        return;
    }

    let html = `
        <h3>Top brawlers por winrate</h3>
        <table class="tabla-top-brawlers">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Brawler</th>
                    <th>Winrate</th>
                    <th>Partidas</th>
                </tr>
            </thead>
            <tbody>
    `;

    lista.slice(0, 10).forEach((b, idx) => {
        const nombre   = b.brawlerNombre || b.nombre || b.name || "Brawler";
        const winR     = typeof b.winRate === "number" ? b.winRate : null;
        const winrate  = winR == null ? "-" : winR.toFixed(2);
        const partidas = b.pickRate ?? b.partidas ?? b.matches ?? "-";

        html += `
            <tr>
                <td>${idx + 1}</td>
                <td>${nombre}</td>
                <td>${winrate}${winrate !== "-" ? "%" : ""}</td>
                <td>${partidas}</td>
            </tr>
        `;
    });

    html += `
            </tbody>
        </table>
    `;

    container.innerHTML = html;
}


async function cargarTierlists() {
    const cont = document.getElementById("tierlistsContainer");

    try {
        const res = await fetch(`${API_BASE}/tierlists`);
        if (!res.ok) {
            cont.innerHTML = "<p>Error cargando tierlists.</p>";
            return;
        }

        const lista = await res.json();

        if (!lista.length) {
            cont.innerHTML = "<p>No hay tierlists aún.</p>";
            return;
        }

        let html = "";
        lista.forEach(t => {
            html += `
                <div class="tierlist-card">
                    <h3>${t.nombre}</h3>
                    <p>Creador: ${t.usuario?.username ?? "Anónimo"}</p>

                    <button class="btn"
                        onclick="window.location.href='tierlist_ver.html?id=${t.id}'">
                        Ver Tierlist
                    </button>
                </div>
            `;
        });

        cont.innerHTML = html;

    } catch(err) {
        console.error(err);
        cont.innerHTML = "<p>Error inesperado.</p>";
    }
}


async function cargarBrawlersParaTierlist() {

    const cont = document.getElementById("brawlersContainer");
    if (!cont) return;

    cont.innerHTML = "<p>Cargando brawlers...</p>";

    try {
        const res = await fetch(`${API_BASE}/brawl/brawlers`);

        if (!res.ok) {
            cont.innerHTML = "<p>Error cargando brawlers.</p>";
            return;
        }

        const brawlers = await res.json();

        let html = '<div class="brawlers-grid-tier">';

        brawlers.forEach(b => {
            html += `
                <div class="brawler-card-tier"
                     draggable="true"
                     data-id="${b.id}">
                    <img src="${b.iconUrl}" alt="${b.nombre}">
                    <p>${b.nombre}</p>
                </div>
            `;
        });

        html += "</div>";

        cont.innerHTML = html;

    } catch (error) {
        console.error("Error cargando brawlers: ", error);
        cont.innerHTML = "<p>Error inesperado cargando brawlers.</p>";
    }
}

