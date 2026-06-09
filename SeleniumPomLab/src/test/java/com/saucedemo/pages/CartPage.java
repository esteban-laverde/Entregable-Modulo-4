package com.saucedemo.pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object para validar y limpiar el carrito en Demoblaze.
 */
public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartRows = By.cssSelector("#tbodyid tr");
    private final By productNames = By.cssSelector("#tbodyid tr td:nth-child(2)");
    private final By deleteLinks = By.xpath("//a[normalize-space()='Delete']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public List<String> getProductNames() {
        return driver.findElements(productNames)
                .stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.isBlank())
                .collect(Collectors.toList());
    }

    public boolean containsProduct(String productName) {
        wait.until(driver -> getProductNames().contains(productName));
        return getProductNames().contains(productName);
    }

    public boolean containsProducts(String firstProduct, String secondProduct) {
        wait.until(driver -> {
            List<String> names = getProductNames();
            return names.contains(firstProduct) && names.contains(secondProduct);
        });

        List<String> names = getProductNames();
        return names.contains(firstProduct) && names.contains(secondProduct);
    }

    public void deleteAllProducts() {
        waitForCartToFinishLoading();

        while (!driver.findElements(deleteLinks).isEmpty()) {
            int before = driver.findElements(cartRows).size();
            driver.findElements(deleteLinks).get(0).click();

            wait.ignoring(StaleElementReferenceException.class)
                    .until(driver -> driver.findElements(cartRows).size() < before);
        }
    }

    public boolean isCartEmpty() {
        wait.until(driver -> driver.findElements(cartRows).isEmpty());
        return driver.findElements(cartRows).isEmpty();
    }

    public void waitForCartToFinishLoading() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
