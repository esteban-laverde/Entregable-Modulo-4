package com.saucedemo.steps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.saucedemo.utils.DriverFactory;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Hooks para cerrar el navegador y guardar evidencias en fallos.
 */
public class Hooks {

    @After
    public void afterScenario(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();

        if (scenario.isFailed() && driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "screenshot-fallo");

            saveScreenshotFile(scenario, screenshot);
        }

        DriverFactory.quitDriver();
    }

    private void saveScreenshotFile(Scenario scenario, byte[] screenshot) {
        try {
            Path screenshotsDir = Path.of("build", "evidences", "screenshots");
            Files.createDirectories(screenshotsDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9-_]", "_");
            Path screenshotPath = screenshotsDir.resolve(scenarioName + "_" + timestamp + ".png");

            Files.write(screenshotPath, screenshot);
        } catch (IOException e) {
            System.err.println("No fue posible guardar el screenshot: " + e.getMessage());
        }
    }
}
