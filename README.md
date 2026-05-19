Plataforma web de análisis de datos para el videojuego móvil de Supercell,
**[Brawl Stars](https://supercell.com/en/games/brawlstars/)**. En ella podrás
vincular tus cuentas para acceder a tus estadísticas personales y a las de todos
tus brawlers. También permite consultar el catálogo completo de mapas, conocer
los mejores personajes para cada uno y visualizar la rotación actual de eventos
en tiempo real.

Todo esto se complementa con un creador de **tierlists interactivo** (con
sistema *Drag and Drop*) y un sistema de valoración mediante estrellas y
comentarios. Para el desarrollo de esta plataforma se integraron la **API
oficial de Brawl Stars** y la API de **Brawlify**.

---

### Características Principales

* **Analítica Real:** Consulta de trofeos, victorias y estadísticas de jugadores
  mediante etiquetas.
* **Algoritmo de Winrate:** Listado de los 10 mejores Brawlers por mapa basado
  en rendimiento real.
* **Map Tracker:** Visualización de la rotación actual de mapas y eventos.
* **Tierlist Builder:** Herramienta interactiva con sistema *drag & drop* para
  crear rankings.
* **Seguridad JWT:** Sistema de registro y login protegido con Spring Security y
  tokens.
* **Reseñas:** Sistema interno para dejar feedback sobre la plataforma.

---

### Stack Tecnológico & Dependencias

#### Backend (Java 17)
Gestionado con **Maven**, el proyecto utiliza las siguientes librerías core:

* **Spring Boot Starter Web:** Desarrollo de la API REST.
* **Spring Data JPA:** Comunicación con la base de datos MySQL.
* **Spring Security & JJWT:** Seguridad y autenticación mediante JSON Web
  Tokens.
* **HttpClient 5:** Cliente de alto rendimiento para el consumo de las APIs de
  Supercell.
* **Lombok:** Optimización de código (Getters/Setters automáticos).
* **Validation:** Validación de datos en los formularios y DTOs.

#### Frontend
* **HTML5 / CSS3 / JavaScript (ES6).**
* **Fetch API:** Para el consumo del backend.
* **Drag & Drop API:** Para el generador de tierlists.

---

### Infraestructura y Base de Datos 
> [!NOTE] Esta sección se encuentra actualmente en desarrollo. Se está
> trabajando en la implementación de **Docker** para automatizar el despliegue
> de la base de datos MySQL. En cuanto esté finalizado, se añadirán las
> instrucciones necesarias en el apartado de instalación.

---

### Build (Instalación)

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/LucasMelendrezBarbeito/BrawlLMBanalytics.git](https://github.com/LucasMelendrezBarbeito/BrawlLMBanalytics.git)
   ```

2. **Configurar las variables de entorno:** Edita el archivo
   `src/main/resources/application.properties` con tus credenciales:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/nombre_tu_db
   brawl.api.token=Bearer TU_TOKEN_AQUI
   ```

   > [!IMPORTANT] Debes generar tu token y registrar tu IP en el [Portal de
   > Desarrolladores de Supercell](https://developer.brawlstars.com/).

3. **Ejecución del proyecto:**
   ```bash
   mvn spring-boot:run
   ```

---

### Estructura del Proyecto

```text
backend/
 ├── src/main/java/com/brawllmbanalytics/
 │    ├── controllers/  # Puntos de entrada de la API
 │    ├── services/     # Lógica de negocio y llamadas a APIs externas
 │    ├── repositories/ # Interfaces de acceso a datos
 │    ├── security/     # Configuración de JWT y filtros
 │    └── dto/          # Objetos de transferencia de datos
 └── src/main/resources/application.properties # Configuración global
```

---

### Futuras Mejoras
- [x] *Eliminación de Lombok del proyecto.*
- [ ] Cambio del build de la base de datos a Docker.
- [ ] Integración de nuevos roles con sus respectivas funciones.
- [ ] Estadísticas sobre Rankeds y más contenido competitivo.
- [x] *Coming soon...*

---

### Autor
Proyecto personal realizado por **Lucas Meléndrez Barbeito**