package co.edu.uniquindio.poo.finalproject.viewController;
import co.edu.uniquindio.poo.finalproject.controller.ControladorUsuario;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;
import co.edu.uniquindio.poo.finalproject.model.Usuario;
import co.edu.uniquindio.poo.finalproject.model.UsuarioFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class LoginViewController extends ViewController {
    @FXML private TextField txtUser;
    @FXML private PasswordField txtPassword;

    @FXML private TextField txtNombre, txtId, txtCorreo, txtTelefono;
    @FXML private PasswordField txtPasswordRegistro;
    @FXML private ComboBox<TipoUsuario> comboTipo;

    private final UsuarioFactory factory = new UsuarioFactory();

    @FXML
    public void initialize() {
        if (comboTipo != null) {
            comboTipo.getItems().setAll(TipoUsuario.values());
        }
    }
    @FXML
    private void iniciarSesion(ActionEvent event) {
        if(txtUser.getText().isEmpty() || txtPassword.getText().isEmpty()){
            mostrarAlerta("Rellene todos los campos");
            return;
        }
        String id = txtUser.getText();
        String pass = txtPassword.getText();
        Usuario usuarioGuardado = ControladorUsuario.getInstance().obtenerUsuario(id, pass);
        if (usuarioGuardado != null) {
            ControladorUsuario.getInstance().setUsuarioActual(usuarioGuardado);
            crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml","Menu Principal",event);
        } else {
            mostrarAlerta("No se encuentra ningun usuario creado en el sistema");
        }
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {
        if (txtId.getText().isEmpty() || txtCorreo.getText().isEmpty() || txtCorreo.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() || txtPasswordRegistro.getText().isEmpty() || comboTipo.getValue() == null){
            mostrarAlerta("Rellene los campos para terminar el registro");
            return;
        }
        if(ControladorUsuario.getInstance().encontrarUsuario(txtId.getText())){
            mostrarAlerta("El usuario ya existe");
            return;
        }
        Usuario usuario = factory.GetUsuario(
                txtId.getText(), txtNombre.getText(), txtPasswordRegistro.getText(),
                txtCorreo.getText(), txtTelefono.getText(), comboTipo.getValue()
        );
        ControladorUsuario.getInstance().registrarUsuario(usuario);
        cargarMenuInicioSesion(event);
    }

    @FXML
    private void cargarMenuRegistro(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/RegistroView.fxml","Registro",event);
    }

    @FXML
    private void cargarMenuInicioSesion(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/LoginView.fxml","Iniciar sesion",event);
    }


}
