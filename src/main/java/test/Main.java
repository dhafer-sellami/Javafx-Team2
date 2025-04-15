package test;


import services.PersonneService;


import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        PersonneService ps = new PersonneService();
        try {
//            ps.modifier(new Personne(1,25, "Ben Foulen","Foulen"));
            System.out.println(ps.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
