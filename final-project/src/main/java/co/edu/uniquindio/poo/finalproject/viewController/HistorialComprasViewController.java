package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import co.edu.uniquindio.poo.finalproject.model.strategy.ReporteCsvStrategy;
import co.edu.uniquindio.poo.finalproject.model.strategy.ReportePdfStrategy;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HistorialComprasViewController extends ViewController {
    @FXML private TableView<ContextoCompra> tablaCompras;
    @FXML private TableColumn<ContextoCompra, String> colId, colFecha, colEvento, colEstado, colTotal, colComprobante;
    @FXML private TextField txtEvento;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> comboEstado;
    @FXML private Label lblAsientos, lblServicios, lblDetalleTotal, lblDetalleComprobante, lblDetalleEstado;

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();
    private final ObservableList<ContextoCompra> comprasMostradas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        comboEstado.getItems().setAll("CREADA", "PAGADA", "CONFIRMADA", "CANCELADA", "REEMBOLSADA", "INCIDENCIA");
        configurarTabla();
        tablaCompras.setItems(comprasMostradas);
        tablaCompras.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> mostrarDetalle(actual));
        cargarCompras();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIdCompra()));
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFechaCreacion().toString()));
        colEvento.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEvento().getNombre()));
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().obtenerNombreEstado()));
        colTotal.setCellValueFactory(cell -> new SimpleStringProperty(formatearMoneda(cell.getValue().calcularTotal())));
        colComprobante.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getComprobante() == null ? "Pendiente" : cell.getValue().getComprobante()));
    }

    private void cargarCompras() {
        comprasMostradas.setAll(controladorFacade.getHistorialComprasCliente());
        if (!comprasMostradas.isEmpty()) {
            tablaCompras.getSelectionModel().selectFirst();
        } else {
            mostrarDetalle(null);
        }
    }

    @FXML
    void aplicarFiltros(ActionEvent event) {
        comprasMostradas.setAll(controladorFacade.filtrarComprasCliente(
                dpFecha.getValue(),
                txtEvento.getText(),
                comboEstado.getValue()
        ));
        if (!comprasMostradas.isEmpty()) {
            tablaCompras.getSelectionModel().selectFirst();
        } else {
            mostrarDetalle(null);
        }
    }

    @FXML
    void limpiarFiltros(ActionEvent event) {
        txtEvento.clear();
        dpFecha.setValue(null);
        comboEstado.getSelectionModel().clearSelection();
        cargarCompras();
    }

    @FXML
    void cancelarSeleccionada(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        try {
            controladorFacade.cancelarCompra(compra);
            refrescarTabla();
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void modificarSeleccionada(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        if (!compra.permiteModificacion()) {
            mostrarAlerta("Solo puedes modificar compras creadas antes del pago.");
            return;
        }
        FXMLLoader loader = crearVista("/co/edu/uniquindio/poo/finalproject/CompraEntradasView.fxml", "Modificar compra", event);
        if (loader != null) {
            CompraEntradasViewController controller = loader.getController();
            controller.inicializacionCompra(compra);
        }
    }

    @FXML
    void confirmarSeleccionada(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        try {
            controladorFacade.confirmarCompra(compra);
            refrescarTabla();
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void reembolsarSeleccionada(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        try {
            controladorFacade.reembolsarCompra(compra);
            refrescarTabla();
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void reportarIncidencia(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        try {
            controladorFacade.reportarIncidencia(compra);
            refrescarTabla();
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void consultarComprobante(ActionEvent event) {
        ContextoCompra compra = obtenerSeleccionada();
        if (compra == null) {
            return;
        }
        if (compra.getComprobante() == null) {
            mostrarAlerta("La compra aun no tiene comprobante porque no ha sido pagada.");
            return;
        }
        mostrarAlerta("Comprobante: " + compra.getComprobante()
                + "\nMetodo: " + compra.getMetodoPago()
                + "\nFecha de pago: " + compra.getFechaPago()
                + "\nTotal: " + formatearMoneda(compra.calcularTotal()));
    }

    @FXML
    void exportarCsv(ActionEvent event) {
        exportar("reporte-compras.csv", "CSV", "*.csv", false);
    }

    @FXML
    void exportarPdf(ActionEvent event) {
        exportar("reporte-compras.pdf", "PDF", "*.pdf", true);
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml", "Menu Principal", event);
    }

    private void exportar(String nombre, String descripcion, String extension, boolean pdf) {
        if (comprasMostradas.isEmpty()) {
            mostrarAlerta("No hay compras para exportar.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar reporte");
        chooser.setInitialFileName(nombre);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(descripcion, extension));
        File destino = chooser.showSaveDialog(tablaCompras.getScene().getWindow());
        if (destino == null) {
            return;
        }
        try {
            List<ContextoCompra> compras = List.copyOf(comprasMostradas);
            if (pdf) {
                controladorFacade.exportarCompras(compras, destino, new ReportePdfStrategy());
            } else {
                controladorFacade.exportarCompras(compras, destino, new ReporteCsvStrategy());
            }
            mostrarAlerta("Reporte exportado en: " + destino.getAbsolutePath());
        } catch (IOException ex) {
            mostrarAlerta("No se pudo exportar el reporte: " + ex.getMessage());
        }
    }

    private ContextoCompra obtenerSeleccionada() {
        ContextoCompra compra = tablaCompras.getSelectionModel().getSelectedItem();
        if (compra == null) {
            mostrarAlerta("Selecciona una compra.");
        }
        return compra;
    }

    private void refrescarTabla() {
        tablaCompras.refresh();
        mostrarDetalle(tablaCompras.getSelectionModel().getSelectedItem());
    }

    private void mostrarDetalle(ContextoCompra compra) {
        if (compra == null) {
            lblAsientos.setText("Sin seleccion");
            lblServicios.setText("Sin seleccion");
            lblDetalleTotal.setText("$0");
            lblDetalleComprobante.setText("Pendiente");
            lblDetalleEstado.setText("-");
            return;
        }
        lblAsientos.setText(compra.getResumenAsientos());
        lblServicios.setText(compra.getResumenServicios());
        lblDetalleTotal.setText(formatearMoneda(compra.calcularTotal()));
        lblDetalleComprobante.setText(compra.getComprobante() == null ? "Pendiente" : compra.getComprobante());
        lblDetalleEstado.setText(compra.obtenerNombreEstado());
    }

    private String formatearMoneda(int valor) {
        return String.format("$%,d", valor).replace(",", ".");
    }
}
