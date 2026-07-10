# [PROJECT_NAME] Constitution
<!-- Example: Spec Constitution, TaskFlow Constitution, etc. -->

## Core Principles

### [PRINCIPLE_1_NAME]
<!-- Example: I. Library-First -->
[PRINCIPLE_1_DESCRIPTION]
<!-- Example: Every feature starts as a standalone library; Libraries must be self-contained, independently testable, documented; Clear purpose required - no organizational-only libraries -->

### [PRINCIPLE_2_NAME]
<!-- Example: II. CLI Interface -->
[PRINCIPLE_2_DESCRIPTION]
<!-- Example: Every library exposes functionality via CLI; Text in/out protocol: stdin/args → stdout, errors → stderr; Support JSON + human-readable formats -->

### [PRINCIPLE_3_NAME]
<!-- Example: III. Test-First (NON-NEGOTIABLE) -->
[PRINCIPLE_3_DESCRIPTION]
<!-- Example: TDD mandatory: Tests written → User approved → Tests fail → Then implement; Red-Green-Refactor cycle strictly enforced -->

### [PRINCIPLE_4_NAME]
<!-- Example: IV. Integration Testing -->
[PRINCIPLE_4_DESCRIPTION]
<!-- Example: Focus areas requiring integration tests: New library contract tests, Contract changes, Inter-service communication, Shared schemas -->

### [PRINCIPLE_5_NAME]
<!-- Example: V. Observability, VI. Versioning & Breaking Changes, VII. Simplicity -->
[PRINCIPLE_5_DESCRIPTION]
<!-- Example: Text I/O ensures debuggability; Structured logging required; Or: MAJOR.MINOR.BUILD format; Or: Start simple, YAGNI principles -->

## [SECTION_2_NAME]
<!-- Example: Additional Constraints, Security Requirements, Performance Standards, etc. -->

[SECTION_2_CONTENT]
<!-- Example: Technology stack requirements, compliance standards, deployment policies, etc. -->

## [SECTION_3_NAME]
<!-- Example: Development Workflow, Review Process, Quality Gates, etc. -->

[SECTION_3_CONTENT]
<!-- Example: Code review requirements, testing gates, deployment approval process, etc. -->

## Governance
<!-- Example: Constitution supersedes all other practices; Amendments require documentation, approval, migration plan -->

[GOVERNANCE_RULES]
<!-- Example: All PRs/reviews must verify compliance; Complexity must be justified; Use [GUIDANCE_FILE] for runtime development guidance -->

**Version**: [CONSTITUTION_VERSION] | **Ratified**: [RATIFICATION_DATE] | **Last Amended**: [LAST_AMENDED_DATE]
<!-- Example: Version: 2.1.1 | Ratified: 2025-06-13 | Last Amended: 2025-07-16 -->
<!--
SYNC IMPACT REPORT
Version change: (plantilla sin versionar) → 1.0.0 → 1.1.0
Motivo del bump:
  - 1.0.0: Ratificación inicial. Se sustituyen todos los placeholders de la plantilla
    por principios concretos del proyecto (MAJOR = establecimiento de la gobernanza).
  - 1.1.0: Se añade a "Flujo de Desarrollo" la regla de commit tras cada cambio
    verificado (MINOR = nueva guía, sin redefinir principios existentes).

Principios definidos (antes → ahora):
  [PRINCIPLE_1] → I. Seguridad primero (NO NEGOCIABLE)
  [PRINCIPLE_2] → II. DTOs como contrato, entidades JPA nunca expuestas
  [PRINCIPLE_3] → III. Arquitectura en capas e inyección por constructor
  [PRINCIPLE_4] → IV. Convenciones del proyecto
  [PRINCIPLE_5] → V. Calidad verificable

Secciones:
  [SECTION_2] → Restricciones Técnicas y de Stack (añadida)
  [SECTION_3] → Flujo de Desarrollo (añadida)
  Governance → concretada

Plantillas dependientes:
  ✅ .specify/templates/plan-template.md — "Constitution Check" es un gate genérico,
     no requiere edición (referencia la constitution de forma abstracta)
  ✅ .specify/templates/spec-template.md — sin gates de principios, no requiere edición
  ✅ .specify/templates/tasks-template.md — categorización por historia de usuario,
     no requiere edición
  ✅ CLAUDE.md — coherente con los principios (guía de runtime referenciada en Governance)

TODOs diferidos: ninguno.
-->

# BrawlLMBanalytics Constitution

## Core Principles

### I. Seguridad primero (NO NEGOCIABLE)

La seguridad prevalece sobre la conveniencia. Reglas de obligado cumplimiento:

- **Sin secrets en el código ni en el repositorio.** Tokens (Supercell), `jwt.secret`
  y credenciales de BD DEBEN leerse de variables de entorno, nunca hardcodearse ni
  commitearse. Un secret expuesto se considera comprometido y DEBE rotarse.
- **Contraseñas siempre con BCrypt.** Nunca en texto plano ni con hashes reversibles.
- **Autenticación JWT stateless (HS256).** Sin estado de sesión en servidor.
- **Nunca exponer entidades JPA con datos sensibles** (`password`, `email`) en las
  respuestas JSON. Se usa `@JsonIgnore`/proyección/DTO para filtrar.
- **La autorización se deriva SIEMPRE del token autenticado**, jamás de un parámetro
  del request (`usuarioId` de query/body está prohibido como fuente de identidad):
  evita IDOR.
- **Toda entrada se valida y sanea** (Bean Validation en DTOs; codificación de salida
  para prevenir XSS; consultas parametrizadas/JPA para prevenir inyección).

