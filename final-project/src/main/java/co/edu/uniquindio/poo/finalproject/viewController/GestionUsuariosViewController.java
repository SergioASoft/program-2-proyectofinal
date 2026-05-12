package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.factory.UsuarioFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestionUsuariosViewController extends ViewController {

    @FXML private TextField txtId, txtNombre, txtCorreo, txtTelefono, txtContrasena, txtBusqueda;
    @FXML private ComboBox<TipoUsuario> comboTipoUsuario;
    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colId, colNombre, colCorreo, colTelefono, colTipo;

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();
    private final UsuarioFactory usuarioFactory = new UsuarioFactory();
    private Usuario usuarioSeleccionado;

    @FXML
    public void initialize() {
        comboTipoUsuario.getItems().setAll(TipoUsuario.values());

        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdUsuario()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colCorreo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumero()));
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipoUsuario().toString()));

        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> cargarUsuario(newValue));
        txtBusqueda.textProperty().addListener((obs, oldValue, newValue) -> cargarUsuarios());
        cargarUsuarios();
    }

    @FXML
    void guardarUsuario(ActionEvent event) {
        if (formularioIncompleto()) {
            mostrarAlerta("Complete todos los datos del usuario");
            return;
        }

        if (usuarioSeleccionado == null) {
            if (controladorFacade.existeUsuario(txtId.getText())) {
                mostrarAlerta("Ya existe un usuario con ese ID");
                return;
            }
            Usuario usuario = usuarioFactory.GetUsuario(
                    txtId.getText(),
                    txtNombre.getText(),
                    txtContrasena.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText(),
                    comboTipoUsuario.getValue()
            );
            controladorFacade.registrarUsuario(usuario);
        } else {
            if (!usuarioSeleccionado.getIdUsuario().equals(txtId.getText()) && controladorFacade.existeUsuario(txtId.getText())) {
                mostrarAlerta("Ya existe un usuario con ese ID");
                return;
            }
            controladorFacade.actualizarUsuario(
                    usuarioSeleccionado.getIdUsuario(),
                    txtId.getText(),
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText(),
                    txtContrasena.getText()
            );
        }

        limpiarFormulario(event);
        cargarUsuarios();
    }

    @FXML
    void eliminarUsuario(ActionEvent event) {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un usuario para eliminar");
            return;
        }
        if (!controladorFacade.eliminarUsuario(seleccionado)) {
            mostrarAlerta("No se puede eliminar el usuario actual");
            return;
        }
        limpiarFormulario(event);
        cargarUsuarios();
    }

    @FXML
    void limpiarFormulario(ActionEvent event) {
        usuarioSeleccionado = null;
        txtId.clear();
        txtNombre.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtContrasena.clear();
        comboTipoUsuario.setValue(null);
        comboTipoUsuario.setDisable(false);
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml", "Menu Principal", event);
    }

    private void cargarUsuarios() {
        String busqueda = txtBusqueda == null || txtBusqueda.getText() == null ? "" : txtBusqueda.getText().toLowerCase();
        tablaUsuarios.getItems().setAll(controladorFacade.getTodosLosUsuarios().stream()
                .filter(usuario -> busqueda.isBlank()
                        || usuario.getIdUsuario().toLowerCase().contains(busqueda)
                        || usuario.getNombre().toLowerCase().contains(busqueda)
                        || usuario.getCorreo().toLowerCase().contains(busqueda))
                .toList());
    }

    private void cargarUsuario(Usuario usuario) {
        if (usuario == null) {
            return;
        }
        usuarioSeleccionado = usuario;
        txtId.setText(usuario.getIdUsuario());
        txtNombre.setText(usuario.getNombre());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getNumero());
        txtContrasena.setText(usuario.getContrasena());
        comboTipoUsuario.setValue(usuario.getTipoUsuario());
        comboTipoUsuario.setDisable(true);
    }

    private boolean formularioIncompleto() {
        return txtId.getText().isBlank()
                || txtNombre.getText().isBlank()
                || txtCorreo.getText().isBlank()
                || txtTelefono.getText().isBlank()
                || txtContrasena.getText().isBlank()
                || comboTipoUsuario.getValue() == null;
    }
}