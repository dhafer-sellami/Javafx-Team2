package com.example.meditrack;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Assurance {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nom;
    private SimpleStringProperty libelle;

    public Assurance() {
        this.id = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.libelle = new SimpleStringProperty();
    }

    public Assurance(int id, String nom, String libelle) {
        this.id = new SimpleIntegerProperty(id);
        this.nom = new SimpleStringProperty(nom);
        this.libelle = new SimpleStringProperty(libelle);
    }

    // Getters and Setters
    public int getId() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public String getNom() { return nom.get(); }
    public SimpleStringProperty nomProperty() { return nom; }
    public void setNom(String nom) { this.nom.set(nom); }

    public String getLibelle() { return libelle.get(); }
    public SimpleStringProperty libelleProperty() { return libelle; }
    public void setLibelle(String libelle) { this.libelle.set(libelle); }
}