**Rationale**: la plataforma maneja cuentas de usuario y datos vinculados; una fuga de
credenciales o una suplantación por IDOR es el peor fallo posible y ya se han detectado
huecos de este tipo en el proyecto.

### II. DTOs como contrato, entidades JPA nunca expuestas

La capa de API se comunica mediante DTOs, no entidades. Reglas:

- Los DTOs son tipos `record` de Java bajo `dto/`.
- Los controllers consumen y devuelven DTOs; NUNCA exponen entidades JPA directamente.
- **Lombok está prohibido.** Fue eliminado deliberadamente; no se reintroduce. Getters,
  setters, constructores y accessors de `record` se escriben/derivan explícitamente.

**Rationale**: desacopla el modelo de persistencia del contrato público, evita fugas de
campos sensibles y elimina la magia de anotaciones que ya causó roturas de compilación.

### III. Arquitectura en capas e inyección por constructor

Flujo unidireccional de dependencias: `controllers → services → repositories`.

- **Controllers** (`@RestController`): sin lógica de negocio; orquestan y validan.
- **Services** (`@Service`): contienen la lógica de negocio, son stateless y usan
  `@Transactional` al nivel más granular necesario.
- **Repositories** (`@Repository`): Spring Data JPA (`JpaRepository`).
- **Inyección por constructor** con campos `private final`. Se evita la inyección por
  campo salvo justificación.

**Rationale**: componentes testeables, dependencias explícitas e inmutables, y separación
de responsabilidades clara.

### IV. Convenciones del proyecto

La coherencia con lo existente es obligatoria:

- **Nomenclatura en español** para rutas de API, entidades y tablas
  (`/mapas`, `usuarios`, `brawlers`, …).
- Stack fijo: **Spring Boot 3.3 / Java 17 / MySQL** con `ddl-auto=none`
  (el esquema se provisiona manualmente; las entidades no crean tablas).
- **Manejo de errores centralizado** con `@ControllerAdvice`; no exponer stack traces.
- **Logging con SLF4J** parametrizado; prohibido `System.out`/`printStackTrace` en
  código productivo.
- Al añadir una llamada HTTP externa, se sigue el stack del servicio vecino:
  `java.net.http.HttpClient` para la API oficial de Supercell y `RestTemplate` para
  BrawlAPI. No se mezclan stacks arbitrariamente.

**Rationale**: un código que se lee como el de al lado es más fácil de mantener; el stack
y las convenciones ya están establecidos y documentados en `CLAUDE.md`.

### V. Calidad verificable

Nada se da por terminado sin evidencia:

- Todo cambio DEBE **compilar y arrancar** antes de considerarse hecho.
- Los controllers se prueban con `@WebMvcTest`/`MockMvc`; los repositories con
  `@DataJpaTest`. Los cambios de seguridad (auth, IDOR) DEBEN llevar test que los cubra.
- La **complejidad debe justificarse** (YAGNI): se prefiere la solución simple salvo
  necesidad demostrada, que se documenta.

**Rationale**: el proyecto arrastraba una compilación rota y solo un test de contexto;
la calidad debe ser observable, no asumida.

## Restricciones Técnicas y de Stack

- **Lenguaje/Runtime**: Java 17.
- **Framework**: Spring Boot 3.3 (módulo único), Spring Security, Spring Data JPA.
- **Base de datos**: MySQL 8 en `127.0.0.1:3306`, base `brawllmbanalytics`,
  `ddl-auto=none` (DDL manual).
- **Frontend**: HTML5/CSS3/ES6 estático servido por Spring Boot desde
  `src/main/resources/static/`. No hay build de Node/React.
- **APIs externas**: Supercell Brawl Stars API (Bearer token, IP-registrado) y
  BrawlAPI/Brawlify (sin auth).
- **Build**: Maven wrapper (`mvnw`), nunca `mvn` del sistema.

## Flujo de Desarrollo

- **Features nuevas** → Spec Kit: `specify → plan → tasks → implement`, contra esta
  constitution (el "Constitution Check" del plan debe pasar).
- **Bugfix y seguridad** → intervención directa sobre el código, sin la ceremonia de
  Spec Kit, pero respetando todos los principios.
- **Ramas y PRs**: el trabajo se hace en ramas de feature; los PR se revisan verificando
  cumplimiento de la constitution antes de fusionar.
- **Commit tras cada cambio verificado**: se commitea en cuanto algo está implementado
  Y probado (compila, la app arranca y se ejercita el comportamiento afectado). Los
  commits son pequeños y acotados a una unidad verificada. Nunca se commitean secrets
  (Principio I).

## Governance

Esta constitution prevalece sobre cualquier otra práctica. En caso de conflicto entre
una decisión puntual y un principio aquí definido, gana el principio (o se enmienda la
constitution de forma explícita).

- **Enmiendas**: se documentan en el Sync Impact Report de este archivo y se versionan
  siguiendo SemVer:
  - **MAJOR**: eliminación o redefinición incompatible de un principio/gobernanza.
  - **MINOR**: nuevo principio o sección, o ampliación material de guía.
  - **PATCH**: aclaraciones, redacción, correcciones no semánticas.
- **Cumplimiento**: todo PR/revisión verifica el cumplimiento de estos principios; las
  violaciones se justifican en la tabla de "Complexity Tracking" del plan o se rechazan.
- **Guía de runtime**: `CLAUDE.md` es la referencia operativa del día a día y debe
  mantenerse coherente con esta constitution.

**Version**: 1.1.0 | **Ratified**: 2026-07-10 | **Last Amended**: 2026-07-10
