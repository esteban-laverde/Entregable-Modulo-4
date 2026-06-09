# PlaywrightPomLab

Proyecto de automatización BDD usando:

- Playwright
- Python
- pytest
- pytest-bdd
- Allure
- Modelo POM
- Gradle como orquestador

## Página automatizada

```text
https://www.demoblaze.com/index.html
```

## Credenciales

```text
Username: estebanlav@gmailc.om
Password: Test1234
```

También puedes usar variables de ambiente:

```powershell
$env:DEMOBLAZE_USERNAME="usuario"
$env:DEMOBLAZE_PASSWORD="clave"
```

## Escenarios automatizados

Este proyecto contiene 2 escenarios BDD:

1. Agregar `Samsung galaxy s6` e `Iphone 6 32gb` al carrito y validar que ambos estén visibles.
2. Realizar el proceso de compra y validar mensaje exitoso.

## Crear entorno virtual en Windows

```powershell
py -m venv .venv
.\.venv\Scripts\activate
python -m pip install --upgrade pip
pip install -r requirements.txt
python -m playwright install chromium
```

## Ejecutar pruebas

```powershell
pytest -v
```

O con Gradle:

```powershell
gradle testPlaywright
```

## Reportes y evidencias

- Reporte HTML Pytest:
  - `reports/pytest-report.html`
- Resultados Allure:
  - `allure-results/`
- Screenshots en fallos:
  - `reports/screenshots/`

## Generar reporte Allure HTML

Requiere Allure CLI instalado:

```powershell
allure generate allure-results -o reports/allure-report --clean
```

O con Gradle:

```powershell
gradle allureReportLocal
```
