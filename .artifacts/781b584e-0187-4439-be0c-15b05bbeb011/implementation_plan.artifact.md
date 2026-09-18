# Saneamiento Inicial y Preparación de Arquitectura

Este plan detalla las acciones necesarias para preparar el proyecto `MyAppCompose` para el desarrollo del taller, implementando Hilt para la inyección de dependencias, configurando Navigation Compose, Firebase Auth y Room, y corrigiendo inconsistencias técnicas en la estructura actual.

## User Review Required

> [!IMPORTANT]
> Se mantendrá el nombre del paquete actual `com.example.myappcompose` para evitar conflictos con el archivo `google-services.json` y configuraciones existentes de Firebase.

> [!NOTE]
> La versión de Kotlin en el proyecto es `2.2.10`. Se seleccionará la versión de KSP compatible (`2.2.10-1.0.31` o similar) para evitar errores de compilación.

## Proposed Changes

### Dependencias y Configuración [Gradle]

#### [MODIFY] [libs.versions.toml](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/gradle/libs.versions.toml)
- Agregar versiones para Hilt, KSP, Room y Navigation.
- Definir librerías y plugins correspondientes.

#### [MODIFY] [build.gradle.kts (Raíz)](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/build.gradle.kts)
- Agregar plugins de Hilt y KSP.

#### [MODIFY] [build.gradle.kts (App)](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/build.gradle.kts)
- Aplicar plugins `dagger.hilt.android.plugin` y `com.google.devtools.ksp`.
- Añadir dependencias de Hilt (android, compiler, navigation-compose), Navigation Compose, Firebase Auth y Room.

---

### Aplicación y Puntos de Entrada [Core]

#### [MODIFY] [TaskManagerApplication.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/TaskManagerApplication.kt)
- Añadir `@HiltAndroidApp` y extender de `Application()`.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/MainActivity.kt)
- Añadir `@AndroidEntryPoint`.

---

### Capa de Datos y Refactorización [Data]

#### [MODIFY] [TaskRepositoryImpl.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/data/repository/TaskRepositoryImpl.kt.kt) (Renombrar y Corregir)
- Renombrar archivo a `TaskRepositoryImpl.kt`.
- Corregir el nombre de la clase a `TaskRepositoryImpl`.
- Añadir `@Inject constructor(private val db: FirebaseFirestore)`.

#### [MODIFY] [AuthRepositoryImpl.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/data/repository/AuthRepositoryImpl.kt)
- Inyectar `FirebaseAuth` vía constructor.

---

### Inyección de Dependencias [DI]

#### [MODIFY] [FirebaseModule.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/di/FirebaseModule.kt)
- Implementar como `@Module` con `@InstallIn(SingletonComponent::class)`.
- Proveer `FirebaseFirestore` y `FirebaseAuth`.

#### [MODIFY] [RepositoryModule.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/di/RepositoryModule.kt)
- Implementar como `@Module` con `@InstallIn(SingletonComponent::class)`.
- Usar `@Binds` para inyectar las implementaciones de los repositorios.

#### [MODIFY] [DatabaseModule.kt](file:///C:/Users/57300/AndroidStudioProjects/MyAppCompose/app/src/main/java/com/example/myappcompose/di/DatabaseModule.kt)
- Implementar esqueleto inicial de Room (pendientes entidades en Fase 5).

## Verification Plan

### Automated Tests
- Ejecutar `gradle sync` para validar dependencias.
- Ejecutar `./gradlew assembleDebug` para verificar que la inyección de Hilt y el procesamiento de KSP funcionan correctamente.

### Manual Verification
- Abrir la aplicación para asegurar que no hay crashes al inicio por falta de configuración en `Application` o `MainActivity`.
