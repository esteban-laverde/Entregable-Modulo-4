package com.saucedemo.steps;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.ProductPage;
import com.saucedemo.utils.DriverFactory;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step Definitions que conectan los escenarios Gherkin con las acciones Java.
 */
public class AddToCartSteps {

    private ProductPage productPage;
    private CartPage cartPage;

    @Given("que ingreso a Demoblaze e inicio sesión")
    public void queIngresoADemoblazeEInicioSesion() {
        productPage = new ProductPage(DriverFactory.getDriver());
        cartPage = new CartPage(DriverFactory.getDriver());

        String username = System.getProperty("demoblaze.username", "estebanlav@gmail.com");
        String password = System.getProperty("demoblaze.password", "Test1234");

        productPage.openStore();
        productPage.login(username, password);

        // Limpieza preventiva para evitar productos de ejecuciones anteriores.
        productPage.goToCart();
        cartPage.deleteAllProducts();
        productPage.goToProductStore();
    }

    @When("agrego el producto {string} al carrito")
    public void agregoElProductoAlCarrito(String productName) {
        productPage.addProductToCart(productName);
    }

    @When("agrego los productos {string} y {string} al carrito")
    public void agregoLosProductosAlCarrito(String firstProduct, String secondProduct) {
        productPage.addProductToCart(firstProduct);
        productPage.addProductToCart(secondProduct);
    }

    @And("que tengo los productos {string} y {string} en el carrito")
    public void queTengoLosProductosEnElCarrito(String firstProduct, String secondProduct) {
        productPage.addProductToCart(firstProduct);
        productPage.addProductToCart(secondProduct);
        productPage.goToCart();
    }

    @And("voy al carrito")
    public void voyAlCarrito() {
        productPage.goToCart();
    }

    @When("elimino los productos del carrito")
    public void eliminoLosProductosDelCarrito() {
        cartPage.deleteAllProducts();
    }

    @Then("debo ver el producto {string} en el carrito")
    public void deboVerElProductoEnElCarrito(String productName) {
        assertTrue(
                cartPage.containsProduct(productName),
                "No se encontró el producto en el carrito: " + productName
        );
    }

    @Then("debo ver los productos {string} y {string} en el carrito")
    public void deboVerLosProductosEnElCarrito(String firstProduct, String secondProduct) {
        assertTrue(
                cartPage.containsProducts(firstProduct, secondProduct),
                "No se encontraron los dos productos esperados en el carrito."
        );
    }

    @Then("debo ver el carrito vacío")
    public void deboVerElCarritoVacio() {
        assertTrue(cartPage.isCartEmpty(), "El carrito no quedó vacío.");
    }
}