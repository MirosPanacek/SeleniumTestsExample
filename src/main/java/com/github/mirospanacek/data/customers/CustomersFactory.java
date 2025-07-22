package com.github.mirospanacek.data.customers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mirospanacek.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CustomersFactory {
    private static final Logger LOG =
            LoggerFactory.getLogger(CustomersFactory.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final DatabaseConfig config;
    private final Connection conn;
    private String path = "";
    private Customer[] customers;

    public CustomersFactory(String path) throws IOException, SQLException {
        this.path = path ;
        config = new DatabaseConfig();
        conn = DriverManager.getConnection(config.getUrl(),
                config.getUser(),
                config.getPassword());
    }

    public Customer[] getCustomers() throws IOException {
       if (customers == null) {
           customers = mapper.readValue(new File(path), Customer[].class);
       }
       return customers;
    }

    public void saveToDb() throws SQLException {
        LOG.info("Length{}", customers.length);
        for (Customer cust : customers){
            setUsers(cust);
        }
    }

    public void removeAllFromDb() throws SQLException {
        for (Customer cust : customers){
            removeUserFromDb(cust.getEmail());
        }
    }

    public int setUsers(Customer customer) throws SQLException {
LOG.info(customer.toString());
        String query = """
        INSERT INTO ps_customer (
          id_shop_group, id_shop, id_gender, id_default_group, id_lang, id_risk,
          company, siret, ape, firstname, lastname, email, passwd, last_passwd_gen,
          birthday, newsletter, ip_registration_newsletter, newsletter_date_add,
          optin, website, outstanding_allow_amount, show_public_prices,
          max_payment_days, secure_key, note, active, is_guest, deleted,
          date_add, date_upd, reset_password_token, reset_password_validity
        ) VALUES (
          1,                     -- id_shop_group
          1,                     -- id_shop
          1,                     -- id_gender
          1,                     -- id_default_group
          1,                     -- id_lang
          1,                     -- id_risk
          'Firma s.r.o.',        -- company
          '12345678901234',      -- siret
          '123456',              -- ape
          '%s',                 -- firstname
          '%s',               -- lastname
          '%s', -- email
          '%s',  -- passwd
          CURRENT_TIMESTAMP,     -- last_passwd_gen
          '1990-05-20',          -- birthday
          1,                     -- newsletter
          '192.168.1.1',         -- ip_registration_newsletter
          NOW(),                 -- newsletter_date_add
          0,                     -- optin
          NULL,                  -- website
          0.000000,              -- outstanding_allow_amount
          0,                     -- show_public_prices
          60,                    -- max_payment_days
          'd41d8cd98f00b204e9800998ecf8427e', -- secure_key
          NULL,                  -- note
          1,                     -- active
          0,                     -- is_guest
          0,                     -- deleted
          NOW(),                 -- date_add
          NOW(),                 -- date_upd
          NULL,                  -- reset_password_token
          NULL                   -- reset_password_validity
        );
        
        """.formatted(customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.encoderPassword(customer.getPassword()),
                customer.getBirthdate());

        PreparedStatement ps = conn.prepareStatement(query);
        return ps.executeUpdate();
    }

    public int removeUserFromDb(String email) throws SQLException {
        String query = """
                DELETE FROM ps_customer
                where email = '%s';
                """.formatted(email);
        PreparedStatement ps = conn.prepareStatement(query);
        return ps.executeUpdate();
    }

}
