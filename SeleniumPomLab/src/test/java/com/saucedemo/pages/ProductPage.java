package com.saucedemo.pages;

import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object para login, navegación y selección de productos en Demoblaze.
 */
public class ProductPage {

    private static final String BASE_URL = "https://www.demoblaze.com/index.html";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productStoreLink = By.id("nava");
    private final By loginMenu = By.id("login2");
    private final By usernameInput = By.id("loginusername");
    private final By passwordInput = By.id("loginpassword");
    private final By loginButton = By.xpath("//button[normalize-space()='Log in']");
    private final By cartLink = By.id("cartur");
    private final By addToCartButton = By.xpath("//a[normalize-space()='Add to cart']");

    private final Map<String, String> productCategory = Map.of(
            "Samsung galaxy s6", "Phones",
            "Iphone 6 32gb", "Phones",
            "Sony vaio i5", "Laptops"
    );

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public void openStore() {
        driver.get(BASE_URL);
        waitForPageReady();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbodyid")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.hrefch")));
    }

    public void login(String username, String password) {
        safeClick(loginMenu);

        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput)).clear();
        driver.findElement(usernameInput).sendKeys(username);

        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput)).clear();
        driver.findElement(passwordInput).sendKeys(password);

        safeClick(loginButton);

        try {
            wait.withTimeout(Duration.ofSeconds(4))
                    .until(ExpectedConditions.alertIsPresent());

            Alert alert = driver.switchTo().alert();
            String message = alert.getText();
            alert.accept();

            throw new IllegalStateException("No fue posible iniciar sesión en Demoblaze. Mensaje: " + message);
        } catch (TimeoutException ignored) {
            // Si no aparece alerta, se asume login correcto.
        } finally {
            wait.withTimeout(Duration.ofSeconds(25));
        }

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")),
                ExpectedConditions.invisibilityOfElementLocated(By.id("logInModal"))
        ));
    }

    public void addProductToCart(String productName) {
        goToProductStore();
        selectCategoryForProduct(productName);
        openProduct(productName);

        safeClick(addToCartButton);

        wait.until(ExpectedConditions.alertIsPresent()).accept();
        waitForPageReady();
    }

    public void goToProductStore() {
        safeClick(productStoreLink);
        waitForPageReady();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbodyid")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.hrefch")));
    }

    public void goToCart() {
        safeClick(cartLink);
        waitForPageReady();
    }

    private void selectCategoryForProduct(String productName) {
        String category = productCategory.get(productName);

        if (category != null) {
            By categoryLink = By.xpath("//a[normalize-space()='" + category + "']");
            safeClick(categoryLink);

            By productLink = By.xpath("//a[contains(@class,'hrefch') and normalize-space()='" + productName + "']");
            wait.until(ExpectedConditions.presenceOfElementLocated(productLink));
            wait.until(ExpectedConditions.visibilityOfElementLocated(productLink));
        }
    }

    private void openProduct(String productName) {
        By productLink = By.xpath("//a[contains(@class,'hrefch') and normalize-space()='" + productName + "']");
        By productTitle = By.xpath("//h2[contains(@class,'name') and normalize-space()='" + productName + "']");

        boolean clicked = false;
        int attempts = 0;

        while (!clicked && attempts < 5) {
            attempts++;

            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(productLink));
                WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(productLink));

                scrollToElement(element);

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

                clicked = true;
            } catch (StaleElementReferenceException | TimeoutException e) {
                waitSmall();
            }
        }

        if (!clicked) {
            throw new IllegalStateException("No fue posible abrir el producto: " + productName);
        }

        waitForPageReady();
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle));
    }

    private void safeClick(By locator) {
        boolean clicked = false;
        int attempts = 0;

        while (!clicked && attempts < 5) {
            attempts++;

            try {
                WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                wait.until(ExpectedConditions.visibilityOf(element));

                scrollToElement(element);

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

                clicked = true;
            } catch (StaleElementReferenceException | TimeoutException e) {
                waitSmall();
            }
        }

        if (!clicked) {
            throw new IllegalStateException("No fue posible hacer clic en el elemento: " + locator);
        }
    }

    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element
        );
    }

    private void waitForPageReady() {
        wait.until(driver -> "complete".equals(
                ((JavascriptExecutor) driver).executeScript("return document.readyState")
        ));
    }

    private void waitSmall() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}