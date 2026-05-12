package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.TipoIncidencia;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.command.CancelarCompraCommand;
import co.edu.uniquindio.poo.finalproject.model.command.CompraCommand;
import co.edu.uniquindio.poo.finalproject.model.command.ConfirmarCompraCommand;
import co.edu.uniquindio.poo.finalproject.model.command.ReembolsarCompraCommand;
import co.edu.uniquindio.poo.finalproject.model.command.ReportarIncidenciaCompraCommand;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class GestionComprasAdminViewController extends ViewController {
    @FXML private TableView<ContextoCompra> tablaCompras;
    @FXML private TableColumn<ContextoCompra, String> colId, colCliente, colEvento, colEstado, colTotal, colEntradas;
    @FXML private TextField txtBusqueda;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> comboEstado;
    @FXML private ComboBox<Zona> comboZona;
    @FXML private ListView<Asiento> listaAsientos;
    @FXML private Label lblDetalle;

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();
    private final ObservableList<ContextoCompra> compras = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboEstado.getItems().setAll("CREADA", "PAGADA", "CONFIRMADA", "CANCELADA", "REEMBOLSADA", "INCIDENCIA");
        listaAsientos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        configurarTabla();
        tablaCompras.setItems(compras);
        tablaCompras.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> cargarDetalle(actual));
        comboZona.valueProperty().addListener((obs, anterior, actual) -> cargarAsientosDisponibles());
        cargarCompras();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCompra()));
        colCliente.setCellValueFactory(cell -> new SimpleStringProperty(obtenerNombreCliente(cell.getValue())));
        colEvento.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEvento().getNombre()));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().obtenerNombreEstado()));
        colTotal.setCellValueFactory(cell -> new SimpleStringProperty(formatearMoneda(cell.getValue().calcularTotal())));
        colEntradas.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().getEntradas().size())));
    }

    @FXML
    void aplicarFiltros(ActionEvent event) {
        compras.setAll(controladorFacade.filtrarTodasLasCompras(dpFecha.getValue(), txtBusqueda.getText(), comboEstado.getValue()));
    }

    @FXML
    void limpiarFiltros(ActionEvent event) {
        txtBusqueda.clear();
        dpFecha.setValue(null);
        comboEstado.getSelectionModel().clearSelection();
        cargarCompras();
    }

    @FXML
    void confirmarCompra(ActionEvent event) {
        ejecutarComando(new ConfirmarCompraCommand(controladorFacade, obtenerSeleccionada()));
    }

    @FXML
    void cancelarCompra(ActionEvent event) {
        ejecutarComando(new CancelarCompraCommand(controladorFacade, obtenerSeleccionada()));
    }

    @FXML
    void reembolsarCompra(ActionEvent event) {
        ejecutarComando(new ReembolsarCompraCommand(controladorFacade, obtenerSeleccionada()));
    }

    @FXML
    void registrarIncidencia(ActionEvent event) {
        ejecutarComando(new ReportarIncidenciaCompraCommand(
                controladorFacade,
                obtenerSeleccionada(),
                TipoIncidencia.OPERATIVA,
                "Incidencia administrativa registrada desde gestion de compras."
        ));
    }

    @FXML
    void reasignarAsientos(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        List<Asiento> seleccionados = new ArrayList<>(listaAsientos.getSelectionModel().getSelectedItems());
        if (seleccionados.isEmpty()) {
            mostrarAlerta("Selecciona al menos un asiento para reasignar.");
            return;
        }
        try {
            controladorFacade.reasignarAsientosCompra(compra, seleccionados);
            cargarDetalle(compra);
            tablaCompras.refresh();
            mostrarAlerta("Asientos reasignados correctamente.");
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml", "Menu Principal", event);
    }

    private void cargarCompras() {
        compras.setAll(controladorFacade.getTodasLasCompras());
        if (!compras.isEmpty()) {
            tablaCompras.getSelectionModel().selectFirst();
        }
    }

    private void ejecutarComando(CompraCommand command) {
        try {
            command.ejecutar();
            tablaCompras.refresh();
            cargarDetalle(tablaCompras.getSelectionModel().getSelectedItem());
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    private ContextoCompra obtenerSeleccionada() {
        ContextoCompra compra = tablaCompras.getSelectionModel().getSelectedItem();
        if (compra == null) {
            mostrarAlerta("Selecciona una compra.");
        }
        return compra;
    }

    private void cargarDetalle(ContextoCompra compra) {
        comboZona.getItems().clear();
        listaAsientos.getItems().clear();
        if (compra == null) {
            lblDetalle.setText("Sin compra seleccionada");
            return;
        }
        lblDetalle.setText("Asientos: " + compra.getResumenAsientos()
                + "\nServicios: " + compra.getResumenServicios()
                + "\nComprobante: " + (compra.getComprobante() == null ? "Pendiente" : compra.getComprobante()));
        if (compra.getEvento().getRecinto() != null) {
            comboZona.getItems().setAll(compra.getEvento().getRecinto().getZonas());
        }
    }

    private void cargarAsientosDisponibles() {
        ContextoCompra compra = tablaCompras.getSelectionModel().getSelectedItem();
        Zona zona = comboZona.getValue();
        listaAsientos.getItems().clear();
        if (compra == null || zona == null) {
            return;
        }
        List<Asiento> disponibles = zona.getAsientos().stream()
                .filter(asiento -> asiento.getEstadoAsiento() == EstadoAsiento.DISPONIBLE || compra.getAsientos().contains(asiento))
                .toList();
        listaAsientos.getItems().setAll(disponibles);
        for (Asiento asiento : compra.getAsientos()) {
            if (disponibles.contains(asiento)) {
                listaAsientos.getSelectionModel().select(asiento);
            }
        }
    }

    private String obtenerNombreCliente(ContextoCompra compra) {
        Usuario usuario = compra.getUsuarioAsociado();
        return usuario == null ? "Sin usuario" : usuario.getNombre();
    }

    private String formatearMoneda(int valor) {
        return String.format("$%,d", valor).replace(",", ".");
    }
}
