# Feature Specification: Desvinculación de Cuentas

**Feature Branch**: `feature/unlink-accounts`

**Created**: 2026-07-11

**Status**: Draft

**Input**: User description: "Desvinculación de cuentas: Front end: En la vista cuentas.html vamos a añadir una opción de eliminar cuenta con su respectivo botón en el div. Backend: Se va a crear un metodo de delete que se llame con su respectivo endpoint. No habrá violaciones a la constitución ni se romperá la seguridad, tampoco el flujo de trabajo."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Eliminar cuenta vinculada (Priority: P1)

Como usuario autenticado, quiero poder eliminar una cuenta de Brawl Stars que vinculé previamente, para deshacerme de cuentas que ya no uso o que vinculé por error.

**Why this priority**: Es la funcionalidad core de la feature. Sin ella, no existe la feature. Los usuarios actualmente no tienen forma de desvincular cuentas una vez añadidas.

**Independent Test**: Puede probarse completamente iniciando sesión, vinculando una cuenta, y pulsando el botón de eliminar en la vista de cuentas vinculadas. La cuenta debe desaparecer de la lista.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado con una cuenta vinculada, **When** pulsa el botón "Eliminar" en la tarjeta de esa cuenta, **Then** el sistema muestra una confirmación y, al confirmar, la cuenta se elimina de la lista.
2. **Given** un usuario autenticado con una cuenta vinculada, **When** pulsa "Eliminar" y cancela la confirmación, **Then** la cuenta permanece sin cambios.
3. **Given** un usuario autenticado con múltiples cuentas vinculadas, **When** elimina una de ellas, **Then** las demás cuentas permanecen intactas.

---

### User Story 2 - Feedback visual tras eliminación (Priority: P2)

Como usuario, quiero ver un mensaje de confirmación tras eliminar una cuenta y que la lista se actualice automáticamente, para saber que la acción se completó correctamente.

**Why this priority**: Mejora la experiencia del usuario. Sin feedback, el usuario no sabe si la acción tuvo efecto.

**Independent Test**: Tras eliminar una cuenta, verificar que aparece un mensaje de éxito y la cuenta desaparece de la lista sin necesidad de recargar la página.

**Acceptance Scenarios**:

1. **Given** un usuario confirma la eliminación de una cuenta, **When** el backend confirma la eliminación, **Then** se muestra un mensaje de éxito y la lista se recarga automáticamente.
2. **Given** un error del servidor al eliminar, **When** la petición falla, **Then** se muestra un mensaje de error claro.

---

### Edge Cases

- ¿Qué pasa si el usuario intenta eliminar una cuenta que ya fue eliminada por otro medio (otra pestaña)?
- ¿Qué pasa si el usuario no está autenticado y accede a la vista de cuentas?
- ¿Qué pasa si el servidor devuelve un error 500 al intentar eliminar?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema DEBE permitir a un usuario autenticado eliminar una cuenta de Brawl Stars previamente vinculada a su perfil.
- **FR-002**: El endpoint de eliminación DEBE derivar la identidad del usuario del token JWT, nunca de parámetros del request (anti-IDOR).
- **FR-003**: El frontend DEBE mostrar una confirmación antes de ejecutar la eliminación.
- **FR-004**: Tras eliminación exitosa, el frontend DEBE recargar la lista de cuentas vinculadas.
- **FR-005**: El endpoint DEBE retornar un error apropiado (403) si el usuario intenta eliminar una cuenta que no le pertenece.
- **FR-006**: La eliminación DEBE ser permanente (hard delete, no soft delete).

### Key Entities

- **CuentaBrawl**: Representa una cuenta de Brawl Stars vinculada a un usuario. Atributos clave: id, tag, nombre, trofeos, nivel, usuario (FK).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Los usuarios pueden eliminar una cuenta vinculada en menos de 5 segundos (2 clics: eliminar + confirmar).
- **SC-002**: El 100% de las eliminaciones exitosas actualizan la lista sin necesidad de recarga manual.
- **SC-003**: Un usuario no puede eliminar cuentas de otros usuarios (0 violaciones de autorización).

## Assumptions

- El usuario está autenticado y tiene un token JWT válido.
- La tabla `cuentas_brawl` en MySQL tiene una FK `usuario_id` que permite identificar el propietario.
- La eliminación es a nivel de base de datos (hard delete), no se necesita soft delete ni auditoría de eliminaciones por ahora.
- No se requiere notificación a la API de Supercell al desvincular.
