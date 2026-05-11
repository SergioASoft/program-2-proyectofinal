package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Objects;

public class ViewController {
    protected FXMLLoader crearVista(String url,String titulo, ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(url));
            Scene scene = new Scene(loader.load(),1280,720);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(titulo);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/co/edu/uniquindio/poo/finalproject/css/style.css")).toExternalForm());
            return loader;
        }catch (Exception e){
            mostrarAlerta("Error: " + e.getMessage());
            return null;
        }
    }
    protected void mostrarAlerta(String msg){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
