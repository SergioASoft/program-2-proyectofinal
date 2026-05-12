package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionAsientosViewController extends ViewController {
    @FXML private ComboBox<Evento> comboEvento;
    @FXML private ComboBox<Zona> comboZona;
    @FXML private ComboBox<EstadoAsiento> comboEstado;
    @FXML private TableView<Asiento> tablaAsientos;
    @FXML private TableColumn<Asiento, String> colId, colEstado;
    @FXML private TableColumn<Asiento, Integer> colFila, colNumero, colPrecio;
    @FXML private TextField txtId, txtFila, txtNumero, txtPrecio;
    @FXML private Label lblOcupacion;

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();
    private final ObservableList<Asiento> asientos = FXCollections.observableArrayList();
    private Asiento asientoSeleccionado;

    @FXML
    public void initialize() {
        comboEvento.getItems().setAll(controladorFacade.getTodosLosEventos());
        comboEstado.getItems().setAll(EstadoAsiento.values());
        configurarTabla();
        tablaAsientos.setItems(asientos);
        comboEvento.valueProperty().addListener((obs, oldValue, newValue) -> cargarZonas(newValue));
        comboZona.valueProperty().addListener((obs, oldValue, newValue) -> cargarAsientos(newValue));
        tablaAsientos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> cargarAsientoSeleccionado(actual));
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdAsiento()));
        colFila.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFila()));
        colNumero.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getNumero()));
        colPrecio.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPrecio()));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstadoAsiento().toString()));
    }

    private void cargarZonas(Evento evento) {
        comboZona.getItems().clear();
        asientos.clear();
        limpiarFormulario();
        if (evento != null && evento.getRecinto() != null) {
            comboZona.getItems().setAll(evento.getRecinto().getZonas());
        }
        actualizarOcupacion();
    }

    private void cargarAsientos(Zona zona) {
        limpiarFormulario();
        asientos.clear();
        if (zona != null) {
            asientos.setAll(zona.getAsientos());
        }
        actualizarOcupacion();
    }

    @FXML
    void guardarAsiento(ActionEvent event) {
        Zona zona = comboZona.getValue();
        if (zona == null || camposInvalidos()) {
            mostrarAlerta("Selecciona una zona y completa los datos del asiento.");
            return;
        }
        try {
            int fila = Integer.parseInt(txtFila.getText());
            int numero = Integer.parseInt(txtNumero.getText());
            int precio = Integer.parseInt(txtPrecio.getText());
            EstadoAsiento estado = comboEstado.getValue();

            if (asientoSeleccionado == null) {
                controladorFacade.agregarAsiento(zona, new Asiento(txtId.getText(), fila, numero, estado, precio));
            } else {
                controladorFacade.actualizarAsiento(asientoSeleccionado, txtId.getText(), fila, numero, precio, estado);
            }
            cargarAsientos(zona);
        } catch (NumberFormatException ex) {
            mostrarAlerta("Fila, numero y precio deben ser numericos.");
        }
    }

    @FXML
    void bloquearAsiento(ActionEvent event) {
        cambiarEstadoSeleccionado(EstadoAsiento.BLOQUEADO);
    }

    @FXML
    void liberarAsiento(ActionEvent event) {
        cambiarEstadoSeleccionado(EstadoAsiento.DISPONIBLE);
    }

    @FXML
    void reservarAsiento(ActionEvent event) {
        cambiarEstadoSeleccionado(EstadoAsiento.RESERVADO);
    }

    @FXML
    void eliminarAsiento(ActionEvent event) {
        Zona zona = comboZona.getValue();
        if (zona == null || asientoSeleccionado == null) {
            mostrarAlerta("Selecciona un asiento.");
            return;
        }
        controladorFacade.eliminarAsiento(zona, asientoSeleccionado);
        cargarAsientos(zona);
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        limpiarFormulario();
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml", "Menu Principal", event);
    }

    private void cambiarEstadoSeleccionado(EstadoAsiento estado) {
        if (asientoSeleccionado == null) {
            mostrarAlerta("Selecciona un asiento.");
            return;
        }
        controladorFacade.cambiarEstadoAsiento(asientoSeleccionado, estado);
        tablaAsientos.refresh();
        actualizarOcupacion();
    }

    private void cargarAsientoSeleccionado(Asiento asiento) {
        asientoSeleccionado = asiento;
        if (asiento == null) {
            return;
        }
        txtId.setText(asiento.getIdAsiento());
        txtFila.setText(String.valueOf(asiento.getFila()));
        txtNumero.setText(String.valueOf(asiento.getNumero()));
        txtPrecio.setText(String.valueOf(asiento.getPrecio()));
        comboEstado.setValue(asiento.getEstadoAsiento());
    }

    private boolean camposInvalidos() {
        return txtId.getText().isBlank()
                || txtFila.getText().isBlank()
                || txtNumero.getText().isBlank()
                || txtPrecio.getText().isBlank()
                || comboEstado.getValue() == null;
    }

    private void limpiarFormulario() {
        asientoSeleccionado = null;
        txtId.clear();
        txtFila.clear();
        txtNumero.clear();
        txtPrecio.clear();
        comboEstado.setValue(EstadoAsiento.DISPONIBLE);
        tablaAsientos.getSelectionModel().clearSelection();
    }

    private void actualizarOcupacion() {
        Zona zona = comboZona.getValue();
        if (zona == null) {
            lblOcupacion.setText("Sin zona seleccionada");
            return;
        }
        lblOcupacion.setText(String.format("Ocupacion: %d/%d (%.1f%%)",
                zona.getAsientosOcupados(), zona.getCapacidad(), zona.getPorcentajeOcupacion()));
    }
}
