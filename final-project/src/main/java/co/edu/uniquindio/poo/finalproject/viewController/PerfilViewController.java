package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.ControladorUsuario;
import co.edu.uniquindio.poo.finalproject.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PerfilViewController extends ViewController{
    @FXML private TextField txtNombre, txtId, txtCorreo, txtTelefono, txtRol;
    @FXML private TextField txtUsername, txtPassword;
    @FXML
    public void initialize() {
        cargarInformacionUsuario();
    }
    private void cargarInformacionUsuario(){
        Usuario usuario = ControladorUsuario.getInstance().getUsuarioActual();
        txtNombre.setText(usuario.getNombre());
        txtId.setText(usuario.getIdUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtTelefono.setText(usuario.getNumero());
        txtRol.setText(usuario.getTipoUsuario().toString());
    }
    public void actualizarUsuario(ActionEvent event) {
        String nuevoUsername = txtUsername.getText();
        String nuevaPass = txtPassword.getText();

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
