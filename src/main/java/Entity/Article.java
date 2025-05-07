package Entity;

import java.sql.Date;
import java.time.LocalDateTime;

public class Article {
    private int id;
    private String nom;
    private String type;
    private String phonenumber;
    private String description;
    private java.sql.Date dateAjoute;

    public Article(int id, String nom, String type, String phonenumber, String description, Date dateAjoute) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.phonenumber = phonenumber;
        this.description = description;
        this.dateAjoute = dateAjoute;
    }

    // Empty constructor
    public Article() {}

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateAjoute() {
        return dateAjoute;
    }

    public void setDateAjoute(Date dateAjoute) {
        this.dateAjoute = dateAjoute;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", description='" + description + '\'' +
                ", dateAjoute=" + dateAjoute +
                '}';
    }
}
