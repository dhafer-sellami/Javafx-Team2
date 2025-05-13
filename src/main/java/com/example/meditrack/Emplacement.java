package com.example.meditrack;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Emplacement {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty address;
    private final SimpleStringProperty city;
    private final SimpleStringProperty postalCode;

    public Emplacement(int id, String address, String city, String postalCode) {
        this.id = new SimpleIntegerProperty(id);
        this.address = new SimpleStringProperty(address);
        this.city = new SimpleStringProperty(city);
        this.postalCode = new SimpleStringProperty(postalCode);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    // Setters
    public void setAddress(String address) {
        this.address.set(address);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    // Property getters for JavaFX binding
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public SimpleStringProperty postalCodeProperty() {
        return postalCode;
    }
}