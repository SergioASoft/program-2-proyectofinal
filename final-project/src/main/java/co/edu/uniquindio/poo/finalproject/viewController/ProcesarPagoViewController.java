package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import co.edu.uniquindio.poo.finalproject.model.state.EstadoCompra;
import co.edu.uniquindio.poo.finalproject.model.state.EstadoPagada;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.UUID;

public class ProcesarPagoViewController extends ViewController {

    @FXML private VBox vboxDetalleAsientos;
    @FXML private Label lblTotalPago;
    @FXML private TextField txtNumeroTarjeta;

    private ContextoCompra compraActual;
    private ControladorFacade controladorFacade = ControladorFacade.getInstance();

    private void cargarResumen(Evento evento, List<Asiento> asientos) {
        String id = "CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        compraActual = new ContextoCompra(evento,asientos);
    }
}
