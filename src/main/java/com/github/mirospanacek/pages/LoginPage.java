package com.github.mirospanacek.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for Login Page – bez použití @FindBy
 */
public class LoginPage {

    private final WebDriver driver;
    WebDriverWait wait;
    private final By usernameInput = By.id("field-email");
    private final By passwordInput = By.id("field-password");
    private final By loginButton = By.id("submit-login");
    private final By errorMessage = By.cssSelector("li[class='alert alert-danger']");


    public LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(3000L));
    }

    public void enterUsername(String username) {
        driver.findElement(usernameInput).clear();
        driver.findElement(usernameInput).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordInput).clear();
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
       wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return driver.findElement(errorMessage).getText();
    }

    public void loginAs(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}
