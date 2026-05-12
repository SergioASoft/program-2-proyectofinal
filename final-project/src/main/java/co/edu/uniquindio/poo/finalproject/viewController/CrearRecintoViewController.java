package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.EstadoAsiento;
import co.edu.uniquindio.poo.finalproject.model.Recinto;
import co.edu.uniquindio.poo.finalproject.model.TipoZona;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class CrearRecintoViewController extends ViewController {

    @FXML private TextField txtIdRecinto, txtNombre, txtCiudad, txtDireccion, txtBusquedaRecinto;
    @FXML private TextField txtNombreZona, txtCapacidadZona, txtPrecioZona;
    @FXML private ComboBox<TipoZona> comboTipoZona;
    @FXML private TableView<Recinto> tablaRecintos;
    @FXML private TableColumn<Recinto, String> colIdRecinto, colNombreRecinto, colCiudadRecinto;
    @FXML private TableView<Zona> tablaZonas;
    @FXML private TableColumn<Zona, String> colNombreZona, colTipoZona;
    @FXML private TableColumn<Zona, Integer> colCapacidadZona, colPrecioZona;
    @FXML private Button btnFinalizar; // Asegúrate de ponerle fx:id="btnFinalizar" en el FXML

    private final ObservableList<Recinto> listaRecintosUI = FXCollections.observableArrayList();
    private final ObservableList<Zona> listaZonas = FXCollections.observableArrayList();
    private Zona zonaSeleccionada;
    private Recinto recintoSeleccionado; // RASTREADOR DE EDICIÓN

    ControladorFacade controladorFacade = ControladorFacade.getInstance();

    @FXML
    public void initialize() {
        comboTipoZona.getItems().setAll(TipoZona.values());
        configurarFiltroNumerico(txtCapacidadZona);
        configurarFiltroNumerico(txtPrecioZona);

        colIdRecinto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdRecinto()));
        colNombreRecinto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCiudadRecinto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCiudad()));
        tablaRecintos.setItems(listaRecintosUI);

        colNombreZona.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdZona()));
        colCapacidadZona.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCapacidad()));
        colPrecioZona.setCellValueFactory(cellData -> new SimpleObjectProperty<>(obtenerPrecioZona(cellData.getValue())));
        colTipoZona.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoZona().toString()));
        tablaZonas.setItems(listaZonas);

        txtBusquedaRecinto.textProperty().addListener((obs, oldValue, newValue) -> actualizarListaRecintos());
        actualizarListaRecintos();

        tablaRecintos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                recintoSeleccionado = newSelection;
                cargarDetallesRecinto(newSelection);
            }
        });
        tablaZonas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> cargarZonaSeleccionada(newSelection));
    }

    private void cargarDetallesRecinto(Recinto recinto) {
        txtIdRecinto.setText(recinto.getIdRecinto().replace("REC-", ""));
        txtIdRecinto.setEditable(false);
        txtNombre.setText(recinto.getNombre());
        txtCiudad.setText(recinto.getCiudad());
        txtDireccion.setText(recinto.getDireccion());
        listaZonas.setAll(new ArrayList<>(recinto.getZonas()));
        if(btnFinalizar != null) btnFinalizar.setText("ACTUALIZAR RECINTO");
    }

    @FXML
    void agregarRecinto(ActionEvent event) {
        if (txtIdRecinto.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtCiudad.getText().isEmpty() || txtDireccion.getText().isEmpty()) {
            mostrarAlerta("Llene los datos del recinto");
            return;
        }
        if (listaZonas.size() < 3) {
            mostrarAlerta("Debe crear las 3 zonas (General, Preferencial y VIP)");
            return;
        }

        if (recintoSeleccionado != null) {
            controladorFacade.eliminarRecinto(recintoSeleccionado);
            recintoSeleccionado = null;
        }
        Recinto nuevo = new Recinto("REC-" + txtIdRecinto.getText(), txtNombre.getText(),
                txtCiudad.getText(), txtDireccion.getText(), new ArrayList<>(listaZonas));
        controladorFacade.registrarRecinto(nuevo);
        actualizarListaRecintos();
        limpiarFormulario();
    }

    @FXML
    void agregarZona(ActionEvent event) {
        if (validarCamposZona()) {
            int capacidad = Integer.parseInt(txtCapacidadZona.getText());
            int precio = Integer.parseInt(txtPrecioZona.getText());
            TipoZona tipo = comboTipoZona.getValue();

            if (listaZonas.stream().anyMatch(z -> z != zonaSeleccionada && z.getTipoZona() == tipo)) {
                mostrarAlerta("Ya existe una zona de tipo " + tipo);
                return;
            }

            List<Asiento> asientos = new ArrayList<>();
            String prefijo = tipo.toString().substring(0, 1);
            for (int i = 1; i <= capacidad; i++) {
                asientos.add(new Asiento(prefijo + i, 1, i, EstadoAsiento.DISPONIBLE, precio));
            }

            Zona nuevaZona = new Zona.Builder(txtNombreZona.getText(), tipo, capacidad, asientos)
                    .reglas(tipo == TipoZona.VIP ? "Acceso exclusivo VIP" : "Sin restricciones")
                    .build();

            if (zonaSeleccionada != null) {
                listaZonas.remove(zonaSeleccionada);
                zonaSeleccionada = null;
            }
            listaZonas.add(nuevaZona);
            limpiarCamposZona();
        }
    }

    @FXML
    void limpiarAlClickAfuera(MouseEvent event) {
    }


    @FXML
    void eliminarRecinto(ActionEvent event) {
        Recinto seleccionado = tablaRecintos.getSelectionModel().getSelectedItem();
        if(seleccionado != null) {
            controladorFacade.eliminarRecinto(seleccionado);
            actualizarListaRecintos();
            limpiarFormulario();
        }
    }

    @FXML
    void eliminarZona(ActionEvent event) {
        Zona seleccionada = tablaZonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            listaZonas.remove(seleccionada);
            limpiarCamposZona();
        }
    }

    private void actualizarListaRecintos() {
        String busqueda = txtBusquedaRecinto == null || txtBusquedaRecinto.getText() == null ? "" : txtBusquedaRecinto.getText().toLowerCase();
        listaRecintosUI.setAll(controladorFacade.getTodosLosRecintos().stream()
                .filter(recinto -> busqueda.isBlank()
                        || recinto.getIdRecinto().toLowerCase().contains(busqueda)
                        || recinto.getNombre().toLowerCase().contains(busqueda)
                        || recinto.getCiudad().toLowerCase().contains(busqueda))
                .toList());
    }

    private void limpiarCamposZona() {
        txtNombreZona.clear();
        txtCapacidadZona.clear();
        txtPrecioZona.clear();
        comboTipoZona.setValue(null);
        zonaSeleccionada = null;
        tablaZonas.getSelectionModel().clearSelection();
    }

    void limpiarFormulario() {
        txtIdRecinto.clear();
        txtIdRecinto.setEditable(true);
        txtNombre.clear();
        txtCiudad.clear();
        txtDireccion.clear();
        limpiarCamposZona();
        listaZonas.clear();
        recintoSeleccionado = null;
        tablaRecintos.getSelectionModel().clearSelection();
        if(btnFinalizar != null) btnFinalizar.setText("FINALIZAR Y CREAR RECINTO");
    }

    private void configurarFiltroNumerico(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private boolean validarCamposZona() {
        return !txtNombreZona.getText().isEmpty() && comboTipoZona.getValue() != null &&
                !txtCapacidadZona.getText().isEmpty() && !txtPrecioZona.getText().isEmpty();
    }

    private void cargarZonaSeleccionada(Zona zona) {
        if (zona == null) {
            return;
        }
        zonaSeleccionada = zona;
        txtNombreZona.setText(zona.getIdZona());
        comboTipoZona.setValue(zona.getTipoZona());
        txtCapacidadZona.setText(String.valueOf(zona.getCapacidad()));
        txtPrecioZona.setText(String.valueOf(obtenerPrecioZona(zona)));
    }

    private int obtenerPrecioZona(Zona zona) {
        if (zona.getAsientos() == null || zona.getAsientos().isEmpty()) {
            return 0;
        }
        return zona.getAsientos().getFirst().getPrecio();
    }

    @FXML void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/AdminEventoView.fxml", "Gestión de Eventos", event);
    }
}
