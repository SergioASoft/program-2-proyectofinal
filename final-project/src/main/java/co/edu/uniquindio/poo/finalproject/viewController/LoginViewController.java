package co.edu.uniquindio.poo.finalproject.viewController;
import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorUsuario;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.factory.UsuarioFactory;
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

    ControladorFacade controladorFacade = ControladorFacade.getInstance();

    @FXML
    public void initialize() {
        if (comboTipo != null) {
            comboTipo.getItems().setAll(TipoUsuario.values());
        }
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        if (txtUser.getText().isEmpty() || txtPassword.getText().isEmpty()) {
            mostrarAlerta("Rellene todos los campos");
            return;
        }
        Usuario usuario = controladorFacade.iniciarSesion(txtUser.getText(), txtPassword.getText());
        if (usuario != null) {
            crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml",
                    "Menu Principal", event);
        } else {
            mostrarAlerta("No se encuentra ningún usuario creado en el sistema");
        }
    }

    @FXML
    private void registrarUsuario(ActionEvent event) {
        if (txtId.getText().isEmpty() || txtNombre.getText().isEmpty()
                || txtCorreo.getText().isEmpty() || txtTelefono.getText().isEmpty()
                || txtPasswordRegistro.getText().isEmpty() || comboTipo.getValue() == null) {
            mostrarAlerta("Rellene los campos para terminar el registro");
            return;
        }
        if (controladorFacade.existeUsuario(txtId.getText())) {
            mostrarAlerta("El usuario ya existe");
            return;
        }
        Usuario usuario = factory.GetUsuario(
                txtId.getText(), txtNombre.getText(), txtPasswordRegistro.getText(),
                txtCorreo.getText(), txtTelefono.getText(), comboTipo.getValue()
        );
        controladorFacade.registrarUsuario(usuario);
        cargarMenuInicioSesion(event);
    }

    @FXML
    private void cargarMenuRegistro(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/RegistroView.fxml", "Registro", event);
    }

    @FXML
    private void cargarMenuInicioSesion(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/LoginView.fxml", "Iniciar sesion", event);
    }
}
