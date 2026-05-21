# PetAdopt

** Sistema de gestión de adopción de mascotas — Examen Corte 2**
Universidad Americana (UAM) · Programación Móvil

PetAdopt es una aplicación Android nativa para administrar el catálogo de un refugio de mascotas. Permite registrar nuevos animales, marcar favoritas, registrar adopciones y buscar y filtrar el catálogo en tiempo real.

---

## Características principales

- **CRUD completo** sobre mascotas: registrar, ver, editar y eliminar.
- **7 pantallas** con navegación animada: Splash, Inicio, Detalle, Registrar, Editar, Favoritos y Acerca de.
- **Barra de navegación inferior** + **botón flotante** para agregar.
- **Búsqueda en tiempo real** por nombre, tipo o raza.
- **Filtros por tipo** de mascota (8 tipos disponibles).
- **Sistema de favoritos** y marcado de adopción.
- **Tablero de estadísticas** (total, disponibles, adoptadas, favoritas) calculadas reactivamente.
- **Persistencia local** con DataStore + Kotlinx Serialization.
- **Tema cálido personalizado** (paleta naranjas/marrones/verdes) acorde a la temática.
- **5 mascotas de ejemplo** precargadas la primera vez que abres la app.
- **Validaciones** en formularios y diálogos de confirmación.
- **Snackbars** de retroalimentación tras cada acción.
  
---

## Cómo ejecutar

### Requisitos

- **Android Studio Otter 3 Feature Drop (2025.2.3)** o superior (necesario para AGP 9.2).
- **JDK 17** o superior.
- Dispositivo o emulador con **Android 7.0 (API 24)** o superior.

---

## Flujo de uso

1. **Splash** (2 s) → **Inicio**.
2. En **Inicio** ves estadísticas, buscas, filtras y tocas cualquier mascota para ver su detalle.
3. En **Detalle** puedes editarla, eliminarla, marcarla como favorita o como adoptada.
4. El **FAB (+)** abre el formulario de **Registrar**.
5. La pestaña **Favoritos** muestra solo las marcadas con corazón.
6. La pestaña **Acerca de** describe la app y muestra el resumen del refugio.

---

## Licencia

Proyecto académico para el curso de Programación Móvil (UAM).
