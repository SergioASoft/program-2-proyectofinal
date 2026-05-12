package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorUsuario;
import co.edu.uniquindio.poo.finalproject.model.factory.Cliente;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class PerfilViewController extends ViewController{
    @FXML private TextField txtNombre, txtId, txtCorreo, txtTelefono, txtRol;
    @FXML private TextField txtUsername, txtPassword;
    @FXML private ComboBox<TipoPago> comboMetodoPago;
    @FXML private StackPane rootStackPane;

    ControladorFacade controladorFacade = ControladorFacade.getInstance();
    @FXML
    public void initialize() {
        javafx.application.Platform.runLater(() -> {
            if (rootStackPane != null) rootStackPane.requestFocus();
        });
        cargarInformacionUsuario();
    }

    private void cargarInformacionUsuario() {
        Usuario usuario = controladorFacade.getUsuarioActual();

        txtNombre.setText(usuario.getNombre());
        txtId.setText(usuario.getIdUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getNumero());
        txtRol.setText(usuario.getTipoUsuario().toString());

        if (usuario instanceof Cliente cliente) {
            comboMetodoPago.setValue(cliente.getTipoPago());
            comboMetodoPago.getItems().setAll(TipoPago.values());
            comboMetodoPago.setDisable(false);
        } else {
            comboMetodoPago.setDisable(true);
        }
    }

    public void actualizarUsuario(ActionEvent event) {
        String nuevoUsername = txtUsername.getText();
        String nuevaPass     = txtPassword.getText();

        boolean actualizoMetodoPago = actualizarMetodoPago();
        boolean quiereActualizarCredenciales = !nuevoUsername.isEmpty() || !nuevaPass.isEmpty();

        if (!quiereActualizarCredenciales) {
            if (actualizoMetodoPago) {
                mostrarAlerta("Metodo de pago actualizado correctamente.");
                cargarInformacionUsuario();
            } else {
                mostrarAlerta("No hay cambios para actualizar.");
            }
            return;
        }

        if (nuevoUsername.isEmpty() || nuevaPass.isEmpty()) {
            mostrarAlerta("Debe llenar usuario y contrasena para actualizar credenciales");
            return;
        }
        if (controladorFacade.existeUsuario(nuevoUsername)) {
            mostrarAlerta("El nombre de usuario ya existe, use otro");
            return;
        }

        String idActual = controladorFacade.getUsuarioActual().getIdUsuario();
        boolean exito = controladorFacade.actualizarCredenciales(idActual, nuevoUsername, nuevaPass);

        if (exito) {
            mostrarAlerta("Datos actualizados correctamente. Recuerda tu nuevo usuario.");
            txtUsername.clear();
            txtPassword.clear();
            cargarInformacionUsuario();
        } else {
            mostrarAlerta("Error al actualizar los datos.");
        }
    }

    private boolean actualizarMetodoPago() {
        Usuario usuario = controladorFacade.getUsuarioActual();
        if (usuario instanceof Cliente cliente && comboMetodoPago.getValue() != null) {
            cliente.setTipoPago(comboMetodoPago.getValue());
            return true;
        }
        return false;
    }

    public void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml",
                "Menu Principal", event);
    }
}
