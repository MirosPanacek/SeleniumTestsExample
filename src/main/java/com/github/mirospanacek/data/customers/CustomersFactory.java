package com.github.mirospanacek.data.customers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CustomersFactory {
    private final ObjectMapper mapper = new ObjectMapper();
    String path = "";

    public CustomersFactory(String path){
        this.path = path ;
    }

    public Customer[] getCustomers() throws IOException {
       return mapper.readValue(new File(path), Customer[].class);
    }
}
