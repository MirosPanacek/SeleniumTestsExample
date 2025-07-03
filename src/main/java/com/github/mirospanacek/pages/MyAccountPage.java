package com.github.mirospanacek.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MyAccountPage {
    private WebDriver driver;
    By myAccount = By.cssSelector(".account");
    WebDriverWait wait;

    public MyAccountPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(3000L));
    }

    public WebElement getMyAccoutIcon() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(myAccount));
        return driver.findElement(myAccount);
    }

}
