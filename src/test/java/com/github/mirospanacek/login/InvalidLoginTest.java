package com.github.mirospanacek.login;

import com.github.mirospanacek.TestPreset;
import com.github.mirospanacek.data.customers.Customer;
import com.github.mirospanacek.data.customers.CustomersFactory;
import com.github.mirospanacek.pages.LoginPage;
import com.github.mirospanacek.pages.MyAccountPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class InvalidLoginTest extends TestPreset {
    private static final Logger LOG =
            LoggerFactory.getLogger(InvalidLoginTest.class);
    private final static String CUSTOMERS = "./src/test/resources/users/invalidCustomers.json";
    private final static String LOGI_PAGE_URL =  "http://localhost:8089/login?back=http%3A%2F%2Flocalhost%3A8089%2F";


    @BeforeMethod
    public void setUrl() throws IOException {
        url = LOGI_PAGE_URL;
        driver.get(url);
    }

    @BeforeMethod
    @DataProvider
    public Customer[] getCustomers() throws IOException {
        CustomersFactory customersFactory = new CustomersFactory(CUSTOMERS);
        return customersFactory.getCustomers();
    }

    @Test(dataProvider = "getCustomers")
    public void invalidLogin(Customer customer) {
        LOG.info("Customer email: {} password: {}", customer.getEmail(), customer.getPassword());
        String accountName = customer.getFirstName() + " " + customer.getLastName();
        LoginPage loginPage = new LoginPage(driver);
        LOG.info("Step 1: Fill in user name password and click the button login");
        loginPage.loginAs(customer.getEmail(), customer.getPassword());

        assertThat(loginPage.getErrorMessage()).isEqualToIgnoringCase("Authentication failed.");
    }

}
