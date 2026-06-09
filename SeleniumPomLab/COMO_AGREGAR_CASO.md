# Cómo agregar un nuevo caso BDD en SeleniumPomLab

## 1. Crear el escenario en Gherkin

Abre:

```text
src/test/resources/features/agregar_articulo_carrito.feature
```

Agrega un nuevo escenario usando Given / When / Then.

Ejemplo:

```gherkin
Scenario: Validar un producto específico en el carrito
  Given que ingreso a Demoblaze e inicio sesión
  When agrego el producto "Samsung galaxy s6" al carrito
  And voy al carrito
  Then debo ver el producto "Samsung galaxy s6" en el carrito
```

## 2. Crear o reutilizar métodos en Page Objects

Los Page Objects están en:

```text
src/test/java/com/saucedemo/pages/
```

- `ProductPage.java`: acciones sobre productos, login y navegación.
- `CartPage.java`: acciones sobre carrito.

## 3. Mapear el paso en Step Definitions

Los steps están en:

```text
src/test/java/com/saucedemo/steps/AddToCartSteps.java
```

Ejemplo:

```java
@When("agrego el producto {string} al carrito")
public void agregoElProductoAlCarrito(String producto) {
    productPage.addProductToCart(producto);
}
```

## 4. Ejecutar pruebas

```powershell
.\gradlew.bat clean test
```

## 5. Revisar reportes

```text
build/reports/cucumber/cucumber.html
build/reports/tests/test/index.html
build/allure-results/
```
