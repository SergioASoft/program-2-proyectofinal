package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.ControladorUsuario;
import co.edu.uniquindio.poo.finalproject.model.Cliente;
import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.Usuario;
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
    @FXML
    public void initialize() {
        javafx.application.Platform.runLater(() -> {
            if (rootStackPane != null) rootStackPane.requestFocus();
        });
        cargarInformacionUsuario();
    }
    private void cargarInformacionUsuario(){
        Cliente usuario = (Cliente) ControladorUsuario.getInstance().getUsuarioActual();
        txtNombre.setText(usuario.getNombre());
        txtId.setText(usuario.getIdUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getNumero());
        txtRol.setText(usuario.getTipoUsuario().toString());
        comboMetodoPago.setValue(usuario.getTipoPago());
        comboMetodoPago.getItems().setAll(TipoPago.values());
    }
    public void actualizarUsuario(ActionEvent event) {
        String nuevoUsername = txtUsername.getText();
        String nuevaPass = txtPassword.getText();
        Cliente usuario = (Cliente) ControladorUsuario.getInstance().getUsuarioActual();
        if(nuevoUsername.isEmpty() || nuevaPass.isEmpty()){
            mostrarAlerta("Debe llenar los campos");
            return;
        }
        if(ControladorUsuario.getInstance().encontrarUsuario(nuevoUsername)){
            mostrarAlerta("El nombre de usuario ya existe, use otro");
            return;
        }
        boolean exito = ControladorUsuario.getInstance().actualizarUsuario(ControladorUsuario.getInstance().getUsuarioActual().getIdUsuario(), nuevoUsername, nuevaPass);
        if(exito){
            Usuario usuarioSesion = ControladorUsuario.getInstance().getUsuarioActual();
            usuarioSesion.setIdUsuario(nuevoUsername);
            usuarioSesion.setContrasena(nuevaPass);

            mostrarAlerta("Datos actualizados correctamente. Recuerda usar tu nuevo usuario para entrar.");
            txtUsername.clear();
            txtPassword.clear();
            cargarInformacionUsuario();
        } else {
            mostrarAlerta("Error al actualizar los datos.");
        }
    }
    public void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml","Menu Principal",event);
    }
}
