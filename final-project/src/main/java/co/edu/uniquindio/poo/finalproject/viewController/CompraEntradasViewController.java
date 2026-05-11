package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.*;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CompraEntradasViewController extends ViewController{
    @FXML private Label lblRecintoTop, lblEventoTop, lblSubtotal, lblNoSeleccion;
    @FXML private GridPane gridMapaAsientos;
    @FXML private VBox vboxListaSeleccionados;
    @FXML private BorderPane rootPane;

    private Evento evento;

    private final List<Asiento> asientosSeleccionados = new ArrayList<>();

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();

    public void inicializacionCompra(Evento evento) {
        this.evento = evento;
        lblEventoTop.setText("Evento: " + evento.getNombre());
        lblRecintoTop.setText("Recinto: " + evento.getRecinto().getNombre());
        generarMapaAsientos();
        actualizarResumen();
    }

    private void generarMapaAsientos() {
        gridMapaAsientos.getChildren().clear();

        gridMapaAsientos.add(crearEtiqueta(evento.getRecinto().getZonas().get(0).getTipoZona().toString()),1, 0);
        gridMapaAsientos.add(crearEtiqueta(evento.getRecinto().getZonas().get(1).getTipoZona().toString()),2, 0);
        gridMapaAsientos.add(crearEtiqueta(evento.getRecinto().getZonas().get(2).getTipoZona().toString()),3, 0);

        List<Zona> zonas = evento.getRecinto().getZonas();
        int fila = 1;
        for (Zona zona : zonas) {
            Label lblZona = new Label(zona.getIdZona());
            lblZona.getStyleClass().add("label-zona-mapa");
            aplicarColorPorTipo(lblZona, zona.getTipoZona());
            gridMapaAsientos.add(lblZona, 0, fila);

            FlowPane bloque = new FlowPane();
            bloque.getStyleClass().add("bloque-asientos");
            bloque.setHgap(3);
            bloque.setVgap(3);
            bloque.setPrefWidth(240);
            bloque.setAlignment(Pos.CENTER);

            for (Asiento asiento : zona.getAsientos()) {
                bloque.getChildren().add(crearBotonAsiento(asiento, zona));
            }

            int columna = switch (zona.getTipoZona()) {
                case GENERAL      -> 1;
                case VIP          -> 2;
                case PREFERENCIAL -> 3;
            };
            gridMapaAsientos.add(bloque, columna, fila);
            fila++;
        }
    }

    private Label crearEtiqueta(String texto) {
        Label lbl = new Label(texto);
        lbl.getStyleClass().add("label-orientacion-mapa");
        return lbl;
    }

    private void aplicarColorPorTipo(Label lbl, TipoZona tipo) {
        String color = switch (tipo) {
            case VIP          -> "#fbc531";
            case PREFERENCIAL -> "#e84393";
            case GENERAL      -> "#a29bfe";
        };
        lbl.setStyle("-fx-text-fill: " + color + ";");
    }

    private Button crearBotonAsiento(Asiento asiento, Zona zona) {
        Button btn = new Button(asiento.getIdAsiento());
        btn.getStyleClass().add("asiento");

        String claseZona = switch (zona.getTipoZona()) {
            case VIP          -> "asiento-vip";
            case PREFERENCIAL -> "asiento-preferencial";
            case GENERAL      -> "asiento-general";
        };
        btn.getStyleClass().add(claseZona);

        if (asiento.getEstadoAsiento() == EstadoAsiento.BLOQUEADO
                || asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO) {
            btn.getStyleClass().add("asiento-ocupado");
            btn.setDisable(true);
        } else {
            btn.setOnAction(e -> toggleSeleccion(asiento, btn));
        }
        return btn;
    }

    private void toggleSeleccion(Asiento asiento, Button btn) {
        if (asientosSeleccionados.contains(asiento)) {
            asientosSeleccionados.remove(asiento);
            btn.getStyleClass().remove("asiento-seleccionado");
        } else {
            asientosSeleccionados.add(asiento);
            btn.getStyleClass().add("asiento-seleccionado");
        }
        actualizarResumen();
    }

    private void actualizarResumen() {
        lblNoSeleccion.setText("");
        if (asientosSeleccionados.isEmpty()) {
            lblNoSeleccion.setText("No has seleccionado asientos aún.");
        }else {
            for (Asiento asiento: asientosSeleccionados){
                lblNoSeleccion.setText(lblNoSeleccion.getText() + " " + asiento.toString());
            }
        }
        float total = 0f;
        for (Asiento asiento : asientosSeleccionados) {
            float precio = asiento.getPrecio();
            total += precio;
        }
        lblSubtotal.setText(String.format("$%.0f", total));
    }

    @FXML
    void procederAlPago(ActionEvent event) {
        if (asientosSeleccionados.isEmpty()) {
            mostrarAlerta("Selecciona al menos un asiento antes de continuar.");
            return;
        }
        Cliente cliente = (Cliente) controladorFacade.getUsuarioActual();
        if (cliente.getTipoPago() == TipoPago.NINGUNO) {
            mostrarAlerta("Agrega 1 metodo de pago valido para continuar");
            return;
        }

        /*
        FXMLLoader loader = crearVista("/co/edu/uniquindio/poo/finalproject/ProcesarPagoView.fxml", "Pagar", event);
        ProcesarPagoViewController procesarPagoViewController  = loader.getController();
        procesarPagoViewController.rellenarCamposEvento(eventoSeleccionado);*/
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/ClienteEventoView.fxml",
                "Eventos actuales", event);
    }
}
