package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.TipoZona;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CompraEntradasViewController extends ViewController {
    @FXML private Label lblRecintoTop, lblEventoTop, lblSubtotal, lblServicios, lblTotal, lblNoSeleccion, lblTituloCompra;
    @FXML private GridPane gridMapaAsientos;
    @FXML private VBox vboxListaSeleccionados;
    @FXML private BorderPane rootPane;
    @FXML private ComboBox<TipoZona> comboFiltroZona;
    @FXML private CheckBox chkVip, chkSeguro, chkMerchandising, chkParqueadero, chkAccesoPreferencial;
    @FXML private Button btnCancelarCompra;

    private Evento evento;
    private ContextoCompra compraActual;
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();
    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();

    @FXML
    public void initialize() {
        if (comboFiltroZona != null) {
            comboFiltroZona.getItems().setAll(TipoZona.values());
            comboFiltroZona.valueProperty().addListener((obs, oldValue, newValue) -> generarMapaAsientos());
        }
        configurarServicios();
        actualizarVisibilidadCancelacion();
    }

    public void inicializacionCompra(Evento evento) {
        this.evento = evento;
        this.compraActual = null;
        this.asientosSeleccionados.clear();
        lblTituloCompra.setText("Seleccion de Entradas");
        lblEventoTop.setText("Evento: " + evento.getNombre());
        lblRecintoTop.setText("Recinto: " + evento.getRecinto().getNombre());
        actualizarVisibilidadCancelacion();
        generarMapaAsientos();
        actualizarResumen();
    }

    public void inicializacionCompra(ContextoCompra compra) {
        this.compraActual = compra;
        this.evento = compra.getEvento();
        this.asientosSeleccionados.clear();
        this.asientosSeleccionados.addAll(compra.getAsientos());
        lblTituloCompra.setText("Modificar compra " + compra.getIdCompra());
        lblEventoTop.setText("Evento: " + evento.getNombre());
        lblRecintoTop.setText("Recinto: " + evento.getRecinto().getNombre());
        seleccionarServicios(compra.getServiciosAdicionales());
        actualizarVisibilidadCancelacion();
        generarMapaAsientos();
        actualizarResumen();
    }

    private void configurarServicios() {
        configurarServicio(chkVip, TipoServicioAdicional.VIP);
        configurarServicio(chkSeguro, TipoServicioAdicional.SEGURO);
        configurarServicio(chkMerchandising, TipoServicioAdicional.MERCHANDISING);
        configurarServicio(chkParqueadero, TipoServicioAdicional.PARQUEADERO);
        configurarServicio(chkAccesoPreferencial, TipoServicioAdicional.ACCESO_PREFERENCIAL);
    }

    private void configurarServicio(CheckBox checkBox, TipoServicioAdicional servicio) {
        if (checkBox != null) {
            checkBox.setText(servicio.toString());
            checkBox.setOnAction(event -> actualizarResumen());
        }
    }

    private void seleccionarServicios(List<TipoServicioAdicional> servicios) {
        seleccionarServicio(chkVip, servicios.contains(TipoServicioAdicional.VIP));
        seleccionarServicio(chkSeguro, servicios.contains(TipoServicioAdicional.SEGURO));
        seleccionarServicio(chkMerchandising, servicios.contains(TipoServicioAdicional.MERCHANDISING));
        seleccionarServicio(chkParqueadero, servicios.contains(TipoServicioAdicional.PARQUEADERO));
        seleccionarServicio(chkAccesoPreferencial, servicios.contains(TipoServicioAdicional.ACCESO_PREFERENCIAL));
    }

    private void seleccionarServicio(CheckBox checkBox, boolean seleccionado) {
        if (checkBox != null) {
            checkBox.setSelected(seleccionado);
        }
    }

    private void generarMapaAsientos() {
        if (gridMapaAsientos == null || evento == null || evento.getRecinto() == null) {
            return;
        }
        gridMapaAsientos.getChildren().clear();

        gridMapaAsientos.add(crearEtiqueta("GENERAL"), 1, 0);
        gridMapaAsientos.add(crearEtiqueta("VIP"), 2, 0);
        gridMapaAsientos.add(crearEtiqueta("PREFERENCIAL"), 3, 0);

        TipoZona filtro = comboFiltroZona == null ? null : comboFiltroZona.getValue();
        int fila = 1;
        for (Zona zona : evento.getRecinto().getZonas()) {
            if (filtro != null && zona.getTipoZona() != filtro) {
                continue;
            }
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

            gridMapaAsientos.add(bloque, obtenerColumnaZona(zona.getTipoZona()), fila);
            fila++;
        }
    }

    private Label crearEtiqueta(String texto) {
        Label lbl = new Label(texto);
        lbl.getStyleClass().add("label-orientacion-mapa");
        return lbl;
    }

    private int obtenerColumnaZona(TipoZona tipoZona) {
        return switch (tipoZona) {
            case GENERAL -> 1;
            case VIP -> 2;
            case PREFERENCIAL -> 3;
        };
    }

    private void aplicarColorPorTipo(Label lbl, TipoZona tipo) {
        String color = switch (tipo) {
            case VIP -> "#fbc531";
            case PREFERENCIAL -> "#e84393";
            case GENERAL -> "#a29bfe";
        };
        lbl.setStyle("-fx-text-fill: " + color + ";");
    }

    private Button crearBotonAsiento(Asiento asiento, Zona zona) {
        Button btn = new Button(asiento.getIdAsiento());
        btn.getStyleClass().add("asiento");
        btn.getStyleClass().add(obtenerClaseZona(zona.getTipoZona()));
        btn.setTooltip(new Tooltip(asiento.getIdAsiento() + " - " + formatearMoneda(asiento.getPrecio())));

        boolean seleccionado = asientosSeleccionados.contains(asiento);
        if (seleccionado) {
            btn.getStyleClass().add("asiento-seleccionado");
        }

        boolean ocupado = asiento.getEstadoAsiento() == EstadoAsiento.BLOQUEADO
                || asiento.getEstadoAsiento() == EstadoAsiento.VENDIDO
                || (asiento.getEstadoAsiento() == EstadoAsiento.RESERVADO && !seleccionado);

        if (ocupado) {
            btn.getStyleClass().add("asiento-ocupado");
            btn.setDisable(true);
        } else {
            btn.setOnAction(e -> toggleSeleccion(asiento, btn));
        }
        return btn;
    }

    private String obtenerClaseZona(TipoZona tipoZona) {
        return switch (tipoZona) {
            case VIP -> "asiento-vip";
            case PREFERENCIAL -> "asiento-preferencial";
            case GENERAL -> "asiento-general";
        };
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
        if (vboxListaSeleccionados == null) {
            return;
        }
        vboxListaSeleccionados.getChildren().clear();
        if (asientosSeleccionados.isEmpty()) {
            lblNoSeleccion.setText("No has seleccionado asientos aun.");
            vboxListaSeleccionados.getChildren().add(lblNoSeleccion);
        } else {
            for (Asiento asiento : asientosSeleccionados) {
                Label item = new Label(asiento.getIdAsiento() + " - " + formatearMoneda(asiento.getPrecio()));
                item.getStyleClass().add("secondary-label");
                vboxListaSeleccionados.getChildren().add(item);
            }
        }

        int subtotal = asientosSeleccionados.stream().mapToInt(Asiento::getPrecio).sum();
        int totalServicios = obtenerServiciosSeleccionados().stream().mapToInt(TipoServicioAdicional::getPrecio).sum();
        lblSubtotal.setText(formatearMoneda(subtotal));
        lblServicios.setText(formatearMoneda(totalServicios));
        lblTotal.setText(formatearMoneda(subtotal + totalServicios));
    }

    private List<TipoServicioAdicional> obtenerServiciosSeleccionados() {
        List<TipoServicioAdicional> servicios = new ArrayList<>();
        agregarServicioSiSeleccionado(servicios, chkVip, TipoServicioAdicional.VIP);
        agregarServicioSiSeleccionado(servicios, chkSeguro, TipoServicioAdicional.SEGURO);
        agregarServicioSiSeleccionado(servicios, chkMerchandising, TipoServicioAdicional.MERCHANDISING);
        agregarServicioSiSeleccionado(servicios, chkParqueadero, TipoServicioAdicional.PARQUEADERO);
        agregarServicioSiSeleccionado(servicios, chkAccesoPreferencial, TipoServicioAdicional.ACCESO_PREFERENCIAL);
        return servicios;
    }

    private void agregarServicioSiSeleccionado(List<TipoServicioAdicional> servicios, CheckBox checkBox, TipoServicioAdicional servicio) {
        if (checkBox != null && checkBox.isSelected()) {
            servicios.add(servicio);
        }
    }

    @FXML
    void limpiarFiltroZona(ActionEvent event) {
        comboFiltroZona.getSelectionModel().clearSelection();
        generarMapaAsientos();
    }

    @FXML
    void procederAlPago(ActionEvent event) {
        if (asientosSeleccionados.isEmpty()) {
            mostrarAlerta("Selecciona al menos un asiento antes de continuar.");
            return;
        }
        Usuario usuario = controladorFacade.getUsuarioActual();
        if (!(usuario instanceof Cliente cliente)) {
            mostrarAlerta("Solo un cliente puede crear compras.");
            return;
        }
        if (cliente.getTipoPago() == TipoPago.NINGUNO) {
            mostrarAlerta("Agrega un metodo de pago valido desde tu perfil para continuar.");
            return;
        }

        try {
            if (compraActual == null) {
                compraActual = controladorFacade.crearCompra(evento, new ArrayList<>(asientosSeleccionados), obtenerServiciosSeleccionados());
            } else {
                controladorFacade.actualizarCompra(compraActual, new ArrayList<>(asientosSeleccionados), obtenerServiciosSeleccionados());
            }
            FXMLLoader loader = crearVista("/co/edu/uniquindio/poo/finalproject/ProcesarPagoView.fxml", "Pagar compra", event);
            if (loader != null) {
                ProcesarPagoViewController controller = loader.getController();
                controller.cargarResumen(compraActual);
            }
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
            generarMapaAsientos();
            actualizarResumen();
        }
    }

    @FXML
    void cancelarCompra(ActionEvent event) {
        if (compraActual == null) {
            regresar(event);
            return;
        }
        try {
            controladorFacade.cancelarCompra(compraActual);
            mostrarAlerta("Compra cancelada. Los asientos quedaron disponibles nuevamente.");
            crearVista("/co/edu/uniquindio/poo/finalproject/ClienteEventoView.fxml", "Eventos actuales", event);
        } catch (RuntimeException ex) {
            mostrarAlerta(ex.getMessage());
        }
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/ClienteEventoView.fxml",
                "Eventos actuales", event);
    }

    private void actualizarVisibilidadCancelacion() {
        if (btnCancelarCompra != null) {
            boolean visible = compraActual != null && compraActual.permiteModificacion();
            btnCancelarCompra.setVisible(visible);
            btnCancelarCompra.setManaged(visible);
        }
    }

    private String formatearMoneda(int valor) {
        return String.format("$%,d", valor).replace(",", ".");
    }
}
