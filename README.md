BrawlLMB Analytics

Plataforma web de análisis de datos de Brawl Stars desarrollada con Spring Boot, HTML, CSS y JavaScript, integrada con la API oficial de Supercell y la API secundaria Brawlify.

Permite consultar estadísticas de jugadores, mapas, brawlers y crear tierlists personalizadas, todo desde una interfaz sencilla y visual.

🚀 Características principales

🔐 Autenticación JWT y sistema de usuarios.

📊 Consulta de estadísticas reales de jugadores mediante la API oficial de Brawl Stars.

🗺️ Visualización de mapas en rotación y del listado completo de mapas.

🧠 Cálculo automático de los 10 mejores brawlers por winrate para cada mapa.

🪁 Creador de tierlists con sistema drag & drop.

⭐ Sistema básico de reseñas dentro de la plataforma.

⚡ Backend modular con arquitectura en capas (Controladores, Servicios, Repositorios, DTOs).

🌐 Integración con APIs externas + manejo de errores 403/500.

🛠️ Tecnologías utilizadas
Backend

Java 17

Spring Boot

Spring Security + JWT

Maven

HttpClient / RestTemplate

API Brawl Stars

API Brawlify

Frontend

HTML5

CSS3

JavaScript (ES6)

Fetch API

Drag & Drop API

📁 Estructura del proyecto
backend/
 ├── src/
 │   ├── main/
 │   │   ├── java/com/brawllmbanalytics/
 │   │   │   ├── controllers/
 │   │   │   ├── services/
 │   │   │   ├── repositories/
 │   │   │   ├── security/
 │   │   │   └── dto/
 │   │   └── resources/
 │   └── test/
 ├── pom.xml
 └── BackendApplication.java

🔧 Instalación y ejecución
1. Clonar el repositorio
git clone https://github.com/LucasMelendrezBarbeito/BrawlLMBanalytics.git

2. Entrar en el backend
cd backend

3. Ejecutar el proyecto
mvn spring-boot:run

🔐 Requisitos

Para que el backend funcione se necesita:

Token de la API oficial de Brawl Stars

Añadir tu IP en la whitelist de Supercell

Configurar tu token en application.properties:

brawl.api.token=Bearer TU_TOKEN_AQUI

Este proyecto ha sido desarrollado con fines educativos.

👤 Autor

Lucas Meléndrez Barbeito
Proyecto del módulo DAW – Proxecto de Desenvolvemento Web (IES Fernando Wirtz Suárez)
