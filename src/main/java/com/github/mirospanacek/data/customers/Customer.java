package com.github.mirospanacek.data.customers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.mirospanacek.data.SocialTitle;

import java.time.LocalDate;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    private String  firstName = "";
    private String lastName = "";
    private String email = "";
    private String password = "";
    private SocialTitle socialTitle;
    private LocalDate birthdate;

    public Customer(){};
    public Customer(String email, String password, SocialTitle socialTitle, LocalDate birthdate) {
        this.email = email;
        this.password = password;
        this.socialTitle = socialTitle;
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SocialTitle getSocialTitle() {
        return socialTitle;
    }

    public void setSocialTitle(SocialTitle socialTitle) {
        this.socialTitle = socialTitle;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", socialTitle=" + socialTitle +
                ", birthdate=" + birthdate +
                '}';
    }
}
