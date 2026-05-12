package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Incidencia;
import co.edu.uniquindio.poo.finalproject.model.TipoIncidencia;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

public class GestionIncidenciasViewController extends ViewController {
    @FXML private TableView<Incidencia> tablaIncidencias;
    @FXML private TableColumn<Incidencia, String> colId, colTipo, colFecha, colEntidad, colDescripcion;
    @FXML private ComboBox<TipoIncidencia> comboTipoFiltro, comboTipoRegistro;
    @FXML private ComboBox<Evento> comboEvento;
    @FXML private DatePicker dpDesde, dpHasta;
    @FXML private TextArea txtDescripcion;

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();
    private final ObservableList<Incidencia> incidencias = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboTipoFiltro.getItems().setAll(TipoIncidencia.values());
        comboTipoRegistro.getItems().setAll(TipoIncidencia.values());
        comboEvento.getItems().setAll(controladorFacade.getTodosLosEventos());
        configurarTabla();
        tablaIncidencias.setItems(incidencias);
        cargarIncidencias();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdIncidencia()));
        colTipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTipo().toString()));
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha().toString()));
        colEntidad.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEntidadAfectada()));
        colDescripcion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescripcion()));
    }

    @FXML
    void registrarIncidencia(ActionEvent event) {
        if (comboTipoRegistro.getValue() == null || txtDescripcion.getText().isBlank()) {
            mostrarAlerta("Selecciona el tipo y escribe la descripcion.");
            return;
        }
        controladorFacade.registrarIncidenciaEvento(comboEvento.getValue(), comboTipoRegistro.getValue(), txtDescripcion.getText());
        txtDescripcion.clear();
        comboTipoRegistro.getSelectionModel().clearSelection();
        cargarIncidencias();
    }

    @FXML
    void aplicarFiltros(ActionEvent event) {
        incidencias.setAll(controladorFacade.filtrarIncidencias(dpDesde.getValue(), dpHasta.getValue(), comboTipoFiltro.getValue()));
    }

    @FXML
    void limpiarFiltros(ActionEvent event) {
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        comboTipoFiltro.getSelectionModel().clearSelection();
        cargarIncidencias();
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml", "Menu Principal", event);
    }

    private void cargarIncidencias() {
        incidencias.setAll(controladorFacade.getIncidencias());
    }
}
