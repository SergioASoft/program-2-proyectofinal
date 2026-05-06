package co.edu.uniquindio.poo.finalproject.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CrearRecintoViewController extends ViewController{
    @FXML
    private TextField txtIdRecinto;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCiudad;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtBusquedaRecinto;

    @FXML private TextField txtNombreZona;
    @FXML private TextField txtCapacidadZona;

    @FXML private TableView<Object> tablaRecintos;
    @FXML private TableColumn<Object, String> colIdRecinto;
    @FXML private TableColumn<Object, String> colNombreRecinto;
    @FXML private TableColumn<Object, String> colCiudadRecinto;

    @FXML private TableView<Object> tablaZonas;
    @FXML private TableColumn<Object, String> colNombreZona;
    @FXML private TableColumn<Object, String> colCapacidadZona;

    @FXML
    void agregarOActualizarZona(ActionEvent event) {
    }

    @FXML
    void eliminarRecinto(ActionEvent event) {
    }

    @FXML
    void eliminarZona(ActionEvent event) {
    }

    @FXML
    void guardarRecintoCompleto(ActionEvent event) {
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        txtIdRecinto.clear();
        txtNombre.clear();
        txtCiudad.clear();
        txtDireccion.clear();
        tablaZonas.getItems().clear();
    }

    @FXML
    void prepararEdicionZona(ActionEvent event) {
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/EventoView.fxml","Gestion de eventos",event);
    }
}
