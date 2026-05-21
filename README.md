# 🐾 PetAdopt

**Sistema de gestión de adopción de mascotas — Examen Corte 2**
Universidad Americana (UAM) · Programación Móvil

PetAdopt es una aplicación Android nativa para administrar el catálogo de un refugio de mascotas. Permite registrar nuevos animales, marcar favoritas, registrar adopciones y buscar y filtrar el catálogo en tiempo real.

---

## ✨ Características principales

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

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Kotlin | 2.2.10 | Lenguaje principal |
| Jetpack Compose BOM | 2026.02.01 | UI declarativa |
| Material 3 | (vía BOM) | Componentes visuales |
| Navigation Compose | 2.9.7 | Navegación |
| DataStore Preferences | 1.2.1 | Persistencia |
| Kotlinx Serialization | 1.10.0 | Serialización JSON |
| Lifecycle ViewModel | 2.10.0 | Manejo de estado |
| Android Gradle Plugin | 9.2.1 | Build |
| min SDK / target SDK | 24 / 36 | Compatibilidad Android 7.0+ |

---

## 🏗️ Arquitectura

```
ni.edu.uam/
├── MainActivity.kt              # Punto de entrada
├── data/
│   └── MascotaRepository.kt     # Capa de persistencia (DataStore)
├── model/
│   └── Mascota.kt               # Modelos (Mascota, TipoMascota, Genero)
├── navigation/
│   ├── Screen.kt                # Definición de rutas
│   └── PetAdoptNavHost.kt       # Grafo de navegación
├── viewmodel/
│   └── PetViewModel.kt          # Estado, CRUD, filtros, estadísticas
└── ui/
    ├── theme/                   # Color, Theme, Type
    ├── components/              # Componentes reutilizables
    │   ├── MascotaCard.kt
    │   ├── FormularioMascota.kt
    │   ├── PetAdoptBottomBar.kt
    │   ├── Filtros.kt
    │   └── TarjetaEstadistica.kt
    └── screens/                 # Una pantalla por archivo
        ├── SplashScreen.kt
        ├── InicioScreen.kt
        ├── DetalleScreen.kt
        ├── RegistrarMascotaScreen.kt
        ├── EditarMascotaScreen.kt
        ├── FavoritosScreen.kt
        └── AcercaDeScreen.kt
```

Sigue el patrón **MVVM** con **Repository Pattern** y **flujo de datos unidireccional**:

- El **ViewModel** mantiene la única fuente de verdad mediante `StateFlow`.
- Las pantallas observan el estado con `collectAsState()` y disparan acciones.
- El **Repository** abstrae el origen de datos (en este caso DataStore).

---

## 🚀 Cómo ejecutar

### Requisitos

- **Android Studio Otter 3 Feature Drop (2025.2.3)** o superior (necesario para AGP 9.2).
- **JDK 17** o superior.
- Dispositivo o emulador con **Android 7.0 (API 24)** o superior.

### Pasos

1. Abre el proyecto en Android Studio (`File → Open` → selecciona la carpeta `PetAdopt`).
2. Espera a que Gradle sincronice las dependencias (la primera vez puede tardar varios minutos).
3. Conecta un dispositivo Android físico (con depuración USB activada) o crea un emulador con API 24+.
4. Pulsa el botón **Run** (▶) o `Shift + F10`.
5. La app se instalará y mostrará el splash; al pasar verás el catálogo con 5 mascotas de ejemplo.

---

## 📱 Flujo de uso

1. **Splash** (2 s) → **Inicio**.
2. En **Inicio** ves estadísticas, buscas, filtras y tocas cualquier mascota para ver su detalle.
3. En **Detalle** puedes editarla, eliminarla, marcarla como favorita o como adoptada.
4. El **FAB (+)** abre el formulario de **Registrar**.
5. La pestaña **Favoritos** muestra solo las marcadas con corazón.
6. La pestaña **Acerca de** describe la app y muestra el resumen del refugio.

---

## 🎨 Paleta de colores

| Color | Hex | Uso |
|-------|-----|-----|
| Naranja primario | `#FF7043` | Header, botones principales, FAB |
| Naranja oscuro | `#E64A19` | Acentos, texto sobre durazno |
| Durazno claro | `#FFCCBC` | Contenedores primarios, chips |
| Crema | `#FFF8F3` | Fondo de pantallas |
| Marrón | `#6D4C41` | Color secundario |
| Verde | `#66BB6A` | Acciones positivas (adoptar) |
| Rojo | `#E53935` | Eliminar, errores |
| Amarillo | `#FFC107` | Favoritos |

---

## 🔁 Git: convención de commits

El proyecto sigue [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` nueva funcionalidad
- `fix:` corrección de errores
- `style:` ajustes visuales o de formato
- `refactor:` refactorización sin cambio de comportamiento
- `docs:` documentación
- `chore:` tareas de mantenimiento

---

## 📄 Licencia

Proyecto académico para el curso de Programación Móvil (UAM).
