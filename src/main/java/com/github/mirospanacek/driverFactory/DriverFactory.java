package com.github.mirospanacek.driverFactory;



import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Class create and set Webdrivermanager.
 */

public class DriverFactory {
    private String dockerScreenResolution = "2560x1440x24";

    /**
     * Method sets Webdrivermanagers capabilities.
     * @param browser type of browser
     * @param inspection if true docker container makes .mp4 record.
     * It is available only for docker container
     * @return set Webdrivermanager
     */
    public WebDriverManager create(String browser, boolean inspection) {
        WebDriverManager wdm;

        Browsers browserType = Browsers.valueOf(browser.toUpperCase());

        switch (browserType) {
            case CHROME:

                ChromeOptions option = new ChromeOptions();
                option.addArguments("start-maximized");
                option.addArguments("--headless=new");
                option.addArguments("--window-size=1920,1080");

                wdm = WebDriverManager
                        .chromedriver()
                        .capabilities(option);
                break;

            case FIREFOX:

                FirefoxProfile profile = new FirefoxProfile();
                profile.setPreference("intl.accept_languages", "cs-CZ");
                FirefoxOptions options = new FirefoxOptions();
                options.setProfile(profile);

                wdm = WebDriverManager
                        .firefoxdriver()
                        .capabilities(options);

                break;

            case EDGE:

                wdm = WebDriverManager
                        .edgedriver();
                break;

            case LOCAL:
                WebDriverManager.chromedriver().clearDriverCache().setup();
                ChromeOptions optionLocal = new ChromeOptions();
                optionLocal.addArguments("start-maximized");
                optionLocal.addArguments("--remote-allow-origins=*");
                optionLocal.addArguments(
                        "--disable-search-engine-choice-screen");
                wdm = WebDriverManager
                        .chromedriver()
                        .capabilities(optionLocal);

                break;

            default:
                throw new IllegalArgumentException("Unexpected value: "
                        + browserType);
        }

        if ((browserType.toString() != "LOCAL") && inspection) {
            wdm.enableRecording()
                    .dockerRecordingOutput("mov")
                    .dockerRecordingFrameRate(50);
        }

        return wdm;
    }
}
