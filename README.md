# Notes app (Android version)

Este repositorio contiene una pequeña aplicación Android cuyo objetivo es mostrar implementación de
historias de usuario, pruebas de aceptación y de integración.

Este es un proyecto de ejemplo para las asignaturas Diseño de Software (EI1039) y Paradigmas de
Software (EI1048) del Grado en Ingeniería Informática de la Universitat Jaume I de Castellón, España.

> [!NOTE] 
> Para obtener la especificación de las historias de usuario, así como otros proyectos de
> ejemplo, visitar el siguiente [repositorio](https://github.com/matey97/NotesAppVersions).

## Tecnologías empleadas

La aplicación en este repositorio es una aplicación Android nativa desarrollada empleando las siguientes tecnologías:

- Lenguaje de programación: Kotlin.
- UI: [Jetpack Compose](https://developer.android.com/jetpack/compose), implementa [Material Design 3](https://m3.material.io).
- DB: [Room](https://developer.android.com/training/data-storage/room), almacena datos de forma local en una SQLite.
- Tests: [JUnit](https://junit.org/junit4/) + [Mockito](https://site.mockito.org)

### Herramientas necesarias

- Descargar la última versión de Android Studio a través de su [web](https://developer.android.com/studio) o
  de la [JetBrains Toolbox](https://www.jetbrains.com/toolbox-app/).
> [!WARNING]
> Android Studio incluye el JDK17, necesario para trabajar con las últimas versiones de
> Gradle. Puede que tengáis problemas para compilar la aplicación si ya teníais otro JDK instalado.
> Para solucionarlo: `Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK` y
> seleccionar `Embedded JDK` o cualquier JDK >= 17.
- Dispositivo móvil Android físico o emulado (que el ordenador virtualización).

> [!TIP]
> Si surge cualquier problema, no dudéis en poneros en contacto.

## Estructura del repositorio

El contenido que nos interesa esta en `app/src/`, donde tenemos el código de la aplicación y las pruebas

### La aplicación: `main`

Contiene los siguientes directorios:

- [`data`](app/src/main/java/com/ei1039_1048/notesapp/data): definición del modelo de datos y del repositorio. A destacar:
    - Existen **dos** modelos de datos, `Note` y `LocalNote`. `Note` se corresponde con el modelo de la aplicación mientras que `LocalNote` representa el modelo de la DB.
    - `NoteRepository`: interfaz que define los métodos que debe cumplir un repositorio. Permitiría intercambiar distintos repositorios (p.ej., local, remoto).
- [`exceptions`](app/src/main/java/com/ei1039_1048/notesapp/exceptions): definición de excepciones.
- [`ui`](app/src/main/java/com/ei1039_1048/notesapp/ui): definición de componentes de interfaz y de estilos (tema de la aplicación).

Contiene tres componentes importantes:

- [`NotesViewModel`](app/src/main/java/com/ei1039_1048/notesapp/NotesViewModel.kt): contiene el estado de la vista y define las operaciones que pueden realizarse desde la misma.
  Aunque la funcionalidad puede implementarse en el propio ViewModel, se ha extraído al controlador para mejorar la testabilidad.
- [`NotesController`](app/src/main/java/com/ei1039_1048/notesapp/NotesController.kt): implementa las operaciones de gestión de notas. Tiene como dependencia `NoteRepository`.
- [`NotesActivity`](app/src/main/java/com/ei1039_1048/notesapp/NotesActivity.kt): punto de entrada de la aplicación. Se instancia el repositorio, el controlador y se enlazan al view model.

### Las pruebas: `test` y `androidTest`

En Android, existen dos tipos de pruebas. Las pruebas bajo el directorio [`test`](app/src/test)
se ejecutan en la máquina de desarrollo, mientras que las pruebas del directorio [`androidTest`](app/src/androidTest)
se ejecutan en el dispositivo móvil Android (físico o emulado). Estas últimas también se llaman tests instrumentalizados y son útiles
para hacer pruebas de interfaz o probar funcionalidades con dependencias que requieren de ser ejecutadas en el dispositivo móvil.

Por tanto, las pruebas se organizan de la siguiente forma:
- [`androidTest`](app/src/androidTest): contiene un fichero con pruebas de aceptación sobre el componente `NotesController`. Se utilizan dependencias reales (base de datos).
- [`test`](app/src/androidTest): contiene pruebas de integración análogas a las de aceptación. En estas pruebas, se inyecta un _mock_ de la base de datos a `NotesController`.

## Autor

<a href="https://github.com/matey97" title="Miguel Matey Sanz">
  <img src="https://avatars3.githubusercontent.com/u/25453537?s=120" alt="Miguel Matey Sanz" width="120"/>
</a>

## Licencia

El código de este repositorio esta bajo la licencia Apache 2.0 (ver [LICENSE](LICENSE)).