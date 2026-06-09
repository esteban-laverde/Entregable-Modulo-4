@echo off
setlocal
where gradle >nul 2>nul
if %ERRORLEVEL% neq 0 (
  echo.
  echo ERROR: Gradle no esta instalado o no esta en el PATH.
  echo Instala Gradle o ejecuta: winget install Gradle.Gradle
  echo.
  exit /b 1
)
gradle %*
endlocal
