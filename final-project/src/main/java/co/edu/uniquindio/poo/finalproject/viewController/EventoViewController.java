package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.ControladorEventos;
import co.edu.uniquindio.poo.finalproject.model.Evento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventoViewController extends ViewController{
    @FXML private TextField txtIdEvento, txtNombre, txtCiudad, txtRecinto, txtHora, txtCapacidad;
    @FXML private ComboBox<String> comboCategoria, comboEstado;
    @FXML private DatePicker dateFecha;
    @FXML private TextArea txtDescripcion, txtPoliticasCancelacion, txtPoliticasReembolso;

    @FXML private FlowPane containerCards;

    @FXML
    public void initialize() {
        mostrarEventos();
    }

    public void crearEvento(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/CrearEventoView.fxml","Crear evento",event);
    }
    public void crearRecinto(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/CrearRecintoView.fxml","Crear recinto",event);
    }

    public void actualizarEvento(ActionEvent event) {
    }

    public void publicarEvento(ActionEvent event) {
    }

    public void pausarEvento(ActionEvent event) {
    }

    public void cancelarEvento(ActionEvent event) {
    }

    public void eliminarEvento(ActionEvent event) {
    }

    public void mostrarEventos() {
        containerCards.getChildren().clear();
        for (Evento evento : ControladorEventos.getInstance().getListaEventos()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/finalproject/CartaEventoView.fxml"));
                VBox card = loader.load();

                Label lblNombre = (Label) card.lookup("#lblNombre");
                lblNombre.setText(evento.getNombre());

                card.setOnMouseClicked(e -> {
                    seleccionarEvento(evento, card);
                });

                containerCards.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void seleccionarEvento(Evento evento, VBox card) {
        containerCards.getChildren().forEach(c -> c.setStyle("-fx-border-color: #dfe6e9; -fx-border-radius: 10;"));
        card.setStyle("-fx-border-color: -fx-primary-color; -fx-border-width: 2; -fx-border-radius: 10;");
    }

}
