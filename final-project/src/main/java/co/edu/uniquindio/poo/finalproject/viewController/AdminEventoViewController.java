package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorEventos;
import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.TipoEvento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminEventoViewController extends ViewController{
    @FXML private TextField txtBusqueda;
    @FXML private ComboBox<TipoEvento> comboCategoria;
    @FXML private ComboBox<EstadoEvento> comboEstado;
    @FXML private DatePicker filterFecha;
    @FXML private FlowPane containerCards;
    private Evento eventoSeleccionado;

    ControladorFacade controladorFacade = ControladorFacade.getInstance();

    @FXML
    public void initialize() {
        comboCategoria.getItems().setAll(TipoEvento.values());
        comboEstado.getItems().setAll(EstadoEvento.values());
        mostrarEventos();
    }

    public void crearEvento(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/CrearEventoView.fxml",
                "Crear evento", event);
    }

    public void crearRecinto(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/CrearRecintoView.fxml",
                "Crear recinto", event);
    }

    public void limpiarFiltros(ActionEvent event) {
        txtBusqueda.clear();
        comboCategoria.getSelectionModel().clearSelection();
        comboEstado.getSelectionModel().clearSelection();
        comboCategoria.setButtonCell(null);
        comboEstado.setButtonCell(null);
        filterFecha.setValue(null);
        mostrarEventos();
    }

    public void publicarEvento(ActionEvent event) {
        cambiarEstado(EstadoEvento.PUBLICADO);
    }

    public void pausarEvento(ActionEvent event) {
        cambiarEstado(EstadoEvento.PAUSADO);
    }

    public void cancelarEvento(ActionEvent event) {
        cambiarEstado(EstadoEvento.CANCELADO);
    }

    public void actualizarEvento(ActionEvent event) {
        if (eventoSeleccionado != null) {
            FXMLLoader loader = crearVista("/co/edu/uniquindio/poo/finalproject/CrearEventoView.fxml", "Crear evento", event);
            CrearEventoViewController crearEventoViewController = loader.getController();
            crearEventoViewController.rellenarCamposEvento(eventoSeleccionado);
        } else {
            mostrarAlerta("Selecciona un evento primero");
        }

    }

    public void eliminarEvento(ActionEvent event) {
        if (eventoSeleccionado != null) {
            controladorFacade.eliminarEvento(eventoSeleccionado);
            eventoSeleccionado = null;
            mostrarEventos();
        } else {
            mostrarAlerta("Selecciona un evento primero");
        }
    }

    private void cambiarEstado(EstadoEvento nuevoEstado) {
        if (eventoSeleccionado != null) {
            controladorFacade.cambiarEstadoEvento(eventoSeleccionado, nuevoEstado);
            mostrarEventos();
            eventoSeleccionado = null;
        } else {
            mostrarAlerta("Selecciona un evento primero");
        }
    }

    public void mostrarEventos() {
        containerCards.getChildren().clear();
        for (Evento evento : controladorFacade.getTodosLosEventos()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/co/edu/uniquindio/poo/finalproject/CartaEventoView.fxml"));
                VBox card = loader.load();

                CartaEventoViewController cardController = loader.getController();
                cardController.actualizarInformacion(evento);
                card.setMinWidth(280);
                card.setOnMouseClicked(e -> seleccionarEvento(evento, card));
                containerCards.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void seleccionarEvento(Evento evento, VBox card) {
        containerCards.getChildren().forEach(c ->
                c.setStyle("-fx-border-color: transparent; -fx-border-width: 2; " +
                        "-fx-border-radius: 10; -fx-background-radius: 10;")
        );
        card.setStyle("-fx-border-color: -fx-primary-color; -fx-border-width: 2; " +
                "-fx-border-radius: 10; -fx-background-radius: 10;");
        this.eventoSeleccionado = evento;
    }

    public void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml",
                "Menu Principal", event);
    }
}
