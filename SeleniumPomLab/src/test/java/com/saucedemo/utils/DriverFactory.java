package com.saucedemo.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Factory para gestionar el ciclo de vida del WebDriver.
 *
 * Permite dos modos de ejecución:
 *
 * 1. Local:
 *    - Usa ChromeDriver directamente.
 *    - Aplica cuando NO existe la variable SELENIUM_REMOTE_URL.
 *
 * 2. Jenkins / Contenedor:
 *    - Usa RemoteWebDriver contra un contenedor Selenium Chrome.
 *    - Aplica cuando existe SELENIUM_REMOTE_URL.
 */
public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(createDriver());
        }

        return DRIVER.get();
    }

    private static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        if (headless) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");

        String remoteUrl = System.getProperty("selenium.remote.url");

        if (remoteUrl == null || remoteUrl.isBlank()) {
            remoteUrl = System.getenv("SELENIUM_REMOTE_URL");
        }

        WebDriver driver;

        if (remoteUrl != null && !remoteUrl.isBlank()) {
            driver = createRemoteDriver(remoteUrl, options);
        } else {
            driver = createLocalDriver(options);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        try {
            driver.manage().window().maximize();
        } catch (Exception ignored) {
            // En algunos ambientes headless/remotos maximize puede no aplicar.
        }

        return driver;
    }

    private static WebDriver createLocalDriver(ChromeOptions options) {
        return new ChromeDriver(options);
    }

    private static WebDriver createRemoteDriver(String remoteUrl, ChromeOptions options) {
        try {
            System.out.println("Ejecutando Selenium con navegador remoto: " + remoteUrl);
            return new RemoteWebDriver(new URL(remoteUrl), options);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("URL remota de Selenium inválida: " + remoteUrl, e);
        }
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();

        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}