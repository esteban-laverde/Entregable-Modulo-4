# SeleniumPomLab

Proyecto de automatización BDD usando:

- Selenium WebDriver
- Java
- Gradle
- Cucumber
- JUnit Platform
- Allure
- Modelo POM

## Página automatizada

```text
https://www.demoblaze.com/index.html
```

## Credenciales

```text
Username: estebanlav@gmailc.om
Password: Test1234
```

También puedes enviarlas por parámetro:

```powershell
.\gradlew.bat clean test -Ddemoblaze.username="usuario" -Ddemoblaze.password="clave"
```

## Escenarios automatizados

Este proyecto contiene 2 escenarios BDD:

1. Agregar `Samsung galaxy s6` y `Sony vaio i5` al carrito y validar que ambos estén visibles.
2. Eliminar los productos del carrito y validar que el carrito quede vacío.

## Ejecutar en Windows

```powershell
.\gradlew.bat clean test
```

También puedes ejecutar con Gradle instalado:

```powershell
gradle clean test
```

## Ejecutar en Linux/macOS

```bash
./gradlew clean test
```

o:

```bash
gradle clean test
```

## Generar reporte Allure

```powershell
.\gradlew.bat clean test allureReport
```

El reporte queda en:

```text
build/reports/allure-report/allureReport/index.html
```

## Abrir reporte Cucumber

```powershell
.\gradlew.bat openCucumberReport
```

El reporte Cucumber queda en:

```text
build/reports/cucumber/cucumber.html
```

## Evidencias

- Screenshots en fallos:
  - `build/evidences/screenshots/`
- Reporte Cucumber:
  - `build/reports/cucumber/cucumber.html`
- Resultados Allure:
  - `build/allure-results/`

