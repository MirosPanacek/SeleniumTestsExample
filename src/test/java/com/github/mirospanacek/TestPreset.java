package com.github.mirospanacek;
import com.github.mirospanacek.config.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import com.github.mirospanacek.driverFactory.DriverFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;

import static io.github.bonigarcia.wdm.WebDriverManager.isDockerAvailable;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *Basic setting for all tests in Mhdent project.
 */
public  abstract class TestPreset {
    private static final Logger LOG =
            LoggerFactory.getLogger(TestPreset.class);
    protected WebDriver driver;
    protected WebDriverManager wdm;
    protected String url = "http://127.0.0.1";
    private Configuration conf;

    @BeforeSuite
    public void createDirectories() throws IOException {
        File tempDirectory = new File("./", "temp");
        File logDirectory = new File("./", "log");
        File pictureDirectory = new File("./", "failTestsPicture");

        if (!tempDirectory.exists()) {
            tempDirectory.mkdir();
        }
        if (!logDirectory.exists()) {
            logDirectory.mkdir();
        }
        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdir();
        }
    }

    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void initial(@Optional("chrome") String browser) throws Exception {
        //set inspection mode by Maven
        conf =  new Configuration();
        if (conf.getBrowser().equalsIgnoreCase("local")) {
            browser = conf.getBrowser();
        }
        url = conf.getUrl();
        LOG.info("Actual test URL: " + url);

        wdm = new DriverFactory().create(browser,
                conf.getInspection());
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(final Method method) throws Exception {
        MDC.put("test", method.getName());

        LOG.info("************* Test: " + method.getName() + " **************");
        //check Docker with retry
        for (int i = 0; i < 3; ++i) {
            if (isDockerAvailable()) {
                break;
            }
            Thread.sleep(20000);
            if( i == 2) {
                throw new RuntimeException("Docker is not available!");
            }
        }

        driver = wdm.create();
        /*If is Driver null Docker image does not download or
        does not start in 30s.*/
        assertThat(driver).as("WDM driver is null, if Driver is null "
                        + "Docker image does not download or "
                        + "does not start in 30s.")
                .isNotNull();


        ///set driver
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(360));
        driver.manage().window().maximize();
        driver.get(url);

    }

    @AfterMethod(alwaysRun = true)
    public void teardown(ITestResult result, final Method method) throws InterruptedException, ExecutionException, TimeoutException {
        //print screen failure tests to dir  ./failTestsPicture/
        if (result.getStatus() == ITestResult.FAILURE) {
           /* FailureManager failureManager = new FailureManager(driver);
            failureManager.takePngScreenshot(result.getName(),
                    "./failTestsPicture/");*/
            Reporter.log(result.getTestName());
            try {
                LOG.error("Error occur: {}/n{}/n{}",
                        result.getThrowable().getMessage(),
                        result.getThrowable().getStackTrace(),
                        result.getThrowable().toString());
            } catch (NullPointerException e) {
                LOG.info("No erro message.");
            }
        }

        if (driver != null) {
            wdm.quit();
        }

        LOG.info("********** end of test:" + method.getName() + " ***********");
        MDC.clear();
    }

    @AfterSuite(alwaysRun = true)
    public void romoveDockerContainers() throws IOException {
        /* A part docker image repository name.
         * It is form filtering running containers */
        String image = "selenoid";
        String imageEdge = "browsers/edge";
        /*docker connection configuration*/
        DockerClient dockerClient = startDockerClient();

        List<Container> containers =
                dockerClient.listContainersCmd().exec();

        containers.forEach(e -> LOG.info("Running docker containers: {}",
                e.getImage()));

        containers.stream()
                .filter(cont -> cont.getImage().contains(image)
                        || cont.getImage().contains(imageEdge))
                .forEach(selenoidCont -> {

                    LOG.info("Zombi container: {}, {}, {}",
                            selenoidCont.getImage(),
                            selenoidCont.getState(),
                            selenoidCont.getNames());

                    dockerClient.killContainerCmd(selenoidCont.getId())
                            .exec();

                    dockerClient.removeContainerCmd(selenoidCont.getId())
                            .exec();
                });

        dockerClient.close();
        LOG.info("End of suite");
    }

    /**
     * Create http client for communication with docker
     * @return
     */
    private DockerClient startDockerClient() {
        DockerClientConfig config =
                DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
        return DockerClientImpl
                .getInstance(config, httpClient);
    }
}
