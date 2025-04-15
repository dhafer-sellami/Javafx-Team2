package controllers;

import entities.Personne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import services.PersonneService;

import java.sql.SQLException;
import java.util.List;

public class AfficherPersonne {
    @javafx.fxml.FXML
    private ListView<Personne> listView;

}
//@FXML
//void initialize(){
//    PersonneService
//            try{
//                List<Personne> personnes = ps.recuperer();
//                ObservableList<Personne> obs = FXCollections;
//                ListView.setItem(personnes);
//        }catch (SQLException e){
//                System.out.println(e.getMessage());
//
//            }
//            @FXMLpublic void supprimerPersonne(ActionEvent actionEvent) {
//
//    }

