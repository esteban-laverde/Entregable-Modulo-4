# Jenkins Pipeline - Selenium Java y Playwright Python

Este proyecto contiene el `Jenkinsfile` para ejecutar dos proyectos de automatización BDD ubicados como carpetas dentro del mismo repositorio.

No se trabaja con archivos comprimidos. Los proyectos deben estar descomprimidos y versionados directamente en Git.

## Estructura esperada del repositorio

```text
Entregable_Modulo_4/
│
├── Jenkinsfile
├── SeleniumPomLab/
└── PlaywrightPomLab/
```

## Proyectos integrados

### 1. SeleniumPomLab

Proyecto Selenium con Java, Gradle, Cucumber, POM y Allure.

El stage de Jenkins ejecuta los **2 escenarios BDD** del proyecto:

```text
1. Agregar Samsung galaxy s6 y Sony vaio i5 al carrito y validar ambos productos.
2. Eliminar los productos del carrito y validar que el carrito quede vacío.
```

La tarea Gradle usada por Jenkins es:

```powershell
gradle testJenkins
```

La tarea debe ejecutarse en modo headless y debe generar:

```text
SeleniumPomLab/build/test-results/test/*.xml
SeleniumPomLab/build/reports/**
SeleniumPomLab/build/allure-results/**
SeleniumPomLab/build/evidences/screenshots/**
```

### 2. PlaywrightPomLab

Proyecto Playwright con Python, pytest-bdd, POM y Allure.

El proyecto tiene 2 escenarios BDD, pero el stage de Jenkins ejecuta **solo 1 escenario** usando el marcador `@carrito`:

```text
Agregar Samsung galaxy s6 e Iphone 6 32gb al carrito y validar ambos productos.
```

El stage configura esta variable antes de ejecutar Gradle:

```powershell
PYTEST_ADDOPTS="-m carrito"
```

La tarea Gradle usada por Jenkins es:

```powershell
gradle testJenkins
```

La tarea debe generar:

```text
PlaywrightPomLab/reports/junit/*.xml
PlaywrightPomLab/reports/**
PlaywrightPomLab/allure-results/**
```

Si se quiere ejecutar solo el escenario de compra, en el `Jenkinsfile` se puede cambiar:

```text
-m carrito
```

por:

```text
-m compra
```

## Jenkinsfile

El pipeline tiene estos stages:

```text
1. Checkout
2. Selenium Java - BDD Demoblaze
3. Playwright Python - BDD Demoblaze
```

### Stage 1: Selenium Java

Este stage:

```text
- Entra a SeleniumPomLab.
- Ejecuta gradle testJenkins.
- Ejecuta los 2 escenarios BDD de Selenium.
- Publica resultados JUnit.
- Archiva reportes y evidencias.
- Publica Allure Report desde Jenkins.
```

### Stage 2: Playwright Python

Este stage:

```text
- Entra a PlaywrightPomLab.
- Crea y activa un entorno virtual Python.
- Configura PYTEST_ADDOPTS="-m carrito" para ejecutar solo 1 escenario.
- Ejecuta gradle testJenkins.
- Publica resultados JUnit.
- Archiva reportes y evidencias.
- Publica Allure Report desde Jenkins.
```

## Requisitos del agente Jenkins

El agente donde se ejecute el pipeline debe tener instalado:

```text
Git
Java 17 o Java 21
Gradle
Python 3.12
pip
Chrome o Chromium
Allure Commandline
```

Además, Jenkins debe tener estos plugins:

```text
Pipeline
Git Plugin
JUnit
Allure Jenkins Plugin
```

Si Jenkins está corriendo en contenedor, el contenedor debe tener las herramientas anteriores instaladas. Selenium necesita Chrome o Chromium disponible dentro del agente.

## Configurar Allure en Jenkins

1. Entra a Jenkins.
2. Ve a `Manage Jenkins`.
3. Entra a `Plugins`.
4. Instala `Allure Jenkins Plugin`, si aún no está instalado.
5. Ve a `Manage Jenkins` > `Tools`.
6. Busca `Allure Commandline`.
7. Agrega una instalación de Allure.
8. Guarda los cambios.

Cuando el pipeline finalice, el build mostrará el acceso a:

```text
Allure Report
```

Desde ahí puedes abrir los resultados en el navegador directamente en Jenkins.

## Guía paso a paso para crear la tarea Pipeline en Jenkins

### 1. Subir el proyecto a Git

Sube el repositorio con esta estructura:

```text
Entregable_Modulo_4/
│
├── Jenkinsfile
├── SeleniumPomLab/
└── PlaywrightPomLab/
```

El `Jenkinsfile` debe quedar en la raíz.

### 2. Crear una nueva tarea en Jenkins

1. Entra a Jenkins.
2. Da clic en `New Item` o `Nueva tarea`.
3. Escribe un nombre, por ejemplo:

```text
Entregable-Modulo-4-QA
```

4. Selecciona:

```text
Pipeline
```

5. Da clic en `OK`.

### 3. Configurar el pipeline desde Git

En la configuración de la tarea:

1. Baja hasta la sección `Pipeline`.
2. En `Definition`, selecciona:

```text
Pipeline script from SCM
```

3. En `SCM`, selecciona:

```text
Git
```

4. En `Repository URL`, pega la URL de tu repositorio.
5. En `Branch Specifier`, usa la rama correspondiente, por ejemplo:

```text
*/main
```

Si tu rama se llama `master`, usa:

```text
*/master
```

6. En `Script Path`, deja:

```text
Jenkinsfile
```

7. Da clic en `Save`.

### 4. Ejecutar el pipeline

1. Entra a la tarea creada.
2. Da clic en:

```text
Build Now
```

3. Abre el build en ejecución.
4. Revisa la consola en:

```text
Console Output
```

### 5. Revisar resultados

Al finalizar el build, revisa:

```text
Test Result
Allure Report
Artifacts
```

En `Test Result` verás los resultados JUnit.

En `Allure Report` verás el reporte de ejecución publicado por Jenkins.

En `Artifacts` quedarán archivados reportes HTML, resultados Allure y screenshots en fallos.

## Comandos locales de referencia

### Selenium local

```powershell
cd SeleniumPomLab
gradle testJenkins
```

### Playwright local ejecutando solo el escenario de carrito

```powershell
cd PlaywrightPomLab
$env:PYTEST_ADDOPTS="-m carrito"
gradle testJenkins
```

### Playwright local ejecutando solo el escenario de compra

```powershell
cd PlaywrightPomLab
$env:PYTEST_ADDOPTS="-m compra"
gradle testJenkins
```

## Consideraciones importantes

- Jenkins no debe abrir un navegador físico.
- Los navegadores deben ejecutarse en modo headless.
- Allure se debe publicar desde Jenkins mediante el plugin de Allure.
- Para Selenium se ejecutan los 2 escenarios BDD.
- Para Playwright se ejecuta solo 1 escenario BDD mediante marcador.
- El archivo `COMO_ARMAR_REPOSITORIO.md` ya no es necesario porque los proyectos no se manejan como ZIP, sino como carpetas dentro del repositorio.
