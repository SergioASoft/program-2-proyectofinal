package co.edu.uniquindio.poo.finalproject.viewController;
import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorUsuario;
import co.edu.uniquindio.poo.finalproject.model.factory.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PlataformaViewController extends ViewController{
    @FXML private VBox adminPanel;
    @FXML private VBox clientPanel;
    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        inicializarPlataforma();
    }

    public void inicializarPlataforma() {
        if (ControladorUsuario.getInstance().getUsuarioActual() instanceof Admin) {
            adminPanel.setVisible(true);
            adminPanel.setManaged(true);
            clientPanel.setVisible(false);
            clientPanel.setManaged(false);
        } else {
            clientPanel.setVisible(true);
            clientPanel.setManaged(true);
            adminPanel.setVisible(false);
            adminPanel.setManaged(false);
        }
    }
    @FXML
    public void gestionarPerfil(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PerfilView.fxml","Perfil del usuario",event);
    }
    @FXML
    public void showExplorarEventos(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/ClienteEventoView.fxml","Eventos actuales",event);
    }

    @FXML
    private void showMetricas() {
        System.out.println("Cargando");
    }


    @FXML
    public void showMisCompras(ActionEvent event) {
    }
    @FXML
    public void showGestionUsuarios(ActionEvent event) {
    }
    @FXML
    public void gestionarEventos(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/AdminEventoView.fxml","Gestion de eventos",event);
    }
    @FXML
    public void showGestionRecintos(ActionEvent event) {
    }
    @FXML
    public void showIncidencias(ActionEvent event) {
    }
    @FXML
    public void cerrarSesion(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/LoginView.fxml","Iniciar sesion",event);
    }
}
