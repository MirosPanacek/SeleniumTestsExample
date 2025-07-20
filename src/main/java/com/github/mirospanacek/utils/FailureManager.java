package com.github.mirospanacek.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FailureManager {
    private static final Logger LOG =
            LoggerFactory.getLogger(FailureManager.class);
    private final TakesScreenshot takeScreen;

    public FailureManager(WebDriver driver) {
        this.takeScreen = (TakesScreenshot) driver;
    }

    /**
     * Takes  screenshot to file
     *
     * @param filename is part of file name example: timestemp-filename.png
     * @param dir      name of directory where to save file created if not exist
     */
    public void takePngScreenshot(String filename, String dir) {
        File screenshot = takeScreen.getScreenshotAs(OutputType.FILE);
        Path destination =
                Paths.get(dir +System.nanoTime() + "-" + filename + ".png");

        try {
            Path path = Paths.get(dir);
            Files.createDirectories(path);
            Files.move(screenshot.toPath(), destination);
            LOG.info("Screenshot is in: {}", destination);
        } catch (IOException e) {
            LOG.error("Exception moving screenshot from {} to {} error: {}",
                    screenshot, destination, e.getMessage());
        }
    }
}
