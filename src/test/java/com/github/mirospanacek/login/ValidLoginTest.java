package com.github.mirospanacek.login;

import com.github.mirospanacek.TestPreset;
import com.github.mirospanacek.data.customers.Customer;
import com.github.mirospanacek.data.customers.CustomersFactory;
import com.github.mirospanacek.pages.LoginPage;
import com.github.mirospanacek.pages.MyAccountPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidLoginTest extends TestPreset {
    private static final Logger LOG =
            LoggerFactory.getLogger(ValidLoginTest.class);
    private final static String CUSTOMERS = "./src/test/resources/users/customers.json";
    private final static String LOGI_PAGE_URL =  "http://localhost:8089/login?back=http%3A%2F%2Flocalhost%3A8089%2F";
    private CustomersFactory customersFactory;


    @BeforeMethod
    public void setUrl() throws IOException {
        url = LOGI_PAGE_URL;
        driver.get(url);
    }

    @BeforeClass
    @DataProvider()
    public Customer[] getCustomers() throws IOException, SQLException {
        customersFactory = new CustomersFactory(CUSTOMERS);
        Customer[] custom = customersFactory.getCustomers();
        customersFactory.saveToDb();
        return custom;
    }

    @AfterClass
    public void cleanDb() throws SQLException {
       customersFactory.removeAllFromDb();
    }

    @Test(dataProvider = "getCustomers")
    public void positiveLogin(Customer customer) {
        LOG.info("Customer email: {} password: {}", customer.getEmail(), customer.getPassword());
        String accountName = customer.getFirstName() + " " + customer.getLastName();
        LoginPage loginPage = new LoginPage(driver);
        LOG.info("Step 1: Fill in user name password and click the button login");
        loginPage.loginAs(customer.getEmail(), customer.getPassword());
        MyAccountPage myAccountPage = new MyAccountPage(driver);
        LOG.info(myAccountPage.getMyAccoutIcon().getText());
        LOG.info(accountName);
        assertThat(myAccountPage.getMyAccoutIcon().getText()).isEqualToIgnoringCase(accountName);
    }



}