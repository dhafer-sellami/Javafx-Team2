package entities;

public class Facture {
    private int id;
    private double prix;  // Prix de type double
    private int idRdv;  // Référence au rendez-vous

    public Facture(int id, double prix, int idRdv) {
        this.id = id;
        this.prix = prix;
        this.idRdv = idRdv;
    }

    public Facture(double prix, int idRdv) {
        this.prix = prix;
        this.idRdv = idRdv;
    }

    public int getId() {
        return id;
    }

    public double getPrix() {
        return prix;
    }

    public int getIdRdv() {
        return idRdv;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setIdRdv(int idRdv) {
        this.idRdv = idRdv;
    }
}
