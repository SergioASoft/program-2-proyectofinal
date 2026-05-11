package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalleEventoViewController extends ViewController{
    @FXML
    private Label lblNombre, lblCiudad, lblLugar, lblFecha, lblDescripcion;
    private Stage stage;
    private Evento evento;

    public void initData(Evento evento, Stage stage) {
        this.evento = evento;
        this.stage = stage;
        lblNombre.setText(evento.getNombre());
        lblCiudad.setText(evento.getCiudad());
        lblLugar.setText(evento.getRecinto().getNombre());
        lblFecha.setText(evento.getFecha().toString());
        lblDescripcion.setText(evento.getDescripcion());
    }

    @FXML void regresar() {
        stage.close();
    }

    @FXML void comprar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/finalproject/CompraEntradasView.fxml"));
            Parent root = loader.load();

            CompraEntradasViewController controller = loader.getController();
            controller.inicializacionCompra(evento);

            Stage popupStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Stage primaryStage = (Stage) popupStage.getOwner();

            Scene scene = new Scene(root);
            var css = getClass().getResource("/co/edu/uniquindio/poo/finalproject/css/style.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            primaryStage.setScene(scene);
            popupStage.close();

        } catch (IOException e) {
            System.err.println("Error al navegar a la compra: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
