package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.MostrarEventos;
import co.edu.uniquindio.poo.finalproject.model.TipoEvento;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteEventoViewController extends ViewController implements MostrarEventos {
    @FXML private FlowPane containerCards;
    @FXML private TextField txtBusqueda, txtCiudad;
    @FXML private ComboBox<TipoEvento> comboCategoria;
    @FXML private DatePicker dpFecha;
    @FXML private Label lblResultados;
    @FXML private BorderPane rootPane;

    ControladorFacade controladorFacade = ControladorFacade.getInstance();

    @FXML
    public void initialize() {
        comboCategoria.getItems().setAll(TipoEvento.values());
        cargarEventos(controladorFacade.getEventosPublicados());
    }

    @FXML
    void aplicarFiltros() {
        String nombre = txtBusqueda.getText();
        String ciudad = txtCiudad.getText();
        TipoEvento tipo = comboCategoria.getValue();
        String fecha = dpFecha.getValue() == null ? "" : dpFecha.getValue().toString();

        if (nombre.isEmpty() && ciudad.isEmpty() && tipo == null && fecha.isEmpty()) {
            mostrarAlerta("Rellene al menos un campo para filtrar eventos");
            return;
        }

        ObservableList<Evento> todos = controladorFacade.getEventosPublicados();
        List<Evento> filtrados = todos.stream()
                .filter(e -> nombre.isEmpty() || e.getNombre().equalsIgnoreCase(nombre))
                .filter(e -> ciudad.isEmpty() || e.getCiudad().equalsIgnoreCase(ciudad))
                .filter(e -> tipo == null     || e.getTipoEvento() == tipo)
                .filter(e -> fecha.isEmpty()  || e.getFecha().equals(fecha))
                .collect(Collectors.toList());

        cargarEventos(filtrados);
        lblResultados.setText("RESULTADOS ENCONTRADOS: " + filtrados.size());
    }

    @Override
    public void cargarEventos(List<Evento> eventos) {
        containerCards.getChildren().clear();
        for (Evento evento : eventos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/co/edu/uniquindio/poo/finalproject/CartaEventoView.fxml"));
                VBox card = loader.load();

                CartaEventoViewController cardController = loader.getController();
                cardController.actualizarInformacion(evento);
                card.setMinWidth(280);
                card.setOnMouseClicked(e -> seleccionarEvento(evento));
                containerCards.getChildren().add(card);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void seleccionarEvento(Evento evento) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);");
        StackPane rootStack = (StackPane) rootPane.getScene().getRoot();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/co/edu/uniquindio/poo/finalproject/DetalleEventoView.fxml"));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initOwner(rootPane.getScene().getWindow());

            DetalleEventoViewController controller = loader.getController();
            controller.initData(evento, popupStage);

            Scene scene = new Scene(root);
            scene.setFill(null);
            scene.getStylesheets().add(getClass()
                    .getResource("/co/edu/uniquindio/poo/finalproject/css/style.css")
                    .toExternalForm());
            popupStage.setScene(scene);
            rootStack.getChildren().add(overlay);
            popupStage.showAndWait();
            rootStack.getChildren().remove(overlay);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void limpiarFiltros(ActionEvent event) {
        txtBusqueda.clear();
        txtCiudad.clear();
        comboCategoria.setValue(null);
        dpFecha.setValue(null);
        cargarEventos(controladorFacade.getEventosPublicados());
        lblResultados.setText("");
    }

    public void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml",
                "Menu Principal", event);
    }

    public void verMisCompras(ActionEvent event) {
    }
}
