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
    private String valid; // Values: valid, not valid, under process
    private String validationResult;


    public Order(String name, String lastname, String numberOfSurferboards, String numberOfDivingSuits, String customerId) {
        this.customerId = customerId;
        this.name = name;
        this.lastname = lastname;
        this.numberOfSurferboards = numberOfSurferboards;
        this.numberOfDivingSuits = numberOfDivingSuits;
    }

    public Order(String customerId, String name, String lastname, String numberOfSurferboards, String numberOfDivingSuits, String overallItems, String orderId, String valid, String validationResult) {
        this.customerId = customerId;
        this.name = name;
        this.lastname = lastname;
        this.numberOfSurferboards = numberOfSurferboards;
        this.numberOfDivingSuits = numberOfDivingSuits;
        this.overallItems = overallItems;
        this.orderId = orderId;
        this.valid = valid;
        this.validationResult = validationResult;
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

    public String getOverallItems() {
        return overallItems;
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

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
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
        return name + " " + lastname;
    }
}
