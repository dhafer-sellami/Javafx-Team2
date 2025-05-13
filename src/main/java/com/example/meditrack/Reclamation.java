package com.example.meditrack;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reclamation {
    private SimpleIntegerProperty id;
    private SimpleStringProperty date;
    private SimpleStringProperty description;

    public Reclamation() {
        this.id = new SimpleIntegerProperty();
        this.date = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

    public Reclamation(int id, String date, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.description = new SimpleStringProperty(description);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getDate() { return date.get(); }
    public SimpleStringProperty dateProperty() { return date; }
    public void setDate(String date) { this.date.set(date); }

    public String getDescription() { return description.get(); }
    public SimpleStringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }
}

