package entities;


import javafx.beans.property.*;

import java.time.LocalDate;

public class RendezVous {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty num = new SimpleStringProperty();
    private final StringProperty etat = new SimpleStringProperty();

    public RendezVous() {

    }

    public RendezVous(int id, LocalDate date, String email, String num, String etat) {
        this.id.set(id);
        this.date.set(date);
        this.email.set(email);
        this.num.set(num);
        this.etat.set(etat);
    }

    public RendezVous(LocalDate date, String email, String num, String etat) {
        this.date.set(date);
        this.email.set(email);
        this.num.set(num);
        this.etat.set(etat);
    }


    public int getId() { return id.get(); }
    public LocalDate getDate() { return date.get(); }
    public String getEmail() { return email.get(); }
    public String getNum() { return num.get(); }
    public String getEtat() { return etat.get(); }


    public void setId(int id) { this.id.set(id); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setEmail(String email) { this.email.set(email); }
    public void setNum(String num) { this.num.set(num); }
    public void setEtat(String etat) { this.etat.set(etat); }


    public IntegerProperty idProperty() { return id; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public StringProperty emailProperty() { return email; }
    public StringProperty numProperty() { return num; }
    public StringProperty etatProperty() { return etat; }
}