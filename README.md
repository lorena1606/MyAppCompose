Aplicación Android para la gestión personal de tareas, desarrollada como proyecto del programa de Análisis y Desarrollo de Software del SENA.

La aplicación permite a los usuarios registrarse e iniciar sesión, crear, consultar, actualizar y eliminar tareas almacenadas en Firebase Firestore. También permite guardar borradores de tareas localmente mediante Room para conservar la información incluso cuando la aplicación se reinicia.

Funcionalidades
Registro de usuarios.
Inicio de sesión con Firebase Authentication.
Persistencia de sesión.
Cierre de sesión.
Protección de las pantallas de usuario autenticado.
Creación de tareas.
Consulta de tareas propias.
Edición de tareas.
Eliminación de tareas.
Separación de tareas por usuario mediante ownerId.
Almacenamiento local de borradores mediante Room.
Consulta de borradores guardados.
Publicación de borradores en Firebase Firestore.
Eliminación del borrador local después de una publicación exitosa.
Manejo de estados de carga, éxito, vacío y error.
Tecnologías utilizadas
Kotlin
Jetpack Compose
Android SDK
Firebase Authentication
Firebase Cloud Firestore
Firebase BoM
Room
KSP
Hilt
Navigation Compose
ViewModel
StateFlow
Coroutines
Git y GitHub
Arquitectura

El proyecto utiliza una arquitectura basada en MVVM y separación por capas.

UI
│
▼
ViewModel
│
▼
UseCase
│
▼
Repository
│
├──────────────► Firebase Firestore
│
└──────────────► Room
Capas principales

UI: contiene las pantallas, componentes y navegación de la aplicación.

ViewModel: coordina las acciones de la interfaz, ejecuta los casos de uso y expone el estado mediante StateFlow.

Domain: contiene los modelos de dominio, interfaces de repositorios y casos de uso.

Data: contiene las implementaciones de los repositorios, modelos de datos, mapeadores y fuentes de datos locales y remotas.

DI: contiene la configuración de Hilt para proporcionar las dependencias utilizadas por la aplicación.

Persistencia de datos
Firebase Firestore

Las tareas se almacenan en Firestore y contienen información como:

ownerId
title
description
completed
createdAt
updatedAt

El campo ownerId permite asociar cada tarea con el usuario autenticado.

Room

Room se utiliza para almacenar localmente los borradores de tareas.

Los borradores se gestionan mediante:

TaskDraftEntity
TaskDraftDao
AppDatabase

Cuando un borrador se publica correctamente en Firebase, se elimina de la base de datos local.

Seguridad

La aplicación utiliza Firebase Authentication para identificar a los usuarios.

Las reglas de Firestore controlan el acceso a las tareas mediante el uid del usuario autenticado.

Cada usuario solamente debe poder consultar y modificar sus propias tareas.

No se almacenan contraseñas directamente en Firestore ni en Room.

Estructura del proyecto
app/
└── src/
└── main/
└── java/
└── com.example.myappcompose/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   ├── remote/
│   │   └── model/
│   ├── mapper/
│   └── repository/
│
├── di/
│
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── ui/
│   ├── component/
│   ├── navigation/
│   ├── screen/
│   ├── state/
│   └── theme/
│
├── MainActivity.kt
└── TaskManagerApplication.kt
Requisitos
Android Studio.
JDK compatible con la configuración del proyecto.
Android SDK.
Un dispositivo Android o emulador.
Un proyecto configurado en Firebase.
Archivo google-services.json.
Ejecución
Clonar el repositorio.
git clone URL_DEL_REPOSITORIO
Abrir el proyecto en Android Studio.
Agregar el archivo google-services.json correspondiente al proyecto Firebase.
Sincronizar el proyecto con Gradle.
Ejecutar la aplicación en un dispositivo físico o emulador.
Flujo principal
Registro / Login
│
▼
Mis tareas
│
├── Crear tarea
├── Editar tarea
├── Eliminar tarea
│
└── Borradores
│
▼
Guardar localmente
│
▼
Publicar
│
▼
Firebase Firestore
Autor

Michell Lorena Roldan Moncada

Proyecto académico desarrollado para el programa de Análisis y Desarrollo de Software – SENA.