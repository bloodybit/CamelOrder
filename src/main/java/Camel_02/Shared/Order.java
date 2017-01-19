package Camel_02.Shared;

import java.io.Serializable;

/**
 * Created by riccardosibani on 15/01/2017.
 */
public class Order implements Serializable {

    private String customerId;
    private String name;
    private String lastname;
    private String numberOfSurferboards;
    private String numberOfDivingSuits;
    private String overallItems;
    private String orderId;
    private Boolean valid; // Values: valid, not valid, under process
    private String validationResult;


    public Order(String name, String lastname, String numberOfSurferboards, String numberOfDivingSuits, String customerId, String orderId) {
        this.customerId = customerId.trim();
        this.name = name.trim();
        this.lastname = lastname.trim();
        this.numberOfSurferboards = numberOfSurferboards.trim();
        this.numberOfDivingSuits = numberOfDivingSuits.trim();
        this.orderId = orderId.trim();
    }

    public Order(String name, String lastname, String numberOfSurferboards, String numberOfDivingSuits, String customerId, Boolean valid) {
        this.customerId = customerId.trim();
        this.name = name.trim();
        this.lastname = lastname.trim();
        this.numberOfSurferboards = numberOfSurferboards.trim();
        this.numberOfDivingSuits = numberOfDivingSuits.trim();
        this.valid = valid;
    }

    public Order(String customerId, String name, String lastname, String numberOfSurferboards, String numberOfDivingSuits, String overallItems, String orderId, Boolean valid, String validationResult) {
        this.customerId = customerId.trim();
        this.name = name.trim();
        this.lastname = lastname.trim();
        this.numberOfSurferboards = numberOfSurferboards.trim();
        this.numberOfDivingSuits = numberOfDivingSuits.trim();
        this.overallItems = overallItems.trim();
        this.orderId = orderId.trim();
        this.valid = valid;
        this.validationResult = validationResult.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNumberOfSurferboards() {
        return numberOfSurferboards;
    }

    public void setNumberOfSurferboards(String numberOfSurferboards) {
        this.numberOfSurferboards = numberOfSurferboards;
    }

    public String getGetNumberOfDivingSuits() {
        return numberOfDivingSuits;
    }

    public void setGetNumberOfDivingSuits(String numberOfDivingSuits) {
        this.numberOfDivingSuits = numberOfDivingSuits;
    }

    public void setOverallItems(String overallItems) {
        this.overallItems = overallItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(String validationResult) {
        this.validationResult = validationResult;
    }


    @Override
    public String toString() {
        return "{name: '"+name + "'; lastname: '"
                + lastname + "'; #B: '" + numberOfSurferboards
                + "'; #S: '" + numberOfDivingSuits + "'; cid: '"
                + customerId + "'; tot: '" + overallItems + "'; orderId: '"
                + orderId + "'; valid: '" + valid + "'; result: '" + validationResult+"'}";
    }

    public String toJSONFormat() {
        return "{\n\t name: '"+name + "';\n\t lastname: '"
                + lastname + "';\n\t #B: '" + numberOfSurferboards
                + "';\n\t #S: '" + numberOfDivingSuits + "';\n\t cid: '"
                + customerId + "';\n\t tot: '" + overallItems + "';\n\t orderId: '"
                + orderId + "';\n\t valid: '" + valid + "';\n\t result: '" + validationResult+"'\n}";
    }
}
