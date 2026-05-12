package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ProcesarPagoViewController extends ViewController {

    @FXML private VBox vboxDetalleAsientos, vboxDetalleServicios;
    @FXML private Label lblTotalPago, lblIdCompra, lblEstadoCompra, lblComprobante;
    @FXML private TextField txtNumeroTarjeta;

    private ContextoCompra compraActual;
    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();

    public void cargarResumen(ContextoCompra compra) {
        this.compraActual = compra;
        lblIdCompra.setText(compra.getIdCompra());
        lblEstadoCompra.setText(compra.obtenerNombreEstado());
        lblComprobante.setText(compra.getComprobante() == null ? "Pendiente" : compra.getComprobante());
        lblTotalPago.setText(formatearMoneda(compra.calcularTotal()));
        cargarAsientos();
        cargarServicios();
        cargarMetodoPago();
    }

    private void cargarAsientos() {
        vboxDetalleAsientos.getChildren().clear();
        for (Asiento asiento : compraActual.getAsientos()) {
            Label label = new Label(asiento.getIdAsiento() + " - " + formatearMoneda(asiento.getPrecio()));
            label.getStyleClass().add("secondary-label");
            vboxDetalleAsientos.getChildren().add(label);
        }
    }

    private void cargarServicios() {
        vboxDetalleServicios.getChildren().clear();
        if (compraActual.getServiciosAdicionales().isEmpty()) {
            Label label = new Label("Sin servicios adicionales");
            label.getStyleClass().add("secondary-label");
            vboxDetalleServicios.getChildren().add(label);
            return;
        }
        for (TipoServicioAdicional servicio : compraActual.getServiciosAdicionales()) {
            Label label = new Label(servicio.getNombre() + " - " + formatearMoneda(servicio.getPrecio()));
            label.getStyleClass().add("secondary-label");
            vboxDetalleServicios.getChildren().add(label);
        }
    }

    private void cargarMetodoPago() {
        Usuario usuario = controladorFacade.getUsuarioActual();
        if (usuario instanceof Cliente cliente) {
            txtNumeroTarjeta.setText(cliente.getTipoPago().toString());
        }
    }

    @FXML
    void confirmarPago(ActionEvent event) {
        try {
            TipoPago metodoPago = TipoPago.valueOf(txtNumeroTarjeta.getText());
            controladorFacade.pagarCompra(compraActual, metodoPago);
            mostrarAlerta("Pago confirmado. Comprobante: " + compraActual.getComprobante());
            crearVista("/co/edu/uniquindio/poo/finalproject/HistorialComprasView.fxml", "Mis compras", event);
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void modificarCompra(ActionEvent event) {
        FXMLLoader loader = crearVista("/co/edu/uniquindio/poo/finalproject/CompraEntradasView.fxml", "Modificar compra", event);
        if (loader != null) {
            CompraEntradasViewController controller = loader.getController();
            controller.inicializacionCompra(compraActual);
        }
    }

    @FXML
    void cancelarCompra(ActionEvent event) {
        try {
            controladorFacade.cancelarCompra(compraActual);
            mostrarAlerta("Compra cancelada antes del pago.");
            crearVista("/co/edu/uniquindio/poo/finalproject/HistorialComprasView.fxml", "Mis compras", event);
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    private String formatearMoneda(int valor) {
        return String.format("$%,d", valor).replace(",", ".");
    }
}
